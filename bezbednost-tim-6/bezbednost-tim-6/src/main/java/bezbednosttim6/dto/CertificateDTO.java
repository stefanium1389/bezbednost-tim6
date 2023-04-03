package bezbednosttim6.dto;

import bezbednosttim6.model.Certificate;

public class CertificateDTO {
	
	public String certificateType;
	public Long userId;
	public String issDate;
	
	public CertificateDTO(Certificate c) {
		this.certificateType = c.getCertificateType().toString();
		this.userId = c.getUser().getId();
		this.issDate = c.getValidFrom().toString();
	}
	
}
