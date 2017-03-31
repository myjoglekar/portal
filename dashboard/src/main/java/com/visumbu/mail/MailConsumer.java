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
        this.queue = queue;
    }

    @Override
    public void run() {
        log.debug("Running of thread in MailConsumer class");
        while (true) {
            if (queue.isEmpty()) {
                queue.poll();
            }

            Object obj = null;
            try {
                obj = (Object) queue.take(); // take the element
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            String status = "";
            // Send mail
            MailProperties props = (MailProperties) obj;
            switch (props.getType()) {
                case Constants.TEXT_MAIL:
                    log.debug("Starting case of TEXT_MAIL  in  MailConsumer class");
                    TextMail mail = new TextMail(props);
                    status = mail.sendMail();
                    log.debug("Ending of thread in MailConsumer class");
                    break;
                case Constants.TEXT_MAIL_WITH_ATTACHMENT:
                    log.debug("Starting case of TEXT_MAIL _WITH_ATTACHMENT in  MailConsumer class");
                    TextMailWithAttachment textMailWithAttachment = new TextMailWithAttachment(props);
                    status = textMailWithAttachment.sendMail();
                    log.debug("Ending of thread in MailConsumer class");
                    break;
                case Constants.HTML_MAIL:
                    log.debug("Starting case of HTML_MAIL  in  MailConsumer class");
                    HtmlMail htmlMail = new HtmlMail(props);
                    status = htmlMail.sendMail();
                    log.debug("Ending of thread in MailConsumer class");
                    break;
                case Constants.HTML_WITH_EMBEDDED_IMAGE:
                    // Dummy function
                    log.debug("Starting case of HTML_WITH_EMBEDDED_IMAGE  in  MailConsumer class");
                    HtmlMailWithEmbeddedImage htmlMailWithEmbeddedImage = new HtmlMailWithEmbeddedImage(props);
                    status = htmlMailWithEmbeddedImage.sendMail();
                    log.debug("Ending of thread in MailConsumer class");
                    break;
            }
        }
    }
}
