package com.dvnb.components.dstqt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.DsqtHachToanCustom;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
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
public class TruyVanGDDaTQT extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TruyVanGDDaTQT.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "TRUY VẤN GD ĐÃ ĐƯỢC TQT";
	private static final String PAN = "PAN";
	private static final String APVCODE = "APV CODE";
	private static final String SEARCH = "SEARCH";
	private static final String EXPORT = "EXPORT";
	private static final String DVCNT = "DVCNT";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String CARD_TYPE = "LOẠI THẺ";
	
	private transient String sUserId;
	private final TextField tfPAN;
	private final TextField tfApvcode;
	private final TextField tfDvcnt;
	private DateField dffromDate;
	private DateField dftoDate;
	private ComboBox cbbCardType;
	final Button btSearch = new Button(SEARCH);
	final Button btExport = new Button(EXPORT);
	
	private DataGridDoiSoatTQTComponent grid;
	private transient Page<DoiSoatData> result;
	private final transient DoiSoatDataService doiSoatDataService;
	private List<DoiSoatData> listDoisoat = new ArrayList<DoiSoatData>();
	
	// Paging
	private final static int SIZE_OF_PAGE = 100;
	private final static int FIRST_OF_PAGE = 0;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	private int i;
	
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	public String filename;
	
	public TruyVanGDDaTQT() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		final HorizontalLayout formLayout3rd = new HorizontalLayout();
		formLayout3rd.setSpacing(true);
		formLayout3rd.setMargin(new MarginInfo(true, false, false, false));
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridDoiSoatTQTComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final Label lbfromDate = new Label("Từ ngày");
		lbfromDate.setWidth(57.0f, Unit.PIXELS);
		dffromDate = new DateField();
		dffromDate.setDateFormat("dd/MM/yyyy");
		dffromDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dffromDate.setValidationVisible(false);
		
		final Label lbtoDate = new Label("Đến ngày");
		lbtoDate.setWidth(60f, Unit.PIXELS);
		dftoDate = new DateField();
		dftoDate.setDateFormat("dd/MM/yyyy");
		dftoDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dftoDate.setValidationVisible(false);
		
		final Label lbCardType = new Label(CARD_TYPE);
		lbCardType.setWidth(57.0f, Unit.PIXELS);
		cbbCardType = new ComboBox();
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD","VS","VSD");
		cbbCardType.setValue("VSD");
		cbbCardType.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbCardType.setValidationVisible(false);
		
		
		final Label lbPAN = new Label(PAN);
		lbPAN.setWidth(60.0f, Unit.PIXELS);
		tfPAN = new TextField();
		tfPAN.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		
		final Label lbApvcode = new Label(APVCODE);
		lbApvcode.setWidth(60f, Unit.PIXELS);
		tfApvcode = new TextField();
		tfApvcode.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbDvcnt = new Label(DVCNT);
		tfDvcnt = new TextField();
		tfDvcnt.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);

		btSearch.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSearch.setWidth(120.0f, Unit.PIXELS);
		btSearch.setIcon(FontAwesome.SEARCH);
		btSearch.addClickListener(event -> {
			grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid(cbbCardType.getValue().toString(),"All");

			grid.refreshData();
			// Refresh paging button
		});
		
		btExport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExport.setWidth(120.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.DOWNLOAD);
		btExport.addClickListener(event -> {
			try {
				String tungay = timeConverter.convertDatetime(dffromDate.getValue());
				String denngay = timeConverter.convertDatetime(dftoDate.getValue());
				String cardno = tfPAN.isEmpty() ? "All" : tfPAN.getValue();
				String apvcode = tfApvcode.isEmpty() ? "All" : tfApvcode.getValue();
				String dvcnt = tfDvcnt.isEmpty() ? "All" : tfDvcnt.getValue();
				String cardType = cbbCardType.isEmpty() ? "All" : cbbCardType.getValue().toString();
				
	        	//EXPORT LIST TO EXCEL FILE
	            XSSFWorkbook workbookExport = new XSSFWorkbook();
	            XSSFSheet sheet = workbookExport.createSheet("Data");
		        
	            XSSFCellStyle cellStyle = workbookExport.createCellStyle();
	            XSSFDataFormat xssfDataFormat = workbookExport.createDataFormat();
	            cellStyle.setDataFormat(xssfDataFormat.getFormat("#,##0"));
	            
	            rowNumExport = 0;
		        LOGGER.info("Creating excel");
		        
		        if(rowNumExport == 0) {
		        	Object[] rowHeader = null;
			        
		        	if(cardType.matches("(MD|VSD)"))
		        		rowHeader = new Object[] {"STT", "TÊN CHỦ THẺ", "CASA", "SỐ THẺ", "MÃ GD", "NGÀY GD", "NGÀY FILE INCOMING", "ST GD", "ST TQT", 
	            			"ST QĐ VNĐ", "LT GD", "LT TQT", "INTERCHANGE", "MÃ CẤP PHÉP", "ĐVCNT", "REVERSAL IND", "ISSUER CHARGE", "MERCHANT CITY", 
	            			"ST TRÍCH NỢ KH", "STGD NGUYÊN TỆ", "LT NGUYÊN TỆ", "PHÍ ISA", "PHÍ RTM","STGD NGUYÊN TỆ CHÊNH LỆCH", "STGD CHÊNH LỆCH DO TỶ GIÁ", 
	            			"TỶ GIÁ TẠI THỜI ĐIỂM TRÍCH NỢ", "STGD HOÀN TRẢ/TRUY THU", "PHÍ ISA HOÀN TRẢ/TRUY THU","VAT PHÍ ISA HOÀN TRẢ/TRUY THU",
	            			"PHÍ RTM HOÀN TRẢ/TRUY THU","VAT PHÍ RTM HOÀN TRẢ/TRUY THU", "TỔNG PHÍ + VAT HOÀN TRẢ/TRUY THU","TỔNG HOÀN TRẢ/TRUY THU",
	            			"PHÍ XỬ LÝ GD", "ĐVPHT","TRACE","STATUS CW", "MCC","NGÀY ADV"};
		        	if(cardType.matches("(MC|VS)"))
		        		rowHeader = new Object[] {"STT", "TÊN CHỦ THẺ", "CASA", "SỐ THẺ", "MÃ GD", "NGÀY GD", "NGÀY FILE INCOMING", "ST GD", "ST TQT", 
		            			"ST QĐ VNĐ", "LT GD", "LT TQT", "INTERCHANGE", "MÃ CẤP PHÉP", "ĐVCNT", "REVERSAL IND", "ISSUER CHARGE", "MERCHANT CITY",
		            			"LOC", "STGD NGUYÊN TỆ", "LOẠI TIỀN", "STGD NGUYÊN TỆ CHÊNH LỆCH", "STGD GHI NỢ KH (VNĐ)", "PHÍ ISA","VAT PHÍ ISA","PHÍ RTM","VAT PHÍ RTM", "STGD CHÊNH LỆCH", "ĐVPHT", "MCC","NGÀY ADV" };
	            	
		        	int colNum = 0;	 
	            	XSSFRow row = sheet.createRow(rowNumExport++);         	
	            	for (Object field : rowHeader) {
	            		Cell cell = row.createCell(colNum++, CellType.STRING);
	            		cell.setCellValue((String)field);
	            	}      
	            	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
		        }
		        
		        for(DoiSoatData item : listDoisoat) {
					XSSFRow row = sheet.createRow(rowNumExport);
					row.createCell(0).setCellValue(rowNumExport++);
					
					if(cardType.matches("(MD|VSD)")) {
						
						row.createCell(1).setCellValue(item.getTenChuThe());
						row.createCell(2).setCellValue(item.getCasa());
						row.createCell(3).setCellValue(item.getSoThe());
						row.createCell(4).setCellValue(item.getMaGd());
						row.createCell(5).setCellValue(item.getNgayGd());
						row.createCell(6).setCellValue(item.getNgayFileIncoming());
						
						Cell cell7 = row.createCell(7,CellType.NUMERIC);
						cell7.setCellValue(item.getStGd().doubleValue());
						cell7.setCellStyle(cellStyle);
						
						Cell cell8 = row.createCell(8,CellType.NUMERIC);
						cell8.setCellValue(item.getStTqt().doubleValue());
						cell8.setCellStyle(cellStyle);
						
						Cell cell9 = row.createCell(9,CellType.NUMERIC);
						cell9.setCellValue(item.getStQdVnd().doubleValue());
						cell9.setCellStyle(cellStyle);
						
						row.createCell(10).setCellValue(item.getLtgd());
						row.createCell(11).setCellValue(item.getLttqt());
						
						Cell cell12 = row.createCell(12,CellType.NUMERIC);
						cell12.setCellValue(item.getInterchange().doubleValue());
						cell12.setCellStyle(cellStyle);
						
						row.createCell(13).setCellValue(item.getMaCapPhep());
						row.createCell(14).setCellValue(item.getDvcnt());
						row.createCell(15).setCellValue(item.getReversalInd());
						row.createCell(16).setCellValue(item.getIssuerCharge());
						row.createCell(17).setCellValue(item.getMerchantCity());
						
						Cell cell18 = row.createCell(18,CellType.NUMERIC);
						cell18.setCellValue(item.getStTrichNoKhGd().doubleValue());
						cell18.setCellStyle(cellStyle);
						
						Cell cell19 = row.createCell(19,CellType.NUMERIC);
						cell19.setCellValue(item.getStgdNguyenTeGd().doubleValue());
						cell19.setCellStyle(cellStyle);
						
						row.createCell(20).setCellValue(item.getLoaiTienNguyenTeGd());
						
						Cell cell21 = row.createCell(21,CellType.NUMERIC);
						cell21.setCellValue(item.getPhiIsaGd().doubleValue());
						cell21.setCellStyle(cellStyle);
						
						Cell cell22 = row.createCell(22,CellType.NUMERIC);
						cell22.setCellValue(item.getPhiRtmGd().doubleValue());
						cell22.setCellStyle(cellStyle);

						Cell cell23 = row.createCell(23,CellType.NUMERIC);
						cell23.setCellValue(item.getStgdNguyenTeChenhLech().doubleValue());
						cell23.setCellStyle(cellStyle);
						
						Cell cell24 = row.createCell(24,CellType.NUMERIC);
						cell24.setCellValue(item.getStgdChenhLechDoTyGia().doubleValue());
						cell24.setCellStyle(cellStyle);
						
						Cell cell25 = row.createCell(25,CellType.NUMERIC);
						cell25.setCellValue(item.getTyGiaTrichNo().doubleValue());
						cell25.setCellStyle(cellStyle);
						
						Cell cell26 = row.createCell(26,CellType.NUMERIC);
						cell26.setCellValue(item.getSoTienGdHoanTraTruyThu().doubleValue());
						cell26.setCellStyle(cellStyle);
						
						Cell cell27 = row.createCell(27,CellType.NUMERIC);
						cell27.setCellValue(item.getPhiIsaHoanTraTruyThu().doubleValue());
						cell27.setCellStyle(cellStyle);
						
						Cell cell28 = row.createCell(28,CellType.NUMERIC);
						cell28.setCellValue(item.getVatPhiIsaHoanTraTruyThu().doubleValue());
						cell28.setCellStyle(cellStyle);
						
						Cell cell29 = row.createCell(29,CellType.NUMERIC);
						cell29.setCellValue(item.getPhiRtmHoanTraTruyThu().doubleValue());
						cell29.setCellStyle(cellStyle);
						
						Cell cell30 = row.createCell(30,CellType.NUMERIC);
						cell30.setCellValue(item.getVatPhiRtmHoanTraTruyThu().doubleValue());
						cell30.setCellStyle(cellStyle);
						
						Cell cell31 = row.createCell(31,CellType.NUMERIC);
						cell31.setCellValue(item.getTongPhiVatHoanTraTruyThu().doubleValue());
						cell31.setCellStyle(cellStyle);
						
						Cell cell32 = row.createCell(32,CellType.NUMERIC);
						cell32.setCellValue(item.getTongHoanTraTruyThu().doubleValue());
						cell32.setCellStyle(cellStyle);
						
						Cell cell33 = row.createCell(33,CellType.NUMERIC);
						cell33.setCellValue(item.getPhiXuLyGd().doubleValue());
						cell33.setCellStyle(cellStyle);
						
						row.createCell(34).setCellValue(item.getDvpht());
						row.createCell(35).setCellValue(item.getTrace());
						row.createCell(36).setCellValue(item.getStatusCw());
						row.createCell(37).setCellValue(item.getMcc());
						row.createCell(38).setCellValue(item.getNgayAdv());
					}
					if(cardType.matches("(MC|VS)")) {
						row.createCell(1).setCellValue(item.getTenChuThe());
						row.createCell(2).setCellValue(item.getCasa());
						row.createCell(3).setCellValue(item.getSoThe());
						row.createCell(4).setCellValue(item.getMaGd());
						row.createCell(5).setCellValue(item.getNgayGd());
						row.createCell(6).setCellValue(item.getNgayFileIncoming());
						
						Cell cell7 = row.createCell(7,CellType.NUMERIC);
						cell7.setCellValue(item.getStGd().doubleValue());
						cell7.setCellStyle(cellStyle);
						
						Cell cell8 = row.createCell(8,CellType.NUMERIC);
						cell8.setCellValue(item.getStTqt().doubleValue());
						cell8.setCellStyle(cellStyle);
						
						Cell cell9 = row.createCell(9,CellType.NUMERIC);
						cell9.setCellValue(item.getStQdVnd().doubleValue());
						cell9.setCellStyle(cellStyle);
						
						row.createCell(10).setCellValue(item.getLtgd());
						row.createCell(11).setCellValue(item.getLttqt());
						
						Cell cell12 = row.createCell(12,CellType.NUMERIC);
						cell12.setCellValue(item.getInterchange().doubleValue());
						cell12.setCellStyle(cellStyle);
						
						row.createCell(13).setCellValue(item.getMaCapPhep());
						row.createCell(14).setCellValue(item.getDvcnt());
						row.createCell(15).setCellValue(item.getReversalInd());
						row.createCell(16).setCellValue(item.getIssuerCharge());
						row.createCell(17).setCellValue(item.getMerchantCity());
						
						row.createCell(18).setCellValue(item.getLoc());
						
						Cell cell19 = row.createCell(19,CellType.NUMERIC);
						cell19.setCellValue(item.getStgdNguyenTeGd().doubleValue());
						cell19.setCellStyle(cellStyle);
						
						row.createCell(20).setCellValue(item.getLoaiTienNguyenTeGd());
						
						Cell cell21 = row.createCell(21,CellType.NUMERIC);
						cell21.setCellValue(item.getStgdNguyenTeChenhLech().doubleValue());
						cell21.setCellStyle(cellStyle);
						
						Cell cell22 = row.createCell(22,CellType.NUMERIC);
						cell22.setCellValue(item.getStTrichNoKhGd().doubleValue());
						cell22.setCellStyle(cellStyle);
						
						Cell cell23 = row.createCell(23,CellType.NUMERIC);
						cell23.setCellValue(item.getPhiIsaGd().doubleValue());
						cell23.setCellStyle(cellStyle);
						
						Cell cell24 = row.createCell(24,CellType.NUMERIC);
						cell24.setCellValue(item.getVatPhiIsaGd().doubleValue());
						cell24.setCellStyle(cellStyle);
						
						Cell cell25 = row.createCell(25,CellType.NUMERIC);
						cell25.setCellValue(item.getPhiRtmGd().doubleValue());
						cell25.setCellStyle(cellStyle);
						
						Cell cell26 = row.createCell(26,CellType.NUMERIC);
						cell26.setCellValue(item.getVatPhiRtmGd().doubleValue());
						cell26.setCellStyle(cellStyle);
						
						Cell cell27 = row.createCell(27,CellType.NUMERIC);
						cell27.setCellValue(item.getStgdChenhLechDoTyGia().doubleValue());
						cell27.setCellStyle(cellStyle);
						
						row.createCell(28).setCellValue(item.getDvpht());
						row.createCell(29).setCellValue(item.getMcc());
						row.createCell(30).setCellValue(item.getNgayAdv());
					}
					
					
		        }
		        
		        sheet.createFreezePane(0, 1);
		        
		        for (int i=0; i<38; i++)  {
		        	sheet.autoSizeColumn(i);
		        }
		        	
		        
	        	fileNameOutput = "ENQ_TRANSACTION_" + cardType + "_"  + timeConverter.getCurrentTime() + ".xlsx";
	        	pathExport = Paths.get(configurationHelper.getPathFileRoot() + "\\Export");
	        	if(Files.notExists(pathExport)) {
	        		Files.createDirectories(pathExport);
	            }
	        	FileOutputStream outputStream = new FileOutputStream(pathExport + "\\" + fileNameOutput);
	            LOGGER.info("Created file excel output " + fileNameOutput);
	            workbookExport.write(outputStream);
	            LOGGER.info("Write data to " + fileNameOutput + " completed");
	            workbookExport.close();
	            outputStream.close();
	            LOGGER.info("Done");
		        LOGGER.info("Export excel file " + fileNameOutput);
		        messageExportXLSX("Info","Export completed.");
		        
	        } catch (FileNotFoundException e) {
	            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
	        } catch (IOException e) {
	            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
	        }
		});
		
		
		formLayout1st.addComponent(lbfromDate);
		formLayout1st.addComponent(dffromDate);
		formLayout1st.addComponent(lbtoDate);
		formLayout1st.addComponent(dftoDate);
		formLayout2nd.addComponent(lbCardType);
		formLayout2nd.addComponent(cbbCardType);
		formLayout2nd.addComponent(lbPAN);
		formLayout2nd.addComponent(tfPAN);
		formLayout2nd.addComponent(lbApvcode);
		formLayout2nd.addComponent(tfApvcode);
		formLayout2nd.addComponent(lbDvcnt);
		formLayout2nd.addComponent(tfDvcnt);
		formLayout3rd.addComponent(btSearch);
		formLayout3rd.addComponent(btExport);
		
		formLayout1st.setComponentAlignment(lbfromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dffromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbtoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dftoDate, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbPAN, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(tfPAN, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbApvcode, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(tfApvcode, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbDvcnt, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(tfDvcnt, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(btSearch, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(btExport, Alignment.MIDDLE_CENTER);
		
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.addComponent(formLayout3rd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		grid = new DataGridDoiSoatTQTComponent();
//		grid.initGrid("All");
		mainLayout.addComponent(grid);

		setCompositionRoot(mainLayout);
	}
	
	private Page<DoiSoatData> getData(Pageable page) {
		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
		String cardno = tfPAN.isEmpty() ? "All" : tfPAN.getValue();
		String apvcode = tfApvcode.isEmpty() ? "All" : tfApvcode.getValue();
		String dvcnt = tfDvcnt.isEmpty() ? "All" : tfDvcnt.getValue();
		String cardType = cbbCardType.isEmpty() ? "All" : cbbCardType.getValue().toString();
		listDoisoat = doiSoatDataService.findAllTuNgayDenNgayAndPanAndApvCodeAndDvcntAndCardtype(tungay, denngay, cardno, apvcode,dvcnt,cardType);
		result = new PageImpl<>(listDoisoat);
		return result;
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
        layoutBtn.setComponentAlignment(bOK, Alignment.BOTTOM_CENTER);
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
	
	@Override
	public void eventReload() {
	}
	

}
