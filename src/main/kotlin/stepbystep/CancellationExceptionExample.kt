package stepbystep

import kotlinx.coroutines.*
import java.io.IOException

/**
 * CoroutineExceptionHandler只适用于launch或produce启动的协程，对runBlocking和async无效
 * 另外，handler必须安装在顶级协程上
 * CancellationException抛到始祖协程后，就不再继续传播了
 */
fun main() {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught original $exception")
    }
    GlobalScope.launch/*(handler)*/ {
//    runBlocking/*(handler)*/ {
//        val handler = CoroutineExceptionHandler { _, exception ->
//            println("Caught original $exception")
//        }
//    val parentJob = GlobalScope.launch(handler) {
        val parentJob = launch(handler) {
            val childJob = launch {
                // Sub-child job
                launch {
                    // Sub-child job
                    launch {
                        throw IOException()
                    }
                }
            }
            try {
                childJob.join()
            } catch (e: CancellationException) {
                println("Rethrowing CancellationException with original cause")
                throw e
            }
        }

        try {
            parentJob.join()
        } catch (e: CancellationException) {
            println("I see CancellationException cause")
            throw e
        }
    }
    Thread.sleep(1000)
}