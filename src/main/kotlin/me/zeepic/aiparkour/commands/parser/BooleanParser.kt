package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.ParseResult
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class BooleanParser : ArgumentParser<Boolean>(Boolean::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<Boolean>) -> Unit
    ): Boolean {
        val result = when (input) {
            "true", "yes" -> ParseResult(success = true, value = true)
            "false", "no" -> ParseResult(success = true, value = false)
            else -> ParseResult(success = false, message = CommandParser.getArgError(parameter.index, input, "yes/no"))
        }
        callback(result)
        return result.success
    }
}