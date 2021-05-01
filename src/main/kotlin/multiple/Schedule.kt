package multiple

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit>(Dispatchers.Default) {
    val channel = Channel<String>()

//    val a29MM = launch {
//        repeat(3) {
//            val x = channel.receive()
//            multiple.log(x)
//        }
//    }
    val dogAlpha = launch {
        channel.send("A1")
        channel.send("A2")
        somethingeasy.log("A done")
    }
    val dogBeta = launch {
        channel.send("B1")
//        channel.send("B2")
        somethingeasy.log("B done")
    }
    val a23MM = launch {

        repeat(3) {
            val x = channel.receive()
            somethingeasy.log(x)
        }
    }
}


fun log(message: Any?) {
    println("[${Thread.currentThread().name}] $message")
}


//receive 3
//[multiple.main] A1
//[multiple.main] B1
//[multiple.main] A done
//[multiple.main] B done
//[multiple.main] A2











//receive 4
//[multiple.main] A1
//[multiple.main] B1
//[multiple.main] A done
//[multiple.main] A2
//[multiple.main] B2
//[multiple.main] B done


//多线程的情况之一
//[DefaultDispatcher-worker-4] A1
//[DefaultDispatcher-worker-4] B1
//[DefaultDispatcher-worker-3] B done
//[DefaultDispatcher-worker-4] A2
//[DefaultDispatcher-worker-4] A done