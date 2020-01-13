package rs.ac.uns.naucnacentrala.utils;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;

import java.util.List;

@Component
public class FormExtractorUtils {

    @Autowired
    FormService formService;

    public FormFieldsDto createFormDTO(Task task, String processInstanceId){
        TaskFormData tfd = formService.getTaskFormData(task.getId());
        List<FormField> properties = tfd.getFormFields();
        return new FormFieldsDto(task.getId(),processInstanceId,properties);
    }

}
