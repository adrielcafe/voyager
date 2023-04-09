/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.application.debug

import io.ktor.util.debug.plugins.PluginTraceElement
import io.ktor.util.debug.plugins.PluginsTrace
import io.ktor.util.debug.useContextElementInDebugMode

internal suspend fun ijDebugReportHandlerStarted(pluginName: String, handler: String) {
    useContextElementInDebugMode(PluginsTrace) { trace ->
        trace.eventOrder.add(PluginTraceElement(pluginName, handler, PluginTraceElement.PluginEvent.STARTED))
    }
}

internal suspend fun ijDebugReportHandlerFinished(pluginName: String, handler: String) {
    useContextElementInDebugMode(PluginsTrace) { trace ->
        trace.eventOrder.add(PluginTraceElement(pluginName, handler, PluginTraceElement.PluginEvent.FINISHED))
    }
}
