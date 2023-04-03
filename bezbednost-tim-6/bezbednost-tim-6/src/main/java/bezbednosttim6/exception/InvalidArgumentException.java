package bezbednosttim6.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidArgumentException extends AuthenticationException {

	public InvalidArgumentException(String msg) {
		super(msg);
	}

}
