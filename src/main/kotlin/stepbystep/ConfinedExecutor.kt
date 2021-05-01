package stepbystep

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * 将协程调度到自定义线程池中
 */
fun main() {
    val executorDispatcher = Executors
        .newWorkStealingPool(2)
        .asCoroutineDispatcher()
    val another = ThreadPoolExecutor(2,4,
        30,TimeUnit.SECONDS,
        LinkedBlockingQueue(),
        CustomThreadFactory(),
        CustomRejectExecutionHandler())

    GlobalScope.launch(context = executorDispatcher/*another.asCoroutineDispatcher()*/) {
        println(Thread.currentThread().name)
    }

    Thread.sleep(50)
//    another.shutdown()
}

class CustomThreadFactory: ThreadFactory{
    private val group: ThreadGroup by lazy {
        System.getSecurityManager()?.threadGroup?:Thread.currentThread().threadGroup
    }

    private val namePrefix: String
    get() = "custom-pool-${POOL_COUNT.incrementAndGet()}-thread-"

    private val threadNumber = AtomicInteger(0)

    override fun newThread(r: Runnable): Thread {
        val t = Thread(group, r,namePrefix + threadNumber.incrementAndGet(),2, true)
        if (t.isDaemon){
            t.isDaemon = false
        }
        if (t.priority!=Thread.NORM_PRIORITY){
            t.priority = Thread.NORM_PRIORITY
        }
        return  t
    }

    companion object{
        val POOL_COUNT = AtomicInteger(0)
    }

}

class CustomRejectExecutionHandler: RejectedExecutionHandler{

    override fun rejectedExecution(r: Runnable?, executor: ThreadPoolExecutor?) {
        throw RejectedExecutionException("Task ${r.toString()} rejected from ${executor.toString()}")
    }

}