package bezbednosttim6.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class Certificate implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false, unique = true)
	private Long id;
	public String SignatureAlgorithm;
    public String Issuer;
    public LocalDateTime ValidFrom;
    public LocalDateTime ValidTo;
	@Enumerated(EnumType.STRING)
    public CertificateStatus Status;
	@Enumerated(EnumType.STRING)
    public CertificateType CertificateType;
	@OneToOne
    public Long idUser;

}
