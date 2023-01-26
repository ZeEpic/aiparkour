package me.zeepic.aiparkour.commands

import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.messaging.log
import me.zeepic.aiparkour.util.anyNull
import org.bukkit.Server
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.kotlinFunction

typealias KCommand = KFunction<CommandResult>

object CommandParser {

    private val aliases = mutableListOf<String>()
    private val argumentParsers = mutableListOf<ArgumentParser<*>>()

    fun registerArgumentParser(parser: Class<out ArgumentParser<*>>) {
        argumentParsers.add(parser.getConstructor().newInstance())
    }

    private fun isCommand(command: KCommand)
        = command.name.endsWith("Command") && command.annotations.any { it is Command }

    private fun getParameterUsage(parameter: KParameter): String {
        if (parameter.isOptional || parameter.type.isMarkedNullable) {
            return "[${parameter.name}]"
        }
        return "<${parameter.name}>"
    }

    private fun convertToBukkit(command: KCommand, group: KClass<out Any>): FunctionCommand {
        val annotation = command.annotations.first { it is Command } as Command
        val groupAnnotation = group.annotations.firstOrNull { it is CommandGroup } as? CommandGroup ?: CommandGroup("")
        val parameters = command.parameters.drop(2).joinToString(" ", transform = ::getParameterUsage)
        val usage = "/${annotation.name}${if (parameters.isNotEmpty()) " " else ""}$parameters"
        val parsers = command.parameters.drop(1).associateWith { argumentParsers.firstOrNull { parser -> parser.type == it.type } }
        if (parsers.values.anyNull()) {
            throw Exception("An error occurred while generating the command, because a class without a valid parser was used in the function definition.")
        }
        return FunctionCommand(
            annotation.name,
            command,
            parsers.mapValues { it.value!! },
            AIParkour.shortName + "." + groupAnnotation.permission,
            annotation.description,
            usage,
            annotation.aliases.toList()
        )
    }

    fun generateCommandMap(commandFunctions: Map<KClass<out Any>, List<Method>>, server: Server) {
        log("Generating command map...")
        commandFunctions
            .asSequence()
            .map { (group, methods) -> group to methods.map(Method::kotlinFunction) }
            .map { (group, functions) -> group to functions.filterIsInstance<KCommand>().filter(CommandParser::isCommand) }
            .map { (group, functions) -> functions.map { convertToBukkit(it, group) } }
            .flatten()
            .filterNot(BukkitCommand::isRegistered)
            .toList()
            .forEach {
                val senderParameter = it.function.parameters.getOrNull(1)
                if (senderParameter?.type?.isSubtypeOf(CommandSender::class.starProjectedType) != true) {
                    log("The command \"/${it.name}\" is not a valid command. It must have a CommandSender or Player as its first parameter.")
                    return@forEach
                }
                aliases += it.name
                aliases.addAll(it.aliases)
                val fallbackPrefix = AIParkour.shortName
                server.commandMap.register(fallbackPrefix, it)
                log("Registered command \"/$fallbackPrefix:${it.usage.drop(1)}\" with permission \"${it.permission}\".")
            }
    }

    fun getArgError(index: Int, badArg: String, requirement: String, note: String = "")
            = "&cIncorrect usage! &7&oFor argument #${index + 1} you provided \"$badArg\", but $requirement was required. $note"

}
