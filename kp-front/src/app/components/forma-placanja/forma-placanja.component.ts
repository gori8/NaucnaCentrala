import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EndpointsService } from 'src/app/services/endpoints.service.js';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-forma-placanja',
  templateUrl: './forma-placanja.component.html',
  styleUrls: ['./forma-placanja.component.scss']
})
export class FormaPlacanjaComponent implements OnInit {

  constructor(private sanitizer: DomSanitizer,private endpoints:EndpointsService,private activatedRoute: ActivatedRoute) {}

  private image=null;
  private uuid=null;
  private body={};
  private nacinPlacana=null;
  private json=null;
  private button="Submit";
  private header="";
  private rows = [
    {
      cols:[]
    }
  ]
  private imageType : string = 'data:image/PNG;base64,';

  ngOnInit() {

    this.nacinPlacana = this.activatedRoute.snapshot.paramMap.get('nacinPlacanja');
    this.uuid = this.activatedRoute.snapshot.paramMap.get('uuid');

    this.getJson();
  }

  getJson(){
    this.endpoints.getJSON(this.nacinPlacana,this.uuid).subscribe(
      res => {
        this.json=res;
       
        for(let field of this.json.form){

          if(field.default!=null){
            field.model=field.default;
          }

          if(field.validation===undefined){
            field.validation = {pattern:".*"};
          }else if(field.validation.pattern===undefined){
            field.validation.pattern = ".*";
          }
    
          if(field.row != null || field.col != null){
            if(this.rows[field.row] === undefined){
              this.rows[field.row]={cols:[]};
            }
            this.rows[field.row].cols[field.col] = field;
          }
        }
    
        this.rows = this.rows.filter(function (el) {
          return el != null;
        });
    
        for(let r of this.rows){
          r.cols= r.cols.filter(function (el) {
            return el != null;
          });
        }
    
        if(this.json.button !== undefined && this.json.button.name !== undefined){
          this.button = this.json.button.name;
        }
    
        if(this.json.header !== undefined){
          this.header = this.json.header;
        }
        
        if(this.json.image!=null){
          var type = this.json.image.split('.')[1];
          this.imageType = `data:image/${type};base64,`
          this.getImage(this.nacinPlacana,this.json.image);
        }
      },

      err => {
        console.log(err);
        alert("Error while getting a payment form");
      }
    );
  }

  getImage(folder,name){  
    this.endpoints.getImage(folder,name)
        .subscribe((data :any ) => {
            this.image = this.sanitizer.bypassSecurityTrustUrl(this.imageType + data.content);
    });
  }

  onSubmit(paymentForm){
    if(paymentForm.valid===true){
      for(let item of this.json.form){
        if(item.type!="reset"){
          this.body[item.id]=item.model;
        }
      }
      this.body["uuid"]=this.uuid;
      this.body["sellerEmail"]=this.json.sellerEmail;

      this.registerOnMs();
    }
  }

  registerOnMs(){
    console.log(this.body);
    this.endpoints.registerOnMs(this.body,this.json.button.url).subscribe(
      res => {
        this.paymentRegistrationCompleted();
      },
      err => {
        console.log(err);
        alert("Error while registrating on microservice");
      }
    );
  }

  paymentRegistrationCompleted(){

    var body = {
      "uuid":this.uuid,
      "nacinPlacanjaId":this.nacinPlacana
    }

    this.endpoints.paymentRegistrationCompleted(body).subscribe(
      res => {
        window.location.href = res;
      },
      err => {
        console.log(err);
        alert("Error while redirecting");
      }
    );
  }
}
