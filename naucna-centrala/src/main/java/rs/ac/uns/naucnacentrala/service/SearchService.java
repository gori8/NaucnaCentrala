package rs.ac.uns.naucnacentrala.service;

import rs.ac.uns.naucnacentrala.dto.*;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    List<SearchResultDTO> searchByField(SearchDTO searchDTO);

    List<SearchResultDTO> advancedSearch(ComplexSearchDTO searchDTO);

    List<ReviewerSearchResultDTO> reviewersSearch(ReviewerSearchDTO searchDTO) throws IOException;

    List<ReviewerSearchResultDTO> reviewersSearchTest(String authorUsername, String pdfPath) throws IOException;




}
