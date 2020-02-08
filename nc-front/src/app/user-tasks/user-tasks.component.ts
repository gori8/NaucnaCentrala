import { Component, OnInit } from '@angular/core';
import { BpmnService } from '../_services/bpmn/bpmn.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-tasks',
  templateUrl: './user-tasks.component.html',
  styleUrls: ['./user-tasks.component.scss']
})
export class UserTasksComponent implements OnInit {

  private tasks;


  constructor(private bpmnService : BpmnService, private router : Router) { }

  ngOnInit() {
    this.bpmnService.getMyTasks().subscribe(
      res => {
        console.log(res);
        this.tasks=res;
      },
      err => {
        console.log(err);
        alert("An error has occured!");
      });
  }

  onClick(taskId){
    this.router.navigate(['task',taskId]);
  }

}
