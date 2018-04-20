package pt.um.tf.lab0.lab0cli;

import io.atomix.catalyst.concurrent.Futures;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.um.tf.lab0.lab0mes.Message;
import pt.um.tf.lab0.lab0mes.Reply;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Spamming Client.
 */
public class Main {
  private final Address adr;
  private final Transport t;
  private final int nc;
  private int balance;
  private final BlockingQueue<Integer> q;
  private final Spammer[] sp;
  private BlockingQueue<Object> term;

  private Main() {
    adr = new Address("127.0.0.1", 22556);
    t = new NettyTransport();
    nc = ThreadLocalRandom.current().nextInt(9, 16);
    q = new ArrayBlockingQueue<>(nc+1);
    balance = 0;
    term = new ArrayBlockingQueue<>(1);
    sp = new Spammer[nc];
  }

    public static void main(String args[]) {
      var m = new Main();
      m.run();
    }

  private void run() {
    var l = IntStream.range(0, nc)
                     .mapToObj(this::fireSpammer)
                     .collect(Collectors.toList());
    var tc = runBalancer();
    try {
      term.take();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    for (ThreadContext tcc : l) {
      tcc.close();
    }
    tc.close();
    t.close();
    System.out.println("I'm done");
  }

  private ThreadContext fireSpammer(int i) {
    sp[i] = new Spammer(i, t, adr, q);
    return sp[i].run();
  }

  private ThreadContext runBalancer() {
    var sr = new Serializer();
    sr.register(Reply.class);
    sr.register(Message.class);
    var tc = new SingleThreadContext("cli-%d", sr);
    tc.execute(() -> {
      t.client().connect(adr).thenAccept(this::runBalance);

    }).join();
    return tc;
  }

  private void runBalance(Connection connection) {
    for (int a = 0; a < nc; a++) {
      try {
        balance += q.take();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    connection.handler(Reply.class,this::consumeReply);
    connection.send(new Message((byte) 0, 0));
  }

  private CompletableFuture<Void> consumeReply(Reply r) {
    System.out.println("Got " + balance +
                       ", Expected " + r.getBalance());
    try {
      term.put(new Object());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Futures.completedFuture(null);
  }


}
