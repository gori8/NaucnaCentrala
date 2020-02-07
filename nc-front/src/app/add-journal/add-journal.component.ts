import { Component, OnInit } from '@angular/core';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-add-journal',
  templateUrl: './add-journal.component.html',
  styleUrls: ['./add-journal.component.scss']
})
export class AddJournalComponent implements OnInit {

  constructor(private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router) { }

  private formFieldsDto = null;
  private formFields = [];
  private processInstanceID = "";
  private enumValues = [];
  private multiSelectValues;

  ngOnInit() {
    	this.scienceJournalService.getNewJournalForm().subscribe(
    		res => {
               this.formFieldsDto = res;
               this.formFields = res.formFields;
               console.log(res);
               this.processInstanceID = res.processInstanceId;
               this.formFields.forEach( (field) => {
                 if( field.type.name=='enum'){
                   this.enumValues = Object.keys(field.type.values);
                 }
               })
            },
        err => {
              console.log(err);
              alert("An error has occured!");
            });
  }

  onSubmit(value, form){
    let o = new Array();
    for (var property in value) {
      if(property=="nacin_placanja" || property=="naucne_oblasti"){
        value[property]=new Array();
        value[property].push("1","2");
        console.log(value[property]);
      }
      o.push({fieldId : property, fieldValue : value[property]});
    }

    console.log(o);
    let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

    x.subscribe(
      res => {
        this.router.navigate(['/journal/activate', this.processInstanceID]);
      },
      err => {
              console.log(err);
              this.formFieldsDto.taskId=err["taskID"];
              let map=new Map(Object.entries(err));
              map.delete("taskID");
              for(let key of Array.from( map.keys()) ) {
                 if(key=="unique"){
                  alert("Username or email are not unique!");
                  let fieldUsername=this.formFields.find(field => field.id=="username");
                  let fieldEmail=this.formFields.find(field => field.id=="email");
                  fieldUsername.err=true;
                  fieldEmail.err=true;
                  break;
                }
               let field=this.formFields.find(field => field.id==key);  
               field.err=true;
               field.errMsg=map[key];
        }
      }
    );
  }

}
