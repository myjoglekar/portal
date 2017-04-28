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
public class MailConsumer implements Runnable {

    protected BlockingQueue queue = null;

    final static Logger log = Logger.getLogger(MailConsumer.class);

    public MailConsumer(BlockingQueue queue) {
        log.debug("Calling of MailConsumer contructor with parameter queue: "+queue);
        this.queue = queue;
    }

    @Override
    public void run() {
        log.debug("Running of thread");
        while (true) {
            if (queue.isEmpty()) {
                queue.poll();
            }

            Object obj = null;
            try {
                obj = (Object) queue.take(); // take the element
            } catch (InterruptedException ex) {
                log.error("Error in queue "+queue+" which catch "+ex);
            }
            String status = "";
            // Send mail
            MailProperties props = (MailProperties) obj;
            switch (props.getType()) {
                case Constants.TEXT_MAIL:
                    TextMail mail = new TextMail(props);
                    status = mail.sendMail();
                    break;
                case Constants.TEXT_MAIL_WITH_ATTACHMENT:
                    TextMailWithAttachment textMailWithAttachment = new TextMailWithAttachment(props);
                    status = textMailWithAttachment.sendMail();
                    break;
                case Constants.HTML_MAIL:
                    HtmlMail htmlMail = new HtmlMail(props);
                    status = htmlMail.sendMail();
                    break;
                case Constants.HTML_WITH_EMBEDDED_IMAGE:
                    // Dummy function
                    HtmlMailWithEmbeddedImage htmlMailWithEmbeddedImage = new HtmlMailWithEmbeddedImage(props);
                    status = htmlMailWithEmbeddedImage.sendMail();
                    break;
            }
        }
    }
}
