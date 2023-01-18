package me.zeepic.aiparkour.levels

import me.zeepic.aiparkour.commands.Command
import me.zeepic.aiparkour.commands.CommandResult
import org.bukkit.command.CommandSender

val levels = mutableListOf<Level>()

@Command(name = "levels", description = "Lists all parkour levels and their location.", permission = "aiparkour.levels")
fun listLevelsCommand(sender: CommandSender): CommandResult {
    val message =
}