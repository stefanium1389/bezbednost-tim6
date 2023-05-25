package bezbednosttim6.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(name="certificate_requests")
@Entity
public class CertificateRequest {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    private CertificateType certificateType;

    private Long issuerCertificateId;

    private Long issuerId;

    private Long userId;

    private String userEmail;

    private Duration duration;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime timeOfRequest;

    private String commonName;

    private String rejection;


    public CertificateRequest(CertificateType certificateType, Long issuerCertificateId, Long issuerId, Long userId, String userEmail, RequestStatus status, LocalDateTime timeOfRequest, Duration duration, String commonName, String rejection) {
        this.certificateType = certificateType;
        this.issuerCertificateId = issuerCertificateId;
        this.issuerId = issuerId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.status = status;
        this.timeOfRequest = timeOfRequest;
        this.duration = duration;
        this.commonName = commonName;
        this.rejection = rejection;
    }

    public CertificateRequest() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public Long getIssuerCertificateId() {
        return issuerCertificateId;
    }

    public void setIssuerCertificateId(Long issuerCertificateId) {
        this.issuerCertificateId = issuerCertificateId;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimeOfRequest() {
        return timeOfRequest;
    }

    public void setTimeOfRequest(LocalDateTime timeOfRequest) {
        this.timeOfRequest = timeOfRequest;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getRejection() {
        return rejection;
    }

    public void setRejection(String rejection) {
        this.rejection = rejection;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
