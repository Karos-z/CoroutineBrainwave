package multiple

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    val ktChannel = Channel<String>(1)
    val data = arrayOf("aaa", "bbb", "ccc","ddd")
    GlobalScope.launch {
        repeat(3){
            val item = ktChannel.receive()
            println(item)
        }
//        ktChannel.cancel()
        ktChannel.close()
        println("skip")
    }
    for (d in data){
        ktChannel.send(d)
    }

    println("Done!")
}