package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.ParseResult
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import kotlin.reflect.KParameter

class ConsoleCommandSenderParser : ArgumentParser<ConsoleCommandSender>(ConsoleCommandSender::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<ConsoleCommandSender>) -> Unit
    ): Boolean {
        if (sender !is ConsoleCommandSender && parameter.index == 1) {
            callback(ParseResult(success = false, message = "&cYou must be the server console to use this command!"))
            return false
        }
        callback(ParseResult(success = true, value = sender as ConsoleCommandSender))
        return true
    }
}
