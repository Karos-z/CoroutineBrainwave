package std

import kotlin.coroutines.*

fun main() {

    var joker: Int? = 0
    /*
    可以把这段代码想象为定义了一个lambda。只是将要做什么记录下来，但里面代码不会立即执行
     */
    val continuation = suspend {
        println("In Coroutine.")
        5
    }.createCoroutine(object : Continuation<Int> {/*
    这里的代码相当于定义了一个回调，当上述lambda执行完成后，触发回调，将结果以及控制权调度会lambda的调用处
    */
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End: $result")
            joker = result.getOrNull()
        }
    })

    /*
    调用lambda，交出控制权
    resume返回后，收回控制权
     */
    continuation.resume(Unit)
    println("joker is $joker")

    val later = object : Continuation<Int> {
        override val context: CoroutineContext
            get() = EmptyCoroutineContext

        override fun resumeWith(result: Result<Int>) {
            println("Coroutine End: $result")
            joker = result.getOrNull()
        }

    }
    suspend {
        println("In Coroutine.")
        6
    }.startCoroutine(later)

    println("joker is $joker")
    later.resume(1)
}