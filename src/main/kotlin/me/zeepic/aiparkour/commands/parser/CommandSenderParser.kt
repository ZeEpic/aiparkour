package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.ParseResult
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class CommandSenderParser : ArgumentParser<CommandSender>(CommandSender::class) {

    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<CommandSender>) -> Unit
    ): Boolean {
        callback(ParseResult(success = true, value = sender))
        return true
    }
}