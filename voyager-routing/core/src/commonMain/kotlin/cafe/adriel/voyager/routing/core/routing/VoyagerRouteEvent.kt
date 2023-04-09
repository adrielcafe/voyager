package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

internal sealed class VoyagerRouteEvent {

    abstract fun execute(navigator: Navigator)

    object Idle : VoyagerRouteEvent() {
        var canPop: Boolean = true
            private set

        override fun execute(navigator: Navigator) {
            canPop = navigator.canPop || navigator.parent?.canPop ?: false
        }
    }

    object Pop : VoyagerRouteEvent() {
        override fun execute(navigator: Navigator) {
            navigator.pop()
        }
    }

    data class PopUntil(val predicate: (Screen) -> Boolean) : VoyagerRouteEvent() {
        override fun execute(navigator: Navigator) {
            navigator.popUntil(predicate)
        }
    }

    data class Push(val screen: Screen) : VoyagerRouteEvent() {
        override fun execute(navigator: Navigator) {
            navigator.push(screen)
        }
    }

    data class Replace(val screen: Screen, val replaceAll: Boolean) : VoyagerRouteEvent() {
        override fun execute(navigator: Navigator) {
            if (replaceAll) {
                navigator.replaceAll(screen)
            } else {
                navigator.replace(screen)
            }
        }
    }
}
