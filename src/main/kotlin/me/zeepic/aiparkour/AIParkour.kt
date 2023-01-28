package me.zeepic.aiparkour

import api.EventListener
import api.commands.ArgumentParser
import api.commands.CommandGroup
import api.commands.CommandParser
import api.commands.Parser
import api.helpers.color
import api.helpers.now
import api.helpers.typesAnnotatedWith
import api.helpers.unbreakable
import api.tasks.Task
import me.zeepic.aiparkour.levels.Level
import me.zeepic.aiparkour.levels.LevelSerializer
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.metadata.PlayerMeta
import org.bukkit.*
import org.bukkit.GameRule.*
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*


class AIParkour : JavaPlugin() {

    override fun onEnable() {
        instance = this
        shortName = this.name.lowercase()
        lobbyLocation = Location(Bukkit.getWorld("world"), 0.5, 65.0, 0.5)

        typesAnnotatedWith<ArgumentParser<*>>(Parser::class)
            .forEach(CommandParser::registerArgumentParser)

        val methods = typesAnnotatedWith<Any>(CommandGroup::class)
            .associate { it.kotlin to it.methods.toList() }
        CommandParser.generateCommandMap(methods, server)

        if (!dataFolder.exists()) dataFolder.mkdir()
        if (!LevelSerializer.levelsFile().exists()) LevelSerializer.levelsFile().createNewFile()
        LevelSerializer.loadLevels()

        typesAnnotatedWith<Listener>(EventListener::class).forEach {
            server.pluginManager.registerEvents(it.getConstructor().newInstance(), this)
        }

        typesAnnotatedWith<Runnable>(Task::class).forEach {
            server.scheduler.runTaskTimer(
                this,
                it.getConstructor().newInstance(),
                0L,
                (it.getAnnotation(Task::class.java).periodSeconds * 20).toLong()
            )
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
            setGameRule(FALL_DAMAGE, false)
            setGameRule(SPECTATORS_GENERATE_CHUNKS, false)
            setGameRule(SHOW_DEATH_MESSAGES, false)
            setGameRule(ANNOUNCE_ADVANCEMENTS, false)
            setGameRule(DROWNING_DAMAGE, false)
            setGameRule(FIRE_DAMAGE, false)
            setGameRule(FREEZE_DAMAGE, false)
            setGameRule(KEEP_INVENTORY, true)
            setGameRule(DO_IMMEDIATE_RESPAWN, true)
            setGameRule(RANDOM_TICK_SPEED, 0)
            time = 6000
        }

    }

    override fun onDisable() {
        LevelSerializer.saveLevels()
    }

    companion object {
        val messagePrefix = "&3AI &bParkour &7➮ &f".component
        lateinit var instance: AIParkour
        lateinit var shortName: String

        lateinit var lobbyLocation: Location

        fun beginParkour(player: Player) {
            val level = Level.random(player)
            PlayerMeta.resetLives(player)

            level.teleport(player)
            player.gameMode = GameMode.ADVENTURE
            player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, false, false))
            player.inventory.boots = ItemStack(Material.LEATHER_BOOTS)
                .unbreakable()
                .color(Color.fromRGB(20, 200,  30))
            player.send("&aParkour started! &7&oDon't fall!")
        }

        val random = Random(now())
    }
}