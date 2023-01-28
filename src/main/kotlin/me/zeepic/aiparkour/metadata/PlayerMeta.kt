package me.zeepic.aiparkour.metadata

import api.helpers.now
import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.players.changeHealth
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

object PlayerMeta {

    private val levelStartTime = NamespacedKey(AIParkour.instance, "level_start_time")
    private val currentLevel = NamespacedKey(AIParkour.instance, "level")
    private val streak = NamespacedKey(AIParkour.instance, "streak")
    private val highestStreak = NamespacedKey(AIParkour.instance, "highest_streak")
    private val coins = NamespacedKey(AIParkour.instance, "coins")
    private val lives = NamespacedKey(AIParkour.instance, "lives")

    fun timeSinceLevelStarted(player: Player) = now() - player.persistentDataContainer.getOrDefault(levelStartTime, PersistentDataType.LONG, now())
    fun resetLevelStartTime(player: Player) = player.persistentDataContainer.set(levelStartTime, PersistentDataType.LONG, now())

    fun level(player: Player) = player.persistentDataContainer.getOrDefault(currentLevel, PersistentDataType.STRING, "N/A")
    fun setLevel(player: Player, level: String) = player.persistentDataContainer.set(currentLevel, PersistentDataType.STRING, level)

    fun lives(player: Player) = player.persistentDataContainer.getOrDefault(lives, PersistentDataType.INTEGER, 0)
    fun increaseLives(player: Player) {
        val newLives = lives(player) + 1
        player.persistentDataContainer.set(lives, PersistentDataType.INTEGER, newLives)
        if (newLives >= 1) {
            player.changeHealth(newLives)
        }
    }
    fun decreaseLives(player: Player) {
        val newLives = lives(player) - 1
        player.persistentDataContainer.set(lives, PersistentDataType.INTEGER, newLives)
        if (newLives >= 1) {
            player.changeHealth(newLives)
        }
    }
    fun resetLives(player: Player) {
        player.persistentDataContainer.set(lives, PersistentDataType.INTEGER, 3)
        player.changeHealth(3)
    }

    fun streak(player: Player) = player.persistentDataContainer.getOrDefault(streak, PersistentDataType.SHORT, 0)
    fun increaseStreak(player: Player) = player.persistentDataContainer.set(streak, PersistentDataType.SHORT, (streak(player) + 1).toShort())
    fun resetStreak(player: Player) = player.persistentDataContainer.set(streak, PersistentDataType.SHORT, 0)

    fun highestStreak(player: Player) = player.persistentDataContainer.getOrDefault(highestStreak, PersistentDataType.SHORT, 0)
    fun setHighestStreak(player: Player, streak: Short) = player.persistentDataContainer.set(highestStreak, PersistentDataType.SHORT, streak)

    fun coins(player: Player) = player.persistentDataContainer.getOrDefault(coins, PersistentDataType.LONG, 0)
    fun addCoins(player: Player, amount: Long) = player.persistentDataContainer.set(coins, PersistentDataType.LONG, (coins(player) + amount))
    fun removeCoins(player: Player, amount: Long) = player.persistentDataContainer.set(coins, PersistentDataType.LONG, (coins(player) - amount))

}