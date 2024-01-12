package cafe.adriel.voyager.navigator

import cafe.adriel.voyager.core.screen.Screen

public fun Screen.self() : Screen = this

context(Screen)
public fun Navigator.push(screen: Screen) {
    push(self(), screen)
}

context(Screen)
public fun Navigator.push(screens: List<Screen>) {
    push(self(), screens)
}

context(Screen)
public fun Navigator.replace(screen: Screen) {
    replace(self(), screen)
}

context(Screen)
public fun Navigator.replaceAll(screen: Screen) {
    replaceAll(self(), screen)
}

context(Screen)
public fun Navigator.replaceAll(screens: List<Screen>) {
    replaceAll(self(), screens)
}

context(Screen)
public fun Navigator.pop() {
    pop(self())
}

context(Screen)
public fun Navigator.popUntil(predicate: (Screen) -> Boolean) {
    popUntil(self(), predicate)
}

context(Screen)
public fun Navigator.popAll() {
    popAll(self())
}

context(Screen)
public fun Navigator.clearEvent() {
    clearEvent(self())
}
