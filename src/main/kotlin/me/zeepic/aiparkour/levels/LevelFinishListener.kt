package me.zeepic.aiparkour.levels

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class LevelFinishListener : Listener {
    @EventHandler
    fun onLevelFinish(event: PlayerMoveEvent) {
        val player = event.player
        if (player.location.block.type != Material.HEAVY_WEIGHTED_PRESSURE_PLATE) return
        if (player.location.block.getRelative(BlockFace.DOWN).type != Material.GOLD_BLOCK) return


    }
}