package stepbystep

import kotlinx.coroutines.*

fun main() {
    val defaultDispatcher = Dispatchers.Default
    val coroutineErrorHandler = CoroutineExceptionHandler { context, error ->
        println("Problems with Coroutine: $error") // we just print the error here
    }
    val emptyParentJob = Job()

    /**
     * 同种类型的协程上下文元素相加，后面的会覆盖前面的
     */
    val combinedContext = coroutineErrorHandler + emptyParentJob + Dispatchers.Unconfined + defaultDispatcher
    GlobalScope.launch(context = combinedContext) {
        println(Thread.currentThread().name)
    }
    Thread.sleep(50)
}