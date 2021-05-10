package com.dvnb.components.dstqt;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang.exception.ExceptionUtils;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutListener;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.PhiInterchange;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.PhiInterchangeService;
import com.dvnb.services.TyGiaTqtService;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.themes.Reindeer;
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
public class DoiChieuThanhQuyetToanCreditForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DoiChieuThanhQuyetToanCreditForm.class);
	
	private DataSource localDataSource;
	private SpringConfigurationValueHelper configurationHelper;
	final TimeConverter timeConverter = new TimeConverter();
	public String filename;
	
	List<String> itemsTemp;
	
	public transient Grid gridGDTTHHDuocTQT;
//	public transient Grid gridGDHTTTHH;
	public transient Grid gridGDRTMDuocTQT;
//	public transient Grid gridGDHTRTM;
	public transient Grid gridGDMoneySendFF;
	public transient Grid gridGDBaoCoBatThuong;
	public transient Grid gridGDBaoNoBatThuong;
	
	public transient Grid gridGDTTHHDuocTQT_USD;
	public transient Grid gridGDHTTTHH_USD;
	public transient Grid gridGDRTMDuocTQT_USD;
	public transient Grid gridGDHTRTM_USD;
	public transient Grid gridGDMoneySendFF_USD;
	public transient Grid gridGDBaoCoBatThuong_USD;
	public transient Grid gridGDBaoNoBatThuong_USD;
	
	private transient IndexedContainer containerGDTTHHDuocTQT;
	private transient IndexedContainer containerGDRTMDuocTQT;
	private transient IndexedContainer containerGDMoneySendFF;
	private transient IndexedContainer containerGDBaoCoBatThuong;
	private transient IndexedContainer containerGDBaoNoBatThuong;
	
	private transient IndexedContainer containerGDTTHHDuocTQT_USD;
	private transient IndexedContainer containerGDHTTTHH_USD;
	private transient IndexedContainer containerGDRTMDuocTQT_USD;
	private transient IndexedContainer containerGDHTRTM_USD;
	private transient IndexedContainer containerGDMoneySendFF_USD;
	private transient IndexedContainer containerGDBaoCoBatThuong_USD;
	private transient IndexedContainer containerGDBaoNoBatThuong_USD;
	
	private Button btGiaoDichTheTTHH;
	private Button btGiaoDichTheRTM;
	private Button btGiaoDichMoneySendFastFund;
	private Button btGDBaoCoBatThuong;
	private Button btGDBaoNoBatThuong;
	
	private Button btGiaoDichTheTTHH_USD;
	private Button btGiaoDichTheRTM_USD;
	private Button btGiaoDichMoneySendFastFund_USD;
	private Button btGDBaoCoBatThuong_USD;
	private Button btGDBaoNoBatThuong_USD;
	
	private Button btExport;
	private Button btClose;
	private Button btExportHTTTHH;
	private Button btExportHTRTM;
	private Button btExportGDTTHH_VND;
	private Button btExportGDTTHH_USD;
	private Button btExportGDRTM_VND;
	private Button btExportGDRTM_USD;
	private Button btExportGDMSFF_VND;
	private Button btExportGDMSFF_USD;
	
	private Page<DoiSoatData> dataSourceGDTTHHDuocTQT;
	private FooterRow footerGDTTHHDuocTQT;
	private Page<DoiSoatData> dataSourceGDRTMDuocTQT;
	private FooterRow footerGDRTMDuocTQT;
	private Page<DoiSoatData> dataSourceGDMoneySendFF;
	private FooterRow footerGDMoneySendFF;
	private Page<DoiSoatData> dataSourceGDBaoCoBatThuong;
	private Page<DoiSoatData> dataSourceGDBaoNoBatThuong;
	
	private Page<DoiSoatData> dataSourceGDTTHHDuocTQT_USD;
	private FooterRow footerGDTTHHDuocTQT_USD;
	private Page<DoiSoatData> dataSourceGDHTTTHH_USD;
	private Page<DoiSoatData> dataSourceGDRTMDuocTQT_USD;
	private FooterRow footerGDRTMDuocTQT_USD;
	private Page<DoiSoatData> dataSourceGDHTRTM_USD;
	private Page<DoiSoatData> dataSourceGDMoneySendFF_USD;
	private FooterRow footerGDMoneySendFF_USD;
	private Page<DoiSoatData> dataSourceGDBaoCoBatThuong_USD;
	private Page<DoiSoatData> dataSourceGDBaoNoBatThuong_USD;
	
	private final transient DoiSoatDataService doiSoatDataService;
	private final transient TyGiaTqtService tyGiaTqtService;
	private transient Page<DoiSoatData> result;
	int stt = 0;
	
	private TyGiaTqt tygiaTqt;
	
	private String ngayAdv;
	private String loaitienTqt;
	private String cardType;
	private String loaigd = "";
	
	private List<DoiSoatData> doisoatDataList = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> doisoatDataListTemp = new ArrayList<DoiSoatData>();
	
	// Paging
	private final static int SIZE_OF_PAGE = 1000000;
	private final static int FIRST_OF_PAGE = 0;
	
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	public DoiChieuThanhQuyetToanCreditForm(List<DoiSoatData> _doisoatDataList, String _loaitienTqt,String loaiGD, String _ngayAdv,String _cardType,final Callback callback) {
		super();
		doisoatDataList = _doisoatDataList;
		//System.out.println("sStatus:" + sStatus);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		DescriptionService descActService = (DescriptionService) helper.getBean("descriptionService");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		tyGiaTqtService = (TyGiaTqtService) helper.getBean("tyGiaTqtService");
		localDataSource = (DataSource) helper.getBean("dataSource");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		this.ngayAdv = _ngayAdv;
		this.loaitienTqt = _loaitienTqt;
		this.cardType = _cardType;
		
		try {
			if(loaitienTqt.equals("USD")) {
				tygiaTqt = tyGiaTqtService.findTyGiaTqtByNgayAdvAndCardType(ngayAdv, cardType);
				if(tygiaTqt==null) {
					Notification.show("Lỗi", "Chưa update tỷ giá", Type.ERROR_MESSAGE);
					return;
				}
					
			}
		} catch (Exception e) {
			// TODO: handle exception
			Notification.show("Lỗi", ExceptionUtils.getFullStackTrace(e), Type.ERROR_MESSAGE);
		}
		
		final String userid = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);
		
		final Panel panelTQTTheoVND= new Panel("THANH QUYẾT TOÁN THEO LOẠI TIỀN VNĐ");
		panelTQTTheoVND.setStyleName(Reindeer.PANEL_LIGHT);
		panelTQTTheoVND.setSizeFull();
		
		btExportHTTTHH = new Button("Xuất file hoàn trả phí + VAT");
		btExportHTTTHH.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportHTTTHH.setIcon(FontAwesome.FILE_EXCEL_O);
		
		btExportHTRTM = new Button("Xuất file hoàn trả phí + VAT");
		btExportHTRTM.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportHTRTM.setIcon(FontAwesome.FILE_EXCEL_O);
		
		btExportGDTTHH_VND = new Button("Xuất dữ liệu đối chiếu");
		btExportGDTTHH_VND.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportGDTTHH_VND.setIcon(FontAwesome.FILE_EXCEL_O);
		btExportGDTTHH_VND.addClickListener(event -> {
			doisoatDataListTemp = dataSourceGDTTHHDuocTQT.getContent();
			exportDataDoiChieu(doisoatDataListTemp,"VND","GDTTHH");
		});
		
		btExportGDTTHH_USD = new Button("Xuất dữ liệu đối chiếu");
		btExportGDTTHH_USD.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportGDTTHH_USD.setIcon(FontAwesome.FILE_EXCEL_O);
		btExportGDTTHH_USD.addClickListener(event -> {
			doisoatDataListTemp = dataSourceGDTTHHDuocTQT_USD.getContent();
			exportDataDoiChieu(doisoatDataListTemp,"USD","GDTTHH");
		});
		
		
		btExportGDRTM_VND = new Button("Xuất dữ liệu đối chiếu");
		btExportGDRTM_VND.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportGDRTM_VND.setIcon(FontAwesome.FILE_EXCEL_O);
		btExportGDRTM_VND.addClickListener(event -> {
			doisoatDataListTemp = dataSourceGDRTMDuocTQT.getContent();
			exportDataDoiChieu(doisoatDataListTemp,"VND","GDRTM");
		});
		
		btExportGDRTM_USD = new Button("Xuất dữ liệu đối chiếu");
		btExportGDRTM_USD.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportGDRTM_USD.setIcon(FontAwesome.FILE_EXCEL_O);
		btExportGDRTM_USD.addClickListener(event -> {
			doisoatDataListTemp = dataSourceGDRTMDuocTQT_USD.getContent();
			exportDataDoiChieu(doisoatDataListTemp,"USD","GDRTM");
		});
		
		btExportGDMSFF_VND = new Button("Xuất dữ liệu đối chiếu");
		btExportGDMSFF_VND.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportGDMSFF_VND.setIcon(FontAwesome.FILE_EXCEL_O);
		btExportGDMSFF_VND.addClickListener(event -> {
			doisoatDataListTemp = dataSourceGDMoneySendFF.getContent();
			exportDataDoiChieu(doisoatDataListTemp,"VND","GDMSFF");
		});
		
		btExportGDMSFF_USD = new Button("Xuất dữ liệu đối chiếu");
		btExportGDMSFF_USD.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportGDMSFF_USD.setIcon(FontAwesome.FILE_EXCEL_O);
		btExportGDMSFF_USD.addClickListener(event -> {
			doisoatDataListTemp = dataSourceGDMoneySendFF_USD.getContent();
			exportDataDoiChieu(doisoatDataListTemp,"USD","GDMSFF");
		});
		
		//----------------------------THANH QUYẾT TOÁN THEO LOẠI TIỀN VNĐ------------------------------------------
		//--- 1. GIAO DỊCH THẺ <MC/VS> CREDIT SCB THANH TOÁN HÀNG HÓA
		gridGDTTHHDuocTQT = new Grid();
		gridGDTTHHDuocTQT.setSizeFull();
		gridGDTTHHDuocTQT.setHeightByRows(10);
		gridGDTTHHDuocTQT.setHeightMode(HeightMode.ROW);
		gridGDTTHHDuocTQT.recalculateColumnWidths();
		
		containerGDTTHHDuocTQT = new IndexedContainer();
		
		containerGDTTHHDuocTQT.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("soThe", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("maGd", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("issuerCharge", String.class, "");
		
		gridGDTTHHDuocTQT.setContainerDataSource(containerGDTTHHDuocTQT);
		gridGDTTHHDuocTQT.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHDuocTQT.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHDuocTQT.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHDuocTQT.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHDuocTQT.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHDuocTQT.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHDuocTQT.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHDuocTQT.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHDuocTQT.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHDuocTQT.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHDuocTQT.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHDuocTQT.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDTTHHDuocTQT.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHDuocTQT.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHDuocTQT.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHDuocTQT.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		
		footerGDTTHHDuocTQT = gridGDTTHHDuocTQT.prependFooterRow();
		
		// 2. GIAO DỊCH THẺ <MC/VS> CREDIT SCB RÚT TIỀN MẶT
		gridGDRTMDuocTQT = new Grid();
		gridGDRTMDuocTQT.setSizeFull();
		gridGDRTMDuocTQT.setHeightByRows(10);
		gridGDRTMDuocTQT.setHeightMode(HeightMode.ROW);
		gridGDRTMDuocTQT.recalculateColumnWidths();
		
		containerGDRTMDuocTQT = new IndexedContainer();
		
		containerGDRTMDuocTQT.addContainerProperty("stt", Integer.class, "");
		containerGDRTMDuocTQT.addContainerProperty("soThe", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("maGd", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("ltgd", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("lttqt", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("issuerCharge", String.class, "");
		
		gridGDRTMDuocTQT.setContainerDataSource(containerGDRTMDuocTQT);
		gridGDRTMDuocTQT.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMDuocTQT.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMDuocTQT.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMDuocTQT.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMDuocTQT.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMDuocTQT.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMDuocTQT.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMDuocTQT.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMDuocTQT.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMDuocTQT.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMDuocTQT.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMDuocTQT.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMDuocTQT.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMDuocTQT.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDRTMDuocTQT.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMDuocTQT.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		
		footerGDRTMDuocTQT = gridGDRTMDuocTQT.appendFooterRow();
		
		
		// 3. GIAO DỊCH THẺ <MC/VS> CREDIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
		gridGDMoneySendFF = new Grid();
		gridGDMoneySendFF.setSizeFull();
		gridGDMoneySendFF.setHeightByRows(10);
		gridGDMoneySendFF.setHeightMode(HeightMode.ROW);
		gridGDMoneySendFF.recalculateColumnWidths();
		
		containerGDMoneySendFF = new IndexedContainer();
		
		containerGDMoneySendFF.addContainerProperty("stt", Integer.class, "");
		containerGDMoneySendFF.addContainerProperty("soThe", String.class, "");
		containerGDMoneySendFF.addContainerProperty("maGd", String.class, "");
		containerGDMoneySendFF.addContainerProperty("ngayGd", String.class, "");
		containerGDMoneySendFF.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDMoneySendFF.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("ltgd", String.class, "");
		containerGDMoneySendFF.addContainerProperty("lttqt", String.class, "");
		containerGDMoneySendFF.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("maCapPhep", String.class, "");
		containerGDMoneySendFF.addContainerProperty("dvcnt", String.class, "");
		containerGDMoneySendFF.addContainerProperty("reversalInd", String.class, "");
		containerGDMoneySendFF.addContainerProperty("issuerCharge", String.class, "");
		
		gridGDMoneySendFF.setContainerDataSource(containerGDMoneySendFF);
		gridGDMoneySendFF.getColumn("stt").setHeaderCaption("STT");
		gridGDMoneySendFF.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDMoneySendFF.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDMoneySendFF.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDMoneySendFF.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDMoneySendFF.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDMoneySendFF.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDMoneySendFF.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDMoneySendFF.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDMoneySendFF.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDMoneySendFF.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDMoneySendFF.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDMoneySendFF.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDMoneySendFF.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDMoneySendFF.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDMoneySendFF.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		
		footerGDMoneySendFF = gridGDMoneySendFF.prependFooterRow();
		
		
		//4. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> CREDIT SCB <TTHH/RTM>
		gridGDBaoCoBatThuong = new Grid();
		gridGDBaoCoBatThuong.setSizeFull();
		gridGDBaoCoBatThuong.setHeightByRows(10);
		gridGDBaoCoBatThuong.setHeightMode(HeightMode.ROW);
		gridGDBaoCoBatThuong.recalculateColumnWidths();
		
		containerGDBaoCoBatThuong = new IndexedContainer();
		
		containerGDBaoCoBatThuong.addContainerProperty("stt", Integer.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("soThe", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("maGd", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("ngayGd", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("ltgd", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("lttqt", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("maCapPhep", String.class, "");
		containerGDBaoCoBatThuong.addContainerProperty("dvcnt", String.class, "");
		
		gridGDBaoCoBatThuong.setContainerDataSource(containerGDBaoCoBatThuong);
		gridGDBaoCoBatThuong.getColumn("stt").setHeaderCaption("STT");
		gridGDBaoCoBatThuong.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDBaoCoBatThuong.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDBaoCoBatThuong.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDBaoCoBatThuong.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDBaoCoBatThuong.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDBaoCoBatThuong.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDBaoCoBatThuong.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDBaoCoBatThuong.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDBaoCoBatThuong.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDBaoCoBatThuong.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDBaoCoBatThuong.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDBaoCoBatThuong.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoCoBatThuong.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		//5. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> CREDIT SCB <TTHH/RTM>
		gridGDBaoNoBatThuong = new Grid();
		gridGDBaoNoBatThuong.setSizeFull();
		gridGDBaoNoBatThuong.setHeightByRows(10);
		gridGDBaoNoBatThuong.setHeightMode(HeightMode.ROW);
		gridGDBaoNoBatThuong.recalculateColumnWidths();
		
		containerGDBaoNoBatThuong = new IndexedContainer();
		
		containerGDBaoNoBatThuong.addContainerProperty("stt", Integer.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("soThe", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("maGd", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("ngayGd", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("ltgd", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("lttqt", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("maCapPhep", String.class, "");
		containerGDBaoNoBatThuong.addContainerProperty("dvcnt", String.class, "");
		
		gridGDBaoNoBatThuong.setContainerDataSource(containerGDBaoNoBatThuong);
		gridGDBaoNoBatThuong.getColumn("stt").setHeaderCaption("STT");
		gridGDBaoNoBatThuong.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDBaoNoBatThuong.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDBaoNoBatThuong.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDBaoNoBatThuong.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDBaoNoBatThuong.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDBaoNoBatThuong.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDBaoNoBatThuong.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDBaoNoBatThuong.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDBaoNoBatThuong.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDBaoNoBatThuong.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDBaoNoBatThuong.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDBaoNoBatThuong.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoNoBatThuong.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		
		//----------------------------THANH QUYẾT TOÁN THEO LOẠI TIỀN USD------------------------------------------
		//--- 1. GIAO DỊCH THẺ <MC/VS> CREDIT SCB THANH TOÁN HÀNG HÓA
		gridGDTTHHDuocTQT_USD = new Grid();
		gridGDTTHHDuocTQT_USD.setCaption("GD được thanh quyết toán");
		gridGDTTHHDuocTQT_USD.setSizeFull();
		gridGDTTHHDuocTQT_USD.setHeightByRows(10);
		gridGDTTHHDuocTQT_USD.setHeightMode(HeightMode.ROW);
		gridGDTTHHDuocTQT_USD.scrollToStart();
		gridGDTTHHDuocTQT_USD.recalculateColumnWidths();
		
		containerGDTTHHDuocTQT_USD = new IndexedContainer();
		
		containerGDTTHHDuocTQT_USD.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("soThe", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("maGd", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("issuerCharge", String.class, "");
		
		
		gridGDTTHHDuocTQT_USD.setContainerDataSource(containerGDTTHHDuocTQT_USD);
		gridGDTTHHDuocTQT_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHDuocTQT_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHDuocTQT_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHDuocTQT_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHDuocTQT_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHDuocTQT_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHDuocTQT_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHDuocTQT_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHDuocTQT_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHDuocTQT_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHDuocTQT_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHDuocTQT_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHDuocTQT_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHDuocTQT_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHDuocTQT_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHDuocTQT_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		
		footerGDTTHHDuocTQT_USD = gridGDTTHHDuocTQT_USD.appendFooterRow();
		
		//----------------------------GD cần hoàn trả phí + VAT (chỉ lọc các GD có code 25 và 06 được hoàn toàn phần)------------------//
		gridGDHTTTHH_USD = new Grid();
		gridGDHTTTHH_USD.setCaption("GD cần hoàn trả phí + VAT");
		gridGDHTTTHH_USD.setSizeFull();
		gridGDHTTTHH_USD.setHeightByRows(10);
		gridGDHTTTHH_USD.setHeightMode(HeightMode.ROW);
		gridGDHTTTHH_USD.recalculateColumnWidths();
		
		containerGDHTTTHH_USD= new IndexedContainer();
		
		containerGDHTTTHH_USD.addContainerProperty("stt", Integer.class, "");
		containerGDHTTTHH_USD.addContainerProperty("soThe", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("maGd", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("ltgd", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("lttqt", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stgdNguyenTe", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("loaiTienNguyenTe", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stgdGhiNoKh", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("stgdChenhLechTyGia", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("phiIsa", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("vatPhiIsa", BigDecimal.class, "");
		containerGDHTTTHH_USD.addContainerProperty("tenChuThe", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("dvpht", String.class, "");
		containerGDHTTTHH_USD.addContainerProperty("loc", String.class, "");
		
		gridGDHTTTHH_USD.setContainerDataSource(containerGDHTTTHH_USD);
		gridGDHTTTHH_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDHTTTHH_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDHTTTHH_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDHTTTHH_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDHTTTHH_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDHTTTHH_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDHTTTHH_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDHTTTHH_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDHTTTHH_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDHTTTHH_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDHTTTHH_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDHTTTHH_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDHTTTHH_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDHTTTHH_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDHTTTHH_USD.getColumn("stgdNguyenTe").setHeaderCaption("STGD nguyên tệ");
		gridGDHTTTHH_USD.getColumn("loaiTienNguyenTe").setHeaderCaption("Loại tiền nguyên tệ");
		gridGDHTTTHH_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDHTTTHH_USD.getColumn("stgdGhiNoKh").setHeaderCaption("STGD ghi nợ KH (VND");
		gridGDHTTTHH_USD.getColumn("stgdChenhLechTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDHTTTHH_USD.getColumn("phiIsa").setHeaderCaption("Phí ISA");
		gridGDHTTTHH_USD.getColumn("vatPhiIsa").setHeaderCaption("VAT phí ISA");
		gridGDHTTTHH_USD.getColumn("tenChuThe").setHeaderCaption("Tên chủ thẻ");
		gridGDHTTTHH_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDHTTTHH_USD.getColumn("loc").setHeaderCaption("LOC");
		
		// 2. GIAO DỊCH THẺ <MC/VS> CREDIT SCB RÚT TIỀN MẶT
		gridGDRTMDuocTQT_USD = new Grid();
		gridGDRTMDuocTQT_USD.setCaption("GD được thanh quyết toán");
		gridGDRTMDuocTQT_USD.setSizeFull();
		gridGDRTMDuocTQT_USD.setHeightByRows(10);
		gridGDRTMDuocTQT_USD.setHeightMode(HeightMode.ROW);
		gridGDRTMDuocTQT_USD.recalculateColumnWidths();
		
		containerGDRTMDuocTQT_USD = new IndexedContainer();
		
		containerGDRTMDuocTQT_USD.addContainerProperty("stt", Integer.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("soThe", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("maGd", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("ltgd", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("lttqt", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("issuerCharge", String.class, "");
		
		
		gridGDRTMDuocTQT_USD.setContainerDataSource(containerGDRTMDuocTQT_USD);
		gridGDRTMDuocTQT_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMDuocTQT_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMDuocTQT_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMDuocTQT_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMDuocTQT_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMDuocTQT_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMDuocTQT_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMDuocTQT_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMDuocTQT_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMDuocTQT_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMDuocTQT_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMDuocTQT_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMDuocTQT_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMDuocTQT_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMDuocTQT_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMDuocTQT_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		
		footerGDRTMDuocTQT_USD = gridGDRTMDuocTQT_USD.appendFooterRow();
		
		//----------------------------GD cần hoàn trả phí + VAT (lọc các GD có code 27 hoàn trả Toàn phần)------------------//
		gridGDHTRTM_USD = new Grid();
		gridGDHTRTM_USD.setCaption("GD cần hoàn trả phí + VAT");
		gridGDHTRTM_USD.setSizeFull();
		gridGDHTRTM_USD.setHeightByRows(10);
		gridGDHTRTM_USD.setHeightMode(HeightMode.ROW);
		gridGDHTRTM_USD.recalculateColumnWidths();
		
		containerGDHTRTM_USD= new IndexedContainer();
		
		containerGDHTRTM_USD.addContainerProperty("stt", Integer.class, "");
		containerGDHTRTM_USD.addContainerProperty("soThe", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("maGd", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("ltgd", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("lttqt", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("stgdNguyenTe", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("loaiTienNguyenTe", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("stgdGhiNoKh", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("stgdChenhLechTyGia", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("phiIsa", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("vatPhiIsa", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("phiRtm", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("vatPhiRtm", BigDecimal.class, "");
		containerGDHTRTM_USD.addContainerProperty("tenChuThe", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("dvpht", String.class, "");
		containerGDHTRTM_USD.addContainerProperty("loc", String.class, "");
		
		gridGDHTRTM_USD.setContainerDataSource(containerGDHTRTM_USD);
		gridGDHTRTM_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDHTRTM_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDHTRTM_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDHTRTM_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDHTRTM_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDHTRTM_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDHTRTM_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDHTRTM_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDHTRTM_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDHTRTM_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDHTRTM_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDHTRTM_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD VND");
		gridGDHTRTM_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDHTRTM_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDHTRTM_USD.getColumn("stgdNguyenTe").setHeaderCaption("STGD nguyên tệ");
		gridGDHTRTM_USD.getColumn("loaiTienNguyenTe").setHeaderCaption("Loại tiền nguyên tệ");
		gridGDHTRTM_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDHTRTM_USD.getColumn("stgdGhiNoKh").setHeaderCaption("STGD ghi nợ KH (VND");
		gridGDHTRTM_USD.getColumn("stgdChenhLechTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDHTRTM_USD.getColumn("phiIsa").setHeaderCaption("Phí ISA");
		gridGDHTRTM_USD.getColumn("vatPhiIsa").setHeaderCaption("VAT phí ISA");
		gridGDHTRTM_USD.getColumn("phiRtm").setHeaderCaption("Phí RTM");
		gridGDHTRTM_USD.getColumn("vatPhiRtm").setHeaderCaption("VAT phí RTM");
		gridGDHTRTM_USD.getColumn("tenChuThe").setHeaderCaption("Tên chủ thẻ");
		gridGDHTRTM_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDHTRTM_USD.getColumn("loc").setHeaderCaption("LOC");
		
		// 3. GIAO DỊCH THẺ <MC/VS> CREDIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
		gridGDMoneySendFF_USD = new Grid();
		gridGDMoneySendFF_USD.setSizeFull();
		gridGDMoneySendFF_USD.setHeightByRows(10);
		gridGDMoneySendFF_USD.setHeightMode(HeightMode.ROW);
		gridGDMoneySendFF_USD.recalculateColumnWidths();
		
		containerGDMoneySendFF_USD = new IndexedContainer();
		
		containerGDMoneySendFF_USD.addContainerProperty("stt", Integer.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("soThe", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("maGd", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("ltgd", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("lttqt", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("issuerCharge", String.class, "");
		
		
		gridGDMoneySendFF_USD.setContainerDataSource(containerGDMoneySendFF_USD);
		gridGDMoneySendFF_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDMoneySendFF_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDMoneySendFF_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDMoneySendFF_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDMoneySendFF_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDMoneySendFF_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDMoneySendFF_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDMoneySendFF_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDMoneySendFF_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDMoneySendFF_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDMoneySendFF_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDMoneySendFF_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDMoneySendFF_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDMoneySendFF_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDMoneySendFF_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDMoneySendFF_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		
		footerGDMoneySendFF_USD = gridGDMoneySendFF_USD.appendFooterRow();
		
		//4. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> CREDIT SCB <TTHH/RTM>
		gridGDBaoCoBatThuong_USD = new Grid();
		gridGDBaoCoBatThuong_USD.setSizeFull();
		gridGDBaoCoBatThuong_USD.setHeightByRows(10);
		gridGDBaoCoBatThuong_USD.setHeightMode(HeightMode.ROW);
		gridGDBaoCoBatThuong_USD.recalculateColumnWidths();
		
		containerGDBaoCoBatThuong_USD = new IndexedContainer();
		
		containerGDBaoCoBatThuong_USD.addContainerProperty("stt", Integer.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("soThe", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("maGd", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("ltgd", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("lttqt", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDBaoCoBatThuong_USD.addContainerProperty("dvcnt", String.class, "");
		
		gridGDBaoCoBatThuong_USD.setContainerDataSource(containerGDBaoCoBatThuong_USD);
		gridGDBaoCoBatThuong_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDBaoCoBatThuong_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDBaoCoBatThuong_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDBaoCoBatThuong_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDBaoCoBatThuong_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDBaoCoBatThuong_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDBaoCoBatThuong_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDBaoCoBatThuong_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDBaoCoBatThuong_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDBaoCoBatThuong_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDBaoCoBatThuong_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDBaoCoBatThuong_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDBaoCoBatThuong_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoCoBatThuong_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		//5. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> CREDIT SCB <TTHH/RTM>
		gridGDBaoNoBatThuong_USD = new Grid();
		gridGDBaoNoBatThuong_USD.setSizeFull();
		gridGDBaoNoBatThuong_USD.setHeightByRows(10);
		gridGDBaoNoBatThuong_USD.setHeightMode(HeightMode.ROW);
		gridGDBaoNoBatThuong_USD.recalculateColumnWidths();
		
		containerGDBaoNoBatThuong_USD = new IndexedContainer();
		
		containerGDBaoNoBatThuong_USD.addContainerProperty("stt", Integer.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("soThe", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("maGd", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("ltgd", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("lttqt", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDBaoNoBatThuong_USD.addContainerProperty("dvcnt", String.class, "");
		
		gridGDBaoNoBatThuong_USD.setContainerDataSource(containerGDBaoNoBatThuong_USD);
		gridGDBaoNoBatThuong_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDBaoNoBatThuong_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDBaoNoBatThuong_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDBaoNoBatThuong_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDBaoNoBatThuong_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDBaoNoBatThuong_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDBaoNoBatThuong_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDBaoNoBatThuong_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDBaoNoBatThuong_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDBaoNoBatThuong_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDBaoNoBatThuong_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDBaoNoBatThuong_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDBaoNoBatThuong_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoNoBatThuong_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		
		btGiaoDichTheTTHH = new Button("[+] 1. GIAO DỊCH THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB THANH TOÁN HÀNG HÓA");
		btGiaoDichTheTTHH.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGiaoDichTheRTM = new Button("[+] 2. GIAO DỊCH THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB RÚT TIỀN MẶT");
		btGiaoDichTheRTM.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGiaoDichMoneySendFastFund = new Button("[+] 3. GIAO DỊCH THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ NGÂN HÀNG LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)");
		btGiaoDichMoneySendFastFund.setStyleName(ValoTheme.BUTTON_LINK);

		btGDBaoCoBatThuong = new Button("[+] 4. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB <TTHH/RTM>");
		btGDBaoCoBatThuong.setStyleName(ValoTheme.BUTTON_LINK);

		btGDBaoNoBatThuong = new Button("[+] 5. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB <TTHH/RTM>");
		btGDBaoNoBatThuong.setStyleName(ValoTheme.BUTTON_LINK);
		
		final VerticalLayout verticalLayoutTQTTheoVND = new VerticalLayout();
		verticalLayoutTQTTheoVND.addComponent(btGiaoDichTheTTHH);
		verticalLayoutTQTTheoVND.addComponent(gridGDTTHHDuocTQT);
		verticalLayoutTQTTheoVND.addComponent(btExportGDTTHH_VND);
		
		verticalLayoutTQTTheoVND.addComponent(btGiaoDichTheRTM);
		verticalLayoutTQTTheoVND.addComponent(gridGDRTMDuocTQT);
		verticalLayoutTQTTheoVND.addComponent(btExportGDRTM_VND);
		
		verticalLayoutTQTTheoVND.addComponent(btGiaoDichMoneySendFastFund);
		verticalLayoutTQTTheoVND.addComponent(gridGDMoneySendFF);
		verticalLayoutTQTTheoVND.addComponent(btExportGDMSFF_VND);
		
		verticalLayoutTQTTheoVND.addComponent(btGDBaoCoBatThuong);
		verticalLayoutTQTTheoVND.addComponent(gridGDBaoCoBatThuong);
		
		verticalLayoutTQTTheoVND.addComponent(btGDBaoNoBatThuong);
		verticalLayoutTQTTheoVND.addComponent(gridGDBaoNoBatThuong);
		
		panelTQTTheoVND.setContent(verticalLayoutTQTTheoVND);
		
		final Panel panelTQTTheoUSD= new Panel("THANH QUYẾT TOÁN THEO LOẠI TIỀN USD");
		panelTQTTheoUSD.setStyleName(Reindeer.PANEL_LIGHT);
		panelTQTTheoUSD.setSizeFull();
		
		btGiaoDichTheTTHH_USD = new Button("[+] 1. GIAO DỊCH THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB THANH TOÁN HÀNG HÓA");
		btGiaoDichTheTTHH_USD.setStyleName(ValoTheme.BUTTON_LINK);

		btGiaoDichTheRTM_USD = new Button("[+] 2. GIAO DỊCH THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB RÚT TIỀN MẶT");
		btGiaoDichTheRTM_USD.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGiaoDichMoneySendFastFund_USD = new Button("[+] 3. GIAO DỊCH THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ NGÂN HÀNG LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)");
		btGiaoDichMoneySendFastFund_USD.setStyleName(ValoTheme.BUTTON_LINK);

		btGDBaoCoBatThuong_USD = new Button("[+] 4. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB <TTHH/RTM>");
		btGDBaoCoBatThuong_USD.setStyleName(ValoTheme.BUTTON_LINK);

		btGDBaoNoBatThuong_USD = new Button("[+] 5. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ " + (cardType.startsWith("V") ? "VS" : "MC") + " CREDIT SCB <TTHH/RTM>");
		btGDBaoNoBatThuong_USD.setStyleName(ValoTheme.BUTTON_LINK);
		
		
		switch(loaiGD) {
			case "GDTTHH":
				visibleAllGrid(false);
				
				//VND
				gridGDTTHHDuocTQT.setVisible(true);
				btGiaoDichTheTTHH.setCaption(btGiaoDichTheTTHH.getCaption().replace("+", "-"));
				btExportGDTTHH_VND.setVisible(true);
				//USD
				gridGDTTHHDuocTQT_USD.setVisible(true);
				gridGDHTTTHH_USD.setVisible(true);
				btGiaoDichTheTTHH_USD.setCaption(btGiaoDichTheTTHH_USD.getCaption().replace("+", "-"));
				btExportGDTTHH_USD.setVisible(true);
				break;
			case "GDRTIM":
				visibleAllGrid(false);
				//VND
				gridGDRTMDuocTQT.setVisible(true);
				btGiaoDichTheRTM.setCaption(btGiaoDichTheRTM.getCaption().replace("+", "-"));
				btExportGDRTM_VND.setVisible(true);
				//USD
				gridGDRTMDuocTQT_USD.setVisible(true);
				gridGDHTRTM_USD.setVisible(true);
				btExportHTRTM.setVisible(true);
				btGiaoDichTheRTM_USD.setCaption(btGiaoDichTheRTM_USD.getCaption().replace("+", "-"));
				btExportGDRTM_USD.setVisible(true);
				break;
			case "GDMSFF": 
				visibleAllGrid(false);
				//VND
				gridGDMoneySendFF.setVisible(true);
				btExportGDMSFF_VND.setVisible(true);
				btGiaoDichMoneySendFastFund.setCaption(btGiaoDichMoneySendFastFund.getCaption().replace("+", "-"));
				//USD
				gridGDMoneySendFF_USD.setVisible(true);
				btExportGDMSFF_USD.setVisible(true);
				btGiaoDichMoneySendFastFund_USD.setCaption(btGiaoDichMoneySendFastFund_USD.getCaption().replace("+", "-"));
				break;
			case "HTGDTT": 
				visibleAllGrid(false);
				break;
			case "HTGDRT": 
				visibleAllGrid(false);
				break;
			case "GDBCBT": 
				visibleAllGrid(false);
				//VND
				gridGDBaoCoBatThuong.setVisible(true);
				btGDBaoCoBatThuong.setCaption(btGDBaoCoBatThuong.getCaption().replace("+", "-"));
				//USD
				gridGDBaoCoBatThuong_USD.setVisible(true);
				btGDBaoCoBatThuong_USD.setCaption(btGDBaoCoBatThuong_USD.getCaption().replace("+", "-"));
				break;
			case "GDBNBT":
				visibleAllGrid(false);
				//VND
				gridGDBaoNoBatThuong.setVisible(true);
				btGDBaoNoBatThuong.setCaption(btGDBaoNoBatThuong.getCaption().replace("+", "-"));
				//USD
				gridGDBaoNoBatThuong_USD.setVisible(true);
				btGDBaoNoBatThuong_USD.setCaption(btGDBaoNoBatThuong_USD.getCaption().replace("+", "-"));
				break;
			case "All":
				visibleAllGrid(true);
				break;
		}
		
		switch(loaitienTqt) {
			case "VND":
				panelTQTTheoVND.setVisible(true);
				panelTQTTheoUSD.setVisible(false);
				break;
			case "USD":
				panelTQTTheoVND.setVisible(false);
				panelTQTTheoUSD.setVisible(true);
				break;
			case "All":
				panelTQTTheoVND.setVisible(true);
				panelTQTTheoUSD.setVisible(true);
				break;
		}
		
		//VND - Setup hide/show grid by button
		btGiaoDichTheTTHH.addClickListener(event -> {
			if(btGiaoDichTheTTHH.getCaption().contains("+")) {
				btGiaoDichTheTTHH.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDTTHHDuocTQT.setVisible(true);
				btExportGDTTHH_VND.setVisible(true);
			}
			else {
				btGiaoDichTheTTHH.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDTTHHDuocTQT.setVisible(false);
				btExportGDTTHH_VND.setVisible(false);
			}
		});
		
		btGiaoDichTheRTM.addClickListener(event -> {
			if(btGiaoDichTheRTM.getCaption().contains("+")) {
				btGiaoDichTheRTM.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDRTMDuocTQT.setVisible(true);
				btExportGDRTM_VND.setVisible(true);
			}
			else {
				btGiaoDichTheRTM.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDRTMDuocTQT.setVisible(false);
				btExportGDRTM_VND.setVisible(false);
			}
		});
		
		btGiaoDichMoneySendFastFund.addClickListener(event -> {
			if(btGiaoDichMoneySendFastFund.getCaption().contains("+")) {
				btGiaoDichMoneySendFastFund.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDMoneySendFF.setVisible(true);
				btExportGDMSFF_VND.setVisible(true);
			}
			else {
				btGiaoDichMoneySendFastFund.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDMoneySendFF.setVisible(false);
				btExportGDMSFF_VND.setVisible(false);
			}
		});
		
		btGDBaoCoBatThuong.addClickListener(event -> {
			if(btGDBaoCoBatThuong.getCaption().contains("+")) {
				btGDBaoCoBatThuong.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDBaoCoBatThuong.setVisible(true);
			}
			else {
				btGDBaoCoBatThuong.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDBaoCoBatThuong.setVisible(false);
			}
		});
		
		btGDBaoNoBatThuong.addClickListener(event -> {
			if(btGDBaoNoBatThuong.getCaption().contains("+")) {
				btGDBaoNoBatThuong.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDBaoNoBatThuong.setVisible(true);
			}
			else {
				btGDBaoNoBatThuong.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDBaoNoBatThuong.setVisible(false);
			}
		});
		
		//USD - Setup hide/show grid by button
		btGiaoDichTheTTHH_USD.addClickListener(event -> {
			if(btGiaoDichTheTTHH_USD.getCaption().contains("+")) {
				btGiaoDichTheTTHH_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDTTHHDuocTQT_USD.setVisible(true);
				btExportGDTTHH_USD.setVisible(true);
				gridGDHTTTHH_USD.setVisible(true);
				btExportHTTTHH.setVisible(true);
			}
			else {
				btGiaoDichTheTTHH_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDTTHHDuocTQT_USD.setVisible(false);
				btExportGDTTHH_USD.setVisible(false);
				gridGDHTTTHH_USD.setVisible(false);
				btExportHTTTHH.setVisible(false);
			}
		});
		
		btGiaoDichTheRTM_USD.addClickListener(event -> {
			if(btGiaoDichTheRTM_USD.getCaption().contains("+")) {
				btGiaoDichTheRTM_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDRTMDuocTQT_USD.setVisible(true);
				gridGDHTRTM_USD.setVisible(true);
				btExportHTRTM.setVisible(true);
				btExportGDRTM_USD.setVisible(true);
			}
			else {
				btGiaoDichTheRTM_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDRTMDuocTQT_USD.setVisible(false);
				gridGDHTRTM_USD.setVisible(false);
				btExportHTRTM.setVisible(false);
				btExportGDRTM_USD.setVisible(false);
			}
		});
		
		btGiaoDichMoneySendFastFund_USD.addClickListener(event -> {
			if(btGiaoDichMoneySendFastFund_USD.getCaption().contains("+")) {
				btGiaoDichMoneySendFastFund_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDMoneySendFF_USD.setVisible(true);
				btExportGDMSFF_USD.setVisible(true);
			}
			else {
				btGiaoDichMoneySendFastFund_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDMoneySendFF_USD.setVisible(false);
				btExportGDMSFF_USD.setVisible(false);
			}
		});
		
		btGDBaoCoBatThuong_USD.addClickListener(event -> {
			if(btGDBaoCoBatThuong_USD.getCaption().contains("+")) {
				btGDBaoCoBatThuong_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDBaoCoBatThuong_USD.setVisible(true);
			}
			else {
				btGDBaoCoBatThuong_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDBaoCoBatThuong_USD.setVisible(false);
			}
		});
		
		btGDBaoNoBatThuong_USD.addClickListener(event -> {
			if(btGDBaoNoBatThuong_USD.getCaption().contains("+")) {
				btGDBaoNoBatThuong_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDBaoNoBatThuong_USD.setVisible(true);
			}
			else {
				btGDBaoNoBatThuong_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDBaoNoBatThuong_USD.setVisible(false);
			}
		});
		
		
		
		final VerticalLayout verticalLayoutTQTTheoUSD = new VerticalLayout();
		verticalLayoutTQTTheoUSD.addComponent(btGiaoDichTheTTHH_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDTTHHDuocTQT_USD);
		verticalLayoutTQTTheoUSD.addComponent(btExportGDTTHH_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDHTTTHH_USD);
		verticalLayoutTQTTheoUSD.addComponent(btExportHTTTHH);
		
		verticalLayoutTQTTheoUSD.addComponent(btGiaoDichTheRTM_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDRTMDuocTQT_USD);
		verticalLayoutTQTTheoUSD.addComponent(btExportGDRTM_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDHTRTM_USD);
		verticalLayoutTQTTheoUSD.addComponent(btExportHTRTM);
		
		verticalLayoutTQTTheoUSD.addComponent(btGiaoDichMoneySendFastFund_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDMoneySendFF_USD);
		verticalLayoutTQTTheoUSD.addComponent(btExportGDMSFF_USD);
		
		verticalLayoutTQTTheoUSD.addComponent(btGDBaoCoBatThuong_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDBaoCoBatThuong_USD);
		
		verticalLayoutTQTTheoUSD.addComponent(btGDBaoNoBatThuong_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDBaoNoBatThuong_USD);
		
		panelTQTTheoUSD.setContent(verticalLayoutTQTTheoUSD);
		
		switch(loaitienTqt) {
			case "VND":
				switch(loaiGD) {
					case "GDTTHH":
						initGridGDTTHHDuocTQT("VND");
						break;
					case "GDRTIM":
						initGridGDRTMDuocTQT("VND");
						break;
					case "GDMSFF": 
						initGridGDMoneySendFF("VND");
						break;
					case "GDBCBT": 
						initGridGDBaoCoBatThuong("VND");
						break;
					case "GDBNBT":
						initGridGDBaoNoBatThuong("VND");
						break;
					case "All":
						//VND - INIT & LOAT DATA 
						initGridGDTTHHDuocTQT("VND");
						initGridGDRTMDuocTQT("VND");
						initGridGDMoneySendFF("VND");
						initGridGDBaoCoBatThuong("VND");
						initGridGDBaoNoBatThuong("VND");
						break;
				}
				
			case "USD":
				switch(loaiGD) {
					case "GDTTHH":
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDHTTTHH_USD("USD");
						break;
					case "GDRTIM":
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDHTRTM_USD("USD");
						break;
					case "GDMSFF": 
						initGridGDMoneySendFF_USD("USD");
						break;
					case "GDBCBT": 
						initGridGDBaoCoBatThuong_USD("USD");
						break;
					case "GDBNBT":
						initGridGDBaoNoBatThuong_USD("USD");
						break;
					case "All":
						//USD - INIT & LOAT DATA 
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDHTTTHH_USD("USD");
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDHTRTM_USD("USD");
						initGridGDMoneySendFF_USD("USD");
						initGridGDBaoCoBatThuong_USD("USD");
						initGridGDBaoNoBatThuong_USD("USD");
						break;
				}
				
			default:
				switch(loaiGD) {
					case "GDTTHH":
						initGridGDTTHHDuocTQT("VND");
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDHTTTHH_USD("USD");
						break;
					case "GDRTIM":
						initGridGDRTMDuocTQT("VND");
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDHTRTM_USD("USD");
						break;
					case "GDMSFF": 
						initGridGDMoneySendFF("VND");
						initGridGDMoneySendFF_USD("USD");
						break;
					case "GDBCBT": 
						initGridGDBaoCoBatThuong("VND");
						initGridGDBaoCoBatThuong_USD("USD");
						break;
					case "GDBNBT":
						initGridGDBaoNoBatThuong("VND");
						initGridGDBaoNoBatThuong_USD("USD");
						break;
					case "All":
						//VND - INIT & LOAT DATA 
						initGridGDTTHHDuocTQT("VND");
						initGridGDRTMDuocTQT("VND");
						initGridGDMoneySendFF("VND");
						initGridGDBaoCoBatThuong("VND");
						initGridGDBaoNoBatThuong("VND");
						//USD - INIT & LOAT DATA 
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDHTTTHH_USD("USD");
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDHTRTM_USD("USD");
						initGridGDMoneySendFF_USD("USD");
						initGridGDBaoCoBatThuong_USD("USD");
						initGridGDBaoNoBatThuong_USD("USD");
						break;
				}
				
		}
		
		btExport = new Button("EXPORT");
		btExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btExport.setWidth(120.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.FILE_EXCEL_O);
		
		btClose = new Button("CLOSE");
		btClose.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btClose.setWidth(120.0f, Unit.PIXELS);
		btClose.setIcon(FontAwesome.CLOSE);
		
		btExport.addClickListener(event -> {
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			StreamResource myResourceXLSX;
			filename = "DoiChieuTqtCredit.jasper";
			addExtension(downloader);
			myResourceXLSX = createTransMKResourceXLS(cardType + "_" + "DoiChieuTQT" + "_ADV" + ngayAdv + "_" + loaitienTqt);
			downloader.setFileDownloadResource(myResourceXLSX);
			
//			switch(loaitienTqt) {
//				case "VND": 
//					filename = "DoiChieuTqtCredit.jasper";
//					addExtension(downloader);
//					myResourceXLSX = createTransMKResourceXLS("DoiChieuTQT_USD);
//					downloader.setFileDownloadResource(myResourceXLSX);
//					break;
//				case "USD": 
//					filename = "DoiChieuTqtCredit.jasper";
//					addExtension(downloader);
//					myResourceXLSX = createTransMKResourceXLSDoiChieuTqtUsd();
//					downloader.setFileDownloadResource(myResourceXLSX);
//					break;
//			}
			
			downloader.download();
		});
		
		btExportHTTTHH.addClickListener(event -> {
			loaigd = "GDHTTTHH";
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			StreamResource myResourceXLSX;
			filename = "RptHoanTraPhi.jasper";
			addExtension(downloader);
			myResourceXLSX = createTransMKResourceXLS(cardType + "_" + "FileHoanTraPhi_ADV" + ngayAdv + "_" + "HTTTHH");
			downloader.setFileDownloadResource(myResourceXLSX);
			downloader.download();
		});
		
		btExportHTRTM.addClickListener(event -> {
			loaigd = "GDHTRTM";
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			StreamResource myResourceXLSX;
			filename = "RptHoanTraPhi.jasper";
			addExtension(downloader);
			myResourceXLSX = createTransMKResourceXLS(cardType + "_" + "FileHoanTraPhi_ADV" + ngayAdv + "_" + "HTRTM");
			downloader.setFileDownloadResource(myResourceXLSX);
			downloader.download();
		});
		
		final HorizontalLayout horizontalLayoutSpace = new HorizontalLayout();
		
		final HorizontalLayout layoutRowFooter = new HorizontalLayout();
		layoutRowFooter.setSpacing(true);
		layoutRowFooter.addComponent(btExport);
		layoutRowFooter.addComponent(btClose);
		layoutRowFooter.setComponentAlignment(btExport, Alignment.MIDDLE_CENTER);
		layoutRowFooter.setComponentAlignment(btClose, Alignment.MIDDLE_CENTER);
		
		final VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(panelTQTTheoVND);
		verticalLayout.addComponent(horizontalLayoutSpace);
		verticalLayout.addComponent(panelTQTTheoUSD);
		verticalLayout.addComponent(layoutRowFooter);
		verticalLayout.setComponentAlignment(layoutRowFooter, Alignment.MIDDLE_CENTER);
		addComponent(verticalLayout);
	}

	@FunctionalInterface
	public interface Callback {
		void closeWindow();
	}
	
	private void visibleAllGrid(boolean isShow) {
		if(isShow)
		{
			//VND
			gridGDTTHHDuocTQT.setVisible(true);
			gridGDMoneySendFF.setVisible(true);
			gridGDRTMDuocTQT.setVisible(true);
			gridGDBaoCoBatThuong.setVisible(true);
			gridGDBaoNoBatThuong.setVisible(true);
			
			btGiaoDichTheTTHH.setCaption(btGiaoDichTheTTHH.getCaption().replace("+", "-"));
			btGiaoDichTheRTM.setCaption(btGiaoDichTheRTM.getCaption().replace("+", "-"));
			btGiaoDichMoneySendFastFund.setCaption(btGiaoDichMoneySendFastFund.getCaption().replace("+", "-"));
			btGDBaoCoBatThuong.setCaption(btGDBaoCoBatThuong.getCaption().replace("+", "-"));
			btGDBaoNoBatThuong.setCaption(btGDBaoNoBatThuong.getCaption().replace("+", "-"));
			
			//USD
			gridGDTTHHDuocTQT_USD.setVisible(true);
			gridGDHTTTHH_USD.setVisible(true);
			gridGDMoneySendFF_USD.setVisible(true);
			gridGDRTMDuocTQT_USD.setVisible(true);
			gridGDHTRTM_USD.setVisible(true);
			gridGDBaoCoBatThuong_USD.setVisible(true);
			gridGDBaoNoBatThuong_USD.setVisible(true);
			
			btGiaoDichTheTTHH_USD.setCaption(btGiaoDichTheTTHH_USD.getCaption().replace("+", "-"));
			btGiaoDichTheRTM_USD.setCaption(btGiaoDichTheRTM_USD.getCaption().replace("+", "-"));
			btGiaoDichMoneySendFastFund_USD.setCaption(btGiaoDichMoneySendFastFund_USD.getCaption().replace("+", "-"));
			btGDBaoCoBatThuong_USD.setCaption(btGDBaoCoBatThuong_USD.getCaption().replace("+", "-"));
			btGDBaoNoBatThuong_USD.setCaption(btGDBaoNoBatThuong_USD.getCaption().replace("+", "-"));
			
		}
		else
		{
			//VND
			gridGDTTHHDuocTQT.setVisible(false);
			gridGDMoneySendFF.setVisible(false);
			gridGDRTMDuocTQT.setVisible(false);
			gridGDBaoCoBatThuong.setVisible(false);
			gridGDBaoNoBatThuong.setVisible(false);
			
			btGiaoDichTheTTHH.setCaption(btGiaoDichTheTTHH.getCaption().replace("-", "+"));
			btGiaoDichTheRTM.setCaption(btGiaoDichTheRTM.getCaption().replace("-", "+"));
			btGiaoDichMoneySendFastFund.setCaption(btGiaoDichMoneySendFastFund.getCaption().replace("-", "+"));
			btGDBaoCoBatThuong.setCaption(btGDBaoCoBatThuong.getCaption().replace("-", "+"));
			btGDBaoNoBatThuong.setCaption(btGDBaoNoBatThuong.getCaption().replace("-", "+"));
			
			//USD
			gridGDTTHHDuocTQT_USD.setVisible(false);
			gridGDHTTTHH_USD.setVisible(false);
			gridGDMoneySendFF_USD.setVisible(false);
			gridGDRTMDuocTQT_USD.setVisible(false);
			gridGDHTRTM_USD.setVisible(false);
			gridGDBaoCoBatThuong_USD.setVisible(false);
			gridGDBaoNoBatThuong_USD.setVisible(false);
			
			btGiaoDichTheTTHH_USD.setCaption(btGiaoDichTheTTHH_USD.getCaption().replace("-", "+"));
			btGiaoDichTheRTM_USD.setCaption(btGiaoDichTheRTM_USD.getCaption().replace("-", "+"));
			btGiaoDichMoneySendFastFund_USD.setCaption(btGiaoDichMoneySendFastFund_USD.getCaption().replace("-", "+"));
			btGDBaoCoBatThuong_USD.setCaption(btGDBaoCoBatThuong_USD.getCaption().replace("-", "+"));
			btGDBaoNoBatThuong_USD.setCaption(btGDBaoNoBatThuong_USD.getCaption().replace("-", "+"));
		}
	}
	
	//1. GIAO DỊCH THẺ <MC/VS> CREDIT SCB THANH TOÁN HÀNG HÓA
	private Page<DoiSoatData> getDataGDTTHHDuocTQT(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr) 
						&& (i.getMaGd().startsWith("00") 
							|| i.getMaGd().startsWith("18")
							|| i.getMaGd().startsWith("20") 
							|| i.getMaGd().startsWith("05") 
							|| i.getMaGd().startsWith("26")
							|| i.getMaGd().startsWith("25")
							|| (i.getMaGd().startsWith("06") && !i.getMerchantCity().contains("Visa Direct")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	private Page<DoiSoatData> getDataGDHTTTHH(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr) && (i.getMaGd().startsWith("20") || (i.getMaGd().startsWith("00") && i.getReversalInd().equals("R"))|| i.getMaGd().startsWith("25") || (i.getMaGd().startsWith("06") && !i.getMerchantCity().contains("Visa Direct")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	
	//2. GIAO DỊCH THẺ <MC/VS> CREDIT SCB RÚT TIỀN MẶT
	private Page<DoiSoatData> getDataGDRTMDuocTQT(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& (i.getMaGd().startsWith("01") 
							|| i.getMaGd().startsWith("12")
							|| i.getMaGd().startsWith("07")
							||i.getMaGd().startsWith("27"))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	private Page<DoiSoatData> getDataGDHTRTM(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr) && ((i.getMaGd().startsWith("01") && i.getReversalInd().equals("R")) || i.getMaGd().startsWith("27"))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	//3. GIAO DỊCH THẺ <MC/VS> CREDIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
	private Page<DoiSoatData> getDataGDMoneySendFF(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& (i.getMaGd().startsWith("28") 
							|| (i.getMaGd().startsWith("06") && i.getMerchantCity().contains("Visa Direct")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	//4. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> CREDIT SCB <TTHH/RTM>
	private Page<DoiSoatData> getDataGDBaoCoBatThuong(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& ((i.getMaGd().startsWith("19") && cardType.equals("MC")) || (i.getMaGd().startsWith("20") && cardType.equals("VS")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	//5. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> CREDIT SCB <TTHH/RTM>
	private Page<DoiSoatData> getDataGDBaoNoBatThuong(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& ((i.getMaGd().startsWith("29") && cardType.equals("MC")) || (i.getMaGd().startsWith("10") && cardType.equals("VS")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
		return result;
	}
	
	//VND - INIT GRID
	public void initGridGDTTHHDuocTQT(String curr) {
		dataSourceGDTTHHDuocTQT = getDataGDTTHHDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHDuocTQT() == true) {
//			if (!gridGDTTHHDuocTQT.isVisible()) {
//				gridGDTTHHDuocTQT.setVisible(true);
//			}
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerGDTTHHDuocTQT.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerGDTTHHDuocTQT.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		}
	
	}
	
	
	
	
	public void initGridGDRTMDuocTQT(String curr) {
		dataSourceGDRTMDuocTQT = getDataGDRTMDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMDuocTQT() == true) {
//			if (!gridGDRTMDuocTQT.isVisible()) {
//				gridGDRTMDuocTQT.setVisible(true);
//			}
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerGDRTMDuocTQT.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerGDRTMDuocTQT.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
		}
	
	}
	
	
	public void initGridGDMoneySendFF(String curr) {
		dataSourceGDMoneySendFF = getDataGDMoneySendFF(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDMoneySendFF() == true) {
//			if (!gridGDMoneySendFF.isVisible()) {
//				gridGDMoneySendFF.setVisible(true);
//			}
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerGDMoneySendFF.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerGDMoneySendFF.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		}
	
	}
	
	
	public void initGridGDBaoCoBatThuong(String curr) {
		dataSourceGDBaoCoBatThuong = getDataGDBaoCoBatThuong(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDBaoCoBatThuong() == true) {
//			if (!gridGDBaoCoBatThuong.isVisible()) {
//				gridGDBaoCoBatThuong.setVisible(true);
//			}
		}
		
	}
	
	public void initGridGDBaoNoBatThuong(String curr) {
		dataSourceGDBaoNoBatThuong = getDataGDBaoNoBatThuong(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDBaoNoBatThuong() == true) {
//			if (!gridGDBaoNoBatThuong.isVisible()) {
//				gridGDBaoNoBatThuong.setVisible(true);
//			}
		}
	}
	
	
	//USD - INIT GRID
	public void initGridGDTTHHDuocTQT_USD(String curr) {
		dataSourceGDTTHHDuocTQT_USD = getDataGDTTHHDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHDuocTQT_USD() == true) {
//			if (!gridGDTTHHDuocTQT_USD.isVisible()) {
//				gridGDTTHHDuocTQT_USD.setVisible(true);
//			}
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			BigDecimal interchangeTotal = BigDecimal.valueOf(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue());
			BigDecimal stTqtTotal = BigDecimal.valueOf(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue());
//			BigDecimal tygiaGdTTHH = tygiaTqt.getTyGiaGdTthh();
			
			footerGDTTHHDuocTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tổng cộng</div>");
			footerGDTTHHDuocTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(stTqtTotal)+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().setScale(0, RoundingMode.HALF_UP).doubleValue())+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(interchangeTotal)+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			
		}
	
	}
	
	public void initGridGDHTTTHH_USD(String curr) {
		dataSourceGDHTTTHH_USD = getDataGDHTTTHH(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		createDataForContainerGDHTTTHH_USD();
	
	}
	
	public void initGridGDRTMDuocTQT_USD(String curr) {
		dataSourceGDRTMDuocTQT_USD = getDataGDRTMDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMDuocTQT_USD() == true) {
//			if (!gridGDRTMDuocTQT_USD.isVisible()) {
//				gridGDRTMDuocTQT_USD.setVisible(true);
//			}
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerGDRTMDuocTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tổng cộng</div>");
			footerGDRTMDuocTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().setScale(0, RoundingMode.HALF_UP).doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
//			BigDecimal tygiaGdRTM = tygiaTqt.getTyGiaGdRtm();
			
		}
	
	}
	
	public void initGridGDHTRTM_USD(String curr) {
		dataSourceGDHTRTM_USD = getDataGDHTRTM(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		createDataForContainerGDHTRTM_USD();
	
	}
	
	public void initGridGDMoneySendFF_USD(String curr) {
		dataSourceGDMoneySendFF_USD = getDataGDMoneySendFF(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDMoneySendFF_USD() == true) {
//			if (!gridGDMoneySendFF_USD.isVisible()) {
//				gridGDMoneySendFF_USD.setVisible(true);
//			}
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerGDMoneySendFF_USD.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerGDMoneySendFF_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().setScale(0, RoundingMode.HALF_UP).doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
//			BigDecimal tygiaGdMSFF = tygiaTqt.getTyGiaGdMsff();
			
			
		}
	
	}
	
	
	public void initGridGDBaoCoBatThuong_USD(String curr) {
		dataSourceGDBaoCoBatThuong_USD = getDataGDBaoCoBatThuong(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDBaoCoBatThuong_USD() == true) {
//			if (!gridGDBaoCoBatThuong_USD.isVisible()) {
//				gridGDBaoCoBatThuong_USD.setVisible(true);
//			}
		}
		
	}
	
	public void initGridGDBaoNoBatThuong_USD(String curr) {
		dataSourceGDBaoNoBatThuong_USD = getDataGDBaoNoBatThuong(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDBaoNoBatThuong_USD() == true) {
//			if (!gridGDBaoNoBatThuong_USD.isVisible()) {
//				gridGDBaoNoBatThuong_USD.setVisible(true);
//			}
		}
	}
	
	//VND - LOAD DATA INTO GRID
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHDuocTQT() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHDuocTQT != null && dataSourceGDTTHHDuocTQT.getTotalElements()>0) {
			containerGDTTHHDuocTQT.removeAllItems();
			dataSourceGDTTHHDuocTQT.forEach(s -> {
				stt++;
				Item item = containerGDTTHHDuocTQT.getItem(containerGDTTHHDuocTQT.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDRTMDuocTQT() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMDuocTQT != null && dataSourceGDRTMDuocTQT.getTotalElements()>0) {
			containerGDRTMDuocTQT.removeAllItems();
			dataSourceGDRTMDuocTQT.forEach(s -> {
				stt++;
				try {
					Item item = containerGDRTMDuocTQT.getItem(containerGDRTMDuocTQT.addItem());
					item.getItemProperty("stt").setValue(stt);
					item.getItemProperty("soThe").setValue(s.getSoThe());
					item.getItemProperty("maGd").setValue(s.getMaGd());
					item.getItemProperty("ngayGd").setValue(s.getNgayGd());
					item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
					item.getItemProperty("stGd").setValue(s.getStGd());
					item.getItemProperty("stTqt").setValue(s.getStTqt());
					item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
					item.getItemProperty("ltgd").setValue(s.getLtgd());
					item.getItemProperty("lttqt").setValue(s.getLttqt());
					item.getItemProperty("interchange").setValue(s.getInterchange());
					item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
					item.getItemProperty("dvcnt").setValue(s.getDvcnt());
					item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
					item.getItemProperty("reversalInd").setValue(s.getReversalInd());
					item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				}
				catch(Exception e) {
				  e.printStackTrace();
				}
			
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDMoneySendFF() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDMoneySendFF != null && dataSourceGDMoneySendFF.getTotalElements()>0) {
			containerGDMoneySendFF.removeAllItems();
			dataSourceGDMoneySendFF.forEach(s -> {
				stt++;
				Item item = containerGDMoneySendFF.getItem(containerGDMoneySendFF.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDBaoCoBatThuong() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDBaoCoBatThuong != null && dataSourceGDBaoCoBatThuong.getTotalElements()>0) {
			containerGDBaoCoBatThuong.removeAllItems();
			dataSourceGDBaoCoBatThuong.forEach(s -> {
				stt++;
				Item item = containerGDBaoCoBatThuong.getItem(containerGDBaoCoBatThuong.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDBaoNoBatThuong() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDBaoNoBatThuong != null && dataSourceGDBaoNoBatThuong.getTotalElements()>0) {
			containerGDBaoNoBatThuong.removeAllItems();
			dataSourceGDBaoNoBatThuong.forEach(s -> {
				stt++;
				Item item = containerGDBaoNoBatThuong.getItem(containerGDBaoNoBatThuong.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	
	//USD - LOAD DATA INTO GRID
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHDuocTQT_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHDuocTQT_USD != null && dataSourceGDTTHHDuocTQT_USD.getTotalElements()>0) {
			containerGDTTHHDuocTQT_USD.removeAllItems();
			dataSourceGDTTHHDuocTQT_USD.forEach(s -> {
				stt++;
				Item item = containerGDTTHHDuocTQT_USD.getItem(containerGDTTHHDuocTQT_USD.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd().setScale(0, RoundingMode.HALF_UP));
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDHTTTHH_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDHTTTHH_USD != null && dataSourceGDHTTTHH_USD.getTotalElements()>0) {
			containerGDHTTTHH_USD.removeAllItems();
			dataSourceGDHTTTHH_USD.forEach(s -> {
				stt++;
				Item item = containerGDHTTTHH_USD.getItem(containerGDHTTTHH_USD.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd().setScale(0, RoundingMode.HALF_UP));
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("stgdNguyenTe").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("loaiTienNguyenTe").setValue(s.getLoaiTienNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdGhiNoKh").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("phiIsa").setValue(s.getPhiIsaGd());
				item.getItemProperty("vatPhiIsa").setValue(s.getVatPhiIsaGd());
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("loc").setValue(s.getLoc());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDRTMDuocTQT_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMDuocTQT_USD != null && dataSourceGDRTMDuocTQT_USD.getTotalElements()>0) {
			containerGDRTMDuocTQT_USD.removeAllItems();
			dataSourceGDRTMDuocTQT_USD.forEach(s -> {
				stt++;
				try {
					Item item = containerGDRTMDuocTQT_USD.getItem(containerGDRTMDuocTQT_USD.addItem());
					item.getItemProperty("stt").setValue(stt);
					item.getItemProperty("soThe").setValue(s.getSoThe());
					item.getItemProperty("maGd").setValue(s.getMaGd());
					item.getItemProperty("ngayGd").setValue(s.getNgayGd());
					item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
					item.getItemProperty("stGd").setValue(s.getStGd());
					item.getItemProperty("stTqt").setValue(s.getStTqt());
					item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
					item.getItemProperty("ltgd").setValue(s.getLtgd());
					item.getItemProperty("lttqt").setValue(s.getLttqt());
					item.getItemProperty("interchange").setValue(s.getInterchange());
					item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
					item.getItemProperty("dvcnt").setValue(s.getDvcnt());
					item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
					item.getItemProperty("reversalInd").setValue(s.getReversalInd());
					item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				}
				catch(Exception e) {
				  e.printStackTrace();
				}
			
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDHTRTM_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDHTRTM_USD != null && dataSourceGDHTRTM_USD.getTotalElements()>0) {
			containerGDHTRTM_USD.removeAllItems();
			dataSourceGDHTRTM_USD.forEach(s -> {
				stt++;
				try {
					Item item = containerGDHTRTM_USD.getItem(containerGDHTRTM_USD.addItem());
					item.getItemProperty("stt").setValue(stt);
					item.getItemProperty("soThe").setValue(s.getSoThe());
					item.getItemProperty("maGd").setValue(s.getMaGd());
					item.getItemProperty("ngayGd").setValue(s.getNgayGd());
					item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
					item.getItemProperty("stGd").setValue(s.getStGd());
					item.getItemProperty("stTqt").setValue(s.getStTqt());
					item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
					item.getItemProperty("ltgd").setValue(s.getLtgd());
					item.getItemProperty("lttqt").setValue(s.getLttqt());
					item.getItemProperty("interchange").setValue(s.getInterchange());
					item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
					item.getItemProperty("dvcnt").setValue(s.getDvcnt());
					item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
					item.getItemProperty("stgdNguyenTe").setValue(s.getStgdNguyenTeGd());
					item.getItemProperty("loaiTienNguyenTe").setValue(s.getLoaiTienNguyenTeGd());
					item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
					item.getItemProperty("stgdGhiNoKh").setValue(s.getStTrichNoKhGd());
					item.getItemProperty("stgdChenhLechTyGia").setValue(s.getStgdChenhLechDoTyGia());
					item.getItemProperty("phiIsa").setValue(s.getPhiIsaGd());
					item.getItemProperty("vatPhiIsa").setValue(s.getVatPhiIsaGd());
					item.getItemProperty("phiRtm").setValue(s.getPhiRtmGd());
					item.getItemProperty("vatPhiRtm").setValue(s.getVatPhiRtmGd());
					item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
					item.getItemProperty("dvpht").setValue(s.getDvpht());
					item.getItemProperty("loc").setValue(s.getLoc());
				}
				catch(Exception e) {
				  e.printStackTrace();
				}
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDMoneySendFF_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDMoneySendFF_USD != null && dataSourceGDMoneySendFF_USD.getTotalElements()>0) {
			containerGDMoneySendFF_USD.removeAllItems();
			dataSourceGDMoneySendFF_USD.forEach(s -> {
				stt++;
				Item item = containerGDMoneySendFF_USD.getItem(containerGDMoneySendFF_USD.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDBaoCoBatThuong_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDBaoCoBatThuong_USD != null && dataSourceGDBaoCoBatThuong_USD.getTotalElements()>0) {
			containerGDBaoCoBatThuong_USD.removeAllItems();
			dataSourceGDBaoCoBatThuong_USD.forEach(s -> {
				stt++;
				Item item = containerGDBaoCoBatThuong_USD.getItem(containerGDBaoCoBatThuong_USD.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDBaoNoBatThuong_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDBaoNoBatThuong_USD != null && dataSourceGDBaoNoBatThuong_USD.getTotalElements()>0) {
			containerGDBaoNoBatThuong_USD.removeAllItems();
			dataSourceGDBaoNoBatThuong_USD.forEach(s -> {
				stt++;
				Item item = containerGDBaoNoBatThuong_USD.getItem(containerGDBaoNoBatThuong_USD.addItem());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}


	public Map<String, Object> getParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(ngayAdv, formatter);
        LocalDate ngayadv_vnd = localDate.plusDays(1);
        String s_ngayadv = formatter.format(ngayadv_vnd);
        parameters.put("p_ngayadv_vnd", s_ngayadv);
		parameters.put("p_ngayadv", ngayAdv);
		parameters.put("p_loaitien", loaitienTqt);
		parameters.put("p_cardbrn", cardType);
		parameters.put("p_loaigd", loaigd);
		
		return parameters;
	}
	
	private ByteArrayOutputStream makeFileForDownLoad(String filename, String extension) throws JRException, SQLException {

		Connection con = localDataSource.getConnection();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (this.getParameter() != null) {
			// Tham so truyen vao cho bao cao
			Map<String, Object> parameters = this.getParameter();

			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(configurationHelper.getPathFileRoot() + "/dstqt/" + filename);
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
	public StreamResource createTransMKResourceXLS(String exportFileName) {
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
		}, exportFileName + ".xlsx");
	}
	
	public boolean checkValidator() {
//		try {
//			dfTuNgay.validate();
//			dfDenNgay.validate();
//			cbbLoaiGD.validate();
//			return true;
//		} catch (InvalidValueException ex) {
//			dfTuNgay.setValidationVisible(true);
//			dfDenNgay.setValidationVisible(true);
//			cbbLoaiGD.setValidationVisible(true);
//		}
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
		layoutBtn.setSpacing(true);
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
	                LOGGER.error(e.getMessage());
	            }
	              return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
	}
	
	private void exportDataDoiChieu(List<DoiSoatData> doisoatDataList,String lttqt,String loaiGD) {
		//EXPORT LIST TO EXCEL FILE
        XSSFWorkbook workbookExport = new XSSFWorkbook();
        XSSFSheet sheetExport = workbookExport.createSheet(loaiGD);
        
        DataFormat format = workbookExport.createDataFormat();
        CellStyle styleNumber;
        styleNumber = workbookExport.createCellStyle();
        styleNumber.setDataFormat(format.getFormat("0.0"));
        
        rowNumExport = 0;
        LOGGER.info("Creating excel");

        if(rowNumExport == 0) {
        	Object[] rowHeader = null;
    		rowHeader = new Object[] {"STT","SỐ THẺ","MÃ GD","NGÀY GD","NGÀY FILE INCOMING","ST GD","ST TQT","ST QD VND","LDGD","LTTQT","INTERCHANGE",
    				"PHÍ XỬ LÝ GD VND","MÃ CẤP PHÉP","ĐVCNT","REVERSAL INDICATOR","ISSUER CHARGE"};
    	
        	int colNum = 0;	 
        	XSSFRow row = sheetExport.createRow(rowNumExport++);         	
        	for (Object field : rowHeader) {
        		Cell cell = row.createCell(colNum++, CellType.STRING);
        		cell.setCellValue((String)field);
        	}      
        	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
        }
        
        try {
	        for(DoiSoatData item : doisoatDataList) {
				XSSFRow row = sheetExport.createRow(rowNumExport++);
				
				row.createCell(0).setCellValue(rowNumExport-1);
				row.createCell(1).setCellValue(item.getSoThe());
				row.createCell(2).setCellValue(item.getMaGd());
				row.createCell(3).setCellValue(item.getNgayGd());
				row.createCell(4).setCellValue(item.getNgayFileIncoming());
				row.createCell(5,CellType.NUMERIC).setCellValue(item.getStGd().doubleValue());
				row.createCell(6,CellType.NUMERIC).setCellValue(item.getStTqt().doubleValue());
				row.createCell(7,CellType.NUMERIC).setCellValue(item.getStQdVnd().doubleValue());
				row.createCell(8,CellType.NUMERIC).setCellValue(item.getLtgd());
				row.createCell(9).setCellValue(item.getLttqt());
				row.createCell(10,CellType.NUMERIC).setCellValue(item.getInterchange().doubleValue());
				row.createCell(11,CellType.NUMERIC).setCellValue(item.getPhiXuLyGd().doubleValue());
				row.createCell(12).setCellValue(item.getMaCapPhep());
				row.createCell(13).setCellValue(item.getDvcnt());
				row.createCell(14).setCellValue(item.getReversalInd());
				row.createCell(15).setCellValue(item.getIssuerCharge());
				
	        }
        
	        sheetExport.createFreezePane(0, 1);
        
        	fileNameOutput = cardType + "_" + loaiGD + "_" + ngayAdv + "_" + lttqt + ".xlsx";
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
	        LOGGER.info("Export excel file " + fileNameOutput);
	        messageExportXLSX("Info","Export compeleted.");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
	}
}
