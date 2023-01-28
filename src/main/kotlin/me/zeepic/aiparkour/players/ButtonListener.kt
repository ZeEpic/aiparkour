package me.zeepic.aiparkour.players

import api.EventListener
import me.zeepic.aiparkour.AIParkour
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

@EventListener
class ButtonListener : Listener {

    @EventHandler
    fun onStartParkourButton(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        if (block.type != Material.POLISHED_BLACKSTONE_BUTTON) return
        if (block.location.distance(AIParkour.lobbyLocation) > 5) return
        val player = event.player
        event.isCancelled = true
        AIParkour.beginParkour(player)
        player.playSound(player, Sound.ITEM_GOAT_HORN_SOUND_0, 1f, 2f)
    }
}