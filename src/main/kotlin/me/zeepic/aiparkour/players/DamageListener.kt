package me.zeepic.aiparkour.players

import api.EventListener
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

@EventListener
class DamageListener : Listener {

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.entityType != EntityType.PLAYER) return
        event.isCancelled = true
    }

    @EventListener
    fun onHunger(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }
}