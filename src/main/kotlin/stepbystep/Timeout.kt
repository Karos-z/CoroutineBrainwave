package stepbystep

import kotlinx.coroutines.*

/**
 * 超时后释放资源, withTimeout会挂起当前协程
 */
var acquired = 0 //所以协程都是在主线程内调度的，不需要用线程安全的数据结构

class Resource {
    init { acquired++ } // Acquire the resource
    fun close() { acquired-- } // Release the resource
}

fun mains() {
    runBlocking {
        repeat(10_000) { // Launch 100K coroutines
            launch {
                var resource: Resource? = null
                try {
                    resource = withTimeout(60) { // Timeout of 60 ms
                        delay(50) // Delay for 50 ms
                        Resource() // Acquire a resource and return it from withTimeout block
                    }
                }finally {
                    resource?.close() // Release the resource
                }


            }
        }
    }
    // Outside of runBlocking all coroutines have completed
    println(acquired) // Print the number of resources still somethingeasy.getAcquired


//    runBlocking {
//        repeat(100_000) { // Launch 100K coroutines
//            launch {
//                var resource: somethingeasy.Resource? = null
//                try {
//                    withTimeout(60) { // Timeout of 60 ms
//                        delay(55) // Delay for 50 ms
//                        resource = somethingeasy.Resource() // Acquire a resource and return it from withTimeout block
//                    }
//                }finally {
//                    resource?.close() // Release the resource
//                }
//
//
//            }
//        }
//    }
//    // Outside of runBlocking all coroutines have completed
//    println(somethingeasy.getAcquired) // Print the number of resources still somethingeasy.getAcquired
}

fun main() = runBlocking {
    var result:String
    try {
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("$i. Crunching numbers [Beep.Boop.Beep]...")
                delay(500L)
            }
            result = "Done" // will get canceled before it produces this result
        }
    }catch (e: TimeoutCancellationException){
        result = ""
        println("ijhuhh")
    }
//     Result will be `null`
    println("Result is $result")
}