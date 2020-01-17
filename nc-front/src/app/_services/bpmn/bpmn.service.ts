import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_CONTROLLER_URL = "http://localhost:8080/restapi/bpmn"

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
}
