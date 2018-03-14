package pt.um.tf.lab0.lab0cli

import io.atomix.catalyst.concurrent.SingleThreadContext
import io.atomix.catalyst.concurrent.ThreadContext
import io.atomix.catalyst.serializer.Serializer
import io.atomix.catalyst.transport.Address
import io.atomix.catalyst.transport.Transport
import io.atomix.catalyst.transport.netty.NettyTransport
import pt.um.tf.lab0.lab0mes.Message
import pt.um.tf.lab0.lab0mes.Reply
import pt.um.tf.lab0.lab0srv.Bank

class BankFactory {
    private val me = Address("127.0.0.1", 22556)
    private val t : Transport = NettyTransport()
    private val l = arrayListOf<ThreadContext>()

    fun newBank() : Bank {
        val sr = Serializer()
        sr.register(Message::class.java)
        sr.register(Reply::class.java)
        val tc : ThreadContext = SingleThreadContext("cli-%d", sr)
        l.add(tc)
        return BankStub(me, t, sr, tc)
    }

    fun closeBanks() {
        l.forEach{ it.close()}
        t.close()
    }
}