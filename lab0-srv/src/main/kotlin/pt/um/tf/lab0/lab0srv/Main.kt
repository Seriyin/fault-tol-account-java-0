package pt.um.tf.lab0.lab0srv

import io.atomix.catalyst.concurrent.Futures
import io.atomix.catalyst.concurrent.SingleThreadContext
import io.atomix.catalyst.serializer.Serializer
import io.atomix.catalyst.transport.Address
import io.atomix.catalyst.transport.Connection
import io.atomix.catalyst.transport.netty.NettyTransport
import pt.um.tf.lab0.lab0mes.Message
import pt.um.tf.lab0.lab0mes.Reply
import java.util.concurrent.CompletableFuture


fun main(args : Array<String>) {
    val main = Main()
    main.run()
}

class Main {
    val me = Address("127.0.0.1", 22556)
    val t = NettyTransport()
    val sr = Serializer()
    val tc = SingleThreadContext("srv-%d",sr)
    val acc = Account()

    fun run() : Unit {
        tc.execute({t.server().listen(me, this::handlers)}).join()
        while(readLine() == null);
        tc.close()
        t.close()
        println("I'm here")
    }

    fun handlers(connection: Connection) {
        println("Handling connection")
        connection.handler(Message::class.java, this::handleOp)
    }

    fun handleOp(it : Message) : CompletableFuture<Reply> {
        val res : CompletableFuture<Reply>
        when (it.op) {
            0 -> res = Futures.completedFuture(Reply(it.op, true, acc.balance()))
            1 -> res = Futures.completedFuture((Reply(it.op, !acc.movement(it.mov), 0)))
            else -> res = Futures.exceptionalFuture(IllegalArgumentException("Not recognized Message"))
        }
        return res
    }
}



