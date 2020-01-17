package rs.ac.uns.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.naucnacentrala.model.NacinPlacanja;

import java.util.List;

public interface NacinPlacanjaRepository extends JpaRepository<NacinPlacanja,Long> {

    @Query("FROM NacinPlacanja")
    public List<NacinPlacanja> getAll();

}
