package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.ParseResult
import me.zeepic.aiparkour.util.milliseconds
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class DurationParser : ArgumentParser<Duration>(Duration::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<Duration>) -> Unit
    ): Boolean {
        // "forever", "20d", "2h", "40s", etc
        val long = input.milliseconds()
        if (long == Long.MAX_VALUE && input != "forever") {
            callback(ParseResult(false, message = CommandParser.getArgError(
                parameter.index,
                input,
                "an amount of time",
                "Use \"forever\" or a number followed by \"d\" for days, \"h\" for hours, \"m\" for minutes, or \"s\" for seconds (like 30m for 30 minutes)."
            )))
            return false
        }
        callback(ParseResult(true, long.milliseconds))
        return true
    }
}