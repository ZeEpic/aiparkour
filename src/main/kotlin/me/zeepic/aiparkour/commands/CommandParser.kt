package me.zeepic.aiparkour.commands

import me.zeepic.aiparkour.messaging.log
import org.bukkit.Bukkit
import org.bukkit.Server
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

typealias KCommand = KFunction<CommandResult>

object CommandParser {

    private val aliases = mutableListOf<String>()

    private fun isCommand(command: KCommand)
        = command.name.endsWith("Command") && command.annotations.any { it is Command }

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
            annotation.description,
            usage,
            annotation.aliases.toList()
        )
    }

    fun generateCommandMap(commandFunctions: Set<Method>, server: Server) {
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