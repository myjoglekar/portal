/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author varghees
 */
public class MailQ {

    private static MailQ instance = null;
    private BlockingQueue queue = new ArrayBlockingQueue(1024);
    private MailProducer producer = null;
    private MailConsumer consumer = null;

    final static Logger log = Logger.getLogger(MailQ.class);

    private MailQ() {
        log.debug("Calling Mailq constructor");
        producer = new MailProducer(queue);
        consumer = new MailConsumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public static MailQ getInstance() {
        log.debug("Calling getInstance function with return type MailQ");
        if (instance == null) {
            synchronized (MailQ.class) {
                instance = new MailQ();
            }
        }
        return instance;
    }

    public void add(Object obj) {
        log.debug("Calling add function");
        producer.accept(obj);
    }

    public int count() {
        log.debug("Calling count function with return type int");
        return queue.size();
    }

    public int remainingCapacity() {
        log.debug("Calling remainingCapacity function with return type int");
        return queue.remainingCapacity();
    }

    public static void main(String args[]) {
        log.debug("Calling Main function");
        MailQ q = MailQ.getInstance();
    }
}
