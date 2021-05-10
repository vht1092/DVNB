package com.dvnb.components.pbcp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.money.Monetary;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class ImportInvoice extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportInvoice.class);
	private final transient DvnbInvoiceMcService dvnbInvoiceMcService;
	private final transient DvnbInvoiceVsService dvnbInvoiceVsService;
	private final transient DvnbInvoiceUploadService dvnbInvoiceUploadService;
	private TyGiaService tyGiaService;
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "IMPORT FILE INVOICE";
	private DataGridInvoiceComponent grid;
	private static final String SHOW = "HIỂN THỊ";
//	private static final String IMPORT = "IMPORT TO DATABASE";
	private static final String CARD_BRN = "LOẠI THẺ";
	private static final String KY = "KỲ";
	private static final String TYGIA = "TỶ GIÁ";
	private static final String VIEW = "VIEW";
	private static final String DELETE = "REMOVE";

	private transient String sUserId;
	private transient Page<DvnbInvoiceMc> resultMC;
	private transient Page<DvnbInvoiceVs> resultVS;
	private final ComboBox cbbCardBrn;
	private final ComboBox cbbKy;
	
	final Button btView = new Button(VIEW);
	
	final Button btDelete = new Button(DELETE);
	final Button btUpdateKhongketchuyen = new Button("Update không kết chuyển");
	private TextField tfTyGia;
	private TextField tfEventID;
	private TextField tfInvoiceNumber;
	String fileNameImport;
	private Window confirmDialog = new Window();
	private Button bOK = new Button("OK");
	private String cardBrn = "";
	
	// Paging
	private final static int SIZE_OF_PAGE = 100;
	private final static int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	List<DvnbInvoiceMc> dvnbInvoiceMcList;
	List<DvnbInvoiceVs> dvnbInvoiceVsList;
	List<InvoiceUpload> invoiceUploadList;
	private final VerticalLayout mainLayout = new VerticalLayout();
	private int i;
	private int tongCongKhongPhanBo = 0;
	
	public ImportInvoice() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		final HorizontalLayout formLayout3rd = new HorizontalLayout();
		formLayout3rd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		dvnbInvoiceMcService = (DvnbInvoiceMcService) helper.getBean("dvnbInvoiceMcService");
		dvnbInvoiceVsService = (DvnbInvoiceVsService) helper.getBean("dvnbInvoiceVsService");
		dvnbInvoiceUploadService = (DvnbInvoiceUploadService) helper.getBean("dvnbInvoiceUploadService");
		tyGiaService = (TyGiaService) helper.getBean("tyGiaService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridInvoiceComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final Label lbEventID = new Label("Event ID");
		lbEventID.setVisible(false);
		tfEventID = new TextField();
		tfEventID.setVisible(false);
		
		final Label lbInvoiceNumber = new Label("Invoice Number");
		lbInvoiceNumber.setVisible(false);
		tfInvoiceNumber = new TextField();
		tfInvoiceNumber.setVisible(false);
		
		final Label lbCardBrn = new Label(CARD_BRN);
		cbbCardBrn = new ComboBox();
		cbbCardBrn.setNullSelectionAllowed(false);
		cbbCardBrn.addItems("MC","VS");

		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		final Label lbKy = new Label(KY);
		cbbKy = new ComboBox();
		cbbKy.setNullSelectionAllowed(false);
		cbbKy.setPageLength(12);
		descService.findAllByTypeByOrderBySequencenoDesc("KYBAOCAO").forEach(item -> {
			cbbKy.addItem(item.getId());
			cbbKy.setItemCaption(item.getId(),item.getDescription());
		});
		String kyhientai = String.valueOf(cal.get(Calendar.YEAR)) + StringUtils.leftPad(String.valueOf(cal.get(Calendar.MONTH)), 2, "0");
		cbbKy.setValue(kyhientai);
		cbbKy.addValueChangeListener(event -> {
			
//			tfTyGia.setValue(BigDecimal.ZERO.toString());
//			DecimalFormat tyGiaFormat = new DecimalFormat("#,###.00000");
//			
//			final Iterable<DvnbTyGia> listTygiaIter = tyGiaService.findAllByKyAndCardbrn(cbbKy.getValue().toString(),cbbCardBrn.getValue().toString());
//			if(listTygiaIter.iterator().hasNext()) {
//				BigDecimal tygia = listTygiaIter.iterator().next().getTyGia();
//				.setValue(tyGiaFormat.format(tygia));
//			}
			if(cbbKy.getValue()!=null && cbbCardBrn.getValue()!=null)
				tfTyGia.setValue(getTyGiaByKyAndCrdbrn(cbbKy.getValue().toString(),cbbCardBrn.getValue().toString()));
			else 
				tfTyGia.setValue("0");
			
		});
		
		cbbCardBrn.addValueChangeListener(event -> {
			if(!cbbKy.isEmpty() && !cbbCardBrn.isEmpty()) {
				tfTyGia.setValue(getTyGiaByKyAndCrdbrn(cbbKy.getValue().toString(),cbbCardBrn.getValue().toString()));
				String cardBrn = cbbCardBrn.getValue().toString();
				if(cardBrn.equals("VS")) {
					lbEventID.setValue("Billing Line");
					lbInvoiceNumber.setValue("Invoice ID");
					tongCongKhongPhanBo = dvnbInvoiceVsService.countInvoiceByKyAndKetChuyenStatus(cbbKy.getValue().toString(), "N");
				} else {
					lbEventID.setValue("Event ID");
					lbInvoiceNumber.setValue("Invoice Number");
					tongCongKhongPhanBo = dvnbInvoiceMcService.countInvoiceByKyAndKetChuyenStatus(cbbKy.getValue().toString(), "N");
				}
					
			}
				
			else 
				tfTyGia.setValue("0");
			
			
		});
		
		
		final Label lbTyGia = new Label(TYGIA);
		tfTyGia = new TextField();
		tfTyGia.setEnabled(false);
		
		final Label lbTotalKhongKetChuyen = new Label("Tổng cộng không phân bổ: ");
		lbTotalKhongKetChuyen.setVisible(false);
		
		pagingLayout = generatePagingLayout();	
		pagingLayout.setSpacing(true);
		
//		final Button btPaging = new Button();
//		btPaging.setCaption(reloadLabelPaging());
//		btPaging.setEnabled(false);

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
					
					confirmDialog.setCaption("Dữ liệu excel sẽ hiển thị trên lưới với chọn tất cả");
					confirmDialog.setWidth(350.0f, Unit.PIXELS);
			        try {
			        	if(!filename.isEmpty()) {
			        		fileImport = new File(configurationHelper.getPathFileRoot() + "/"+ fileNameImport);
				            if(!fileImport.exists()) {
				            	fileImport.createNewFile();
				            }
							outputFile =  new FileOutputStream(fileImport);
			        	
							bYes.addClickListener(event -> {
								cardBrn = cbbCardBrn.getValue().toString();
//					        	SHOW DATA IN GRID
								grid.dataSourceMC = null;
								grid.dataSourceVS = null;
								
								switch(cardBrn) {
									case "MC":
										try 
										{
											dvnbInvoiceMcList = new ArrayList<DvnbInvoiceMc>();
											InputStream is = null;
											try {
												is = new FileInputStream(fileImport);
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
									    	LOGGER.info("Reading file " + fileImport.getName());
//										    	XSSFWorkbook workbook = new XSSFWorkbook(is);
									    	Workbook workbook = StreamingReader.builder()
									        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
									        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
									        .open(is);  
											
									    	Sheet sheet = workbook.getSheetAt(0);
									    	
									    	LOGGER.info("Reading row in " + fileImport.getName());
									    	i = 0;
									    	try {
									    		for (Row row : sheet) {
										    		if(row.getRowNum()>0) {
										    			i++;
										    			DvnbInvoiceMc dvnbInvoiceMc = new DvnbInvoiceMc();
										    			String currentTime = timeConverter.getCurrentTime();
										    			dvnbInvoiceMc.setId(currentTime + String.format("%07d", i));
										    			dvnbInvoiceMc.setCreTms(new BigDecimal(currentTime));
										    			dvnbInvoiceMc.setUsrId(sUserId);
//										    			System.out.println("ROWNUM: " + row.getRowNum() + ", QuantityAmount: " + row.getCell(17) + row.getCell(17).getCellType() + ", TotalCharge: " + row.getCell(21) + row.getCell(21).getCellType());
										    			dvnbInvoiceMc.setDocType(getCellStringValue(row.getCell(0)));
										    			dvnbInvoiceMc.setInvoiceNumber(getCellStringValue(row.getCell(1)));
										    			dvnbInvoiceMc.setCurrency(getCellStringValue(row.getCell(2)));
										    			String billingCycleDate = row.getCell(3).getStringCellValue().substring(0, 8);
//										    			DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
//										    			                .parseCaseInsensitive()
//										    			                .appendPattern("dd/MM/yy")
//										    			                .toFormatter(Locale.UK);
//										    			LocalDate billingCycleDateFormatter = LocalDate.parse(billingCycleDate, dateFormatter);
										    			
										    			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
										    			Date billingCycleDateConvers = sdf.parse(billingCycleDate);
										    			sdf = new SimpleDateFormat("yyyy/MM/dd");
										    			dvnbInvoiceMc.setBillingCycleDate(sdf.format(billingCycleDateConvers));
										    			dvnbInvoiceMc.setInvoiceIca(getCellStringValue(row.getCell(4)));
										    			dvnbInvoiceMc.setActivityIca(getCellStringValue(row.getCell(5)));
										    			dvnbInvoiceMc.setBillableIca(getCellStringValue(row.getCell(6)));
										    			dvnbInvoiceMc.setCollectionMethod(getCellStringValue(row.getCell(7)));
										    			dvnbInvoiceMc.setServiceCode(getCellStringValue(row.getCell(8)));
										    			dvnbInvoiceMc.setServiceCodeDesc(getCellStringValue(row.getCell(9)));
										    			dvnbInvoiceMc.setPeriodStartTime(getCellStringValue(row.getCell(10)));
										    			dvnbInvoiceMc.setPeriodEndTime(getCellStringValue(row.getCell(11)));
										    			dvnbInvoiceMc.setOriginalInvoiceNumber(getCellStringValue(row.getCell(12)));
										    			dvnbInvoiceMc.setEventId(getCellStringValue(row.getCell(13)));
										    			dvnbInvoiceMc.setEventDesc(getCellStringValue(row.getCell(14)));
										    			dvnbInvoiceMc.setAffiliate(getCellStringValue(row.getCell(15)));
										    			dvnbInvoiceMc.setUom(getCellStringValue(row.getCell(16)));
										    			dvnbInvoiceMc.setQuantityAmount(getCellNumberValue(row.getCell(17)).replaceAll(",", ""));
										    			dvnbInvoiceMc.setRate(getCellNumberValue(row.getCell(18)));
										    			dvnbInvoiceMc.setCharge(getCellNumberValue(row.getCell(19)));
										    			dvnbInvoiceMc.setTaxCharge(getCellNumberValue(row.getCell(20)));
										    			dvnbInvoiceMc.setTotalCharge(getCellNumberValue(row.getCell(21)));
										     			dvnbInvoiceMc.setVatCharge(getCellNumberValue(row.getCell(22)));
										    			dvnbInvoiceMc.setVatCurrency(getCellNumberValue(row.getCell(23)));
										    			dvnbInvoiceMc.setVatCode(getCellNumberValue(row.getCell(24)));
										    			dvnbInvoiceMc.setVatRate(getCellNumberValue(row.getCell(25)));
										    			dvnbInvoiceMc.setSbfExplanatoryText(getCellStringValue(row.getCell(26)));
										    			dvnbInvoiceMc.setKy(cbbKy.getValue().toString());
										    			dvnbInvoiceMc.setKetChuyen("Y");
										    			dvnbInvoiceMc.setNgayThucHien(new BigDecimal(timeConverter.getCurrentTime().substring(0, 8)));
										    			dvnbInvoiceMc.setDeviation("0");
										    			
										    			System.out.println("dvnbInvoiceMc: " +" " + dvnbInvoiceMc.getActivityIca() +" " + dvnbInvoiceMc.getAffiliate()+" " + dvnbInvoiceMc.getBillableIca()+" " + 
										    					dvnbInvoiceMc.getBillingCycleDate()+" " + dvnbInvoiceMc.getCharge()+" " + dvnbInvoiceMc.getCollectionMethod()+" " + dvnbInvoiceMc.getCreTms()+" " + 
										    					dvnbInvoiceMc.getCurrency()+" " + dvnbInvoiceMc.getDocType()+" " + dvnbInvoiceMc.getEventDesc()+" " + dvnbInvoiceMc.getEventId()+" " + 
										    					dvnbInvoiceMc.getInvoiceIca()+" " + dvnbInvoiceMc.getInvoiceNumber()+" " + dvnbInvoiceMc.getKetChuyen()+" " + 
										    					dvnbInvoiceMc.getKy()+" " + dvnbInvoiceMc.getNgayThucHien()+" " + dvnbInvoiceMc.getOriginalInvoiceNumber()+" " + dvnbInvoiceMc.getPeriodEndTime()+" " +
										    					dvnbInvoiceMc.getPeriodStartTime()+" " + dvnbInvoiceMc.getQuantityAmount()+" " + dvnbInvoiceMc.getRate()+" " + 
										    					dvnbInvoiceMc.getSbfExplanatoryText()+" " + dvnbInvoiceMc.getServiceCode()+" " + dvnbInvoiceMc.getServiceCodeDesc()+" " + 
										    					dvnbInvoiceMc.getTaxCharge()+" " + dvnbInvoiceMc.getTotalCharge()+" " + dvnbInvoiceMc.getUom()+" " + dvnbInvoiceMc.getUpdTms()+" " + 
										    					dvnbInvoiceMc.getUpdUid()+" " + dvnbInvoiceMc.getUsrId()+" " + dvnbInvoiceMc.getVatCharge()+" " + dvnbInvoiceMc.getVatCode()+" " +
										    					dvnbInvoiceMc.getVatCurrency()+" " + dvnbInvoiceMc.getVatRate()+" " + dvnbInvoiceMc.getId());
										    			dvnbInvoiceMcService.create(dvnbInvoiceMc);
										    		}
												} 
									    		
									    	}
									    	catch (Exception e) {
												// TODO: handle exception
									    		e.printStackTrace();
											}
									    	
									    	workbook.close();
								            is.close();
											
										} catch (Exception e) {
											// TODO: handle exception
											LOGGER.error(e.toString());
											System.out.println(e.toString());
										}
										grid.dataSourceMC = getDataMC(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
										break;
									case "VS":
										try 
										{
											dvnbInvoiceVsList = new ArrayList<DvnbInvoiceVs>();
											InputStream is = null;
											try {
												is = new FileInputStream(fileImport);
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
									    	LOGGER.info("Reading file " + fileImport.getName());
									    	
									    	Workbook workbook = StreamingReader.builder()
									        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
									        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
									        .open(is);  
											
									    	Sheet sheet = workbook.getSheetAt(0);
									    	
									    	LOGGER.info("Reading row in " + fileImport.getName());
									    	i=0;
									    	for (Row row : sheet) {
									    		if(row.getRowNum()>0) {
									    			i++;
									    			DvnbInvoiceVs dvnbInvoiceVs = new DvnbInvoiceVs();
									    			String currentTime = timeConverter.getCurrentTime();
									    			dvnbInvoiceVs.setId(currentTime + String.format("%07d", i));
									    			dvnbInvoiceVs.setCreTms(new BigDecimal(currentTime));
									    			dvnbInvoiceVs.setUsrId(sUserId);
									    			dvnbInvoiceVs.setBillingPeriod(getCellStringValue(row.getCell(0)));
									    			String invoiceDate = row.getCell(1).getStringCellValue();
//									    			DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
//									    			                .parseCaseInsensitive()
//									    			                .appendPattern("dd-MMM-yyyy")
//									    			                .toFormatter(Locale.UK);
//									    			LocalDate invoiceDateFormatter = LocalDate.parse(invoiceDate, dateFormatter);
									    			
									    			Locale.setDefault(Locale.US);
									    			Date invoiceDateConvers = DateUtils.parseDate(invoiceDate, new String[] {"dd-MMM-yyyy"});
									    			
//									    			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
//									    			Date invoiceDateConvers = sdf.parse(invoiceDate);
//									    			sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.ENGLISH);
									    			
									    			dvnbInvoiceVs.setInvoiceDate(DateFormatUtils.format(invoiceDateConvers, "yyyy/MM/dd"));
									    			dvnbInvoiceVs.setInvoiceAccount(getCellStringValue(row.getCell(2)));
									    			dvnbInvoiceVs.setName(getCellStringValue(row.getCell(3)));
									    			dvnbInvoiceVs.setInvoiceId(getCellStringValue(row.getCell(4)));
									    			dvnbInvoiceVs.setSubInvoice(getCellStringValue(row.getCell(5)));	
									    			dvnbInvoiceVs.setCurrentOrPrevious(getCellStringValue(row.getCell(6)));	
									    			dvnbInvoiceVs.setEntityType(getCellStringValue(row.getCell(7)));
									    			dvnbInvoiceVs.setEntityId(getCellStringValue(row.getCell(8)));
									    			dvnbInvoiceVs.setBinMap(getCellStringValue(row.getCell(9)));
									    			dvnbInvoiceVs.setEntityName(getCellStringValue(row.getCell(10)));	
									    			dvnbInvoiceVs.setSettlementId(getCellStringValue(row.getCell(11)));
									    			dvnbInvoiceVs.setDescription(getCellStringValue(row.getCell(12)));	
									    			dvnbInvoiceVs.setFutureUse(getCellStringValue(row.getCell(13)));
									    			dvnbInvoiceVs.setNtwk(getCellStringValue(row.getCell(14)));
									    			dvnbInvoiceVs.setBillingLine(getCellStringValue(row.getCell(15)));	
									    			dvnbInvoiceVs.setType(getCellStringValue(row.getCell(16)));
									    			dvnbInvoiceVs.setRateType(getCellStringValue(row.getCell(17)));	
//									    			dvnbInvoiceVs.setUnits(String.valueOf(row.getCell(18).getNumericCellValue()));	
									    			dvnbInvoiceVs.setUnits(getCellNumberValue(row.getCell(18)));
									    			dvnbInvoiceVs.setRateCur(getCellStringValue(row.getCell(19)));
//									    			dvnbInvoiceVs.setRate(row.getCell(20).getStringCellValue());
									    			dvnbInvoiceVs.setRate(getCellNumberValue(row.getCell(20)));
//									    			dvnbInvoiceVs.setForeignExchangeRate(row.getCell(21).getStringCellValue());
									    			dvnbInvoiceVs.setForeignExchangeRate(getCellNumberValue(row.getCell(21)));
									    			dvnbInvoiceVs.setBillingCurrency(getCellStringValue(row.getCell(22)));	
									    			dvnbInvoiceVs.setDeviation("0");
//									    			dvnbInvoiceVs.setTotal(String.valueOf(row.getCell(23).getNumericCellValue())); 	
									    			dvnbInvoiceVs.setTotal(getCellNumberValue(row.getCell(23)));
									    			dvnbInvoiceVs.setTaxType(getCellStringValue(row.getCell(24))); 	
//									    			dvnbInvoiceVs.setTax(row.getCell(25).getStringCellValue()); 	
									    			dvnbInvoiceVs.setTax(getCellNumberValue(row.getCell(25))); 	
//									    			dvnbInvoiceVs.setTaxRate(row.getCell(26).getStringCellValue());
									    			dvnbInvoiceVs.setTaxRate(getCellNumberValue(row.getCell(26))); 	
									    			dvnbInvoiceVs.setTaxCurrency(getCellStringValue(row.getCell(27)));	
//									    			dvnbInvoiceVs.setTaxableAmount(row.getCell(28).getStringCellValue());
									    			dvnbInvoiceVs.setTaxableAmount(getCellNumberValue(row.getCell(28))); 	
//									    			dvnbInvoiceVs.setTaxTaxCurrency(row.getCell(29).getStringCellValue());	
									    			dvnbInvoiceVs.setTaxTaxCurrency(getCellNumberValue(row.getCell(29))); 	
									    			dvnbInvoiceVs.setKy(cbbKy.getValue().toString());
									    			dvnbInvoiceVs.setKetChuyen("Y");
									    			dvnbInvoiceVs.setNgayThucHien(new BigDecimal(timeConverter.getCurrentTime().substring(0, 8)));
									    			
//										    			dvnbInvoiceVsList.add(dvnbInvoiceVs);
									    			dvnbInvoiceVsService.create(dvnbInvoiceVs);
									    		}
											}
									    	workbook.close();
								            is.close();
										}
										catch (Exception e) {
											// TODO: handle exception
											LOGGER.error(e.toString());
											e.printStackTrace();
										}
										grid.dataSourceVS = getDataVS(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
										break;
								}
								grid.initGrid(cbbCardBrn.getValue().toString(),cbbKy.getValue().toString(), "All");
//								switch(cardBrn) {
//									case "MC":
//										if(resultMC != null)
//										{
//											grid.dataSourceMC = getDataMC(new PageRequest(resultMC.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
//											grid.refreshData();
//										}
//										break;
//									case "VS":
//										if(resultVS != null)
//										{
//											grid.dataSourceVS = getDataVS(new PageRequest(resultVS.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
//											grid.refreshData();
//										}
//										break;
//								}
								grid.refreshData();
								// Refresh paging button
								mainLayout.removeComponent(pagingLayout);
								pagingLayout = generatePagingLayout();
								pagingLayout.setSpacing(true);
								mainLayout.addComponent(pagingLayout);
								mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
								confirmDialog.close();
					        	
					        });
					        
							//-----------------
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
			            
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error(e.toString());
				}
				return outputFile;
				
			}
			
			
		});
		chooseFile.setButtonCaption("IMPORT");
		chooseFile.addStyleName("myCustomUpload");
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(100.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		btView.addClickListener(event -> {
			
			cardBrn = cbbCardBrn.getValue().toString();
//        	SHOW DATA IN GRID
			grid.dataSourceMC = null;
			grid.dataSourceVS = null;
			
			switch(cardBrn) {
				case "MC":
					grid.dataSourceMC = getDataMC(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					break;
					
				case "VS":
					grid.dataSourceVS = getDataVS(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					break;
			}
			grid.initGrid(cbbCardBrn.getValue().toString(),cbbKy.getValue().toString(), "All");
			switch(cardBrn) {
				case "MC":
					if(resultMC != null)
					{
						grid.dataSourceMC = getDataMC(new PageRequest(resultMC.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
						grid.refreshData();
					}
					break;
				case "VS":
					if(resultVS != null)
					{
						grid.dataSourceVS = getDataVS(new PageRequest(resultVS.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
						grid.refreshData();
					}
					break;
			}
			
			grid.grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

				@Override
				public void preCommit(CommitEvent commitEvent) throws CommitException {
					// TODO Auto-generated method stub
//					Notification.show("Cập nhật lệch số.");
					return;
				}

				@Override
				public void postCommit(CommitEvent commitEvent) throws CommitException {
					
					try {
						String id = grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "id").toString();
						String deviation = grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "deviation").toString();
						
						switch(cbbCardBrn.getValue().toString()) {
							case "MC":
								dvnbInvoiceMcService.updateDeviationById(deviation, id);
								Notification.show("Lệch số đã được cập nhật.");
								break;
							case "VS":
								dvnbInvoiceVsService.updateDeviationById(deviation, id);
								Notification.show("Lệch số đã được cập nhật.");
								break;
						}
						
						
					} catch (Exception e) {
						Notification.show("Lỗi ứng dụng: "+ e.getMessage(), Type.ERROR_MESSAGE);
					}
				}
			});
			
			lbTotalKhongKetChuyen.setValue("Tổng cộng không phân bổ: " + tongCongKhongPhanBo);
			// Refresh paging button
			mainLayout.removeComponent(pagingLayout);
			pagingLayout = generatePagingLayout();
			pagingLayout.setSpacing(true);
			mainLayout.addComponent(pagingLayout);
			mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		
			if(grid.lbNoDataFound.isVisible()) {
				pagingLayout.setVisible(false);
			}
			else
			{
				pagingLayout.setVisible(true);
			}
			
		});
		
		btDelete.setStyleName(ValoTheme.BUTTON_DANGER);
		btDelete.setWidth(100.0f, Unit.PIXELS);
		btDelete.setIcon(FontAwesome.REMOVE);
		btDelete.addClickListener(event -> {
			String crdbrn = cbbCardBrn.getValue()==null ? "" : cbbCardBrn.getValue().toString();
			String ky = cbbKy.getValue()==null ? "" : cbbKy.getValue().toString();
			switch(crdbrn) {
				case "MC":
					dvnbInvoiceMcService.deleteByKyHoaDon(ky);
					break;
				case "VS":
					dvnbInvoiceVsService.deleteByKyHoaDon(ky);
					break;
			}
			
			grid.dataSourceVS = getDataVS(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid(cbbCardBrn.getValue().toString(),cbbKy.getValue().toString(), "All");
			grid.refreshData();
			
		});
		
//		final Button btImportToDb = new Button(IMPORT);
//		btImportToDb.setStyleName(ValoTheme.BUTTON_PRIMARY);
//	
//		btImportToDb.addClickListener(event -> {
//			i = 0;
//			grid.grid.getContainerDataSource().getItemIds().forEach(item -> {
//				i++;
//				if(cbbCardBrn.getValue().equals("MC")) {
//					DvnbInvoiceMcTemp dvnbInvoiceMc = new DvnbInvoiceMcTemp();
//					dvnbInvoiceMc.setId(timeConverter.getCurrentTime() + String.format("%07d", i));
//					dvnbInvoiceMc.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
//					dvnbInvoiceMc.setUsrId(sUserId);
//	    			dvnbInvoiceMc.setDocType(grid.grid.getContainerDataSource().getContainerProperty(item, "docType").getValue().toString());
//	    			dvnbInvoiceMc.setInvoiceNumber(grid.grid.getContainerDataSource().getContainerProperty(item, "invoiceNumber").getValue().toString());
//	    			dvnbInvoiceMc.setCurrency(grid.grid.getContainerDataSource().getContainerProperty(item, "currency").getValue().toString());
//	    			dvnbInvoiceMc.setBillingCycleDate(grid.grid.getContainerDataSource().getContainerProperty(item, "billingCycleDate").getValue().toString());
//	    			dvnbInvoiceMc.setInvoiceIca(grid.grid.getContainerDataSource().getContainerProperty(item, "invoiceIca").getValue().toString());
//	    			dvnbInvoiceMc.setActivityIca(grid.grid.getContainerDataSource().getContainerProperty(item, "activityIca").getValue().toString());
//	    			dvnbInvoiceMc.setBillableIca(grid.grid.getContainerDataSource().getContainerProperty(item, "billableIca").getValue().toString());
//	    			dvnbInvoiceMc.setCollectionMethod(grid.grid.getContainerDataSource().getContainerProperty(item, "collectionMethod").getValue().toString());
//	    			dvnbInvoiceMc.setServiceCode(grid.grid.getContainerDataSource().getContainerProperty(item, "serviceCode").getValue().toString());
//	    			dvnbInvoiceMc.setServiceCodeDesc(grid.grid.getContainerDataSource().getContainerProperty(item, "serviceCodeDesc").getValue().toString());
//	    			dvnbInvoiceMc.setPeriodStartTime(grid.grid.getContainerDataSource().getContainerProperty(item, "periodStartTime").getValue().toString());
//	    			dvnbInvoiceMc.setPeriodEndTime(grid.grid.getContainerDataSource().getContainerProperty(item, "periodEndTime").getValue().toString());
//	    			dvnbInvoiceMc.setOriginalInvoiceNumber(grid.grid.getContainerDataSource().getContainerProperty(item, "originalInvoiceNumber").getValue().toString());
//	    			dvnbInvoiceMc.setEventId(grid.grid.getContainerDataSource().getContainerProperty(item, "eventId").getValue().toString());
//	    			dvnbInvoiceMc.setEventDesc(grid.grid.getContainerDataSource().getContainerProperty(item, "eventDesc").getValue().toString());
//	    			dvnbInvoiceMc.setAffiliate(grid.grid.getContainerDataSource().getContainerProperty(item, "affiliate").getValue().toString());
//	    			dvnbInvoiceMc.setUom(grid.grid.getContainerDataSource().getContainerProperty(item, "uom").getValue().toString());
//	    			dvnbInvoiceMc.setQuantityAmount(grid.grid.getContainerDataSource().getContainerProperty(item, "quantityAmount").getValue().toString());
//	    			dvnbInvoiceMc.setRate(grid.grid.getContainerDataSource().getContainerProperty(item, "rate").getValue().toString());
//	    			dvnbInvoiceMc.setCharge(grid.grid.getContainerDataSource().getContainerProperty(item, "charge").getValue().toString());
//	    			dvnbInvoiceMc.setTaxCharge(grid.grid.getContainerDataSource().getContainerProperty(item, "taxCharge").getValue().toString());
//	    			dvnbInvoiceMc.setTotalCharge(grid.grid.getContainerDataSource().getContainerProperty(item, "totalCharge").getValue().toString());
//	    			dvnbInvoiceMc.setVatCharge(grid.grid.getContainerDataSource().getContainerProperty(item, "vatCharge").getValue().toString());
//	    			dvnbInvoiceMc.setVatCurrency(grid.grid.getContainerDataSource().getContainerProperty(item, "vatCurrency").getValue().toString());
//	    			dvnbInvoiceMc.setVatCode(grid.grid.getContainerDataSource().getContainerProperty(item, "vatCode").getValue().toString());
//	    			dvnbInvoiceMc.setVatRate(grid.grid.getContainerDataSource().getContainerProperty(item, "vatRate").getValue().toString());
//	    			dvnbInvoiceMc.setSbfExplanatoryText(grid.grid.getContainerDataSource().getContainerProperty(item, "sbfExplanatoryText").getValue().toString());
//	    			dvnbInvoiceMc.setKetChuyen(grid.grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").getValue().toString());
//	    			dvnbInvoiceMc.setNgayThucHien(new BigDecimal(grid.grid.getContainerDataSource().getContainerProperty(item, "ngayThucHien").getValue().toString()));
//	    			dvnbInvoiceMcService.create(dvnbInvoiceMc);
//				}
//				if(cbbCardBrn.getValue().equals("VS")) {
//					DvnbInvoiceVsTemp dvnbInvoiceVs = new DvnbInvoiceVsTemp();
//					dvnbInvoiceVs.setId(timeConverter.getCurrentTime() + String.format("%07d", i));
//					dvnbInvoiceVs.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
//					dvnbInvoiceVs.setUsrId(sUserId);
//					dvnbInvoiceVs.setBillingPeriod(grid.grid.getContainerDataSource().getContainerProperty(item, "billingPeriod").getValue().toString());
//	    			dvnbInvoiceVs.setInvoiceDate(grid.grid.getContainerDataSource().getContainerProperty(item, "invoiceDate").getValue().toString());
//	    			dvnbInvoiceVs.setInvoiceAccount(grid.grid.getContainerDataSource().getContainerProperty(item, "invoiceAccount").getValue().toString());
//	    			dvnbInvoiceVs.setName(grid.grid.getContainerDataSource().getContainerProperty(item, "name").getValue().toString());
//	    			dvnbInvoiceVs.setInvoiceId(grid.grid.getContainerDataSource().getContainerProperty(item, "invoiceId").getValue().toString());
//	    			dvnbInvoiceVs.setSubInvoice(grid.grid.getContainerDataSource().getContainerProperty(item, "subInvoice").getValue().toString());	
//	    			dvnbInvoiceVs.setCurrentOrPrevious(grid.grid.getContainerDataSource().getContainerProperty(item, "currentOrPrevious").getValue().toString());	
//	    			dvnbInvoiceVs.setEntityType(grid.grid.getContainerDataSource().getContainerProperty(item, "entityType").getValue().toString());
//	    			dvnbInvoiceVs.setEntityId(grid.grid.getContainerDataSource().getContainerProperty(item, "entityId").getValue().toString());
//	    			dvnbInvoiceVs.setBinMap(grid.grid.getContainerDataSource().getContainerProperty(item, "binMap").getValue().toString());
//	    			dvnbInvoiceVs.setEntityName(grid.grid.getContainerDataSource().getContainerProperty(item, "entityName").getValue().toString());	
//	    			dvnbInvoiceVs.setSettlementId(grid.grid.getContainerDataSource().getContainerProperty(item, "settlementId").getValue().toString());
//	    			dvnbInvoiceVs.setDescription(grid.grid.getContainerDataSource().getContainerProperty(item, "description").getValue().toString());	
//	    			dvnbInvoiceVs.setFutureUse(grid.grid.getContainerDataSource().getContainerProperty(item, "futureUse").getValue().toString());
//	    			dvnbInvoiceVs.setNtwk(grid.grid.getContainerDataSource().getContainerProperty(item, "ntwk").getValue().toString());
//	    			dvnbInvoiceVs.setBillingLine(grid.grid.getContainerDataSource().getContainerProperty(item, "billingLine").getValue().toString());	
//	    			dvnbInvoiceVs.setType(grid.grid.getContainerDataSource().getContainerProperty(item, "type").getValue().toString());
//	    			dvnbInvoiceVs.setRateType(grid.grid.getContainerDataSource().getContainerProperty(item, "rateType").getValue().toString());	
//	    			dvnbInvoiceVs.setUnits(grid.grid.getContainerDataSource().getContainerProperty(item, "units").getValue().toString());	
//	    			dvnbInvoiceVs.setRateCur(grid.grid.getContainerDataSource().getContainerProperty(item, "rateCur").getValue().toString());
//	    			dvnbInvoiceVs.setRate(grid.grid.getContainerDataSource().getContainerProperty(item, "rate").getValue().toString());
//	    			dvnbInvoiceVs.setForeignExchangeRate(grid.grid.getContainerDataSource().getContainerProperty(item, "foreignExchangeRate").getValue().toString());
//	    			dvnbInvoiceVs.setBillingCurrency(grid.grid.getContainerDataSource().getContainerProperty(item, "billingCurrency").getValue().toString());	
//	    			dvnbInvoiceVs.setTotal(grid.grid.getContainerDataSource().getContainerProperty(item, "total").getValue().toString()); 	
//	    			dvnbInvoiceVs.setTaxType(grid.grid.getContainerDataSource().getContainerProperty(item, "taxType").getValue().toString()); 	
//	    			dvnbInvoiceVs.setTax(grid.grid.getContainerDataSource().getContainerProperty(item, "tax").getValue().toString()); 	
//	    			dvnbInvoiceVs.setTaxRate(grid.grid.getContainerDataSource().getContainerProperty(item, "taxRate").getValue().toString());
//	    			dvnbInvoiceVs.setTaxCurrency(grid.grid.getContainerDataSource().getContainerProperty(item, "taxCurrency").getValue().toString());	
//	    			dvnbInvoiceVs.setTaxableAmount(grid.grid.getContainerDataSource().getContainerProperty(item, "taxableAmount").getValue().toString());
//	    			dvnbInvoiceVs.setTaxTaxCurrency(grid.grid.getContainerDataSource().getContainerProperty(item, "taxTaxCurrency").getValue().toString());	
//	    			dvnbInvoiceVs.setKetChuyen(grid.grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").getValue().toString());
//	    			dvnbInvoiceVs.setNgayThucHien(new BigDecimal(grid.grid.getContainerDataSource().getContainerProperty(item, "ngayThucHien").getValue().toString()));
//	    			dvnbInvoiceVsService.create(dvnbInvoiceVs);
//				}
//			});
//			
//			
//			final FormLayout content = new FormLayout();
//            content.setMargin(true);
//            
//			confirmDialog.setCaption("Import thành công " + grid.grid.getContainerDataSource().size() + " dòng");
//			confirmDialog.setWidth(250.0f, Unit.PIXELS);
//	        
//			//-----------------
//    		VerticalLayout layoutBtn = new VerticalLayout();
//            layoutBtn.addComponents(bOK);
//            layoutBtn.setComponentAlignment(bOK, Alignment.BOTTOM_CENTER);
//            content.addComponent(layoutBtn);
//            
//            confirmDialog.setContent(content);
//
//            getUI().addWindow(confirmDialog);
//            
//            // Center it in the browser window
//            confirmDialog.center();
//            confirmDialog.setResizable(false);
//			
//			
////			if(cbbCardBrn.getValue().equals("MC")) {
////				for(dvnbInvoiceMc dvnbInvoiceMc : dvnbInvoiceMcList) {
////					i++;
////					dvnbInvoiceMc.setId(timeConverter.getCurrentTime() + String.valueOf(i));
////					dvnbInvoiceMc.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
////					dvnbInvoiceMc.setUsrId(sUserId);
////					dvnbInvoiceMcService.create(dvnbInvoiceMc);
////				}
////			}
//			
////			if(cbbCardBrn.getValue().equals("VS")) {
////				for(dvnbInvoiceVs dvnbInvoiceVs : dvnbInvoiceVsList) {
////					i++;
////					dvnbInvoiceVs.setId(timeConverter.getCurrentTime() + String.valueOf(i));
////					dvnbInvoiceVs.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
////					dvnbInvoiceVs.setUsrId(sUserId);
////					dvnbInvoiceVsService.create(dvnbInvoiceVs);
////				}
////			}
//		});
		
		bOK.addClickListener(event -> {
        	confirmDialog.close();
        });
		
		
		btUpdateKhongketchuyen.setStyleName(ValoTheme.BUTTON_SMALL);
		btUpdateKhongketchuyen.setVisible(false);
		btUpdateKhongketchuyen.addClickListener(event -> {
			String crdbrn = cbbCardBrn.getValue().toString();
			String ky = cbbKy.getValue().toString();
			String eventID = tfEventID.getValue();
			String invoiceNumber = tfInvoiceNumber.getValue().isEmpty() ? "All" : tfInvoiceNumber.getValue();
			
			switch(crdbrn) {
				case "MC":
					dvnbInvoiceMcService.updateKhongphanboByEventIdAndInvoiceNumber(ky, eventID, invoiceNumber);
					lbTotalKhongKetChuyen.setValue("Tổng cộng không phân bổ: " + dvnbInvoiceMcService.countInvoiceByKyAndKetChuyenStatus(ky, "N"));
					grid.dataSourceMC = getDataMC(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.initGrid(cbbCardBrn.getValue().toString(),cbbKy.getValue().toString(), "All");
					grid.refreshData();
					
					break;
				case "VS":
					dvnbInvoiceVsService.updateKhongphanboByBillingLineAndInvoiceId(ky, eventID, invoiceNumber);
					lbTotalKhongKetChuyen.setValue("Tổng cộng không phân bổ: " + dvnbInvoiceVsService.countInvoiceByKyAndKetChuyenStatus(ky, "N"));
					grid.dataSourceVS = getDataVS(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.initGrid(cbbCardBrn.getValue().toString(),cbbKy.getValue().toString(), "All");
					grid.refreshData();
					break;
			}
			
			// Refresh paging button
			mainLayout.removeComponent(pagingLayout);
			pagingLayout = generatePagingLayout();
			pagingLayout.setSpacing(true);
			mainLayout.addComponent(pagingLayout);
			mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		});
		
		Button btKhongKetChuyenShow = new Button("[+] Xử lý không kết chuyển");
		btKhongKetChuyenShow.setStyleName(ValoTheme.BUTTON_LINK);
		btKhongKetChuyenShow.addClickListener(event -> {
			if(event.getButton().getCaption().contains("[+]")){
				lbEventID.setVisible(true);
				tfEventID.setVisible(true);
				lbInvoiceNumber.setVisible(true);
				tfInvoiceNumber.setVisible(true);
				btUpdateKhongketchuyen.setVisible(true);
				lbTotalKhongKetChuyen.setVisible(true);
				btKhongKetChuyenShow.setCaption("[-] Xử lý không kết chuyển"); 
			} else {
				lbEventID.setVisible(false);
				tfEventID.setVisible(false);
				lbInvoiceNumber.setVisible(false);
				tfInvoiceNumber.setVisible(false);
				btUpdateKhongketchuyen.setVisible(false);
				lbTotalKhongKetChuyen.setVisible(false);
				btKhongKetChuyenShow.setCaption("[+] Xử lý không kết chuyển"); 
			}
		});
		
		formLayout.addComponent(lbKy);
		formLayout.addComponent(cbbKy);
		formLayout.addComponent(lbCardBrn);
		formLayout.addComponent(cbbCardBrn);
		formLayout.addComponent(lbTyGia);
		formLayout.addComponent(tfTyGia);
		formLayout2nd.addComponent(chooseFile);
		formLayout2nd.addComponent(btView);
		formLayout2nd.addComponent(btDelete);
		formLayout2nd.addComponent(btKhongKetChuyenShow);
		formLayout3rd.addComponent(lbEventID);
		formLayout3rd.addComponent(tfEventID);
		formLayout3rd.addComponent(lbInvoiceNumber);
		formLayout3rd.addComponent(tfInvoiceNumber);
		formLayout3rd.addComponent(btUpdateKhongketchuyen);
		formLayout3rd.addComponent(lbTotalKhongKetChuyen);
		
		formLayout.setComponentAlignment(lbKy, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(cbbKy, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(lbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(cbbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(lbTyGia, Alignment.MIDDLE_RIGHT);
		formLayout.setComponentAlignment(tfTyGia, Alignment.MIDDLE_RIGHT);
		formLayout2nd.setComponentAlignment(chooseFile, Alignment.TOP_CENTER);
		formLayout2nd.setComponentAlignment(btView, Alignment.MIDDLE_RIGHT);
		formLayout3rd.setComponentAlignment(lbEventID, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(tfEventID, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(lbInvoiceNumber, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(tfInvoiceNumber, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(btUpdateKhongketchuyen, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(lbTotalKhongKetChuyen, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		form.addComponent(formLayout2nd);
		form.addComponent(formLayout3rd);
		
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		grid = new DataGridInvoiceComponent();
		mainLayout.addComponent(grid);
		
//		pagingLayout = generatePagingLayout();
//		pagingLayout.setSpacing(true);
//		
//		mainLayout.addComponent(pagingLayout);
//		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);

		
//		mainLayout.addComponent(btPaging);
//		mainLayout.setComponentAlignment(btPaging, Alignment.BOTTOM_RIGHT);
		
		setCompositionRoot(mainLayout);
	}
	
	private Page<DvnbInvoiceMc> getDataMC(Pageable page) {
	    resultMC = dvnbInvoiceMcService.findAllByKy(cbbKy.getValue().toString(), page);
		return resultMC;
	}
	
	private Page<DvnbInvoiceVs> getDataVS(Pageable page) {
		resultVS = dvnbInvoiceVsService.findAllByKy(cbbKy.getValue().toString(), page);
		return resultVS;
		
	}

	private HorizontalLayout generatePagingLayout() {
		final Button btPaging = new Button();
		btPaging.setCaption(reloadLabelPaging());
		btPaging.setEnabled(false);

		final Button btPreviousPage = new Button("Trang trước");
		btPreviousPage.setIcon(FontAwesome.ANGLE_LEFT);
		btPreviousPage.setEnabled(true);

		final Button btNextPage = new Button("Trang sau");
		btNextPage.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btNextPage.setIcon(FontAwesome.ANGLE_RIGHT);
		btNextPage.setEnabled(true);
		
		final HorizontalLayout pagingLayout = new HorizontalLayout();
		pagingLayout.setSizeUndefined();
		pagingLayout.setSpacing(true);
		pagingLayout.addComponent(btPaging);
		pagingLayout.addComponent(btPreviousPage);
		pagingLayout.addComponent(btNextPage);
		pagingLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
		
		switch(cardBrn) {
			case "MC":
				btPreviousPage.setEnabled(resultMC.hasPrevious());
				btNextPage.setEnabled(resultMC.hasNext());
				break;
			case "VS":
				btPreviousPage.setEnabled(resultVS.hasPrevious());
				btNextPage.setEnabled(resultVS.hasNext());
				break;
		}

		btNextPage.addClickListener(evt -> {
			switch(cardBrn) {
				case "MC":
					
					grid.dataSourceMC = getDataMC(resultMC.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultMC.hasNext());
					btPreviousPage.setEnabled(resultMC.hasPrevious());
					break;
				case "VS":
					
					grid.dataSourceVS = getDataVS(resultVS.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultVS.hasNext());
					btPreviousPage.setEnabled(resultVS.hasPrevious());
					break;
			}
			
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
				}
			});

		});

		btPreviousPage.addClickListener(evt -> {
			switch(cardBrn) {
				case "MC":
					grid.dataSourceMC = getDataMC(resultMC.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultMC.hasNext());
					btPreviousPage.setEnabled(resultMC.hasPrevious());
					break;
				case "VS":
					grid.dataSourceVS = getDataVS(resultVS.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultVS.hasNext());
					btPreviousPage.setEnabled(resultVS.hasPrevious());
					break;
			}
					
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
				}
			});
		});
		
 		return pagingLayout;
	}

	private String reloadLabelPaging() {
		final StringBuilder sNumberOfElements = new StringBuilder();
		String sTotalElements = null;
		String sLabelPaging = "";
		switch(cardBrn) {
			case "MC":
				if(resultMC != null) {
					if (resultMC.getSize() * (resultMC.getNumber() + 1) >= resultMC.getTotalElements()) {
						sNumberOfElements.append(resultMC.getTotalElements());
					} else {
						sNumberOfElements.append(resultMC.getSize() * (resultMC.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultMC.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "VS":
				if(resultVS != null) {
					if (resultVS.getSize() * (resultVS.getNumber() + 1) >= resultVS.getTotalElements()) {
						sNumberOfElements.append(resultVS.getTotalElements());
					} else {
						sNumberOfElements.append(resultVS.getSize() * (resultVS.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultVS.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
		}
		return sLabelPaging;
	}
	
	@Override
	public void eventReload() {
		switch(cardBrn) {
			case "MC":
				if(resultMC != null)
				{
					grid.dataSourceMC = getDataMC(new PageRequest(resultMC.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.refreshData();
				}
				break;
			case "VS":
				if(resultVS != null)
				{
					grid.dataSourceVS = getDataVS(new PageRequest(resultVS.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.refreshData();
				}
				break;
		}
		
		// Refresh paging button
//		mainLayout.removeComponent(pagingLayout);
//		pagingLayout = generatePagingLayout();
//		pagingLayout.setSpacing(true);
//		mainLayout.addComponent(pagingLayout);
//		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
	}
	
	private String getTyGiaByKyAndCrdbrn(String ky,String crdbrn) {
		String tygia = "";
		DecimalFormat tyGiaFormat = new DecimalFormat("#,###.00000");
		
		final Iterable<DvnbTyGia> listTygiaIter = tyGiaService.findAllByKyAndCardbrn(ky,crdbrn);
		if(listTygiaIter.iterator().hasNext()) {
			BigDecimal tygiaNum = listTygiaIter.iterator().next().getTyGia();
			tygia = tyGiaFormat.format(tygiaNum);
		}
		return tygia;
	}

	private String getCellNumberValue(Cell cell) {
		String value = "0";
		try{
			if(cell==null) 
				value = "0";
			else
				switch(cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						value="0";
						break;
					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						value = String.valueOf(cell.getNumericCellValue());
						break;
				}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return value;
		
	}
	
	private String getCellStringValue(Cell cell) {
		String value = " ";
		try{
			if(cell==null) 
				value = " ";
			else
				switch(cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						value=" ";
						break;
					case Cell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						value = Double.toString(cell.getNumericCellValue());
						break;
				}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return value;
		
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
