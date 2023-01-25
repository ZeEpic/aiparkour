package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.util.EventListener
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.metadata.PlayerMeta
import me.zeepic.aiparkour.util.readableTimeLength
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import kotlin.time.Duration.Companion.milliseconds

@EventListener
class LevelFinishListener : Listener {
    @EventHandler
    fun onLevelFinish(event: PlayerMoveEvent) {
        val player = event.player
        val block = player.location.block
        if (block.type != Material.HEAVY_WEIGHTED_PRESSURE_PLATE) return
        if (block.getRelative(BlockFace.DOWN).type != Material.GOLD_BLOCK) return
        val minutes = PlayerMeta.timeSinceLevelStarted(player).milliseconds.inWholeMinutes
        player.send("&7You finished &3${PlayerMeta.level(player)} &7in &3${minutes.readableTimeLength()}&7.")
        val level = Level.random()
        level.teleport(player)
    }

}