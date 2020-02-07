package rs.ac.uns.naucnacentrala.camunda.tasks.papers.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.service.JournalService;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.service.CasopisService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import java.util.List;
import java.util.Map;

@Service
public class SetSelectedJournalCompleteListener implements TaskListener {

    @Autowired
    FormService formService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    JournalService journalService;

    @Override
    public void notify(DelegateTask delegateTask) {

        List<FormField> formFieldList=formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if(formFieldList!=null){
            for(FormField field : formFieldList){
                if( field.getId().equals("selBox1")){
                    Casopis casopis = casopisService.getOne(Long.valueOf(field.getValue().getValue().toString()));
                    CasopisDTO casopisDTO= ObjectMapperUtils.map(casopis, CasopisDTO.class);
                    delegateTask.getExecution().setVariable("casopis",casopisDTO);
                }
            }
        }

    }

}
