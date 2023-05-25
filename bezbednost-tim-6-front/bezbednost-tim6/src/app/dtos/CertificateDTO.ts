export interface CertificateDTO {
    certificateType:string,
	userId:number,
	validFrom: string,
    userEmail: string,
    serialNumber: number,
    validTo: string,
    commonName: string
}