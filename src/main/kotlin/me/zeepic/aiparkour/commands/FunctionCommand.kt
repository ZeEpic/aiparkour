package me.zeepic.aiparkour.commands

import me.zeepic.aiparkour.players.OfflineManager
import me.zeepic.aiparkour.util.anyNull
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.messaging.toEntityType
import me.zeepic.aiparkour.messaging.toMaterial
import me.zeepic.aiparkour.util.milliseconds
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*
import kotlin.reflect.KParameter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class FunctionCommand(name: String, val function: KCommand, permission: String = "", description: String = "", usage: String = "", aliases: List<String> = listOf()) : BukkitCommand(name, description, usage, aliases) {

    init {
        this.permission = permission
    }

    private fun getArgError(index: Int, badArg: String, requirement: String, note: String = "")
        = "&cIncorrect usage! &7&oFor argument #${index + 1} you provided \"$badArg\", but $requirement was required. $note"

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        if (!testPermission(sender)) {
            sender.send("&cYou do not have permission to use this command!")
            return true
        }

        var argStack = args?.toMutableList() ?: mutableListOf()
        val argList = args?.toList() ?: listOf()
        val params = mutableMapOf<KParameter, Any?>()
        function.parameters.forEach { parameter ->
            val arg = argStack.firstOrNull()
            if (arg == null) {
                sender.send("&cIncorrect usage! &7&oYou did not provide enough arguments.")
                return false
            }
            parseParameter(parameter, sender, arg, commandLabel, argList) {
                if (it.success) {
                    argStack.removeAt(0)
                }
                params[parameter] = if (parameter.isVararg) {
                    if (it.success) {
                        val varargList = mutableListOf(it.value)
                        var lastIndex = 0
                        for (nextArg in argStack) {
                            val parsed = parseParameter(parameter, sender, nextArg, commandLabel, argList)
                            // Offline player has a special case where it will return null since it requires a callback
                            val success = parsed?.success ?: (parameter.type == OfflinePlayer::class)
                            if (!success) break // When the first argument encountered is not part of the vararg,
                                                // lastIndex will be 0, and 0 arguments will be removed
                            lastIndex++
                            varargList += parsed?.value
                        }
                        argStack = argStack.drop(lastIndex).toMutableList()
                        varargList.toTypedArray()
                    } else return@parseParameter
                } else {
                    handleOptionalResult(it, parameter, sender) ?: return@parseParameter
                }
            }
        }

        return when (function.callBy(params)) {
            CommandResult.SUCCESS -> true
            CommandResult.FAILURE -> false
            CommandResult.INVALID_ARGS -> {
                sender.send("&cInvalid arguments!")
                true
            }
            CommandResult.NO_PERMISSION -> {
                sender.send("&cYou do not have permission to execute this command.")
                true
            }
            CommandResult.SILENT_FAILURE -> true
        }
    }

    private data class ParseResult<R>(val success: Boolean, val value: R? = null, val message: String = "")

    private fun parseParameter(
        parameter: KParameter,
        sender: CommandSender,
        arg: String,
        commandLabel: String,
        commandArgs: List<String>,
        callback: (ParseResult<*>) -> Unit = {}
    ): ParseResult<*>? {
        if (parameter.type == OfflinePlayer::class) {
            parseOfflinePlayer(arg, callback)
            return null
        }
        val value = when (parameter.type) {
            CommandSender::class -> ParseResult(true, value = sender)
            ConsoleCommandSender::class -> parseCommandSender(sender)
            Player::class -> parsePlayer(parameter, sender, arg)
            EntityType::class -> parseEntityType(parameter, arg)
            Material::class -> parseMaterial(parameter, arg)
            Int::class -> parseInt(parameter, arg)
            Long::class -> parseLong(parameter, arg)
            Byte::class -> parseByte(parameter, arg)
            Double::class -> parseDouble(parameter, arg)
            Boolean::class -> parseBoolean(parameter, arg)
            Duration::class -> parseDuration(parameter, arg)
            Date::class -> parseDate(parameter, arg)
            Component::class -> ParseResult(success = true, value = arg.component)
            String::class -> ParseResult(success = true, value = arg)
            else -> {
                sender.send("&cDeveloper note: an error occurred while generating the command, because an unknown class was used as an argument in the command's definition.")
                throw Exception("When ${sender.name} used the command \"/${commandLabel} ${commandArgs.joinToString(" ")}\", an error occurred while generating the command, because an unknown class was used in the function definition.")
            }
        }
        callback(value)
        return value
    }

    private fun parseDate(parameter: KParameter, time: String): ParseResult<Date> {
        // mm/dd/yyyy or mm-dd-yyyy
        val regex = Regex("(\\d{1,2})[/-](\\d{1,2})[/-](\\d{4})")
        if (!regex.matches(time)) {
            return ParseResult(false, message = getArgError(
                parameter.index,
                time,
                "a date",
                "Use mm/dd/yyyy or mm-dd-yyyy."
            ))
        }
        val groups = regex.find(time)!!.groupValues.map { date -> date.toIntOrNull() }
        if (groups.anyNull() || groups.size != 3) {
            return ParseResult(false, message = getArgError(
                parameter.index,
                time,
                "a date",
                "Use either mm/dd/yyyy or mm-dd-yyyy."
            ))
        }
        val calendar = Calendar.getInstance()
        calendar.set(groups[2]!!, groups[0]!!, groups[1]!!)
        return ParseResult(true, calendar.time)
    }

    private fun parseDuration(parameter: KParameter, time: String): ParseResult<Duration> {
        // "forever", "20d", "2h", "40s", etc
        val long = time.milliseconds()
        if (long == Long.MAX_VALUE && time != "forever") {
            return ParseResult(false, message = getArgError(
                parameter.index,
                time,
                "an amount of time",
                "Use \"forever\" or a number followed by \"d\" for days, \"h\" for hours, \"m\" for minutes, or \"s\" for seconds (like 30m for 30 minutes)."
            ))
        }
        return ParseResult(true, long.milliseconds)
    }

    private fun parseBoolean(parameter: KParameter, arg: String): ParseResult<Boolean> {
        return when (arg) {
            "true", "yes" -> ParseResult(success = true, value = true)
            "false", "no" -> ParseResult(success = true, value = false)
            else -> ParseResult(success = false, message = getArgError(parameter.index, arg, "yes/no"))
        }
    }

    private fun parseInt(parameter: KParameter, arg: String): ParseResult<Int> {
        val number = arg.toIntOrNull()
            ?: return ParseResult(success = false, message = getArgError(parameter.index, arg, "a whole number"))
        return ParseResult(success = true, value = number)
    }

    private fun parseLong(parameter: KParameter, arg: String): ParseResult<Long> {
        val number = arg.toLongOrNull()
            ?: return ParseResult(success = false, message = getArgError(parameter.index, arg, "a whole number"))
        return ParseResult(success = true, value = number)
    }

    private fun parseDouble(parameter: KParameter, arg: String): ParseResult<Double> {
        val number = arg.toDoubleOrNull()
            ?: return ParseResult(success = false, message = getArgError(parameter.index, arg, "a decimal or whole number"))
        return ParseResult(success = true, value = number)
    }

    private fun parseByte(parameter: KParameter, arg: String): ParseResult<Byte> {
        val number = arg.toByteOrNull()
            ?: return ParseResult(success = false, message = getArgError(parameter.index, arg, "a whole number between 0 and 255"))
        return ParseResult(success = true, value = number)
    }

    private fun parseMaterial(parameter: KParameter, material: String): ParseResult<Material> {
        val mat = material.toMaterial()
            ?: return ParseResult(success = false, message = getArgError(parameter.index, material, "an item/block", "Use underscores for blocks like grass_block."))
        return ParseResult(success = true, value = mat)
    }

    private fun parseEntityType(parameter: KParameter, entityType: String): ParseResult<EntityType> {
        val type = entityType.toEntityType()
            ?: return ParseResult(success = false, message = getArgError(parameter.index, entityType, "an entity type", "Use underscores for mobs like elder_guardian."))
        return ParseResult(success = true, value = type)
    }

    private fun parseCommandSender(sender: CommandSender): ParseResult<ConsoleCommandSender> {
        if (sender !is ConsoleCommandSender) {
            return ParseResult(success = false, message = "&cYou must be the server console to use this command!")
        }
        return ParseResult(success = true, value = sender)
    }

    private fun parsePlayer(parameter: KParameter, sender: CommandSender, arg: String): ParseResult<Player> {
        if (parameter.index == 0) {
            if (sender !is Player) {
                return ParseResult(success = false, message = "&cYou must be a player to use this command!")
            }
            return ParseResult(success = true, value = sender)
        }
        val player = Bukkit.getPlayer(arg)
            ?: return ParseResult(success = false, message = "&c$arg isn't on the server right now.")
        return ParseResult(success = true, value = player)
    }

    private fun parseOfflinePlayer(arg: String, callback: (ParseResult<OfflinePlayer>) -> Unit) {
        OfflineManager.getOfflinePlayer(arg) {
            if (it == null) {
                callback(ParseResult(success = false, message = "&c$arg hasn't played on this server yet."))
            } else {
                callback(ParseResult(success = true, value = it))
            }
        }
    }

    private fun handleOptionalResult(result: ParseResult<*>, parameter: KParameter, sender: CommandSender): Any? {
        if (result.success) {
            return result.value
        }
        if (parameter.isOptional) {
            return null
        }
        if (parameter.type.isMarkedNullable) {
            return null
        }
        sender.send(result.message)
        return null
    }

}
