package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.ParseResult
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class DoubleParser : ArgumentParser<Double>(Double::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<Double>) -> Unit
    ): Boolean {
        val number = input.toDoubleOrNull()
        if (number == null ) {
            callback(ParseResult(success = false, message = CommandParser.getArgError(
                parameter.index,
                input,
                "a decimal or whole number"
            )))
            return false
        }
        callback(ParseResult(success = true, value = number))
        return true
    }
}