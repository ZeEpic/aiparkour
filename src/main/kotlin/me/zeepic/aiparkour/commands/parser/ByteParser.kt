package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.ParseResult
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class ByteParser : ArgumentParser<Byte>(Byte::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<Byte>) -> Unit
    ): Boolean {
        val number = input.toByteOrNull()
        if (number == null ) {
            callback(
                ParseResult(success = false, message = CommandParser.getArgError(
                parameter.index,
                input,
                "a whole number between 0 and 255"
            ))
            )
            return false
        }
        callback(ParseResult(success = true, value = number))
        return true
    }
}