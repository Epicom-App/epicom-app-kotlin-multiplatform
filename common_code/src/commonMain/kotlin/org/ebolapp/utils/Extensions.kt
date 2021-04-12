package org.ebolapp.utils

fun Throwable.isCanceled() : Boolean = message?.contains("cancel", ignoreCase = true) ?: false