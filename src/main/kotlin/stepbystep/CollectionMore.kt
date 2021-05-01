package stepbystep

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        runBlocking {
            launch {
                repeat(3) {
                    delay(100)
                    println("I`m not blocked")
                }
            }


//        simple().collect { value -> println(value) }
            simple2().iterator().forEachRemaining(::println)
        }
    }

    println(time)
}


fun simple(): Flow<Int> = flow {
    repeat(3){
        delay(100)
        emit(it+1)
    }
}

fun simple2():Sequence<Int> = sequence{
    repeat(3){
//        delay(100)
        Thread.sleep(100)
        yield(it)
    }
}