package bezbednosttim6.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Table(name="certificates")
@Entity
public class Certificate implements Serializable {
	
	@Id
	@Column(nullable = false, updatable = false, unique = true)
	public Long serialNumber;
	public String SignatureAlgorithm;
    public Long Issuer;
    public Date ValidFrom;
    public Date ValidTo;
	@Enumerated(EnumType.STRING)
    public CertificateStatus Status;
	@Enumerated(EnumType.STRING)
    public CertificateType CertificateType;
	@ManyToOne
    public User user;
	
	public Certificate(Long serialNumber, String signatureAlgorithm, Long issuer, LocalDateTime validFrom,
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
	public Long getIssuer() {
		return Issuer;
	}
	public void setIssuer(Long issuer) {
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
	
	

	public Certificate(Long serialNumber, String signatureAlgorithm, String issuer, Date validFrom,
					   Date validTo, CertificateStatus status, bezbednosttim6.model.CertificateType certificateType,
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
	public Long getIssuer() {
		return Issuer;
	}
	public void setIssuer(Long issuer) {
		Issuer = issuer;
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

	public Date getValidFrom() {
		return ValidFrom;
	}

	public void setValidFrom(Date validFrom) {
		ValidFrom = validFrom;
	}

	public Date getValidTo() {
		return ValidTo;
	}

	public void setValidTo(Date validTo) {
		ValidTo = validTo;
	}
}
