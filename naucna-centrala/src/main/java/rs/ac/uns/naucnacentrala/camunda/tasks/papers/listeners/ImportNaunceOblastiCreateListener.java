package rs.ac.uns.naucnacentrala.camunda.tasks.papers.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.types.MultiSelectionFormType;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.repository.NacinPlacanjaRepository;
import rs.ac.uns.naucnacentrala.repository.NaucnaOblastRepository;

import java.util.List;
import java.util.Map;

@Service
public class ImportNaunceOblastiCreateListener implements TaskListener {

    @Autowired
    FormService formService;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<FormField> formFieldList=formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if(formFieldList!=null){
            for(FormField field : formFieldList){
                if( field.getId().equals("naucna_oblast")){
                    Map<String,String> items = ((EnumFormType)field.getType()).getValues();
                    for(NaucnaOblast naucnaOblast : naucnaOblastRepository.findAll()){
                        items.put(naucnaOblast.getId().toString(),naucnaOblast.getName());
                    }
                }
            }
        }
    }
}
