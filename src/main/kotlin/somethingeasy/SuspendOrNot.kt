package somethingeasy

import kotlinx.coroutines.*

fun main() = runBlocking {
    println(1)
    println(Thread.currentThread())
//    println(2)
//    launch {
//        println(3)
//    }
//    println(4)
    newFunc1(this)
//    newFunc2()
    println(5)
}


suspend fun newFunc1(scope: CoroutineScope) = scope.let{
    println(Thread.currentThread())
    println(2)
    it.launch {
//        delay(200)
        println(3)
    }
//    delay(200)
    println(4)
}

suspend fun newFunc2() = coroutineScope{
    println(Thread.currentThread())
    println(2)
    launch {
//        delay(200)
        println(3)
    }
//    delay(200)
    println(4)
}

suspend fun newFunc3() {
    println(2)
    GlobalScope.launch {
        println(3)
    }
    println(4)
}