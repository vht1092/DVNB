package com.dvnb.components;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.ldap.UncategorizedLdapException;

import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@Scope("prototype")
public class LoginForm extends VerticalLayout {

	private static final long serialVersionUID = -2856504351116608566L;
	public static final String VIEW_NAME = "LoginView";
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginForm.class);
	private SpringConfigurationValueHelper configurationHelper;
	
	public LoginForm(final LoginCallback callback) {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		setSizeFull();
		Page.getCurrent().setTitle("Đăng nhập");
		final Panel panelLogin = new Panel();
		panelLogin.setCaption("Đăng nhập");
		panelLogin.setStyleName(ValoTheme.PANEL_WELL);
		panelLogin.setWidth(null);
		final VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);

		final TextField txtfUserName = new TextField("Tên đăng nhập (không bao gồm @scb.com.vn)");
		txtfUserName.setIcon(FontAwesome.USER);
		txtfUserName.setWidth("400px");

		txtfUserName.addValidator(new StringLengthValidator("Vui lòng nhập tên đăng nhập", 1, 20, false));
		txtfUserName.setValidationVisible(false);
		contentLayout.addComponent(txtfUserName);

		final PasswordField pfPassword = new PasswordField("Mật khẩu");
		pfPassword.setWidth("400px");
		pfPassword.setIcon(FontAwesome.LOCK);
		pfPassword.addValidator(new StringLengthValidator("Vui lòng nhập mật khẩu", 3, 50, false));
		pfPassword.setValidationVisible(false);
		contentLayout.addComponent(pfPassword);
		
		final ComboBox cbbAppname = new ComboBox("Choose application");
		cbbAppname.setWidth("400px");
		cbbAppname.setNullSelectionAllowed(false);
		cbbAppname.setIcon(FontAwesome.WINDOWS);
		cbbAppname.addItem("DVNB");
		cbbAppname.addItem("PBCP");
		cbbAppname.addItem("DSQT");
		cbbAppname.setItemCaption("DVNB", "DỊCH VỤ NỘI BỘ");
		cbbAppname.setItemCaption("PBCP", "PHÂN BỔ CHI PHÍ INVOICE");
		cbbAppname.setItemCaption("DSQT", "ĐỐI SOÁT THANH QUYẾT TOÁN");
		contentLayout.addComponent(cbbAppname);
		
		cbbAppname.addValueChangeListener(item -> {
			configurationHelper.setAppname(item.getProperty().getValue().toString());
		});
		
		final Button btLogin = new Button("Đăng nhập", evt -> {
			txtfUserName.setValidationVisible(false);
			pfPassword.setValidationVisible(false);
			try {
				if(cbbAppname.getValue() == null) {
					Notification.show("Chưa chọn application", Type.ERROR_MESSAGE);
					return;
				} else {
					configurationHelper.setAppname(cbbAppname.getValue().toString());
				}
				
				txtfUserName.validate();
				pfPassword.validate();

				String pword = pfPassword.getValue();
				pfPassword.setValue("");
				if (!callback.login(txtfUserName.getValue().toLowerCase(), pword)) {
					Notification.show("Đăng nhập không thành công", Type.ERROR_MESSAGE);
					txtfUserName.focus();
				}

			} catch (InvalidValueException e) {
				txtfUserName.setValidationVisible(true);
				pfPassword.setValidationVisible(true);
			} catch (UncategorizedLdapException ex) {
				Notification.show("Đăng nhập không thành công", Type.ERROR_MESSAGE);
				LOGGER.error("Login fail " + txtfUserName + " - Message:" + ex.getMessage());
				txtfUserName.focus();
			}

		});
		btLogin.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btLogin.setIcon(FontAwesome.SIGN_IN);
		btLogin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		contentLayout.addComponent(btLogin);

		panelLogin.setContent(contentLayout);		
		
		VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
		HttpServletRequest httpServletRequest = ((VaadinServletRequest)vaadinRequest).getHttpServletRequest();
		String urlServer = getBaseUrl(httpServletRequest);
		
		final Label lbHeader = new Label(
				"<div style=\"background-color: #0072c6;\">" + "<img class=\"v-icon\" src=\"" + urlServer + "/VAADIN/themes/mytheme/img/logo.png\"></img>" 
				+ " <span style=\"font-size:24px; color:#ffffff;font-weight: bold;text-transform: uppercase;\">K.T&NHS - DỊCH VỤ NỘI BỘ</span></div>");
		lbHeader.setContentMode(ContentMode.HTML);

		final Label lbFireFox = new Label("Ứng dụng dùng tốt trên <img class=\"v-icon\" src=\'" + urlServer + "/VAADIN/themes/mytheme/img/rsz_mozilla-firefox-icon.png'></img>"
				+ "<img class=\"v-icon\" src=\'" + urlServer + "/VAADIN/themes/mytheme/img/rsz_internet-chrome-icon.png'></img><br/>Bản quyền © 2019 Ngân Hàng TMCP SCB. Version 1.0");
		lbFireFox.setCaptionAsHtml(true);
		lbFireFox.setContentMode(ContentMode.HTML);
		lbFireFox.setStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);

		addComponent(lbHeader);
		
		setComponentAlignment(lbHeader, Alignment.TOP_CENTER);
		setExpandRatio(lbHeader, 1f);
		addComponent(panelLogin);
		setComponentAlignment(panelLogin, Alignment.MIDDLE_CENTER);
		setExpandRatio(panelLogin, 2f);
		addComponent(lbFireFox);
		setComponentAlignment(lbFireFox, Alignment.BOTTOM_CENTER);
		setExpandRatio(lbFireFox, 1f);

	}

	@FunctionalInterface
	public interface LoginCallback {
		boolean login(String username, String password);
	}

	private String getBaseUrl(HttpServletRequest req) {
	    return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
	}
}
