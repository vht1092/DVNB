package com.dvnb;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.dvnb.entities.DvnbLdapUserDetails;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	// DEVELOPMENT
	// ===============================================================
	// public static boolean isLoggedIn() {
	// Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// return authentication != null && authentication.isAuthenticated();
	// // return true;
	// }
	//
	// public static boolean hasRole(String role) {
	// Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
	// }
	//
	// public static String getFullname() {
	// return "User test";
	// }
	//
	// public static String getLastLogin() {
	// TimeConverter timeConverter = new TimeConverter();
	// return timeConverter.convertStrToDateTime("20161004092231025");
	// }
	//
	// public static String getUserId() {
	// Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	// return authentication.getName();
	// // return "chautk";
	// }
	//
	// public static void makeLogout() {
	// SecurityContextHolder.clearContext();
	// }
	// ===============================================================

	// ============================= PRODUCTION =============================

	public static boolean isLoggedIn() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated();
	}

	public static void makeLogout() {
		SecurityContextHolder.clearContext();
	}

	public static boolean hasRole(String role) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
	}

	public static String getFullname() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			final DvnbLdapUserDetails myUserDetails = (DvnbLdapUserDetails) authentication.getPrincipal();
			return String.valueOf(myUserDetails.getFullname());
		}
		return "";
	}

	public static String getLastLogin() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			final TimeConverter timeConverter = new TimeConverter();
			final DvnbLdapUserDetails myUserDetails = (DvnbLdapUserDetails) authentication.getPrincipal();
			return timeConverter.convertStrToDateTime(myUserDetails.getLastlogin());
		}
		return "";
	}

	public static String getUserId() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			final DvnbLdapUserDetails myUserDetails = (DvnbLdapUserDetails) authentication.getPrincipal();
			return myUserDetails.getId();
		}
		return "";
	}

	// ===============================================================

}