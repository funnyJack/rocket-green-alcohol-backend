package com.funnyjack.monolith.util

private val matchedChars = arrayOf(' ', '　', '\t', '\r', '\n')

fun String.fullTrim() = trim { it in matchedChars }