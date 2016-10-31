/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author varghees
 */
public class MailQ {

    private static MailQ instance = null;
    private BlockingQueue queue = new ArrayBlockingQueue(1024);
    private MailProducer producer = null;
    private MailConsumer consumer = null;

    private MailQ() {
        producer = new MailProducer(queue);
        consumer = new MailConsumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public static MailQ getInstance() {
        if (instance == null) {
            synchronized (MailQ.class) {
                instance = new MailQ();
            }
        }
        return instance;
    }

    public void add(Object obj) {
        producer.accept(obj);
    }

    public int count() {
        return queue.size();
    }

    public int remainingCapacity() {
        return queue.remainingCapacity();
    }
    
    public static void main(String args[])
    {
        MailQ q = MailQ.getInstance();
    }
}
