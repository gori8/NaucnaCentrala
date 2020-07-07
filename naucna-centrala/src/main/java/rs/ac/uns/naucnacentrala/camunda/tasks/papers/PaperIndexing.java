package rs.ac.uns.naucnacentrala.camunda.tasks.papers;


import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.elasticsearch.model.ESPaper;
import rs.ac.uns.naucnacentrala.elasticsearch.model.ESRecenzent;
import rs.ac.uns.naucnacentrala.elasticsearch.repository.ESPaperRepository;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.repository.*;
import rs.ac.uns.naucnacentrala.service.PaymentService;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaperIndexing implements JavaDelegate {


    @Autowired
    PaperRepository paperRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    ESPaperRepository esPaperRepository;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    CoauthorRepository coauthorRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long paperId = (Long)execution.getVariable("paperId");
        Paper paper = paperRepository.getOne(paperId);

        ESPaper esPaper = new ESPaper();
        esPaper.setApstrakt(paper.getApstrakt());
        esPaper.setKljucniPojmovi(paper.getKljucniPojmovi());
        esPaper.setNaslov(paper.getNaslov());
        esPaper.setId(paperId);
        esPaper.setFilename(paper.getPdfPath());

        NaucnaOblast naucnaOblast = naucnaOblastRepository.getOne(paper.getNaucnaOblastId());

        esPaper.setNaucnaOblast(naucnaOblast.getName());

        User author = userRepository.findByUsername(paper.getAuthorUsername());

        String autori = author.getIme()+" "+author.getPrezime()+",";
        for (Coauthor koautor : coauthorRepository.findAllByPaperId(paper.getId())){
            autori+=koautor.getIme()+" "+koautor.getPrezime()+",";
        }

        autori = removeLastChar(autori);

        esPaper.setAutori(autori);

        //Get text from pdf
        File f = storageService.loadAsResource(paper.getPdfPath()).getFile();

        PDDocument pdDoc = PDDocument.load(f);
        String parsedText = new PDFTextStripper().getText(pdDoc);

        parsedText = parsedText.replace("\n"," ");


        esPaper.setSadrzaj(parsedText);

        List<String> recezenti = (List<String>)execution.getVariable("selectedRecezenti");
        ArrayList<ESRecenzent> esRecezenti = new ArrayList<>();
        for(String recezentUsername : recezenti){
            User recenzent = userRepository.findByUsername(recezentUsername);

            ESRecenzent esRecenzent = new ESRecenzent();
            esRecenzent.setUsername(recenzent.getUsername());

            GeoPoint location = new GeoPoint(recenzent.getLat(),recenzent.getLng());

            esRecenzent.setLocation(location);

            esRecezenti.add(esRecenzent);
        }

        esPaper.setReviewers(esRecezenti);


        esPaperRepository.save(esPaper);
        

    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

}