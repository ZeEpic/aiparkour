package me.zeepic.aiparkour.levels

import api.EventListener
import api.helpers.readableTimeLength
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.metadata.PlayerMeta
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

@EventListener
class LevelFinishListener : Listener {
    @EventHandler
    fun onLevelFinish(event: PlayerMoveEvent) {
        val player = event.player
        val block = player.location.block
        if (block.type != Material.LIGHT_WEIGHTED_PRESSURE_PLATE) return
        if (block.getRelative(BlockFace.DOWN).type != Material.GOLD_BLOCK) return
        if (PlayerMeta.level(player) == "N/A") return
        val time = PlayerMeta.timeSinceLevelStarted(player).readableTimeLength()
        PlayerMeta.increaseStreak(player)
        val streak = PlayerMeta.streak(player)
        player.send("&7Completed &3${PlayerMeta.level(player)} &7in &3$time&7 &8(level $streak).")
        if (streak > PlayerMeta.highestStreak(player)) {
            PlayerMeta.setHighestStreak(player, streak)
        }
        val level = Level.random(player)
        level.teleport(player)
    }

}