/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import com.visumbu.vb.utils.PropertyReader;
import java.util.ArrayList;
import java.util.Iterator;
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

    PropertyReader propReader = new PropertyReader();

    private String HostName = "smtp.hostname";
    private String SetPort = "smtp.port";
    private String AuthUser = "smtp.authuser";
    private String AuthPass = "smtp.authpass";
    private String FromAddress = "smtp.fromaddress";

    public TextMailWithAttachment(MailProperties props) {
        log.debug("Calling TextMailWithAttachment constructor with parameter " + props);
        this.props = props;
    }

//    public String sendMail() {
//        try {
//            // Create the email message
//            MultiPartEmail email = new MultiPartEmail();
//            email.setHostName(props.getHostName());
//            email.setSmtpPort(props.getPort());
//            email.setAuthentication(props.getAuthUser(), props.getAuthPasswd());
//            //email.setSSLOnConnect(props.isSetSSLOnConnect());
//            email.setFrom(props.getFrom());
//            email.setSubject(props.getSubject());
//            email.setMsg(props.getTxtMessage());
//            email.addTo(props.getTo());
//
//            // add attachments
//            List<MailAttachment> attachFiles = props.getAttachment();
//            for (int i = 0; i < attachFiles.size(); i++) {
//                MailAttachment attachFile = attachFiles.get(i);
//                // Create the attachment
//                EmailAttachment attachment = new EmailAttachment();
//                //attachment.setURL(props.getAttachmentURL());
//                attachment.setPath(attachFile.getAttachmentPath());
//                attachment.setDisposition(EmailAttachment.ATTACHMENT);
//                attachment.setDescription(attachFile.getAttachDescription());
//                attachment.setName(attachFile.getAttachName());
//                email.attach(attachment);
//            }
//
//            // send the email
//            return email.send();
//
//        } catch (EmailException ex) {
//            Logger.getLogger(TextMailWithAttachment.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return "Not Sent";
//    }
    public String sendMail(String to, String subject, String message, List<String> attachments) {

        System.out.println("===============================================================>>>>");
        System.out.println(propReader.ReadProperties("smtp.hostname"));
        System.out.println("===============================================================>>>>");
        List<MailAttachment> mailAttachments = new ArrayList<>();
        for (Iterator<String> iterator = attachments.iterator(); iterator.hasNext();) {
            String attachment = iterator.next();
            MailAttachment mailAttachment = new MailAttachment();
            mailAttachment.setAttachDescription("");
            mailAttachment.setAttachmentPath(attachment);
            mailAttachments.add(mailAttachment);
        }
        this.props.setHostName(propReader.ReadProperties(HostName));
        int portNo = Integer.parseInt(propReader.ReadProperties(SetPort));
        this.props.setPort(portNo);
        this.props.setAuthUser(propReader.ReadProperties(AuthUser));
        this.props.setAuthPasswd(propReader.ReadProperties(AuthPass));
        this.props.setFrom(propReader.ReadProperties(FromAddress));
        this.props.setHtmlMessage(message);
        this.props.setTxtMessage(message);
        this.props.setSubject(subject);
        this.props.setTo(to);
        this.props.setAttachment(mailAttachments);
        return sendMail();
    }

    public String sendMail() {
        log.debug("Calling sendMail function with return type String");
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
            String[] toAddressArr = props.getTo().split(",");
            for (int i = 0; i < toAddressArr.length; i++) {
                String to = toAddressArr[i];
                email.addTo(to);
            }

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
            log.error("Error in setting of mail properties " + props + " which catch " + ex);
        }
        return "Not Sent";
    }
}
