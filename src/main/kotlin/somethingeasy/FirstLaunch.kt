package somethingeasy

import kotlinx.coroutines.*
import somethingeasy.pojo.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() {

    /**
     * 引入协程作用域的快捷方法，开启一个调度于当前线程（默认情况）的协程作用域，背后是一个event-loop在不断检测其上协程是否complete，阻塞住所在线程，避免直接结束
     */
//    println("Blocking is coming...")
//    runBlocking {
//        GlobalScope.launch { //GlobalScope有没有很重要，它决定了内外协程作用域有没有父子关系
//            delay(2000)
//            println("a new coroutine")
//        }
//        delay(1000)
//
//        println("eventually finish.")
//    }
//    println("when turn to me?")


//    return runBlocking<Unit> {
//        val job = GlobalScope.launch(start= CoroutineStart.LAZY) { // launch a new coroutine and keep a reference to its Job
//            delay(1000L)
//            println("World!")
//        }
//        print("Hello,")
////        job.start()
//        job.join() // wait until child coroutine completes
//    }

    /**
     * 与runBlocking有挂起与非挂起的区别，所以不能用在常规函数里
     */
//    return coroutineScope<Int> {
//        1
//    }




    /**
     * 在协程内部发起的子协程，默认配置立即调度，不会挂起父协程
     */
//    GlobalScope.launch {
//        launch {
//            delay(300)
//            println("niubiplus")
//        }
//      delay(200)
//      println("aha? ")
//    }
//    print("Hello ")
//    Thread.sleep(2000L)

//    (1..100000).forEach {
//        GlobalScope.launch {
//            println("$it printed on the thread ${Thread.currentThread().name}")
//        }
//    }
//    Thread.sleep(1000)

    /**
     * start 可以从一般作用域调用  join被suspend修饰，只能从协程作用域调用
     */
//    val job1 = GlobalScope.launch(start = CoroutineStart.LAZY) {
//        delay(200)
//        println("Pong")
//        delay(200)
//    }
//    val job2 = GlobalScope.launch(start = CoroutineStart.LAZY) {
//        delay(100)
//        print("Ping ")
//        delay(200)
//    }
//    GlobalScope.launch {
//        job1.join()
//    }
//    job2.start()
//    Thread.sleep(1000)

    /**
     *job1和匿名job是同级关系，不是父子关系，因此在匿名job代码块中用join切协程到job1时，匿名job会挂起等job1 complete
     * 相当于匿名job依赖job1
     */
//    val job1 = GlobalScope.launch(start = CoroutineStart.LAZY) {
//        delay(200)
//        println("Pong")
//        delay(200)
//    }
//    GlobalScope.launch {
//        delay(200)
//        println("Ping")
////        job1.join()
//        job1.start()
//        println("Ping")
//        delay(200)
//    }
//    Thread.sleep(1000)

    /**
     *此时匿名job引用着子协程，父子之间理论上是并行执行的,context = pJob显式的表面coroutineScope的继承关系，例子一则是默认隐含的
     */
//    with(GlobalScope){
//        val pJob = launch {
//            delay(200)
//            println("I am parent")
//            delay(200)
//        }
//        launch(context = pJob){
//            delay(200)
//            println("I am child")
//            delay(200)
//        }
//        if (pJob.children.iterator().hasNext()){
//            println("The Job has children ${pJob.children}")
//        }
//    }

    /**
     * 也可以用while循环
     */
//    var isDoorOpen = false
//
//    println("Unlocking the door, please wait.\n")
//
//    with(GlobalScope){
//        launch {
//            delay(3000)
//
//            isDoorOpen = true
//        }
//
//        launch {
//            repeat(4){
//                println("Trying open the door...\n")
//
//                delay(800)
//                if (isDoorOpen){
//                    println("Opened door!\n")
//
//                }else{
//                    println("failed open")
//                }
//            }
//        }
//    }
//
//    Thread.sleep(5000)

    /**
     * async/await 的使用，suspendCoroutine可以用来转换回调api的写法
     * 初学误区：协程并不意味着消除了所有等待，只是减少了不必要的等待，所以await时如果结果没有准备好，下方的代码会阻塞不会走下去
     */
//    GlobalScope.launch {
//        val user =async { somethingeasy.getUserSuspend("51254") }
////        val user = somethingeasy.getUserSuspendss("icxiohc")
//        println("Wait 3 seconds")
//        println(user.await())
//        println(user.getCompleted())
////        println("Wait 3 seconds..")
//    }
//    println("Wait 3 seconds.")
//    Thread.sleep(3500)

    /**
     * 默认的协程API，父协程的取消会导致子协程的取消
     */
//    val parent = GlobalScope.launch(start = CoroutineStart.LAZY) {
//        delay(2000)
//        println("I am parent, spent less time.${System.currentTimeMillis()}")
//    }
//
//    val child = GlobalScope.launch(context = parent) {
//        delay(3000)
//        println("I am child, spent more time.${System.currentTimeMillis()}")
//    }
//
//    GlobalScope.launch {
//        parent.start()
//        delay(2500)
//        parent.cancel()
//    }

    /**
     * 一个容易误解的，其实这里不存在父子关系
     * 协程可以调用cancel()从内部取消，只不过cancel()是非阻塞式调用，只是在递归设置isActive属性，不会强行取消协程，相当于只是发一个信号。
     * 下面的打印语句直接就执行了，导致看上去没有成功取消，原因是协程的取消是通过检查isActive，抛CancellationException实现的
     * 想要立即取消，就得手动抛出CancellationException,见下一个章节
     * 从挂起点恢复时，框架会自动检查当前要恢复的协程的isActive属性，若为false会抛出CancellationException,后面的代码就不再执行了
     * 如果要取消没有挂起点的耗时协程，只能在代码执行时插入isActive检查，为false时手动抛异常
     */

    runBlocking {
        val job = launch {
            GlobalScope.launch {
                delay(3000)
                println("I am not a child")
            }
            delay(2000)
            println("I am parent, spent less time.${System.currentTimeMillis()}")

            delay(600)
//            this.cancel()
//            yield()
            repeat(100_000){
                println(it)
            }
            println("I would not be print")
        }
        delay(2700)
        job.cancel()
        delay(1000)
    }

}

suspend fun getUserSuspend(userId: String): User {
    delay(3000)
//    launch{} //此处没有可以继承的协程作用域，不能直接使用launch启动新的协程
    return User(userId,"karos",)
}

suspend fun getUserSuspends(userId: String)= coroutineScope {
    delay(3000)
//    launch {  } //此处因为外部包裹有coroutineScope，可以在这个挂起函数中直接开新的协程
    return@coroutineScope User(userId,"karos",)
}

suspend fun getUserSuspendss(userId: String): User {
    delay(1000)
    return suspendCoroutine<User> {
        Thread.sleep(3000)
        it.resume(User(userId, "sdjhi",))
    }
}


/**
 * coroutineScope有挂起协程的作用，试试调换它与scope.launch的位置
 */
suspend fun hangSomething(scope: CoroutineScope){

    coroutineScope { // Creates a coroutine scope
        launch {
            delay(500L)
            println("!!!Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope") // This line will be printed before the nested launch
    }

    scope.launch {
        delay(200L)
        println("Task from runBlocking")
    }


    println("Coroutine scope is over")
}


/**
 * 为线程分配CPU时间，为协程分配线程时间
 */


/**
 * 检查标记位，协作方式取消,手动检查或者引入一个挂起点
 */
//fun multiple.main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    val job = launch(Dispatchers.Default) {
//        var nextPrintTime = startTime
//        var i = 0
//        while (i < 5 && isActive) { // computation loop, just wastes CPU
//            //yield()
//            // print a message twice a second
//            if (System.currentTimeMillis() >= nextPrintTime) {
//                //delay(1)
//                println("job: I'm sleeping ${i++} ...")
//                nextPrintTime += 500L
//            }
//        }
//    }
//    delay(1300L) // delay a bit
//    println("multiple.main: I'm tired of waiting!")
//    job.cancelAndJoin() // cancels the job and waits for its completion
//    println("multiple.main: Now I can quit.")
//}

