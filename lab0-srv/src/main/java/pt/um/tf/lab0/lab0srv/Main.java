package pt.um.tf.lab0.lab0srv;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.um.tf.lab0.lab0mes.Message;
import pt.um.tf.lab0.lab0mes.Reply;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class Main {
    public static void main(String args[]) throws IOException {
        Address me = new Address("127.0.0.1", 22556);
        Transport t = new NettyTransport();
        Serializer sr = new Serializer();
        sr.register(Message.class);
        sr.register(Reply.class);
        ThreadContext tc = new SingleThreadContext("srv-%d", sr);
        Account acc = new Account();
        tc.execute(() -> t.server().listen(me, c -> handlers(c, acc))).join();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (!in.readLine().equals("q")) ;
        tc.close();
        t.close();
        System.out.println("I'm done");
    }

    public static void handlers(Connection connection, Account acc) {
        connection.handler(Message.class, (Consumer<Message>)  m -> handleOp(m, connection, acc));
    }

    public static void handleOp(Message m, Connection conn, Account acc)
    {
        System.out.println("Handling connection");
        if (m.getOp()==0) {
            conn.send(new Reply(m.getOp(), true, acc.getBalance()));
        } else if(m.getOp()==1) {
            conn.send(new Reply(m.getOp(), acc.movement(m.getMov()), 0));
        }
    }

}

