import kotlin.system.measureTimeMillis

fun main() {
    val time = measureTimeMillis {
        repeat(100_000){
        println(it)
     }
    }
    println(time)

}