package pt.um.tf.lab0.lab0srv;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Account is just an integer based balance.
 */
public class Account {
    private int balance  = 0;

    protected synchronized boolean movement(int mov)  {
        boolean res = true;
        if (mov > 0) {
            balance += mov;
        }
        else {
            if (-mov > balance) {
                res = false;
            }
            else {
                balance += mov;
            }
        }
        return res;
    }

    public int getBalance() {
        return balance;
    }
}