package rs.ac.uns.naucnacentrala.camunda.tasks.papers.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.repository.NaucnaOblastRepository;

import java.util.List;
import java.util.Map;

@Service
public class SetNOViewCompleteListener implements TaskListener {

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String noId = runtimeService.getVariable(delegateTask.getProcessInstanceId(),"naucna_oblast").toString();
        for (NaucnaOblast naucnaOblast : naucnaOblastRepository.findAll()) {
            if (Long.valueOf(noId) == naucnaOblast.getId()) {
                runtimeService.setVariable(delegateTask.getProcessInstanceId(), "noDefValue", naucnaOblast.getName());
                break;
            }
        }
    }
}
