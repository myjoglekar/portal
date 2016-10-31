/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

/**
 *
 * @author vsamraj
 */
public class Mailer {
/*
    public Boolean triggerEmailToGroup(Integer projectId, UserTicket userTicket, Email email) {
        Boolean returnVal = false;
        String toAddress = email.getRecepient();
        String emailTemplate = email.getSubject();


        String userQuery = "select email from `user` where id in (select person_id from person_group where group_id in (select id from `group` where id in (" + toAddress + ")))";

        String templateQuery = Database.getQuery("get_emailtmpl") + " and id = " + emailTemplate;

        List<EmailTemplate> emailTemplateList = Database.getInstance().executeQuery(new EmailTemplate(), EmailTemplate.class, templateQuery, projectId);
        Object emailObject[] = Database.getInstance().executeQueryRows(userQuery);
        List<String> emailAddressList = new ArrayList<>();
        for (int i = 0; i < emailObject.length; i++) {
            String emailAddress = (String) ((Object[]) emailObject[i])[0];
            emailAddressList.add(emailAddress);
        }
        EmailConfiguration ec = new EmailConfigurations().get(projectId + "");

        EmailComplier emailCompiler = new EmailComplier();
        EmailTemplate et = emailTemplateList.get(0);
        et = emailCompiler.compile(projectId + "", userTicket.getId() + "", et);
        Object[] toArray = emailAddressList.toArray(new String[emailAddressList.size()]);

        MailQ mailq = MailQ.getInstance();
        MailProperties props = new MailProperties();
        props.setType(Constants.TEXT_MAIL);
        props.setHostName(ec.getSmtpServer());
        props.setAuthUser(ec.getAuthUser());
        props.setAuthPasswd(ec.getAuthPassword());
        props.setFrom("varghees@gmail.com");
        props.setTo(StringUtils.join(toArray, ","));

        props.setSubject(et.getNotificationSubject());
        props.setTxtMessage(et.getNotificationBody());
        mailq.add(props);

        return returnVal;
    }

    public Boolean triggerEmailToUser(Integer projectId, UserTicket userTicket, Email email) {
        Boolean returnVal = false;
        String toAddress = email.getRecepient();
        String emailTemplate = email.getSubject();
        String userQuery = "select email from `user` where id in (" + toAddress + ")";

        String templateQuery = Database.getQuery("get_emailtmpl") + " and id = " + emailTemplate;
        List<EmailTemplate> emailTemplateList = Database.getInstance().executeQuery(new EmailTemplate(), EmailTemplate.class, templateQuery, projectId);
        Object emailObject[] = Database.getInstance().executeQueryRows(userQuery);
        List<String> emailAddressList = new ArrayList<>();
        for (int i = 0; i < emailObject.length; i++) {
            String emailAddress = (String) ((Object[]) emailObject[i])[0];
            emailAddressList.add(emailAddress);
        }
        EmailConfiguration ec = new EmailConfigurations().get(projectId + "");

        EmailComplier emailCompiler = new EmailComplier();
        EmailTemplate et = emailTemplateList.get(0);

        et = emailCompiler.compile(projectId + "", userTicket.getId() + "", et);
        Object[] toArray = emailAddressList.toArray(new String[emailAddressList.size()]);

        MailQ mailq = MailQ.getInstance();
        MailProperties props = new MailProperties();
        props.setType(Constants.TEXT_MAIL);
        props.setHostName(ec.getSmtpServer());
        props.setAuthUser(ec.getAuthUser());
        props.setAuthPasswd(ec.getAuthPassword());
        props.setFrom("varghees@gmail.com");
        props.setTo(StringUtils.join(toArray, ","));

        props.setSubject(et.getNotificationSubject());
        props.setTxtMessage(et.getNotificationBody());
        System.out.println(props);
        mailq.add(props);

        return returnVal;
    }

    public Boolean triggerEmailToGroup(Integer projectId, UserTicket userTicket, com.businesslense.tracker.conf.dispatcher.Email email) {
        Boolean returnVal = false;
        String toAddress = email.getRecepient();
        String emailTemplate = email.getSubject();


        String userQuery = "select email from `user` where id in (select person_id from person_group where group_id in (select id from `group` where id in (" + toAddress + ")))";

        String templateQuery = Database.getQuery("get_emailtmpl") + " and id = " + emailTemplate;

        List<EmailTemplate> emailTemplateList = Database.getInstance().executeQuery(new EmailTemplate(), EmailTemplate.class, templateQuery, projectId);
        Object emailObject[] = Database.getInstance().executeQueryRows(userQuery);
        List<String> emailAddressList = new ArrayList<>();
        for (int i = 0; i < emailObject.length; i++) {
            String emailAddress = (String) ((Object[]) emailObject[i])[0];
            emailAddressList.add(emailAddress);
        }
        EmailConfiguration ec = new EmailConfigurations().get(projectId + "");

        EmailComplier emailCompiler = new EmailComplier();
        EmailTemplate et = emailTemplateList.get(0);
        et = emailCompiler.compile(projectId + "", userTicket.getId() + "", et);
        Object[] toArray = emailAddressList.toArray(new String[emailAddressList.size()]);

        MailQ mailq = MailQ.getInstance();
        MailProperties props = new MailProperties();
        props.setType(Constants.TEXT_MAIL);
        props.setHostName(ec.getSmtpServer());
        props.setAuthUser(ec.getAuthUser());
        props.setAuthPasswd(ec.getAuthPassword());
        props.setFrom("varghees@gmail.com");
        props.setTo(StringUtils.join(toArray, ","));

        props.setSubject(et.getNotificationSubject());
        props.setTxtMessage(et.getNotificationBody());
        mailq.add(props);

        return returnVal;
    }

    public Boolean triggerEmailToUser(Integer projectId, UserTicket userTicket, com.businesslense.tracker.conf.dispatcher.Email email) {
        Boolean returnVal = false;
        String toAddress = email.getRecepient();
        String emailTemplate = email.getSubject();
        String userQuery = "select email from `user` where id in (" + toAddress + ")";

        String templateQuery = Database.getQuery("get_emailtmpl") + " and id = " + emailTemplate;
        List<EmailTemplate> emailTemplateList = Database.getInstance().executeQuery(new EmailTemplate(), EmailTemplate.class, templateQuery, projectId);
        Object emailObject[] = Database.getInstance().executeQueryRows(userQuery);
        List<String> emailAddressList = new ArrayList<>();
        for (int i = 0; i < emailObject.length; i++) {
            String emailAddress = (String) ((Object[]) emailObject[i])[0];
            emailAddressList.add(emailAddress);
        }
        EmailConfiguration ec = new EmailConfigurations().get(projectId + "");

        EmailComplier emailCompiler = new EmailComplier();
        EmailTemplate et = emailTemplateList.get(0);

        et = emailCompiler.compile(projectId + "", userTicket.getId() + "", et);
        Object[] toArray = emailAddressList.toArray(new String[emailAddressList.size()]);

        MailQ mailq = MailQ.getInstance();
        MailProperties props = new MailProperties();
        props.setType(Constants.TEXT_MAIL);
        props.setHostName(ec.getSmtpServer());
        props.setAuthUser(ec.getAuthUser());
        props.setAuthPasswd(ec.getAuthPassword());
        props.setFrom("varghees@gmail.com");
        props.setTo(StringUtils.join(toArray, ","));

        props.setSubject(et.getNotificationSubject());
        props.setTxtMessage(et.getNotificationBody());
        System.out.println(props);
        mailq.add(props);

        return returnVal;
    }*/
}
