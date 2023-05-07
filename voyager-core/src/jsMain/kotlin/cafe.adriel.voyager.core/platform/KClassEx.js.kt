package cafe.adriel.voyager.core.platform

import kotlin.reflect.KClass

public actual val KClass<*>.multiplatformName: String? get() = simpleName
