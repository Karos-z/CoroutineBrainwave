package somethingeasy

import kotlinx.coroutines.*

//Accessing the thread-local without corresponding context element leads to undefined value:

val tl: ThreadLocal<String> = ThreadLocal.withInitial { "initial" }

fun main() =
//        runBlocking(Dispatchers.Unconfined) {
//    runBlocking {
    runBlocking(Dispatchers.Unconfined + tl.asContextElement()) {
            println("${Thread.currentThread()}, ${tl.get()}") // Will print "initial"
            // Change context
            withContext(newSingleThreadContext("babiq")+ tl.asContextElement("modified")) {
                println("${Thread.currentThread()}, ${tl.get()}") // Will print "modified"
            }
            // Context is changed again
            println("${Thread.currentThread()}, ${tl.get()}") // <- WARN: can print either "modified" or "initial"
        }


//to fix this behaviour use runBlocking(somethingeasy.getTl.asContextElement())