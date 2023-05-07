package cafe.adriel.voyager.sample.kodeinIntegration

import org.kodein.di.bindings.ScopeCloseable

data class KodeinScopedDependencySample(
    val screenKey: String
) : ScopeCloseable {
    override fun close() {
        println("Being disposed")
    }
}
