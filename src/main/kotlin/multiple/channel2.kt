package multiple

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking



fun main() {
    runBlocking {
        val fruits = produceFruits()
        fruits.consumeEach { println(it) }
        println("Done!")
    }
}

fun produceFruits() = GlobalScope.produce<String> {
    val fruitArray = arrayOf("Apple", "Banana", "Pear", "Grapes","Strawberry")
    for (fruit in fruitArray){
        send(fruit)
        if (fruit == "Pear"){
            close()
        }
    }

}