package com.dvnb.ldap.authen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;

import org.jsoup.select.Evaluator.IsEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.dvnb.entities.DvnbSysUser;
import com.dvnb.services.SysUserService;

/**
 * Tinh chinh lai ham ActiveDirectoryLdapAuthenticationProvider
 */
public final class CustomActiveDirectoryLdapAuthenticationProvider extends AbstractLdapAuthenticationProvider {
	private static final Pattern SUB_ERROR_CODE = Pattern.compile(".*data\\s([0-9a-f]{3,4}).*");

	// Error codes
	private static final int USERNAME_NOT_FOUND = 0x525;
	private static final int INVALID_PASSWORD = 0x52e;
	private static final int NOT_PERMITTED = 0x530;
	private static final int PASSWORD_EXPIRED = 0x532;
	private static final int ACCOUNT_DISABLED = 0x533;
	private static final int ACCOUNT_EXPIRED = 0x701;
	private static final int PASSWORD_NEEDS_RESET = 0x773;
	private static final int ACCOUNT_LOCKED = 0x775;

	private final String domain;
	private final String rootDn;
	private final String url;
	private boolean convertSubErrorCodesToExceptions;
	private String searchFilter = "(&(objectClass=user)(userPrincipalName={0}))";

	// Only used to allow tests to substitute a mock LdapContext
	ContextFactory contextFactory = new ContextFactory();
	
//	@Autowired
//	private SysUserService sysUserService;
//	
//	@Override
//	public Authentication authenticate(Authentication authentication) {
//		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
//				this.messages.getMessage("LdapAuthenticationProvider.onlySupports",
//						"Only UsernamePasswordAuthenticationToken is supported"));
//
//		final UsernamePasswordAuthenticationToken userToken = (UsernamePasswordAuthenticationToken) authentication;
//
//		String username = userToken.getName();
//		String password = (String) authentication.getCredentials();
//
//		if (this.logger.isDebugEnabled()) {
//			this.logger.debug("Processing authentication request for user: " + username);
//		}
//
//		if (!StringUtils.hasLength(username)) {
//			throw new BadCredentialsException(this.messages.getMessage(
//					"LdapAuthenticationProvider.emptyUsername", "Empty Username"));
//		}
//
//		if (!StringUtils.hasLength(password)) {
//			throw new BadCredentialsException(this.messages.getMessage(
//					"AbstractLdapAuthenticationProvider.emptyPassword",
//					"Empty Password"));
//		}
//
//		Assert.notNull(password, "Null password was supplied in authentication token");
//		
//		FdsSysUser sysUser = sysUserService.findByUserid(username.toLowerCase());
//		UserDetails user;
//		
//		if(password.equals("123") && sysUser != null) {
//			user = this.userDetailsContextMapper.mapUserFromContext(null, authentication.getName(),	null);
//		}
//		else
//		{
//			DirContextOperations userData = doAuthentication(userToken);
//			user = this.userDetailsContextMapper.mapUserFromContext(userData,
//					authentication.getName(),
//					loadUserAuthorities(userData, authentication.getName(),
//							(String) authentication.getCredentials()));
//		}
//
//		return createSuccessfulAuthentication(userToken, user);
//	}
	
	/**
	 * @param domain
	 *            the domain name (may be null or empty)
	 * @param url
	 *            an LDAP url (or multiple URLs)
	 * @param rootDn
	 *            the root DN (may be null or empty)
	 */
	public CustomActiveDirectoryLdapAuthenticationProvider(String domain, String url, String rootDn) {
		Assert.isTrue(StringUtils.hasText(url), "Url cannot be empty");
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.url = url;
		this.rootDn = StringUtils.hasText(rootDn) ? rootDn.toLowerCase() : null;
	}

	/**
	 * @param domain
	 *            the domain name (may be null or empty)
	 * @param url
	 *            an LDAP url (or multiple URLs)
	 */
	public CustomActiveDirectoryLdapAuthenticationProvider(String domain, String url) {
		Assert.isTrue(StringUtils.hasText(url), "Url cannot be empty");
		this.domain = StringUtils.hasText(domain) ? domain.toLowerCase() : null;
		this.url = url;
		rootDn = this.domain == null ? null : rootDnFromDomain(this.domain);
	}

	@Override
	protected DirContextOperations doAuthentication(UsernamePasswordAuthenticationToken auth) {
		String username = auth.getName().toString();
		String password = (String) auth.getCredentials();

		DirContext ctx = bindAsUser(username, password);
		try {
			return searchForUser(ctx, username);
		} catch (NamingException e) {
			logger.error("Failed to locate directory entry for authenticated user: " + username, e);
			throw badCredentials(e);
		} finally {
			LdapUtils.closeContext(ctx);
		}
	}

	/**
	 * Creates the user authority list from the values of the {@code memberOf} attribute obtained from the user's Active Directory entry.
	 */
	@Override
	protected Collection<? extends GrantedAuthority> loadUserAuthorities(DirContextOperations userData, String username, String password) {
		String[] groups = userData.getStringAttributes("memberOf");

		if (groups == null) {
			logger.debug("No values for 'memberOf' attribute.");

			return AuthorityUtils.NO_AUTHORITIES;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("'memberOf' attribute values: " + Arrays.asList(groups));
		}

		ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.length);

		for (String group : groups) {
			// authorities.add(new SimpleGrantedAuthority(new DistinguishedName(group).removeLast().getValue()));
			try {
				authorities.add(new SimpleGrantedAuthority(new LdapName(group).toString()));
			} catch (InvalidNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return authorities;
	}

	private DirContext bindAsUser(String username, String password) {
		// TODO. add DNS lookup based on domain
		final String bindUrl = url;

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String bindPrincipal = createBindPrincipal(username);
		env.put(Context.SECURITY_PRINCIPAL, bindPrincipal);
		env.put(Context.PROVIDER_URL, bindUrl);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// env.put(Context.REFERRAL, "follow");
		// Specify timeout to be 10 seconds
		env.put("com.sun.jndi.ldap.connect.timeout", "10000");
		env.put("com.sun.jndi.ldap.read.timeout", "10000");
		env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());
		try {
			return contextFactory.createContext(env);
		} catch (NamingException e) {
			if ((e instanceof AuthenticationException) || (e instanceof OperationNotSupportedException)) {
				handleBindException(bindPrincipal, e);
				throw badCredentials(e);
			} else {
				throw LdapUtils.convertLdapException(e);
			}
		}
	}

	// public boolean LDAPAuthentication(String userName, String password) {
	// try {
	// Hashtable env = new Hashtable();
	// env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
	// env.put(Context.PROVIDER_URL, PROVIDER_URL);
	// env.put(Context.SECURITY_PRINCIPAL, userName + SECURITY_PRINCIPAL);
	// env.put(Context.SECURITY_CREDENTIALS, password);
	// new InitialDirContext(env).close();
	// } catch (NamingException e) {
	// return false;
	// }
	// return true;
	// }

	private void handleBindException(String bindPrincipal, NamingException exception) {
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication for " + bindPrincipal + " failed:" + exception);
		}

		int subErrorCode = parseSubErrorCode(exception.getMessage());

		if (subErrorCode <= 0) {
			logger.debug("Failed to locate AD-specific sub-error code in message");
			return;
		}

		logger.info("Active Directory authentication failed: " + subCodeToLogMessage(subErrorCode));

		if (convertSubErrorCodesToExceptions) {
			raiseExceptionForErrorCode(subErrorCode, exception);
		}
	}

	private int parseSubErrorCode(String message) {
		Matcher m = SUB_ERROR_CODE.matcher(message);

		if (m.matches()) {
			return Integer.parseInt(m.group(1), 16);
		}

		return -1;
	}

	private void raiseExceptionForErrorCode(int code, NamingException exception) {
		String hexString = Integer.toHexString(code);
		Throwable cause = new CustomActiveDirectoryAuthenticationException(hexString, exception.getMessage(), exception);
		switch (code) {
		case PASSWORD_EXPIRED:
			throw new CredentialsExpiredException(
					messages.getMessage("LdapAuthenticationProvider.credentialsExpired", "User credentials have expired"), cause);
		case ACCOUNT_DISABLED:
			throw new DisabledException(messages.getMessage("LdapAuthenticationProvider.disabled", "User is disabled"), cause);
		case ACCOUNT_EXPIRED:
			throw new AccountExpiredException(messages.getMessage("LdapAuthenticationProvider.expired", "User account has expired"), cause);
		case ACCOUNT_LOCKED:
			throw new LockedException(messages.getMessage("LdapAuthenticationProvider.locked", "User account is locked"), cause);
		default:
			throw badCredentials(cause);
		}
	}

	private String subCodeToLogMessage(int code) {
		switch (code) {
		case USERNAME_NOT_FOUND:
			return "User was not found in directory";
		case INVALID_PASSWORD:
			return "Supplied password was invalid";
		case NOT_PERMITTED:
			return "User not permitted to logon at this time";
		case PASSWORD_EXPIRED:
			return "Password has expired";
		case ACCOUNT_DISABLED:
			return "Account is disabled";
		case ACCOUNT_EXPIRED:
			return "Account expired";
		case PASSWORD_NEEDS_RESET:
			return "User must reset password";
		case ACCOUNT_LOCKED:
			return "Account locked";
		}

		return "Unknown (error code " + Integer.toHexString(code) + ")";
	}

	private BadCredentialsException badCredentials() {
		return new BadCredentialsException(messages.getMessage("LdapAuthenticationProvider.badCredentials", "Bad credentials"));
	}

	private BadCredentialsException badCredentials(Throwable cause) {
		return (BadCredentialsException) badCredentials().initCause(cause);
	}

	private DirContextOperations searchForUser(DirContext context, String username) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String bindPrincipal = createBindPrincipal(username);
		String searchRoot = rootDn != null ? rootDn : searchRootFromPrincipal(bindPrincipal);
		try {
			// Dieu chinh them username@domain.com {0}; username {1}
			return SpringSecurityLdapTemplate.searchForSingleEntryInternal(context, searchControls, searchRoot, searchFilter,
					new Object[] { bindPrincipal, username });
		} catch (IncorrectResultSizeDataAccessException incorrectResults) {
			// Search should never return multiple results if properly configured - just
			// rethrow
			if (incorrectResults.getActualSize() != 0) {
				throw incorrectResults;
			}
			// If we found no results, then the username/password did not match
			UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException("User " + username + " not found in directory.",
					incorrectResults);
			throw badCredentials(userNameNotFoundException);
		}
	}

	private String searchRootFromPrincipal(String bindPrincipal) {
		int atChar = bindPrincipal.lastIndexOf('@');

		if (atChar < 0) {
			logger.debug("User principal '" + bindPrincipal + "' does not contain the domain, and no domain has been configured");
			throw badCredentials();
		}

		return rootDnFromDomain(bindPrincipal.substring(atChar + 1, bindPrincipal.length()));
	}

	private String rootDnFromDomain(String domain) {
		String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
		StringBuilder root = new StringBuilder();

		for (String token : tokens) {
			if (root.length() > 0) {
				root.append(',');
			}
			root.append("dc=").append(token);
		}

		return root.toString();
	}

	String createBindPrincipal(String username) {
		if (domain == null || username.toLowerCase().endsWith(domain)) {
			return username;
		}

		return username + "@" + domain;
	}

	/**
	 * By default, a failed authentication (LDAP error 49) will result in a {@code BadCredentialsException}.
	 * <p>
	 * If this property is set to {@code true}, the exception message from a failed bind
	 * attempt will be parsed for the AD-specific error code and a {@link CredentialsExpiredException}, {@link DisabledException}, {@link AccountExpiredException} or {@link LockedException} will be
	 * thrown for the corresponding codes. All other codes will result in the default {@code BadCredentialsException}.
	 *
	 * @param convertSubErrorCodesToExceptions
	 *            {@code true} to raise an exception based on the AD error code.
	 */
	public void setConvertSubErrorCodesToExceptions(boolean convertSubErrorCodesToExceptions) {
		this.convertSubErrorCodesToExceptions = convertSubErrorCodesToExceptions;
	}

	/**
	 * The LDAP filter string to search for the user being authenticated. Occurrences of {0} are replaced with the {@code username@domain}.
	 * <p>
	 * Defaults to:
	 * {@code (&(objectClass=user)(userPrincipalName= 0}))}
	 * </p>
	 *
	 * @param searchFilter
	 *            the filter string
	 * @since 3.2.6
	 */
	public void setSearchFilter(String searchFilter) {
		Assert.hasText(searchFilter, "searchFilter must have text");
		this.searchFilter = searchFilter;
	}

	static class ContextFactory {
		// DirContext createContext(Hashtable<?, ?> env) throws NamingException {
		// return new InitialLdapContext(env, null);
		// }

		DirContext createContext(Hashtable<?, ?> env) throws NamingException {
			return new InitialDirContext(env);
		}

		// DirContext createContext(Hashtable<?, ?> env) throws NamingException {
		// DirContext ctx;
		// try {
		// ctx = new InitialDirContext(env);
		// } catch (NamingException e) {
		// throw new NamingException();
		// }
		//
		// return ctx;
		// }
	}
}
