import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PaperServiceService } from '../_services/paper/paper-service.service';
import { FileUploader } from 'ng2-file-upload';
import { AuthenticationService } from '../_services/authentication/authentication.service';
import { NotifierService } from 'angular-notifier';

@Component({
  selector: 'app-active-task',
  templateUrl: '../_common/generic-form.html',
  styleUrls: ['./active-task.component.scss']
})
export class ActiveTaskComponent implements OnInit {

  constructor(private authService : AuthenticationService, private notifierService : NotifierService, private paperService : PaperServiceService, private modalService : NgbModal, private scienceJournalService : ScienceJournalService, private bpmnService : BpmnService, private router : Router, private route : ActivatedRoute) { }

  private formFieldsDto = null;
  private formFields = [];
  private processInstanceID = "";
  private enumValues = [];
  private multiselectList={};
  private selectedItems={};
  private propertyType={};
  private dropdownSettings={};
  private src;
  private taskId;
  private uploader: FileUploader;
  private hasUpload = false;
  private uploadingField;

  @ViewChild('fileuploader',{static: false})
  fileuploaderVar: ElementRef;


  ngOnInit() {

    this.uploader= new FileUploader({ url: "https://localhost:8080/restapi/paper", removeAfterUpload: false, authToken: "Bearer " + this.authService.currentUserValue.token});


    this.dropdownSettings = {
      singleSelection: false,
      idField: 'item_id',
      textField: 'item_text',
      selectAllText: 'Select All',
      unSelectAllText: 'UnSelect All',
      itemsShowLimit: 5,
      allowSearchFilter: true
    };

    this.taskId=this.route.snapshot.paramMap.get('taskID');

    this.bpmnService.getTask(this.taskId).subscribe(
      res => {
             
                this.formFieldsDto = res;
                this.formFields = res.formFields;
                console.log(res);
                this.processInstanceID = res.processInstanceId;
                this.formFields.forEach( (field) => {
                  
                  this.propertyType[field.id]=field.type.name;

                  if(field.properties['type']=='file' && !this.isReadonly(field.validationConstraints)){
                    this.hasUpload=true;
                  }

                  if(field.properties['type']=='file' && this.isReadonly(field.validationConstraints)){
                    this.paperService.getCasopisPdf(field.defaultValue).subscribe(
                      res => {
                        this.src=res;
                      },
                      err => {

                      }
                    )
                  }
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
                this.router.navigate(['stagod']);
              });
  }

  onItemSelect(item: any) {
  }
  onSelectAll(items: any) {
  }

  fileSelectionChanged(field){
    this.uploadingField=field.id;
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

  openJson(content){
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {      
    }, (reason) => {
    });
  }

  openPdf(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {

    }, (reason) => {

    });
  }

  onSubmit(value, form){
    if(this.hasUpload){
      this.uploader.onSuccessItem = (item:any, response:any, status:any, headers:any) => {
        value[this.uploadingField]=response;
        console.log(this.propertyType);
        let o = new Array();
        for (var property in value) {
          let fieldReset=this.formFields.find(field => field.id==property);
          fieldReset.err=false;
          fieldReset.errMsg=null;
          if(!this.isReadonly(fieldReset.validationConstraints)){
            if(this.propertyType[property].includes('multi-select')){
              let arr = [];
              for(let itm of value[property]){
                arr.push(itm.item_id);
              }
              o.push({fieldId: property, fieldValue: arr})
            }else{
              if(this.propertyType[property].includes('add-children')){
                value[property] = JSON.stringify(value[property]);
              }
              o.push({fieldId : property, fieldValue : value[property]});
            }
          }
        }

        console.log(o);
        let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

        x.subscribe(
          res => {
            this.router.navigate(['tasks']);
            this.notifierService.notify("success","Task completed successfully");
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
    }else{
      console.log(this.propertyType);
        let o = new Array();
        for (var property in value) {
          let fieldReset=this.formFields.find(field => field.id==property);
          fieldReset.err=false;
          fieldReset.errMsg=null;
          if(!this.isReadonly(fieldReset.validationConstraints)){
            if(this.propertyType[property].includes('multi-select')){
              let arr = [];
              for(let itm of value[property]){
                arr.push(itm.item_id);
              }
              o.push({fieldId: property, fieldValue: arr})
            }else{
              if(this.propertyType[property].includes('add-children')){
                value[property] = JSON.stringify(value[property]);
              }
              o.push({fieldId : property, fieldValue : value[property]});
            }
          }
        }

        console.log(o);
        let x = this.bpmnService.postProtectedFormData(this.formFieldsDto.taskId,o);

        x.subscribe(
          res => {
            this.router.navigate(['tasks']);
            this.notifierService.notify("success","Task completed successfully");
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
