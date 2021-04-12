package org.ebolapp.utils

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual class Language {
    actual val code: String = if (NSLocale.currentLocale.languageCode == "de") "de" else "en"
}