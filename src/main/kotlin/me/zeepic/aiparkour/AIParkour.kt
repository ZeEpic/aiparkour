package me.zeepic.aiparkour

import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.commands.KCommand
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.util.*
import kotlin.reflect.jvm.kotlinFunction

class AIParkour : JavaPlugin() {

    override fun onEnable() {
        instance = this

        val commandsReflection = Reflections("me.zeepic.aiparkour.commands")
        val commandClasses = commandsReflection.getMethodsReturn(CommandResult::class.java)
        commandClasses
            .map { it.kotlinFunction }
            .filterIsInstance<KCommand>()
            .filter(CommandParser::isCommand)
            .map(CommandParser::convertToBukkit)
            .forEach {
                CommandParser.aliases += it.name
                CommandParser.aliases.addAll(it.aliases)
                server.commandMap.register("parkour", it)
                 Bukkit.getLogger().info("Registered command \"/${it.function.name}\" with permission \"${it.permission}\".")
            }
    }

    companion object {
        lateinit var instance: AIParkour

        fun runAsync(runnable: () -> Unit) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, runnable)
        }

        val random = Random(now())
    }
}