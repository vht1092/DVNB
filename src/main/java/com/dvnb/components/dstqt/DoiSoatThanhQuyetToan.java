package com.dvnb.components.dstqt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.money.Monetary;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.DsqtCurrencyService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.sass.internal.util.StringUtil;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class DoiSoatThanhQuyetToan extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DoiSoatThanhQuyetToan.class);
	private SpringConfigurationValueHelper configurationHelper;
	private final transient DoiSoatDataService doiSoatDataService;
	private final transient DsqtCurrencyService dsqtCurrencyService;
	public static final String CAPTION = "ĐỐI SOÁT THANH QUYẾT TOÁN";
	private DataGridDoiSoatTQTComponent grid;
	private static final String CARD_TYPE = "LOẠI THẺ";
	private static final String LOAI_TIEN = "LOẠI TIỀN TQT";
	private static final String VIEW = "VIEW";
	private static final String IMPORT = "IMPORT";
	private static final String REMOVE = "REMOVE";
	private static final String DOI_CHIEU = "ĐỐI CHIẾU";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String ERROR_MESSAGE = "Lỗi ứng dụng";
	
	private transient String sUserId;
	public ComboBox cbbCardType;
	private ComboBox cbbLoaiTienTQT;
	private final ComboBox cbbLoaiGD;
	public DateField dfAdvDate;
	private DateField dfFromIncomingDate;
	private DateField dfToIncomingDate;
	private final OptionGroup optgrpIncomingFile;
	
	final Button btView = new Button(VIEW);
	final Button btImport = new Button(IMPORT);
	final Button btRemove = new Button(REMOVE);
	
	final Button btDoiChieu = new Button(DOI_CHIEU);
	private Window window;
	
	// Paging
	private final static int SIZE_OF_PAGE = 1000000;
	private final static int FIRST_OF_PAGE = 0;
//	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	String fileNameImport;
	
	List<DoiSoatData> doiSoatDataList = new ArrayList<DoiSoatData>();
	private transient Page<DoiSoatData> result;
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	private int id;
	
	public DoiSoatThanhQuyetToan() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		
		final HorizontalLayout formLayout1a= new HorizontalLayout();
		formLayout1a.setSpacing(true);
		final VerticalLayout formLayout1b= new VerticalLayout();
		formLayout1b.setSpacing(true);
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		final HorizontalLayout formLayout3rd = new HorizontalLayout();
		formLayout3rd.setSpacing(true);
		formLayout3rd.setMargin(new MarginInfo(true, false, false, false));
		
		window = new Window();
		window.setWidth(90f, Unit.PERCENTAGE);
		window.setHeight(70f, Unit.PERCENTAGE);
		window.center();
		window.setModal(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		dsqtCurrencyService = (DsqtCurrencyService) helper.getBean("dsqtCurrencyService");
		this.sUserId = SecurityUtils.getUserId();
		
		Label lbCardType = new Label(CARD_TYPE);
		lbCardType.setWidth(113.0f, Unit.PIXELS);
		cbbCardType = new ComboBox();
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD","VS","VSD");
		cbbCardType.setValue("VSD");
		configurationHelper.setCardtype(cbbCardType==null ? "" : cbbCardType.getValue().toString());
		cbbCardType.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbCardType.setValidationVisible(false);
		cbbCardType.addValueChangeListener(event -> {
			configurationHelper.setCardtype(cbbCardType.isEmpty() ? "" : cbbCardType.getValue().toString());
		});
		
		grid = new DataGridDoiSoatTQTComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final Label lbAdvDate = new Label("NGÀY ADV");
		lbAdvDate.setWidth(105.0f, Unit.PIXELS);
		dfAdvDate = new DateField();
		dfAdvDate.setDateFormat("dd/MM/yyyy");
		dfAdvDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfAdvDate.setValidationVisible(false);
		dfAdvDate.addValueChangeListener(event -> {
			String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
			configurationHelper.setNgayAdv(advDate);
		});
		
		final Label lbFromIncomingDate = new Label("Từ ngày Incoming");
		dfFromIncomingDate = new DateField();
		dfFromIncomingDate.setDateFormat("dd/MM/yyyy");
		dfFromIncomingDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfFromIncomingDate.setValidationVisible(false);
		
		final Label lbToIncomingDate = new Label("Đến ngày Incoming");
//		lbToIncomingDate.setWidth(113.0f, Unit.PIXELS);
		dfToIncomingDate = new DateField();
		dfToIncomingDate.setDateFormat("dd/MM/yyyy");
		dfToIncomingDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfToIncomingDate.setValidationVisible(false);
		
		
		final Label lbFileIncoming = new Label("File incoming");
		lbFileIncoming.setWidth(105.0f, Unit.PIXELS);
		
		btImport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btImport.setWidth(120.0f, Unit.PIXELS);
		btImport.setIcon(FontAwesome.UPLOAD);
		btImport.setDescription("Import từ dữ liệu có sẵn trong database.");
		
		btRemove.setStyleName(ValoTheme.BUTTON_DANGER);
		btRemove.setWidth(100.0f, Unit.PIXELS);
		btRemove.setIcon(FontAwesome.REMOVE);
		btRemove.setDescription("Xóa dữ liệu theo Ngày ADV, Loại thẻ, Loại tiền TQT. Loại thẻ/Loại tiền TQT nếu không chọn hoặc chọn 'All' thì xem như đã chọn tất cả");
		
		optgrpIncomingFile = new OptionGroup();
		optgrpIncomingFile.setSizeFull();
		optgrpIncomingFile.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		optgrpIncomingFile.setMultiSelect(true);
		
		dfToIncomingDate.addValueChangeListener(event -> {
			showListIncomingFileName(dfFromIncomingDate,dfToIncomingDate);
		});
		
//		dfFromIncomingDate.addValueChangeListener(event -> {
//			showListIncomingFileName(dfFromIncomingDate,dfToIncomingDate);
//		});
//		
		
		//VS/VSD: Ngay incoming file lấy theo ngày PST_DT trong VIEW 
		btImport.addClickListener(event -> {
			try {
				if(!checkValidatorView()) {
					return;
				}
				String cardtype = cbbCardType.getValue().toString();
				String[] arrIncomFile = optgrpIncomingFile.getValue().toString().equals("[]") ? new String[]{"All"} : optgrpIncomingFile.getValue().toString().replace("[", "").replace("]", "").replaceAll("\\s+","").split(",");
				LOGGER.info("List imcoming file import ADV " + dfAdvDate.getValue() + ": "  + Arrays.toString(arrIncomFile));
				Set<String> setIncomFile = new HashSet<>();
				Collections.addAll(setIncomFile, arrIncomFile); 
				
				List<DoiSoatData> doisoatList = new ArrayList<DoiSoatData>();
				if(cardtype.startsWith("M")) 
					doisoatList.addAll(doiSoatDataService.findAllMasterByFileIncomName(setIncomFile));
				else
					doisoatList.addAll(doiSoatDataService.findAllVisaByFileIncomName(setIncomFile));
				
				LOGGER.info("Tong so giao dich import: " + doisoatList.size());
				id = 0;
				
				String binVS = doiSoatDataService.findBinByCardtype("VS", 8);
				String binVSD = doiSoatDataService.findBinByCardtype("VS", 2);
				String binMC = doiSoatDataService.findBinByCardtype("MC", 8);
				String binMD = doiSoatDataService.findBinByCardtype("MC", 2);
				
				for(DoiSoatData doiSoatData : doisoatList) {
					String binCardType="";
					String cardType = cbbCardType.getValue().toString();
					
					for (Field field : doiSoatData.getClass().getDeclaredFields()) {
					    field.setAccessible(true);
					    String name = field.getName();
					    Object value = field.get(doiSoatData);
					    LOGGER.info(name + ": " + value);
					}
					
					if(cardType.startsWith("M")) {
						if(!doiSoatData.getLtgd().isEmpty())
							doiSoatData.setLtgd(dsqtCurrencyService.findOneByCurrNum(doiSoatData.getLtgd()).getCurrCode());
//						if(!doiSoatData.getLttqt().isEmpty())
//							doiSoatData.setLttqt(dsqtCurrencyService.findOneByCurrNum(doiSoatData.getLttqt()).getCurrCode());
						
						if(doiSoatData.getMaGd().matches("(190000|290000)") && StringUtils.isEmpty(doiSoatData.getSoThe())) {
							binCardType = "MC";
						}
						else {
							if(StringUtils.isEmpty(doiSoatData.getSoThe())) {
								continue;
							} else {
								doiSoatData.setPan(doiSoatDataService.convertPanByCardno(doiSoatData.getSoThe()));
							}
						}
						
					} else {
						if(StringUtils.isEmpty(doiSoatData.getSoThe())) {
							continue;
						} else {
							doiSoatData.setPan(doiSoatDataService.convertPanByCardno(doiSoatData.getSoThe()));
						}
					}
					
					if(StringUtils.isNotEmpty(doiSoatData.getSoThe())) {
						if(doiSoatData.getSoThe().matches("(" + binMD + ").*"))
		    				binCardType = "MD";
		    			if(doiSoatData.getSoThe().matches("(" + binVSD + ").*"))
		    				binCardType = "VSD";
		    			if(doiSoatData.getSoThe().matches("(" + binVS + ").*"))
		    				binCardType = "VS";
		    			if(doiSoatData.getSoThe().matches("(" + binMC + ").*")) { 
		    				binCardType = "MC";
		    			}
					}
						
					importDataDoiSoat(doiSoatData,binCardType);
				}
				
//				doisoatList.forEach(doiSoatData -> {
////					if(doiSoatData.getSoThe()==null) {
////						return;
////					}
//					String binCardType="";
//					
//					if(doiSoatData.getSoThe().startsWith("M")) {
//						if(!doiSoatData.getLtgd().isEmpty())
//							doiSoatData.setLtgd(dsqtCurrencyService.findOneByCurrNum(doiSoatData.getLtgd()).getCurrCode());
//						if(!doiSoatData.getLttqt().isEmpty())
//							doiSoatData.setLtgd(dsqtCurrencyService.findOneByCurrNum(doiSoatData.getLttqt()).getCurrCode());
//						
//						if(doiSoatData.getMaGd().matches("(190000|290000)") && doiSoatData.getSoThe().isEmpty()) {
//							binCardType = "MC";
//						}
//						else {
//							if(doiSoatData.getSoThe().isEmpty()) {
////								continue;
//							} else {
//								doiSoatData.setPan(doiSoatDataService.convertPanByCardno(doiSoatData.getSoThe()));
//							}
//						}
//						
//					} else {
//						if(doiSoatData.getSoThe().isEmpty()) {
////							continue;
//						} else {
//							doiSoatData.setPan(doiSoatDataService.convertPanByCardno(doiSoatData.getSoThe()));
//						}
//					}
//					
//					importDataDoiSoat(doiSoatData,binCardType);
//					
//					
//					
//				});
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				LOGGER.error("Error: " + e.getMessage());
			}
			
			
		});
		
		btRemove.addClickListener(event -> {
			if(!dfAdvDate.isEmpty()) {
				String ngayAdv = timeConverter.convertDatetime(dfAdvDate.getValue());
				String lttqt = cbbLoaiTienTQT.getValue().toString();
				String cardbrn = cbbCardType.isEmpty() ? "All" : cbbCardType.getValue().toString();
				
				int countDel = doiSoatDataService.deleteAllByNgayAdvAndLttqtAndCardbrn(ngayAdv, lttqt, cardbrn);
				Notification.show("Thông báo", sUserId.toUpperCase() + " Đã xóa " + countDel + " dòng ADV" + ngayAdv + "_" + cardbrn + "_" + lttqt, Type.WARNING_MESSAGE);
				LOGGER.info(sUserId.toUpperCase() + " Đã xóa " + countDel + " dòng ADV" + ngayAdv + "_" + cardbrn + "_" + lttqt);
			}
			
				
		});
		
		final Label lbLoaiTienTQT = new Label(LOAI_TIEN);
		cbbLoaiTienTQT = new ComboBox();
		cbbLoaiTienTQT.setNullSelectionAllowed(false);
		cbbLoaiTienTQT.addItems("All","VND","USD");
		cbbLoaiTienTQT.setValue("All");
		cbbLoaiTienTQT.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbLoaiTienTQT.setValidationVisible(false);
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(120.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		btView.setDescription("Truy vấn dữ liệu theo Ngày ADV, Loại thẻ, Loại tiền TQT");
		
		btDoiChieu.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btDoiChieu.setWidth(120.0f, Unit.PIXELS);
		btDoiChieu.setIcon(FontAwesome.ARCHIVE);
		btDoiChieu.addClickListener(eventClickBTDoiChieu());
		btDoiChieu.setDescription("Đối với USD, cập nhật tỷ giá TQT trước khi Đối chiếu (nếu không sẽ hiển thị thông báo lỗi).");
		
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setNullSelectionAllowed(false);
		cbbLoaiGD.addItems("All","GDTTHH","GDRTIM","GDMSFF","HTGDTT","HTGDRT","GDBCBT","GDBNBT");
		cbbLoaiGD.setItemCaption("GDTTHH", "GD thanh toán hàng hóa");
		cbbLoaiGD.setItemCaption("GDRTIM","GD rút tiền mặt");
		cbbLoaiGD.setItemCaption("GDMSFF","GD <MONEYSEND/FASTFUND>");
		cbbLoaiGD.setItemCaption("HTGDTT","Hoàn trả GD thanh toán hàng hóa");
		cbbLoaiGD.setItemCaption("HTGDRT","Hoàn trả GD rút tiền mặt ");
		cbbLoaiGD.setItemCaption("GDBCBT","GD báo có bất thường");
		cbbLoaiGD.setItemCaption("GDBNBT","GD báo nợ bất thường");
		cbbLoaiGD.setValue("All");
		
		Upload chooseFile = new Upload(null, new Upload.Receiver() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream outputFile = null;
				try {
					if(!checkValidatorView()) {
						return outputFile;
					}
					// TODO Auto-generated method stub
					fileNameImport = StringUtils.substringBefore(filename, ".xlsx") + "_" + timeConverter.getCurrentTime() + ".xlsx";
					
					Window confirmDialog = new Window();
					final FormLayout content = new FormLayout();
		            content.setMargin(true);
		            
		            Button bYes = new Button("OK");
					
					confirmDialog.setCaption("Dữ liệu sẽ import, vui lòng đợi trong quá trình xử lý");
					confirmDialog.setWidth(350.0f, Unit.PIXELS);
			        try {
			        	if(!filename.isEmpty()) {
			        		fileImport = new File(configurationHelper.getPathFileRoot() + "/"+ fileNameImport);
				            if(!fileImport.exists()) {
				            	fileImport.createNewFile();
				            }
							outputFile =  new FileOutputStream(fileImport);
			        	
							bYes.addClickListener(event -> {
//					        	SHOW DATA IN GRID
								grid.dataSource = null;
								try 
								{
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
							    	id = 0;
							    	String binVS = doiSoatDataService.findBinByCardtype("VS", 8);
									String binVSD = doiSoatDataService.findBinByCardtype("VS", 2);
									String binMC = doiSoatDataService.findBinByCardtype("MC", 8);
									String binMD = doiSoatDataService.findBinByCardtype("MC", 2);
									
							    	for (Row row : sheet) {
							    		String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
							    		String cardno = "";
							    		String lttqt = "";
							    		String loaiTienTQT = cbbLoaiTienTQT.getValue().toString();
							    		String cardType = cbbCardType.getValue().toString();
							    		String binCardType = "";
							    		String maGd = "";
							    		String apvcode = "";
							    		String ngayGd = "";
							    		BigDecimal stGd = BigDecimal.ZERO;
							    		BigDecimal stTqt = BigDecimal.ZERO;
							    		BigDecimal stQdVnd = BigDecimal.ZERO;
							    		BigDecimal interchange = BigDecimal.ZERO;
							    		String aditionalData = "";
							    		String ngayFileIncoming = "";
							    		String ltgd = "";
							    		String ltgdCode = "";
							    		String lttqtCode = "";
							    		String dvcnt = "";
							    		String issuerCharge = "";
							    		String merchantCity = "";
							    		String mcc = "";
							    		String reversalInd = "";
//							    		row.getCell(18)==null ? "" : row.getCell(18).getStringCellValue()
							    		String pan = "";
							    		List<Object[]> doisoatDataTemp;
							    		DoiSoatData doiSoatData;
							    		DoiSoatData doiSoatDataCheck = null;
							    		
							    		if(row.getRowNum()>0)
							    		{
							    			if(cardType.startsWith("M")) {
							    				maGd = isCellEmpty(row.getCell(2)) ? "" : row.getCell(2).getStringCellValue();
							    				apvcode = isCellEmpty(row.getCell(24)) ? "" : row.getCell(24).getStringCellValue().replaceAll("[\\s|\\u00A0]+", "");
							    				ngayGd = isCellEmpty(row.getCell(12)) ? "" : row.getCell(12).getStringCellValue().substring(0, 6);
							    				stGd = isCellEmpty(row.getCell(3)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(3).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
								    			stTqt = isCellEmpty(row.getCell(4)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(4).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
								    			stQdVnd = isCellEmpty(row.getCell(5)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(5).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
								    			interchange = isCellEmpty(row.getCell(44)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(44).getStringCellValue().replace(",", ""));
								    			aditionalData = isCellEmpty(row.getCell(29)) ? "" : row.getCell(29).getStringCellValue().toUpperCase();
								    			ngayFileIncoming = isCellEmpty(row.getCell(0)) ? "" : row.getCell(0).getStringCellValue().substring(row.getCell(0).getStringCellValue().lastIndexOf("TT112T0.")+8, row.getCell(0).getStringCellValue().lastIndexOf("TT112T0.")+27);
								    			
								    			ltgd = isCellEmpty(row.getCell(6)) ? "" : String.format("%03d", Integer.parseInt(row.getCell(6).getStringCellValue()));
								    			if(!ltgd.isEmpty())
								    				ltgdCode = dsqtCurrencyService.findOneByCurrNum(ltgd.trim()).getCurrCode();
								    			
								    			lttqt = isCellEmpty(row.getCell(7)) ? "" : String.format("%03d", Integer.parseInt(row.getCell(7).getStringCellValue()));
								    			if(!lttqt.isEmpty())
								    				lttqtCode = dsqtCurrencyService.findOneByCurrNum(lttqt.trim()).getCurrCode();
								    			
								    			dvcnt = isCellEmpty(row.getCell(28)) ? "" : row.getCell(28).getStringCellValue();
								    			mcc = isCellEmpty(row.getCell(18)) ? "" : row.getCell(18).getStringCellValue();
								    			
							    				if(maGd.matches("(190000|290000)") && isCellEmpty(row.getCell(1))) {
							    					binCardType = "MC";
							    				}
							    				else {
							    					if(isCellEmpty(row.getCell(1))) {
								    					continue;
								    				} else {
								    					cardno = row.getCell(1).getStringCellValue();
								    					pan = doiSoatDataService.convertPanByCardno(cardno);
								    				}
								    					
							    				}
							    				
							    				
							    			} else {
							    				maGd = isCellEmpty(row.getCell(0)) ? "" : row.getCell(0).getStringCellValue();
							    				lttqt = isCellEmpty(row.getCell(27)) ? "" : row.getCell(27).getStringCellValue();
							    				apvcode = isCellEmpty(row.getCell(43)) ? "" : row.getCell(43).getStringCellValue().replaceAll("[\\s|\\u00A0]+", "");
							    				stGd = isCellEmpty(row.getCell(12)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(12).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
								    			stQdVnd = isCellEmpty(row.getCell(10)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(10).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
								    			interchange = isCellEmpty(row.getCell(28)) ? BigDecimal.ZERO : new BigDecimal(row.getCell(28).getStringCellValue().replace(",", ""));
								    			ltgd = isCellEmpty(row.getCell(13)) ? "" : row.getCell(13).getStringCellValue();
								    			issuerCharge = isCellEmpty(row.getCell(49)) ? "" : row.getCell(49).getStringCellValue();
								    			ngayFileIncoming = filename.substring(filename.lastIndexOf("INCTF.")+6, filename.lastIndexOf("INCTF.")+20);
								    			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
								    			Date ngayFileIncomingConvers = sdf.parse(ngayFileIncoming);
								    			sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
								    			ngayFileIncoming = sdf.format(ngayFileIncomingConvers);
								    			ngayGd = isCellEmpty(row.getCell(38)) ? "" : ngayFileIncoming.substring(2, 4) + String.format("%04d",Integer.valueOf(row.getCell(38).getStringCellValue()));
								    			LOGGER.info("ngayFileIncoming: " + ngayFileIncoming);
								    			LOGGER.info("ngaGD: " + ngayGd);
								    			merchantCity = isCellEmpty(row.getCell(22)) ? "" : row.getCell(22).getStringCellValue();
								    			dvcnt = isCellEmpty(row.getCell(21)) ? "" : row.getCell(21).getStringCellValue();
								    			mcc = isCellEmpty(row.getCell(24)) ? "" : row.getCell(24).getStringCellValue();
								    			
							    				if(isCellEmpty(row.getCell(8))) {
							    					continue;
							    				} else {
							    					cardno = row.getCell(8).getStringCellValue();
							    					pan = doiSoatDataService.convertPanByCardno(cardno);
							    				}
							    					
							    			}
							    			
//							    			if(cardno.matches("(550796|547139|524188).*"))
//							    				binCardType = "MD";
//							    			if(cardno.startsWith("453618"))
//							    				binCardType = "VSD";
//							    			if(cardno.matches("(489516|489517|489518).*"))
//							    				binCardType = "VS";
//							    			if(cardno.matches("(510235|512454|554627|545579).*")) { 
//							    				binCardType = "MC";
//							    			}
							    			
							    			if(StringUtils.isNotEmpty(cardno)) {
												if(cardno.matches("(" + binMD + ").*"))
								    				binCardType = "MD";
								    			if(cardno.matches("(" + binVSD + ").*"))
								    				binCardType = "VSD";
								    			if(cardno.matches("(" + binVS + ").*"))
								    				binCardType = "VS";
								    			if(cardno.matches("(" + binMC + ").*")) { 
								    				binCardType = "MC";
								    			}
											}
							    			
							    			switch(binCardType) {
							    				case "MD":
//							    					cardno = row.getCell(1)==null ? "" : row.getCell(8).getStringCellValue();
//							    					if(cardno.matches("(550796|547139|524188).*")){
							    					if(loaiTienTQT.equals("All") || loaiTienTQT.equals(lttqtCode)){
							    						id++;
											    		
										    			LOGGER.info("STT: " + id + ", Rownum: " + row.getRowNum() + ", cardno: "+ cardno + ", apvcode: " + apvcode + ", stGd: " + stGd);
										    			
										    			if(StringUtils.containsIgnoreCase(aditionalData, "25007R")) {
										    				reversalInd = "R";
										    			} else
										    				reversalInd = " ";
										    			
										    			doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(cardno, apvcode,maGd,reversalInd,advDate);
										    			doisoatDataTemp = doiSoatDataService.findDoiSoatInfoByPanAndApvCode(pan,apvcode,ngayGd);
										    			
										    			doiSoatData = new DoiSoatData();
										    			doiSoatData.setCardBrn("MD");
										    			doiSoatData.setId(timeConverter.getCurrentTime() + String.format("%07d", id));
										    			doiSoatData.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
										    			doiSoatData.setUsrId(sUserId);
										    			doiSoatData.setNgayAdv(advDate);
										    			doiSoatData.setSoThe(cardno);
										    			doiSoatData.setMaGd(maGd);
										    			doiSoatData.setNgayGd(ngayGd);
										    			doiSoatData.setNgayFileIncoming(ngayFileIncoming);
										    			doiSoatData.setLtgd(ltgdCode);
										    			doiSoatData.setLttqt(lttqtCode);
										    			doiSoatData.setMaCapPhep(apvcode);
										    			doiSoatData.setDvcnt(dvcnt);
										    			doiSoatData.setReversalInd(reversalInd);
										    			doiSoatData.setPan(pan);
										    			
										    			if(doisoatDataTemp.size()>0)
										    			{
											    			for (Object[] b : doisoatDataTemp) {
				
										    					doiSoatData.setLoaiTienNguyenTeGd(b[12]==null ? " " : b[12].toString());
										    					
										    					doiSoatData.setTenChuThe(b[1]==null ? " " : b[1].toString());
										    					
//										    					doiSoatData.setTenChuTk(b[16]==null ? " " : b[16].toString());
										    						
										    					doiSoatData.setCasa(b[11]==null ? " " : b[11].toString());
										    					
										    					String accountDesc = doiSoatDataService.findAccountDescription(doiSoatData.getCasa());
										    					doiSoatData.setTenChuTk(accountDesc);
										    					
										    					doiSoatData.setStatusCw(b[9]==null ? " " : b[9].toString());
										    					
										    					doiSoatData.setDvpht(b[8]==null ? " " : b[8].toString());
										    						
										    					doiSoatData.setCif(b[14]==null ? " " : b[14].toString());
										    					
										    					doiSoatData.setCrdpgm(b[15]==null ? " " : b[15].toString());
										    					
										    					doiSoatData.setTrace(b[10]==null ? " " : b[10].toString());
										    					
									    						doiSoatData.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")) : BigDecimal.ZERO);
										    					doiSoatData.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")) : BigDecimal.ZERO);
										    					doiSoatData.setPhiRtmGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
										    					doiSoatData.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
										    					
										    					if(ltgdCode.equals("IDR")) {
										    						doiSoatData.setStgdNguyenTeGd(doiSoatData.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
										    					}
										    				}
										    			}
										    			else {
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
									    					doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
									    					doiSoatData.setPhiRtmGd(BigDecimal.ZERO);
									    					doiSoatData.setPhiIsaGd(BigDecimal.ZERO);
										    			}
										    			
										    			if(maGd.startsWith("28") && !reversalInd.equals("R") && doiSoatData.getStatusCw().equals(" "))
										    			{
										    				stGd = stGd.negate();
										    				stTqt = stTqt.negate();
										    				stQdVnd = stQdVnd.negate();
										    			}
										    			
										    			if(maGd.startsWith("01") & reversalInd.equals("R") && doiSoatData.getStatusCw().equals("C")
//											    			|| maGd.startsWith("00") & !reversalInd.equals("R") && doiSoatData.getStatusCw().equals("C")
								    					|| (maGd.startsWith("00") && reversalInd.equals("R") && doiSoatData.getStatusCw().equals(" "))
								    					|| (maGd.startsWith("00") && reversalInd.equals("R") && doiSoatData.getStatusCw().equals("C"))
								    					|| (maGd.startsWith("20") && doiSoatData.getStatusCw().equals("C"))
										    			|| (maGd.startsWith("20") && !reversalInd.equals("R") && doiSoatData.getStatusCw().equals(" "))
										    			|| (maGd.startsWith("01") && reversalInd.equals("R") && doiSoatData.getStatusCw().equals(" "))) {
										    				stGd = stGd.negate();
										    				stTqt = stTqt.negate();
										    				stQdVnd = stQdVnd.negate();
										    				doiSoatData.setStTrichNoKhGd(doiSoatData.getStTrichNoKhGd().negate());
									    					doiSoatData.setStgdNguyenTeGd(doiSoatData.getStgdNguyenTeGd().negate());
										    			}
										    			
										    			
										    			if(maGd.startsWith("00") & !reversalInd.equals("R") && doiSoatData.getStatusCw().equals("C")) {
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
									    					doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
										    			}
									    				doiSoatData.setStGd(stGd);
										    			doiSoatData.setStTqt(stTqt);
										    			doiSoatData.setStQdVnd(stQdVnd);
										    			
										    			doiSoatData.setInterchange(interchange);
										    			doiSoatData.setStgdNguyenTeChenhLech(stGd.subtract(doiSoatData.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatData.getStgdNguyenTeGd()));
									    				doiSoatData.setStgdChenhLechDoTyGia(stQdVnd.subtract(doiSoatData.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatData.getStTrichNoKhGd()));
									    				
										    			if(!doiSoatData.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
									    					doiSoatData.setTyGiaTrichNo(doiSoatData.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatData.getStTrichNoKhGd().divide(doiSoatData.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP));
									    				else
									    					doiSoatData.setTyGiaTrichNo(BigDecimal.ZERO);
									    				doiSoatData.setSoTienGdHoanTraTruyThu(doiSoatData.getStgdNguyenTeChenhLech().multiply(doiSoatData.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
									    				
									    				if(!doiSoatData.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
									    				{
									    					doiSoatData.setPhiIsaHoanTraTruyThu(doiSoatData.getStgdNguyenTeChenhLech().multiply(doiSoatData.getPhiIsaGd()).divide(doiSoatData.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
									    				}
									    				else 
									    					doiSoatData.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
									    				
									    				doiSoatData.setVatPhiIsaHoanTraTruyThu(doiSoatData.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
									    				
									    				if(!doiSoatData.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
									    					doiSoatData.setPhiRtmHoanTraTruyThu(doiSoatData.getStgdNguyenTeChenhLech().multiply(doiSoatData.getPhiRtmGd()).divide(doiSoatData.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
									    				else 
									    					doiSoatData.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
									    				
									    				doiSoatData.setVatPhiRtmHoanTraTruyThu(doiSoatData.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
									    				doiSoatData.setTongPhiVatHoanTraTruyThu(doiSoatData.getPhiIsaHoanTraTruyThu().add(doiSoatData.getVatPhiIsaHoanTraTruyThu()).add(doiSoatData.getPhiRtmHoanTraTruyThu()).add(doiSoatData.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
									    				doiSoatData.setTongHoanTraTruyThu(doiSoatData.getSoTienGdHoanTraTruyThu().add(doiSoatData.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
									    				
										    			doiSoatData.setIssuerCharge(" "); //Value with VS
										    			doiSoatData.setPhiXuLyGd(BigDecimal.ZERO); //Value with VS
		//								    			doiSoatData.setMerchantCity(); //Value with VS
										    			doiSoatData.setMcc(mcc);
										    			
										    			if(doiSoatDataCheck!=null && doiSoatDataCheck.getDvcnt().contains("VIETNAM AIRLINES")) {
										    				//Accumulated value
										    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(stGd));
										    				doiSoatDataCheck.setStTqt(doiSoatDataCheck.getStTqt().add(stTqt));
										    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(stQdVnd));
//											    				doiSoatDataCheck.setInterchange(doiSoatDataCheck.getInterchange().add(interchange));
										    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().subtract(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
										    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().subtract(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
//											    				doiSoatDataCheck.setSoTienGdHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
//											    				if(!doiSoatDataCheck.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
//											    					doiSoatDataCheck.setPhiIsaHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getPhiIsaGd()).divide(doiSoatDataCheck.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
//											    				else 
//											    					doiSoatDataCheck.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
//											    				doiSoatDataCheck.setVatPhiIsaHoanTraTruyThu(doiSoatDataCheck.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));	
//											    				if(!doiSoatDataCheck.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
//											    					doiSoatDataCheck.setPhiRtmHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getPhiRtmGd()).divide(doiSoatDataCheck.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
//											    				else 
//											    					doiSoatDataCheck.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
//											    				doiSoatDataCheck.setVatPhiRtmHoanTraTruyThu(doiSoatDataCheck.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
//											    				doiSoatDataCheck.setTongPhiVatHoanTraTruyThu(doiSoatDataCheck.getPhiIsaHoanTraTruyThu().add(doiSoatDataCheck.getVatPhiIsaHoanTraTruyThu()).add(doiSoatDataCheck.getPhiRtmHoanTraTruyThu()).add(doiSoatDataCheck.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
//											    				doiSoatDataCheck.setTongHoanTraTruyThu(doiSoatDataCheck.getSoTienGdHoanTraTruyThu().add(doiSoatDataCheck.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
										    				doiSoatDataService.create(doiSoatDataCheck);
										    				
										    				//Add new row
										    				doiSoatData.setStGd(BigDecimal.ZERO);
										    				doiSoatData.setStTqt(BigDecimal.ZERO);
										    				doiSoatData.setStQdVnd(BigDecimal.ZERO);
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
										    				doiSoatData.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
										    				
											    			doiSoatDataService.create(doiSoatData);
										    			} else
										    				doiSoatDataService.create(doiSoatData);
									    			
									    			}	
									    			break;
							    				case "VSD":
//							    					cardno = row.getCell(8)==null ? "" : row.getCell(8).getStringCellValue();
							    					LOGGER.info("Card no: " + cardno + ", lttqt: " + lttqt + ", loaiTienTQT: " + loaiTienTQT);
									    			if(cardno.startsWith("453618") && (loaiTienTQT.equals("All") || loaiTienTQT.equals(lttqt))) { 
									    				id++;
									    				
										    			LOGGER.info("STT: " + id + ", Rownum: " + row.getRowNum() + ", cardno: "+ cardno + ", apvcode: " + apvcode + ", stGd: " + stGd);
										    			
										    			doisoatDataTemp = doiSoatDataService.findDoiSoatInfoByPanAndApvCode(pan,apvcode,ngayGd);
										    			
										    			doiSoatData = new DoiSoatData();
										    			doiSoatData.setCardBrn("VSD");
										    			doiSoatData.setId(timeConverter.getCurrentTime() + String.format("%07d", id));
										    			doiSoatData.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
										    			doiSoatData.setUsrId(sUserId);
										    			doiSoatData.setNgayAdv(advDate);
										    			doiSoatData.setSoThe(cardno);
										    			doiSoatData.setMaGd(maGd);
										    			doiSoatData.setNgayGd(ngayGd);
										    			doiSoatData.setNgayFileIncoming(ngayFileIncoming);
										    			doiSoatData.setLtgd(ltgd);
										    			doiSoatData.setLttqt(lttqt);
										    			doiSoatData.setMaCapPhep(apvcode);
										    			doiSoatData.setDvcnt(dvcnt);
										    			doiSoatData.setReversalInd(" ");
										    			doiSoatData.setPan(pan);
										    			doiSoatData.setIssuerCharge(issuerCharge); //Value with VS
										    			if(issuerCharge.equals("S"))
										    				doiSoatData.setPhiXuLyGd(stQdVnd.divide(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP)); //Value with VS
										    			else 
										    				doiSoatData.setPhiXuLyGd(BigDecimal.ZERO);
										    			doiSoatData.setMerchantCity(merchantCity); //Value with VS
										    			doiSoatData.setMcc(mcc);
										    			
										    			
										    			if(doisoatDataTemp.size()>0)
										    			{
											    			for (Object[] b : doisoatDataTemp) {
				
										    					doiSoatData.setLoaiTienNguyenTeGd(b[12]==null ? " " : b[12].toString());
										    					
										    					doiSoatData.setTenChuThe(b[1]==null ? " " : b[1].toString());
										    					
//										    					doiSoatData.setTenChuTk(b[16]==null ? " " : b[16].toString());
										    						
										    					doiSoatData.setCasa(b[11]==null ? " " : b[11].toString());
										    					
										    					String accountDesc = doiSoatDataService.findAccountDescription(doiSoatData.getCasa());
										    					doiSoatData.setTenChuTk(accountDesc);
										    						
										    					doiSoatData.setStatusCw(b[9]==null ? " " : b[9].toString());
										    					
										    					doiSoatData.setDvpht(b[8]==null ? " " : b[8].toString());
										    						
										    					doiSoatData.setCif(b[14]==null ? " " : b[14].toString());
										    					
										    					doiSoatData.setCrdpgm(b[15]==null ? " " : b[15].toString());
										    					
										    					doiSoatData.setTrace(b[10]==null ? " " : b[10].toString());
										    					
									    						doiSoatData.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")) : BigDecimal.ZERO);
										    					doiSoatData.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")) : BigDecimal.ZERO);
										    					doiSoatData.setPhiRtmGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
										    					doiSoatData.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
										    					
										    					if(ltgd.equals("IDR")) {
										    						doiSoatData.setStgdNguyenTeGd(doiSoatData.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
										    					}
										    				}
										    			}
										    			else {
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
									    					doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
									    					doiSoatData.setPhiRtmGd(BigDecimal.ZERO);
									    					doiSoatData.setPhiIsaGd(BigDecimal.ZERO);
										    			}
										    			
										    			if(maGd.startsWith("06") || maGd.startsWith("25") || maGd.startsWith("27")) {
										    				stGd = stGd.negate();
										    				stQdVnd = stQdVnd.negate();
										    				doiSoatData.setStTrichNoKhGd(doiSoatData.getStTrichNoKhGd().negate());
									    					doiSoatData.setStgdNguyenTeGd(doiSoatData.getStgdNguyenTeGd().negate());
										    			}
										    			
										    			if(maGd.startsWith("05") && doiSoatData.getStatusCw().equals("C")) {
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
									    					doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
										    			}
										    			
									    				doiSoatData.setStGd(stGd);
										    			doiSoatData.setStTqt(BigDecimal.ZERO);
										    			doiSoatData.setStQdVnd(stQdVnd);
										    			
										    			doiSoatData.setInterchange(interchange);
										    			doiSoatData.setStgdNguyenTeChenhLech(stGd.subtract(doiSoatData.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatData.getStgdNguyenTeGd()));
									    				doiSoatData.setStgdChenhLechDoTyGia(stQdVnd.subtract(doiSoatData.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatData.getStTrichNoKhGd()));
									    				
										    			if(!doiSoatData.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
									    					doiSoatData.setTyGiaTrichNo(doiSoatData.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatData.getStTrichNoKhGd().divide(doiSoatData.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP));
									    				else
									    					doiSoatData.setTyGiaTrichNo(BigDecimal.ZERO);
									    				doiSoatData.setSoTienGdHoanTraTruyThu(doiSoatData.getStgdNguyenTeChenhLech().multiply(doiSoatData.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
									    				
									    				if(!doiSoatData.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
									    				{
									    					doiSoatData.setPhiIsaHoanTraTruyThu(doiSoatData.getStgdNguyenTeChenhLech().multiply(doiSoatData.getPhiIsaGd()).divide(doiSoatData.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
									    				}
									    				else 
									    					doiSoatData.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
									    				
									    				doiSoatData.setVatPhiIsaHoanTraTruyThu(doiSoatData.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
									    				
									    				if(!doiSoatData.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
									    					doiSoatData.setPhiRtmHoanTraTruyThu(doiSoatData.getStgdNguyenTeChenhLech().multiply(doiSoatData.getPhiRtmGd()).divide(doiSoatData.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
									    				else 
									    					doiSoatData.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
									    				
									    				doiSoatData.setVatPhiRtmHoanTraTruyThu(doiSoatData.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
									    				doiSoatData.setTongPhiVatHoanTraTruyThu(doiSoatData.getPhiIsaHoanTraTruyThu().add(doiSoatData.getVatPhiIsaHoanTraTruyThu()).add(doiSoatData.getPhiRtmHoanTraTruyThu()).add(doiSoatData.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
									    				doiSoatData.setTongHoanTraTruyThu(doiSoatData.getSoTienGdHoanTraTruyThu().add(doiSoatData.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
									    				
									    				
									    				if(!((maGd.startsWith("25") && doiSoatData.getStatusCw().equals(" "))
							    						|| (maGd.startsWith("06") && doiSoatData.getStatusCw().equals(" ") && !doiSoatData.getMerchantCity().contains("Visa Direct"))
							    						|| (maGd.startsWith("27") && doiSoatData.getStatusCw().equals(" "))))
									    				{
									    					doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(cardno, apvcode,maGd," ",advDate);
									    				}
									    				
										    			if(doiSoatDataCheck!=null && doiSoatDataCheck.getDvcnt().contains("VIETNAM AIRLINES")) {
										    				//Accumulated value
										    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(stGd));
										    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(stQdVnd));
										    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().subtract(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
										    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().subtract(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
										    				doiSoatDataCheck.setSoTienGdHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
										    				
										    				if(!doiSoatDataCheck.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
										    				{
										    					doiSoatDataCheck.setPhiIsaHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getPhiIsaGd()).divide(doiSoatDataCheck.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
										    				}
										    				else 
										    					doiSoatDataCheck.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatDataCheck.setVatPhiIsaHoanTraTruyThu(doiSoatDataCheck.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
										    				
										    				if(!doiSoatDataCheck.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
										    					doiSoatDataCheck.setPhiRtmHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getPhiRtmGd()).divide(doiSoatDataCheck.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
										    				else 
										    					doiSoatData.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
										    				
										    				doiSoatDataCheck.setVatPhiRtmHoanTraTruyThu(doiSoatDataCheck.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
										    				doiSoatDataCheck.setTongPhiVatHoanTraTruyThu(doiSoatDataCheck.getPhiIsaHoanTraTruyThu().add(doiSoatDataCheck.getVatPhiIsaHoanTraTruyThu()).add(doiSoatDataCheck.getPhiRtmHoanTraTruyThu()).add(doiSoatDataCheck.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
										    				doiSoatDataCheck.setTongHoanTraTruyThu(doiSoatDataCheck.getSoTienGdHoanTraTruyThu().add(doiSoatDataCheck.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
										    				
										    				doiSoatDataService.create(doiSoatDataCheck);
										    				
										    				//Add new row
										    				doiSoatData.setStGd(BigDecimal.ZERO);
										    				doiSoatData.setStTqt(BigDecimal.ZERO);
										    				doiSoatData.setStQdVnd(BigDecimal.ZERO);
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
										    				doiSoatData.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
										    				doiSoatData.setSoTienGdHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatData.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatData.setVatPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatData.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatData.setVatPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatData.setTongPhiVatHoanTraTruyThu(BigDecimal.ZERO);
										    				doiSoatData.setTongHoanTraTruyThu(BigDecimal.ZERO);
										    				
											    			doiSoatDataService.create(doiSoatData);
										    			} else
										    				doiSoatDataService.create(doiSoatData);
									    			
									    			}
							    					break;
							    					
							    				case "VS":
//							    					cardno = row.getCell(8)==null ? "" : row.getCell(8).getStringCellValue();
//							    					lttqt = row.getCell(27)==null ? "" : row.getCell(27).getStringCellValue();
//							    					loaiTienTQT = cbbLoaiTienTQT.getValue().toString();
							    					LOGGER.info("Card no: " + cardno + ", lttqt: " + lttqt + ", loaiTienTQT: " + loaiTienTQT);
									    			if(cardno.matches("(489516|489517|489518).*") 				    					
									    			&& (loaiTienTQT.equals("All") || loaiTienTQT.equals(lttqt))) { 
									    				id++;
//									    				String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
//									    				String ngayGd = "";
//									    				if(row.getCell(38).getCellType() == Cell.CELL_TYPE_BLANK) 
//									    					ngayGd = row.getCell(38)==null ? "" : "20" + StringUtils.leftPad(row.getCell(38).getStringCellValue(),4,"0");
//									    				else
//									    					ngayGd = row.getCell(38).getStringCellValue().isEmpty() ? "" : "20" + String.format("%04d",Integer.valueOf(row.getCell(38).getStringCellValue()));
//									    				 
//										    			String apvcode = row.getCell(43)==null ? "" : row.getCell(43).getStringCellValue().replaceAll("[\\s|\\u00A0]+", "");
//										    			
//										    			BigDecimal stGd = row.getCell(12)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(12).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
//										    			BigDecimal stQdVnd = BigDecimal.ZERO;
//										    			if(row.getCell(10).getCellType() == Cell.CELL_TYPE_BLANK) 
//										    				stQdVnd = BigDecimal.ZERO;
//										    			else
//										    				stQdVnd = row.getCell(10)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(10).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
//										    			BigDecimal interchange = row.getCell(28)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(28).getStringCellValue().replace(",", ""));
										    			LOGGER.info("STT: " + id + ", Rownum: " + row.getRowNum() + ", cardno: "+ cardno + ", apvcode: " + apvcode + ", stGd: " + stGd + ", ngayGd: " + ngayGd);
//										    			String ltgd = row.getCell(13)==null ? "" : row.getCell(13).getStringCellValue();
//										    			String issuerCharge = row.getCell(49)==null ? "" : row.getCell(49).getStringCellValue();
//										    			String merchantCity = row.getCell(22)==null ? "" : row.getCell(22).getStringCellValue();
//										    			String ngayFileIncoming = filename.substring(filename.lastIndexOf("INCTF.")+6, filename.lastIndexOf("INCTF.")+20);
//										    			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
//										    			Date ngayFileIncomingConvers = sdf.parse(ngayFileIncoming);
//										    			sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
//										    			ngayFileIncoming = sdf.format(ngayFileIncomingConvers);
//										    			
//										    			String pan = doiSoatDataService.convertPanByCardno(cardno);
										    			
										    			doisoatDataTemp = doiSoatDataService.findDoiSoatCreditInfoByPanAndApvCode(pan, apvcode);
										    			
										    			doiSoatData = new DoiSoatData();
										    			doiSoatData.setCardBrn("VS");
										    			doiSoatData.setId(timeConverter.getCurrentTime() + String.format("%07d", id));
										    			doiSoatData.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
										    			doiSoatData.setUsrId(sUserId);
										    			doiSoatData.setNgayAdv(advDate);
										    			doiSoatData.setSoThe(cardno);
										    			doiSoatData.setMaGd(maGd);
										    			doiSoatData.setNgayGd(ngayGd);
										    			doiSoatData.setNgayFileIncoming(ngayFileIncoming);
										    			doiSoatData.setLtgd(ltgd);
										    			doiSoatData.setLttqt(lttqt);
										    			doiSoatData.setMaCapPhep(apvcode);
										    			doiSoatData.setDvcnt(dvcnt);
										    			doiSoatData.setReversalInd(" ");
										    			doiSoatData.setPan(pan);
										    			doiSoatData.setIssuerCharge(issuerCharge); //Value with VS
										    			if(issuerCharge.equals("S"))
										    				doiSoatData.setPhiXuLyGd(stQdVnd.divide(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP)); //Value with VS
										    			else 
										    				doiSoatData.setPhiXuLyGd(BigDecimal.ZERO);
										    			doiSoatData.setMerchantCity(merchantCity); //Value with VS
										    			doiSoatData.setMcc(row.getCell(24)==null ? "" : row.getCell(24).getStringCellValue());
										    			doiSoatData.setTyGiaTrichNo(BigDecimal.ZERO);
										    			doiSoatData.setSoTienGdHoanTraTruyThu(BigDecimal.ZERO);
										    			doiSoatData.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
										    			doiSoatData.setVatPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
										    			doiSoatData.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
										    			doiSoatData.setVatPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
										    			doiSoatData.setTongPhiVatHoanTraTruyThu(BigDecimal.ZERO);
										    			doiSoatData.setTongHoanTraTruyThu(BigDecimal.ZERO);
										    			
										    			if(doisoatDataTemp.size()>0)
										    			{
											    			for (Object[] b : doisoatDataTemp) {
				
											    				if((maGd.startsWith("06") && !merchantCity.contains("Visa Direct")) || maGd.startsWith("25")) {
											    					doiSoatData.setPhiRtmGd(BigDecimal.ZERO);
											    					doiSoatData.setVatPhiRtmGd(BigDecimal.ZERO);
											    					doiSoatData.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
											    					doiSoatData.setVatPhiIsaGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
										    						doiSoatData.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
											    					doiSoatData.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
											    					doiSoatData.setLoaiTienNguyenTeGd(b[5]==null ? " " : b[5].toString());
											    					if(ltgd.equals("IDR")) {
											    						doiSoatData.setStgdNguyenTeGd(doiSoatData.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
											    					}
											    				} else {
											    					doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
											    					doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
											    					doiSoatData.setLoaiTienNguyenTeGd(" ");
											    					doiSoatData.setPhiRtmGd(BigDecimal.ZERO);
											    					doiSoatData.setPhiIsaGd(BigDecimal.ZERO);
											    					doiSoatData.setVatPhiRtmGd(BigDecimal.ZERO);
											    					doiSoatData.setVatPhiIsaGd(BigDecimal.ZERO);
											    				}
										    					
										    					doiSoatData.setTenChuThe(b[1]==null ? " " : b[1].toString());
										    					doiSoatData.setTenChuTk(" ");
										    					doiSoatData.setCasa(" ");
										    					doiSoatData.setDvpht(b[10]==null ? " " : b[10].toString());
										    					doiSoatData.setCif(" ");
										    					doiSoatData.setCrdpgm(b[9]==null ? " " : b[9].toString());
									    						doiSoatData.setLoc(b[11]==null ? " " : b[11].toString());
										    					
										    					
										    				}
										    			}
										    			else {
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
									    					doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
									    					doiSoatData.setPhiRtmGd(BigDecimal.ZERO);
									    					doiSoatData.setPhiIsaGd(BigDecimal.ZERO);
										    			}
										    			
										    			if(maGd.matches("(25|06|27).*"))
									    				{
									    					stGd = stGd.negate();
										    				stQdVnd = stQdVnd.negate();
									    				}
										    			
									    				doiSoatData.setStGd(stGd);
										    			doiSoatData.setStTqt(BigDecimal.ZERO);
										    			doiSoatData.setStQdVnd(stQdVnd);
										    			
										    			doiSoatData.setInterchange(interchange);
										    			doiSoatData.setStgdNguyenTeChenhLech(stGd.add(doiSoatData.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatData.getStgdNguyenTeGd()));
									    				doiSoatData.setStgdChenhLechDoTyGia(stQdVnd.add(doiSoatData.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatData.getStTrichNoKhGd()));
									    				
									    				if(!((maGd.startsWith("25")) 
									    				|| (maGd.startsWith("06") && !doiSoatData.getMerchantCity().contains("Visa Direct"))
									    				|| (maGd.startsWith("27"))))
									    				{
									    					doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(cardno, apvcode,maGd," ",advDate);
									    				}
									    				
									    				if(doiSoatDataCheck!=null && doiSoatDataCheck.getDvcnt().contains("VIETNAM AIRLINES")) {
										    				//Accumulated value
										    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(stGd));
										    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(stQdVnd));
										    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().add(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
										    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().add(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
										    				
										    				doiSoatDataService.create(doiSoatDataCheck);
										    				
										    				//Add new row
										    				doiSoatData.setStGd(BigDecimal.ZERO);
										    				doiSoatData.setStTqt(BigDecimal.ZERO);
										    				doiSoatData.setStQdVnd(BigDecimal.ZERO);
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
										    				doiSoatData.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
										    				
											    			doiSoatDataService.create(doiSoatData);
										    			} else
										    				doiSoatDataService.create(doiSoatData);
									    			
									    			}
							    					break;
							    					
							    				case "MC":
//							    					cardno = row.getCell(1)==null ? "" : row.getCell(1).getStringCellValue();
//							    					if(cardno.matches("(510235|512454|554627|545579).*")) { 
							    					if(loaiTienTQT.equals("All") || loaiTienTQT.equals(lttqtCode)){
									    				id++;
//									    				advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
//									    				ngayGd = "";
//									    				
//									    				if(row.getCell(12) != null) {
//									    					if(row.getCell(12).getCellType() == Cell.CELL_TYPE_BLANK) 
//										    					ngayGd = row.getCell(12)==null ? "" : row.getCell(12).getStringCellValue().substring(0, 6);
//										    				else
//										    					ngayGd = row.getCell(12).getStringCellValue().isEmpty() ? "" : row.getCell(12).getStringCellValue().substring(0, 6);
//									    				}
//									    				
//										    			apvcode = row.getCell(24)==null ? "" : row.getCell(24).getStringCellValue().replaceAll("[\\s|\\u00A0]+", "");
//										    			
//										    			stGd = row.getCell(3)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(3).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);;
//										    			stTqt = row.getCell(4)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(4).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);;
//										    			stQdVnd = BigDecimal.ZERO;
//										    			if(row.getCell(5) != null) {
//											    			if(row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK) 
//											    				stQdVnd = BigDecimal.ZERO;
//											    			else
//											    				stQdVnd = row.getCell(5)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(5).getNumericCellValue()).setScale(2,RoundingMode.HALF_UP);
//										    			}
//										    			
//											    		interchange = row.getCell(44)==null ? BigDecimal.ZERO : new BigDecimal(row.getCell(44).getStringCellValue().replace(",", ""));
//										    			aditionalData = row.getCell(29).getStringCellValue().toUpperCase();
//										    			ngayFileIncoming = row.getCell(0)==null ? "" : row.getCell(0).getStringCellValue().substring(row.getCell(0).getStringCellValue().lastIndexOf("TT112T0.")+8, row.getCell(0).getStringCellValue().lastIndexOf("TT112T0.")+27);
										    			LOGGER.info("STT: " + id + ", Rownum: " + row.getRowNum() + ", cardno: "+ cardno + ", apvcode: " + apvcode + ", stGd: " + stGd);
//										    			ltgd = row.getCell(6)==null ? "" : String.format("%03d", Integer.parseInt(row.getCell(6).getStringCellValue()));
//										    			ltgdCode = dsqtCurrencyService.findOneByCurrNum(ltgd).getCurrCode();
//										    			lttqt = row.getCell(7)==null ? "" : String.format("%03d", Integer.parseInt(row.getCell(7).getStringCellValue()));
//										    			lttqtCode = dsqtCurrencyService.findOneByCurrNum(lttqt).getCurrCode();
										    			
										    			if(StringUtils.containsIgnoreCase(aditionalData, "25007R")) {
										    				reversalInd = "R";
										    			} else
										    				reversalInd = " ";
										    			
//										    			pan = doiSoatDataService.convertPanByCardno(cardno);
										    			
										    			doisoatDataTemp = doiSoatDataService.findDoiSoatCreditInfoByPanAndApvCode(pan,apvcode);
										    			
										    			doiSoatData = new DoiSoatData();
										    			doiSoatData.setCardBrn("MC");
										    			doiSoatData.setId(timeConverter.getCurrentTime() + String.format("%07d", id));
										    			doiSoatData.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
										    			doiSoatData.setUsrId(sUserId);
										    			doiSoatData.setNgayAdv(advDate);
										    			doiSoatData.setSoThe(cardno);
										    			doiSoatData.setMaGd(maGd);
										    			doiSoatData.setNgayGd(ngayGd);
										    			doiSoatData.setNgayFileIncoming(ngayFileIncoming);
										    			doiSoatData.setLtgd(ltgdCode);
										    			doiSoatData.setLttqt(lttqtCode);
										    			doiSoatData.setMaCapPhep(apvcode);
										    			doiSoatData.setDvcnt(dvcnt);
										    			doiSoatData.setReversalInd(reversalInd);
										    			doiSoatData.setPan(pan);
										    			
										    			if(doisoatDataTemp.size()>0)
										    			{
											    			for (Object[] b : doisoatDataTemp) {
				
											    				if(maGd.startsWith("20") || (maGd.startsWith("00") && reversalInd.equals("R")) || (maGd.startsWith("01") && reversalInd.equals("R")) ) {
											    					
											    					doiSoatData.setPhiRtmGd(BigDecimal.ZERO);
											    					doiSoatData.setVatPhiRtmGd(BigDecimal.ZERO);
											    					doiSoatData.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
											    					doiSoatData.setVatPhiIsaGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
											    					doiSoatData.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
											    					doiSoatData.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
											    					doiSoatData.setLoaiTienNguyenTeGd(b[5]==null ? " " : b[5].toString());
											    					
											    					if(ltgdCode.equals("IDR")) {
											    						doiSoatData.setStgdNguyenTeGd(doiSoatData.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
											    					}
											    					
											    				} 
											    				
											    				doiSoatData.setTenChuThe(b[1]==null ? " " : b[1].toString());
										    					doiSoatData.setDvpht(b[10]==null ? " " : b[10].toString());
										    					doiSoatData.setCrdpgm(b[9]==null ? " " : b[9].toString());
										    					doiSoatData.setLoc(b[11]==null ? " " : b[11].toString());
										    				}
										    			}
										    			
										    			if((maGd.startsWith("28") && !reversalInd.equals("R")) || maGd.startsWith("20") || (maGd.startsWith("00") && reversalInd.equals("R")) || (maGd.startsWith("01") && reversalInd.equals("R")) ) {
										    				stGd = stGd.negate();
										    				stTqt = stTqt.negate();
										    				stQdVnd = stQdVnd.negate();
										    			}
										    			
										    			doiSoatDataCheck = null;
										    			if(!(maGd.startsWith("20") || (maGd.startsWith("00") && reversalInd.equals("R")) || (maGd.startsWith("01") && reversalInd.equals("R")))) {
										    				doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(cardno, apvcode,maGd,reversalInd,advDate);
										    			}
									    				
									    				doiSoatData.setStGd(stGd);
										    			doiSoatData.setStTqt(stTqt);
										    			doiSoatData.setStQdVnd(stQdVnd);
										    			
										    			doiSoatData.setInterchange(interchange);
										    			doiSoatData.setStgdNguyenTeChenhLech(stGd.add(doiSoatData.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatData.getStgdNguyenTeGd()));
										    			doiSoatData.setStgdChenhLechDoTyGia(stQdVnd.add(doiSoatData.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatData.getStTrichNoKhGd()));
									    				
										    			doiSoatData.setMcc(mcc);
										    			
										    			if(doiSoatDataCheck!=null && doiSoatDataCheck.getDvcnt().contains("VIETNAM AIRLINES")) {
										    				//Accumulated value
										    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(stGd));
										    				doiSoatDataCheck.setStTqt(doiSoatDataCheck.getStTqt().add(stTqt));
										    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(stQdVnd));
										    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().add(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
										    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().add(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
										    				doiSoatDataService.create(doiSoatDataCheck);
										    				
										    				//Add new row
										    				doiSoatData.setStGd(BigDecimal.ZERO);
										    				doiSoatData.setStTqt(BigDecimal.ZERO);
										    				doiSoatData.setStQdVnd(BigDecimal.ZERO);
										    				doiSoatData.setStTrichNoKhGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeGd(BigDecimal.ZERO);
										    				doiSoatData.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
										    				doiSoatData.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
										    				
											    			doiSoatDataService.create(doiSoatData);
										    			} else
										    				doiSoatDataService.create(doiSoatData);
									    			
									    			}	
									    			break;
							    					
							    			}
							    			
							    		}
									} 
							    	workbook.close();
						            is.close();
									
								} catch (Exception e) {
									// TODO: handle exception
//									LOGGER.error(e.toString());
									e.printStackTrace();
									LOGGER.error("Error: " +  e.getMessage());
								}
								
								String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
								String loaithe = cbbCardType.isEmpty() ? "All" : cbbCardType.getValue().toString();
								grid.dataSource = getData(loaithe, cbbLoaiTienTQT.getValue().toString(),advDate,new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
								grid.initGrid(cbbCardType.getValue().toString(),"All");

								grid.refreshData();
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
			            LOGGER.error("Error: " + e.getMessage());
			        }
			        
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					LOGGER.error(e.toString());
				}
				return outputFile;
				
			}
			
			
		});
		chooseFile.setButtonCaption(IMPORT);
		chooseFile.addStyleName("myCustomUpload");
		chooseFile.setDescription("Import dữ liệu từ file excel (*.xlsx); Tên file incoming VS có định dạng INCTFyyyyMMddhhmmss; Dữ liệu import đồng thời MC/MD hoặc VS/VSD; XÓA PASSWORD file excel trước khi import");
		
		btView.addClickListener(event -> {
			if(!checkValidatorView()) {
				return;
			}
			String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
			String loaithe = cbbCardType.isEmpty() ? "All" : cbbCardType.getValue().toString();
			grid.dataSource = getData(loaithe, cbbLoaiTienTQT.getValue().toString(),advDate,new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid(cbbCardType.getValue().toString(),"All");

			grid.refreshData();
		});
		
		formLayout1a.addComponent(lbFromIncomingDate);
		formLayout1a.addComponent(dfFromIncomingDate);
		formLayout1a.addComponent(lbToIncomingDate);
		formLayout1a.addComponent(dfToIncomingDate);
		formLayout1a.addComponent(btImport);
		formLayout1a.addComponent(btRemove);
		
		formLayout1b.addComponent(optgrpIncomingFile);
		
		formLayout1st.addComponent(lbFileIncoming);
		formLayout1st.addComponent(chooseFile);
		formLayout2nd.addComponent(lbAdvDate);
		formLayout2nd.addComponent(dfAdvDate);
//		formLayout2nd.addComponent(lbtoDate);
//		formLayout2nd.addComponent(dftoDate);
		formLayout2nd.addComponent(lbCardType);
		formLayout2nd.addComponent(cbbCardType);
		formLayout2nd.addComponent(lbLoaiTienTQT);
		formLayout2nd.addComponent(cbbLoaiTienTQT);
		
		
		formLayout3rd.addComponent(btView);
		formLayout3rd.addComponent(btDoiChieu);
		formLayout3rd.addComponent(cbbLoaiGD);
		
		
		formLayout1a.setComponentAlignment(lbFromIncomingDate, Alignment.MIDDLE_LEFT);
		formLayout1a.setComponentAlignment(dfFromIncomingDate, Alignment.MIDDLE_LEFT);
		formLayout1a.setComponentAlignment(lbToIncomingDate, Alignment.MIDDLE_LEFT);
		formLayout1a.setComponentAlignment(dfToIncomingDate, Alignment.MIDDLE_LEFT);
		formLayout1a.setComponentAlignment(btImport, Alignment.MIDDLE_LEFT);
		formLayout1b.setComponentAlignment(optgrpIncomingFile, Alignment.MIDDLE_LEFT);
		
		formLayout1st.setComponentAlignment(chooseFile, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbAdvDate, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(dfAdvDate, Alignment.MIDDLE_LEFT);
//		formLayout2nd.setComponentAlignment(lbtoDate, Alignment.MIDDLE_LEFT);
//		formLayout2nd.setComponentAlignment(dftoDate, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbLoaiTienTQT, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbLoaiTienTQT, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(btView, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(btDoiChieu, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(cbbLoaiGD, Alignment.MIDDLE_CENTER);
		
		formLayout.addComponent(formLayout2nd);
		formLayout.addComponent(formLayout1a);
		formLayout.addComponent(formLayout1b);
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout3rd);
		formLayout.setComponentAlignment(formLayout1a, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout1b, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		grid = new DataGridDoiSoatTQTComponent();
//		grid.initGrid("All");
		mainLayout.addComponent(grid);

//		generatePagingLayout().setVisible(false);
//		pagingLayout = generatePagingLayout();
//		pagingLayout.setSpacing(true);
//		
//		mainLayout.addComponent(pagingLayout);
//		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		
		setCompositionRoot(mainLayout);
	}
	

//	private HorizontalLayout generatePagingLayout() {
//		final Button btPaging = new Button();
//		btPaging.setCaption(reloadLabelPaging());
//		btPaging.setEnabled(false);
//
//		final Button btPreviousPage = new Button("Trang trước");
//		btPreviousPage.setIcon(FontAwesome.ANGLE_LEFT);
//		btPreviousPage.setEnabled(true);
//
//		final Button btNextPage = new Button("Trang sau");
//		btNextPage.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
//		btNextPage.setIcon(FontAwesome.ANGLE_RIGHT);
//		btNextPage.setEnabled(true);
//		
//		final HorizontalLayout pagingLayout = new HorizontalLayout();
//		pagingLayout.setSizeUndefined();
//		pagingLayout.setSpacing(true);
//		pagingLayout.addComponent(btPaging);
//		pagingLayout.addComponent(btPreviousPage);
//		pagingLayout.addComponent(btNextPage);
//		pagingLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
//		
// 		return pagingLayout;
//	}

	private Page<DoiSoatData> getData(String loaithe, String lttqt, String ngayAdv, Pageable page) {
		doiSoatDataList = doiSoatDataService.findAllByLoaiTheAndLttqtAndNgayAdv(loaithe, lttqt, ngayAdv);
		result = new PageImpl<>(doiSoatDataList);
		return result;
	}
	
//	private String reloadLabelPaging() {
//		final StringBuilder sNumberOfElements = new StringBuilder();
//		String sTotalElements = null;
//		String sLabelPaging = "";
//		return sLabelPaging;
//	}
	
	@Override
	public void eventReload() {
	}
	
	private Button.ClickListener eventClickBTDoiChieu() {
		return event -> {
			try {
				if(!checkValidatorView()) {
					return;
				}
				String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
				String cardtype = cbbCardType.getValue().toString();
				configurationHelper.setCardtype(cardtype);
				if(cardtype.equals("VS") || cardtype.equals("MC"))
					getUI().addWindow(createWindowComponentDoiChieu("Đối chiếu thanh quyết toán credit card", new DoiChieuThanhQuyetToanCreditForm(doiSoatDataList, cbbLoaiTienTQT.getValue().toString(),cbbLoaiGD.getValue().toString(),advDate,cardtype,this::closeWindow)));
				else
					getUI().addWindow(createWindowComponentDoiChieu("Đối chiếu thanh quyết toán debit card", new DoiChieuThanhQuyetToanDebitForm(doiSoatDataList, cbbLoaiTienTQT.getValue().toString(),cbbLoaiGD.getValue().toString(),advDate,cardtype,this::closeWindow)));
			} catch (Exception e) {
				Notification.show(ERROR_MESSAGE, Type.ERROR_MESSAGE);
				LOGGER.error("DoiChieuClickListener - " + e.getMessage());
				e.printStackTrace();
			
			}
		};
	}
	
	private Window createWindowComponentDoiChieu(final String caption, final Component comp) {
		window.setCaption(caption);
		window.setContent(comp);
		window.setSizeFull();
		window.setWidth(95, Unit.PERCENTAGE);
		return window;
	}
	
	private void closeWindow() {
		getUI().removeWindow(window);
	}
	
	public boolean checkValidatorImport() {
		try {
			dfAdvDate.validate();
			return true;
		} catch (InvalidValueException ex) {
			dfAdvDate.setValidationVisible(true);
		}
		return false;
	}

	public boolean checkValidatorView() {
		try {
			dfAdvDate.validate();
			cbbCardType.validate();
			cbbLoaiTienTQT.validate();
			return true;
		} catch (InvalidValueException ex) {
			dfAdvDate.setValidationVisible(true);
			cbbCardType.setValidationVisible(true);
			cbbLoaiTienTQT.setValidationVisible(true);
		}
		return false;
	}

	
	private void showListIncomingFileName(DateField dfFromIncomingDate,DateField dfToIncomingDate) {
		optgrpIncomingFile.removeAllItems();
		String fromIncomingDate = timeConverter.convertDatetime(dfFromIncomingDate.getValue());
		String toIncomingDate = timeConverter.convertDatetime(dfToIncomingDate.getValue());
		String cardtype = cbbCardType.getValue().toString();
		List<String> listIncomingFileName = new ArrayList<String>();
			
		if(cardtype.startsWith("M")) {
			listIncomingFileName = doiSoatDataService.findMCIncomingFileNameByDate(fromIncomingDate,toIncomingDate);
			for(String item : listIncomingFileName) {
				optgrpIncomingFile.addItem(item);
				String itemCaption = item.substring(item.lastIndexOf("TT112T0.")+8, item.lastIndexOf("TT112T0.")+27);
				optgrpIncomingFile.setItemCaption(item, itemCaption); 
			}
		} else {
			fromIncomingDate = fromIncomingDate.substring(0, 8);
			toIncomingDate = toIncomingDate.substring(0, 8);
			listIncomingFileName = doiSoatDataService.findVSIncomingFileNameByDate(fromIncomingDate,toIncomingDate);
			for(String item : listIncomingFileName) {
				optgrpIncomingFile.addItem(item);
			}
		}
			
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
	
	private void importDataDoiSoat(DoiSoatData doiSoatInput, String binCardType) {
		
		String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
		String loaiTienTQT = cbbLoaiTienTQT.getValue().toString();
//		String binCardType = "";
		
		
//		if(cardType.startsWith("M")) {
//			if(!doiSoatInput.getLtgd().isEmpty())
//				ltgdCode = dsqtCurrencyService.findOneByCurrNum(doiSoatInput.getLtgd()).getCurrCode();
//			if(!doiSoatInput.getLttqt().isEmpty())
//				lttqtCode = dsqtCurrencyService.findOneByCurrNum(doiSoatInput.getLttqt()).getCurrCode();
//			
//			if(doiSoatInput.getMaGd().matches("(190000|290000)") && doiSoatInput.getSoThe().isEmpty()) {
//				binCardType = "MC";
//			}
//			else {
//				if(doiSoatInput.getSoThe().isEmpty()) {
//					continue;
//				} else {
//					doiSoatInput.setPan(doiSoatDataService.convertPanByCardno(doiSoatInput.getSoThe()));
//				}
//			}
//			
//		} else {
//			if(doiSoatInput.getSoThe().isEmpty()) {
//				continue;
//			} else {
//				doiSoatInput.setPan(doiSoatDataService.convertPanByCardno(doiSoatInput.getSoThe()));
//			}
//		}
		
		List<Object[]> doisoatDataTemp;
		DoiSoatData doiSoatDataCheck = null;
		
		
		
		doiSoatInput.setId(timeConverter.getCurrentTime() + String.format("%07d", id));
		doiSoatInput.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
		doiSoatInput.setUsrId(sUserId);
		doiSoatInput.setNgayAdv(advDate);
		
		switch(binCardType) {
			case "MD":
				id++;
    			LOGGER.info("STT: " + id + ", cardno: " + doiSoatInput.getSoThe() + ", apvcode: " + doiSoatInput.getMaCapPhep() + ", stGd: " + doiSoatInput.getStGd());
    			
    			doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(doiSoatInput.getSoThe(), doiSoatInput.getMaCapPhep(),doiSoatInput.getMaGd(),doiSoatInput.getReversalInd(),advDate);
    			doisoatDataTemp = doiSoatDataService.findDoiSoatInfoByPanAndApvCode(doiSoatInput.getPan(),doiSoatInput.getMaCapPhep(),doiSoatInput.getNgayGd());
    			
    			doiSoatInput.setCardBrn("MD");
    			
    			if(doisoatDataTemp.size()>0)
    			{
	    			for (Object[] b : doisoatDataTemp) {

    					doiSoatInput.setLoaiTienNguyenTeGd(b[12]==null ? " " : b[12].toString());
    					doiSoatInput.setTenChuThe(b[1]==null ? " " : b[1].toString());
//    					doiSoatInput.setTenChuTk(b[16]==null ? " " : b[16].toString());
    					doiSoatInput.setCasa(b[11]==null ? " " : b[11].toString());
    					String accountDesc = doiSoatDataService.findAccountDescription(doiSoatInput.getCasa());
    					doiSoatInput.setTenChuTk(accountDesc);
    					doiSoatInput.setStatusCw(b[9]==null ? " " : b[9].toString());
    					doiSoatInput.setDvpht(b[8]==null ? " " : b[8].toString());
    					doiSoatInput.setCif(b[14]==null ? " " : b[14].toString());
    					doiSoatInput.setCrdpgm(b[15]==null ? " " : b[15].toString());
    					doiSoatInput.setTrace(b[10]==null ? " " : b[10].toString());
						doiSoatInput.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")) : BigDecimal.ZERO);
    					doiSoatInput.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")) : BigDecimal.ZERO);
    					doiSoatInput.setPhiRtmGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
    					doiSoatInput.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
    					
    					if(doiSoatInput.getLtgd().equals("IDR")) {
    						doiSoatInput.setStgdNguyenTeGd(doiSoatInput.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
    					}
    				}
    			}
    			
    			if(doiSoatInput.getMaGd().startsWith("28") && !doiSoatInput.getReversalInd().equals("R") && doiSoatInput.getStatusCw().equals(" "))
    			{
    				doiSoatInput.setStGd(doiSoatInput.getStGd().negate());
    				doiSoatInput.setStTqt(doiSoatInput.getStTqt().negate()); 
    				doiSoatInput.setStQdVnd(doiSoatInput.getStQdVnd().negate());
    			}
    			
    			if(doiSoatInput.getMaGd().startsWith("01") & doiSoatInput.getReversalInd().equals("R") && doiSoatInput.getStatusCw().equals("C")
				|| (doiSoatInput.getMaGd().startsWith("00") && doiSoatInput.getReversalInd().equals("R") && doiSoatInput.getStatusCw().equals(" "))
				|| (doiSoatInput.getMaGd().startsWith("20") && doiSoatInput.getStatusCw().equals("C"))
    			|| (doiSoatInput.getMaGd().startsWith("20") && !doiSoatInput.getReversalInd().equals("R") && doiSoatInput.getStatusCw().equals(" "))
    			|| (doiSoatInput.getMaGd().startsWith("01") && doiSoatInput.getReversalInd().equals("R") && doiSoatInput.getStatusCw().equals(" "))) {
    				doiSoatInput.setStGd(doiSoatInput.getStGd().negate());
    				doiSoatInput.setStTqt(doiSoatInput.getStTqt().negate());
    				doiSoatInput.setStQdVnd(doiSoatInput.getStQdVnd().negate());
    				doiSoatInput.setStTrichNoKhGd(doiSoatInput.getStTrichNoKhGd().negate());
					doiSoatInput.setStgdNguyenTeGd(doiSoatInput.getStgdNguyenTeGd().negate());
    			}
    			
    			if(doiSoatInput.getMaGd().startsWith("00") & !doiSoatInput.getReversalInd().equals("R") && doiSoatInput.getStatusCw().equals("C")) {
    				doiSoatInput.setStTrichNoKhGd(BigDecimal.ZERO);
					doiSoatInput.setStgdNguyenTeGd(BigDecimal.ZERO);
    			}
    			
    			doiSoatInput.setStgdNguyenTeChenhLech(doiSoatInput.getStGd().subtract(doiSoatInput.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatInput.getStgdNguyenTeGd()));
				doiSoatInput.setStgdChenhLechDoTyGia(doiSoatInput.getStQdVnd().subtract(doiSoatInput.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatInput.getStTrichNoKhGd()));
				
    			if(!doiSoatInput.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
					doiSoatInput.setTyGiaTrichNo(doiSoatInput.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatInput.getStTrichNoKhGd().divide(doiSoatInput.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP));

    			doiSoatInput.setSoTienGdHoanTraTruyThu(doiSoatInput.getStgdNguyenTeChenhLech().multiply(doiSoatInput.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
				
				if(!doiSoatInput.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
					doiSoatInput.setPhiIsaHoanTraTruyThu(doiSoatInput.getStgdNguyenTeChenhLech().multiply(doiSoatInput.getPhiIsaGd()).divide(doiSoatInput.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
				
				doiSoatInput.setVatPhiIsaHoanTraTruyThu(doiSoatInput.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
				
				if(!doiSoatInput.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
					doiSoatInput.setPhiRtmHoanTraTruyThu(doiSoatInput.getStgdNguyenTeChenhLech().multiply(doiSoatInput.getPhiRtmGd()).divide(doiSoatInput.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
				
				doiSoatInput.setVatPhiRtmHoanTraTruyThu(doiSoatInput.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
				doiSoatInput.setTongPhiVatHoanTraTruyThu(doiSoatInput.getPhiIsaHoanTraTruyThu().add(doiSoatInput.getVatPhiIsaHoanTraTruyThu()).add(doiSoatInput.getPhiRtmHoanTraTruyThu()).add(doiSoatInput.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
				doiSoatInput.setTongHoanTraTruyThu(doiSoatInput.getSoTienGdHoanTraTruyThu().add(doiSoatInput.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
				
    			if(doiSoatDataCheck!=null) {
    				//Accumulated value
    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(doiSoatInput.getStGd()));
    				doiSoatDataCheck.setStTqt(doiSoatDataCheck.getStTqt().add(doiSoatInput.getStTqt()));
    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(doiSoatInput.getStQdVnd()));
    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().subtract(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().subtract(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
    				doiSoatDataService.create(doiSoatDataCheck);
    				
    				//Add new row
    				doiSoatInput.setStGd(BigDecimal.ZERO);
    				doiSoatInput.setStTqt(BigDecimal.ZERO);
    				doiSoatInput.setStQdVnd(BigDecimal.ZERO);
    				doiSoatInput.setStTrichNoKhGd(BigDecimal.ZERO);
    				doiSoatInput.setStgdNguyenTeGd(BigDecimal.ZERO);
    				doiSoatInput.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
    				doiSoatInput.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
    				
	    			doiSoatDataService.create(doiSoatInput);
    			} else
    				doiSoatDataService.create(doiSoatInput);
				
				break;
			case "VSD":
				LOGGER.info("Card no: " + doiSoatInput.getSoThe() + ", lttqt: " + doiSoatInput.getLttqt() + ", loaiTienTQT: " + loaiTienTQT);
				if(doiSoatInput.getSoThe().startsWith("453618") && (loaiTienTQT.equals("All") || loaiTienTQT.equals(doiSoatInput.getLttqt()))) { 
					id++;
					
	    			LOGGER.info("STT: " + id + ", doiSoatInput.getSoThe(): "+ doiSoatInput.getSoThe() + ", apvcode: " + doiSoatInput.getMaCapPhep() + ", stGd: " + doiSoatInput.getStGd());
	    			
	    			doisoatDataTemp = doiSoatDataService.findDoiSoatInfoByPanAndApvCode(doiSoatInput.getPan(),doiSoatInput.getMaCapPhep(),doiSoatInput.getNgayGd());
	    			
	    			doiSoatInput.setCardBrn("VSD");
	    			if(doiSoatInput.getIssuerCharge().equals("S"))
	    				doiSoatInput.setPhiXuLyGd(doiSoatInput.getStQdVnd().divide(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP)); //Value with VS
	    			else 
	    				doiSoatInput.setPhiXuLyGd(BigDecimal.ZERO);
	    			
	    			
	    			if(doisoatDataTemp.size()>0)
	    			{
		    			for (Object[] b : doisoatDataTemp) {
	
	    					doiSoatInput.setLoaiTienNguyenTeGd(b[12]==null ? " " : b[12].toString());
	    					doiSoatInput.setTenChuThe(b[1]==null ? " " : b[1].toString());
//	    					doiSoatInput.setTenChuTk(b[16]==null ? " " : b[16].toString());
	    					doiSoatInput.setCasa(b[11]==null ? " " : b[11].toString());
	    					String accountDesc = doiSoatDataService.findAccountDescription(doiSoatInput.getCasa());
	    					doiSoatInput.setTenChuTk(accountDesc);
	    					doiSoatInput.setStatusCw(b[9]==null ? " " : b[9].toString());
	    					doiSoatInput.setDvpht(b[8]==null ? " " : b[8].toString());
	    					doiSoatInput.setCif(b[14]==null ? " " : b[14].toString());
	    					doiSoatInput.setCrdpgm(b[15]==null ? " " : b[15].toString());
	    					doiSoatInput.setTrace(b[10]==null ? " " : b[10].toString());
							doiSoatInput.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")) : BigDecimal.ZERO);
	    					doiSoatInput.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")) : BigDecimal.ZERO);
	    					doiSoatInput.setPhiRtmGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
	    					doiSoatInput.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
	    					
	    					if(doiSoatInput.getLtgd().equals("IDR")) {
	    						doiSoatInput.setStgdNguyenTeGd(doiSoatInput.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
	    					}
	    				}
	    			}
	    			
	    			if(doiSoatInput.getMaGd().startsWith("06") || doiSoatInput.getMaGd().startsWith("25") || doiSoatInput.getMaGd().startsWith("27")) {
	    				doiSoatInput.setStGd(doiSoatInput.getStGd().negate());
	    				doiSoatInput.setStQdVnd(doiSoatInput.getStQdVnd().negate());
	    				doiSoatInput.setStTrichNoKhGd(doiSoatInput.getStTrichNoKhGd().negate());
						doiSoatInput.setStgdNguyenTeGd(doiSoatInput.getStgdNguyenTeGd().negate());
	    			}
	    			
	    			if(doiSoatInput.getMaGd().startsWith("05") && doiSoatInput.getStatusCw().equals("C")) {
	    				doiSoatInput.setStTrichNoKhGd(BigDecimal.ZERO);
						doiSoatInput.setStgdNguyenTeGd(BigDecimal.ZERO);
	    			}
	    			
	    			doiSoatInput.setStgdNguyenTeChenhLech(doiSoatInput.getStGd().subtract(doiSoatInput.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatInput.getStgdNguyenTeGd()));
					doiSoatInput.setStgdChenhLechDoTyGia(doiSoatInput.getStQdVnd().subtract(doiSoatInput.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatInput.getStTrichNoKhGd()));
					
	    			if(!doiSoatInput.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
						doiSoatInput.setTyGiaTrichNo(doiSoatInput.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatInput.getStTrichNoKhGd().divide(doiSoatInput.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP));
					
	    			doiSoatInput.setSoTienGdHoanTraTruyThu(doiSoatInput.getStgdNguyenTeChenhLech().multiply(doiSoatInput.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
					
					if(!doiSoatInput.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
						doiSoatInput.setPhiIsaHoanTraTruyThu(doiSoatInput.getStgdNguyenTeChenhLech().multiply(doiSoatInput.getPhiIsaGd()).divide(doiSoatInput.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
					
					doiSoatInput.setVatPhiIsaHoanTraTruyThu(doiSoatInput.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
					
					if(!doiSoatInput.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
						doiSoatInput.setPhiRtmHoanTraTruyThu(doiSoatInput.getStgdNguyenTeChenhLech().multiply(doiSoatInput.getPhiRtmGd()).divide(doiSoatInput.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
					
					doiSoatInput.setVatPhiRtmHoanTraTruyThu(doiSoatInput.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
					doiSoatInput.setTongPhiVatHoanTraTruyThu(doiSoatInput.getPhiIsaHoanTraTruyThu().add(doiSoatInput.getVatPhiIsaHoanTraTruyThu()).add(doiSoatInput.getPhiRtmHoanTraTruyThu()).add(doiSoatInput.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
					doiSoatInput.setTongHoanTraTruyThu(doiSoatInput.getSoTienGdHoanTraTruyThu().add(doiSoatInput.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
					
					
					if(!((doiSoatInput.getMaGd().startsWith("25") && doiSoatInput.getStatusCw().equals(" "))
					|| (doiSoatInput.getMaGd().startsWith("06") && doiSoatInput.getStatusCw().equals(" ") && !doiSoatInput.getMerchantCity().contains("Visa Direct"))
					|| (doiSoatInput.getMaGd().startsWith("27") && doiSoatInput.getStatusCw().equals(" "))))
					{
						doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(doiSoatInput.getSoThe(), doiSoatInput.getMaCapPhep(),doiSoatInput.getMaGd()," ",advDate);
					}
					
	    			if(doiSoatDataCheck!=null) {
	    				//Accumulated value
	    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(doiSoatInput.getStGd()));
	    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(doiSoatInput.getStQdVnd()));
	    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().subtract(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
	    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().subtract(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
	    				doiSoatDataCheck.setSoTienGdHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getTyGiaTrichNo()).setScale(0, RoundingMode.HALF_UP));
	    				
	    				if(!doiSoatDataCheck.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
	    					doiSoatDataCheck.setPhiIsaHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getPhiIsaGd()).divide(doiSoatDataCheck.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
	    				
	    				doiSoatDataCheck.setVatPhiIsaHoanTraTruyThu(doiSoatDataCheck.getPhiIsaHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
	    				
	    				if(!doiSoatDataCheck.getStgdNguyenTeGd().equals(BigDecimal.ZERO))
	    					doiSoatDataCheck.setPhiRtmHoanTraTruyThu(doiSoatDataCheck.getStgdNguyenTeChenhLech().multiply(doiSoatDataCheck.getPhiRtmGd()).divide(doiSoatDataCheck.getStgdNguyenTeGd(),5,RoundingMode.HALF_UP).setScale(0, RoundingMode.HALF_UP));
	    				
	    				doiSoatDataCheck.setVatPhiRtmHoanTraTruyThu(doiSoatDataCheck.getPhiRtmHoanTraTruyThu().multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.HALF_UP));
	    				doiSoatDataCheck.setTongPhiVatHoanTraTruyThu(doiSoatDataCheck.getPhiIsaHoanTraTruyThu().add(doiSoatDataCheck.getVatPhiIsaHoanTraTruyThu()).add(doiSoatDataCheck.getPhiRtmHoanTraTruyThu()).add(doiSoatDataCheck.getVatPhiRtmHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
	    				doiSoatDataCheck.setTongHoanTraTruyThu(doiSoatDataCheck.getSoTienGdHoanTraTruyThu().add(doiSoatDataCheck.getTongPhiVatHoanTraTruyThu()).setScale(0, RoundingMode.HALF_UP));
	    				
	    				doiSoatDataService.create(doiSoatDataCheck);
	    				
	    				//Add new row
	    				doiSoatInput.setStGd(BigDecimal.ZERO);
	    				doiSoatInput.setStTqt(BigDecimal.ZERO);
	    				doiSoatInput.setStQdVnd(BigDecimal.ZERO);
	    				doiSoatInput.setStTrichNoKhGd(BigDecimal.ZERO);
	    				doiSoatInput.setStgdNguyenTeGd(BigDecimal.ZERO);
	    				doiSoatInput.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
	    				doiSoatInput.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
	    				doiSoatInput.setSoTienGdHoanTraTruyThu(BigDecimal.ZERO);
	    				doiSoatInput.setPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
	    				doiSoatInput.setVatPhiIsaHoanTraTruyThu(BigDecimal.ZERO);
	    				doiSoatInput.setPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
	    				doiSoatInput.setVatPhiRtmHoanTraTruyThu(BigDecimal.ZERO);
	    				doiSoatInput.setTongPhiVatHoanTraTruyThu(BigDecimal.ZERO);
	    				doiSoatInput.setTongHoanTraTruyThu(BigDecimal.ZERO);
	    				
		    			doiSoatDataService.create(doiSoatInput);
	    			} else
	    				doiSoatDataService.create(doiSoatInput);
				
				}
				break;
				
			case "VS":
				LOGGER.info("Card no: " + doiSoatInput.getSoThe() + ", lttqt: " + doiSoatInput.getLttqt() + ", loaiTienTQT: " + loaiTienTQT);
				if(doiSoatInput.getSoThe().matches("(489516|489517|489518).*") 				    					
				&& (loaiTienTQT.equals("All") || loaiTienTQT.equals(doiSoatInput.getLttqt()))) { 
					id++;
	    			LOGGER.info("STT: " + id + ", cardno: "+ doiSoatInput.getSoThe() + ", apvcode: " + doiSoatInput.getMaCapPhep() + ", stGd: " + doiSoatInput.getStGd() + ", ngayGd: " + doiSoatInput.getNgayGd());
	    			
	    			doisoatDataTemp = doiSoatDataService.findDoiSoatCreditInfoByPanAndApvCode(doiSoatInput.getPan(), doiSoatInput.getMaCapPhep());
	    			
	    			doiSoatInput.setCardBrn("VS");
	    			if(doiSoatInput.getIssuerCharge().equals("S"))
	    				doiSoatInput.setPhiXuLyGd(doiSoatInput.getStQdVnd().divide(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP)); //Value with VS
	    			else 
	    				doiSoatInput.setPhiXuLyGd(BigDecimal.ZERO);
	    			
	    			if(doisoatDataTemp.size()>0)
	    			{
		    			for (Object[] b : doisoatDataTemp) {
	
		    				if((doiSoatInput.getMaGd().startsWith("06") && !doiSoatInput.getMerchantCity().contains("Visa Direct")) || doiSoatInput.getMaGd().startsWith("25")) {
		    					doiSoatInput.setPhiRtmGd(BigDecimal.ZERO);
		    					doiSoatInput.setVatPhiRtmGd(BigDecimal.ZERO);
		    					doiSoatInput.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
		    					doiSoatInput.setVatPhiIsaGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
	    						doiSoatInput.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
		    					doiSoatInput.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
		    					doiSoatInput.setLoaiTienNguyenTeGd(b[5]==null ? " " : b[5].toString());
		    					if(doiSoatInput.getLtgd().equals("IDR")) {
		    						doiSoatInput.setStgdNguyenTeGd(doiSoatInput.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
		    					}
		    				} 
	    					doiSoatInput.setTenChuThe(b[1]==null ? " " : b[1].toString());
	    					doiSoatInput.setDvpht(b[10]==null ? " " : b[10].toString());
	    					doiSoatInput.setCrdpgm(b[9]==null ? " " : b[9].toString());
							doiSoatInput.setLoc(b[11]==null ? " " : b[11].toString());
	    					
	    					
	    				}
	    			}
	    			
	    			if(doiSoatInput.getMaGd().matches("(25|06|27).*"))
					{
						doiSoatInput.setStGd(doiSoatInput.getStGd().negate());
	    				doiSoatInput.setStQdVnd(doiSoatInput.getStQdVnd().negate());
					}
	    			
	    			
	    			doiSoatInput.setStgdNguyenTeChenhLech(doiSoatInput.getStGd().add(doiSoatInput.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatInput.getStgdNguyenTeGd()));
					doiSoatInput.setStgdChenhLechDoTyGia(doiSoatInput.getStQdVnd().add(doiSoatInput.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatInput.getStTrichNoKhGd()));
					
					if(!((doiSoatInput.getMaGd().startsWith("25")) 
					|| (doiSoatInput.getMaGd().startsWith("06") && !doiSoatInput.getMerchantCity().contains("Visa Direct"))
					|| (doiSoatInput.getMaGd().startsWith("27"))))
					{
						doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(doiSoatInput.getSoThe(), doiSoatInput.getMaCapPhep(),doiSoatInput.getMaGd()," ",advDate);
					}
					
					if(doiSoatDataCheck!=null) {
	    				//Accumulated value
	    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(doiSoatInput.getStGd()));
	    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(doiSoatInput.getStQdVnd()));
	    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().add(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
	    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().add(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
	    				
	    				doiSoatDataService.create(doiSoatDataCheck);
	    				
	    				//Add new row
	    				doiSoatInput.setStGd(BigDecimal.ZERO);
	    				doiSoatInput.setStTqt(BigDecimal.ZERO);
	    				doiSoatInput.setStQdVnd(BigDecimal.ZERO);
	    				doiSoatInput.setStTrichNoKhGd(BigDecimal.ZERO);
	    				doiSoatInput.setStgdNguyenTeGd(BigDecimal.ZERO);
	    				doiSoatInput.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
	    				doiSoatInput.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
	    				
		    			doiSoatDataService.create(doiSoatInput);
	    			} else
	    				doiSoatDataService.create(doiSoatInput);
				
				}
				break;
				
			case "MC":
				id++;
    			LOGGER.info("STT: " + id + ", cardno: "+ doiSoatInput.getSoThe() + ", apvcode: " + doiSoatInput.getMaCapPhep() + ", stGd: " + doiSoatInput.getStGd());
    			
    			doisoatDataTemp = doiSoatDataService.findDoiSoatCreditInfoByPanAndApvCode(doiSoatInput.getPan(),doiSoatInput.getMaCapPhep());
    			
    			doiSoatInput.setCardBrn("MC");
    			
    			if(doisoatDataTemp.size()>0)
    			{
	    			for (Object[] b : doisoatDataTemp) {

	    				if(doiSoatInput.getMaGd().startsWith("20") || (doiSoatInput.getMaGd().startsWith("00") && doiSoatInput.getReversalInd().equals("R")) || (doiSoatInput.getMaGd().startsWith("01") && doiSoatInput.getReversalInd().equals("R")) ) {
	    					
	    					doiSoatInput.setPhiRtmGd(BigDecimal.ZERO);
	    					doiSoatInput.setVatPhiRtmGd(BigDecimal.ZERO);
	    					doiSoatInput.setPhiIsaGd(b[6]==null ? BigDecimal.ZERO : new BigDecimal(b[6].toString()));
	    					doiSoatInput.setVatPhiIsaGd(b[7]==null ? BigDecimal.ZERO : new BigDecimal(b[7].toString()));
	    					doiSoatInput.setStTrichNoKhGd(b[4] != null ? new BigDecimal(b[4].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
	    					doiSoatInput.setStgdNguyenTeGd(b[3] != null ? new BigDecimal(b[3].toString().replace(",", "")).divide(BigDecimal.valueOf(100)) : BigDecimal.ZERO);
	    					doiSoatInput.setLoaiTienNguyenTeGd(b[5]==null ? " " : b[5].toString());
	    					
	    					if(doiSoatInput.getLtgd().equals("IDR")) {
	    						doiSoatInput.setStgdNguyenTeGd(doiSoatInput.getStgdNguyenTeGd().multiply(BigDecimal.valueOf(100)));
	    					}
	    					
	    				} 
	    				
	    				doiSoatInput.setTenChuThe(b[1]==null ? " " : b[1].toString());
    					doiSoatInput.setDvpht(b[10]==null ? " " : b[10].toString());
    					doiSoatInput.setCrdpgm(b[9]==null ? " " : b[9].toString());
    					doiSoatInput.setLoc(b[11]==null ? " " : b[11].toString());
    				}
    			}
    			
    			if((doiSoatInput.getMaGd().startsWith("28") && !doiSoatInput.getReversalInd().equals("R")) || doiSoatInput.getMaGd().startsWith("20") || (doiSoatInput.getMaGd().startsWith("00") && doiSoatInput.getReversalInd().equals("R")) || (doiSoatInput.getMaGd().startsWith("01") && doiSoatInput.getReversalInd().equals("R")) ) {
    				doiSoatInput.setStGd(doiSoatInput.getStGd().negate());
    				doiSoatInput.setStTqt(doiSoatInput.getStTqt().negate());
    				doiSoatInput.setStQdVnd(doiSoatInput.getStQdVnd().negate());
    			}
    			
    			doiSoatDataCheck = null;
    			if(!(doiSoatInput.getMaGd().startsWith("20") || (doiSoatInput.getMaGd().startsWith("00") && doiSoatInput.getReversalInd().equals("R")) || (doiSoatInput.getMaGd().startsWith("01") && doiSoatInput.getReversalInd().equals("R")))) {
    				doiSoatDataCheck = doiSoatDataService.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(doiSoatInput.getSoThe(), doiSoatInput.getMaCapPhep(),doiSoatInput.getMaGd(),doiSoatInput.getReversalInd(),advDate);
    			}
				
    			doiSoatInput.setStgdNguyenTeChenhLech(doiSoatInput.getStGd().add(doiSoatInput.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatInput.getStgdNguyenTeGd()));
    			doiSoatInput.setStgdChenhLechDoTyGia(doiSoatInput.getStQdVnd().add(doiSoatInput.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatInput.getStTrichNoKhGd()));
				
    			if(doiSoatDataCheck!=null) {
    				//Accumulated value
    				doiSoatDataCheck.setStGd(doiSoatDataCheck.getStGd().add(doiSoatInput.getStGd()));
    				doiSoatDataCheck.setStTqt(doiSoatDataCheck.getStTqt().add(doiSoatInput.getStTqt()));
    				doiSoatDataCheck.setStQdVnd(doiSoatDataCheck.getStQdVnd().add(doiSoatInput.getStQdVnd()));
    				doiSoatDataCheck.setStgdNguyenTeChenhLech(doiSoatDataCheck.getStGd().add(doiSoatDataCheck.getStgdNguyenTeGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStgdNguyenTeGd()));
    				doiSoatDataCheck.setStgdChenhLechDoTyGia(doiSoatDataCheck.getStQdVnd().add(doiSoatDataCheck.getStTrichNoKhGd()==null?BigDecimal.ZERO:doiSoatDataCheck.getStTrichNoKhGd()));
    				doiSoatDataService.create(doiSoatDataCheck);
    				
    				//Add new row
    				doiSoatInput.setStGd(BigDecimal.ZERO);
    				doiSoatInput.setStTqt(BigDecimal.ZERO);
    				doiSoatInput.setStQdVnd(BigDecimal.ZERO);
    				doiSoatInput.setStTrichNoKhGd(BigDecimal.ZERO);
    				doiSoatInput.setStgdNguyenTeGd(BigDecimal.ZERO);
    				doiSoatInput.setStgdNguyenTeChenhLech(BigDecimal.ZERO);
    				doiSoatInput.setStgdChenhLechDoTyGia(BigDecimal.ZERO);
    				
	    			doiSoatDataService.create(doiSoatInput);
    			} else
    				doiSoatDataService.create(doiSoatInput);
			
//			}	
			break;
				
		}
	}
}
