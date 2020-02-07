package rs.ac.uns.naucnacentrala.service;

import rs.ac.uns.naucnacentrala.model.Casopis;

import java.util.List;

public interface CasopisService {

    Casopis save(Casopis casopis);

    List<Casopis> getAllActivated();

    Casopis getOne(Long id);
}
