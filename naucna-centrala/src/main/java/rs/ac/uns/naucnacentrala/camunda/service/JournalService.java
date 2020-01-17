package rs.ac.uns.naucnacentrala.camunda.service;

import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.User;

import java.util.List;

public interface JournalService {

    public abstract CasopisDTO getCasopisFromProcessVariables(String processInstanceID);

    public abstract List<CasopisDTO> getAllNotActivated(String username);

}
