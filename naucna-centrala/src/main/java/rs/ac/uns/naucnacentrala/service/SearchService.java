package rs.ac.uns.naucnacentrala.service;

import rs.ac.uns.naucnacentrala.dto.ComplexSearchDTO;
import rs.ac.uns.naucnacentrala.dto.SearchDTO;
import rs.ac.uns.naucnacentrala.dto.SearchResultDTO;

import java.util.List;

public interface SearchService {

    List<SearchResultDTO> searchByField(SearchDTO searchDTO);

    List<SearchResultDTO> advancedSearch(ComplexSearchDTO searchDTO);


}
