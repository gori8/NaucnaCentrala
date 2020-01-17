import { Component, OnInit } from '@angular/core';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-admin-journals',
  templateUrl: './admin-journals.component.html',
  styleUrls: ['./admin-journals.component.scss']
})
export class AdminJournalsComponent implements OnInit {

	private casopisi;

  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService,private toastr : ToastrService) { }

  ngOnInit() {
  	this.scienceJournalService.getNonActivatedJournals().subscribe(
		res => {
               this.casopisi=res;
            },
        err => {
              console.log(err);
              alert("An error has occured!");
            });
  }

  onClick(taskID,flag){
    let dtos=new Array();
    dtos.push({fieldId : "potvrda_admina", fieldValue : flag});
    this.bpmnService.postProtectedFormData(taskID,dtos).subscribe(
        res => {
          this.toastr.success("Accepted successfully")
          this.ngOnInit();
        },
        err => {
          console.log(err);
          alert("Error Occured!");
        }
      );
  }

}
