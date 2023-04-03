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

    private Long userId;

    private Duration duration;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime timeOfRequest;


    public CertificateRequest(CertificateType certificateType, Long issuerCertificateId, Long userId, RequestStatus status, LocalDateTime timeOfRequest, Duration duration) {
        this.certificateType = certificateType;
        this.issuerCertificateId = issuerCertificateId;
        this.userId = userId;
        this.status = status;
        this.timeOfRequest = timeOfRequest;
        this.duration = duration;
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
}
