package rs.ac.uns.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.naucnacentrala.model.Coauthor;
import rs.ac.uns.naucnacentrala.model.Paper;

import java.util.List;

public interface CoauthorRepository extends JpaRepository<Coauthor,Long> {

    List<Coauthor> findAllByPaperId(Long paperId);

}
