/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.net.URL;

/**
 *
 * @author varghees
 */
public class MailAttachment {
    private String attachmentPath;
    private URL attachmentURL;
    private String attachDescription;
    private String attachName;

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public URL getAttachmentURL() {
        return attachmentURL;
    }

    public void setAttachmentURL(URL attachmentURL) {
        this.attachmentURL = attachmentURL;
    }

    public String getAttachDescription() {
        return attachDescription;
    }

    public void setAttachDescription(String attachDescription) {
        this.attachDescription = attachDescription;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }
    
}
