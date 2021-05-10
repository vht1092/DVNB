package com.dvnb.components.pbcp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@SpringComponent
@Scope("prototype")
public class DataGridInvoiceTreoComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridInvoiceTreoComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	public Grid gridUnselect = new Grid();
	private static final String EXPORTKETCHUYEN = "EXPORT - KẾT CHUYỂN";
	private static final String EXPORTKHONGKETCHUYEN = "EXPORT - KHÔNG KẾT CHUYỂN";
	private static final String EXPORTUPLOAD = "EXPORT FILE UPLOAD";
	
	
	private final transient DvnbInvoiceMcService dvnbInvoiceMcService;
	private final transient DvnbInvoiceVsService dvnbInvoiceVsService;
	private final transient DvnbInvoiceUploadService dvnbInvoiceUploadService;
	
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	private IndexedContainer containerUnselect= new IndexedContainer();
	private final transient HorizontalLayout formKetChuyen;
	private String cardBrn;
	private String ky;
	private String ketchuyenFlag;
	private String fileNameExport;
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	private SpringConfigurationValueHelper configurationHelper;
	public String filename;
	protected DataSource localDataSource;
	
	public Page<DvnbInvoiceMc> dataSourceMC;
	public Page<DvnbInvoiceVs> dataSourceVS;
	
	List<InvoiceUpload> invoiceUploadSummary;
	List<InvoiceUpload> invoiceUploadDetail;
	
	@SuppressWarnings("unchecked")
	public DataGridInvoiceTreoComponent() {

		setSizeFull();

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		dvnbInvoiceMcService = (DvnbInvoiceMcService) helper.getBean("dvnbInvoiceMcService");
		dvnbInvoiceVsService = (DvnbInvoiceVsService) helper.getBean("dvnbInvoiceVsService");
		dvnbInvoiceUploadService = (DvnbInvoiceUploadService) helper.getBean("dvnbInvoiceUploadService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();

		// init grid
		grid = new Grid();
		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(15);
//		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.setEditorEnabled(true);
		
		container = new IndexedContainer();
		
		grid.addSelectionListener(event -> {
			
			if(!event.getAdded().isEmpty())
				event.getAdded().forEach(item -> {
					if(grid.getContainerDataSource().getItemIds().contains(item)) {
						switch(cardBrn) {
							case "MC":
								grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").setValue("Y");
//								dvnbInvoiceMcService.updateKetChuyenById("Y", grid.getContainerDataSource().getContainerProperty(item, "id").getValue().toString());
								break;
							case "VS":
								grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").setValue("Y");
//								dvnbInvoiceVsService.updateKetChuyenById("Y", grid.getContainerDataSource().getContainerProperty(item, "id").getValue().toString());
								break;
						
						}
					}
					
					
				});
				
			
			if(!event.getRemoved().isEmpty())
				event.getRemoved().forEach(item -> {
					if(grid.getContainerDataSource().getItemIds().contains(item)) {
						switch(cardBrn) {
							case "MC":
								grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").setValue("N");
//								dvnbInvoiceMcService.updateKetChuyenById("N", grid.getContainerDataSource().getContainerProperty(item, "id").getValue().toString());
								break;
							case "VS":
								grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").setValue("N");
//								dvnbInvoiceVsService.updateKetChuyenById("N", grid.getContainerDataSource().getContainerProperty(item, "id").getValue().toString());
								break;
						}
					}
					
				});
			
		});
		
		grid.setCellStyleGenerator(cell -> {
			BigDecimal numQuantityAmount;
			BigDecimal numRate;
			BigDecimal numCharge;
			BigDecimal numTaxCharge;
			BigDecimal numTotalCharge;
			BigDecimal numUnits;
			BigDecimal numTotal;
			BigDecimal numForeignExchangeRate;
			
			if(cardBrn.equals("MC")) {
				if (cell.getPropertyId().equals("quantityAmount")) {
					System.out.println("quantityAmount: " + cell.getItem().getItemProperty("quantityAmount").getValue());
					numQuantityAmount = cell.getItem().getItemProperty("quantityAmount").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("quantityAmount").getValue().toString());
					if(numQuantityAmount.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("rate")) {
					numRate = cell.getItem().getItemProperty("rate").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("rate").getValue().toString());
					if(numRate.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("charge")) {
					numCharge = cell.getItem().getItemProperty("charge").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("charge").getValue().toString());
					if(numCharge.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("taxCharge")) {
					numTaxCharge = cell.getItem().getItemProperty("taxCharge").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("taxCharge").getValue().toString());
					if(numTaxCharge.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("totalCharge")) {
					numTotalCharge = cell.getItem().getItemProperty("totalCharge").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("totalCharge").getValue().toString());
					if(numTotalCharge.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("deviation")) {
					return "v-align-right";
				}
				
			}
			if(cardBrn.equals("VS")) {
				if (cell.getPropertyId().equals("units")) {
					System.out.println(cell.getItem().getItemProperty("units").getValue());
					
					numUnits = cell.getItem().getItemProperty("units").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("units").getValue().toString());
					if(numUnits.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("total")) {
					numTotal = cell.getItem().getItemProperty("total").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("total").getValue().toString());
					if(numTotal.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("foreignExchangeRate")) {
					numForeignExchangeRate = cell.getItem().getItemProperty("foreignExchangeRate").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("foreignExchangeRate").getValue().toString());
					if(numForeignExchangeRate.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("rate")) {
					numRate = cell.getItem().getItemProperty("rate").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("rate").getValue().toString());
					if(numRate.compareTo(BigDecimal.ZERO) < 0) {
						return "v-align-right-color";
					}
					return "v-align-right";
				}
				if (cell.getPropertyId().equals("deviation")) {
					return "v-align-right";
				}
			}
			return "";
				
		});
		
		formKetChuyen = new HorizontalLayout();
		formKetChuyen.setSpacing(true);
		formKetChuyen.setVisible(false);
		formKetChuyen.setMargin(true);
		
		
		
		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);
		addComponent(formKetChuyen);
		setComponentAlignment(formKetChuyen, Alignment.BOTTOM_CENTER);
	}

	public void initGrid(final String cardBrn, String kymoi, final String getColumn) {
		
		this.cardBrn = cardBrn;
		this.ky = kymoi;
		
		if(!cardBrn.equals("MC") && !cardBrn.equals("VS")) {
			formKetChuyen.setVisible(false);
		}
		
		IndexedContainer containerTemp = new IndexedContainer();
		try {
			containerTemp = (IndexedContainer) container.clone();
			for (Object propertyId : containerTemp.getContainerPropertyIds()) {
			    container.removeContainerProperty(propertyId);
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		switch(cardBrn) {
			case "MC":
				formKetChuyen.setVisible(true);
				container.addContainerProperty("ketChuyen", String.class, "");
				container.addContainerProperty("ngayThucHien", String.class, "");
				container.addContainerProperty("kyMoi", String.class, "");
				container.addContainerProperty("invoiceNumber", String.class, "");
				container.addContainerProperty("billingCycleDate", String.class, "");
				container.addContainerProperty("eventId", String.class, "");
				container.addContainerProperty("eventDesc", String.class, "");
				container.addContainerProperty("totalCharge", String.class, "");
				container.addContainerProperty("deviation", String.class, "");
				container.addContainerProperty("serviceCode", String.class, "");
				container.addContainerProperty("serviceCodeDesc", String.class, "");
				container.addContainerProperty("uom", String.class, "");
				container.addContainerProperty("quantityAmount", String.class, "");
				container.addContainerProperty("currency", String.class, "");
				container.addContainerProperty("rate", String.class, "");
				container.addContainerProperty("charge", String.class, "");
				container.addContainerProperty("taxCharge", String.class, "");
				container.addContainerProperty("vatCharge", String.class, "");
				container.addContainerProperty("vatCurrency", String.class, "");
				container.addContainerProperty("vatCode", String.class, "");
				container.addContainerProperty("vatRate", String.class, "");
				container.addContainerProperty("docType", String.class, "");
				container.addContainerProperty("invoiceIca", String.class, "");
				container.addContainerProperty("activityIca", String.class, "");
				container.addContainerProperty("billableIca", String.class, "");
				container.addContainerProperty("collectionMethod", String.class, "");
				container.addContainerProperty("periodStartTime", String.class, "");
				container.addContainerProperty("periodEndTime", String.class, "");
				container.addContainerProperty("originalInvoiceNumber", String.class, "");
				container.addContainerProperty("affiliate", String.class, "");
				container.addContainerProperty("sbfExplanatoryText", String.class, "");
				container.addContainerProperty("id", String.class, "");
				
				grid.setContainerDataSource(container);
				grid.getColumn("ketChuyen").setHeaderCaption("KẾT CHUYỂN VỀ ĐƠN VỊ");
//				grid.getColumn("ketChuyen").setWidth(100);
				grid.getColumn("ngayThucHien").setHeaderCaption("NGÀY THỰC HIỆN");
				grid.getColumn("kyMoi").setHeaderCaption("KỲ MỚI");
//				grid.getColumn("id").setHidden(true);
				grid.getColumn("docType").setHeaderCaption("DOCUMENT TYPE");
				grid.getColumn("invoiceNumber").setHeaderCaption("INVOICE NUMBER");
				grid.getColumn("currency").setHeaderCaption("CURRENCY");
				grid.getColumn("billingCycleDate").setHeaderCaption("BILLING CYCLE DATE");
				grid.getColumn("invoiceIca").setHeaderCaption("INVOICE ICA");
				grid.getColumn("activityIca").setHeaderCaption("ACTIVITY ICA");
				grid.getColumn("billableIca").setHeaderCaption("BILLABLE ICA");
				grid.getColumn("collectionMethod").setHeaderCaption("COLLECTION METHOD ");
				grid.getColumn("serviceCode").setHeaderCaption("SERVICE CODE");
				grid.getColumn("serviceCodeDesc").setHeaderCaption("SERVICE CODE DESCRIPTION");
				grid.getColumn("periodStartTime").setHeaderCaption("PERIOD START TIME");
				grid.getColumn("periodEndTime").setHeaderCaption("PERIOD END TIME");
				grid.getColumn("originalInvoiceNumber").setHeaderCaption("ORIGINAL INVOICE NUMBER");
				grid.getColumn("eventId").setHeaderCaption("EVENT ID");
				grid.getColumn("eventDesc").setHeaderCaption("EVENT DESCRIPTION");
				grid.getColumn("eventDesc").setWidth(250);
				grid.getColumn("deviation").setHeaderCaption("DEVIATION");
				grid.getColumn("deviation").setEditable(true);
				grid.getColumn("totalCharge").setHeaderCaption("TOTAL CHARGE");
				grid.getColumn("totalCharge").setRenderer(new HtmlRenderer());
				grid.getColumn("totalCharge").setWidth(130);
				grid.getColumn("affiliate").setHeaderCaption("AFFILIATE");
				grid.getColumn("uom").setHeaderCaption("UOM");
				grid.getColumn("quantityAmount").setHeaderCaption("QUANTITY AMOUNT");
				grid.getColumn("quantityAmount").setRenderer(new HtmlRenderer());
				grid.getColumn("quantityAmount").setWidth(130);
				grid.getColumn("rate").setHeaderCaption("RATE");
				grid.getColumn("rate").setRenderer(new HtmlRenderer());
				grid.getColumn("rate").setWidth(130);
				grid.getColumn("charge").setHeaderCaption("CHARGE");
				grid.getColumn("charge").setRenderer(new HtmlRenderer());
				grid.getColumn("rate").setWidth(130);
				grid.getColumn("taxCharge").setHeaderCaption("TAX CHARGE");
				grid.getColumn("taxCharge").setRenderer(new HtmlRenderer());
				grid.getColumn("taxCharge").setWidth(130);
				grid.getColumn("vatCharge").setHeaderCaption("VAT CHARGE");
//				grid.getColumn("vatCharge").setWidth(100);
				grid.getColumn("vatCurrency").setHeaderCaption("VAT CURRENCY");
//				grid.getColumn("vatCurrency").setWidth(100);
				grid.getColumn("vatCode").setHeaderCaption("VAT CODE");
//				grid.getColumn("vatCode").setWidth(100);
				grid.getColumn("vatRate").setHeaderCaption("VAT RATE");
//				grid.getColumn("vatRate").setWidth(100);
				grid.getColumn("sbfExplanatoryText").setHeaderCaption("SBF EXPLANATORY TEXT");
				
				
				break;
				
			case "VS":
				formKetChuyen.setVisible(true);
				
				container.addContainerProperty("ketChuyen", String.class, "");
				container.addContainerProperty("ngayThucHien", String.class, "");
				container.addContainerProperty("kyMoi", String.class, "");
				container.addContainerProperty("invoiceId", String.class, "");
				container.addContainerProperty("invoiceDate", String.class, "");
				container.addContainerProperty("billingLine", String.class, "");	
				container.addContainerProperty("description", String.class, "");
				container.addContainerProperty("total", String.class, ""); 	
				container.addContainerProperty("deviation", String.class, "");
				container.addContainerProperty("type", String.class, "");
				container.addContainerProperty("rateType", String.class, "");	
				container.addContainerProperty("units", String.class, "");	
				container.addContainerProperty("rateCur", String.class, "");
				container.addContainerProperty("rate", String.class, "");
				container.addContainerProperty("foreignExchangeRate", String.class, "");
				container.addContainerProperty("billingCurrency", String.class, "");	
				container.addContainerProperty("taxType", String.class, ""); 	
				container.addContainerProperty("tax", String.class, ""); 	
				container.addContainerProperty("taxRate", String.class, "");
				container.addContainerProperty("taxCurrency", String.class, "");	
				container.addContainerProperty("taxableAmount", String.class, "");
				container.addContainerProperty("taxTaxCurrency", String.class, "");	
				container.addContainerProperty("billingPeriod", String.class, "");
				container.addContainerProperty("invoiceAccount", String.class, "");
				container.addContainerProperty("name", String.class, "");
				container.addContainerProperty("subInvoice", String.class, "");	
				container.addContainerProperty("currentOrPrevious", String.class, "");	
				container.addContainerProperty("entityType", String.class, "");
				container.addContainerProperty("entityId", String.class, "");
				container.addContainerProperty("binMap", String.class, "");
				container.addContainerProperty("entityName", String.class, "");	
				container.addContainerProperty("settlementId", String.class, "");
				container.addContainerProperty("futureUse", String.class, "");
				container.addContainerProperty("ntwk", String.class, "");
				container.addContainerProperty("id", String.class, "");
				
				grid.setContainerDataSource(container);
				grid.getColumn("ketChuyen").setHeaderCaption("KẾT CHUYỂN VỀ ĐƠN VỊ");
//				grid.getColumn("ketChuyen").setWidth(100);
				grid.getColumn("ngayThucHien").setHeaderCaption("NGÀY THỰC HIỆN");
				grid.getColumn("kyMoi").setHeaderCaption("KỲ MỚI");
//				grid.getColumn("id").setHidden(true);
				grid.getColumn("billingPeriod").setHeaderCaption("BILLING PERIOD");
				grid.getColumn("invoiceDate").setHeaderCaption("INVOICE DATE");
				grid.getColumn("invoiceAccount").setHeaderCaption("INVOICE ACCOUNT");
				grid.getColumn("name").setHeaderCaption("NAME");
				grid.getColumn("invoiceId").setHeaderCaption("INVOICE ID");
				grid.getColumn("subInvoice").setHeaderCaption("SUB INVOICE");	
				grid.getColumn("currentOrPrevious").setHeaderCaption("CURRENT OR PREVIOUS");	
				grid.getColumn("entityType").setHeaderCaption("ENTITY TYPE");
				grid.getColumn("entityId").setHeaderCaption("ENTITY ID");
				grid.getColumn("binMap").setHeaderCaption("BIN MAP");
				grid.getColumn("entityName").setHeaderCaption("ENTITY NAME");	
				grid.getColumn("settlementId").setHeaderCaption("SETTLEMENT ID");
				grid.getColumn("description").setHeaderCaption("DESCRIPTION");	
				grid.getColumn("description").setWidth(250);
				grid.getColumn("futureUse").setHeaderCaption("FUTURE USE");
				grid.getColumn("ntwk").setHeaderCaption("NTWK");
				grid.getColumn("billingLine").setHeaderCaption("BILLING LINE");	
				grid.getColumn("type").setHeaderCaption("TYPE");
				grid.getColumn("rateType").setHeaderCaption("RATE TYPE");	
				grid.getColumn("units").setHeaderCaption("UNITS");	
				grid.getColumn("units").setRenderer(new HtmlRenderer());
				grid.getColumn("rateCur").setHeaderCaption("RATE CUR");
				grid.getColumn("rate").setHeaderCaption("RATE");
				grid.getColumn("foreignExchangeRate").setHeaderCaption("FOREIGN EXCHANGE RATE");
				grid.getColumn("billingCurrency").setHeaderCaption("BILLING CURRENCY");	
				grid.getColumn("deviation").setHeaderCaption("DEVIATION");
				grid.getColumn("deviation").setEditable(true);
				grid.getColumn("total").setHeaderCaption("TOTAL");
				grid.getColumn("total").setRenderer(new HtmlRenderer());
				grid.getColumn("taxType").setHeaderCaption("TAX TYPE"); 	
				grid.getColumn("tax").setHeaderCaption("TAX"); 	
				grid.getColumn("taxRate").setHeaderCaption("TAX RATE");
				grid.getColumn("taxCurrency").setHeaderCaption("TAX CURRENCY");	
				grid.getColumn("taxableAmount").setHeaderCaption("TAXABLE AMOUNT (TAX CURRENCY)");
				grid.getColumn("taxTaxCurrency").setHeaderCaption("TAX (TAX CURRENCY)");	
				
				
				break;
		}
		
		if (createDataForContainer() == false) {
			if (!lbNoDataFound.isVisible() && (dataSourceMC != null || dataSourceVS != null)) {
				lbNoDataFound.setVisible(true);
				grid.setVisible(false);
			}
		} else {
			if (!grid.isVisible()) {
				grid.setVisible(true);
				lbNoDataFound.setVisible(false);
			}
		}
		
//		((SelectionModel.Multi) grid.getSelectionModel()).selectAll();
		
	}

	public void refreshData() {
		getUI().access(() -> {
			if (createDataForContainer() == false) {
				if (!lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(true);
				}
				if (grid.isVisible()) {
					grid.setVisible(false);
				}
			} else {
				if (lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(false);
				}
				if (!grid.isVisible()) {
					grid.setVisible(true);
				}
			}
//			((SelectionModel.Multi) grid.getSelectionModel()).selectAll();
			grid.getContainerDataSource().getItemIds().forEach(item -> {
				if(grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").getValue().equals("Y")) {
					grid.select(item);
				} else
					grid.deselect(item);
			});
		});
	}
	int i=0;
	@SuppressWarnings("unchecked")
	private boolean createDataForContainer() {
		boolean flagMC = false;
		boolean flagVS = false;
		
		if (dataSourceMC != null && dataSourceMC.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceMC.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				item.getItemProperty("id").setValue(s.getId());
				item.getItemProperty("docType").setValue(s.getDocType());
				item.getItemProperty("invoiceNumber").setValue(s.getInvoiceNumber());
				item.getItemProperty("currency").setValue(s.getCurrency());
				item.getItemProperty("billingCycleDate").setValue(s.getBillingCycleDate());
				item.getItemProperty("invoiceIca").setValue(s.getInvoiceIca());
				item.getItemProperty("activityIca").setValue(s.getActivityIca());
				item.getItemProperty("billableIca").setValue(s.getBillableIca());
				item.getItemProperty("collectionMethod").setValue(s.getCollectionMethod());
				item.getItemProperty("serviceCode").setValue(s.getServiceCode());
				item.getItemProperty("serviceCodeDesc").setValue(s.getServiceCodeDesc());
				item.getItemProperty("periodStartTime").setValue(s.getPeriodStartTime());
				item.getItemProperty("periodEndTime").setValue(s.getPeriodEndTime());
				item.getItemProperty("originalInvoiceNumber").setValue(s.getOriginalInvoiceNumber());
				item.getItemProperty("eventId").setValue(s.getEventId());
				item.getItemProperty("eventDesc").setValue(s.getEventDesc());
				item.getItemProperty("affiliate").setValue(s.getAffiliate());
				item.getItemProperty("uom").setValue(s.getUom());
//				String formatQuantityAmount = s.getQuantityAmount() == "" ? "" : formatNumberColor(new BigDecimal(s.getQuantityAmount()));
				item.getItemProperty("quantityAmount").setValue(s.getQuantityAmount());
//				String formatRate = s.getRate() == "" ? "" : formatNumberColor(new BigDecimal(s.getRate()));
				item.getItemProperty("rate").setValue(s.getRate());
//				String formatCharge = s.getCharge() == "" ? "" : formatNumberColor(new BigDecimal(s.getCharge()));
				item.getItemProperty("charge").setValue(s.getCharge());
//				String formatTaxCharge = s.getTaxCharge() == "" ? "" : formatNumberColor(new BigDecimal(s.getTaxCharge()));
				item.getItemProperty("taxCharge").setValue(s.getTaxCharge());
				item.getItemProperty("deviation").setValue(s.getDeviation());
//				String formatTotalCharge = s.getTotalCharge() == "" ? "" : formatNumberColor(new BigDecimal(s.getTotalCharge()));
				item.getItemProperty("totalCharge").setValue(s.getTotalCharge());
				item.getItemProperty("vatCharge").setValue(s.getVatCharge());
				item.getItemProperty("vatCurrency").setValue(s.getVatCurrency());
				item.getItemProperty("vatCode").setValue(s.getVatCode());
				item.getItemProperty("vatRate").setValue(s.getVatRate());
				item.getItemProperty("sbfExplanatoryText").setValue(s.getSbfExplanatoryText());
				item.getItemProperty("ketChuyen").setValue(s.getKetChuyen());
				item.getItemProperty("ngayThucHien").setValue(s.getNgayThucHien().toString());
				item.getItemProperty("kyMoi").setValue(s.getKyMoi());
//				grid.getSelectionModel().isSelected(item);
			});
			flagMC = true;
		} 
		
		if (dataSourceVS != null && dataSourceVS.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceVS.forEach(s -> {
				Item item = container.getItem(container.addItem());
				item.getItemProperty("id").setValue(s.getId());
				item.getItemProperty("billingPeriod").setValue(s.getBillingPeriod());
				item.getItemProperty("invoiceDate").setValue(s.getInvoiceDate());
				item.getItemProperty("invoiceAccount").setValue(s.getInvoiceAccount());
				item.getItemProperty("name").setValue(s.getName());
				item.getItemProperty("invoiceId").setValue(s.getInvoiceId());
				item.getItemProperty("subInvoice").setValue(s.getSubInvoice());	
				item.getItemProperty("currentOrPrevious").setValue(s.getCurrentOrPrevious());	
				item.getItemProperty("entityType").setValue(s.getEntityType());
				item.getItemProperty("entityId").setValue(s.getEntityId());
				item.getItemProperty("binMap").setValue(s.getBinMap());
				item.getItemProperty("entityName").setValue(s.getEntityName());	
				item.getItemProperty("settlementId").setValue(s.getSettlementId());
				item.getItemProperty("description").setValue(s.getDescription());	
				item.getItemProperty("futureUse").setValue(s.getFutureUse());
				item.getItemProperty("ntwk").setValue(s.getNtwk());
				item.getItemProperty("billingLine").setValue(s.getBillingLine());	
				item.getItemProperty("type").setValue(s.getType());
				item.getItemProperty("rateType").setValue(s.getRateType());	
//				String formatunits = s.getUnits() == "" ? "" : formatNumberColor(new BigDecimal(s.getUnits()));
				item.getItemProperty("units").setValue(s.getUnits());	
				item.getItemProperty("rateCur").setValue(s.getRateCur());
				item.getItemProperty("rate").setValue(s.getRate());
				item.getItemProperty("foreignExchangeRate").setValue(s.getForeignExchangeRate());
				item.getItemProperty("billingCurrency").setValue(s.getBillingCurrency());	
				item.getItemProperty("deviation").setValue(s.getDeviation());
//				String formatTotal = s.getTotal() == "" ? "" : formatNumberColor(new BigDecimal(s.getTotal()));
				item.getItemProperty("total").setValue(s.getTotal()); 	
				item.getItemProperty("taxType").setValue(s.getTaxType()); 	
				item.getItemProperty("tax").setValue(s.getTax()); 	
				item.getItemProperty("taxRate").setValue(s.getTaxRate());
				item.getItemProperty("taxCurrency").setValue(s.getTaxCurrency());	
				item.getItemProperty("taxableAmount").setValue(s.getTaxableAmount());
				item.getItemProperty("taxTaxCurrency").setValue(s.getTaxTaxCurrency());	
				item.getItemProperty("ketChuyen").setValue(s.getKetChuyen());
				item.getItemProperty("ngayThucHien").setValue(s.getNgayThucHien().toString());
				item.getItemProperty("kyMoi").setValue(s.getKyMoi());
//				grid.getSelectionModel().isSelected(item);
			});
			flagVS = true;
		} 
		
		if(flagMC == true || flagVS == true)
			return true;
		else 
			return false;
	}
	
	private void messageExportXLSX(String caption, String text) {
		Window confirmDialog = new Window();
		FormLayout content = new FormLayout();
        content.setMargin(true);
		Button bOK = new Button("OK");
		Label lbText = new Label(text);
		confirmDialog.setCaption(caption);
		confirmDialog.setWidth(300.0f, Unit.PIXELS);
		
		 bOK.addClickListener(event -> {
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			StreamResource resource = getStream(new File(pathExport + "\\" + fileNameOutput));
			downloader.setFileDownloadResource(resource);
			downloader.download();
         	confirmDialog.close();
         });
		
		VerticalLayout layoutBtn = new VerticalLayout();
		layoutBtn.addComponent(lbText);
        layoutBtn.addComponents(bOK);
        content.addComponent(layoutBtn);
        
        confirmDialog.setContent(content);

        getUI().addWindow(confirmDialog);
        // Center it in the browser window
        confirmDialog.center();
        confirmDialog.setResizable(false);
	}
	
	private StreamResource getStream(File inputfile) {
	    
	    StreamResource.StreamSource source = new StreamResource.StreamSource() {

	        public InputStream getStream() {
	           
	            InputStream input=null;
	            try
	            {
	                input = new  FileInputStream(inputfile);
	            } 
	            catch (FileNotFoundException e)
	            {
	                e.printStackTrace();
	            }
	              return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
	}
	
	private String formatNumberColor(BigDecimal number) {
		if (number.compareTo(BigDecimal.ZERO) < 0) {
			return "<span style=\"padding:7px 0px; background-color: #FFFF00\">" + number + "</span>";

		} else
			return String.valueOf(number);
	}
	
	public Map<String, Object> getParameter() {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_ky", ky);
			parameters.put("p_ketchuyen", ketchuyenFlag);
			parameters.put("p_ketchuyen_status", ketchuyenFlag.equals("Y") ? "KẾT CHUYỂN VỀ ĐƠN VỊ" : "KHÔNG KẾT CHUYỂN VỀ ĐƠN VỊ");
			parameters.put("p_tungay", "");
			parameters.put("p_denngay", "");

			return parameters;
	}
	
	private ByteArrayOutputStream makeFileForDownLoad(String filename, String extension) throws JRException, SQLException {

		Connection con = localDataSource.getConnection();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (this.getParameter() != null) {
			// Tham so truyen vao cho bao cao
			Map<String, Object> parameters = this.getParameter();

			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(configurationHelper.getPathFileRoot() + "/invoicetemplate/" + filename);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

			// Xuat file Excel
			if (extension.equals("XLSX")) {
				JRXlsxExporter xlsx = new JRXlsxExporter();
				xlsx.setExporterInput(new SimpleExporterInput(jasperPrint));
				xlsx.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
				xlsx.exportReport();
			} else if (extension.equals("PDF")) { // File PDF
				JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			}
			return output;
		} else {
			return null;
		}

	}
	
	@SuppressWarnings("serial")
	public StreamResource createTransMKResourceXLS() {
		return new StreamResource(new StreamSource() {
			@Override
			public InputStream getStream() {

				try {
					ByteArrayOutputStream outpuf = makeFileForDownLoad(filename, "XLSX");
					return new ByteArrayInputStream(outpuf.toByteArray());
				} catch (Exception e) {
					LOGGER.error("createTransMKResourceXLS - Message: " + e.getMessage());
				}
				return null;

			}
		}, "Invoice_baocao.xlsx");
	}
	
	private BigDecimal convertStringToBigdecimal(String stringValue) {
		stringValue = stringValue.replaceAll(",", "");
		if (stringValue.startsWith("(") && stringValue.endsWith(")"))
		{
		   return new BigDecimal(stringValue.substring(1, stringValue.length() - 1)).negate();
		}
		else
		{
		   return new BigDecimal(stringValue);
		}
	}
	

}
