import { Component, OnInit } from '@angular/core';
import { PaperServiceService } from '../_services/paper/paper-service.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-show-pdf',
  templateUrl: './show-pdf.component.html',
  styleUrls: ['./show-pdf.component.scss']
})
export class ShowPdfComponent implements OnInit {

  private src;

  constructor(private paperService : PaperServiceService, private modalService : NgbModal, private route : ActivatedRoute) { }

  ngOnInit() {
       this.paperService.getCasopisPdf(this.route.snapshot.paramMap.get('pdfpath')).subscribe(
          res => {
            console.log(res);
            this.src=res;
          },
          err => {

          }
        );
  }

 
  

}
