package rs.ac.uns.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.naucnacentrala.model.Casopis;

import java.util.List;

public interface CasopisRepository extends JpaRepository<Casopis,Long> {

    public Casopis findByIssn(String issn);

    public Casopis findByNaziv(String naziv);

    public Casopis findByProcessInstanceId(String processInstanceId);

    public List<Casopis> findByGlavniUrednik(String glavniUrednik);

}
