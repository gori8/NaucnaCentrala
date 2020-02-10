import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_CONTROLLER_URL = "https://localhost:8080/restapi/bpmn"

@Injectable({
  providedIn: 'root'
})
export class BpmnService {

  constructor(private http: HttpClient) { }


  postFormData(taskID,formData): Observable<any>{
    return this.http.post(`${BASE_CONTROLLER_URL}/form/`+taskID,formData);
  }

  postProtectedFormData(taskID,formData): Observable<any>{
    return this.http.post(`${BASE_CONTROLLER_URL}/protected/form/`+taskID,formData);
  }

  getActiveTaskForm(processInstanceId): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/task/active/`+processInstanceId);
  }
 
  getNonActivatedJournals(): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/admin/journal`);
  }

  getTask(id): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/tasks/`+id);
  }

  getMyTasks(): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}/tasks`);
  }

}
