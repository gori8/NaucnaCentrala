package rs.ac.uns.naucnacentrala.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;

@Transactional(readOnly = true)
@Service
public class CasopisServiceImpl implements CasopisService{


    @Autowired
    CasopisRepository casopisRepository;

    @Override
    public Casopis save(Casopis casopis) {
        return casopisRepository.save(casopis);
    }


}
