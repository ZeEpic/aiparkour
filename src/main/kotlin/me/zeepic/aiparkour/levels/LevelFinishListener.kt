package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.EventListener
import me.zeepic.aiparkour.messaging.send
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

@EventListener
class LevelFinishListener : Listener {
    @EventHandler
    fun onLevelFinish(event: PlayerMoveEvent) {
        val player = event.player
        if (player.location.block.type != Material.HEAVY_WEIGHTED_PRESSURE_PLATE) return
        if (player.location.block.getRelative(BlockFace.DOWN).type != Material.GOLD_BLOCK) return
        player.send("&aYou finished the level!")
        val level = Level.random()
        level.teleport(player)
    }
}