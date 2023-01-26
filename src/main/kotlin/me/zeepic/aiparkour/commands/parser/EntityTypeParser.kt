package me.zeepic.aiparkour.commands.parser

import me.zeepic.aiparkour.commands.ArgumentParser
import me.zeepic.aiparkour.commands.CommandParser
import me.zeepic.aiparkour.commands.ParseResult
import me.zeepic.aiparkour.messaging.toEntityType
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import kotlin.reflect.KParameter

class EntityTypeParser : ArgumentParser<EntityType>(EntityType::class) {
    override fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<EntityType>) -> Unit
    ): Boolean {
        val entityType = input.toEntityType()
        if (entityType == null ) {
            callback(ParseResult(success = false, message = CommandParser.getArgError(
                parameter.index,
                input,
                "an entity type",
                "Use underscores for mobs like elder_guardian."
            )))
            return false
        }
        callback(ParseResult(success = true, value = entityType))
        return true
    }
}