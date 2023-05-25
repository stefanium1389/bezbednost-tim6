import { Component, OnInit } from '@angular/core';
import { CertificateDTO } from '../dtos/CertificateDTO';
import { CertificateService } from '../backend-services/certificate.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-view-all-certs',
  templateUrl: './view-all-certs.component.html',
  styleUrls: ['./view-all-certs.component.css']
})
export class ViewAllCertsComponent implements OnInit {

  constructor(private certService: CertificateService, private sanitizer: DomSanitizer) { }
  lista: Array<CertificateDTO> = new Array<CertificateDTO>();
  ngOnInit(): void {
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

}
