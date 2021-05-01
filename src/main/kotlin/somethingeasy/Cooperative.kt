package somethingeasy

import kotlinx.coroutines.*
import somethingeasy.pojo.Users
import java.io.File

fun main() {
    val userId = 562

    val parent = GlobalScope.launch {
        val dataDeferred = getUserByIdFromNetworkCancelable2(userId, this)
        println("Not cancelled")

        val data = dataDeferred.await()
        println(data)
    }

    Thread.sleep(30)
    parent.cancel()
    while (true){}

//    GlobalScope.launch {
//        val one = somethingeasy.getUserByIdFromNetwork(userId)
//        val two = somethingeasy.readUserFromFile("users.txt")
//
//        if (one.await() in two.await()){
//            println("Find!")
//        }
//    }
//
//    while (true){}
}

private suspend fun getUserByIdFromNetwork(userId: Int): Deferred<Users> {
    return GlobalScope.async {
        println("Retrieving user from network")
        delay(3000)
        println("Still in the coroutine")

        Users(userId,"Sana","Ade")
    }
}

private suspend fun getUserByIdFromNetworkCancelable(userId: Int, parentScope: CoroutineScope) = parentScope.async {
    if (isActive){
        println("child is not canceled")
    }
    println("Retrieving user from network")
    delay(3000)
    println("Still in the coroutine")

    Users(userId,"Sana","Ade")
}

/**
 * 手动取消子协程
 */
private suspend fun getUserByIdFromNetworkCancelable2(userId: Int, parentScope: CoroutineScope) = GlobalScope.async {
    println("Retrieving user from network")
    delay(3000)
    if (!parentScope.isActive){
        println("child is canceled")
//        this.cancel()
        throw CancellationException("User cancel")
    }
    println("Still in the coroutine")

    Users(userId,"Sana","Ade")
}

private fun readUserFromFile(filePath: String) = GlobalScope.async {
    println("Reading the file of user")

   File(filePath).readLines()
       .asSequence()
       .filter { it.isNotEmpty() }
       .map {
           val data = it.split(" ")
           if (data.size == 3) data else emptyList()
       }
       .filter {
           it.isNotEmpty()
       }
       .map {
           Users(it[0].toInt(),it[1],it[2])
       }
       .toList()
}

