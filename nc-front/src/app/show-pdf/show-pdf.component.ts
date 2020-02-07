import { Component, OnInit } from '@angular/core';
import { PaperServiceService } from '../_services/paper/paper-service.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-show-pdf',
  templateUrl: './show-pdf.component.html',
  styleUrls: ['./show-pdf.component.scss']
})
export class ShowPdfComponent implements OnInit {

  private src;

  constructor(private paperService : PaperServiceService, private modalService : NgbModal) { }

  ngOnInit() {
      this.paperService.getCasopisPdf("proba.pdf").subscribe(
          res => {
            console.log(res);
            this.src=res;
          },
          err => {

          }
        )
  }

  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title',windowClass : "myCustomModalClass"}).result.then((result) => {
      console.log(result);
    }, (reason) => {
      console.log(reason);
    });
  }

}
