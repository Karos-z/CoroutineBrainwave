package multiple

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 没有发生切线程时，父子协程都在当前线程调度，runBlocking开启的是主协程，会先执行
 * 如果runBlocking切换了线程，父子协程都要放到新线程执行，执行顺序是代码的先后顺序
 */
fun main() {
    val fruitArray = arrayOf(
        "Apple", "Banana", "Pear", "Grapes",
        "Strawberry"
    )
    val kotlinChannel = Channel<String>()
    runBlocking(Dispatchers.Unconfined) {
        launch {
            for (fruit in kotlinChannel){
                println("${Thread.currentThread()} Receive: $fruit")
            }
        }
        launch {
            for (fruit in fruitArray) {
                val wasSent = kotlinChannel.offer(fruit)
                if (wasSent) {
                    println("Sent: $fruit")
                } else {
                    println("${Thread.currentThread()} $fruit wasn’t sent")
                }
            }
            kotlinChannel.close()
        }
//        launch {
//            for (fruit in kotlinChannel){
//                println("${Thread.currentThread()} Receive: $fruit")
//            }
//        }
//        for (fruit in kotlinChannel){
//            println("${Thread.currentThread()} Receive: $fruit")
//        }
        println("${Thread.currentThread()} Done!")
    }
}