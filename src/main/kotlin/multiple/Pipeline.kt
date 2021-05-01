package multiple

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import multiple.PipelineUtil.isFruit
import multiple.PipelineUtil.isRed
import multiple.PipelineUtil.produceItems


interface Item{
    val name: String
    val color: String
}

data class Fruit(
    override val name: String,
    override val color: String,
):Item

data class Vegetable(
    override val name: String,
    override val color: String,
): Item

/**
 * 这里有个坑，for循环中必须直接在ReceiveChannel上迭代，如果for中是用task.channelReferences.last()，框架为你做的自动receive会失效
 * 这里是我封装的一个并行数据处理流水线
 * 可以处理无穷序列，手动取消
 */

fun main() = runBlocking{

    val task = PipelineUtil.PipelineTask<Item>("choose red fruit", ArrayList())


    val pipeline = GlobalScope.launch {

        val itemsArray = ArrayList<Item>()
        itemsArray.add(Fruit("Apple", "Red"))
        itemsArray.add(Vegetable("Zucchini", "Green"))
        itemsArray.add(Fruit("Grapes", "Green"))
        itemsArray.add(Vegetable("Radishes", "Red"))
        itemsArray.add(Fruit("Banana", "Yellow"))
        itemsArray.add(Fruit("Cherries", "Red"))
        itemsArray.add(Vegetable("Broccoli ", "Green"))
        itemsArray.add(Fruit("Strawberry", "Red"))


//        val reds = task.produceItems(itemsArray.asFlow()).isFruit().isRed().channelReferences.last()
        val reds = task
            .produceItems(infiniteFruits(task))
            .isFruit()
            .isRed()
            .channelReferences.last()
        for (item in reds){
            println(item.name)
        }
        println("Done!")
    }
    pipeline.join()
//    delay(5000)
//    task.cancelTask()
}


fun infiniteFruits(task: PipelineUtil.PipelineTask<*>) = flow<Item> {
    var i = 0
    while (!task.isCancelled){
        emit(Fruit("Apple${i}", if(i%2 == 0) "Red" else "Green"))
        i++
        delay(100)
    }
}

