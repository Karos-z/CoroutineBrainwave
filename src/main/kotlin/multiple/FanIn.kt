package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach

/**
 * 有点问题，5号协程不知道是什么原因一直挂起，1号协程也就是runBlocking for循环后就挂起了，互相等待起来了
 */

@ExperimentalCoroutinesApi
fun main() {

    data class Fruit(override val name: String, override val color: String) : Item
    data class Vegetable(override val name: String, override val color: String) : Item

    // ------------ Helper Methods ------------
    fun isFruit(item: Item) = item is Fruit

    fun isVegetable(item: Item) = item is Vegetable


    // Produces a finite number of items
    // which are either a fruit or vegetable
    fun produceItems(): ArrayList<Item> {
        val itemsArray = ArrayList<Item>()
        itemsArray.add(Fruit("Apple", "Red"))
        itemsArray.add(Vegetable("Zucchini", "Green"))
        itemsArray.add(Fruit("Grapes", "Green"))
        itemsArray.add(Vegetable("Radishes", "Red"))
        itemsArray.add(Fruit("Banana", "Yellow"))
        itemsArray.add(Fruit("Cherries", "Red"))
        itemsArray.add(Vegetable("Broccoli", "Green"))
        itemsArray.add(Fruit("Strawberry", "Red"))
        itemsArray.add(Vegetable("Red bell pepper", "Red"))
        return itemsArray
    }


    runBlocking {

        // Initialize the Channels
        val destinationChannel = Channel<Item>()

        val fruitsChannel = Channel<Item>()
        val vegetablesChannel = Channel<Item>()

        // Launch the coroutine in order to produce items that are fruits
        launch {
            produceItems().forEach {
                if (isFruit(it)) {
                    fruitsChannel.send(it)
                }
            }
            fruitsChannel.close()
            println("fruitsChannel has closed:${fruitsChannel.isClosedForSend}")
        }

        // Launch the coroutine in order to produce items that are vegetables
        launch {
            produceItems().forEach {
                if (isVegetable(it)) {
                    vegetablesChannel.send(it)
                }
            }
            vegetablesChannel.close()
            println("vegetablesChannel has closed:${vegetablesChannel.isClosedForSend}")
        }


        // Multiplex values into the common destination channel
        launch {
            for (item in fruitsChannel) {
                destinationChannel.send(item)
            }
            println("1 reach !!!!!!!!")
        }

        // Multiplex values into the common destination channel
        launch {
            for (item in vegetablesChannel) {
                destinationChannel.send(item)
            }
            println("2 reach !!!!!!!!")
            destinationChannel.close()
        }


        // Consume the channel
//        for (it in destinationChannel){
        destinationChannel.consumeEach {
            if (isFruit(it)) {
                println("${it.name} is a fruit")
            } else if (isVegetable(it)) {
                println("${it.name} is a vegetable")
            }
        }
        println("cancel?")
//        // Cancel everything
//        coroutineContext.cancelChildren()


//        val job = GlobalScope.launch {
//            for (it in destinationChannel){
////        destinationChannel.consumeEach {
//                if (isFruit(it)) {
//                    println("${it.name} is a fruit")
//                } else if (isVegetable(it)) {
//                    println("${it.name} is a vegetable")
//                }
//            }
//            println("can't reach")
//        }
//
//        job.join()
    }
}