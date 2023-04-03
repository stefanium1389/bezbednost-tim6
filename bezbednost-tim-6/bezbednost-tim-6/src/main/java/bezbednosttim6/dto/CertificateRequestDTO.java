package bezbednosttim6.dto;

public class CertificateRequestDTO {
	private String certificateType;
	private Long issuerCertificateId;
	private String duration;
	private String commonName;

	public CertificateRequestDTO() {
		super();
	}

	public CertificateRequestDTO(String certificateType, Long issuerCertificateId, String duration, String commonName) {
		super();
		this.certificateType = certificateType;
		this.issuerCertificateId = issuerCertificateId;
		this.duration = duration;
		this.commonName = commonName;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
}
