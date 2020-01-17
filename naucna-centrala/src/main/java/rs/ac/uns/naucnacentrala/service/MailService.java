package rs.ac.uns.naucnacentrala.service;

import javax.mail.MessagingException;

public interface MailService {

    public abstract void sendMail(String to,String subject,String body) throws MessagingException;

}
