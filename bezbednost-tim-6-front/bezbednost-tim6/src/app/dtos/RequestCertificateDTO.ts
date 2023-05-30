export interface RequestCertificateDTO {
    certificateType: string,
	issuerCertificateId: number;
	duration: string,
	commonName: string
}