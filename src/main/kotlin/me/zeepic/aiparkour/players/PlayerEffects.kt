package me.zeepic.aiparkour.players

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player

fun Player.changeHealth(hearts: Int) {
    this.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = hearts.toDouble() * 2.0
    this.health = hearts.toDouble() * 2.0
}