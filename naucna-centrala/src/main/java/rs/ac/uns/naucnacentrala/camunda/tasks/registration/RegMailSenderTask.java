package rs.ac.uns.naucnacentrala.camunda.tasks.registration;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.service.MailService;

@Service
public class RegMailSenderTask implements JavaDelegate {

    @Autowired
    MailService mailService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String to=delegateExecution.getVariable("email").toString();
        String subject="Naucna Centrala registration confirmation";
        String htmlBody="<a href=\"http://localhost:8080/restapi/registration/complete/"+delegateExecution.getProcessInstanceId()+"\">Click to confirm your registration</a>\n";
        System.out.println(htmlBody);
        mailService.sendMail(to,subject,htmlBody);
    }

}
