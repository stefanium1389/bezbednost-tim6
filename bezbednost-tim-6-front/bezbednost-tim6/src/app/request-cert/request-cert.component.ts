import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistrationDTO } from '../dtos/RegistrationDtos';
import { UserdataService } from '../backend-services/userdata.service'
import { CertificateService } from '../backend-services/certificate.service';
import { CertificateDTO } from '../dtos/CertificateDTO';
import { RequestCertificateDTO } from '../dtos/RequestCertificateDTO';

@Component({
  selector: 'app-request-cert',
  templateUrl: './request-cert.component.html',
  styleUrls: ['./request-cert.component.css']
})
export class RequestCertComponent implements OnInit {
  selectedOption!: string;
  typeOptions = ['ROOT','INTERMEDIATE', 'END'];
  issuers: any[] = ['None'];
  lista: Array<CertificateDTO> = new Array<CertificateDTO>();

  constructor(private userData: UserdataService,private router: Router, private certService: CertificateService) { }

  requestForm!: FormGroup;

  ngOnInit(): void {
    this.getAll();
    this.requestForm = new FormGroup({
    typeOption: new FormControl('', Validators.required),
    issuer: new FormControl('', Validators.required),
    duration: new FormControl('', Validators.required),
    commonName: new FormControl('', Validators.required)
    });
  }

  request(){
    console.log(this.requestForm.get('typeOption')?.value);
    console.log(this.requestForm.get('issuer')?.value);
    console.log(this.requestForm.get('duration')?.value);
    console.log(this.requestForm.get('commonName')?.value);
    let issuerId: any = null;
    if (this.requestForm.get('issuer')?.value != 'None') {
      issuerId = this.requestForm.get('issuer')?.value;
    }
    const dto: RequestCertificateDTO = {
      certificateType: this.requestForm.get('typeOption')?.value ,
      issuerCertificateId: issuerId,
      duration: 'P' +  this.requestForm.get('duration')?.value + 'D',
      commonName: this.requestForm.get('commonName')?.value
    }
    this.certService.request(dto).subscribe({
      next: result => {
        alert('Request sent, view it in "View Sent Requests')
      },
      error: error => {
        if (error?.error?.message != undefined) {
          alert(error?.error?.message);
        }
    }})
  }

  getAll() {
    this.certService.getAllCertificates().subscribe({
      next: result => {
        this.lista = result;
        for (let x of this.lista) {
          this.issuers.push(x.serialNumber);
        }
      }
    });
  }
}
