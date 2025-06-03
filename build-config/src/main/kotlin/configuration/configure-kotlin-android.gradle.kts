import extensions.kotlinAndroid
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlinAndroid {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
