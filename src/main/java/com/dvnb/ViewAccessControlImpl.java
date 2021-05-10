package com.dvnb;

import org.springframework.stereotype.Component;

import com.vaadin.server.Page;
import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

//@Component
public class ViewAccessControlImpl implements ViewAccessControl {
	// DEVELOPMENT
	@Override
	public boolean isAccessGranted(UI ui, String beanName) {
		if (beanName.equals("mainView")) {
			if (!SecurityUtils.isLoggedIn()) {
				return false;
			}
		}
		return true;
	}

	// PRODUCTION
	// @Override
	// public boolean isAccessGranted(UI arg0, String beanName) {
	//
	// switch (beanName) {
	// case "inboxView":
	// if (SercurityUtils.hasRole("role_inbox".toUpperCase())) {
	// return true;
	// }
	// case "caseDistributionView":
	// if (SercurityUtils.hasRole("role_case-distribution".toUpperCase())) {
	// return true;
	// }
	// default:
	// return false;
	// }
	//
	// }

}
