package me.zeepic.aiparkour.commands

import me.zeepic.aiparkour.OfflineManager
import me.zeepic.aiparkour.anyNull
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.messaging.toEntityType
import me.zeepic.aiparkour.messaging.toMaterial
import me.zeepic.aiparkour.milliseconds
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
import java.util.Calendar
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

typealias KCommand = KFunction<CommandResult>

class FunctionCommand(name: String, val function: KCommand, description: String = "", usage: String = "", aliases: List<String> = listOf()) : BukkitCommand(name, description, usage, aliases) {

    private fun getArgError(index: Int, badArg: String, requirement: String, note: String = "")
        = "&cIncorrect usage! &7&oFor argument #${index + 1} you provided \"$badArg\", but $requirement was required. $note"

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        val argStack = args?.toMutableList() ?: mutableListOf()
        val params = function.parameters.associateWith {
            parseParameter(it, sender, argStack, commandLabel, args) {
                TODO("When the parameter is varargs, optional, nullable, etc, this needs to have different responses.")
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
        }
    }

    private data class ParseResult<R>(val success: Boolean, val value: R? = null, val message: String = "")

    private fun parseParameter(
        parameter: KParameter,
        sender: CommandSender,
        argStack: Stack<String>,
        commandLabel: String,
        args: Array<out String>?,
        callback: (ParseResult<*>) -> Unit
    ) {
        if (parameter.type == OfflinePlayer::class) {
            parseOfflinePlayer(argStack.pop(), callback)
            return
        }
        callback(when (parameter.type) {
            CommandSender::class -> ParseResult(true, sender)
            ConsoleCommandSender::class -> parseCommandSender(sender)
            Player::class -> parsePlayer(parameter, sender, argStack.pop())
            EntityType::class -> parseEntityType(parameter, argStack.pop())
            Material::class -> parseMaterial(parameter, argStack.pop())
            Int::class -> parseInt(parameter, argStack.pop())
            Long::class -> parseLong(parameter, argStack.pop())
            Byte::class -> parseByte(parameter, argStack.pop())
            Double::class -> parseDouble(parameter, argStack.pop())
            Boolean::class -> parseBoolean(parameter, argStack.pop())
            Duration::class -> parseDuration(parameter, argStack.pop())
            Date::class -> parseDate(parameter, argStack.pop())
            Component::class -> ParseResult(true, argStack.pop().component)
            String::class -> ParseResult(true, argStack.pop())
            else -> {
                sender.send("&cDeveloper note: an error occurred while generating the command, because an unknown class was used as an argument in the command's definition.")
                throw Exception("When ${sender.name} used the command \"/${commandLabel} ${args?.joinToString(" ")}\", an error occurred while generating the command, because an unknown class was used in the function definition.")
            }
        })
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

}

object CommandParser {

    val aliases = mutableListOf<String>()

    fun isCommand(command: KCommand)
        = command.name.endsWith("Command") && command.annotations.any { it is Command }

    fun convertToBukkit(command: KCommand): FunctionCommand {
        val annotation = command.annotations.first { it is Command } as Command
        return FunctionCommand(
            annotation.name,
            command,
            annotation.description,
            command.parameters.joinToString("> <") { it.name ?: it.type.javaClass.simpleName.split(".").last() },
            annotation.aliases.toList()
        )
    }
}