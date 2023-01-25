package me.zeepic.aiparkour.commands

import me.zeepic.aiparkour.messaging.log
import org.bukkit.Server
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

typealias KCommand = KFunction<CommandResult>

object CommandParser {

    private val aliases = mutableListOf<String>()

    fun isCommand(command: KCommand)
        = command.name.endsWith("Command") && command.annotations.any { it is Command } && command.returnType == CommandResult::class

    private fun getParameterUsage(parameter: KParameter): String {
        if (parameter.isOptional || parameter.type.isMarkedNullable) {
            return "[${parameter.name}]"
        }
        return "<${parameter.name}>"

    }
    private fun convertToBukkit(command: KCommand): FunctionCommand {
        val annotation = command.annotations.first { it is Command } as Command
        val usage = "/${annotation.name} " + command.parameters.joinToString(" ", transform = ::getParameterUsage)
        return FunctionCommand(
            annotation.name,
            command,
            annotation.permission,
            annotation.description,
            usage,
            annotation.aliases.toList()
        )
    }

    fun generateCommandMap(commandFunctions: Set<Method>, server: Server) {
        log("Generating command map...")
        log("Found ${commandFunctions.size} command functions, first one being named ${commandFunctions.first().name}.")
        commandFunctions
            .map { it.kotlinFunction }
            .filterIsInstance<KCommand>()
            .filter(::isCommand)
            .map(::convertToBukkit)
            .forEach {
                aliases += it.name
                aliases.addAll(it.aliases)
                server.commandMap.register("parkour", it)
                log("Registered command \"/${it.function.name}\" with permission \"${it.permission}\".")
            }
    }
}