import { Component, OnInit } from '@angular/core';
import { NotifierService } from "angular-notifier";
import { AuthenticationService } from '../_services/authentication/authentication.service';
import { Observable } from 'rxjs';
import { User } from '../_model/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  currentUser$: Observable<User>;

  searchByVal="naslov";

  constructor(private authenticationService : AuthenticationService) { 
    this.currentUser$=this.authenticationService.currentUser;
  }

  ngOnInit() {

  }

}
