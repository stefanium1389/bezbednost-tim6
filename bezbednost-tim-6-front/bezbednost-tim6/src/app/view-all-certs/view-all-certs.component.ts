import { Component, OnInit } from '@angular/core';
import { CertificateDTO } from '../dtos/CertificateDTO';
import { CertificateService } from '../backend-services/certificate.service';

@Component({
  selector: 'app-view-all-certs',
  templateUrl: './view-all-certs.component.html',
  styleUrls: ['./view-all-certs.component.scss']
})
export class ViewAllCertsComponent implements OnInit {

  constructor(private certService: CertificateService) { }
  lista: Array<CertificateDTO> = new Array<CertificateDTO>();
  ngOnInit(): void {
    this.certService.getAllCertificates().subscribe({
      next: result => {
        this.lista = result;
      }
    });
  }

}
