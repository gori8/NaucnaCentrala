package rs.ac.uns.naucnacentrala.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.naucnacentrala.elasticsearch.model.ESPaper;
import rs.ac.uns.naucnacentrala.model.Paper;

@Repository
public interface ESPaperRepository extends ElasticsearchRepository<ESPaper, String> {

    Page<Paper> findByNaslov(String name, Pageable pageable);


}
