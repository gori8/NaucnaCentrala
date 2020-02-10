package rs.ac.uns.naucnacentrala.camunda.tasks.papers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.MailService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailSendTask implements JavaDelegate {

    @Autowired
    MailService mailService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String to = (String)delegateExecution.getVariable("oneUser");
        String taskName = delegateExecution.getVariable("taskName").toString();
        String content = delegateExecution.getVariable("content").toString();
        String subject=taskName;

        String email = userRepository.findByUsername(to).getEmail();
        mailService.sendMail(email,subject,content);

    }

}
