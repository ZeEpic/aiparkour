package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.AIParkour
import java.io.File

val levels = mutableListOf<Level>()

object LevelSerializer {

    private fun levelsFile()
        = File(AIParkour.instance.dataFolder, "levels.txt")

    fun saveLevels() {
        levelsFile().writeText(
            levels.joinToString("\n") { it.serialize().values.joinToString(" ") }
        )
    }

    fun loadLevels() {
        levels.addAll(
            levelsFile().readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { it.split(" ") }
                .map { Level.deserialize(mapOf("creator" to it[0], "name" to it[1], "start" to it[2])) }
        )
    }

}