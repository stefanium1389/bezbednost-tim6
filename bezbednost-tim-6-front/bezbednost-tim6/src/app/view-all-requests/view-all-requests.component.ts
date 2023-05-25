import { Component, OnInit, Output } from '@angular/core';
import { CertificateRequestService } from '../backend-services/certificate-request.service';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import { InputReasonComponent } from '../input-reason/input-reason.component';
import { CertificateRequestDTO } from '../view-received-requests/view-received-requests.component';

@Component({
  selector: 'app-view-all-requests',
  templateUrl: './view-all-requests.component.html',
  styleUrls: ['./view-all-requests.component.css']
})
export class ViewAllRequestsComponent implements OnInit {

  @Output() request!:CertificateRequestDTO;
  requests!: CertificateRequestDTO[];


  constructor(private router: Router, private service: CertificateRequestService) { }

  ngOnInit(): void {
    this.getAllReceivedRequests();
  }

  private getAllReceivedRequests() {
    this.service.getAll()
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
