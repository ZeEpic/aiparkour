package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandGroup
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.messaging.title
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandGroup("levels")
class ParkourLevelCommands {

    @Command("mklevel", "Creates a new parkour level.")
    fun createLevelCommand(sender: Player, name: String): CommandResult {
        if (levels.any { it.name.equals(name, ignoreCase = true) }) {
            sender.send("&cA level with that name already exists!")
            return CommandResult.SILENT_FAILURE
        }
        levels += Level(sender.uniqueId, name.title(), sender.location)
        sender.send("&aLevel created!")
        return CommandResult.SUCCESS
    }

    @Command("levels", "Lists all parkour levels and their location.")
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

    @Command("dellevel", "Removes parkour level from the system. This doesn't remove any blocks.")
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

}
