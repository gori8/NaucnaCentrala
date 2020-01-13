package rs.ac.uns.naucnacentrala.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.utils.FormExtractorUtils;

import java.util.List;

@Controller
@RequestMapping("/restapi/registration")
@CrossOrigin("*")
public class RegistrationController {

    private static final String processKey="registracija";

    @Autowired
    FormExtractorUtils formExtractorUtils;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<FormFieldsDto> startProcess(){

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processKey);

        Task task=taskService.createTaskQuery().active().processInstanceId(pi.getId()).list().get(0);

        return new ResponseEntity<>(formExtractorUtils.createFormDTO(task,pi.getId()), HttpStatus.OK);
    }


}
