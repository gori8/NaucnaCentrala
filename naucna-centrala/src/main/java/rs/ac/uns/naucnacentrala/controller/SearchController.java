package rs.ac.uns.naucnacentrala.controller;


import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.naucnacentrala.dto.ComplexSearchDTO;
import rs.ac.uns.naucnacentrala.dto.SearchDTO;
import rs.ac.uns.naucnacentrala.service.SearchService;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/restapi/search")
@CrossOrigin("*")
public class SearchController {

    @Autowired
    SearchService searchService;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity searchByField(@RequestBody SearchDTO searchDTO)  {
        return ResponseEntity.ok(searchService.searchByField(searchDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST, value = "/advanced")
    public ResponseEntity advancedSearch(@RequestBody ComplexSearchDTO searchDTO)  {
        return ResponseEntity.ok(searchService.advancedSearch(searchDTO));
    }

}
