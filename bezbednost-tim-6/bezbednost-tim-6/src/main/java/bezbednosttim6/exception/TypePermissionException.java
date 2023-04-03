package bezbednosttim6.exception;

import org.springframework.security.core.AuthenticationException;

public class TypePermissionException extends AuthenticationException {

	public TypePermissionException(String msg) {
		super(msg);
	}

}
