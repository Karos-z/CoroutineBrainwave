package multiple

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


/**
 * Alpha和Beta去果园偷水果
 */
fun main() {
    val fruitArray = arrayOf("Apple", "Banana", "Pear", "Grapes",
        "Strawberry")
    val kotlinChannel = Channel<String>()
    val Beta = runBlocking {
        val Alpha = launch {
            for (fruit in fruitArray) {
                try {
                    kotlinChannel.send(fruit)
                } catch (e: Exception) {
                    println("Exception raised: ${e.javaClass.simpleName}")
                }
            }
            println("Done!")
        }
        repeat(fruitArray.size - 1) {
            val fruit = kotlinChannel.receive()
            // Conditional close
            if (fruit == "Grapes") {
                // Signal that closure of channel
                kotlinChannel.close()
            }
            println(fruit)
        }
    }
}