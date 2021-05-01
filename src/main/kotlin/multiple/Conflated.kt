package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach

/**
 * 时间充裕时，所有订阅者依次获得结果。时间紧迫时，会尽量把一个协程走完
 * 建议留足够的时间，就这个例子，按我的经验，每开一个独立调度的协程，下方delay的时间要增加10ms,这10ms用于新协程同公共的ConflatedBroadcastChannel建立联系
 * 时间不够的话，无法保证每个协程都收到最后的结果。现在3个接受者协程，虽然中间数据无法保证，但最后的数据都能收到。
 * 为了保险起见，每开一个协程都留20ms准备
 */
@OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.ObsoleteCoroutinesApi::class)
fun main() {
    val fruitArray = arrayOf("Apple", "Banana", "Pear", "Grapes",
        "Strawberry","ososos","pskxcks","pwkdkpwk")
    // 1
    val kotlinChannel = ConflatedBroadcastChannel<String>()
    runBlocking {
        // 2
        kotlinChannel.apply {
            send(fruitArray[0])
            send(fruitArray[1])
            send(fruitArray[2])
        }
        // 3
        GlobalScope.launch {
            kotlinChannel.consumeEach { value ->
                println("Consumer 1: $value")
            }
        }
        GlobalScope.launch {
            kotlinChannel.consumeEach { value ->
                println("Consumer 2: $value")
            }
        }
        GlobalScope.launch {
            kotlinChannel.consumeEach { value ->
                println("Consumer 3: $value")
            }
        }
        delay(30)
        // 4
        kotlinChannel.apply {
            send(fruitArray[3])
            send(fruitArray[4])
            send(fruitArray[5])
            send(fruitArray[6])
            send(fruitArray[7])
        }
//        delay(10)
        // 6
        kotlinChannel.close()
//        delay(10)
    }
}