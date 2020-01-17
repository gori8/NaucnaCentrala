package rs.ac.uns.naucnacentrala.controller;

import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.naucnacentrala.camunda.service.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.utils.CamundaUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Controller
@RequestMapping("/restapi/registration")
@CrossOrigin("*")
public class RegistrationController {

    private static final String PROCESS_KEY="registracija";


    @Autowired
    CamundaUtils camundaUtils;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    TaskService taskService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<FormFieldsDto> startProcess(){

        Task task = camundaUtils.startProcess(PROCESS_KEY);

        return new ResponseEntity<>(camundaUtils.createFormDTO(task,task.getProcessInstanceId()), HttpStatus.OK);
    }




    @RequestMapping(method = RequestMethod.GET, value = "/complete/{processInstanceId}")
    public ResponseEntity completeTask(@PathVariable String processInstanceId) throws URISyntaxException {
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().taskName("Potvrda emaila").singleResult();
        taskService.complete(task.getId());
        URI redirectUrl = new URI("http://localhost:4200/reg/success");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
    }


}
