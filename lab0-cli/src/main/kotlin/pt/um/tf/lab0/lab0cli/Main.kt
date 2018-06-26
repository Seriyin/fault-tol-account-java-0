import pt.um.tf.lab0.lab0cli.BankFactory
import pt.um.tf.lab0.lab0cli.Spammer
import java.util.concurrent.*
import kotlin.system.exitProcess

fun main(args : Array<String>) {
    val bf = BankFactory()
    val r = (2..ForkJoinPool.getCommonPoolParallelism()).random()
    var balance = 0
    val q : BlockingQueue<Int> = ArrayBlockingQueue<Int>(r)
    repeat(r) {
        val sp = Spammer(it, bf, q)
        ForkJoinPool.commonPool().execute(sp::execute)
    }
    repeat(r) {
        balance += q.take()
    }
    val b = bf.newBank()
    println("Got $balance, Expected ${b.balance()}")
    bf.closeBanks()
    println("I'm done")
    exitProcess(0)
}

fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(this.start, this.endInclusive)

