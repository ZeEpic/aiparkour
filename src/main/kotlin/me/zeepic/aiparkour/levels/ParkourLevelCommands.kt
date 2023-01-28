package me.zeepic.aiparkour.levels

import api.commands.Command
import api.commands.CommandGroup
import api.commands.CommandResult
import api.helpers.dateFormat
import api.helpers.now
import me.zeepic.aiparkour.AIParkour
import me.zeepic.aiparkour.messaging.component
import me.zeepic.aiparkour.messaging.send
import me.zeepic.aiparkour.messaging.title
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandGroup("levels")
class ParkourLevelCommands {

    @Command("start", "Starts a level. This is for testing.")
    fun startLevelCommand(sender: Player): CommandResult {
        if (levels.isEmpty()) {
            sender.send("&cThere are no levels to play.")
            return CommandResult.SUCCESS
        }

        AIParkour.beginParkour(sender)
        return CommandResult.SUCCESS
    }

    @Command("savelevels", "Saves all levels to disk.")
    fun saveLevelsCommand(sender: CommandSender): CommandResult {
        LevelSerializer.saveLevels()
        sender.send("&aSaved all levels to disk.")
        return CommandResult.SUCCESS
    }

    @Command("mklevel", "Creates a new parkour level.")
    fun createLevelCommand(sender: Player, name: String): CommandResult {
        if (levels.any { it.name.equals(name, ignoreCase = true) }) {
            sender.send("&cA level with that name already exists!")
            return CommandResult.SILENT_FAILURE
        }
        levels += Level(sender.uniqueId, name.title(), sender.location, now())
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
        levels.map { "&7 - &f&o${it.name} &3@ &7&o".component.append(it.start.component).append(" &3⌚ &7&o${it.creationDate.dateFormat}".component) }
            .forEach { sender.sendMessage(it) }
        return CommandResult.SUCCESS
    }

    @Command("dellevel", "Removes parkour level from the system. This doesn't remove any blocks.")
    fun removeLevelCommand(sender: Player, name: String): CommandResult {
        val level = Level.fromName(name)
        if (level == null) {
            sender.send("&cA level with that name doesn't exist!")
            return CommandResult.SILENT_FAILURE
        }
        levels -= level
        sender.send("&aLevel removed.")
        return CommandResult.SUCCESS
    }

}
