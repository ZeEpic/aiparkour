package me.zeepic.aiparkour.players

import api.EventListener
import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.metadata.PlayerMeta
import net.kyori.adventure.title.Title
import net.kyori.adventure.util.Ticks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

@EventListener
class JoinListener : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        with(event.player) {
            teleport(AIParkour.lobbyLocation)
            PlayerMeta.resetStreak(this)
            PlayerMeta.resetLevelStartTime(this)
            changeHealth(10)
            PlayerMeta.resetStreak(this)
            sendMessage(" ")
            send("&7Welcome to &3AI &bParkour&7!")
            sendMessage(" ")
            showTitle(
                Title.title(
                    "&3AI &bParkour".component,
                    " ".component,
                    Title.Times.times(Ticks.duration(10), Ticks.duration(40), Ticks.duration(10))
                )
            )
            event.joinMessage("&7ツ &6$name".component)
        }
    }
}