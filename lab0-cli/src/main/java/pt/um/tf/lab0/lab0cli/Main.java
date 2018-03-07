package pt.um.tf.lab0.lab0cli;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.um.tf.lab0.lab0mes.Message;
import pt.um.tf.lab0.lab0mes.Reply;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Spamming Client.
 */
public class Main {
  private final Address adr;
  private final Transport t;
  private final int nc;
  private final BlockingQueue<Integer> q;

  private Main() {
    adr = new Address("127.0.0.1", 22556);
    t = new NettyTransport();
    nc = ThreadLocalRandom.current().nextInt(2, 7);
    q = new ArrayBlockingQueue<>(nc+1);
  }

    public static void main(String args[]) {
      Main m = new Main();
      m.run();
    }

  private void run() {
    List<ThreadContext> l = IntStream.range(0, nc)
                                     .mapToObj(this::fireSpammer)
                                     .collect(Collectors.toList());
    Serializer sr = new Serializer();
    sr.register(Reply.class);
    sr.register(Message.class);
    ThreadContext tc = new SingleThreadContext("cli-%d", sr);
    tc.execute(() -> {
      try {
        runBalance(t.client().connect(adr).get(), q, nc);
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }).join();
    IntStream.range(0,nc+1).forEach(i -> l.get(i).close());
    tc.close();
    t.close();
    System.out.println("I'm done");
  }

  private ThreadContext fireSpammer(int i) {
        Serializer sr = new Serializer();
        sr.register(Reply.class);
        sr.register(Message.class);
        io.atomix.catalyst.concurrent.ThreadContext tc = new SingleThreadContext("cli-%d", sr);
        tc.execute(() -> {
          try {
            runSpam(t.client().connect(adr).get(), q);
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
        });
        return tc;
    }

    private void runBalance(Connection connection, BlockingQueue<Integer> q , int r)
        throws ExecutionException, InterruptedException {
        int sum = q.stream().mapToInt(a -> {System.out.println("Balance: " + a);return a;}).sum();
        System.out.println("Got " + sum +
                           ", Expected " +
                           ((Reply) connection.sendAndReceive(new Message((byte) 0, 0))
                                              .get())
                                              .getBalance());
    }

    private void runSpam(Connection connection, BlockingQueue<Integer> q) {
        System.out.println("Will begin spam");
        int balance = IntStream.range(0, ThreadLocalRandom.current()
                                            .nextInt(20000,1000000))
                               .map(a -> {
                                 try {
                                   return spamIter(connection);
                                 } catch (ExecutionException | InterruptedException e) {
                                   e.printStackTrace();
                                   return 0;
                                 }
                               })
                               .sum();
        q.add(balance);
    }

    private int spamIter(Connection connection)
        throws ExecutionException, InterruptedException {
      int res = 0;
      Message m = new Message((byte)1, ThreadLocalRandom.current()
                                                        .nextInt(-200, 200));
      Reply r = (Reply) connection.sendAndReceive(m).get();
      if(!r.isDenied()) {
        res = r.getBalance();
      }
      else {
        System.out.println("Rejected " + m.getMov());
      }
      return res;
    }

}
