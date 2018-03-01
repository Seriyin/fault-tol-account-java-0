package pt.um.tf.lab0.lab0cli

import io.atomix.catalyst.concurrent.SingleThreadContext
import io.atomix.catalyst.serializer.Serializer
import io.atomix.catalyst.transport.Address
import io.atomix.catalyst.transport.Connection
import io.atomix.catalyst.transport.netty.NettyTransport
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.sumBy
import kotlinx.coroutines.experimental.channels.toSet
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import pt.um.tf.lab0.lab0mes.Message
import pt.um.tf.lab0.lab0mes.Reply
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom

data class repVal(val movement : Int, val deny : Boolean)

fun main(args : Array<String>) {
    val me = Address("127.0.0.1", 22556)
    val t = NettyTransport()
    val sr = Serializer()
    val tc = SingleThreadContext("cli-%d", sr)
    registerHandler(t.client().connect(me).get())
}

fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(this.start, this.endInclusive)

fun registerHandler(connection: Connection?) {
    connection?.handler<Reply, CompletableFuture<Reply>>(Reply::class.java, handleReply())

    for(i in 0.rangeTo((0..16).random())) {
        var balances = Channel<Int>(16)
        launch {
            val mov = (-200..200).random()
            var accum = 0;
            var channel = Channel<repVal>(5000)
            launch {
                for(i in 0.rangeTo((0..1000000).random())) {
                    val mov = (-200..200).random()
                    launch {
                        var rep = connection?.sendAndReceive<Message, Reply>(Message(1, mov))?.get()
                        if(rep?.res == true) {
                            channel?.send(repVal(mov,false))
                        }
                        else{
                            channel?.send(repVal(mov,true))
                        }
                    }
                }
                channel.close()
            }
            var balance = 0
            for(rep in channel) {
                if(rep.deny) {
                    balance += rep.movement
                }
                else {
                    println("Rejected ${rep.movement}")
                }
            }
            println("Balance ${balance}")
            balances.send(balance)
        }
        balances.close()
        println("Got : ${runBlocking{ balances.sumBy{ it} }}, expected ${connection?.sendAndReceive<Message,Reply>(Message(0, 0))?.get()?.balance}")
    }
}




fun handleReply() : (Reply) -> Unit {
    return {
        when  {
            it.op == 0 -> println("Insufficient funds")
            it.op == 1 -> println("Cur balance {it.balance}")
        }
    }
}


