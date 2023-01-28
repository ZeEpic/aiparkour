package api.gui

import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class SimpleDataMenu(title: String, data: List<ItemStack>) : CustomMenu(title, InventoryType.HOPPER) {

    init {
        create()
        data.forEachIndexed(inventory::setItem)
    }

}