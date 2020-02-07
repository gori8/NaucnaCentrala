import { Component, OnInit } from '@angular/core';
import { PaperServiceService } from '../_services/paper/paper-service.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { NotifierService } from 'angular-notifier';
import { Router } from '@angular/router';

@Component({
  selector: 'app-author-select-journal',
  templateUrl: './author-select-journal.component.html',
  styleUrls: ['./author-select-journal.component.scss']
})
export class AuthorSelectJournalComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];
  private processInstanceID = "";
  private enumValues = [];
  private multiselectList={};
  private selectedItems={};
  private propertyType={};
  private dropdownSettings={};

  constructor(private paperService : PaperServiceService, private bpmnService : BpmnService, private notifierService : NotifierService, private router : Router) {  }

  ngOnInit() {
    this.paperService.getCasopisiAndStartProcess().subscribe(
        res => {
          this.formFieldsDto = res;
               this.formFields = res.formFields;
               console.log(res);
               this.processInstanceID = res.processInstanceId;
               this.formFields.forEach( (field) => {
                 this.propertyType[field.id]=field.type.name;
                 if( field.type.name=='enum'){
                   this.enumValues = Object.keys(field.type.values);
                 }
               })
        },
        err =>{

        }
      )
  }

  onSelect(fieldId,key){
    let o = new Array();
    o.push({'fieldId' : fieldId, 'fieldValue' : key});
    console.log(o);
    let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

    x.subscribe(
      res => {
        this.notifierService.notify("success","Bravo!");
        this.router.navigate(['input/paper/'+this.processInstanceID])
      },
      err => {
              console.log(err);
              this.formFieldsDto.taskId=err.error["taskID"];
              let map=new Map(Object.entries(err.error));
              map.delete("taskID");
              for(let key of Array.from( map.keys()) ) {
               let field=this.formFields.find(field => field.id==key);  
               field.err=true;
               field.errMsg=map.get(key);
        }
      }
    );
  }

}
