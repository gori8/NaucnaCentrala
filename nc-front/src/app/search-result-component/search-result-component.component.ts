import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SearchServiceService } from '../_services/search/search-service.service';
import { RouterModule, Routes } from '@angular/router';


@Component({
  selector: 'app-search-result-component',
  templateUrl: './search-result-component.component.html',
  styleUrls: ['./search-result-component.component.scss']
})
export class SearchResultComponentComponent implements OnInit {

  constructor(private route : ActivatedRoute, private searchService : SearchServiceService) { }

  results=[];
  query={};
  searchBy={};

  ngOnInit() {
    this.searchBy=this.route.snapshot.paramMap.get('searchby');
    this.query=this.route.snapshot.paramMap.get('query');
    var searchDTO = {
      field : {} ,
      query : {}     
    }
    searchDTO.field=this.searchBy;
    searchDTO.query=this.query;
    this.searchService.search(searchDTO).subscribe(
      res =>{
        console.log(res);
        this.results=res;
    },err=>{
      console.log(err);
    });
  }

}
