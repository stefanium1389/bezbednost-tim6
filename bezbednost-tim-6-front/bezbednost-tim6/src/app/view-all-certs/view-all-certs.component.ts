import { Component, OnInit } from '@angular/core';
import { CertificateService } from '../backend-services/certificate.service';
import { DomSanitizer } from '@angular/platform-browser';
import {MatDialog} from "@angular/material/dialog";
import { InputReasonComponent } from '../input-reason/input-reason.component';
import { Reason, createReason } from '../view-received-requests/view-received-requests.component'; 

@Component({
  selector: 'app-view-all-certs',
  templateUrl: './view-all-certs.component.html',
  styleUrls: ['./view-all-certs.component.css']
})
export class ViewAllCertsComponent implements OnInit {

  constructor(private certService: CertificateService, private sanitizer: DomSanitizer, private dialog: MatDialog) { }
  lista: Array<CertificateDTO> = new Array<CertificateDTO>();
  ngOnInit(): void {
    this.getAll();
  }

  getAll() {
    this.certService.getAllCertificates().subscribe({
      next: result => {
        this.lista = result;
      }
    });
  }

  download(serialNumber: number) {
    this.certService.downloadFile(serialNumber).subscribe({
      next: result => {
        let filename = serialNumber+'.zip'
        let link = document.createElement('a');
        link.href = window.URL.createObjectURL(result);
        link.download = filename;
        link.click();
        link.remove();
      },
      error: e =>
      {console.log(e.message)}
    })
  }

  reason!: string;
  rejection!: Reason;
  async revoke(id: number) {
    try {
      const obj: Reason = {
        reason: ''
      };
  
      const dialogRef = this.dialog.open(InputReasonComponent, {
        data: { reason: this.reason },
        panelClass: 'my-dialog-container-class',
      });
  
      const result = await dialogRef.afterClosed().toPromise();
      if (result?.reason !== undefined) {
        obj.reason = result.reason;
        this.rejection = createReason(result.reason);
  
        await this.certService.revoke(id, this.rejection).toPromise();
        alert("Successfully revoked");
        await this.getAll();
      }
    } catch (error: any) {
      console.log(error);
      const errorMessage = (error?.error?.message) || 'An error occurred.';
      const errorStatus = (error?.error?.status) || '400';
      console.log(errorMessage);
      alert(errorMessage + " " + errorStatus);
    }
  }
  
  
  

}

export interface CertificateDTO {
  certificateType:string,
userId:number,
validFrom: string,
  userEmail: string,
  serialNumber: number,
  validTo: string,
  commonName: string,
  revokationStatus: string,
  isValid: string
}
