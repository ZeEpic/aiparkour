package me.zeepic.aiparkour.commands

import me.zeepic.aiparkour.messaging.send
import org.bukkit.GameMode
import org.bukkit.entity.Player

@Command("gmc", "Sets your gamemode to creative.", "aiparkour.gamemode")
fun gamemodeCreativeCommand(sender: Player): CommandResult {
    sender.gameMode = GameMode.CREATIVE
    sender.send("&aGamemode set to creative.")
    return CommandResult.SUCCESS
}

@Command("gms", "Sets your gamemode to survival.", "aiparkour.gamemode")
fun gamemodeSurvivalCommand(sender: Player): CommandResult {
    sender.gameMode = GameMode.SURVIVAL
    sender.send("&aGamemode set to survival.")
    return CommandResult.SUCCESS
}

@Command("gma", "Sets your gamemode to adventure.", "aiparkour.gamemode")
fun gamemodeAdventureCommand(sender: Player): CommandResult {
    sender.gameMode = GameMode.ADVENTURE
    sender.send("&aGamemode set to adventure.")
    return CommandResult.SUCCESS
}

@Command("gmsp", "Sets your gamemode to spectator.", "aiparkour.gamemode")
fun gamemodeSpectatorCommand(sender: Player): CommandResult {
    sender.gameMode = GameMode.SPECTATOR
    sender.send("&aGamemode set to spectator.")
    return CommandResult.SUCCESS
}