package rs.ac.uns.naucnacentrala.service;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.task.Task;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.*;
import rs.ac.uns.naucnacentrala.elasticsearch.model.ESPaper;
import rs.ac.uns.naucnacentrala.elasticsearch.repository.ESPaperRepository;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.utils.uploadingfiles.storage.StorageService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    ESPaperRepository esPaperRepository;

    @Autowired
    ElasticsearchRestTemplate operations;

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    StorageService storageService;

    @Autowired
    UserRepository userRepository;


    @Override
    public List<SearchResultDTO> searchByField(SearchDTO searchDTO) {

        BoolQueryBuilder query = QueryBuilders.boolQuery();

        query.must(QueryBuilders.matchPhraseQuery(searchDTO.getField(),searchDTO.getQuery()));

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query)
                .withHighlightFields(new HighlightBuilder.Field(searchDTO.getField()))
                .build();



        SearchHits<ESPaper> searchHits = operations.search(searchQuery,
                ESPaper.class,
                IndexCoordinates.of("naucni_rad"));


        List<SearchResultDTO> results = new ArrayList<>();
        System.out.println("----------------------------RESULTS----------------------------");
        for(SearchHit<ESPaper> hit : searchHits.getSearchHits()){
            System.out.println("-----------------SCORE : "+hit.getScore());
            SearchResultDTO result = new SearchResultDTO();
            ESPaper resultPaper = hit.getContent();
            result.setAutori(resultPaper.getAutori());
            result.setNaslov(resultPaper.getNaslov());
            result.setFilepath(resultPaper.getFilename());
            result.setNaucnaOblast(resultPaper.getNaucnaOblast());
            result.setDynamicHighlights(new ArrayList<>());
            System.out.println("--------------------START-------------------------------");
            System.out.println(resultPaper);
            System.out.println("---------------------END-------------------------------");
            Map<String, List<String>> highlightMap = hit.getHighlightFields();
            for(String field : highlightMap.keySet()) {
                boolean flagToAdd = false;
                SazetakDTO sazetakDTO = new SazetakDTO();
                sazetakDTO.setField(field);
                String sazetakStr = "";
                if(field.equals("sadrzaj")){
                    sazetakStr+="<b>Sadržaj</b>: ";
                    flagToAdd = true;
                }
                if(field.equals("apstrakt")){
                    sazetakStr+="<b>Apstrakt</b>: ";
                    flagToAdd = true;
                }

                List<String> highlights = highlightMap.get(field);
                System.out.println("-------------HIGHLIGHTS " + field + "-------------------");
                for (String s : highlights) {
                    System.out.println("-------------HIGHLIGHT: " + s);
                    sazetakStr+=s+" ... ";
                }
                sazetakDTO.setSazetak(sazetakStr);
                if(flagToAdd) {
                    result.getDynamicHighlights().add(sazetakDTO);
                }
            }
            results.add(result);
        }

        return results;
    }

    @Override
    public List<SearchResultDTO> advancedSearch(ComplexSearchDTO searchDTO) {

        BoolQueryBuilder complexQuery = QueryBuilders.boolQuery();



        HighlightBuilder highlightBuilder = new HighlightBuilder();

        for(GroupDTO group : searchDTO.getGroups()){
            BoolQueryBuilder groupQuery = QueryBuilders.boolQuery();
            for(ElementDTO element : group.getElements()){
                QueryBuilder leafQuery = QueryBuilders.matchPhraseQuery(element.getField(),element.getSearchText());
                highlightBuilder.field(element.getField());
                if(group.getType().equals("AND")){
                    groupQuery.must(leafQuery);
                }else{
                    groupQuery.should(leafQuery);
                }
            }
            if(searchDTO.getOperator().equals("AND")){
                complexQuery.must(groupQuery);
            }else{
                complexQuery.should(groupQuery);
            }

        }

        /*Iterable<ESPaper> papersResult = esPaperRepository.search(complexQuery);

        System.out.println("----------------------------RESULTS----------------------------");
        for(ESPaper paper : papersResult){
            System.out.println("--------------------START-------------------------------");
            System.out.println(paper);
            System.out.println("---------------------END-------------------------------");
        }*/




        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(complexQuery)
                .withHighlightBuilder(highlightBuilder)
                .build();

        SearchHits<ESPaper> searchHits = operations.search(searchQuery,
                ESPaper.class,
                IndexCoordinates.of("naucni_rad"));


        List<SearchResultDTO> results = new ArrayList<>();
        System.out.println("----------------------------RESULTS----------------------------");
        for(SearchHit<ESPaper> hit : searchHits.getSearchHits()){
            SearchResultDTO result = new SearchResultDTO();
            ESPaper resultPaper = hit.getContent();
            result.setAutori(resultPaper.getAutori());
            result.setNaslov(resultPaper.getNaslov());
            result.setFilepath(resultPaper.getFilename());
            result.setNaucnaOblast(resultPaper.getNaucnaOblast());
            result.setDynamicHighlights(new ArrayList<>());
            System.out.println("--------------------START-------------------------------");
            System.out.println(resultPaper);
            System.out.println("---------------------END-------------------------------");
            Map<String, List<String>> highlightMap = hit.getHighlightFields();
            for(String field : highlightMap.keySet()) {
                boolean flagToAdd = false;
                SazetakDTO sazetakDTO = new SazetakDTO();
                sazetakDTO.setField(field);
                String sazetakStr = "";
                if(field.equals("sadrzaj")){
                    sazetakStr+="<b>Sadržaj</b>: ";
                    flagToAdd = true;
                }
                if(field.equals("apstrakt")){
                    sazetakStr+="<b>Apstrakt</b>: ";
                    flagToAdd = true;
                }

                List<String> highlights = highlightMap.get(field);
                System.out.println("-------------HIGHLIGHTS " + field + "-------------------");
                for (String s : highlights) {
                    System.out.println("-------------HIGHLIGHT: " + s);
                    sazetakStr+=s+" ... ";
                }
                sazetakDTO.setSazetak(sazetakStr);
                if(flagToAdd) {
                    result.getDynamicHighlights().add(sazetakDTO);
                }
            }
            results.add(result);
        }

        return results;
    }

    @Override
    public List<ReviewerSearchResultDTO> reviewersSearch(ReviewerSearchDTO searchDTO) throws IOException {
        Task task=taskService.createTaskQuery().taskId(searchDTO.getTaskId()).singleResult();
        String processInstanceId=task.getProcessInstanceId();
        String authorUsername = (String)runtimeService.getVariable(processInstanceId, "author");
        User author = userRepository.findByUsername(authorUsername);

        String pdfPath = runtimeService.getVariable(processInstanceId,"pdf").toString();

        File f = storageService.loadAsResource(pdfPath).getFile();
        PDDocument pdDoc = PDDocument.load(f);
        String parsedText = new PDFTextStripper().getText(pdDoc);

        String sadrzaj = parsedText.replace("\n"," ");

        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

        if(searchDTO.isGeoLoc()){
            QueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("reviewers.location").distance("100km").point(author.getLat(),author.getLng());
            rootQuery.mustNot(queryBuilder);
        }

        Iterable<ESPaper> papersResult = esPaperRepository.search(rootQuery);

        System.out.println("----------------------------RESULTS----------------------------");
        for(ESPaper paper : papersResult){
            System.out.println("--------------------START-------------------------------");
            System.out.println(paper);
            System.out.println("---------------------END-------------------------------");
        }



        return null;
    }

    @Override
    public List<ReviewerSearchResultDTO> reviewersSearchTest(String authorUsername, String pdfPath) throws IOException {
        User author = userRepository.findByUsername(authorUsername);


        File f = storageService.loadAsResource(pdfPath).getFile();
        PDDocument pdDoc = PDDocument.load(f);
        String parsedText = new PDFTextStripper().getText(pdDoc);

        String sadrzaj = parsedText.replace("\n"," ");


        BoolQueryBuilder rootQuery = QueryBuilders.boolQuery();

        //QueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("reviewers.location").distance("100km").point(author.getLat(),author.getLng());
        //rootQuery.mustNot(queryBuilder);


        //MoreLikeThisQueryBuilder.Item item = new MoreLikeThisQueryBuilder.Item();

        QueryBuilder moreLikeThisQuery = QueryBuilders.moreLikeThisQuery(new String[]{"sadrzaj"},new String[]{sadrzaj},null).minDocFreq(1).minTermFreq(1);




        Iterable<ESPaper> papersResult = esPaperRepository.search(moreLikeThisQuery);

        System.out.println("----------------------------RESULTS----------------------------");
        for(ESPaper paper : papersResult){
            System.out.println("--------------------START-------------------------------");
            System.out.println(paper);
            System.out.println("---------------------END-------------------------------");
        }



        return null;
    }


}
