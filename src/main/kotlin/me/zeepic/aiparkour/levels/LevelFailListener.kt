package me.zeepic.aiparkour.levels

import api.EventListener
import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.metadata.PlayerMeta
import me.zeepic.aiparkour.players.changeHealth
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.potion.PotionEffectType

@EventListener
class LevelFailListener : Listener {

    @EventHandler
    fun onFail(event: PlayerMoveEvent) {
        if (event.to.y > 20) return
        val player = event.player
        PlayerMeta.decreaseLives(player)
        if (PlayerMeta.lives(player) <= 0) {
            player.send("&cYou died with a streak of &3${PlayerMeta.streak(player)}&c.")
            player.send("&cYou have been sent to the lobby.")
            PlayerMeta.setLevel(player, "N/A")
            PlayerMeta.resetStreak(player)
            PlayerMeta.resetLevelStartTime(player)
            player.changeHealth(10)
            player.inventory.boots = null
            player.removePotionEffect(PotionEffectType.INVISIBILITY)
            player.teleport(AIParkour.lobbyLocation)
            return
        }
        val level = Level.fromName(PlayerMeta.level(player)) ?: return
        player.teleport(level.start)
        val lives = PlayerMeta.lives(player)
        player.send("&7☠ &cLost a life. &7&oYou now have $lives ${if (lives == 1) "life" else "lives"} left.")
        player.playSound(player, Sound.ITEM_TOTEM_USE, 1f, 2f)
    }
}