package rs.ac.uns.naucnacentrala.camunda.tasks.registration;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.AuthorityRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetRecezentTask implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    IdentityService identityService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        User user = userRepository.findByUsername(delegateExecution.getVariable("username").toString());
        Authority authority= authorityRepository.findOneByName("ROLE_RECEZENT");
        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(authority);
        user.setAuthorities(authorityList);
        userRepository.save(user);
        identityService.createMembership(user.getUsername(),"recezent");
    }


}
