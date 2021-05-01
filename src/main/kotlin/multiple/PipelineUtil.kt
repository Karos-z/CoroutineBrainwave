package multiple

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import java.lang.IllegalArgumentException
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMembers

object PipelineUtil {


    @OptIn(ExperimentalCoroutinesApi::class)
    fun <T> PipelineTask<T>.produceItems(sequence: Flow<T>): PipelineTask<T> {
        val ref = GlobalScope.produce<T> {
            sequence.collect {
                send(it)
            }

//            sequence.collect().forEach {
//                send(it)
//            }
        }
        if (ref !in this.channelReferences) this.channelReferences.add(ref)
        return this
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun <T> PipelineTask<T>.isFruit() : PipelineTask<T> {
        val receives = this.channelReferences.last()
        val ref = GlobalScope.produce<T> {
            for (item in receives) {
                if (item is Fruit) {
                    send(item)
                }
            }
        }
        if (ref !in this.channelReferences) this.channelReferences.add(ref)
        return this
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    inline fun <reified T> PipelineTask<T>.isRed() : PipelineTask<T> {
        val result = T::class
        var flag = false
        for (property in result.declaredMembers) {
            println("${property.name} return ${property.returnType}")
            if (property.name == "color" && property.returnType == String::class.createType()) {
//            println("wowowo")
                flag = true
                break
            }
        }

        val receives = this.channelReferences.last()
        val ref = GlobalScope.produce<T> {
            if (flag) {
                for (item in receives) run {
                    val proceed = item!!::class.members.find { it.name == "color" }
                    if (proceed?.call(item) == "Red") {
                        send(item)
                    }
                }
            } else {
                throw IllegalArgumentException()
            }

//    for (item in this@isRed){
//        if ((item as Item).color == "Red")
//        {
//            send(item)
//        }
//    }

        }
        if (ref !in this.channelReferences) this.channelReferences.add(ref)
        return this
    }


    class PipelineTask<T>(val name: String, val channelReferences: ArrayList<ReceiveChannel<T>>){
        var isCancelled = false
        private set

        fun cancelTask(){
            isCancelled = true
            channelReferences.forEach {
                it.cancel()
            }
            channelReferences.clear()
        }
    }

}