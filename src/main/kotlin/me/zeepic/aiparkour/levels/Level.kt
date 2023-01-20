package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.messaging.send
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.UUID

data class Level(
    val creator: UUID,
    val name: String,
    val start: Location
) {

    fun serialize(): Map<String, String> {
        return mapOf(
            "creator" to creator.toString(),
            "name" to name,
            "start" to start.serialize().values.joinToString()
        )
    }

    fun teleport(player: Player) {
        player.teleport(start)
        player.send(" ")
        player.send("&6&9Entering \"$name\" &7&o-&6&o by $creator")
        player.send(" ")
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1.5f)
    }

    companion object {
        private fun fromParts(parts: List<String>)
                = Location(Bukkit.getWorld(parts[0]), parts[1].toDouble(), parts[2].toDouble(), parts[3].toDouble())

        fun deserialize(map: Map<String, String>): Level {
            return Level(
                UUID.fromString(map["creator"]!!),
                map["name"]!!,
                fromParts(map["start"]!!.split(", "))
            )
        }

        fun random(): Level {
            return levels.random()
        }

    }
}