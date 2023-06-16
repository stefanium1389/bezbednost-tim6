import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, NgModel, Validators } from '@angular/forms';
import { CertificateService } from '../backend-services/certificate.service';

@Component({
  selector: 'app-check-cert-validity',
  templateUrl: './check-cert-validity.component.html',
  styleUrls: ['./check-cert-validity.component.css']
})
export class CheckCertValidityComponent implements OnInit {

  selectedFile: File | undefined;
  selectModeForm: FormControl;
  serialNumber: FormControl;
  responded: boolean = false;
  certificateStatus: string = ""

  constructor(private certService:CertificateService) {
    this.selectModeForm = new FormControl();
    this.serialNumber = new FormControl();
   }
  
  ngOnInit(): void {
    this.selectModeForm = new FormControl('serial');
  }

  checkSerial(): void {
    const sn = this.serialNumber.value;
    this.certService.checkIsValid(sn).subscribe({
      next: result => {
        this.responded = true;
        this.certificateStatus = 'Valid'
      },
      error: error => {
        if(error.status == 404){
          this.responded = true;
          this.certificateStatus = 'Not Existing'
        }
        if(error.status == 400){
          this.responded = true;
          this.certificateStatus = 'Invalid'
        }
      }
    })
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  checkFile(): void {
    if(this.selectedFile){
      const formData = new FormData();
      formData.append("file",this.selectedFile);  
      if(this.selectedFile.size > 1024){
        alert('file too big');
        return;
      }
      if(!(this.selectedFile.name.endsWith('.cer') || this.selectedFile.name.endsWith(".crt"))){
        alert('file is not certificate');
        return
      }
      
      this.certService.checkIsValidFIle(formData).subscribe({
        next: result => {
          this.responded = true;
          this.certificateStatus = 'Valid'
        },
        error: error => {
          if(error.status == 404){
            this.responded = true;
            this.certificateStatus = 'Not Existing'
          }
          if(error.status == 400){
            this.responded = true;
            this.certificateStatus = 'Invalid'
          }
        }
      }
      )
    }
  }

}
