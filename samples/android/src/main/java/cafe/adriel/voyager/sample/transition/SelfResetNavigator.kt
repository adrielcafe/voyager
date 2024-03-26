package cafe.adriel.voyager.sample.transition

import android.util.Log
import androidx.compose.runtime.saveable.SaveableStateHolder
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.Stack
import cafe.adriel.voyager.core.stack.toMutableStateStack
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorCreator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun SelfResetNavigatorCreator(resetAfter: Long): NavigatorCreator = {
        screenCollection: List<Screen>,
        s: String,
        saveableStateHolder: SaveableStateHolder,
        navigatorDisposeBehavior: NavigatorDisposeBehavior,
        navigator: Navigator?, ->
    SelfResetNavigator(resetAfter, screenCollection, s, saveableStateHolder, navigatorDisposeBehavior, navigator)
}

private class SelfResetNavigator @InternalVoyagerApi constructor(
    val resetAfter: Long,
    screens: List<Screen>,
    key: String,
    stateHolder: SaveableStateHolder,
    disposeBehavior: NavigatorDisposeBehavior,
    parent: Navigator? = null,
    private val stack: Stack<Screen> = screens.toMutableStateStack(minSize = 1)
) : Navigator(screens, key, stateHolder, disposeBehavior, parent, stack) {

    private val timeStamp = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    init {
        Log.d("@@@", "Init")
        observeTimeStamp()
    }

    private fun observeTimeStamp() {
        scope.launch {
            timeStamp.collectLatest {
                Log.d("@@@", "Before delay")
                delay(resetAfter)
                Log.d("@@@", "After delay")
                if (canPop) {
                    Log.d("@@@", "Reset")
                    popAll()
                }
            }
        }
    }

    override fun push(item: Screen) {
        timeStamp.tryEmit(Unit)
        stack.push(item)
    }

    override fun push(items: List<Screen>) {
        timeStamp.tryEmit(Unit)
        stack.push(items)
    }

    override fun replace(item: Screen) {
        timeStamp.tryEmit(Unit)
        stack.replace(item)
    }

    override fun replaceAll(item: Screen) {
        timeStamp.tryEmit(Unit)
        stack.replaceAll(item)
    }

    override fun replaceAll(items: List<Screen>) {
        timeStamp.tryEmit(Unit)
        stack.replaceAll(items)
    }

    override fun pop(): Boolean {
        if (canPop) {
            timeStamp.tryEmit(Unit)
        }
        return stack.pop()
    }

    override fun popAll() {
        timeStamp.tryEmit(Unit)
        stack.popAll()
    }

    override fun popUntil(predicate: (Screen) -> Boolean): Boolean {
        timeStamp.tryEmit(Unit)
        return stack.popUntil(predicate)
    }

    override fun plusAssign(item: Screen) {
        timeStamp.tryEmit(Unit)
        stack.plusAssign(item)
    }

    override fun plusAssign(items: List<Screen>) {
        timeStamp.tryEmit(Unit)
        stack.plusAssign(items)
    }

    override fun clearEvent() {
        timeStamp.tryEmit(Unit)
        stack.clearEvent()
    }
}
