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
        log.debug("Start function of getInstance in MailQ class");
        if (instance == null) {
            synchronized (MailQ.class) {
                instance = new MailQ();
            }
        }
        log.debug("End function of getInstance in MailQ class");
        return instance;
    }

    public void add(Object obj) {
        log.debug("Start function of add in MailQ class");
        producer.accept(obj);
        log.debug("End function of add in MailQ class");
    }

    public int count() {
        log.debug("Start function of count in MailQ class");
        log.debug("End function of count in MailQ class");
        return queue.size();
    }

    public int remainingCapacity() {
        log.debug("Start function of remainingCapacity in MailQ class");
        log.debug("End function of remainingCapacity in MailQ class");
        return queue.remainingCapacity();
    }

    public static void main(String args[]) {
        log.debug("Start Main function in MailQ class");
        MailQ q = MailQ.getInstance();
        log.debug("Start Main function  in MailQ class");
    }
}
