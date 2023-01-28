package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.AIParkour
import java.io.File

val levels = mutableListOf<Level>()

object LevelSerializer {

    fun levelsFile()
        = File(AIParkour.instance.dataFolder, "levels.txt")

    fun saveLevels() {
        levelsFile().writeText(
            levels.joinToString("\n", transform = Level::serialize)
        )
    }

    fun loadLevels() {
        levels.addAll(
            levelsFile().readText()
                .split("\n")
                .filter { it.isNotBlank() }
                .map { Level.deserialize(it) }
        )
    }

}