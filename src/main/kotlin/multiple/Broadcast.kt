package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {
    val fruitArray = arrayOf("Apple", "Banana", "Pear", "Grapes",
        "Strawberry")
    // 1
    val kotlinChannel = BroadcastChannel<String>(3)
    runBlocking {
        // 2
        kotlinChannel.apply {
            send(fruitArray[0])
            send(fruitArray[1])
            send(fruitArray[2])
        }
        //3 Consumers
        val j1 = GlobalScope.launch {
            // 4
            val channel = kotlinChannel.openSubscription()
            for (value in channel) {
                println("Consumer 1: $value")
            }
                // 6
            }
        val j2 = GlobalScope.launch {
            val channel = kotlinChannel.openSubscription()
            for (value in channel) {
                println("Consumer 2: $value")
            }
        }
        // 7
        delay(100)
        kotlinChannel.apply {
            send(fruitArray[3])
            send(fruitArray[4])
            println("five")
        }
        // 8
        // 9
        kotlinChannel.close()
        joinAll(j1,j2)
    }
}
