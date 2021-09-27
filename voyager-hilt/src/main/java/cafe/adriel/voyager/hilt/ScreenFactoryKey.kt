package cafe.adriel.voyager.hilt

import dagger.MapKey
import kotlin.reflect.KClass

/**
 * A Dagger multibinding key used to identify a [ScreenFactory]
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
public annotation class ScreenFactoryKey(val value: KClass<out ScreenFactory>)
