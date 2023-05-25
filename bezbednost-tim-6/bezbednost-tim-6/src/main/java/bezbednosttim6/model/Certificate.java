package bezbednosttim6.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Table(name="certificates")
@Entity
public class Certificate implements Serializable {
	
	@Id
	@Column(nullable = false, updatable = false, unique = true)
	public Long serialNumber;
	public String signatureAlgorithm;
    public Long issuer;
    public Date validFrom;
    public Date validTo;
	@Enumerated(EnumType.STRING)
    public CertificateStatus status;
	@Enumerated(EnumType.STRING)
    public CertificateType certificateType;

	@Enumerated(EnumType.STRING)
	public CertificateRevocationStatus certificateRevocationStatus;
	@ManyToOne
    public User user;
	
	public String commonName;

	public String revocationReason;

	public Certificate(Long serialNumber, String signatureAlgorithm, Long issuer, Date validFrom,
					   Date validTo, CertificateStatus status, bezbednosttim6.model.CertificateType certificateType, CertificateRevocationStatus certificateRevocationStatus,
					   User user, String commonName, String revocationReason) {
		super();
		this.serialNumber = serialNumber;
		this.signatureAlgorithm = signatureAlgorithm;
		this.issuer = issuer;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.status = status;
		this.certificateType = certificateType;
		this.certificateRevocationStatus = certificateRevocationStatus;
		this.user = user;
		this.commonName = commonName;
		this.revocationReason = revocationReason;
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
		return signatureAlgorithm;
	}
	public void setSignatureAlgorithm(String signatureAlgorithm) {
		this.signatureAlgorithm = signatureAlgorithm;
	}
	public Long getIssuer() {
		return issuer;
	}
	public void setIssuer(Long issuer) {
		this.issuer = issuer;
	}
	public CertificateStatus getStatus() {
		return status;
	}
	public void setStatus(CertificateStatus status) {
		this.status = status;
	}
	public CertificateType getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(CertificateType certificateType) {
		this.certificateType = certificateType;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public CertificateRevocationStatus getCertificateRevocationStatus() {
		return certificateRevocationStatus;
	}

	public void setCertificateRevocationStatus(CertificateRevocationStatus certificateRevocationStatus) {
		this.certificateRevocationStatus = certificateRevocationStatus;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getRevocationReason() {
		return revocationReason;
	}

	public void setRevocationReason(String revocationReason) {
		this.revocationReason = revocationReason;
	}
}
