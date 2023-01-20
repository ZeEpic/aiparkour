package me.zeepic.aiparkour

import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.levels.LevelSerializer
import me.zeepic.aiparkour.util.now
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.util.*

annotation class EventListener

class AIParkour : JavaPlugin() {

    override fun onEnable() {
        instance = this

        val commandsReflection = Reflections("me.zeepic.aiparkour.commands")
        val commandFunctions = commandsReflection.getMethodsReturn(CommandResult::class.java)
        CommandParser.generateCommandMap(commandFunctions, server)

        LevelSerializer.loadLevels()

        val rootReflection = Reflections("me.zeepic.aiparkour")
        val eventListeners = rootReflection.getTypesAnnotatedWith(EventListener::class.java)
        eventListeners.forEach {
            server.pluginManager.registerEvents(it.getConstructor().newInstance() as Listener, this)
        }

    }

    override fun onDisable() {
        LevelSerializer.saveLevels()
    }

    companion object {
        lateinit var instance: AIParkour

        fun runAsync(runnable: () -> Unit) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, runnable)
        }

        val random = Random(now())
    }
}