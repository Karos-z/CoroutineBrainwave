package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

/**
 * FIXED_DELAY与FIXED_PERIOD的区别在于计时起点不同，后者从product视角计时，前者从receiver角度计时
 */
fun main() = runBlocking<Unit> {
    val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0, mode = TickerMode.FIXED_DELAY) // create ticker channel
    var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("Initial element is available immediately: $nextElement") // no initial delay

//    nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } // all subsequent elements have 100ms delay
//    println("Next element is not ready in 50 ms: $nextElement")
//
//    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
//    println("Next element is ready in 100 ms: $nextElement")

    // Emulate large consumption delays
    println("Consumer pauses for 150ms")
    delay(250)
    nextElement =  withTimeoutOrNull(1) { tickerChannel.receive() }
    // Next element is available immediately
//    val nextElements = withTimeoutOrNull(50) {
////        tickerChannel.receive()
//        Pair<Unit?,Unit?>(tickerChannel.receive(),tickerChannel.receive())
//    }
    println("Next element is available immediately after large consumer delay: $nextElement")
    // Note that the pause between `receive` calls is taken into account and next element arrives faster
    nextElement = withTimeoutOrNull(110) { tickerChannel.receive() }
    println("Next element is ready in 30ms after consumer pause in 200ms: $nextElement")

    tickerChannel.cancel() // indicate that no more elements are needed
}
