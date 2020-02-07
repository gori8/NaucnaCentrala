import { Component, OnInit } from '@angular/core';
import { NotifierService } from "angular-notifier";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor(private notifierService: NotifierService) { }

  ngOnInit() {
  }

}
