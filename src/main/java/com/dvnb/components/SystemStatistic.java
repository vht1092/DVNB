package com.dvnb.components;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringContextHelper;
import com.dvnb.services.SysUserroleService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Man hinh thong ke chung
 * 
 */
@SpringComponent
@ViewScope
public class SystemStatistic extends VerticalLayout implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	public static final String CAPTION = "THÔNG TIN ĐĂNG NHẬP";
	private final Label lbLatestLogin = new Label();
	private final Label lbTotalAssignedCases = new Label();
	private final SysUserroleService sysUserroleService;
	private String sUserId = "";
	private String CheckUserId = "";
	private String tenPhongBan = "";

	public SystemStatistic() {
		setCaption(CAPTION);
		setSpacing(true);
		setMargin(true);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		sysUserroleService = (SysUserroleService) helper.getBean("sysUserroleService");
		
		this.sUserId = SecurityUtils.getUserId();
		CheckUserId = sysUserroleService.findByRoleId(sUserId);
		
		final Date date = new Date();
		final SimpleDateFormat simpledateformat_current = new SimpleDateFormat("dd/M/yyyy");

		switch(CheckUserId) {
			case "1": 
				tenPhongBan = "PHÒNG GIÁM SÁT VÀ XỬ LÝ TRA SOÁT THẺ VÀ NGÂN HÀNG SỐ";
				break;
			case "2": 
				tenPhongBan = "PHÒNG KỸ THUẬT THẺ VÀ NGÂN HÀNG SỐ";
				break;
			case "3": 
				tenPhongBan = "PHÒNG NGHIÊN CỨU VÀ PTKD THẺ VÀ NGÂN HÀNG SỐ";
				break;
			case "4": 
				tenPhongBan = "KHỐI THẺ VÀ NGÂN HÀNG SỐ";
				break;
		}
		final Label label_title = new Label("Phòng ban: " + tenPhongBan);
		label_title.setStyleName(ValoTheme.LABEL_H3);
		
		lbLatestLogin.setValue("Latest login: [" + SecurityUtils.getLastLogin() + "]");
//		lbTotalAssignedCases.setValue("Tổng số case đã xử lý: [" + "" + "]");

		addComponent(label_title);
		addComponent(lbLatestLogin);
		addComponent(lbTotalAssignedCases);
	}

	@Override
	public void eventReload() {
		lbLatestLogin.setValue("Latest login: [" + SecurityUtils.getLastLogin() + "]");
//		lbTotalAssignedCases.setValue("Tổng số case đã xử lý: [" + "" + "]");

	}

}
