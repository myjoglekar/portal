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
public class MailConsumer implements Runnable {
    protected BlockingQueue queue = null;

    public MailConsumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
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
