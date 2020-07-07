import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

const BASE_SEARCH_CONTROLLER_URL = "http://localhost:8080/restapi/search"

@Injectable({
  providedIn: 'root'
})
export class SearchServiceService {

  constructor(private http: HttpClient) { }


  advancedSearch(groups): Observable<any>{
    return this.http.post(`${BASE_SEARCH_CONTROLLER_URL}/advanced`,groups);
  }

  search(dto): Observable<any>{
    return this.http.post(`${BASE_SEARCH_CONTROLLER_URL}`,dto);
  }


}
