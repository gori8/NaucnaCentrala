package rs.ac.uns.naucnacentrala.camunda.tasks.papers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.dto.KoautorDTO;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.CoauthorRepository;
import rs.ac.uns.naucnacentrala.repository.PaperRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.PaymentService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageProperties;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class SavePaperTask implements JavaDelegate {

    private final Path rootLocation;

    @Autowired
    public SavePaperTask(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    PaperRepository paperRepository;

    @Autowired
    CoauthorRepository coauthorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String authorUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        CasopisPV casopisPV = (CasopisPV) execution.getVariable("casopis");
        Casopis casopis = casopisRepository.getOne(casopisPV.getId());

        String naslov = execution.getVariable("naslov").toString();
        String apstrakt = execution.getVariable("apstrakt").toString();
        String kljucniPojmovi = execution.getVariable("kljucni_pojmovi").toString();
        String pdfPath = this.rootLocation.toString() + "/" + execution.getVariable("pdf").toString();
        Long noId = Long.valueOf(execution.getVariable("naucna_oblast").toString());

        Paper paper = new Paper();
        paper.setNaslov(naslov);
        paper.setApstrakt(apstrakt);
        paper.setKljucniPojmovi(kljucniPojmovi);
        paper.setPdfPath(pdfPath);
        paper.setNaucnaOblastId(noId);
        paper.setCasopis(casopis);
        paper = paperRepository.save(paper);

        casopis.getRadovi().add(paper);
        casopisRepository.save(casopis);

        execution.setVariable("paperId",paper.getId());

        String coauthorsJson = execution.getVariable("coauthors").toString();

        ArrayList<KoautorDTO> koautorDTOList = objectMapper.readValue(coauthorsJson,new TypeReference<ArrayList<KoautorDTO>>(){});

        for(KoautorDTO koautorDTO : koautorDTOList){
            Coauthor coauthor = ObjectMapperUtils.map(koautorDTO, Coauthor.class);
            coauthor.setPaperId(paper.getId());
            coauthor.setAuthorUsername(authorUsername);
            User authorInSys = userRepository.findByEmail(coauthor.getEmail());
            if(authorInSys != null) {
                if (((Authority) authorInSys.getAuthorities().toArray()[0]).getName().equals("ROLE_AUTHORS")) {
                    coauthor.setUsername(authorInSys.getUsername());
                }
            }

            coauthorRepository.save(coauthor);
        }


    }


}
