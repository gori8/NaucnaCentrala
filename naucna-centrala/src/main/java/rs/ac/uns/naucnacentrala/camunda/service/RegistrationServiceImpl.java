package rs.ac.uns.naucnacentrala.camunda.service;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.NaucnaOblastDTO;
import rs.ac.uns.naucnacentrala.dto.UserDTO;
import rs.ac.uns.naucnacentrala.model.Authority;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.NaucnaOblast;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.AuthorityRepository;
import rs.ac.uns.naucnacentrala.repository.NaucnaOblastRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import java.util.*;

@Service
public class RegistrationServiceImpl implements RegistrationService{

    private static final String[] variableArr={"username","password","ime","prezime","email","grad","drzava","titula","naucne_oblasti"};

    private static final String VALIDATION_RESULT="validationErrors";

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;


    @Override
    public UserDTO getUserFromProcessVariables(String processInstanceID){
        ArrayList<String> variableNames=new ArrayList<String>(Arrays.asList(variableArr));
        VariableMap map=runtimeService.getVariablesTyped(processInstanceID,variableNames,true);
        UserDTO user=new UserDTO();

        StringValue usernameVal=map.getValueTyped("username");
        user.setUsername(usernameVal.getValue());
        StringValue passwordVal=map.getValueTyped("password");
        user.setPassword(passwordVal.getValue());
        StringValue imeVal=map.getValueTyped("ime");
        user.setIme(imeVal.getValue());
        StringValue prezimeVal=map.getValueTyped("prezime");
        user.setPrezime(prezimeVal.getValue());
        StringValue emailVal=map.getValueTyped("email");
        user.setEmail(emailVal.getValue());
        StringValue gradVal=map.getValueTyped("grad");
        user.setGrad(gradVal.getValue());
        StringValue drzavaVal=map.getValueTyped("drzava");
        user.setDrzava(drzavaVal.getValue());
        StringValue titulaVal=map.getValueTyped("titula");
        user.setTitula(titulaVal.getValue());
        ObjectValue naucneOblastiValue=map.getValueTyped("naucne_oblasti");
        for(String id : (List<String>) naucneOblastiValue.getValue()){
            NaucnaOblast no=naucnaOblastRepository.getOne(Long.valueOf(id));
            NaucnaOblastDTO noDTO= ObjectMapperUtils.map(no,NaucnaOblastDTO.class);
            user.getNaucneOblasti().add(noDTO);
        }

        return user;
    }

    @Override
    public List<UserDTO> getAllNotActivated(String username) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(username).active().taskName("Potvrda recezenta").list();

        ArrayList<UserDTO> users=new ArrayList<UserDTO>();
        for(Task task : taskList){
            User user=userRepository.findByUsername(runtimeService.getVariable(task.getProcessInstanceId(),"username").toString());
            if(user!=null) {
                UserDTO userDTO = ObjectMapperUtils.map(user,UserDTO.class);
                userDTO.setTaskId(task.getId());
                users.add(userDTO);
            }
        }
        return users;
    }

}
