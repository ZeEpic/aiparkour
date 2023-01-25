package me.zeepic.aiparkour.util

fun <E> List<E>.anyNull() = any { it == null }

fun <E> Collection<Collection<E>>.fold() = this.fold(mutableListOf<E>()) { acc, list -> acc.addAll(list); acc }