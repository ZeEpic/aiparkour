package me.zeepic.aiparkour.levels

import api.helpers.random
import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.messaging.color
import me.zeepic.aiparkour.messaging.hoverText
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.metadata.PlayerMeta
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

data class Level(
    val creator: UUID,
    val name: String,
    val start: Location,
    val creationDate: Long
) {

    fun serialize(): String {
        return listOf(
            creator.toString(),
            name,
            start.serialize().values.joinToString(";"),
            creationDate.toString()
        ).joinToString(" \\\\ ")
    }

    fun teleport(player: Player) {
        with (player) {
            teleport(start)
            sendMessage(" ")
            send("&aEntering &7\"&o${this@Level.name}&7\"")
            sendMessage("&f                     &6✎ &o${Bukkit.getOfflinePlayer(creator).name}".color().hoverText("Author"))
            sendMessage(" ")
            if (PlayerMeta.streak(player) % 10 == 0) {
                playSound(this, Sound.ENTITY_PLAYER_LEVELUP, 1f, 0.75f)
            } else {
                playSound(this, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1.5f)
            }
            PlayerMeta.resetLevelStartTime(this)
            PlayerMeta.setLevel(this, this@Level.name)
        }
    }

    companion object {

        fun deserialize(level: String): Level {
            val (creator, name, start, creationDate) = level.split(" \\\\ ")
            val (world, x, y, z, yaw, pitch) = start.split(";")
            return Level(
                UUID.fromString(creator),
                name,
                Location(Bukkit.getWorld(world), x.toDouble(), y.toDouble(), z.toDouble(), pitch.toFloat(), yaw.toFloat()),
                creationDate.toLong()
            )
        }

        fun random(player: Player): Level {
            val previousLevel = fromName(PlayerMeta.level(player))
            if (previousLevel != null) {
                levels -= previousLevel
            }
            val randomLevel = levels.random(AIParkour.random)
            if (previousLevel != null) {
                levels += previousLevel
            }
            return randomLevel
        }

        fun fromName(name: String)
            = levels.firstOrNull { it.name.equals(name, ignoreCase = true) }

    }
}

private operator fun <E> List<E>.component6() = get(5)
