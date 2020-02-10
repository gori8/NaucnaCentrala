import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_REG_CONTROLLER_URL = "https://localhost:8080/restapi/registration"
const BASE_BPMN_CONTROLLER_URL = "https://localhost:8080/restapi/bpmn"


@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) { }


  getRegistrationForm(): Observable<any>{
    return this.http.get(`${BASE_REG_CONTROLLER_URL}`);
  }

  getUrednikRegistrationForm(): Observable<any>{
    return this.http.get(`${BASE_BPMN_CONTROLLER_URL}/urednik`);
  }

  checkValidity(processInstanceID): Observable<any>{
    return this.http.get(`${BASE_REG_CONTROLLER_URL}/valid/`+processInstanceID);
  }

  getNonActivatedUsers(): Observable<any>{
    return this.http.get(`${BASE_BPMN_CONTROLLER_URL}/admin/registration`);
  }

  
}
