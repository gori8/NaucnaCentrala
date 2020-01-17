package rs.ac.uns.naucnacentrala.camunda.tasks.journals.add;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;

import java.util.ArrayList;

@Service
public class ActivateJournalTask implements JavaDelegate {



    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Casopis casopis=casopisRepository.findByProcessInstanceId(execution.getProcessInstanceId());
        casopis.setEnabled(true);
        casopis.setCasopisStatus(CasopisStatus.APPROVED);
        casopisRepository.save(casopis);
    }

}
