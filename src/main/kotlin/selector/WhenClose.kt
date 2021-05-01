package selector

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.*

@OptIn(InternalCoroutinesApi::class)
suspend fun selectAorB(a: ReceiveChannel<String>, b: ReceiveChannel<String>): String =
    select<String> {
        a.onReceiveOrClosed { result ->
            if(result.valueOrNull != null) "a:${result.value}" else "Channel 'a' is closed"
        }
        b.onReceiveOrClosed { result ->
            if(result.valueOrNull != null) "b:${result.value}" else "Channel 'b' is closed"
        }
    }

@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    val a = produce<String> {
        repeat(4) { send("Hello $it") }
    }
    val b = produce<String> {
        repeat(4) { send("World $it") }
    }
    repeat(8) { // print first eight results
        println(selectAorB(a, b))
    }
    coroutineContext.cancelChildren()
}