package me.zeepic.aiparkour.levels

import org.bukkit.Bukkit
import org.bukkit.Location

data class Level(
    val creator: String,
    val name: String,
    val start: Location
) {

    fun serialize(): Map<String, String> {
        return mapOf(
            "creator" to creator,
            "name" to name,
            "start" to start.serialize().values.joinToString()
        )
    }
    companion object {
        private fun fromParts(parts: List<String>)
                = Location(Bukkit.getWorld(parts[0]), parts[1].toDouble(), parts[2].toDouble(), parts[3].toDouble())

        fun deserialize(map: Map<String, String>): Level {
            return Level(
                map["creator"]!!,
                map["name"]!!,
                fromParts(map["start"]!!.split(", "))
            )
        }
    }
}