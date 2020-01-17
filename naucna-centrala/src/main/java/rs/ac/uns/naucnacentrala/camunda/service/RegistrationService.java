package rs.ac.uns.naucnacentrala.camunda.service;

import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.UserDTO;

import java.util.List;


public interface RegistrationService {

    public abstract UserDTO getUserFromProcessVariables(String processInstanceID);

    public abstract List<UserDTO> getAllNotActivated(String username);

}
