package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() = runBlocking {
    val numbers = produceNumbers() // produces integers from 1 and on
    val squares = square(numbers) // squares integers
    repeat(5) {
        println(squares.receive()) // print first five
    }
    println("Done!") // we are done
    coroutineContext.cancelChildren() // cancel children coroutines
}

fun CoroutineScope.produceNumbers() = produce<Int> {
    println("come in produce")
    var x = 1
    while (true) send(x++) // infinite stream of integers starting from 1
}

fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in numbers) send(x * x)
}


//@OptIn(ExperimentalCoroutinesApi::class)
//suspend fun squares(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = coroutineScope {
//    println("come in coroutine")
//    return@coroutineScope produce {
//        for (x in numbers) send(x * x)
//    }
//}

//@OptIn(ExperimentalCoroutinesApi::class)
//suspend fun produceNumberss() = coroutineScope {
//    suspendCoroutine<ReceiveChannel<Int>> {
//        val recv = produce<Int> {
//            var x = 1
//            while (true) send(x++)
//        }
//        it.resume(recv)
//    }
//}
