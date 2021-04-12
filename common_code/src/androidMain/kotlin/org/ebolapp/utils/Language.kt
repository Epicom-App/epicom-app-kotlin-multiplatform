package org.ebolapp.utils

import java.util.Locale

actual class Language {
    actual val code: String = if (Locale.getDefault().language == "de") "de" else "en"

}