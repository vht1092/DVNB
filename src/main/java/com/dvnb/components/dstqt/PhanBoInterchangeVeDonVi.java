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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.money.Monetary;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.dvnb.entities.DsqtCurrency;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbSummary;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.DsqtCurrencyService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.dvnb.services.TyGiaTqtService;
import com.monitorjbl.xlsx.StreamingReader;
import com.sun.javafx.print.Units;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.MultiSelectMode;
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
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class PhanBoInterchangeVeDonVi extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PhanBoInterchangeVeDonVi.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "PHÂN BỔ INTERCHANGE VỀ ĐƠN VỊ";
	private static final String MA_GD = "MÃ GD";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String CARD_TYPE = "LOẠI THẺ";
	private static final String LOAI_GD = "LOẠI GD";
	private static final String PAN = "PAN";
	private static final String APVCODE = "APV CODE";
	private static final String SEARCH = "SEARCH";
	private static final String XUAT_FILE_UPLOAD = "XUẤT FILE UPLOAD";
	
	private transient String sUserId;
	private final ComboBox cbbMaGD;
	private DateField dfNgayAdv;
	private final ComboBox cbbCardType;
	private final ComboBox cbbLoaiGD;
	private final TextField tfPAN;
	private final TextField tfApvcode;
	final Button btSearch = new Button(SEARCH);
	final Button btXuatUpload = new Button(XUAT_FILE_UPLOAD);
	private final OptionGroup optgrpMaGD;
	
	private DataGridUploadInterchange grid;
	private transient Page<DoiSoatData> result;
	private final transient DoiSoatDataService doiSoatDataService;
	private List<DoiSoatData> doisoatList;
	private final transient DsqtCurrencyService dsqtCurrencyService;
	private final transient TyGiaTqtService tyGiaTqtService;
	
	// Paging
	private final static int SIZE_OF_PAGE = 100;
	private final static int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	
	
	public PhanBoInterchangeVeDonVi() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		final HorizontalLayout formLayout3rd = new HorizontalLayout();
		formLayout3rd.setSpacing(true);
		final HorizontalLayout formLayout4th = new HorizontalLayout();
		formLayout4th.setSpacing(true);
		formLayout4th.setMargin(new MarginInfo(false, false, true, false));
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		dsqtCurrencyService = (DsqtCurrencyService) helper.getBean("dsqtCurrencyService");
		tyGiaTqtService = (TyGiaTqtService) helper.getBean("tyGiaTqtService");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridUploadInterchange();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, true, false, true));
		
		final Label lbNgayADV = new Label("NGÀY ADV");
		dfNgayAdv = new DateField();
		dfNgayAdv.setDateFormat("dd/MM/yyyy");
		dfNgayAdv.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayAdv.setValidationVisible(false);
		
		final Label lbCardType = new Label(CARD_TYPE);
		lbCardType.setWidth(60f, Unit.PIXELS);
		cbbCardType = new ComboBox();
		cbbCardType.setWidth(100f, Unit.PIXELS);
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbCardType.addItems("MC","MD","VS","VSD");
		cbbCardType.setValidationVisible(false);
		
		final Label lbLoaiGD = new Label(LOAI_GD);
		lbLoaiGD.setWidth(60f, Unit.PIXELS);
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setWidth(100f, Unit.PIXELS);
		cbbLoaiGD.setNullSelectionAllowed(true);
		cbbLoaiGD.addItems("TTHH","RTM","MSFF");
		cbbLoaiGD.setItemCaption("TTHH", "Thanh toán hàng hóa");
		cbbLoaiGD.setItemCaption("RTM", "Rút tiền mặt");
		cbbLoaiGD.setItemCaption("MSFF", "Money send/ Fast fund");
		cbbLoaiGD.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbLoaiGD.setValidationVisible(false);
		
		optgrpMaGD = new OptionGroup();
		optgrpMaGD.setSizeFull();
		optgrpMaGD.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
//		optgrpMaGD.setCaption("Merchant");
		optgrpMaGD.setMultiSelect(true);
//		optgrpMaGD.addItems("00","18","01","20","28","29","19");
		
		final Label lbMaGD = new Label(MA_GD);
		lbMaGD.setWidth(53.66f, Unit.PIXELS);
		cbbMaGD = new ComboBox();
		cbbMaGD.setNullSelectionAllowed(false);
		
		
		cbbCardType.addValueChangeListener(event -> {
			optgrpMaGD.removeAllItems();
			if(cbbCardType.getValue().toString().startsWith("M")) 
				optgrpMaGD.addItems("00","18","01","20","28","29","19");
			else 
				optgrpMaGD.addItems("05","25","26","07","27","06","10","20");
			
			optgrpMaGD.getItemIds().forEach(item-> {
				optgrpMaGD.select(item);
			});
			
		});
		
		final Label lbPAN = new Label(PAN);
		lbPAN.setWidth(61.75f, Unit.PIXELS);
		tfPAN = new TextField();
		tfPAN.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbApvcode = new Label(APVCODE);
		tfApvcode = new TextField();
		tfApvcode.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		btSearch.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSearch.setWidth(250.0f, Unit.PIXELS);
		btSearch.setIcon(FontAwesome.SEARCH);
		
		btXuatUpload.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btXuatUpload.setWidth(250f, Unit.PIXELS);
		btXuatUpload.setIcon(FontAwesome.FILE_EXCEL_O);
		
		btSearch.addClickListener(event -> {
			if(!checkValidator()) {
				return;
			}
			
			grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid("All");

			grid.refreshData();
			// Refresh paging button
		});
		
		btXuatUpload.addClickListener(event -> {
			
			String ngayAdv = timeConverter.convertDatetime(dfNgayAdv.getValue());
			String cardType = cbbCardType.getValue().toString();
			TyGiaTqt tygiaTQT = tyGiaTqtService.findTyGiaTqtByNgayAdvAndCardType(ngayAdv, cardType);
//			if(tygiaTQT==null) {
//				Notification.show("Lỗi","Chưa cập nhật tỷ giá TQT", Type.ERROR_MESSAGE);
//				return;
//			}
				
			//EXPORT LIST TO EXCEL FILE
            XSSFWorkbook workbookExport = new XSSFWorkbook();
            XSSFSheet sheetExport = workbookExport.createSheet("Daily upload");
	        
            DataFormat format = workbookExport.createDataFormat();
            CellStyle styleNumber;
            styleNumber = workbookExport.createCellStyle();
            styleNumber.setDataFormat(format.getFormat("0.0"));
            
            rowNumExport = 0;
	        LOGGER.info("Creating excel");

	        if(rowNumExport == 0) {
	        	Object[] rowHeader = null;
	        	if(cbbCardType.getValue().toString().startsWith("M")) {
	        		rowHeader = new Object[] {"STT","Mã giao dịch","Số thẻ","ST giao dịch","Số tiền TQT","ST quy đổi VND","LT GD","LT TQT","LT quy đổi",
	        				"Phí interchange","Tỷ giá","ĐVCNT","Ngày giao dịch","Mã chuẩn chi","T112","STT file","Ghi chú","Mã MCC"};
	        	}
	        	else {
	        		rowHeader = new Object[] {"STT","Mã giao dịch","Số thẻ","ST giao dịch","LT GD","ST quy đổi VND","LT quy đổi","LT TQT","Phí interchange",
	        				"Tỷ giá","Phí ISA","ĐVCNT","Quốc gia","Mã chuẩn chi","Ngày giao dịch","Ngày CW xử lý","VSS","Ghi chú","MCC"};
	        	}
            	
            	int colNum = 0;	 
            	XSSFRow row = sheetExport.createRow(rowNumExport++);         	
            	for (Object field : rowHeader) {
            		Cell cell = row.createCell(colNum++, CellType.STRING);
            		cell.setCellValue((String)field);
            	}      
            	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
	        }
	        
	        try {
		        for(DoiSoatData item : doisoatList) {
					XSSFRow row = sheetExport.createRow(rowNumExport++);
					String cardtype = cbbCardType.getValue().toString();
					if(cardtype.startsWith("M")) {
						row.createCell(0).setCellValue(rowNumExport-1);
						row.createCell(1).setCellValue(item.getMaGd());
						row.createCell(2).setCellValue(item.getSoThe());
						row.createCell(3,CellType.NUMERIC).setCellValue(item.getStGd().doubleValue());
						row.createCell(4,CellType.NUMERIC).setCellValue(item.getStTqt().doubleValue());
						row.createCell(5,CellType.NUMERIC).setCellValue(item.getStQdVnd().doubleValue());
						row.createCell(6,CellType.NUMERIC).setCellValue(item.getLtgd());
						row.createCell(7).setCellValue(item.getLttqt());
						row.createCell(8,CellType.NUMERIC).setCellValue(704);
						row.createCell(9,CellType.NUMERIC).setCellValue(item.getInterchange().doubleValue());
						if(item.getLttqt().equals("USD"))
						{
							if(cardtype.equals("MC")) {
								if(((item.getMaGd().startsWith("20")) || (item.getMaGd().startsWith("00") || item.getMaGd().startsWith("18"))))
									row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdTthh().doubleValue());
								if(item.getMaGd().startsWith("01"))
									row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdRtm().doubleValue());
								if(item.getMaGd().startsWith("28"))
									row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdMsff().doubleValue());
							} else
							{
								if((item.getMaGd().startsWith("00") && !item.getReversalInd().equals("R") && item.getStatusCw().equals(" "))
										|| (item.getMaGd().startsWith("00") && item.getReversalInd().equals("R") && !item.getStatusCw().equals(" "))
										|| (item.getMaGd().startsWith("00") && !item.getReversalInd().equals("R") && !item.getStatusCw().equals(" "))	
										|| (item.getMaGd().startsWith("18") && item.getStatusCw().equals(" "))
										|| (item.getMaGd().startsWith("20") && item.getStatusCw().equals("C")))
//										|| (cardtype.equals("MC") && ((item.getMaGd().startsWith("20") && item.getStatusCw().equals(" ")) || (item.getMaGd().startsWith("20") && item.getReversalInd().equals("R") && item.getStatusCw().equals(" ")))))
										row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdTthh().doubleValue());
									if(((item.getMaGd().startsWith("01") || item.getMaGd().startsWith("12")) && item.getStatusCw().equals(" ") && !item.getReversalInd().equals("R"))
										|| ((item.getMaGd().startsWith("01") || item.getMaGd().startsWith("12")) && !item.getStatusCw().equals(" ")))
//										|| (cardtype.equals("MC") && item.getMaGd().startsWith("01") && item.getReversalInd().equals("R") && item.getStatusCw().equals(" ")))
										row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdRtm().doubleValue());
									if(item.getMaGd().startsWith("28") && item.getStatusCw().equals(" "))
										row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdMsff().doubleValue());
									if(cardtype.equals("MD") && ((item.getMaGd().startsWith("20") && item.getStatusCw().equals(" ")) || (item.getMaGd().startsWith("00") && item.getReversalInd().equals("R") && item.getStatusCw().equals(" "))) )
										row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdHttthh().doubleValue());
									if(cardtype.equals("MD") && item.getMaGd().startsWith("01") && item.getReversalInd().equals("R") && item.getStatusCw().equals(" "))
										row.createCell(10,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdHtrtm().doubleValue());
							}
							
							
							
							
						}
						else
							row.createCell(10,CellType.NUMERIC).setCellValue(1);
						row.createCell(11).setCellValue(item.getDvcnt());
						row.createCell(12).setCellValue(item.getNgayGd());
						row.createCell(13).setCellValue(item.getMaCapPhep());
						String ngayFileIncoming = item.getNgayFileIncoming();
		    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		    			Date ngayFileIncomingConvers = sdf.parse(ngayFileIncoming);
						sdf = new SimpleDateFormat("dd/MM/yyyy");
		    			ngayFileIncoming = sdf.format(ngayFileIncomingConvers);
						row.createCell(14).setCellValue(ngayFileIncoming);
						row.createCell(15).setCellValue("");
						row.createCell(16).setCellValue("");
						row.createCell(17).setCellValue(item.getMcc());
						
					}
					else {
						row.createCell(0).setCellValue(rowNumExport-1);
						row.createCell(1).setCellValue(item.getMaGd());
						row.createCell(2).setCellValue(item.getSoThe());
						row.createCell(3,CellType.NUMERIC).setCellValue(item.getStGd().doubleValue());
						row.createCell(4).setCellValue(item.getLtgd());
						row.createCell(5,CellType.NUMERIC).setCellValue(item.getStQdVnd().doubleValue());
						row.createCell(6,CellType.NUMERIC).setCellValue(704);
						row.createCell(7).setCellValue(item.getLttqt());
						row.createCell(8,CellType.NUMERIC).setCellValue(item.getInterchange().doubleValue());
						if(item.getLttqt().equals("USD"))
						{
							if(item.getMaGd().startsWith("05") || item.getMaGd().startsWith("26"))
								row.createCell(9,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdTthh().doubleValue());
							if(item.getMaGd().startsWith("07"))
								row.createCell(9,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdRtm().doubleValue());
							if(item.getMaGd().startsWith("06") && item.getMerchantCity().contains("Visa Direct"))
								row.createCell(9,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdMsff().doubleValue());
							if(item.getMaGd().startsWith("25") || (item.getMaGd().startsWith("06") && !item.getMerchantCity().contains("Visa Direct")))
								row.createCell(9,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdHttthh().doubleValue());
							if(item.getMaGd().startsWith("27"))
								row.createCell(9,CellType.NUMERIC).setCellValue(tygiaTQT.getTyGiaGdHtrtm().doubleValue());
						}
						else
							row.createCell(9,CellType.NUMERIC).setCellValue(1);
						row.createCell(10,CellType.NUMERIC).setCellValue(item.getPhiXuLyGd().doubleValue());
						row.createCell(11).setCellValue(item.getDvcnt());
						row.createCell(12).setCellValue("");
						row.createCell(13).setCellValue(item.getMaCapPhep());
						row.createCell(14).setCellValue(item.getNgayGd());
						
						String ngayFileIncoming = item.getNgayFileIncoming();
		    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		    			Date ngayFileIncomingConvers = sdf.parse(ngayFileIncoming);
						sdf = new SimpleDateFormat("dd/MM/yyyy");
		    			ngayFileIncoming = sdf.format(ngayFileIncomingConvers);
		    			
						row.createCell(15).setCellValue(ngayFileIncoming);
						row.createCell(16).setCellValue("");
						row.createCell(17).setCellValue("");
						row.createCell(18).setCellValue(item.getMcc());
					}
					
		        }
	        
		        sheetExport.createFreezePane(0, 1);
	        
	        	String ngayADV = timeConverter.convertDatetime(dfNgayAdv.getValue());
	        	fileNameOutput = "Daily_Upload_" + ngayADV + "_" + cardType + "_" + cbbLoaiGD.getValue() + ".xlsx";
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
		        messageExportXLSX("Info","Export compeleted.");
		        
	        } catch (FileNotFoundException e) {
	            LOGGER.error(e.getMessage());
	        } catch (IOException e) {
	            LOGGER.error(e.getMessage());
	        } catch (ParseException e) {
				// TODO Auto-generated catch block
	        	LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		});
		
		formLayout1st.addComponent(lbNgayADV);
		formLayout1st.addComponent(dfNgayAdv);
		formLayout1st.addComponent(lbCardType);
		formLayout1st.addComponent(cbbCardType);
		formLayout1st.addComponent(lbLoaiGD);
		formLayout1st.addComponent(cbbLoaiGD);
		
		formLayout2nd.addComponent(lbMaGD);
		formLayout2nd.addComponent(optgrpMaGD);
		formLayout3rd.addComponent(lbPAN);
		formLayout3rd.addComponent(tfPAN);
		formLayout3rd.addComponent(lbApvcode);
		formLayout3rd.addComponent(tfApvcode);
		
		formLayout4th.addComponent(btSearch);
		formLayout4th.addComponent(btXuatUpload);
		
		formLayout1st.setComponentAlignment(lbNgayADV, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dfNgayAdv, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbLoaiGD, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbLoaiGD, Alignment.MIDDLE_LEFT);
		
		formLayout2nd.setComponentAlignment(lbMaGD, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(optgrpMaGD, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(lbPAN, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(tfPAN, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(lbApvcode, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(tfApvcode, Alignment.MIDDLE_LEFT);
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.addComponent(formLayout3rd);
		formLayout.addComponent(formLayout4th);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout4th, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(grid);
		
		setCompositionRoot(mainLayout);
	}
	

	private Page<DoiSoatData> getData(Pageable page) {
		String ngayADV = timeConverter.convertDatetime(dfNgayAdv.getValue());
		String cardno = tfPAN.getValue().isEmpty() ? "All" : tfPAN.getValue();
		String apvcode = tfApvcode.getValue().isEmpty() ? "All" : tfApvcode.getValue();
		String loaiGd = cbbLoaiGD.isEmpty() ? "All" : cbbLoaiGD.getValue().toString();
		String cardType = cbbCardType.getValue().toString();
		String[] arrMaGd = optgrpMaGD.getValue().toString().equals("[]") ? new String[]{"999"} : optgrpMaGD.getValue().toString().replace("[", "").replace("]", "").replaceAll("\\s+","").split(",");
		Set<String> setMagd = new HashSet<>();
		Collections.addAll(setMagd, arrMaGd); 
		
		doisoatList = new ArrayList<DoiSoatData>();
		doisoatList = doiSoatDataService.findAllTuNgayDenNgayAndMagdAndCardnoAndApvCodeAndCardtype(ngayADV, ngayADV, setMagd, cardno, apvcode,cardType);
		
		switch(cardType) {
			case "VSD":
				doisoatList = doisoatList.stream().
				filter(i -> (loaiGd.equals("TTHH") 
								&& ((i.getMaGd().startsWith("05") && i.getStatusCw().equals(" ")) 
								|| i.getMaGd().startsWith("25")
								|| (i.getMaGd().startsWith("06") && i.getStatusCw().equals(" ") && !i.getMerchantCity().contains("Visa Direct"))
								|| i.getMaGd().startsWith("26"))) 
						|| (loaiGd.equals("RTM") 
								&& (i.getMaGd().startsWith("07") || i.getMaGd().startsWith("27"))) 
						|| (loaiGd.equals("MSFF") && i.getMaGd().startsWith("06") && i.getMerchantCity().contains("Visa Direct") && i.getStatusCw().equals(" ")) 
				).collect(Collectors.toList());
				break;
			case "VS":
				doisoatList = doisoatList.stream().
				filter(i -> (loaiGd.equals("TTHH") && (i.getMaGd().startsWith("05") || i.getMaGd().startsWith("25") 
								|| (i.getMaGd().startsWith("06") && !i.getMerchantCity().contains("Visa Direct"))
								|| i.getMaGd().startsWith("26"))) 
						|| (loaiGd.equals("RTM") && (i.getMaGd().startsWith("07") || i.getMaGd().startsWith("27"))) 
						|| (loaiGd.equals("MSFF") && i.getMaGd().startsWith("06") && i.getMerchantCity().contains("Visa Direct")) 
				).collect(Collectors.toList());
				break;
			case "MD" : case "MC":
				doisoatList = doisoatList.stream().
				filter(i -> (loaiGd.equals("TTHH") && (i.getMaGd().startsWith("00") || i.getMaGd().startsWith("18") || i.getMaGd().startsWith("20"))) 
						|| (loaiGd.equals("RTM") && (i.getMaGd().startsWith("01") || i.getMaGd().startsWith("12"))) 
						|| (loaiGd.equals("MSFF") && i.getMaGd().startsWith("28")) 
				).collect(Collectors.toList());
				break;
		}
		
		TyGiaTqt tygiaTQT = tyGiaTqtService.findTyGiaTqtByNgayAdvAndCardType(ngayADV, cardType);
		for(DoiSoatData item : doisoatList) {
			if(item.getLttqt().equals("USD"))
			{
				if(item.getCardBrn().equals("MC")) {
					if(((item.getMaGd().startsWith("20")) || (item.getMaGd().startsWith("00") || item.getMaGd().startsWith("18"))))
						item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdTthh());
					if(item.getMaGd().startsWith("01"))
						item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdRtm());
					if(item.getMaGd().startsWith("28"))
						item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdMsff());
				} else
				{
					if((item.getMaGd().startsWith("00") && !item.getReversalInd().equals("R") && item.getStatusCw().equals(" "))
							|| (item.getMaGd().startsWith("00") && item.getReversalInd().equals("R") && !item.getStatusCw().equals(" "))
							|| (item.getMaGd().startsWith("00") && !item.getReversalInd().equals("R") && !item.getStatusCw().equals(" "))	
							|| (item.getMaGd().startsWith("18") && item.getStatusCw().equals(" "))
							|| (item.getMaGd().startsWith("20") && item.getStatusCw().equals("C")))
						item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdTthh());
						if(((item.getMaGd().startsWith("01") || item.getMaGd().startsWith("12")) && item.getStatusCw().equals(" ") && !item.getReversalInd().equals("R"))
							|| ((item.getMaGd().startsWith("01") || item.getMaGd().startsWith("12")) && !item.getStatusCw().equals(" ")))
							item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdRtm());
						if(item.getMaGd().startsWith("28") && item.getStatusCw().equals(" "))
							item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdMsff());
						if(item.getCardBrn().equals("MD") && ((item.getMaGd().startsWith("20") && item.getStatusCw().equals(" ")) || (item.getMaGd().startsWith("00") && item.getReversalInd().equals("R") && item.getStatusCw().equals(" "))) )
							item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdHttthh());
						if(item.getCardBrn().equals("MD") && item.getMaGd().startsWith("01") && item.getReversalInd().equals("R") && item.getStatusCw().equals(" "))
							item.setTyGiaTrichNo(tygiaTQT.getTyGiaGdHtrtm());
				}
				
			}
			else
				item.setTyGiaTrichNo(new BigDecimal(1));
		}
		
		
		List<DsqtCurrency> currList = new ArrayList<DsqtCurrency>();
		currList = dsqtCurrencyService.findAll();
		for(DsqtCurrency s : currList) {
			doisoatList.forEach( elem -> {
			    if(elem.getLtgd().equals(s.getCurrCode()))
			    	elem.setLtgd(s.getCurrNum());
			});
		}
		
		
	    result = new PageImpl<>(doisoatList);
		return result;
	}
	
	@Override
	public void eventReload() {
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
	                LOGGER.error(e.getMessage());
	            }
	              return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
	}
	
	public boolean checkValidator() {
		try {
			dfNgayAdv.validate();
			cbbCardType.validate();
			cbbLoaiGD.validate();
			return true;
		} catch (InvalidValueException ex) {
			dfNgayAdv.setValidationVisible(true);
			cbbCardType.setValidationVisible(true);
			cbbLoaiGD.setValidationVisible(true);
		}
		return false;
	}
}
