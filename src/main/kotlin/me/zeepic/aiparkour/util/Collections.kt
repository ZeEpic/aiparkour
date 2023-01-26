package me.zeepic.aiparkour.util

fun <E> Collection<E?>.anyNull() = any { it == null }
