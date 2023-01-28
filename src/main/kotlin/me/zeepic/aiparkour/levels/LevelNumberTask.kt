package me.zeepic.aiparkour.levels

import api.helpers.readableTimeLength
import api.tasks.Task
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.metadata.PlayerMeta
import org.bukkit.Bukkit

@Task(1.0)
class LifeTask : Runnable {
    override fun run() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (PlayerMeta.level(player) == "N/A") continue
            player.sendActionBar("&3&oStreak &7| &b&o${PlayerMeta.streak(player)}          &3&oTimer &7| &b&o${PlayerMeta.timeSinceLevelStarted(player).readableTimeLength()}".component)
        }
    }
}