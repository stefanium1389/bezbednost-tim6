package bezbednosttim6.exception;

import org.springframework.security.core.AuthenticationException;

public class CertificateNotFoundException extends AuthenticationException {

	public CertificateNotFoundException(String msg) {
		super(msg);
	}

}
