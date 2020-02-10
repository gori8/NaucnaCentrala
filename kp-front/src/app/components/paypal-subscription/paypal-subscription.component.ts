import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';

@Component({
  selector: 'app-paypal-subscription',
  templateUrl: './paypal-subscription.component.html',
  styleUrls: ['./paypal-subscription.component.scss']
})
export class PaypalSubscriptionComponent implements OnInit {

  private body={
    "tipCiklusa":"INFINITE",
    "brojCiklusa":0,
    "period":"DAY",
    "casopisUuid":null,
    "ucestalostPerioda":1
  }

  private hidden = false;

  constructor(private activatedRoute: ActivatedRoute, private endpoints: EndpointsService) { }

  ngOnInit() {
    this.body.casopisUuid=this.activatedRoute.snapshot.paramMap.get('uuid');
  }

  onSubmit(form){
    this.hidden=true;
    if(form.valid===true){
      if(this.body.tipCiklusa=="INFINITE"){
        this.body.brojCiklusa=0;
      }
      console.log(this.body);
    
      this.endpoints.paypalSubscription(this.body).subscribe(
        res => {
            console.log(res);
            window.location.href = res;
        },
        err => {
          this.hidden=false;
          console.log(err);  
        }
      );
    }
  }

}
