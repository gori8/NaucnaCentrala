import { Injectable } from '@angular/core';
import { HttpClient,  HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_CONTROLLER_URL = "https://localhost:8080/restapi/paper"

@Injectable({
  providedIn: 'root'
})
export class PaperServiceService {

  constructor(private http: HttpClient) { }

  private headers = new HttpHeaders();
  

  getCasopisiAndStartProcess(): Observable<any>{
    return this.http.get(`${BASE_CONTROLLER_URL}`);
  }

  getCasopisPdf(filename): Observable<any>{
    this.headers = this.headers.set('Content-Type', 'application/pdf');
    return this.http.get(`${BASE_CONTROLLER_URL}/files/`+filename,{headers: this.headers,
    responseType: 'blob'});
  }
  
}
