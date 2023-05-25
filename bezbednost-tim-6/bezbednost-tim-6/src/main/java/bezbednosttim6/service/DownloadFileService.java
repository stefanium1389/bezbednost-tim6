package bezbednosttim6.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bezbednosttim6.dto.SuccessDTO;
import bezbednosttim6.exception.ActionExpiredException;
import bezbednosttim6.exception.ObjectNotFoundException;
import bezbednosttim6.model.Activation;
import bezbednosttim6.model.Certificate;
import bezbednosttim6.model.User;
import bezbednosttim6.repository.ActivationRepository;
import bezbednosttim6.repository.CertificateRepository;
import bezbednosttim6.repository.UserRepository;

@Service
public class DownloadFileService {
	
	@Autowired
	CertificateRepository certRepo;
	
	@Autowired
	UserRepository userRepo;
	
	static private String FILE_PATH = "src/main/resources/certificates" ;

	public byte[] getZipBytes(Long serialNumber, Principal principal) throws IOException {
		Optional<Certificate> db_certificate = certRepo.findBySerialNumber(serialNumber);
		if(db_certificate.isEmpty()) {
			throw new ObjectNotFoundException("Invalid serial number!");
		}
		String username = principal.getName();
		boolean isAdmin = false;
		if(userRepo.findUserByEmail(username).getRole().getName().equals("ROLE_ADMIN")) {
			isAdmin = true;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos); 						//inicajlizuj zip
        
		if(db_certificate.get().getUser().getEmail().equals(username) || isAdmin) { //ako trazim svoj ili ako sam admin
			
			String public_fileName = serialNumber+".cer";
			String public_path = FILE_PATH+"/public/"+public_fileName;
	        addToZip(zipOut, public_path, public_fileName);
	        String private_fileName = serialNumber+".key";
			String private_path = FILE_PATH+"/private/"+private_fileName;
	        addToZip(zipOut, private_path, private_fileName);
		}
		else { 																		//ako trazim tudji sertifikat
			String public_fileName = serialNumber+".cer";
			String public_path = FILE_PATH+"/public/"+public_fileName;
	        addToZip(zipOut, public_path, public_fileName);
		}
		zipOut.close();
        baos.close();
		return baos.toByteArray();
	} 
	
	private void addToZip(ZipOutputStream zipOut, String filePath, String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }

        fis.close();
        zipOut.closeEntry();
    }
	
}
