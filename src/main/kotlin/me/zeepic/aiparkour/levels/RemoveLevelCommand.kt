package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.messaging.send
import org.bukkit.entity.Player

@Command("dellevel", "Removes parkour level from the system. This doesn't remove any blocks.", "aiparkour.mklevel")
fun removeLevelCommand(sender: Player, name: String): CommandResult {
    val level = levels.firstOrNull { it.name == name }
    if (level == null) {
        sender.send("&cA level with that name doesn't exist!")
        return CommandResult.SILENT_FAILURE
    }
    levels -= level
    sender.send("&aLevel removed.")
    return CommandResult.SUCCESS
}
