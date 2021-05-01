package flows

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.*

/**
 * 进入emit时，collectLatest控制权恢复，有新值到来，取消上一个开启的协程
 * 为什么这里可以直接取消，因为collectLatest里面的代码还在挂起状态，没有占有线程，自然也就没有占有CPU，取消只需要清除相关内存即可。delay是由另外的专门的线程管理的。
 * 对于那些正在运行的占用着线程（CPU）的协程，无法通过直接清内存取消，因为一些运行信息还在寄存器里，直接清内存CPU还按照寄存器寻址，程序就炸了
 * 要么代码中协程自己检查isActive,自己放弃。要么引入挂起点，进入挂起点，保存现场时寄存器里的信息转到了内存，协程不活动，不占CPU，框架取消层级协程时，一看isActive=false，并且还是挂起状态
 * 直接清内存，此协程就没有resume的那一天了
 */
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100) // pretend we are asynchronously waiting 100 ms
        emit(i) // emit next value
    }
}

fun main() = runBlocking<Unit> {
    val time = measureTimeMillis {
        simple()
            .collectLatest { value -> // cancel & restart on the latest value
                println("Collecting $value ${System.currentTimeMillis()}")
                delay(300) // pretend we are processing it for 300 ms
                println("Done $value")
            }
    }
    println("Collected in $time ms")
}