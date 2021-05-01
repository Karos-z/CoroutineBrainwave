package somethingeasy

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


data class Active(var continuation: Continuation<String>?)

fun main() {
    val kick = Active(null)
    runBlocking{
        launch {
            println("112233")
        }
        launch {
            println("223344")
            println(waitMe(kick))
            delay(2000)
            println("......")

        }
        launch {
            println("33445566")
        }
        delay(4000)
        println("Now I will manual active from outside ${LocalTime.now()}")
        kick.continuation?.resume("manual active")
        println("All can complete ${LocalTime.now()}")
    }
}

suspend fun waitMe(kick: Active) = coroutineScope {
    delay(3000)
    return@coroutineScope suspendCoroutine<String> {
//        it.resumeWith(Result.success("three seconds later"))
        kick.continuation = it
    }
}
