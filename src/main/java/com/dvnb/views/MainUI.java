package com.dvnb.views;


import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dvnb.SecurityUtils;
import com.dvnb.components.LoginForm;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

@SpringUI
@Theme("mytheme")
public class MainUI extends UI {

	//private static final Logger LOGGER = LoggerFactory.getLogger(MainUI.class);
	private static final Logger LOGGER = LogManager.getLogger(MainUI.class);

	private static final long serialVersionUID = 1383791387363344726L;
	@Autowired
	private ErrorView errorView;
	@Autowired
	public SpringViewProvider viewProvider;
	@Value("${time.refresh.content}")
	private int sTimeRefreshContent;
	@Autowired
	private AuthenticationManager authentionManager;

	@Override
	protected void init(VaadinRequest request) {
//		VaadinSession.getCurrent().getSession().setMaxInactiveInterval( ( int ) TimeUnit.MINUTES.toSeconds( 1 ) );  // Setting timeout of 1000.000 minutes = ( 1.000.000 * 60 ) seconds.

		if (!SecurityUtils.isLoggedIn()) {
			showLogin();
		} else {
			showMainForm();
		}

//		addDetachListener(new DetachListener() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void detach(DetachEvent event) {
//				// Xoa tat ca cac case dang xu da duoc dang ky
//				getSession().close();
//			}
//		});
	}

	private void showLogin() {
		setContent(new LoginForm(this::login));
	}

	private void showMainForm() {
		Navigator navigator = new Navigator(this, this);
		navigator.setErrorView(errorView);
		navigator.addProvider(viewProvider);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		getUI().getNavigator().navigateTo("");
	}

	private boolean login(final String username, final String password) {
		WebBrowser webBrowser = UI.getCurrent().getSession().getBrowser();
		try {
			Authentication token = authentionManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			getUI().setPollInterval(sTimeRefreshContent);
			LOGGER.info(username + " login successful");
			LOGGER.info("UserID " + username + " access with IP Address:" + webBrowser.getAddress());
			showMainForm();
			return true;
		} catch (AuthenticationException ex) {
			LOGGER.error(username + " login - " + ex.getMessage());
			return false;
		}
	}

}
