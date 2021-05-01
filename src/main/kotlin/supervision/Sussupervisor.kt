package supervision

import kotlin.coroutines.*
import kotlinx.coroutines.*


/**
 * supervisorScope和coroutineScope对下方的代码有挂起作用
 */
fun main() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    supervisorScope {
//    coroutineScope {
        val child = launch(handler) {
            println("The child throws an exception")
            throw AssertionError()
        }
        delay(200)
        println("The scope is completing")
    }
    println("The scope is completed")
}
