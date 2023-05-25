package bezbednosttim6.dto;

import bezbednosttim6.model.Certificate;

public class CertificateDTO {

	public Long serialNumber;
	public String certificateType;
	public Long userId;
	public String userEmail;

	public String validFrom;

	public String validTo;

	public String commonName;
	
	public CertificateDTO(Certificate c) {
		this.certificateType = c.getCertificateType().toString();
		this.userId = c.getUser().getId();
		this.userEmail = c.getUser().getEmail();
		this.serialNumber = c.getSerialNumber();
		this.validFrom = c.getValidFrom().toString();
		this.validTo = c.getValidTo().toString();
		this.commonName = c.getCommonName();
	}
	
}
