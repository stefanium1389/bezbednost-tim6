import { Component, OnInit, Output } from '@angular/core';
import { CertificateRequestService } from '../backend-services/certificate-request.service';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import { InputReasonComponent } from '../input-reason/input-reason.component';

@Component({
  selector: 'app-view-received-requests',
  templateUrl: './view-received-requests.component.html',
  styleUrls: ['./view-received-requests.component.css']
})
export class ViewReceivedRequestsComponent implements OnInit {

  @Output() request!:CertificateRequestDTO;
  requests!: CertificateRequestDTO[];


  constructor(private router: Router, private service: CertificateRequestService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.getAllReceivedRequests();
  }

  private getAllReceivedRequests() {
    this.service.getReceived()
    .subscribe(data => {
      console.log(data);
        this.requests = data.results;
      }
      , error => {
        console.log(error.error.message);
      }
    );
  }

  accept(id:number) {
    this.service.accept(id)
    .subscribe(data => {
      alert("Successfully accepted");
      this.getAllReceivedRequests();
      }
      , error => {
        console.log(error.error.message);
      }
    );
  }

  reason!: string;
  rejection!: Reason;
  reject(id:number) {
    let obj: Reason;

    const dialogRef = this.dialog.open(InputReasonComponent, {
      data: {reason: this.reason},
      panelClass: 'my-dialog-container-class',
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result.reason != undefined ) {
        obj = {
          "reason": result.reason
        }
        this.rejection = createReason(result.reason);
         
        this.service.reject(id, this.rejection)
        .subscribe(data => {
            alert("Successfully rejected");
            this.getAllReceivedRequests();
        }
      , error => {
        console.log(error.error.message);
      }
    );
    }
  });
}
}

export interface Reason {
  reason: string;
}
export function createReason(reason: string): Reason {
  return {reason};
}

export interface CertificateRequestDTO {
  id : number;
  certificateType : string;
  issuerCertificateId: number;
  userId: number;
  userEmail: string;
  timeOfRequest: string;
  status: string;
  commonName: string;
  rejection: string;
}