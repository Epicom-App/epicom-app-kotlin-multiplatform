package org.ebolapp.utils

expect class Language() {
    val code: String
}

val Language.isGerman
    get() = code == "de"

val Language.isEnglish
    get() = code == "en"