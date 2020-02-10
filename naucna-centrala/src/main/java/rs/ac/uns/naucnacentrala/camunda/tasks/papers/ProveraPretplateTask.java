package rs.ac.uns.naucnacentrala.camunda.tasks.papers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.PaymentService;

@Service
public class ProveraPretplateTask implements JavaDelegate {


    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentService paymentService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CasopisPV casopisPV = (CasopisPV) execution.getVariable("casopis");
        Casopis casopis = casopisRepository.getOne(casopisPV.getId());
        User autor = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(autor.getKupljeniCasopisi().contains(casopis)){
            execution.setVariable("autor_pretplacen",true);
        }else{
            execution.setVariable("autor_pretplacen",false);
            String url = paymentService.getRedirectUrl(casopis.getUuid().toString(),autor.getUsername(),execution.getProcessInstanceId());
            execution.setVariable("urlToPay",url);
        }
    }

}
