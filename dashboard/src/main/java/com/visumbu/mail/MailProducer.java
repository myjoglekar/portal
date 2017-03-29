/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author varghees
 */
public class MailProducer implements Runnable {

    protected BlockingQueue queue = null;
    final static Logger log = Logger.getLogger(MailProducer.class);

    public MailProducer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void accept(Object obj) {
        log.debug("Start function of accept in MailConsumer class");
        try {
            queue.put(obj);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        log.debug("End function of accept in MailConsumer class");
    }

    @Override
    public void run() {
    }
}
