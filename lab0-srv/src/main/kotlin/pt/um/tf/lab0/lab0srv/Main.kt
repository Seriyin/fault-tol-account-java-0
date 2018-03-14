package pt.um.tf.lab0.lab0srv

import io.atomix.catalyst.concurrent.Futures
import io.atomix.catalyst.concurrent.SingleThreadContext
import io.atomix.catalyst.serializer.Serializer
import io.atomix.catalyst.transport.Address
import io.atomix.catalyst.transport.Connection
import io.atomix.catalyst.transport.Transport
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
    val t : Transport = NettyTransport()
    val sr = Serializer()
    val tc = SingleThreadContext("srv-%d",sr)
    val acc = Account()

    fun run() {
        sr.register(Message::class.java)
        sr.register(Reply::class.java)
        tc.execute({t.server().listen(me, this::handlers)})
        while(readLine() == null);
        tc.close()
        t.close()
        println("I'm here")
    }

    private fun handlers(connection: Connection) {
        println("Handling connection")
        connection.handler(Message::class.java, this::handleOp)
    }

    private fun handleOp(it : Message) : CompletableFuture<Reply> {
        println("Handle Message")
        return when (it.op) {
            0 -> Futures.completedFuture(Reply(it.op, true, acc.balance()))
            1 -> Futures.completedFuture((Reply(it.op, acc.movement(it.mov), it.mov)))
            else -> Futures.exceptionalFuture(IllegalArgumentException("Not recognized Message"))
        }
    }
}



