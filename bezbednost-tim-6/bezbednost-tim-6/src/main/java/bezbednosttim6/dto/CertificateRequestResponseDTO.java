package bezbednosttim6.dto;

import bezbednosttim6.model.CertificateRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CertificateRequestResponseDTO {
	private String certificateType;
	private Long issuerCertificateId;
	private Long userId;
	private String timeOfRequest;

	private String status;

	public CertificateRequestResponseDTO() {
		super();
	}

	public CertificateRequestResponseDTO(String certificateType, Long issuerCertificateId, Long userId, LocalDateTime timeOfRequest, String status) {
		super();
		this.certificateType = certificateType;
		this.issuerCertificateId = issuerCertificateId;
		this.userId = userId;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timeOfRequest = timeOfRequest.format(formatter);
		this.status = status;

	}

	public CertificateRequestResponseDTO(CertificateRequest cr) {
		super();
		this.certificateType = cr.getCertificateType().toString();
		this.issuerCertificateId = cr.getIssuerCertificateId();
		this.userId = cr.getUserId();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		this.timeOfRequest = formatter.format(cr.getTimeOfRequest());
		this.status = cr.getStatus().toString();

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
