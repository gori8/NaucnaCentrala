import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service';

@Component({
  selector: 'app-izbor-placanja',
  templateUrl: './izbor-placanja.component.html',
  styleUrls: ['./izbor-placanja.component.scss']
})
export class IzborPlacanjaComponent implements OnInit {

  constructor(private router: Router, private endpoints: EndpointsService,private activatedRoute: ActivatedRoute) { }

  naciniPlacanja = [];
  selectedPaymentMethod = null;
  uuid="";
  private hidden = false;

  ngOnInit() {

    this.uuid = this.activatedRoute.snapshot.paramMap.get('uuid');
    console.log(this.uuid);

    this.getNacinePlacanjaZaCasopis();
    
  }

  submitPaymetMethod(){

    this.hidden=true;

    for(let np of this.naciniPlacanja){
      if(np.value == this.selectedPaymentMethod){
        this.endpoints.callSelectedMicroservice(np.url,this.uuid).subscribe(
          res => {
              console.log(res);
              window.location.href = res;
          },
          err => {
            console.log(err);
            this.hidden=false;
            alert("An error has occured while usgin url "+np.url);
          }
        )
        break;
      }
    }
  }

  public getNacinePlacanjaZaCasopis(){
    this.endpoints.getNacinePlacanjaZaCasopis(this.uuid).subscribe(
      res => {
        this.naciniPlacanja = res;
        if(res[0] !== undefined){
          this.selectedPaymentMethod = res[0].value;
        }
      },
      err => {
        alert("An error has occured while getting payment methods")
      }
    )
  }

}
