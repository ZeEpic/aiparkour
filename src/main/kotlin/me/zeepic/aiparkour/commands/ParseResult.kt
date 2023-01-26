package me.zeepic.aiparkour.commands

data class ParseResult<R>(val success: Boolean, val value: R? = null, val message: String = "")
