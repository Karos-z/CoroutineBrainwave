package stepbystep

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        // 1
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception,${exception.suppressed.contentToString()}")
        }
        // 2
        val job = GlobalScope.launch(exceptionHandler) {
            throw AssertionError("My Custom Assertion Error!")
        }
        // 3
        val deferred = GlobalScope.async(exceptionHandler) {
            // Nothing will be printed,
            // relying on user to call deferred.await()
            throw ArithmeticException()
        }

        joinAll(job,deferred)
    }
}