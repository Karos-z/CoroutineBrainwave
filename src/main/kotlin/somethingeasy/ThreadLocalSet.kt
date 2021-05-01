package somethingeasy

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

val threadLocal: ThreadLocal<AtomicInteger> = ThreadLocal.withInitial { AtomicInteger(0) } // declare thread-local variable

@OptIn(ObsoleteCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    println("Pre-multiple.main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    val job =
        launch(Dispatchers.Default + threadLocal.asContextElement(value = AtomicInteger(1))) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
//            somethingeasy.getThreadLocal.get()?.addAndGet(1)
            threadLocal.set(AtomicInteger(2))//错误示范
            withContext(newSingleThreadContext("rpga")){
                println(threadLocal.get())
            }
// 协程恢复后(因为有线程切换的可能)threadLocal会被重置，所以如果后文中有调用挂起函数的需要，不要直接set线程本地变量，先拿到所持有的可变容器的引用，通过引用改值，不改变引用本身，
// threadLocal重置后里面的内容还是最初传入的那个，但引用指向的内存已经更改
//            yield()
//            somethingeasy.jumpOut()
            println("After modify, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")

//            somethingeasy.getThreadLocal.remove()
//            launch {
//                println("new launch start, current thread: ${Thread.currentThread()}, thread local value: '${somethingeasy.getThreadLocal.get()}'")
//            }
//            launch(somethingeasy.getThreadLocal.asContextElement(value = AtomicInteger(1))) {
//                println("new launch start, current thread: ${Thread.currentThread()}, thread local value: '${somethingeasy.getThreadLocal.get()}'")
//            }
//            yield()
//            delay(10)
//            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${somethingeasy.getThreadLocal.get()}'")
        }
    job.join()
    threadLocal.set(threadLocal.get().also { it.addAndGet(10) })
    println("Post-multiple.main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
}

suspend fun jumpOut() = coroutineScope {
//    launch(newSingleThreadContext("fff")) {  }
}

