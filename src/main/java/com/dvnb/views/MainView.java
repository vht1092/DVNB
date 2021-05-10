package com.dvnb.views;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.dvnb.DvnbTabType;
import com.dvnb.ReloadAutoComponent;
import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.components.SystemStatistic;
import com.dvnb.services.SysTxnService;
import com.vaadin.event.UIEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = MainView.VIEW_NAME, ui = MainUI.class)
public class MainView extends HorizontalLayout implements View {

	public static final String VIEW_NAME = "";
	private static final long serialVersionUID = 1L;
	private transient List<Object[]> listMenu = null;
	private transient Tree tree;
	@Autowired
	private transient SysTxnService sysTxnService;
	private final transient TabSheet tabsheet = new TabSheet();
	private static final Logger LOGGER = LoggerFactory.getLogger(MainView.class);
	private SpringConfigurationValueHelper configurationHelper;
	
	@PostConstruct
	void init() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		Page.getCurrent().setTitle("DỊCH VỤ NỘI BỘ - KHỐI THẺ & NGÂN HÀNG SỐ");
		setSizeFull();
		tabsheet.setSizeFull();
		tabsheet.addSelectedTabChangeListener(evt -> {
			refreshGridManual(evt.getTabSheet().getSelectedTab());
		});
		tabsheet.setCloseHandler((tabsheet, tabContent) -> {
			final Tab tab = tabsheet.getTab(tabContent);
//			if (tabContent instanceof CaseRegister) {
//				((CaseRegister) tabContent).closeProcessingCase();
//			}
			tabsheet.removeTab(tab);
		});
		tabsheet.addTab(new SystemStatistic(), SystemStatistic.CAPTION);
		tree = new Tree();
		tree.setImmediate(true);
		tree.addItemClickListener(evt -> {
			final String itemName = evt.getItemId().toString();
			addNewTab(itemName);

		});
		
		createMenu();
	}

	private void addNewTab(final String itemName) {
		try {
			final Class<? extends Component> tabType = DvnbTabType.getTabType(itemName).getClassComponent();
			addTab(tabType.newInstance(), itemName);
		} catch (Exception e) {
			LOGGER.error("Can not create instance " + itemName + " Message: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Dung load lai du lieu khi nguoi dung chon tab
	 * 
	 * @param tab
	 *            Component can refresh
	 */
	private void refreshGridManual(final Component tab) {
		if (tab instanceof ReloadComponent) {
			((ReloadComponent) tab).eventReload();
		}
	}

	/**
	 * Dung load lai du lieu tu dong
	 * 
	 * @param tab
	 *            Component can refresh
	 */
	private void refreshGrid(final Component tab) {
		if (tab instanceof ReloadAutoComponent) {
			((ReloadAutoComponent) tab).eventReloadAuto();
		}
	}

	/**
	 * Tao cay menu
	 */
	private void createMenu() {
		String appname = configurationHelper.getAppname();
		if (SecurityUtils.getUserId() != null) {
//			listMenu = sysTxnService.findAllByUserId(SecurityUtils.getUserId());
			listMenu = sysTxnService.findAllByUserIdAndAppName(SecurityUtils.getUserId(),appname);
		}

		final CssLayout cssLayout = new CssLayout();
		cssLayout.setPrimaryStyleName("valo-menu");
		cssLayout.addStyleName("sidebar");
		cssLayout.addStyleName("no-vertical-drag-hints");
		cssLayout.addStyleName("no-horizontal-drag-hints");
		cssLayout.setWidth(null);
		cssLayout.setHeight("100%");

		final VerticalLayout top = new VerticalLayout();
		top.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		top.addStyleName(ValoTheme.MENU_TITLE);
		top.setHeight(100f, Unit.PIXELS);
		top.setSpacing(true);
		
		VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
		HttpServletRequest httpServletRequest = ((VaadinServletRequest)vaadinRequest).getHttpServletRequest();
		String urlServer = getBaseUrl(httpServletRequest);
		
		String logoInside = "";
		switch(appname) {
			case "PBCP":
				logoInside = "logo_inside_pbcp.png";
				break;
			case "DSQT":
				logoInside = "logo_inside_dstqt.png";
				break;
			case "BATCH":
				logoInside = "logo_inside_batchipp.png";
				break;
			default:
				logoInside = "logo_inside_new.png";
				break;
		}
		
//		final Label title = new Label("<img class=\"v-icon\" src=\"" + urlServer + "/VAADIN/themes/mytheme/img/" + logoInside + "\"></img>");
		final Label title = new Label("<img class=\"v-icon\" src=\"" + urlServer + "/VAADIN/themes/mytheme/img/" + logoInside + "\" style='width: 215px;height: 90px;'></img>");
		title.setContentMode(ContentMode.HTML);//.addStyleName("title-header");
		//title.setSizeUndefined();
		
		top.addComponent(title);
		cssLayout.addComponent(top);

		final MenuBar settings = new MenuBar();
		settings.addStyleName("user-menu");
		final MenuItem settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
		settingsItem.setText(SecurityUtils.getFullname());
		settingsItem.setDescription("Đăng nhập gần đây: " + SecurityUtils.getLastLogin());
		settingsItem.addItem("Thoát", selectedItem -> {
			LOGGER.info(SecurityUtils.getUserId() + " logout");
			getUI().getSession().close();
			Page.getCurrent().setLocation("");
			getUI().setPollInterval(-1);
		});
		createTreeItem();
		cssLayout.addComponent(settings);
		cssLayout.addComponent(tree);
		addComponent(cssLayout);
		addComponent(tabsheet);
		setExpandRatio(tabsheet, 1);
	}

	/**
	 * Tao item cho cai menu
	 */
	public void createTreeItem() {
		for (final Object[] b : listMenu) {
			createTreeItem(b[0] != null ? b[0].toString() : "", b[1] != null ? b[1].toString() : "", b[2] != null ? b[2].toString() : "",
					b[4] != null ? b[4].toString() : "", b[5].toString().equals("V000") ? "" : b[5].toString());
		}

	}

	/**
	 * Tao item cho cai menu
	 * 
	 * @param
	 */
	private void createTreeItem(final String id, final String caption, final String description, final String icon, final String parent) {

		tree.addItem(description);
		if (!icon.equals("")) {
			tree.setItemIcon(description, FontAwesome.valueOf(icon));
		} else {
			tree.setItemIcon(description, FontAwesome.ANGLE_DOUBLE_RIGHT);
		}
		tree.setChildrenAllowed(description, true);
		for (final Object[] b : listMenu) {

			final String sParent = b[5] != null ? b[5].toString() : "";
			final String sDescription = b[2] != null ? b[2].toString() : "";
			if (sParent.equals(id)) {
				tree.addItem(sDescription);
				tree.setParent(sDescription, description);
			}
		}
		if (tree.hasChildren(description) == false) {
			tree.setChildrenAllowed(description, false);
		}else{
			tree.expandItemsRecursively(description);
		}
		
	}

	/**
	 * Them tab cho tabsheet neu tab da duoc add roi se khong tao moi
	 * 
	 * @param
	 */
	public void addTab(final Component component, final String caption) {
		boolean isFound = false;
		final Iterator<Component> iterator = tabsheet.iterator();
		while (iterator.hasNext()) {
			final Component comp = (Component) iterator.next();
			final Tab tab = tabsheet.getTab(comp);
			if (tab.getCaption().equals(caption)) {
				tabsheet.setSelectedTab(tab);
				isFound = true;
			}
		}
		if (isFound == false) {
			final Tab tab = tabsheet.addTab(component, caption);
			tab.setClosable(true);
			tabsheet.setSelectedTab(component);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (SecurityUtils.isLoggedIn()) {
			getUI().addPollListener(new UIEvents.PollListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void poll(UIEvents.PollEvent event) {
					refreshTabContent();					
				}
			});
		} else {
			getUI().getPage().reload();
		}
	}

	private synchronized void refreshTabContent() {
		final Iterator<Component> iterator = tabsheet.iterator();
		while (iterator.hasNext()) {
			final Component comp = (Component) iterator.next();
			refreshGrid(comp);
		}
	}
	
	private String getBaseUrl(HttpServletRequest req) {
	    return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath();
	}

}
