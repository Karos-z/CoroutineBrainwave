package stepbystep

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    fun context(): CoroutineContext
}