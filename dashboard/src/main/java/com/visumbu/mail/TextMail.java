/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 *
 * @author varghees
 */
public class TextMail {

    private MailProperties props = null;

    final static Logger log = Logger.getLogger(TextMail.class);

    public TextMail(MailProperties props) {
        log.debug("Calling TextMail function with parameter " + props);
        this.props = props;
    }

    public String sendMail() {
        log.debug("Calling sendMail function with return type String");

        try {
            Email email = new SimpleEmail();
            email.setHostName(props.getHostName());
            email.setSmtpPort(props.getPort());
            email.setAuthentication(props.getAuthUser(), props.getAuthPasswd());
            //email.setSSLOnConnect(props.isSetSSLOnConnect());
            email.setFrom(props.getFrom());
            email.setSubject(props.getSubject());
            email.setMsg(props.getTxtMessage());
            email.addTo(props.getTo());
            email.addCc(props.getCc());
            return email.send();
        } catch (EmailException ex) {
            log.error("Error in setting of mail properties " + props + " which catch " + ex);
        }
        return "Not Sent";
    }
}
