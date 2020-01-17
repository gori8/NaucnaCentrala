import { Component, OnInit } from '@angular/core';
import { ScienceJournalService } from '../_services/science-journal/science-journal.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../_services/authentication/authentication.service';
import { Observable } from 'rxjs';
import { User } from '../_model/user';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

   currentUser$: Observable<User>;

  constructor(private scienceJournalService : ScienceJournalService,private router : Router, private authenticationService : AuthenticationService) { 
    this.currentUser$=this.authenticationService.currentUser;
  }

  ngOnInit() {
  }

  newJournal(){
    this.scienceJournalService.getNewJournalForm().subscribe(
        res => {
          this.router.navigate(['/journal/activate', res.processInstanceId]);
        },
        err => {
          alert("Error occured!");
        }
      );
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
  }

}
