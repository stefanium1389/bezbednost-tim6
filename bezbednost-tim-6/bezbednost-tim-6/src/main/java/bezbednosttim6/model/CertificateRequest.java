package bezbednosttim6.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

public class CertificateRequest {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private CertificateType certificateType;

    private Long issuerCertificateId;

    private Long userId;


}
