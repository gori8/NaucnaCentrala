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
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.elasticsearch.model.ESPaper;
import rs.ac.uns.naucnacentrala.elasticsearch.repository.ESPaperRepository;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.Paper;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.PaperRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.PaymentService;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageService;

import java.io.File;

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


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long paperId = (Long)execution.getVariable("paperId");
        Paper paper = paperRepository.getOne(paperId);


        ESPaper esPaper = new ESPaper();
        esPaper.setApstrakt(paper.getApstrakt());
        esPaper.setKljucniPojmovi(paper.getKljucniPojmovi());
        esPaper.setNaslov(paper.getNaslov());

        //Get text from pdf
        File f = storageService.loadAsResource(paper.getPdfPath()).getFile();

        PDDocument pdDoc = PDDocument.load(f);
        String parsedText = new PDFTextStripper().getText(pdDoc);

        parsedText = parsedText.replace("\n"," ");




        esPaper.setSadrzaj(parsedText);

        esPaperRepository.save(esPaper);
        

    }

}