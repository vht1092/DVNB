package com.dvnb.components.pbcp;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dvnb.ReloadAutoComponent;
import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DvnbBillingGrp;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbBillingGrpService;
import com.dvnb.services.SysUserroleService;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * tanvh1 Aug 20, 2019
 *
 */
@SpringComponent
@Scope("prototype")
public class UpdateCodeFee extends CustomComponent implements ReloadAutoComponent, ReloadComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateCodeFee.class);
	
	public static final String CAPTION = "UPDATE BILLING GRP";
	
	private static final String CARD_BRN = "LOẠI THẺ";
	private static final String VIEW = "VIEW";
	
	public final transient FormLayout formLayout = new FormLayout();
	
	private final SysUserroleService sysUserroleService;
	private DvnbBillingGrpService dvnbBillingGrpService;
	private SpringConfigurationValueHelper configurationHelper;
	
	public final transient Grid gridContent;
	public final transient IndexedContainer containerContent;
	
	public final transient ComboBox cbbCardbrn;
	final Button btView = new Button(VIEW);
	private Window confirmDialog = new Window();
	private Button bYes;
	private Button bNo;
	private transient String sUserId;
	private String CheckUserId = "";
	final TimeConverter timeConverter = new TimeConverter();
	
	private File fileImport = null;
	private String fileNameImport;
	
	public UpdateCodeFee() {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		dvnbBillingGrpService = (DvnbBillingGrpService) helper.getBean("dvnbBillingGrpService");
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		sysUserroleService = (SysUserroleService) helper.getBean("sysUserroleService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		this.sUserId = SecurityUtils.getUserId();
		CheckUserId = sysUserroleService.findByRoleId(sUserId);
		
		Label lbCardbrn = new Label("LOẠI THẺ");
		cbbCardbrn = new ComboBox();
		cbbCardbrn.setNullSelectionAllowed(false);
		cbbCardbrn.setWidth("20%");
		cbbCardbrn.addItems("MC","VS");
		cbbCardbrn.setValue("VS");
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(100.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		btView.addClickListener(event -> {
			if(!cbbCardbrn.isEmpty()) {
				String cardBrn = cbbCardbrn.getValue().toString();
				refreshData(cardBrn);
			}
			
		});
		
		Upload chooseFile = new Upload(null, new Upload.Receiver() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream outputFile = null;
				try {
					// TODO Auto-generated method stub
					fileNameImport = StringUtils.substringBefore(filename, ".xlsx") + "_" + timeConverter.getCurrentTime() + ".xlsx";

					Window confirmDialog = new Window();
					final FormLayout content = new FormLayout();
					content.setMargin(true);

					Button bYes = new Button("OK");

					confirmDialog.setCaption("Dữ liệu sẽ import, vui lòng chờ..");
					confirmDialog.setWidth(400.0f, Unit.PIXELS);
					
					if (!filename.isEmpty()) {
						fileImport = new File(configurationHelper.getPathFileRoot() + "/" + fileNameImport);
						if (!fileImport.exists()) {
							fileImport.createNewFile();
						}
						outputFile = new FileOutputStream(fileImport);

						bYes.addClickListener(event -> {
							try {
								String cardbrn = cbbCardbrn.getValue().toString();
								InputStream is = null;
								is = new FileInputStream(fileImport);

								LOGGER.info("Reading file " + fileImport.getName());
								// XSSFWorkbook workbook = new XSSFWorkbook(is);
								Workbook workbook = StreamingReader.builder().rowCacheSize(100) // number of rows to keep in memory (defaults to 10)
										.bufferSize(4096) // buffer size to use when reading InputStream to file (defaults to 1024)
										.open(is);

								Sheet sheet = workbook.getSheetAt(0);

								LOGGER.info("Reading row in " + fileImport.getName());

								for (Row row : sheet) {
									if (row.getRowNum() > 0 && !isCellEmpty(row.getCell(1))) {
										DvnbBillingGrp dvnbBillingGrp = new DvnbBillingGrp();
										String creTms = timeConverter.getCurrentTime();
										dvnbBillingGrp.setCreTms(creTms);
										dvnbBillingGrp.setUsrId(sUserId);
										dvnbBillingGrp.setGrp(isCellEmpty(row.getCell(0)) ? "" : row.getCell(0).getStringCellValue().trim().replaceFirst("'", ""));
										String billingCode = isCellEmpty(row.getCell(1)) ? "" : row.getCell(1).getStringCellValue().trim().replaceFirst("'", "");
										dvnbBillingGrp.setBillingCde(billingCode);
										dvnbBillingGrp.setBillingName(isCellEmpty(row.getCell(2)) ? "" : row.getCell(2).getStringCellValue().trim().replaceFirst("'", ""));
										dvnbBillingGrp.setBillingDesc(isCellEmpty(row.getCell(3)) ? "" : row.getCell(3).getStringCellValue().trim().replaceFirst("'", ""));
										dvnbBillingGrp.setDriverCde(isCellEmpty(row.getCell(4)) ? "" : row.getCell(4).getStringCellValue().trim().replaceFirst("'", ""));
										dvnbBillingGrp.setDriverName(isCellEmpty(row.getCell(5)) ? "" : row.getCell(5).getStringCellValue().trim().replaceFirst("'", ""));
										dvnbBillingGrp.setCrdBrn(cardbrn);
										
										if(!dvnbBillingGrpService.existsById(billingCode)) 
						    				dvnbBillingGrpService.save(dvnbBillingGrp);
										
									}
								}
								confirmDialog.close();
								
								refreshData(cardbrn);
								
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								LOGGER.error(e.getMessage());
							}

						});

						// -----------------
						VerticalLayout layoutBtn = new VerticalLayout();
						layoutBtn.addComponents(bYes);
						layoutBtn.setComponentAlignment(bYes, Alignment.BOTTOM_CENTER);
						content.addComponent(layoutBtn);

						confirmDialog.setContent(content);

						getUI().addWindow(confirmDialog);

						// Center it in the browser window
						confirmDialog.center();
						confirmDialog.setResizable(false);
					} else
						outputFile = null;


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					LOGGER.error(e.toString());
				}
				return outputFile;

			}

		});
		chooseFile.setButtonCaption("IMPORT");
		chooseFile.addStyleName("myCustomUpload");
		
		gridContent = new Grid();
		gridContent.setSizeFull();
		gridContent.setHeightByRows(15);
		gridContent.setHeightMode(HeightMode.ROW);
		gridContent.setEditorEnabled(true);
		
		containerContent = new IndexedContainer();
		containerContent.addContainerProperty("cretms", String.class, "");
		containerContent.addContainerProperty("usrid", String.class, "");
		containerContent.addContainerProperty("updtms", String.class, "");
		containerContent.addContainerProperty("upduid", String.class, "");
		containerContent.addContainerProperty("grp", String.class, "");
		containerContent.addContainerProperty("billingCode", String.class, "");
		containerContent.addContainerProperty("billingName", String.class, "");
		containerContent.addContainerProperty("billDesc", String.class, "");
		containerContent.addContainerProperty("driverCode", String.class, "");
		containerContent.addContainerProperty("driverName", String.class, "");
		containerContent.addContainerProperty("cardBrn", String.class, "");
		
		gridContent.setContainerDataSource(containerContent);
		gridContent.getColumn("cretms").setHidden(true);
		gridContent.getColumn("usrid").setHidden(true);
		gridContent.getColumn("updtms").setHidden(true);
		gridContent.getColumn("upduid").setHidden(true);
		gridContent.getColumn("grp").setHeaderCaption("GRP");
		gridContent.getColumn("billingCode").setHeaderCaption("BILLING CODE");
		gridContent.getColumn("billingName").setHeaderCaption("BILLING NAME");
		gridContent.getColumn("billDesc").setHeaderCaption("BILLING DESCRIPTION");
		gridContent.getColumn("driverCode").setHeaderCaption("driver CODE");
		gridContent.getColumn("driverName").setHeaderCaption("driver NAME");
		gridContent.getColumn("cardBrn").setHeaderCaption("CARD BRN");
		
		refreshData("All");
		
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		
		final HorizontalLayout hBodyLayout = new HorizontalLayout();
		hBodyLayout.setSizeFull();
		hBodyLayout.setMargin(true);
		hBodyLayout.setSpacing(true);
		
		final HorizontalLayout layoutButton = new HorizontalLayout();
		layoutButton.setMargin(true);
		layoutButton.setSpacing(true);
		layoutButton.addComponent(lbCardbrn);
		layoutButton.addComponent(cbbCardbrn);
		layoutButton.addComponent(chooseFile);
		layoutButton.addComponent(btView);
		
//		formLayout.addComponent(layoutButton);
//		
//		hBodyLayout.addComponent(formLayout);
		
		
		mainLayout.addComponent(layoutButton);
		mainLayout.addComponent(gridContent);
		
		setCompositionRoot(mainLayout);
		
		
	}
	
	@Override
	public void eventReload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventReloadAuto() {
		// TODO Auto-generated method stub
		
	}
	
	private void refreshData(String cardBrn) {
//		String cardBrn = cbbCardbrn.getValue().toString();
		List<DvnbBillingGrp> listBillingGrp = new ArrayList<DvnbBillingGrp>();
		if(cardBrn.equals("All"))
			listBillingGrp.addAll(dvnbBillingGrpService.findAllByOrderByCreTms());
		else
			listBillingGrp.addAll(dvnbBillingGrpService.findAllByCrdBrnOrderByCreTms(cardBrn));
		
		if (!listBillingGrp.isEmpty()) {
			if (!containerContent.getItemIds().isEmpty()) {
				containerContent.removeAllItems();
			}
			for (int i = 0; i <= listBillingGrp.size() - 1; i++) {
				Item item = containerContent.getItem(containerContent.addItem());
				item.getItemProperty("cretms").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getCreTms()) ? listBillingGrp.get(i).getCreTms() : "");
				item.getItemProperty("usrid").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getUsrId()) ? listBillingGrp.get(i).getUsrId() : "");
				item.getItemProperty("updtms").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getUpdTms()) ? listBillingGrp.get(i).getUpdTms() : "");
				item.getItemProperty("upduid").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getUpdUid()) ? listBillingGrp.get(i).getUpdUid() : "");
				item.getItemProperty("grp").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getGrp()) ? listBillingGrp.get(i).getGrp() : "");
				item.getItemProperty("billingCode").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getBillingCde()) ? listBillingGrp.get(i).getBillingCde(): "");
				item.getItemProperty("billingName").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getBillingName()) ? listBillingGrp.get(i).getBillingName() : "");
				item.getItemProperty("billDesc").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getBillingDesc()) ? listBillingGrp.get(i).getBillingDesc() : "");
				item.getItemProperty("driverCode").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getDriverCde()) ? listBillingGrp.get(i).getDriverCde() : "");
				item.getItemProperty("driverName").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getDriverName()) ? listBillingGrp.get(i).getDriverName() : "");
				item.getItemProperty("cardBrn").setValue(StringUtils.isNotEmpty(listBillingGrp.get(i).getCrdBrn()) ? listBillingGrp.get(i).getCrdBrn() : "");
				
			}
		}
		else
			containerContent.removeAllItems();
	}
	
	public static boolean isCellEmpty(final Cell cell) {
	    if (cell == null) { // use row.getCell(x, Row.CREATE_NULL_AS_BLANK) to avoid null cells
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().trim().isEmpty()) {
	        return true;
	    }

	    return false;
	}

}
