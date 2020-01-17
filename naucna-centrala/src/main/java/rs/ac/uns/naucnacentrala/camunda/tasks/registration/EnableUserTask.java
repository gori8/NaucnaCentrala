package rs.ac.uns.naucnacentrala.camunda.tasks.registration;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.MailService;

@Service
public class EnableUserTask implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User user = userRepository.findByUsername(delegateExecution.getVariable("username").toString());
        user.setEnabled(true);
        userRepository.save(user);
    }
}
