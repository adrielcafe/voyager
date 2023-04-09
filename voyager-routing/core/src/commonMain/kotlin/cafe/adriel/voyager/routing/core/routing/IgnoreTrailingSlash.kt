/*
 * Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package cafe.adriel.voyager.routing.core.routing

import cafe.adriel.voyager.routing.core.application.ApplicationCall
import cafe.adriel.voyager.routing.core.application.ApplicationPlugin
import cafe.adriel.voyager.routing.core.application.createApplicationPlugin
import io.ktor.util.AttributeKey

private val IgnoreTrailingSlashAttributeKey: AttributeKey<Unit> = AttributeKey("IgnoreTrailingSlashAttributeKey")

internal var ApplicationCall.ignoreTrailingSlash: Boolean
    get() = attributes.contains(IgnoreTrailingSlashAttributeKey)
    private set(value) = if (value) {
        attributes.put(IgnoreTrailingSlashAttributeKey, Unit)
    } else {
        attributes.remove(IgnoreTrailingSlashAttributeKey)
    }

/**
 * A plugin that enables ignoring a trailing slash when resolving URLs.
 * @see [Application.routing]
 */
public val IgnoreTrailingSlash: ApplicationPlugin<Unit> = createApplicationPlugin("IgnoreTrailingSlash") {
    onCall { call ->
        call.ignoreTrailingSlash = true
    }
}
