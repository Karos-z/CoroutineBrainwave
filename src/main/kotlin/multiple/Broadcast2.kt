package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {
    runBlocking {
        val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
        val producer = GlobalScope.launch {
          repeat(3){
              delay(100)
              broadcastChannel.send(it)
          }
            delay(100)
            broadcastChannel.close()
        }

        List(3){
            GlobalScope.launch {
                broadcastChannel.openSubscription().let { channel ->
                    for (i in channel){
                        println("[#$it] receive: $i")
                    }
                }
            }
        }.joinAll()

    }
}