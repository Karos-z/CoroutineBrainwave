package flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
fun main() {
    val flowOfStrings = channelFlow<String> {
        for (num in 0..100){
            withContext(Dispatchers.Default) {
//            GlobalScope.launch {
                if (num == 10) send("")
                else send("Emitting $num")
            }
        }
    }


    GlobalScope.launch {
        flowOfStrings
            .map { it.split(" ") }
//            .filter { it.size > 1 }
            .map { it[1] }
            .catch {
                it.printStackTrace()
                // send the fallback value or values
//                emitAll(arrayOf("Fallback","ss").asFlow())
                emit("Fallback")
            }
            .flowOn(Dispatchers.Default)
            .collect {
                println(it)
            }
        println("kafka")
    }
    Thread.sleep(1500)
}