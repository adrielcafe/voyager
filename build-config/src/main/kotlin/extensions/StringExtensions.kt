package extensions

import java.util.Locale

fun String.capitalize(): String = replaceFirstChar { firstChar ->
    if (firstChar.isLowerCase()) {
        firstChar.titlecase(Locale.getDefault())
    } else {
        toString()
    }
}
