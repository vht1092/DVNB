package com.dvnb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import com.dvnb.ldap.authen.CustomActiveDirectoryLdapAuthenticationProvider;
import com.dvnb.services.DvnbUserDetailsContextMapper;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends GlobalMethodSecurityConfiguration {

	@Autowired
	private DvnbUserDetailsContextMapper gstsUserDetailsContextMapper;

	@Value("${ldap.domain}")
	private String sLdapDomain;

	@Value("${ldap.host}")
	private String sLdapHost;

	@Value("${ldap.port}")
	private String sLdapPort;

	@Bean
	public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {

		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(sLdapDomain,	"LDAP://" + sLdapHost + ":" + sLdapPort);
		provider.setConvertSubErrorCodesToExceptions(true);
		//
		provider.setSearchFilter("(&(&(objectClass=user)(objectClass=organizationalPerson)(objectClass=person))(userPrincipalName={0}))");
		provider.setSearchFilter("(&(objectClass=user)(accountExpires=0)(userPrincipalName={0}))");
		provider.setUserDetailsContextMapper(gstsUserDetailsContextMapper);
		return provider;
	}

	@Bean
	public CustomActiveDirectoryLdapAuthenticationProvider customActiveDirectoryLdapAuthenticationProvider() {

		CustomActiveDirectoryLdapAuthenticationProvider provider = new CustomActiveDirectoryLdapAuthenticationProvider(sLdapDomain, "ldap://" + sLdapHost + ":" + sLdapPort, "OU=OU_Synced_Users,DC=scb,DC=local");
		provider.setConvertSubErrorCodesToExceptions(true);
		// username@domain.com {0}; username {1}
		provider.setSearchFilter("(&(CN={1}))");
		provider.setUserDetailsContextMapper(gstsUserDetailsContextMapper);
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// DEVELOP
		// auth.inMemoryAuthentication().withUser("huyennt").password("huyennt").roles("MAN").and().withUser("gsts").password("gsts").roles("OFF");
		// PRODUCTION
		auth.eraseCredentials(true).authenticationProvider(customActiveDirectoryLdapAuthenticationProvider());

	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return authenticationManager();
	}

	static {
		SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
	}

}
