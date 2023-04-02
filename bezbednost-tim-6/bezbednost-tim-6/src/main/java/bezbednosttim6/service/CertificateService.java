package bezbednosttim6.service;


import bezbednosttim6.dto.CertificateRequestDTO;
import bezbednosttim6.dto.CertificateRequestResponseDTO;
import bezbednosttim6.dto.LoginResponseDTO;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.exception.TypeNotFoundException;
import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.RequestStatus;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.CertificateRequestRepository;
import bezbednosttim6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class CertificateService {
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private CertificateRequestRepository certificateRequestRepo;
	
	public User findUserById (Long id) 
	{
		return userRepo.findUserById(id).orElseThrow(()-> new ObjectNotFoundException("User not found."));
	}
	
	public User findUserByEmail(String email) {
		return userRepo.findUserByEmail(email);
	}


	public CertificateRequestResponseDTO createRequest(CertificateRequestDTO certificateRequestDTO, String mail) {

		String requestedType = certificateRequestDTO.getCertificateType().toUpperCase().trim();
		CertificateType type = null;
		if (requestedType.equals("ROOT"))
			type = CertificateType.ROOT;
		else if (requestedType.equals("INTERMEDIATE"))
			type = CertificateType.INTERMEDIATE;
		else if (requestedType.equals("END"))
			type = CertificateType.END;
		else throw new TypeNotFoundException("Type of certificate is not valid.");

		LocalDateTime now = LocalDateTime.now();
		//TODO: proveriti da li postoji id issuera
		//TODO: dodati koliko dugo treba da traje novi cert
		//nisam siguran u vezi ovog mail xd
		CertificateRequest newRequest = new CertificateRequest(type, certificateRequestDTO.getIssuerCertificateId(), mail, RequestStatus.CREATED, now);
		certificateRequestRepo.save(newRequest);

		return new CertificateRequestResponseDTO(certificateRequestDTO.getCertificateType(), certificateRequestDTO.getIssuerCertificateId(),mail,now);
	}
}
