package bezbednosttim6.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class Certificate implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false, unique = true)
	public Long serialNumber;
	public String SignatureAlgorithm;
    public String Issuer;
    public LocalDateTime ValidFrom;
    public LocalDateTime ValidTo;
	@Enumerated(EnumType.STRING)
    public CertificateStatus Status;
	@Enumerated(EnumType.STRING)
    public CertificateType CertificateType;
	@ManyToOne
    public User user;
	
	public Certificate(Long serialNumber, String signatureAlgorithm, String issuer, LocalDateTime validFrom,
			LocalDateTime validTo, CertificateStatus status, bezbednosttim6.model.CertificateType certificateType,
			User user) {
		super();
		this.serialNumber = serialNumber;
		SignatureAlgorithm = signatureAlgorithm;
		Issuer = issuer;
		ValidFrom = validFrom;
		ValidTo = validTo;
		Status = status;
		CertificateType = certificateType;
		this.user = user;
	}
	
	public Certificate() {
		super();
	}

	public Long getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(Long serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSignatureAlgorithm() {
		return SignatureAlgorithm;
	}
	public void setSignatureAlgorithm(String signatureAlgorithm) {
		SignatureAlgorithm = signatureAlgorithm;
	}
	public String getIssuer() {
		return Issuer;
	}
	public void setIssuer(String issuer) {
		Issuer = issuer;
	}
	public LocalDateTime getValidFrom() {
		return ValidFrom;
	}
	public void setValidFrom(LocalDateTime validFrom) {
		ValidFrom = validFrom;
	}
	public LocalDateTime getValidTo() {
		return ValidTo;
	}
	public void setValidTo(LocalDateTime validTo) {
		ValidTo = validTo;
	}
	public CertificateStatus getStatus() {
		return Status;
	}
	public void setStatus(CertificateStatus status) {
		Status = status;
	}
	public CertificateType getCertificateType() {
		return CertificateType;
	}
	public void setCertificateType(CertificateType certificateType) {
		CertificateType = certificateType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	

}
