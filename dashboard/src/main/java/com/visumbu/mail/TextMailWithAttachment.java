/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.List;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.log4j.Logger;

/**
 *
 * @author varghees
 */
public class TextMailWithAttachment {

    private MailProperties props = null;
    
    final static Logger log = Logger.getLogger(TextMailWithAttachment.class);

    public TextMailWithAttachment(MailProperties props) {
        this.props = props;
    }

    public String sendMail() {
        log.debug("Start function of sendMail in TextMailWithAttachment class");
        try {
            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(props.getHostName());
            email.setSmtpPort(props.getPort());
            email.setAuthentication(props.getAuthUser(), props.getAuthPasswd());
            //email.setSSLOnConnect(props.isSetSSLOnConnect());
            email.setFrom(props.getFrom());
            email.setSubject(props.getSubject());
            email.setMsg(props.getTxtMessage());
            email.addTo(props.getTo());

            // add attachments
            List<MailAttachment> attachFiles = props.getAttachment();
            for (int i = 0; i < attachFiles.size(); i++) {
                MailAttachment attachFile = attachFiles.get(i);
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                //attachment.setURL(props.getAttachmentURL());
                attachment.setPath(attachFile.getAttachmentPath());
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription(attachFile.getAttachDescription());
                attachment.setName(attachFile.getAttachName());
                email.attach(attachment);
            }

            // send the email
            return email.send();

        } catch (EmailException ex) {
            log.error("EmailException in sendMail function: "+ex);
        }
                log.debug("End function of sendMail in TextMailWithAttachment class");
        return "Not Sent";
    }
}
