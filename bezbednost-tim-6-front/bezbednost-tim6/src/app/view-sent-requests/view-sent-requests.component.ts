import { Component, OnInit, Output } from '@angular/core';
import { CertificateRequestService } from '../backend-services/certificate-request.service';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import { InputReasonComponent } from '../input-reason/input-reason.component';
import { CertificateRequestDTO } from '../view-received-requests/view-received-requests.component';

@Component({
  selector: 'app-view-sent-requests',
  templateUrl: './view-sent-requests.component.html',
  styleUrls: ['./view-sent-requests.component.css']
})
export class ViewSentRequestsComponent implements OnInit {

  @Output() request!:CertificateRequestDTO;
  requests!: CertificateRequestDTO[];

  constructor(private router: Router, private service: CertificateRequestService) { }

  ngOnInit(): void {
    this.getAllReceivedRequests();
  }

  private getAllReceivedRequests() {
    this.service.getSent()
    .subscribe(data => {
      console.log(data);
        this.requests = data.results;
      }
      , error => {
        console.log(error.error.message);
      }
    );
  }

}
