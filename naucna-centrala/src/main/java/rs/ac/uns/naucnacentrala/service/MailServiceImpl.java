package rs.ac.uns.naucnacentrala.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService{

    @Value("${mail.from}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String to,String subject,String body) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(from);
        message.setSubject(subject);
        MimeMessageHelper helper;
        helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setText(body, true);
        javaMailSender.send(message);

    }


}
