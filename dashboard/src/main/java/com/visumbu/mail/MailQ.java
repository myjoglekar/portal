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
        producer = new MailProducer(queue);
        consumer = new MailConsumer(queue);

        new Thread(producer).start();
        new Thread(consumer).start();
    }

    public static MailQ getInstance() {
        log.debug("Calling getInstance function in MailQ class");
        if (instance == null) {
            synchronized (MailQ.class) {
                instance = new MailQ();
            }
        }
        log.debug("Ending getInstance function in MailQ class");
        return instance;
    }

    public void add(Object obj) {
        log.debug("Calling add function in MailQ class");
        producer.accept(obj);
    }

    public int count() {
        log.debug("Calling count function in MailQ class");
        return queue.size();
    }

    public int remainingCapacity() {
        log.debug("Calling remainingCapacity function in MailQ class");
        return queue.remainingCapacity();
    }

    public static void main(String args[]) {
        log.debug("Calling Main function in MailQ class");
        MailQ q = MailQ.getInstance();
    }
}
