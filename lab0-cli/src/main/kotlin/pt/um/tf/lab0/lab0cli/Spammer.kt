package pt.um.tf.lab0.lab0cli

import mu.KLogging
import random
import java.util.concurrent.BlockingQueue

class Spammer(val i: Int, val bf: BankFactory, val q: BlockingQueue<Int>) {
    companion object : KLogging()

    fun execute() {
        val b = bf.newBank()
        val rand = (20000..80000).random()
        var balance = 0
        logger.info("$i Spammer will do $rand iterations")
        (0..rand).forEach {
            val mov = (-200..200).random()
            if (!b.movement(mov)) {
                balance += mov
            }
            else {
                logger.info("Rejected $mov")
            }
        }
        logger.info("$i Spammer finished with $balance")
        q.put(balance)
    }
}
