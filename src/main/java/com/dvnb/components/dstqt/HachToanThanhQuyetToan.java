package com.dvnb.components.dstqt;

import java.io.ByteArrayInputStream;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DsqtHachToan;
import com.dvnb.entities.DsqtHachToanCustom;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DsqtHachToanService;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
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

public class HachToanThanhQuyetToan extends CustomComponent implements ReloadComponent  {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(HachToanThanhQuyetToan.class);
	public static final String CAPTION = "HẠCH TOÁN TQT";
	public static final String GDTTHH = "GDTTHH";
	public static final String GDRTM = "GDRTM";
	public static final String GDMSFF = "GDMSFF";
	public static final String GDNONFI = "GDNONFI";
	public static final String GDCBTTHH = "GDCBTTHH";
	public static final String GDCBRTM= "GDCBRTM";
	public static final String FEECOLL = "FEECOLL";
	private static final String CARD_TYPE = "LOẠI THẺ";
	private static final String LOAI_TIEN = "LOẠI TIỀN";
	private static final String EXPORT = "EXPORT";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String ERROR_MESSAGE = "Lỗi ứng dụng";
	private static final String REMOVE = "REMOVE";
	private DateField dfAdvDate;
	private final Label lbLatestLogin = new Label();
	private final Label lbTotalAssignedCases = new Label();
	private String sUserId = "";
	private String CheckUserId = "1";
	private String roleDescription = "";
	private String note = "";
	private String fileNameImport;
	final TimeConverter timeConverter = new TimeConverter();
	private SpringConfigurationValueHelper configurationHelper;
	private File fileImport = null;
	private final VerticalLayout mainLayout = new VerticalLayout();
	private final transient DsqtHachToanService dsqtHachToanService;
	private DsqtHachToan dsqtHachToan;
	private ComboBox cbbCardType;
	private ComboBox cbbLoaiTien;
	final Button btRemove = new Button(REMOVE);
	
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	public String filename;
	List<DsqtHachToanCustom> dsqtHachToanList;
	private boolean isChargeback = false;
	private final OptionGroup optgrpFileImport;
	
	public HachToanThanhQuyetToan() {
		setCaption(CAPTION);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		dsqtHachToanService = (DsqtHachToanService) helper.getBean("dsqtHachToanService");
		this.sUserId = SecurityUtils.getUserId();
		
		final HorizontalLayout horizontalLayout1st= new HorizontalLayout();
		horizontalLayout1st.setSpacing(true);
		
		final HorizontalLayout horizontalLayout2nd= new HorizontalLayout();
		horizontalLayout2nd.setSpacing(true);
		
		final SimpleDateFormat simpledateformat_current = new SimpleDateFormat("dd/M/yyyy");

		optgrpFileImport = new OptionGroup("DANH SÁCH FILE ĐÃ XỬ LÝ");
		optgrpFileImport.setSizeFull();
		optgrpFileImport.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		optgrpFileImport.setReadOnly(true);
		optgrpFileImport.setVisible(false);
		
		final Label lbAdvDate = new Label("NGÀY ADV");
		lbAdvDate.setWidth(105.0f, Unit.PIXELS);
		dfAdvDate = new DateField();
		dfAdvDate.setDateFormat("dd/MM/yyyy");
		dfAdvDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfAdvDate.setValidationVisible(false);
		dfAdvDate.addValueChangeListener(event -> {
			optgrpFileImport.setVisible(true);
			String ngayAdv = timeConverter.convertDatetime(dfAdvDate.getValue());
			optgrpFileImport.removeAllItems();
			List<String> listFilenameImported = dsqtHachToanService.findAllFilenameByNgayadv(ngayAdv);
			for(String item : listFilenameImported) {
				optgrpFileImport.addItem(item);
			}
		});
		
		Label lbCardType = new Label(CARD_TYPE);
//		lbCardType.setWidth(113.0f, Unit.PIXELS);
		cbbCardType = new ComboBox();
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD");
		cbbCardType.setValue("MC");
		cbbCardType.addValidator(new NullValidator(INPUT_FIELD, false));
		
		final Label lbLoaiTien = new Label(LOAI_TIEN);
		lbLoaiTien.setVisible(false);
		cbbLoaiTien = new ComboBox();
		cbbLoaiTien.setNullSelectionAllowed(false);
		cbbLoaiTien.addItems("All","VND","USD");
		cbbLoaiTien.setValue("All");
		cbbLoaiTien.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbLoaiTien.setValidationVisible(false);
		cbbLoaiTien.setVisible(false);
		
		final Button btExport = new Button(EXPORT);
		btExport.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExport.setIcon(FontAwesome.DOWNLOAD);
		btExport.setDescription("Xuất hạch toán theo Ngày Adv & Loại thẻ. Dữ liệu xuất ra gồm 2 sheet VND & USD");
		btExport.addClickListener(event -> {
	        
	        try {
	        	if(dfAdvDate.isEmpty() || cbbCardType.isEmpty()) {
	        		Notification.show(ERROR_MESSAGE, "Vui lòng chọn Ngày ADV & Loại thẻ", Type.ERROR_MESSAGE);
	        		return;
	        	}
	        	
	        	String ngayAdv = timeConverter.convertDatetime(dfAdvDate.getValue());
				String cardBrn = cbbCardType.getValue().toString();
	        	
	        	//EXPORT LIST TO EXCEL FILE
	            XSSFWorkbook workbookExport = new XSSFWorkbook();
	            XSSFSheet sheetVND = workbookExport.createSheet("VND");
	            XSSFSheet sheetUSD = workbookExport.createSheet("USD");
		        
	            XSSFCellStyle csNumber = workbookExport.createCellStyle();
	            XSSFDataFormat xssfDataFormat = workbookExport.createDataFormat();
	            csNumber.setDataFormat(xssfDataFormat.getFormat("#,##0"));
	            
	            XSSFCellStyle csNumberDecimal = workbookExport.createCellStyle();
	            XSSFDataFormat xssfDataFormatDecimal = workbookExport.createDataFormat();
	            csNumberDecimal.setDataFormat(xssfDataFormatDecimal.getFormat("#,##0.00"));
	            
	            XSSFCellStyle cellStyleFontBold = workbookExport.createCellStyle();
	            XSSFFont font = workbookExport.createFont();
	            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	            cellStyleFontBold.setFont(font);   
	            
	            XSSFCellStyle cellStyleFontBoldItalic = workbookExport.createCellStyle();
	            XSSFFont fontBI = workbookExport.createFont();
	            fontBI.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	            fontBI.setItalic(true);
	            cellStyleFontBoldItalic.setFont(fontBI);   
	            
	            rowNumExport = 0;
		        LOGGER.info("Creating excel");

		        if(rowNumExport == 0) {
	            	Object[] rowHeader = {null, "SL GD chấp thuận", "SL GD từ chối", "Số tiền BIDV trích nợ", "Số tiền GD", "Thu phí interchange", "Phí interchange phải trả", "Phí xử lý GD ATM", "T140/T461","File name","Clearing cycle"};
	            	int colNum = 0;	 
	            	XSSFRow row = sheetVND.createRow(rowNumExport++);         	
	            	for (Object field : rowHeader) {
	            		Cell cell = row.createCell(colNum++, CellType.STRING);
	            		cell.setCellValue((String)field);
	            		cell.setCellStyle(cellStyleFontBold);
	            	}      
	            	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
		        }
		        
		        dsqtHachToanList = dsqtHachToanService.findHachToanByNgayAdvAndCardBrnAndCurr(ngayAdv, cardBrn, "VND");
		        for(DsqtHachToanCustom item : dsqtHachToanList) {
					XSSFRow row = sheetVND.createRow(rowNumExport++);
					
					Cell cellHachToan = row.createCell(0);
					cellHachToan.setCellValue(item.getHachToan());
					
					if(item.getHachToan().matches("(A. GIAO DỊCH THANH TOÁN HÀNG HÓA|B. GIAO DỊCH RÚT TM|C. GIAO DỊCH MONEYSEND|D. PHÍ GIAO DỊCH PHI TÀI CHÍNH|E. GIAO DỊCH CHARGEBACK|F. FEECOLL).*") || item.getHachToan().equals("TỔNG")) 
						cellHachToan.setCellStyle(cellStyleFontBold);
						
					if(item.getSlGdChapThuan().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellSlGdChapThuan = row.createCell(1,CellType.NUMERIC);
						cellSlGdChapThuan.setCellValue(item.getSlGdChapThuan().doubleValue());
						cellSlGdChapThuan.setCellStyle(csNumber);
					} 
					
					if(item.getStBidvTrichNo().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellStBidvTrichNo = row.createCell(3,CellType.NUMERIC);
						cellStBidvTrichNo.setCellValue(item.getStBidvTrichNo().doubleValue());
						cellStBidvTrichNo.setCellStyle(csNumber);
					}
					
					if(item.getStGd().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellStGd = row.createCell(4,CellType.NUMERIC);
						cellStGd.setCellValue(item.getStGd().doubleValue());
						cellStGd.setCellStyle(csNumber);
					}
					
					if(item.getThuPhiInterchange().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellThuPhiInterchange = row.createCell(5,CellType.NUMERIC);
						cellThuPhiInterchange.setCellValue(item.getThuPhiInterchange().doubleValue());
						cellThuPhiInterchange.setCellStyle(csNumber);
					}
						
					if(item.getPhiInterchangePhaiTra().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellPhiInterchangePhaiTra = row.createCell(6,CellType.NUMERIC);
						cellPhiInterchangePhaiTra.setCellValue(item.getPhiInterchangePhaiTra().doubleValue());
						cellPhiInterchangePhaiTra.setCellStyle(csNumber);
					}
						
					row.createCell(8).setCellValue(item.getT140());
					row.createCell(9).setCellValue(item.getFilename());
					row.createCell(10).setCellValue(item.getCycle());
					
		        }
		        
		        //---------------------------------------------------------
		        rowNumExport = 0;
		        if(rowNumExport == 0) {
		        	Object[] rowHeader = {null, "SL GD chấp thuận", "SL GD từ chối", "Số tiền BIDV trích nợ", "Số tiền GD", "Thu phí interchange", "Phí interchange phải trả", "Phí xử lý GD ATM", "T140/T461","File name","Clearing cycle"};
	            	int colNum = 0;	 
	            	XSSFRow row = sheetUSD.createRow(rowNumExport++);         	
	            	for (Object field : rowHeader) {
	            		Cell cell = row.createCell(colNum++, CellType.STRING);
	            		cell.setCellValue((String)field);
	            		cell.setCellStyle(cellStyleFontBold);
	            	}      
	            	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
		        }
		        
		        dsqtHachToanList = dsqtHachToanService.findHachToanByNgayAdvAndCardBrnAndCurr(ngayAdv, cardBrn, "USD");
		        for(DsqtHachToanCustom item : dsqtHachToanList) {
					XSSFRow row = sheetUSD.createRow(rowNumExport++);
					
					Cell cellHachToan = row.createCell(0);
					cellHachToan.setCellValue(item.getHachToan());
					
					if(item.getHachToan().matches("(A. GIAO DỊCH THANH TOÁN HÀNG HÓA|B. GIAO DỊCH RÚT TM|C. GIAO DỊCH MONEYSEND|D. PHÍ GIAO DỊCH PHI TÀI CHÍNH|E. GIAO DỊCH CHARGEBACK|F. FEECOLL).*") || item.getHachToan().equals("TỔNG")) 
						cellHachToan.setCellStyle(cellStyleFontBold);
					
					
					if(item.getSlGdChapThuan().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellSlGdChapThuan = row.createCell(1,CellType.NUMERIC);
						cellSlGdChapThuan.setCellValue(item.getSlGdChapThuan().doubleValue());
						cellSlGdChapThuan.setCellStyle(csNumber);
					} 
					
					if(item.getStBidvTrichNo().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellStBidvTrichNo = row.createCell(3,CellType.NUMERIC);
						cellStBidvTrichNo.setCellValue(item.getStBidvTrichNo().doubleValue());
						cellStBidvTrichNo.setCellStyle(csNumberDecimal);
					}
					
					if(item.getStGd().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellStGd = row.createCell(4,CellType.NUMERIC);
						cellStGd.setCellValue(item.getStGd().doubleValue());
						cellStGd.setCellStyle(csNumberDecimal);
					}
					
					if(item.getThuPhiInterchange().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellThuPhiInterchange = row.createCell(5,CellType.NUMERIC);
						cellThuPhiInterchange.setCellValue(item.getThuPhiInterchange().doubleValue());
						cellThuPhiInterchange.setCellStyle(csNumberDecimal);
					}
						
					if(item.getPhiInterchangePhaiTra().compareTo(BigDecimal.ZERO)!=0) {
						Cell cellPhiInterchangePhaiTra = row.createCell(6,CellType.NUMERIC);
						cellPhiInterchangePhaiTra.setCellValue(item.getPhiInterchangePhaiTra().doubleValue());
						cellPhiInterchangePhaiTra.setCellStyle(csNumberDecimal);
					}
						
					row.createCell(8).setCellValue(item.getT140());
					row.createCell(9).setCellValue(item.getFilename());
					row.createCell(10).setCellValue(item.getCycle());
		        }
		        
		        
		        
		        sheetVND.createFreezePane(0, 1);
		        sheetUSD.createFreezePane(0, 1);
		        
		        for (int i=0; i<11; i++)  {
		        	sheetVND.autoSizeColumn(i);
		        	sheetUSD.autoSizeColumn(i);
		        }
		        	
		        
	        	fileNameOutput = "HACHTOAN_" + "ADV"  + ngayAdv + "_" + cardBrn + ".xlsx";
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
	        } catch (SQLException e) {
	            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
	        }
	        
		});
		
		btRemove.setStyleName(ValoTheme.BUTTON_DANGER);
		btRemove.setWidth(100.0f, Unit.PIXELS);
		btRemove.setIcon(FontAwesome.REMOVE);
		btRemove.setDescription("Xóa dữ liệu theo Ngày ADV, cả MC & MD");
		btRemove.addClickListener(event -> {
			if(!dfAdvDate.isEmpty()) {
				String ngayAdv = timeConverter.convertDatetime(dfAdvDate.getValue());
				int countDel = dsqtHachToanService.deleteAllByNgayAdv(ngayAdv);
				Notification.show("Thông báo", sUserId.toUpperCase() + " Đã xóa " + countDel + " dòng ADV" + ngayAdv, Type.WARNING_MESSAGE);
				LOGGER.info(sUserId.toUpperCase() + " Đã xóa " + countDel + " dòng ADV" + ngayAdv);
				optgrpFileImport.removeAllItems();
			}
			
				
		});
		
		Upload chooseFile = new Upload(null, new Upload.Receiver() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream outputFile = null;
				try {
					int rowCount = dsqtHachToanService.countByFilename(filename);
					
					// TODO Auto-generated method stub
					fileNameImport = StringUtils.substringBefore(filename, ".txt") + "_" + timeConverter.getCurrentTime() + ".txt";

					Window confirmDialog = new Window();
					final FormLayout content = new FormLayout();
					content.setMargin(true);

					Button bYes = new Button("OK");

					confirmDialog.setCaption("Chương trình sẽ xử lý file import, bấm OK để tiếp tục");
					confirmDialog.setWidth(400.0f, Unit.PIXELS);
					try {
						if (!filename.isEmpty()) {
							fileImport = new File(configurationHelper.getPathFileRoot() + "/" + fileNameImport);
							if (!fileImport.exists()) {
								fileImport.createNewFile();
							}
							outputFile = new FileOutputStream(fileImport);
							
							bYes.addClickListener(event -> {
								try {
									String ngayAdv = timeConverter.convertDatetime(dfAdvDate.getValue());
									String cycleNo = null;
									String cardbrn = null;
									String sDate = null;
								    Scanner sc = new Scanner(fileImport); 
								    
								    int rownum = 0;
								    while (sc.hasNextLine()) 
								    {
								    	rownum++;
								    	String sLine = sc.nextLine();
								    	
								    	if(!sLine.substring(0,1).trim().isEmpty()) {
								    		System.out.println("-------------------NEW OBJECT------------------"); 
								    		cycleNo = null;
								    		cardbrn = null;
								    		isChargeback = false;
								    	}
								    	
								    	if(sLine.contains("CLEARING CYCLE"))
								    		cycleNo = sLine.substring(63, 66);
								    		
								    	if(sLine.contains("ACCEPTANCE BRAND")) {
								    		if(sLine.substring(19, 22).equals("DMC"))
								    			cardbrn = "MD";
								    		else
								    			cardbrn = "MC";
								    	}
								    	
								    	if(sLine.contains("BUSINESS SERVICE LEVEL")) {
								    		sDate = sLine.substring(62, 72);
								    		if(!sDate.trim().isEmpty()) {
								    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								    			Date sDateConvers = sdf.parse(sDate);
								    			sdf = new SimpleDateFormat("dd.MM.yy");
								    			sDate = sdf.format(sDateConvers);
								    		}
								    		
								    	}
								    	
								    	
								    	if(sLine.contains("FIRST C/B"))
								    		isChargeback = true;
								    	
								    	LOGGER.info("ROWNUM: " + rownum);
								    	
								    	if(!isChargeback && (sLine.contains("PURCHASE") || sLine.contains("CREDIT") || sLine.contains("UNIQUE TXN"))){
								    		dsqtHachToan = new DsqtHachToan();
								    		dsqtHachToan.setId(timeConverter.getCurrentTime() + String.format("%07d", rownum));
								    		dsqtHachToan.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
								    		dsqtHachToan.setUsrId(sUserId);
								    		dsqtHachToan.setNgayAdv(ngayAdv);
								    		if(sLine.substring(1, 5).equals("SEC.") && sLine.substring(11, 12).equals("-"))
								    			dsqtHachToan.setTransType(GDCBTTHH);
								    		else
								    			dsqtHachToan.setTransType(GDTTHH);
								    		dsqtHachToan.setCardBrn(cardbrn);
								    		dsqtHachToan.setCurr(sLine.substring(74, 77));
								    		dsqtHachToan.setCycleNo(new BigDecimal(cycleNo.trim()));
								    		dsqtHachToan.setCount(new BigDecimal(sLine.substring(34, 42).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmount(new BigDecimal(sLine.substring(43, 66).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmountType(sLine.substring(67, 69));
								    		dsqtHachToan.setTransFee(new BigDecimal(sLine.substring(78, 98).trim().replaceAll(",", "")));
								    		dsqtHachToan.setTransFeeType(sLine.substring(99, 101));
								    		dsqtHachToan.setT140Date(sDate);
								    		dsqtHachToan.setFilename(filename);
								    		if(sLine.contains("RVSL"))
								    			dsqtHachToan.setReversal("R");
								    		
								    		if(!dsqtHachToan.getAmount().equals(BigDecimal.ZERO) || !dsqtHachToan.getTransFee().equals(BigDecimal.ZERO))
								    			dsqtHachToanService.save(dsqtHachToan);
								    	}
								    		
								    	if(!isChargeback && (sLine.contains("ATM CASH") || sLine.contains("CASH DISB"))) {
								    		dsqtHachToan = new DsqtHachToan();
								    		dsqtHachToan.setId(timeConverter.getCurrentTime() + String.format("%07d", rownum));
								    		dsqtHachToan.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
								    		dsqtHachToan.setUsrId(sUserId);
								    		dsqtHachToan.setNgayAdv(ngayAdv);
								    		if(sLine.substring(1, 5).equals("SEC.") && sLine.substring(11, 12).equals("-"))
								    			dsqtHachToan.setTransType(GDCBRTM);
								    		else
								    			dsqtHachToan.setTransType(GDRTM);
								    		
								    		dsqtHachToan.setCardBrn(cardbrn);
								    		dsqtHachToan.setCurr(sLine.substring(74, 77));
								    		dsqtHachToan.setCycleNo(new BigDecimal(cycleNo.trim()));
								    		dsqtHachToan.setCount(new BigDecimal(sLine.substring(34, 42).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmount(new BigDecimal(sLine.substring(43, 66).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmountType(sLine.substring(67, 69));
								    		dsqtHachToan.setTransFee(new BigDecimal(sLine.substring(78, 98).trim().replaceAll(",", "")));
								    		dsqtHachToan.setTransFeeType(sLine.substring(99, 101));
								    		dsqtHachToan.setT140Date(sDate);
								    		dsqtHachToan.setFilename(filename);
								    		if(sLine.contains("RVSL"))
								    			dsqtHachToan.setReversal("R");
								    		
								    		if(!dsqtHachToan.getAmount().equals(BigDecimal.ZERO) || !dsqtHachToan.getTransFee().equals(BigDecimal.ZERO))
								    			dsqtHachToanService.save(dsqtHachToan);
								    	}
								    	
								    	if(!isChargeback && sLine.contains("PAYM TRANS")){
								    		dsqtHachToan = new DsqtHachToan();
								    		dsqtHachToan.setId(timeConverter.getCurrentTime() + String.format("%07d", rownum));
								    		dsqtHachToan.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
								    		dsqtHachToan.setUsrId(sUserId);
								    		dsqtHachToan.setNgayAdv(ngayAdv);
								    		dsqtHachToan.setTransType(GDMSFF);
								    		dsqtHachToan.setCardBrn(cardbrn);
								    		dsqtHachToan.setCurr(sLine.substring(74, 77));
								    		dsqtHachToan.setCycleNo(new BigDecimal(cycleNo.trim()));
								    		dsqtHachToan.setCount(new BigDecimal(sLine.substring(34, 42).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmount(new BigDecimal(sLine.substring(43, 66).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmountType(sLine.substring(67, 69));
								    		dsqtHachToan.setTransFee(new BigDecimal(sLine.substring(78, 98).trim().replaceAll(",", "")));
								    		dsqtHachToan.setTransFeeType(sLine.substring(99, 101));
								    		dsqtHachToan.setT140Date(sDate);
								    		dsqtHachToan.setFilename(filename);
								    		if(sLine.contains("RVSL"))
								    			dsqtHachToan.setReversal("R");
								    		
								    		if(!dsqtHachToan.getAmount().equals(BigDecimal.ZERO) || !dsqtHachToan.getTransFee().equals(BigDecimal.ZERO))
								    			dsqtHachToanService.save(dsqtHachToan);
								    	}
								    		
								    	if(sLine.contains("BALNCE INQ")) {
								    		dsqtHachToan = new DsqtHachToan();
								    		dsqtHachToan.setId(timeConverter.getCurrentTime() + String.format("%07d", rownum));
								    		dsqtHachToan.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
								    		dsqtHachToan.setUsrId(sUserId);
								    		dsqtHachToan.setNgayAdv(ngayAdv);
								    		dsqtHachToan.setTransType(GDNONFI);
								    		dsqtHachToan.setCardBrn(cardbrn);
								    		dsqtHachToan.setCurr(sLine.substring(74, 77));
								    		dsqtHachToan.setCycleNo(new BigDecimal(cycleNo.trim()));
								    		dsqtHachToan.setCount(new BigDecimal(sLine.substring(34, 42).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmount(new BigDecimal(sLine.substring(43, 66).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmountType(sLine.substring(67, 69));
								    		dsqtHachToan.setTransFee(new BigDecimal(sLine.substring(78, 98).trim().replaceAll(",", "")));
								    		dsqtHachToan.setTransFeeType(sLine.substring(99, 101));
								    		dsqtHachToan.setT140Date(sDate);
								    		dsqtHachToan.setFilename(filename);
								    		if(sLine.contains("RVSL"))
								    			dsqtHachToan.setReversal("R");
								    		
								    		if(!dsqtHachToan.getAmount().equals(BigDecimal.ZERO) || !dsqtHachToan.getTransFee().equals(BigDecimal.ZERO))
								    			dsqtHachToanService.save(dsqtHachToan);
								    	}
								    	
								    	if(isChargeback && sLine.contains("PURCHASE")) {
								    		dsqtHachToan = new DsqtHachToan();
								    		dsqtHachToan.setId(timeConverter.getCurrentTime() + String.format("%07d", rownum));
								    		dsqtHachToan.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
								    		dsqtHachToan.setUsrId(sUserId);
								    		dsqtHachToan.setNgayAdv(ngayAdv);
								    		if(sLine.contains("PURCHASE"))
								    			dsqtHachToan.setTransType(GDCBTTHH);
								    		else
								    			dsqtHachToan.setTransType(GDCBRTM);
								    		dsqtHachToan.setCardBrn(cardbrn);
								    		dsqtHachToan.setCurr(sLine.substring(74, 77));
								    		dsqtHachToan.setCycleNo(new BigDecimal(cycleNo.trim()));
								    		dsqtHachToan.setCount(new BigDecimal(sLine.substring(34, 42).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmount(new BigDecimal(sLine.substring(43, 66).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmountType(sLine.substring(67, 69));
								    		dsqtHachToan.setTransFee(new BigDecimal(sLine.substring(78, 98).trim().replaceAll(",", "")));
								    		dsqtHachToan.setTransFeeType(sLine.substring(99, 101));
								    		dsqtHachToan.setT140Date(sDate);
								    		dsqtHachToan.setFilename(filename);
								    		if(sLine.contains("RVSL"))
								    			dsqtHachToan.setReversal("R");
								    		
								    		if(!dsqtHachToan.getAmount().equals(BigDecimal.ZERO) || !dsqtHachToan.getTransFee().equals(BigDecimal.ZERO))
								    			dsqtHachToanService.save(dsqtHachToan);
								    	}
								    	
								    	if(sLine.contains("FEE COL") && sLine.contains("ORIG")) {
								    		dsqtHachToan = new DsqtHachToan();
								    		dsqtHachToan.setId(timeConverter.getCurrentTime() + String.format("%07d", rownum));
								    		dsqtHachToan.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
								    		dsqtHachToan.setUsrId(sUserId);
								    		dsqtHachToan.setNgayAdv(ngayAdv);
								    		dsqtHachToan.setTransType(FEECOLL);
								    		dsqtHachToan.setCardBrn(cardbrn);
								    		dsqtHachToan.setCurr(sLine.substring(74, 77));
								    		dsqtHachToan.setCycleNo(new BigDecimal(cycleNo.trim()));
								    		dsqtHachToan.setCount(new BigDecimal(sLine.substring(34, 42).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmount(new BigDecimal(sLine.substring(43, 66).trim().replaceAll(",", "")));
								    		dsqtHachToan.setAmountType(sLine.substring(67, 69));
								    		dsqtHachToan.setTransFee(new BigDecimal(sLine.substring(78, 98).trim().replaceAll(",", "")));
								    		dsqtHachToan.setTransFeeType(sLine.substring(99, 101));
								    		dsqtHachToan.setT140Date(sDate);
								    		dsqtHachToan.setFilename(filename);
								    		if(sLine.contains("RVSL"))
								    			dsqtHachToan.setReversal("R");
								    		
								    		if(!dsqtHachToan.getAmount().equals(BigDecimal.ZERO) || !dsqtHachToan.getTransFee().equals(BigDecimal.ZERO))
								    			dsqtHachToanService.save(dsqtHachToan);
								    	}
								    	
								    	
								    }
								      
								    optgrpFileImport.setVisible(true);
								    optgrpFileImport.removeAllItems();
								    List<String> listFilenameImported = dsqtHachToanService.findAllFilenameByNgayadv(ngayAdv);
									for(String item : listFilenameImported) {
										optgrpFileImport.addItem(item);
									}
									
									confirmDialog.close();
									
								} catch (Exception e) {
									// TODO: handle exception
									Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
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

					} catch (IOException e) {
						e.printStackTrace();
						LOGGER.error("Error: " + e.getMessage());
					}
					
					
					if(rowCount>0) {
						Notification.show("Lỗi","File này đã tồn tại", Type.ERROR_MESSAGE);
						confirmDialog.close();
					}
					

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

		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		
		horizontalLayout1st.addComponent(lbAdvDate);
		horizontalLayout1st.addComponent(dfAdvDate);
		horizontalLayout1st.addComponent(chooseFile);
		horizontalLayout1st.setComponentAlignment(lbAdvDate, Alignment.MIDDLE_LEFT);
		horizontalLayout1st.setComponentAlignment(chooseFile, Alignment.MIDDLE_LEFT);
		
		horizontalLayout2nd.addComponent(lbCardType);
		horizontalLayout2nd.addComponent(cbbCardType);
		horizontalLayout2nd.addComponent(lbLoaiTien);
		horizontalLayout2nd.addComponent(cbbLoaiTien);
		horizontalLayout2nd.addComponent(btExport);
		horizontalLayout2nd.addComponent(btRemove);
		horizontalLayout2nd.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		horizontalLayout2nd.setComponentAlignment(lbLoaiTien, Alignment.MIDDLE_LEFT);
		
		mainLayout.addComponent(horizontalLayout1st);
		mainLayout.addComponent(horizontalLayout2nd);
		mainLayout.addComponent(optgrpFileImport);
		setCompositionRoot(mainLayout);
	}


	private static int getRandomSeq(int min, int max) {
		// It has a minimum value of -2,147,483,648 and a maximum value of 2,147,483,647 (inclusive)
		return min + (int) (Math.random() * ((max - min) + 1));
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
	
	 public static String deAccent(String str) {
         String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
         Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
         return pattern.matcher(nfdNormalizedString).replaceAll("").replace("đ", "d").replaceAll("Đ", "D");
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
		
		private String formatNumberColor(BigDecimal number) {
			if (number.compareTo(BigDecimal.ZERO) < 0) {
				return "<span style=\"padding:7px 0px; background-color: #FFFF00\">" + number + "</span>";

			} else
				return String.valueOf(number);
		}
		
		public static boolean isValidFormat(String format, String value) {
	        Date date = null;
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(format);
	            date = sdf.parse(value);
	            if (!value.equals(sdf.format(date))) {
	                date = null;
	            }
	        } catch (ParseException ex) {
	            ex.printStackTrace();
	        }
	        return date != null;
	    }

		

	@Override
	public void eventReload() {
		
	}
}
