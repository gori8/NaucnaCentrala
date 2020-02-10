import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_JOURNAL_CONTROLLER_URL = "https://localhost:8080/restapi/journal"
const BASE_BPMN_CONTROLLER_URL = "https://localhost:8080/restapi/bpmn"

@Injectable({
  providedIn: 'root'
})
export class ScienceJournalService {

  constructor(private http: HttpClient) { }


  getNewJournalForm(): Observable<any>{
    return this.http.get(`${BASE_JOURNAL_CONTROLLER_URL}`);
  }

  
  getMineJournals(): Observable<any>{
    return this.http.get(`${BASE_JOURNAL_CONTROLLER_URL}/mine`);
  }
  

}
