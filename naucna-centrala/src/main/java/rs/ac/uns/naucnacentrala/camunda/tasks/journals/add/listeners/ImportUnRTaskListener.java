package rs.ac.uns.naucnacentrala.camunda.tasks.journals.add.listeners;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.types.NacinPlacanjaMultiSelectionFormType;
import rs.ac.uns.naucnacentrala.camunda.types.NaucneOblastiMultiSelectionFormType;
import rs.ac.uns.naucnacentrala.camunda.types.RecezentiMultiSelectionFormType;
import rs.ac.uns.naucnacentrala.camunda.types.UredniciMultiSelectionFormType;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.NacinPlacanjaRepository;
import rs.ac.uns.naucnacentrala.repository.NaucnaOblastRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class ImportUnRTaskListener implements TaskListener {

    @Autowired
    FormService formService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void notify(DelegateTask delegateTask) {
        List<FormField> formFieldList=formService.getTaskFormData(delegateTask.getId()).getFormFields();
        if(formFieldList!=null){
            for(FormField field : formFieldList){
                if( field.getId().equals("recezentiSel")){
                    Map<Long,String> items = ((RecezentiMultiSelectionFormType)field.getType()).getValues();
                    for(User user : userRepository.findAllByRole("ROLE_RECEZENT")){
                        items.put(user.getId(),user.getUsername());
                    }
                }
                if( field.getId().equals("uredniciSel")){
                    Map<Long,String> items = ((UredniciMultiSelectionFormType)field.getType()).getValues();
                    for(User user : userRepository.findAllByRole("ROLE_UREDNIK")){
                        items.put(user.getId(),user.getUsername());
                    }
                }
            }
        }
    }

}
