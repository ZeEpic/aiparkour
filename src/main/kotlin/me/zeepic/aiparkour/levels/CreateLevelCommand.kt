package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.messaging.send
import org.bukkit.entity.Player

@Command("mklevel", "Creates a new parkour level.", "aiparkour.mklevel")
fun createLevelCommand(sender: Player, name: String): CommandResult {
    if (levels.any { it.name == name }) {
        sender.send("&cA level with that name already exists!")
        return CommandResult.SILENT_FAILURE
    }
    levels += Level(sender.uniqueId, name, sender.location)
    sender.send("&aLevel created!")
    return CommandResult.SUCCESS
}