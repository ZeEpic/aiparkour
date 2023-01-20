package me.zeepic.aiparkour.levels

import com.google.gson.Gson
import me.zeepic.aiparkour.AIParkour
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File

val levels = mutableListOf<Level>()

object LevelSerializer {

    private val gson = Gson()

    private fun levelsFile()
        = File(AIParkour.instance.server.pluginsFolder, "levels.json")

    fun saveLevels() {
        levelsFile().writeText(
            levels.joinToString("\n") { gson.toJson(it) }
        )
    }

    fun loadLevels() {
        levels.addAll(
            levelsFile().readText().split("\n")
                .map { gson.fromJson(it, Level::class.java) }
        )
    }

}