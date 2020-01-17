import { Component, OnInit } from '@angular/core';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-urednik-journals',
  templateUrl: './urednik-journals.component.html',
  styleUrls: ['./urednik-journals.component.scss']
})
export class UrednikJournalsComponent implements OnInit {

  private casopisi;


  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router) { }

  ngOnInit() {
    this.scienceJournalService.getMineJournals().subscribe(
    res => {
               console.log(res);
               this.casopisi=res;
            },
        err => {
              console.log(err);
              alert("An error has occured!");
            });
  }

  onClick(processInstanceId){
    console.log("processInstanceId :",processInstanceId);
    this.router.navigate(['journal/activate',processInstanceId]);
  }

}
