/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package cafe.adriel.voyager.routing.core.application

import io.ktor.http.Parameters
import io.ktor.util.AttributeKey
import io.ktor.util.Attributes
import io.ktor.util.reflect.TypeInfo

private val RECEIVE_TYPE_KEY: AttributeKey<TypeInfo> = AttributeKey("ReceiveType")

/**
 * A single act of communication between a client and server.
 */
public interface ApplicationCall {
    /**
     * An application being called.
     */
    public val application: Application

    /**
     * Gets a request's URI, including a query string.
     */
    public val uri: String

    /**
     * [Attributes] attached to this call.
     */
    public val attributes: Attributes

    /**
     * Parameters associated with this call.
     */
    public val parameters: Parameters
}

/**
 * Get a request's URL path without a query string.
 */
public fun ApplicationCall.path(): String = uri.substringBefore('?')

/**
 * The [TypeInfo] recorded from the last [call.receive<Type>()] call.
 */
public var ApplicationCall.receiveType: TypeInfo
    get() = attributes[RECEIVE_TYPE_KEY]
    internal set(value) {
        attributes.put(RECEIVE_TYPE_KEY, value)
    }
