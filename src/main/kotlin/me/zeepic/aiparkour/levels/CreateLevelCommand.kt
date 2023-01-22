package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.messaging.title
import org.bukkit.entity.Player

@Command("mklevel", "Creates a new parkour level.", "aiparkour.mklevel")
fun createLevelCommand(sender: Player, name: String): CommandResult {
    if (levels.any { it.name.equals(name, ignoreCase = true) }) {
        sender.send("&cA level with that name already exists!")
        return CommandResult.SILENT_FAILURE
    }
    levels += Level(sender.uniqueId, name.title(), sender.location)
    sender.send("&aLevel created!")
    return CommandResult.SUCCESS
}