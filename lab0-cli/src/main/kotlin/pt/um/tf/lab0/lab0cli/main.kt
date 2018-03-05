package pt.um.tf.lab0.lab0cli

import io.atomix.catalyst.concurrent.SingleThreadContext
import io.atomix.catalyst.concurrent.ThreadContext
import io.atomix.catalyst.serializer.Serializer
import io.atomix.catalyst.transport.Address
import io.atomix.catalyst.transport.Connection
import io.atomix.catalyst.transport.netty.NettyTransport
import pt.um.tf.lab0.lab0mes.Message
import pt.um.tf.lab0.lab0mes.Reply
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom

data class repVal(val movement : Int, val deny : Boolean)

fun main(args : Array<String>) {
    val me = Address("127.0.0.1", 22556)
    val t = NettyTransport()
    val sr = Serializer()
    val r = (0..16).random()
    val q : BlockingQueue<Int> = ArrayBlockingQueue<Int>(r+1)
    var l : List<ThreadContext> = ArrayList<ThreadContext>(r)
    for(i in 0.rangeTo(r)) {
        val tc = SingleThreadContext("cli-%d", sr)
        sr.register(Reply::class.java);
        sr.register(Message::class.java);
        tc.execute({runSpam(t.client().connect(me).get(), q)})
        l += tc
    }
    val tc = SingleThreadContext("cli-%d", sr)
    sr.register(Reply::class.java);
    sr.register(Message::class.java);
    tc.execute({runBalance(t.client().connect(me).get(), q, r)}).join()
    for(i in 0.rangeTo(r)) l[i].close()
    tc.close()
    t.close()
    println("I'm done")
}

fun runBalance(connection: Connection?, q: BlockingQueue<Int>, r : Int) {
    var sum = 0
    for(i in 0.rangeTo(r)) {
        val balance = q.take()
        println("Balance ${i} ${balance}")
        sum += balance
    }
    println("Got ${sum}, Expected ${connection?.sendAndReceive<Message, Reply>(Message(0, 0))?.get()?.balance}")
}

fun runSpam(connection: Connection?, q: BlockingQueue<Int>) {
    println("Will begin spam")
    var balance = 0
    for(j in 0.rangeTo((0..1000000).random()))
    {
        val mov = (-200..200).random()
        val rep = connection?.sendAndReceive<Message, Reply>(Message(1, mov))?.get()
        if(rep?.res == true) {
            balance += mov
        }
        else{
            println("Rejected ${mov}")
        }
    }
    q.add(balance)
}

fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(this.start, this.endInclusive)


/*
fun handleReply() : (Reply) -> Unit {
    return {
        when  {
            it.op == 0 -> println("Insufficient funds")
            it.op == 1 -> println("Cur balance {it.balance}")
        }
    }
}
*/

