package rs.ac.uns.naucnacentrala.controller;

import lombok.val;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.form.FormType;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.value.BooleanValue;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.naucnacentrala.camunda.service.JournalService;
import rs.ac.uns.naucnacentrala.camunda.service.RegistrationService;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.FormFieldsDto;
import rs.ac.uns.naucnacentrala.dto.FormSubmissionDto;
import rs.ac.uns.naucnacentrala.dto.TaskDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.utils.CamundaUtils;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import javax.ws.rs.Path;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/restapi/bpmn")
@CrossOrigin("*")
public class BPMNController {

    private static final String VALIDATION_FLAG_VARIABLE="flag_val";
    private static final String VALIDATION_ERRORS_VARIABLE="validationErrors";
    private static final String UREDNIK_PROCESS_KEY="registracijaUrednika";


    @Autowired
    CamundaUtils camundaUtils;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    IdentityService identityService;

    @Autowired
    JournalService journalService;

    @Autowired
    RegistrationService registrationService;



    @RequestMapping(method = RequestMethod.POST, value = "/form/{taskId}")
    public ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = camundaUtils.mapListToDto(dto);
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId=task.getProcessInstanceId();
        if(task.getName().equals("Unos podataka i naucnih oblasti")) {
            formService.submitTaskForm(taskId, map);


            Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
            if (execution == null) {
                return ResponseEntity.status(201).build();
            } else {
                BooleanValue validationValue = runtimeService.getVariableTyped(processInstanceId, VALIDATION_FLAG_VARIABLE);
                if (validationValue.getValue()) {
                    return new ResponseEntity<>(HttpStatus.CREATED);
                } else {
                    ObjectValue valErrorsVal = runtimeService.getVariableTyped(task.getProcessInstanceId(), VALIDATION_ERRORS_VARIABLE);
                    HashMap<String, String> valErrors = (HashMap<String, String>) valErrorsVal.getValue();
                    Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0);
                    valErrors.put("taskID", activeTask.getId());
                    return ResponseEntity.badRequest().body(valErrors);
                }
            }
        }else{
            return ResponseEntity.status(403).build();
        }
    }

    @PreAuthorize("hasRole('UREDNIK') or hasRole('ADMIN') or hasRole('AUTHORS') or hasRole('RECEZENT')")
    @RequestMapping(method = RequestMethod.POST, value = "/protected/form/{taskId}")
    public ResponseEntity postProtected(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        HashMap<String, Object> map = camundaUtils.mapListToDto(dto);
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId=task.getProcessInstanceId();

        HashMap<String,String> validationMap = formValidation(taskId,map);
        if(!validationMap.isEmpty()){
            return ResponseEntity.badRequest().body(validationMap);
        }

        if(task.getAssignee().equals(auth.getName())) {
            formService.submitTaskForm(taskId, map);
        }else{
            return ResponseEntity.status(403).build();
        }


        List<Execution> executions=runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        if(executions.size()==0){
            return  ResponseEntity.status(201).build();
        }else {
            BooleanValue validationValue = runtimeService.getVariableTyped(processInstanceId, VALIDATION_FLAG_VARIABLE);
            if(validationValue!=null){
                if (validationValue.getValue()) {
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }
                else {
                    ObjectValue valErrorsVal = runtimeService.getVariableTyped(task.getProcessInstanceId(), VALIDATION_ERRORS_VARIABLE);
                    HashMap<String, String> valErrors = (HashMap<String, String>) valErrorsVal.getValue();
                    Task activeTask = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list().get(0);
                    valErrors.put("taskID", activeTask.getId());
                    return ResponseEntity.badRequest().body(valErrors);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

    private HashMap<String,String> formValidation(String taskId, HashMap<String,Object> dataMap){
        HashMap<String,String> ret = new HashMap<>();
        List<FormField> formFields = formService.getTaskFormData(taskId).getFormFields();
        for(FormField field : formFields){
            List<FormFieldValidationConstraint> constraints = field.getValidationConstraints();
            for(FormFieldValidationConstraint constraint : constraints){
                switch (constraint.getName()) {
                    case "required": {
                        if(!dataMap.containsKey(field.getId())){
                            ret.put(field.getId(),"This field is required");
                        }
                        break;
                    }
                    case "readonly": {
                        if(dataMap.containsKey(field.getId())){
                            ret.put(field.getId(),"This field is readonly");
                        }
                        break;
                    }
                    case "min": {
                        Object val = dataMap.get(field.getId());
                        if(field.getType().getName().equals("long")) {
                            Long number = Long.valueOf(val.toString());
                            if (number < Long.valueOf(constraint.getConfiguration().toString())) {
                                ret.put(field.getId(), "Min value is " + constraint.getConfiguration().toString());
                            }
                        }
                        if(field.getType().getName().equals("string")){
                            if(val.toString().length() < Long.valueOf(constraint.getConfiguration().toString())){
                                ret.put(field.getId(),"Min length is "+constraint.getConfiguration().toString());
                            }
                        }
                        break;
                    }
                    case "max": {
                        Object val = dataMap.get(field.getId());
                        if(field.getType().getName().equals("long")) {
                            Long number = Long.valueOf(val.toString());
                            if (number > Long.valueOf(constraint.getConfiguration().toString())) {
                                ret.put(field.getId(), "Min value is " + constraint.getConfiguration().toString());
                            }
                        }
                        if(field.getType().getName().equals("string")){
                            if(val.toString().length() > Long.valueOf(constraint.getConfiguration().toString())){
                                ret.put(field.getId(),"Min length is "+constraint.getConfiguration().toString());
                            }
                        }
                        break;
                    }
                }
            }
        }

        if(!ret.isEmpty()) {
            ret.put("taskID", taskId);
        }

        return ret;

    }

    @PreAuthorize("hasRole('UREDNIK') or hasRole('AUTHORS')")
    @RequestMapping(method = RequestMethod.GET, value = "/task/active/{processInstanceId}")
    public ResponseEntity getActiveTask(@PathVariable String processInstanceId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        Task task = taskService.createTaskQuery().active().processInstanceId(processInstanceId).singleResult();
        if(task!=null) {
            if (task.getAssignee().equals(auth.getName())) {
                return ResponseEntity.ok().body(camundaUtils.createFormDTO(task, processInstanceId));
            } else {
                return ResponseEntity.status(404).build();
            }
        }else{
            return ResponseEntity.status(404).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/admin/journal")
    public ResponseEntity getActiveAdminJournalTasks() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        return ResponseEntity.ok().body(journalService.getAllNotActivated(auth.getName()));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/admin/registration")
    public ResponseEntity getActiveAdminRegTasks() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        identityService.setAuthenticatedUserId(auth.getName());

        return ResponseEntity.ok().body(registrationService.getAllNotActivated(auth.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/urednik")
    public ResponseEntity<FormFieldsDto> startProcessUrednik(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        Task task = camundaUtils.startProcess(UREDNIK_PROCESS_KEY);

        return new ResponseEntity<>(camundaUtils.createFormDTO(task,task.getProcessInstanceId()), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    public ResponseEntity getMyTasks(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Task> tasks = taskService.createTaskQuery().active().taskAssignee(auth.getName()).processDefinitionKey("obradaNaucnogRada").list();

        ArrayList<TaskDTO> ret = new ArrayList<>();
        for(Task task : tasks){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setName(task.getName());
            taskDTO.setTaskId(task.getId());
            Object naslovVal = runtimeService.getVariable(task.getProcessInstanceId(),"naslov");
            if(naslovVal==null){
                continue;
            }
            String rad = naslovVal.toString();
            taskDTO.setRad(rad);
            taskDTO.setProcessInstanceId(task.getProcessInstanceId());
            ret.add(taskDTO);
        }

        return ResponseEntity.ok().body(ret);
    }

    private void addSpecialProcessVariables(Map<String,Object> map,String processInstanceId){
        if(map.get("koautori")!=null) {
            System.out.println(runtimeService.getVariable(processInstanceId,"koautori"));
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks/{id}")
    public ResponseEntity getMyTask(@PathVariable String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Task task= taskService.createTaskQuery().taskId(id).singleResult();

        if(task!=null) {
            if (task.getAssignee().equals(auth.getName())) {
                return ResponseEntity.ok().body(camundaUtils.createFormDTO(task, task.getProcessInstanceId()));
            } else {
                return ResponseEntity.status(403).build();
            }
        }else{
            return ResponseEntity.status(404).build();
        }

    }


}
