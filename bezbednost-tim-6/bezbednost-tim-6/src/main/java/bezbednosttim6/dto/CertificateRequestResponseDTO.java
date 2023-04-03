package bezbednosttim6.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CertificateRequestResponseDTO {
	private String certificateType;
	private Long issuerCertificateId;
	private Long userId;
	private String timeOfRequest;

	public CertificateRequestResponseDTO() {
		super();
	}

	public CertificateRequestResponseDTO(String certificateType, Long issuerCertificateId, Long userId, LocalDateTime timeOfRequest) {
		super();
		this.certificateType = certificateType;
		this.issuerCertificateId = issuerCertificateId;
		this.userId = userId;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timeOfRequest = timeOfRequest.format(formatter);

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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTimeOfRequest() {
		return timeOfRequest;
	}

	public void setTimeOfRequest(String timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}
}
