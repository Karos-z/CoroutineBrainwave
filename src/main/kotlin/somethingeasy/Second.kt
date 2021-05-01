package somethingeasy

import kotlinx.coroutines.*

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

@OptIn(ObsoleteCoroutinesApi::class)
fun main() {
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                launch{
//                    withContext(ctx2) {
//                        multiple.log("Working in ctx2")
//                    }
                    log("Working in ctx2")
                }

                log("Back to ctx1")
            }
        }
    }
}