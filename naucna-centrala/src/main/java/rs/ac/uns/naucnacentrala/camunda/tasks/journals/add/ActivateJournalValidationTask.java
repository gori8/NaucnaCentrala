package rs.ac.uns.naucnacentrala.camunda.tasks.journals.add;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.service.JournalService;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.CasopisService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service
public class ActivateJournalValidationTask implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Casopis casopis=casopisRepository.findByProcessInstanceId(execution.getProcessInstanceId());
        ArrayList<String> userIds= (ArrayList<String>) execution.getVariable("recezentiSel");
        for(String id : userIds){
            User user=userRepository.getOne(Long.valueOf(id));
            user.getRecenziramCasopise().add(casopis);
            casopis.getRecezenti().add(user);
            casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
            casopisRepository.save(casopis);
        }
        execution.setVariable("flag_val",true);
    }

}
