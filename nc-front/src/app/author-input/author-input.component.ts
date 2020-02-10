import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { NotifierService } from 'angular-notifier';
import { FileUploader } from 'ng2-file-upload';
import { AuthenticationService } from '../_services/authentication/authentication.service';

@Component({
  selector: 'app-author-input',
  templateUrl: '../_common/generic-form.html',
  styleUrls: ['./author-input.component.scss']
})
export class AuthorInputComponent implements OnInit {

  constructor(private authService : AuthenticationService, private notifierService : NotifierService, private modalService : NgbModal, private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router, private route : ActivatedRoute) { }

  private formFieldsDto = null;
  private formFields = [];
  private processInstanceID = "";
  private enumValues = [];
  private multiselectList={};
  private selectedItems={};
  private propertyType={};
  private dropdownSettings={};
  private child=new Object();
  private uploader: FileUploader;
  private uploadingField;

  @ViewChild('fileuploader',{static: false})
  fileuploaderVar: ElementRef;


  ngOnInit() {

    this.uploader= new FileUploader({ url: "https://localhost:8080/restapi/paper", removeAfterUpload: false, authToken: "Bearer " + this.authService.currentUserValue.token});

    console.log(this.uploader);

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 5,
      allowSearchFilter: true
    };

    this.processInstanceID=this.route.snapshot.paramMap.get('processInstanceID');

    this.bpmnService.getActiveTaskForm(this.processInstanceID).subscribe(
      res => {
        console.log(res);
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstanceID = res.processInstanceId;
        this.formFields.forEach( (field) => {
          if(field.properties.type=='url'){
            window.location.href=field.value.value;
          }
          this.propertyType[field.id]=field.type.name;
          if(field.type.name.includes('add-children')){
            field.value.value=JSON.parse(field.value.value);
          }
          if(field.properties['type']=='json' && this.isReadonly(field.validationConstraints)){
                    field.value.value=JSON.parse(field.value.value);
                  }
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

  addChild(child,fieldID){
    let field = this.formFields.find(field => field.id==fieldID);
    let clone = {...child};
    field.value.value.push(clone);
    child={};
  }

  openChild(content,fieldID) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {
      
    }, (reason) => {
    });
  }

  

  onSubmit(value, form){

    let o = new Array();

    this.uploader.onSuccessItem = (item:any, response:any, status:any, headers:any) => {
      value[this.uploadingField]=response;
      console.log('Field: ',value[this.uploadingField]);
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
          if(this.propertyType[property].includes('add-children')){
            value[property] = JSON.stringify(value[property]);
            console.log(value[property]);
          }
          o.push({fieldId : property, fieldValue : value[property]});
        }
      }
      console.log(o);
      let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

      x.subscribe(
        res => {
          this.notifierService.notify("info","Podaci su poslati uredniku na proveru");
          this.router.navigate(['tasks']);
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

    this.upload();
    
  }

  fileSelectionChanged(field){
    this.uploadingField=field.id;
  }

  upload(){
    
    this.uploader.uploadItem(this.uploader.queue[0]);
  }

  isReadonly(constraints){
    if(constraints.length!=0){
      if(constraints[0].name=='readonly')
        return true;
      else
        return false;
    }else{
      return false;
    }
  }

  chooseFile(){
    this.fileuploaderVar.nativeElement.click();
  }

  removeFromList(list,x){
    list.splice(x, 1);
  }

}
