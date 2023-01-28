package api.gui

import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import kotlin.math.floor

open class PaginatedMenu<T: Button>(title: String,
                                    rowsPerPage: Int,
                                    private val data: Collection<T>) : DataMenu<T>(title, rowsPerPage, data) {

    private val itemsPerPage = (rowsPerPage - 2) * 7.0
    private val pages = floor(data.size / itemsPerPage)
    private var page = 0
    private fun hasNextPage() = page < pages
    private fun hasPreviousPage() = page > 0
    private val itemOnPage = { i: Int, _: T -> i >= page * itemsPerPage && i <= (page + 1) * itemsPerPage }
    private val finalSlot = rowsPerPage * 9 - 1
    private val bottomLeftSlot = (rowsPerPage - 1) * 9
    protected val bottomMiddleSlot = bottomLeftSlot + 4

    override fun onClick(event: InventoryClickEvent): Boolean {
        super.onClick(event)
        val player = event.whoClicked as Player
        val clicked = event.currentItem ?: return false
        page += when (clicked) {
            nextPageItem -> 1
            previousPageItem -> -1
            else -> return false
        }
        updateInventory(player)
        player.playSound(player.location, Sound.ITEM_BOOK_PAGE_TURN, 1f, 1f)
        return false
    }

    override fun updateInventory(player: Player) {
        inventory.clear()
        borderWith(borderItem)
        if (hasNextPage()) inventory.setItem(finalSlot, nextPageItem)
        if (hasPreviousPage()) inventory.setItem(bottomLeftSlot, previousPageItem)
        data.filterIndexed(itemOnPage)
            .map { it.getIcon(player) }
            .forEach(inventory::addItem)
    }



}