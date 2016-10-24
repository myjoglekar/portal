/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.net.URL;
import java.util.List;

/**
 *
 * @author varghees
 */
public class MailProperties {

    private int type = Constants.TEXT_MAIL;
    // Server properties
    private String hostName;
    private int port = 465;
    private String authUser;
    private String authPasswd;
    private boolean setSSLOnConnect = true;
    // Mail properties
    private String from;
    private String subject;
    private String txtMessage;
    private String htmlMessage;
    private String to;
    private String cc;
    
    // Attachment properties
    List<MailAttachment> attachment;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getAuthPasswd() {
        return authPasswd;
    }

    public void setAuthPasswd(String authPasswd) {
        this.authPasswd = authPasswd;
    }

    public boolean isSetSSLOnConnect() {
        return setSSLOnConnect;
    }

    public void setSetSSLOnConnect(boolean setSSLOnConnect) {
        this.setSSLOnConnect = setSSLOnConnect;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTxtMessage() {
        return txtMessage;
    }

    public void setTxtMessage(String txtMessage) {
        this.txtMessage = txtMessage;
    }

    public String getHtmlMessage() {
        return htmlMessage;
    }

    public void setHtmlMessage(String htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }
    
    

    public List<MailAttachment> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<MailAttachment> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "MailProperties{" + "type=" + type + ", hostName=" + hostName + ", port=" + port + ", authUser=" + authUser + ", authPasswd=" + authPasswd + ", setSSLOnConnect=" + setSSLOnConnect + ", from=" + from + ", subject=" + subject + ", txtMessage=" + txtMessage + ", htmlMessage=" + htmlMessage + ", to=" + to + ", attachment=" + attachment + '}';
    }
}
