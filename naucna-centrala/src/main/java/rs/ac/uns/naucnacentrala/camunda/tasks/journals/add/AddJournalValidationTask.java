package rs.ac.uns.naucnacentrala.camunda.tasks.journals.add;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.service.JournalService;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.CasopisService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Set;

@Service
public class AddJournalValidationTask implements JavaDelegate {


    @Autowired
    JournalService journalService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        ValidatorFactory validatorFactory= Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        CasopisDTO casopis = journalService.getCasopisFromProcessVariables(execution.getProcessInstanceId());
        System.out.println(casopis.getNaziv());
        System.out.println(casopis.getIssn());
        Set<ConstraintViolation<CasopisDTO>> violations = validator.validate(casopis);
        boolean flag_val=true;
        HashMap<String,String> valErrors=new HashMap<String,String>();
        for (ConstraintViolation<CasopisDTO> violation : violations) {
            flag_val=false;
            valErrors.put(violation.getPropertyPath().toString(),violation.getMessage());
        }
        if(!flag_val) {
            execution.setVariable("validationErrors", valErrors);
        }else{
            Casopis maybe=casopisRepository.findByProcessInstanceId(execution.getProcessInstanceId());
            if(casopisRepository.findByIssn(casopis.getIssn())!=null && casopisRepository.findByIssn(casopis.getIssn()).getId()!=maybe.getId()){
                flag_val = false;
                valErrors.put("issn", "Already exists");
                execution.setVariable("validationErrors", valErrors);
            }else {
                execution.setVariable("validationErrors", null);
                Casopis realCasopis= ObjectMapperUtils.map(casopis,Casopis.class);
                realCasopis.setProcessInstanceId(execution.getProcessInstanceId());
                if(maybe!=null){
                    realCasopis.setId(maybe.getId());
                }
                String glavniUrednik=SecurityContextHolder.getContext().getAuthentication().getName();
                realCasopis.setGlavniUrednik(glavniUrednik);
                realCasopis.setCasopisStatus(CasopisStatus.WAITING_FOR_INPUT);
                casopisService.save(realCasopis);
            }
        }
        execution.setVariable("flag_val",flag_val);
    }

}
