package cafe.adriel.voyager.core.annotation

@Target(
    allowedTargets = [
        AnnotationTarget.CLASS,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.TYPEALIAS,
        AnnotationTarget.CONSTRUCTOR
    ]
)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is Voyager Internal. It may be changed in the future without notice."
)
public annotation class InternalVoyagerApi

@Target(
    allowedTargets = [
        AnnotationTarget.CLASS,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.TYPEALIAS,
        AnnotationTarget.CONSTRUCTOR
    ]
)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is experimental. It may be changed in the future without notice."
)
public annotation class ExperimentalVoyagerApi
