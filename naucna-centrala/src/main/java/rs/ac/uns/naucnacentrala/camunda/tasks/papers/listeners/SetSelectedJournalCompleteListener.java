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
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.service.CasopisService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
                    CasopisPV casopisPV= ObjectMapperUtils.map(casopis, CasopisPV.class);
                    for(User urednik : casopis.getUrednici()){
                        for(NaucnaOblast no : urednik.getNaucneOblasti()){
                            casopisPV.getUrednici().put(no.getId().toString(),urednik.getUsername());
                        }
                    }
                    for(User recezent : casopis.getRecezenti()){
                        for(NaucnaOblast no : recezent.getNaucneOblasti()){
                            ArrayList<String> noRecezenti = casopisPV.getRecezenti().get(no.getId().toString());
                            if(noRecezenti==null){
                                noRecezenti = new ArrayList<>();
                            }
                            noRecezenti.add(recezent.getUsername());
                            casopisPV.getRecezenti().put(no.getId().toString(),noRecezenti);
                        }
                    }
                    delegateTask.getExecution().setVariable("casopis", casopisPV);
                }
            }
        }

    }

}
