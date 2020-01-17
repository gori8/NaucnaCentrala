import { Component, OnInit } from '@angular/core';
import { RegistrationService } from '../_services/registration/registration.service';
import { BpmnService } from '../_services/bpmn/bpmn.service';

@Component({
  selector: 'app-admin-registrations',
  templateUrl: './admin-registrations.component.html',
  styleUrls: ['./admin-registrations.component.scss']
})
export class AdminRegistrationsComponent implements OnInit {

  private users;


  constructor(private registrationService : RegistrationService, private bpmnService : BpmnService) { }

  ngOnInit() {
    this.registrationService.getNonActivatedUsers().subscribe(
    res => {
               this.users=res;
            },
        err => {
              console.log(err);
              alert("An error has occured!");
            });
  }

  onClick(taskID,flag){
    let dtos=new Array();
    dtos.push({fieldId : "potvrdi_recezent", fieldValue : flag});
    this.bpmnService.postProtectedFormData(taskID,dtos).subscribe(
        res => {
          this.ngOnInit();
        },
        err => {
          console.log(err);
          alert("Error Occured!");
        }
      );
  }

}
