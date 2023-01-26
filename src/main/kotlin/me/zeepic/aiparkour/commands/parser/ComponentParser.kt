package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.ParseResult
import me.zeepic.aiparkour.messaging.component
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class ComponentParser : ArgumentParser<Component>(Component::class) {

    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<Component>) -> Unit
    ): Boolean {
        callback(ParseResult(success = true, value = input.component))
        return true
    }
}