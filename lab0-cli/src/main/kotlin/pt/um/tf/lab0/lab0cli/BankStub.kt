package pt.um.tf.lab0.lab0cli

import io.atomix.catalyst.concurrent.ThreadContext
import io.atomix.catalyst.transport.Address
import io.atomix.catalyst.transport.Connection
import io.atomix.catalyst.transport.Transport
import mu.KLogging
import pt.um.tf.lab0.lab0mes.Message
import pt.um.tf.lab0.lab0mes.Reply
import pt.um.tf.lab0.lab0mes.Bank
import java.util.concurrent.CompletableFuture

class BankStub(private val me: Address, private val t: Transport, private val tc: ThreadContext) : Bank {
    companion object : KLogging()


    private var connF : CompletableFuture<Connection> = CompletableFuture()
    private val conn : Connection

    init {
        tc.execute(this@BankStub::connect).get()
        conn = connF.get()
    }


    private fun connect() {
        connF = t.client().connect(me)
    }

    override fun movement(mov: Int): Boolean {
        val res : CompletableFuture<Boolean> = CompletableFuture()
        tc.execute {
            conn.sendAndReceive<Message, Reply>(Message(1, mov)).thenAccept {
                res.complete(it.denied)
            }
        }
        return res.get()
    }

    override fun balance(): Int {
        val res : CompletableFuture<Int> = CompletableFuture()
        tc.execute {
            conn.sendAndReceive<Message, Reply>(Message()).thenAccept {
                res.complete(it.balance)
                logger.info("Balance incoming ${it.balance}")
            }
        }
        return res.get()
    }

}