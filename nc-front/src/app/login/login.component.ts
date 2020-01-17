import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { AuthenticationService } from '../_services/authentication/authentication.service';
import { first } from 'rxjs/operators';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loading = false;
  submitted = false;
  returnUrl: string;
  error = '';

	loginForm = new FormGroup({
    	username: new FormControl(''),
      password: new FormControl(''),
  	});

  constructor(private authenticationService : AuthenticationService,private router: Router, private route: ActivatedRoute) { }

  ngOnInit() {
    this.authenticationService.logout();

      // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  onSubmit(){
  	this.submitted = true;

      // stop here if form is invalid
      if (this.loginForm.invalid) {
          return;
      }

      this.loading = true;
      this.authenticationService.login(this.loginForm.value["username"],this.loginForm.value["password"])
          .pipe(first())
          .subscribe(
              data => {
                  this.router.navigate([this.returnUrl]);
              },
              err => {
                console.log(err);
                  this.error = err;
                  this.loading = false;
              });
  }

}
