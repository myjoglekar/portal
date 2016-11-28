/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author varghees
 */
public class TextMail {

    private MailProperties props = null;

    public TextMail(MailProperties props) {
        this.props = props;
    }

    public String sendMail() {
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
            ex.printStackTrace();
            Logger.getLogger(TextMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Sent";
    }
}
