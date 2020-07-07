import { Component, OnInit, ɵNOT_FOUND_CHECK_ONLY_ELEMENT_INJECTOR } from '@angular/core';
import { NotifierService } from 'angular-notifier';
import { SearchServiceService } from '../_services/search/search-service.service';
import { RouterModule, Routes } from '@angular/router';

@Component({
  selector: 'app-advanced-search',
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss']
})
export class AdvancedSearchComponent implements OnInit {

  query={
    naslov:null,
    kljucneReci:null,
    apstrakt:null,
    naucnaOblast:null,
    sadrzaj:null,
    autor:null,
  }

  boolQueryGroups = {
    groups:[],
    operator:{}
  }
  
  resultsShown=false;
  results=[];

  radovi=[];

  private readonly notifier: NotifierService;

  constructor(notifierService: NotifierService, private searchService : SearchServiceService) { 
    this.notifier = notifierService;
  }

  ngOnInit() {
    this.boolQueryGroups.operator = 'AND' as String;
  }

  searchByField(field,value){

    var simpleQuery={
      field:field,
      value:value
    }

    
  }

  addGroup(){

    var group = {
      elements:[],
      type:{}
    }

    group.type='AND' as String;

    this.boolQueryGroups.groups.push(group);
    
  }

  removeGroup(){
    var index = this.boolQueryGroups.groups.length-1;
    this.boolQueryGroups.groups.splice(index,1);
  }

  addElement(group){
    
    var element = {
      field:null,
      searchText:null
    }

    group.elements.push(element);

    
  }

  removeElement(group){
    var index = group.elements.length-1;
    group.elements.splice(index,1);
    group.operators.splice(index,1);
  }

  executeBool(){

    console.log(this.boolQueryGroups);

    this.searchService.advancedSearch(this.boolQueryGroups).subscribe(
      res => {
        console.log(res);
        this.resultsShown=true;
        this.results=res;
      }, err => {
        console.log(err);
      }
      );
    
  }

  onGroupTypeChanged(group){
    
  }
}
