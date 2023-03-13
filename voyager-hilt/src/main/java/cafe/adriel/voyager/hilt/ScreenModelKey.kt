package cafe.adriel.voyager.hilt

import cafe.adriel.voyager.core.model.ScreenModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * A Dagger multibinding key used to identify a [ScreenModel]
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
public annotation class ScreenModelKey(val value: KClass<out ScreenModel>)
