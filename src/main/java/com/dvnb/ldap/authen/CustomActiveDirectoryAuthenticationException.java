package com.dvnb.ldap.authen;
import org.springframework.security.core.AuthenticationException;
@SuppressWarnings("serial")
public final class CustomActiveDirectoryAuthenticationException extends AuthenticationException {
	private final String dataCode;

	CustomActiveDirectoryAuthenticationException(String dataCode, String message, Throwable cause) {
		super(message, cause);
		this.dataCode = dataCode;
	}

	public String getDataCode() {
		return dataCode;
	}
}
