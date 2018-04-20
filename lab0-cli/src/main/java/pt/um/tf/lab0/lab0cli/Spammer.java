package pt.um.tf.lab0.lab0cli;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import pt.um.tf.lab0.lab0mes.Message;
import pt.um.tf.lab0.lab0mes.Reply;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Spams the server with between 200k and 1M random requests.
 */
public class Spammer {
  private int id;
  private final Transport t;
  private final Address adr;
  private final Serializer sr;
  private final ThreadContext tc;
  private int res;
  private final int iters;
  private int left;
  private final BlockingQueue<Integer> collect;


  Spammer(int id, Transport t, Address adr, BlockingQueue<Integer> collect) {
    this.id = id;
    this.t = t;
    this.adr = adr;
    sr = new Serializer();
    sr.register(Reply.class);
    sr.register(Message.class);
    tc = new SingleThreadContext("cli-%d", sr);
    res = 0;
    left = iters = ThreadLocalRandom.current()
                             .nextInt(20000,80000);
    this.collect = collect;
  }


  ThreadContext run() {
    tc.execute(() -> {
      t.client().connect(adr).thenAccept(this::runSpam);
    });
    return tc;
  }

  private void runSpam(Connection connection) {
    System.out.println(id + " Will begin spam of " + iters);
    connection.handler(Reply.class, this::ConsumeReply);
    IntStream.range(0, iters)
             .forEach(a -> spamIter(connection));
  }



  private void ConsumeReply(Reply reply) {
    if(!reply.isDenied()) {
      res += reply.getBalance();
    }
    else {
      System.out.println("Rejected " + reply.getBalance());
    }
    if(left == 1) {
      try {
        System.out.println("Balance from " + id + ": " + res);
        collect.put(res);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    else {
      left--;
    }
  }


  /**
   * Can't join or get inside single thread context.
   * It will block context from executing the complete.
   * @param connection
   * @return
   */
  private void spamIter(Connection connection) {
    var res = 0;
    var m = new Message((byte)1, ThreadLocalRandom.current()
                                                  .nextInt(-200, 200));
    connection.send(m);
  }
}
