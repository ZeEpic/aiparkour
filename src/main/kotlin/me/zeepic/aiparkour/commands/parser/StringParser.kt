package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.ParseResult
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class StringParser : ArgumentParser<String>(String::class) {

    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<String>) -> Unit
    ): Boolean {
        callback(ParseResult(success = true, value = input))
        return true
    }

}