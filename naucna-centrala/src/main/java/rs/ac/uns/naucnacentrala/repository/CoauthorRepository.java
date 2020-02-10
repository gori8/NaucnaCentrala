package rs.ac.uns.naucnacentrala.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.naucnacentrala.model.Coauthor;
import rs.ac.uns.naucnacentrala.model.Paper;

public interface CoauthorRepository extends JpaRepository<Coauthor,Long> {
}
