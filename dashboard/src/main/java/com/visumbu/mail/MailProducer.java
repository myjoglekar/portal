/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.concurrent.BlockingQueue;

/**
 *
 * @author varghees
 */
public class MailProducer implements Runnable {

    protected BlockingQueue queue = null;

    public MailProducer(BlockingQueue queue) {
        this.queue = queue;
    }

    public void accept(Object obj) {
        try {
            queue.put(obj);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
    }
}
