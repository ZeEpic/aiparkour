package api.gui

import api.helpers.description
import api.helpers.named
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

val borderMaterial = Material.GRAY_STAINED_GLASS_PANE
val borderItem = ItemStack(borderMaterial).named(" ")
val nextPageItem = ItemStack(Material.ARROW).named("Next Page").description("Click me")
val previousPageItem = ItemStack(Material.ARROW).named("Previous Page").description("Click me")

open class DataMenu<T : Button>(title: String,
                                rows: Int,
                                private val data: Collection<T>) : CustomMenu(title, rows) {

    init { create() }


    open fun T.isMatching(item: ItemStack, player: Player)
        = getIcon(player) == item

    override fun onClick(event: InventoryClickEvent): Boolean {
        val player = event.whoClicked as Player
        val clicked = event.currentItem ?: return false
        when (clicked) {
            borderItem -> return false
            else -> {
                if (clicked.type.isAir) return false
                val tClicked = data.firstOrNull { it.isMatching(clicked, player) } ?: return false
                if (dataClicked(player, tClicked)) updateInventory(player)
            }
        }
        return true
    }

    open fun dataClicked(player: Player, clicked: T): Boolean {
        return false
    }

    override fun updateInventory(player: Player) {
        inventory.clear()
        borderWith(borderItem)
        data.map { it.getIcon(player) }
            .forEach(inventory::addItem)
    }

}