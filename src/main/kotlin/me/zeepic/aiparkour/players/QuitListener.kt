package me.zeepic.aiparkour.players

import api.EventListener
import me.zeepic.aiparkour.messaging.component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

@EventListener
class QuitListener : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        event.quitMessage("&7☹ &c${event.player.name}".component)
    }
}