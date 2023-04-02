package bezbednosttim6.dto;

public class CertificateRequestDTO {
	private String certificateType;
	private Long issuerCertificateId;

	public CertificateRequestDTO() {
		super();
	}

	public CertificateRequestDTO(String certificateType, Long issuerCertificateId) {
		super();
		this.certificateType = certificateType;
		this.issuerCertificateId = issuerCertificateId;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public Long getIssuerCertificateId() {
		return issuerCertificateId;
	}

	public void setIssuerCertificateId(Long issuerCertificateId) {
		this.issuerCertificateId = issuerCertificateId;
	}

}
