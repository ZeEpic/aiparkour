package me.zeepic.aiparkour.players

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandGroup
import me.zeepic.aiparkour.commands.CommandResult
import me.zeepic.aiparkour.messaging.send
import org.bukkit.GameMode
import org.bukkit.entity.Player

@CommandGroup("gamemode")
class GamemodeCommands {

    @Command("gmc", "Sets your gamemode to creative.")
    fun gamemodeCreativeCommand(sender: Player): CommandResult {
        sender.gameMode = GameMode.CREATIVE
        sender.send("&aGamemode set to creative.")
        return CommandResult.SUCCESS
    }

    @Command("gms", "Sets your gamemode to survival.")
    fun gamemodeSurvivalCommand(sender: Player): CommandResult {
        sender.gameMode = GameMode.SURVIVAL
        sender.send("&aGamemode set to survival.")
        return CommandResult.SUCCESS
    }

    @Command("gma", "Sets your gamemode to adventure.")
    fun gamemodeAdventureCommand(sender: Player): CommandResult {
        sender.gameMode = GameMode.ADVENTURE
        sender.send("&aGamemode set to adventure.")
        return CommandResult.SUCCESS
    }

    @Command("gmsp", "Sets your gamemode to spectator.")
    fun gamemodeSpectatorCommand(sender: Player): CommandResult {
        sender.gameMode = GameMode.SPECTATOR
        sender.send("&aGamemode set to spectator.")
        return CommandResult.SUCCESS
    }

}
