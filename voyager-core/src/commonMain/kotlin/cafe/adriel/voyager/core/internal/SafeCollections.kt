package cafe.adriel.voyager.core.internal

/**
 * https://youtrack.jetbrains.com/issue/KT-71375/Prevent-Kotlins-removeFirst-and-removeLast-from-causing-crashes-on-Android-14-and-below-after-upgrading-to-Android-API-Level-35
 */

internal fun <T> MutableList<T>.removeFirstElement(): T =
    if (isEmpty()) throw NoSuchElementException("List is empty.") else removeAt(0)

internal fun <T> MutableList<T>.removeFirstElementOrNull(): T? =
    if (isEmpty()) null else removeAt(0)

internal fun <T> MutableList<T>.removeLastElement(): T =
    if (isEmpty()) throw NoSuchElementException("List is empty.") else removeAt(lastIndex)
