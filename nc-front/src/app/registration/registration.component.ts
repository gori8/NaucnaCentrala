import { Component, OnInit } from '@angular/core';
import { AppRoutingModule } from '../app-routing.module';
import { RegistrationService } from '../_services/registration/registration.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NotifierService } from "angular-notifier";



@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {


   constructor(private registrationService : RegistrationService, private bpmnService : BpmnService, private router : Router, private route : ActivatedRoute, private notifierService : NotifierService) { }

  private formFieldsDto = null;
  private formFields = [];
  private processInstanceID = "";
  private enumValues = [];
  private multiselectList={};
  private selectedItems={};
  private propertyType={};
  private dropdownSettings={};

  ngOnInit() {

      this.dropdownSettings = {
        singleSelection: false,
        idField: 'item_id',
        textField: 'item_text',
        selectAllText: 'Select All',
        unSelectAllText: 'UnSelect All',
        itemsShowLimit: 5,
        allowSearchFilter: true
      };


      this.registrationService.getRegistrationForm().subscribe(
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
                 if(field.type.name.includes('multi-select')){
                   this.selectedItems[field.id]=[];
                   this.multiselectList[field.id]=[];
                   let map=new Map(Object.entries(field.type.values));
                   //let values = Object.values(field.type.values);
                   for(let key of Array.from(map.keys())){
                     var item={item_id:"",item_text:""};
                     item.item_id=key;
                     item.item_text=map.get(key);
                     console.log(item);
                     this.multiselectList[field.id].push(item);
                    let selVals=field.value.value;
                    console.log("SelVals:",selVals);
                    if(selVals!=null){
                      if(selVals.includes(key)){
                        this.selectedItems[field.id].push(item);
                      }
                    }
                   }
                 }
               })
            },
        err => {
                console.log(err);
                
              });
  }

  onItemSelect(item: any) {
    //console.log(item);
  }
  onSelectAll(items: any) {
    //console.log(items);
  }

  onSubmit(value, form){

    console.log(this.propertyType);
    let o = new Array();
    for (var property in value) {
      let fieldReset=this.formFields.find(field => field.id==property);
      fieldReset.err=false;
      fieldReset.errMsg=null;
      if(this.propertyType[property].includes('multi-select')){
        let arr = [];
        for(let itm of value[property]){
          arr.push(itm.item_id);
        }
        o.push({fieldId: property, fieldValue: arr})
      }else{
        o.push({fieldId : property, fieldValue : value[property]});
      }
    }

    console.log(o);
    let x = this.bpmnService.postFormData(this.formFieldsDto.taskId,o);

    x.subscribe(
      res => {
        this.notifierService.notify("info","Please confrim your registration via email.");
        this.router.navigate(['/login']);
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
