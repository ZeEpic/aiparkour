package me.zeepic.aiparkour.commands

import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class ArgumentParser<T : Any>(val type: KClass<T>) {
    abstract fun parse(
        input: String,
        sender: CommandSender,
        parameter: KParameter,
        callback: (ParseResult<T>) -> Unit
    ): Boolean
}