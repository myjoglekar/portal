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
        log.error("Calling MailProducer constructor with parameter queue: "+queue);
        this.queue = queue;
    }

    public void accept(Object obj) {
        log.debug("Calling accept function with parameter " + obj);
        try {
            queue.put(obj);
        } catch (InterruptedException ex) {
            log.error("Error in adding obj in queue" + obj + " which catch " + ex);
        }
    }

    @Override
    public void run() {
    }
}
