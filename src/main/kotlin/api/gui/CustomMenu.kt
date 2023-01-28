package api.gui

import me.zeepic.aiparkour.messaging.component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import kotlin.math.abs

abstract class CustomMenu(private val title: String, private val rows: Int) : InventoryHolder, Listener, Menu {

    private lateinit var type: InventoryType
    private lateinit var inventory: Inventory
    override fun getInventory() = inventory

    constructor(title: String, type: InventoryType) : this(title, -1) {
        this.type = type
    }

    final override fun create() {
        if (::type.isInitialized) {
            inventory = Bukkit.createInventory(this, type, title.component)
            return
        }
        inventory = Bukkit.createInventory(this, rows * 9, title.component)
    }

    override fun showTo(player: Player) {
        player.openInventory(inventory)
    }

    fun fillWith(item: ItemStack) {
        (0 until inventory.size).forEach { inventory.setItem(it, item) }
    }

    fun borderWith(item: ItemStack) {
        for (i in 0 until inventory.size) {
            if (i % 9 == 0 || i % 9 == 8 || i < 10 || abs(i - inventory.size) < 9) {
                inventory.setItem(i, item)
            }
        }
    }

}
