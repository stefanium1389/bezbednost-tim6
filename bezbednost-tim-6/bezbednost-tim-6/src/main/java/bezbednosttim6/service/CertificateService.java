package bezbednosttim6.service;


import bezbednosttim6.dto.CertificateRequestDTO;
import bezbednosttim6.dto.CertificateRequestResponseDTO;
import bezbednosttim6.dto.LoginResponseDTO;
import bezbednosttim6.exception.*;
import bezbednosttim6.model.CertificateRequest;
import bezbednosttim6.model.CertificateType;
import bezbednosttim6.model.RequestStatus;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.CertificateRequestRepository;
import bezbednosttim6.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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

		User user = userRepo.findUserByEmail(mail);
		if (user == null)
		{
			throw new UserNotFoundException("User not found.");
		}

		String requestedType = certificateRequestDTO.getCertificateType().toUpperCase().trim();
		CertificateType type = findCertificateType(requestedType);

		if (type == CertificateType.ROOT && user.getRole().getName().equals("user"))
			throw new TypePermissionException("You don't have the permission to create root certificates");

		LocalDateTime now = LocalDateTime.now();
		Duration duration;
		try {
			// pogledajte (hoverujte) .parse metodu za primere
			 duration = Duration.parse(certificateRequestDTO.getDuration());
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid duration provided");
		}

		if (!checkIfCertificateExist(certificateRequestDTO.getIssuerCertificateId()))
		{
			throw new CertificateNotFoundException("Issuer certificate not found");
		}

		if (!checkIfValidDuration(certificateRequestDTO.getIssuerCertificateId(),duration))
		{
			throw new InvalidArgumentException("Requested duration is longer than possible");
		}

		CertificateRequest newRequest = new CertificateRequest(type, certificateRequestDTO.getIssuerCertificateId(), user.getId(), RequestStatus.CREATED, now, duration);
		certificateRequestRepo.save(newRequest);

		return new CertificateRequestResponseDTO(certificateRequestDTO.getCertificateType(), certificateRequestDTO.getIssuerCertificateId(),user.getId(),now);
	}

	private boolean checkIfValidDuration(Long issuerCertificateId, Duration requestedDuration) {
		//TODO: zapravo dobaviti certifikat, napraviti duration od now i enddate
		//"something must be done, 500 years" xdd
		Duration dummyDuration = Duration.of(500L * 365, ChronoUnit.DAYS);


		if (requestedDuration.compareTo(dummyDuration) > 0) {
			return false;
		}
		return true;
	}


	private boolean checkIfCertificateExist(Long issuerCertificateId) {
		//TODO: implementirati
		return true;
	}

	private CertificateType findCertificateType(String type)
	{
		if (type.equals("ROOT"))
			return CertificateType.ROOT;
		else if (type.equals("INTERMEDIATE"))
			return CertificateType.INTERMEDIATE;
		else if (type.equals("END"))
			return CertificateType.END;
		else throw new TypeNotFoundException("Type of certificate is not valid.");
	}
}
