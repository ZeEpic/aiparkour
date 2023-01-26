package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.ParseResult
import me.zeepic.aiparkour.messaging.toMaterial
import org.bukkit.Material
import org.bukkit.command.CommandSender
import kotlin.reflect.KParameter

class MaterialParser : ArgumentParser<Material>(Material::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<Material>) -> Unit
    ): Boolean {
        val material = input.toMaterial()
        if (material == null ) {
            callback(ParseResult(success = false, message = CommandParser.getArgError(
                parameter.index,
                input,
                "an item/block",
                "Use underscores for blocks like grass_block."
            )))
            return false
        }
        callback(ParseResult(success = true, value = material))
        return true
    }
}