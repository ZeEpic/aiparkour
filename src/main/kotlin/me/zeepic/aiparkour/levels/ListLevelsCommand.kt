package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import org.bukkit.command.CommandSender

@Command(name = "levels", description = "Lists all parkour levels and their location.", permission = "aiparkour.levels")
fun listLevelsCommand(sender: CommandSender): CommandResult {
    if (levels.isEmpty()) {
        sender.send("&cThere aren't any parkour levels yet!")
        return CommandResult.SILENT_FAILURE
    }
    sender.send("&9Parkour Levels")
    levels.map { "&7 - &e${it.name} &3@ &e&o".component.append(it.start.component) }
        .forEach { sender.sendMessage(it) }
    return CommandResult.SUCCESS
}