package me.zeepic.aiparkour

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.levels.LevelSerializer
import me.zeepic.aiparkour.util.now
import org.bukkit.Bukkit
import org.bukkit.GameRule.*
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import java.util.*

annotation class EventListener

class AIParkour : JavaPlugin() {

    override fun onEnable() {
        instance = this

        val commandsReflection = Reflections("me.zeepic.aiparkour")
        val commandFunctions = commandsReflection.getMethodsAnnotatedWith(Command::class.java)//getMethodsReturn(CommandResult::class.java)
        CommandParser.generateCommandMap(commandFunctions, server)

        saveResource("levels.txt", false)
        LevelSerializer.loadLevels()

        val rootReflection = Reflections("me.zeepic.aiparkour")
        val eventListeners = rootReflection.getTypesAnnotatedWith(EventListener::class.java)
        eventListeners.forEach {
            server.pluginManager.registerEvents(it.getConstructor().newInstance() as Listener, this)
        }

        val world = server.worlds.first()
        with (world) {
            world.pvp = false
            setGameRule(MOB_GRIEFING, false)
            setGameRule(DO_DAYLIGHT_CYCLE, false)
            setGameRule(DO_WEATHER_CYCLE, false)
            setGameRule(DO_FIRE_TICK, false)
            setGameRule(DO_MOB_SPAWNING, false)
            setGameRule(DO_TRADER_SPAWNING, false)
            setGameRule(DO_PATROL_SPAWNING, false)
            setGameRule(DO_INSOMNIA, false)
            setGameRule(DO_IMMEDIATE_RESPAWN, true)
            setGameRule(FALL_DAMAGE, false)
            setGameRule(SPECTATORS_GENERATE_CHUNKS, false)
            setGameRule(SHOW_DEATH_MESSAGES, false)
            setGameRule(RANDOM_TICK_SPEED, 0)
            setGameRule(ANNOUNCE_ADVANCEMENTS, false)
            setGameRule(DROWNING_DAMAGE, false)
            setGameRule(FIRE_DAMAGE, false)
            setGameRule(KEEP_INVENTORY, true)
            setGameRule(FREEZE_DAMAGE, false)
            time = 6000
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