package api.gui

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun interface Button {
    fun getIcon(player: Player): ItemStack
}