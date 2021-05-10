package com.dvnb.components;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

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
public class DoiChieuThanhQuyetToanForm extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DoiChieuThanhQuyetToanForm.class);
	
	private DataSource localDataSource;
	private SpringConfigurationValueHelper configurationHelper;
	final TimeConverter timeConverter = new TimeConverter();
	public String filename;
	
	List<String> itemsTemp;
	
	public transient Grid gridGDTTHHDuocTQT;
	public transient Grid gridGDTTHHChiTietGDDuocTQT;
	public transient Grid gridGDTTHHTrichNoBoSungKH;
	public transient Grid gridGDTTHHHoanTraTienTrichThua;
	public transient Grid gridGDMoneySendFF;
	public transient Grid gridGDRTMDuocTQT;
	public transient Grid gridGDRTMChiTietGDDuocTQT;
	public transient Grid gridHoanTraGDTTHH;
	public transient Grid gridHoanTraGDRTM;
	public transient Grid gridGDBaoCoBatThuong;
	public transient Grid gridGDBaoNoBatThuong;
	public transient Grid gridGDRTMTrichNoBoSungKH;
	public transient Grid gridGDRTMHoanTraTienTrichThua;
	
	public transient Grid gridGDTTHHDuocTQT_USD;
	public transient Grid gridGDTTHHChiTietGDDuocTQT_USD;
	public transient Grid gridGDTTHHTrichNoBoSungKH_USD;
	public transient Grid gridGDTTHHHoanTraTienTrichThua_USD;
	public transient Grid gridGDMoneySendFF_USD;
	public transient Grid gridGDRTMDuocTQT_USD;
	public transient Grid gridGDRTMChiTietGDDuocTQT_USD;
	public transient Grid gridHoanTraGDTTHH_USD;
	public transient Grid gridHoanTraGDRTM_USD;
	public transient Grid gridGDBaoCoBatThuong_USD;
	public transient Grid gridGDBaoNoBatThuong_USD;
	public transient Grid gridGDRTMTrichNoBoSungKH_USD;
	public transient Grid gridGDRTMHoanTraTienTrichThua_USD;
	
	private transient IndexedContainer containerGDTTHHDuocTQT;
	private transient IndexedContainer containerGDTTHHChiTietGDDuocTQT;
	private transient IndexedContainer containerGDTTHHTrichNoBoSungKH;
	private transient IndexedContainer containerGDTTHHHoanTraTienTrichThua;
	private transient IndexedContainer containerGDMoneySendFF;
	private transient IndexedContainer containerGDRTMDuocTQT;
	private transient IndexedContainer containerGDRTMChiTietGDDuocTQT;
	private transient IndexedContainer containerHoanTraGDTTHH;
	private transient IndexedContainer containerHoanTraGDRTM;
	private transient IndexedContainer containerGDBaoCoBatThuong;
	private transient IndexedContainer containerGDBaoNoBatThuong;
	private transient IndexedContainer containerGDRTMTrichNoBoSungKH;
	private transient IndexedContainer containerGDRTMHoanTraTienTrichThua;
	
	private transient IndexedContainer containerGDTTHHDuocTQT_USD;
	private transient IndexedContainer containerGDTTHHChiTietGDDuocTQT_USD;
	private transient IndexedContainer containerGDTTHHTrichNoBoSungKH_USD;
	private transient IndexedContainer containerGDTTHHHoanTraTienTrichThua_USD;
	private transient IndexedContainer containerGDMoneySendFF_USD;
	private transient IndexedContainer containerGDRTMDuocTQT_USD;
	private transient IndexedContainer containerGDRTMChiTietGDDuocTQT_USD;
	private transient IndexedContainer containerHoanTraGDTTHH_USD;
	private transient IndexedContainer containerHoanTraGDRTM_USD;
	private transient IndexedContainer containerGDBaoCoBatThuong_USD;
	private transient IndexedContainer containerGDBaoNoBatThuong_USD;
	private transient IndexedContainer containerGDRTMTrichNoBoSungKH_USD;
	private transient IndexedContainer containerGDRTMHoanTraTienTrichThua_USD;
	
	private Button btGiaoDichTheTTHH;
	private Button btGiaoDichTheRTM;
	private Button btGiaoDichMoneySendFastFund;
	private Button btHoanTraGiaoDichTTHH;
	private Button btHoanTraGDRTM;
	private Button btGDBaoCoBatThuong;
	private Button btGDBaoNoBatThuong;
	
	private Button btGiaoDichTheTTHH_USD;
	private Button btGiaoDichTheRTM_USD;
	private Button btGiaoDichMoneySendFastFund_USD;
	private Button btHoanTraGiaoDichTTHH_USD;
	private Button btHoanTraGDRTM_USD;
	private Button btGDBaoCoBatThuong_USD;
	private Button btGDBaoNoBatThuong_USD;
	
	private Button btExport;
	private Button btClose;
	
	private Page<DoiSoatData> dataSourceGDTTHHDuocTQT;
	private FooterRow footerGDTTHHDuocTQT;
	private Page<DoiSoatData> dataSourceGDTTHHChiTietGDDuocTQT;
	private Page<DoiSoatData> dataSourceGDTTHHTrichNoBoSungKH;
	private Page<DoiSoatData> dataSourceGDTTHHHoanTraTienTrichThua;
	private Page<DoiSoatData> dataSourceGDRTMDuocTQT;
	private FooterRow footerGDRTMDuocTQT;
	private FooterRow footerGDRTMDuocTQTInterchangeKHCN;
	private FooterRow footerGDRTMDuocTQTInterchangeKHDN;
	private FooterRow footerGDRTMDuocTQTInterchangeTotal;
	private Page<DoiSoatData> dataSourceGDRTMChiTietGDDuocTQT;
	private Page<DoiSoatData> dataSourceGDRTMTrichNoBoSungKH;
	private Page<DoiSoatData> dataSourceGDRTMHoanTraTienTrichThua;
	private Page<DoiSoatData> dataSourceGDMoneySendFF;
	private FooterRow footerGDMoneySendFF;
	private Page<DoiSoatData> dataSourceHoanTraGDTTHH;
	private FooterRow footerHoanTraGDTTHH;
	private Page<DoiSoatData> dataSourceHoanTraGDRTM;
	private FooterRow footerHoanTraGDRTM;
	private Page<DoiSoatData> dataSourceGDBaoCoBatThuong;
	private Page<DoiSoatData> dataSourceGDBaoNoBatThuong;
	
	private Page<DoiSoatData> dataSourceGDTTHHDuocTQT_USD;
	private FooterRow footerGDTTHHDuocTQT_USD;
	private FooterRow footerGDTTHHDuocTQTTyGiaTQT_USD;
	private FooterRow footerGDTTHHDuocTQTThuPhiGD_USD;
	private FooterRow footerGDTTHHDuocTQTTongGDDuocTQT_USD;
	private Page<DoiSoatData> dataSourceGDTTHHChiTietGDDuocTQT_USD;
	private Page<DoiSoatData> dataSourceGDTTHHTrichNoBoSungKH_USD;
	private Page<DoiSoatData> dataSourceGDTTHHHoanTraTienTrichThua_USD;
	private Page<DoiSoatData> dataSourceGDRTMDuocTQT_USD;
	private FooterRow footerGDRTMDuocTQT_USD;
	private FooterRow footerGDRTMDuocTQTTyGiaTQT_USD;
	private FooterRow footerGDRTMDuocTQTInterchangeKHCN_USD;
	private FooterRow footerGDRTMDuocTQTInterchangeKHDN_USD;
	private FooterRow footerGDRTMDuocTQTInterchangeTotal_USD;
	private Page<DoiSoatData> dataSourceGDRTMChiTietGDDuocTQT_USD;
	private Page<DoiSoatData> dataSourceGDRTMTrichNoBoSungKH_USD;
	private Page<DoiSoatData> dataSourceGDRTMHoanTraTienTrichThua_USD;
	private Page<DoiSoatData> dataSourceGDMoneySendFF_USD;
	private FooterRow footerGDMoneySendFF_USD;
	private FooterRow footerGDMoneySendFFTyGiaTQT_USD;
	private FooterRow footerGDMoneySendFFInterchangeKHCN_USD;
	private FooterRow footerGDMoneySendFFInterchangeKHDN_USD;
	private FooterRow footerGDMoneySendFFInterchangeTotal_USD;
	private Page<DoiSoatData> dataSourceHoanTraGDTTHH_USD;
	private FooterRow footerHoanTraGDTTHH_USD;
	private FooterRow footerHoanTraGDTTHHTyGiaTQT_USD;
	private FooterRow footerHoanTraGDTTHHThuPhiGD_USD;
	private FooterRow footerHoanTraGDTTHHTongGDDuocTQT_USD;
	
	private Page<DoiSoatData> dataSourceHoanTraGDRTM_USD;
	private FooterRow footerHoanTraGDRTM_USD;
	private FooterRow footerHoanTraGDRTMTyGiaTQT_USD;
	private FooterRow footerHoanTraGDRTMInterchangeKHCN_USD;
	private FooterRow footerHoanTraGDRTMInterchangeKHDN_USD;
	private FooterRow footerHoanTraGDRTMInterchangeTotal_USD;
	private Page<DoiSoatData> dataSourceGDBaoCoBatThuong_USD;
	private Page<DoiSoatData> dataSourceGDBaoNoBatThuong_USD;
	
	private final transient DoiSoatDataService doiSoatDataService;
	private final transient PhiInterchangeService phiInterchangeService;
	private final transient TyGiaTqtService tyGiaTqtService;
	private transient Page<DoiSoatData> result;
	int stt = 0;
	
	private BigDecimal interchangeGdrtmKhcn;
	private BigDecimal interchangeGdrtmKhdn;
	
	private BigDecimal interchangeGdrtmKhcnUSD;
	private BigDecimal interchangeGdrtmKhdnUSD;
	
	private BigDecimal interchangeGdmsffKhcnUSD;
	private BigDecimal interchangeGdmsffKhdnUSD;
	
	private BigDecimal interchangeHtgdrtmKhcnUSD;
	private BigDecimal interchangeHtgdrtmKhdnUSD;
	
	private BigDecimal tyGiaTQT = BigDecimal.ZERO;
	
	private String ngayAdv;
	private String loaitienTqt;
	private String cardType;
	
	private List<DoiSoatData> doisoatDataList = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> doisoatDataListTemp = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListGDTTHH = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListGDRTM = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListGDMoneySendFF = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListHoanTraGDTTHH = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListHoanTraGDRTM = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListGDBaoCoBatThuong = new ArrayList<DoiSoatData>();
	private List<DoiSoatData> dsDataListGDBaoNoBatThuong = new ArrayList<DoiSoatData>();
	
	// Paging
	private final static int SIZE_OF_PAGE = 1000000;
	private final static int FIRST_OF_PAGE = 0;
	
	public DoiChieuThanhQuyetToanForm(List<DoiSoatData> _doisoatDataList, String _loaitienTqt,String loaiGD, String _ngayAdv,String _cardType,final Callback callback) {
		super();
		doisoatDataList = _doisoatDataList;
		//System.out.println("sStatus:" + sStatus);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		DescriptionService descActService = (DescriptionService) helper.getBean("descriptionService");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		phiInterchangeService = (PhiInterchangeService) helper.getBean("phiInterchangeService");
		tyGiaTqtService = (TyGiaTqtService) helper.getBean("tyGiaTqtService");
		localDataSource = (DataSource) helper.getBean("dataSource");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		this.ngayAdv = _ngayAdv;
		this.loaitienTqt = _loaitienTqt;
		this.cardType = _cardType;
		
		try {
			if(loaitienTqt.equals("USD"))
				tyGiaTQT = tyGiaTqtService.findTyGiaTqtByNgayAdvAndCardType(ngayAdv, cardType).getTyGiaTqt();
		} catch (Exception e) {
			// TODO: handle exception
			Notification.show("Lỗi", "Chưa update tỷ giá " + e.getMessage(), Type.ERROR_MESSAGE);
		}
		
		
//		for(DoiSoatData ds : doisoatDataList) {
//			try {
//				System.out.print("SoThe: " + ds.getSoThe() + ", Pan: " + ds.getPan() + ", MaCapPhep: " + ds.getMaCapPhep() 
//				+ ", MaGd: " + ds.getMaGd() + ", StatusCw: " + ds.getStatusCw() + ", ReversalInd: " + ds.getReversalInd());
//				if((ds.getMaGd().startsWith("00") && ds.getStatusCw().equals(" "))
//				|| (ds.getMaGd().startsWith("00") && !ds.getStatusCw().equals(" ") && ds.getReversalInd().equals("R"))) {
//					dsDataListGDTTHH.add(ds);
//				}
//				
//				if((ds.getMaGd().startsWith("01") && ds.getStatusCw().equals(" "))
//				|| (ds.getMaGd().startsWith("01") && !ds.getStatusCw().equals(" ") && ds.getReversalInd().equals("R"))) {
//					dsDataListGDRTM.add(ds);
//				}
//				
//				if(ds.getMaGd().startsWith("28") && ds.getStatusCw().equals(" ")) {
//					dsDataListGDMoneySendFF.add(ds);
//				}
//				
//				if((ds.getMaGd().startsWith("20") && ds.getStatusCw().equals(" "))
//				|| (ds.getMaGd().startsWith("00") && !ds.getStatusCw().equals(" ") && ds.getReversalInd().equals("R"))) {
//					dsDataListHoanTraGDTTHH.add(ds);
//				}
//				
//				if(ds.getMaGd().startsWith("01") && ds.getStatusCw().equals(" ") && ds.getReversalInd().equals("R")) {
//					dsDataListHoanTraGDRTM.add(ds);
//				}
//				
//				if(ds.getMaGd().startsWith("29")) {
//					dsDataListGDBaoCoBatThuong.add(ds);
//				}
//				
//				if(ds.getMaGd().startsWith("19")) {
//					dsDataListGDBaoNoBatThuong.add(ds);
//				}
//			}
//			catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//			
//		}
		
		
		final String userid = SecurityUtils.getUserId();
		setSpacing(true);
		setMargin(true);
		
		final Panel panelTQTTheoVND= new Panel("THANH QUYẾT TOÁN THEO LOẠI TIỀN VNĐ");
		panelTQTTheoVND.setStyleName(Reindeer.PANEL_LIGHT);
		panelTQTTheoVND.setSizeFull();
		
		//----------------------------THANH QUYẾT TOÁN THEO LOẠI TIỀN VNĐ------------------------------------------
		//--- 1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA
		//--- 1.1. GD được thanh quyết toán
		gridGDTTHHDuocTQT = new Grid();
		gridGDTTHHDuocTQT.setCaption("GD được thanh quyết toán");
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
		containerGDTTHHDuocTQT.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHDuocTQT.addContainerProperty("statusCW", String.class, "");
		
		
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
		gridGDTTHHDuocTQT.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHDuocTQT.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHDuocTQT.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHDuocTQT.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHDuocTQT.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHDuocTQT.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHDuocTQT.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHDuocTQT.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHDuocTQT.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHDuocTQT.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		footerGDTTHHDuocTQT = gridGDTTHHDuocTQT.prependFooterRow();
		
		//-----------------------------------------------------//
		//--- 1.2. Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
		gridGDTTHHChiTietGDDuocTQT = new Grid();
		gridGDTTHHChiTietGDDuocTQT.setCaption("Chi tiết GD được thanh quyết toán có phát sinh chênh lệch");
		gridGDTTHHChiTietGDDuocTQT.setSizeFull();
		gridGDTTHHChiTietGDDuocTQT.setHeightByRows(10);
		gridGDTTHHChiTietGDDuocTQT.setHeightMode(HeightMode.ROW);
		gridGDTTHHChiTietGDDuocTQT.recalculateColumnWidths();
		
		containerGDTTHHChiTietGDDuocTQT = new IndexedContainer();
		
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("soThe", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("maGd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHChiTietGDDuocTQT.addContainerProperty("statusCW", String.class, "");
		
		
		gridGDTTHHChiTietGDDuocTQT.setContainerDataSource(containerGDTTHHChiTietGDDuocTQT);
		gridGDTTHHChiTietGDDuocTQT.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHChiTietGDDuocTQT.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHChiTietGDDuocTQT.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHChiTietGDDuocTQT.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHChiTietGDDuocTQT.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHChiTietGDDuocTQT.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHChiTietGDDuocTQT.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHChiTietGDDuocTQT.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHChiTietGDDuocTQT.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHChiTietGDDuocTQT.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHChiTietGDDuocTQT.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		//-----------------------------------------------------//
		//--- 1.3. Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDTTHHTrichNoBoSungKH = new Grid();
		gridGDTTHHTrichNoBoSungKH.setCaption("Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDTTHHTrichNoBoSungKH.setSizeFull();
		gridGDTTHHTrichNoBoSungKH.setHeightByRows(10);
		gridGDTTHHTrichNoBoSungKH.setHeightMode(HeightMode.ROW);
		gridGDTTHHTrichNoBoSungKH.recalculateColumnWidths();
		
		containerGDTTHHTrichNoBoSungKH = new IndexedContainer();
		
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("soThe", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("maGd", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("statusCW", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("soTienGdTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("phiIsaTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("vatPhiIsaTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("tongPhiVatTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("tongTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("tenChuTk", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("casa", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("dvpht", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("trace", String.class, "");
		containerGDTTHHTrichNoBoSungKH.addContainerProperty("adv", String.class, "");
		
		gridGDTTHHTrichNoBoSungKH.setContainerDataSource(containerGDTTHHTrichNoBoSungKH);
		gridGDTTHHTrichNoBoSungKH.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHTrichNoBoSungKH.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHTrichNoBoSungKH.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHTrichNoBoSungKH.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHTrichNoBoSungKH.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHTrichNoBoSungKH.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHTrichNoBoSungKH.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHTrichNoBoSungKH.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHTrichNoBoSungKH.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHTrichNoBoSungKH.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHTrichNoBoSungKH.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHTrichNoBoSungKH.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHTrichNoBoSungKH.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHTrichNoBoSungKH.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHTrichNoBoSungKH.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHTrichNoBoSungKH.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHTrichNoBoSungKH.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHTrichNoBoSungKH.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHTrichNoBoSungKH.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHTrichNoBoSungKH.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHTrichNoBoSungKH.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDTTHHTrichNoBoSungKH.getColumn("soTienGdTruyThu").setHeaderCaption("Số tiền GD truy thu");
		gridGDTTHHTrichNoBoSungKH.getColumn("phiIsaTruyThu").setHeaderCaption("Phí ISA truy thu");
		gridGDTTHHTrichNoBoSungKH.getColumn("vatPhiIsaTruyThu").setHeaderCaption("VAT phí ISA truy thu");
		gridGDTTHHTrichNoBoSungKH.getColumn("tongPhiVatTruyThu").setHeaderCaption("Tổng phí + VAT truy thu");
		gridGDTTHHTrichNoBoSungKH.getColumn("tongTruyThu").setHeaderCaption("Tổng truy thu");
		gridGDTTHHTrichNoBoSungKH.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDTTHHTrichNoBoSungKH.getColumn("casa").setHeaderCaption("CASA");
		gridGDTTHHTrichNoBoSungKH.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDTTHHTrichNoBoSungKH.getColumn("trace").setHeaderCaption("Trace");
		gridGDTTHHTrichNoBoSungKH.getColumn("adv").setHeaderCaption("ADV");
		
		//-----------------------------------------------------//
		//--- 1.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch TTHH Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDTTHHHoanTraTienTrichThua = new Grid();
		gridGDTTHHHoanTraTienTrichThua.setCaption("Hoàn trả KH tiền trích thừa các giao dịch các giao dịch TTHH Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDTTHHHoanTraTienTrichThua.setSizeFull();
		gridGDTTHHHoanTraTienTrichThua.setHeightByRows(10);
		gridGDTTHHHoanTraTienTrichThua.setHeightMode(HeightMode.ROW);
		gridGDTTHHHoanTraTienTrichThua.recalculateColumnWidths();
		
		containerGDTTHHHoanTraTienTrichThua = new IndexedContainer();
		
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("soThe", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("maGd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("statusCW", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("phiIsaHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("vatPhiIsaHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("tongPhiVatHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("tongHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("tenChuTk", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("casa", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("dvpht", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("trace", String.class, "");
		containerGDTTHHHoanTraTienTrichThua.addContainerProperty("adv", String.class, "");
		
		gridGDTTHHHoanTraTienTrichThua.setContainerDataSource(containerGDTTHHHoanTraTienTrichThua);
		gridGDTTHHHoanTraTienTrichThua.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHHoanTraTienTrichThua.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHHoanTraTienTrichThua.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHHoanTraTienTrichThua.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHHoanTraTienTrichThua.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHHoanTraTienTrichThua.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHHoanTraTienTrichThua.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHHoanTraTienTrichThua.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHHoanTraTienTrichThua.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHHoanTraTienTrichThua.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHHoanTraTienTrichThua.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDTTHHHoanTraTienTrichThua.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridGDTTHHHoanTraTienTrichThua.getColumn("phiIsaHoanTra").setHeaderCaption("Phí ISA hoàn trả");
		gridGDTTHHHoanTraTienTrichThua.getColumn("vatPhiIsaHoanTra").setHeaderCaption("VAT phí ISA hoàn trả");
		gridGDTTHHHoanTraTienTrichThua.getColumn("tongPhiVatHoanTra").setHeaderCaption("Tổng phí + VAT hoàn trả");
		gridGDTTHHHoanTraTienTrichThua.getColumn("tongHoanTra").setHeaderCaption("Tổng hoàn trả");
		gridGDTTHHHoanTraTienTrichThua.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDTTHHHoanTraTienTrichThua.getColumn("casa").setHeaderCaption("CASA");
		gridGDTTHHHoanTraTienTrichThua.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDTTHHHoanTraTienTrichThua.getColumn("trace").setHeaderCaption("Trace");
		gridGDTTHHHoanTraTienTrichThua.getColumn("adv").setHeaderCaption("ADV");
		
		// 2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT
		//--- 2.1. GD được thanh quyết toán
		gridGDRTMDuocTQT = new Grid();
		gridGDRTMDuocTQT.setCaption("GD được thanh quyết toán");
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
		containerGDRTMDuocTQT.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMDuocTQT.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMDuocTQT.addContainerProperty("statusCW", String.class, "");
		
		
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
		gridGDRTMDuocTQT.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMDuocTQT.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMDuocTQT.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMDuocTQT.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMDuocTQT.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMDuocTQT.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMDuocTQT.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMDuocTQT.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		footerGDRTMDuocTQT = gridGDRTMDuocTQT.appendFooterRow();
		footerGDRTMDuocTQTInterchangeKHCN = gridGDRTMDuocTQT.appendFooterRow();
		footerGDRTMDuocTQTInterchangeKHDN = gridGDRTMDuocTQT.appendFooterRow();
		footerGDRTMDuocTQTInterchangeTotal = gridGDRTMDuocTQT.appendFooterRow();
		
		//-----------------------------------------------------//
		//--- 2.2. Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
		gridGDRTMChiTietGDDuocTQT = new Grid();
		gridGDRTMChiTietGDDuocTQT.setCaption("Chi tiết GD được thanh quyết toán có phát sinh chênh lệch");
		gridGDRTMChiTietGDDuocTQT.setSizeFull();
		gridGDRTMChiTietGDDuocTQT.setHeightByRows(10);
		gridGDRTMChiTietGDDuocTQT.setHeightMode(HeightMode.ROW);
		gridGDRTMChiTietGDDuocTQT.recalculateColumnWidths();
		
		containerGDRTMChiTietGDDuocTQT = new IndexedContainer();
		
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stt", Integer.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("soThe", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("maGd", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("ltgd", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("lttqt", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMChiTietGDDuocTQT.addContainerProperty("statusCW", String.class, "");
		
		gridGDRTMChiTietGDDuocTQT.setContainerDataSource(containerGDRTMChiTietGDDuocTQT);
		gridGDRTMChiTietGDDuocTQT.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMChiTietGDDuocTQT.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMChiTietGDDuocTQT.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMChiTietGDDuocTQT.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMChiTietGDDuocTQT.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMChiTietGDDuocTQT.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMChiTietGDDuocTQT.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMChiTietGDDuocTQT.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMChiTietGDDuocTQT.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMChiTietGDDuocTQT.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMChiTietGDDuocTQT.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMChiTietGDDuocTQT.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMChiTietGDDuocTQT.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMChiTietGDDuocTQT.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMChiTietGDDuocTQT.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMChiTietGDDuocTQT.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMChiTietGDDuocTQT.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMChiTietGDDuocTQT.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMChiTietGDDuocTQT.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMChiTietGDDuocTQT.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMChiTietGDDuocTQT.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		//-----------------------------------------------------//
		//--- 2.3. Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDRTMTrichNoBoSungKH = new Grid();
		gridGDRTMTrichNoBoSungKH.setCaption("Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDRTMTrichNoBoSungKH.setSizeFull();
		gridGDRTMTrichNoBoSungKH.setHeightByRows(10);
		gridGDRTMTrichNoBoSungKH.setHeightMode(HeightMode.ROW);
		gridGDRTMTrichNoBoSungKH.recalculateColumnWidths();
		
		containerGDRTMTrichNoBoSungKH = new IndexedContainer();
		
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stt", Integer.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("soThe", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("maGd", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("ltgd", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("lttqt", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("statusCW", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("soTienGdTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("phiIsaTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("vatPhiIsaTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("tongPhiVatTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("tongTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("tenChuTk", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("casa", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("dvpht", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("trace", String.class, "");
		containerGDRTMTrichNoBoSungKH.addContainerProperty("adv", String.class, "");
		
		gridGDRTMTrichNoBoSungKH.setContainerDataSource(containerGDRTMTrichNoBoSungKH);
		gridGDRTMTrichNoBoSungKH.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMTrichNoBoSungKH.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMTrichNoBoSungKH.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMTrichNoBoSungKH.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMTrichNoBoSungKH.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMTrichNoBoSungKH.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMTrichNoBoSungKH.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMTrichNoBoSungKH.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMTrichNoBoSungKH.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMTrichNoBoSungKH.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMTrichNoBoSungKH.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMTrichNoBoSungKH.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMTrichNoBoSungKH.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMTrichNoBoSungKH.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMTrichNoBoSungKH.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMTrichNoBoSungKH.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMTrichNoBoSungKH.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMTrichNoBoSungKH.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMTrichNoBoSungKH.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMTrichNoBoSungKH.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMTrichNoBoSungKH.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDRTMTrichNoBoSungKH.getColumn("soTienGdTruyThu").setHeaderCaption("Số tiền GD truy thu");
		gridGDRTMTrichNoBoSungKH.getColumn("phiIsaTruyThu").setHeaderCaption("Phí ISA truy thu");
		gridGDRTMTrichNoBoSungKH.getColumn("vatPhiIsaTruyThu").setHeaderCaption("VAT phí ISA truy thu");
		gridGDRTMTrichNoBoSungKH.getColumn("tongPhiVatTruyThu").setHeaderCaption("Tổng phí + VAT truy thu");
		gridGDRTMTrichNoBoSungKH.getColumn("tongTruyThu").setHeaderCaption("Tổng truy thu");
		gridGDRTMTrichNoBoSungKH.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDRTMTrichNoBoSungKH.getColumn("casa").setHeaderCaption("CASA");
		gridGDRTMTrichNoBoSungKH.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDRTMTrichNoBoSungKH.getColumn("trace").setHeaderCaption("Trace");
		gridGDRTMTrichNoBoSungKH.getColumn("adv").setHeaderCaption("ADV");
		
		//-----------------------------------------------------//
		//--- 2.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDRTMHoanTraTienTrichThua = new Grid();
		gridGDRTMHoanTraTienTrichThua.setCaption("Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDRTMHoanTraTienTrichThua.setSizeFull();
		gridGDRTMHoanTraTienTrichThua.setHeightByRows(10);
		gridGDRTMHoanTraTienTrichThua.setHeightMode(HeightMode.ROW);
		gridGDRTMHoanTraTienTrichThua.recalculateColumnWidths();
		
		containerGDRTMHoanTraTienTrichThua = new IndexedContainer();
		
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stt", Integer.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("soThe", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("maGd", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("ltgd", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("lttqt", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("statusCW", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("phiIsaHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("vatPhiIsaHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("tongPhiVatHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("tongHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("tenChuTk", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("casa", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("dvpht", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("trace", String.class, "");
		containerGDRTMHoanTraTienTrichThua.addContainerProperty("adv", String.class, "");
		
		gridGDRTMHoanTraTienTrichThua.setContainerDataSource(containerGDRTMHoanTraTienTrichThua);
		gridGDRTMHoanTraTienTrichThua.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMHoanTraTienTrichThua.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMHoanTraTienTrichThua.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMHoanTraTienTrichThua.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMHoanTraTienTrichThua.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMHoanTraTienTrichThua.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMHoanTraTienTrichThua.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMHoanTraTienTrichThua.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMHoanTraTienTrichThua.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMHoanTraTienTrichThua.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMHoanTraTienTrichThua.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMHoanTraTienTrichThua.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMHoanTraTienTrichThua.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMHoanTraTienTrichThua.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMHoanTraTienTrichThua.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMHoanTraTienTrichThua.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMHoanTraTienTrichThua.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMHoanTraTienTrichThua.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMHoanTraTienTrichThua.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMHoanTraTienTrichThua.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMHoanTraTienTrichThua.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDRTMHoanTraTienTrichThua.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridGDRTMHoanTraTienTrichThua.getColumn("phiIsaHoanTra").setHeaderCaption("Phí ISA hoàn trả");
		gridGDRTMHoanTraTienTrichThua.getColumn("vatPhiIsaHoanTra").setHeaderCaption("VAT phí ISA hoàn trả");
		gridGDRTMHoanTraTienTrichThua.getColumn("tongPhiVatHoanTra").setHeaderCaption("Tổng phí + VAT hoàn trả");
		gridGDRTMHoanTraTienTrichThua.getColumn("tongHoanTra").setHeaderCaption("Tổng hoàn trả");
		gridGDRTMHoanTraTienTrichThua.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDRTMHoanTraTienTrichThua.getColumn("casa").setHeaderCaption("CASA");
		gridGDRTMHoanTraTienTrichThua.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDRTMHoanTraTienTrichThua.getColumn("trace").setHeaderCaption("Trace");
		gridGDRTMHoanTraTienTrichThua.getColumn("adv").setHeaderCaption("ADV");
		
		// 3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
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
		containerGDMoneySendFF.addContainerProperty("maCapPhep", String.class, "");
		containerGDMoneySendFF.addContainerProperty("dvcnt", String.class, "");
		containerGDMoneySendFF.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDMoneySendFF.addContainerProperty("reversalInd", String.class, "");
		containerGDMoneySendFF.addContainerProperty("issuerCharge", String.class, "");
		containerGDMoneySendFF.addContainerProperty("statusCW", String.class, "");
		
		
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
		gridGDMoneySendFF.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDMoneySendFF.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDMoneySendFF.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDMoneySendFF.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDMoneySendFF.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDMoneySendFF.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDMoneySendFF.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDMoneySendFF.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDMoneySendFF.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDMoneySendFF.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		footerGDMoneySendFF = gridGDMoneySendFF.prependFooterRow();
		
		// 4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
		gridHoanTraGDTTHH = new Grid();
		gridHoanTraGDTTHH.setSizeFull();
		gridHoanTraGDTTHH.setHeightByRows(10);
		gridHoanTraGDTTHH.setHeightMode(HeightMode.ROW);
		gridHoanTraGDTTHH.recalculateColumnWidths();
		
		containerHoanTraGDTTHH = new IndexedContainer();
		
		containerHoanTraGDTTHH.addContainerProperty("stt", Integer.class, "");
		containerHoanTraGDTTHH.addContainerProperty("soThe", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("maGd", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("ngayGd", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("ngayFileIncoming", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stGd", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stTqt", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("ltgd", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("lttqt", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("interchange", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("maCapPhep", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("dvcnt", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("reversalInd", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("issuerCharge", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("statusCW", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("phiIsaGd", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("ghiChu", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("phiHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("thueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("tongPhiThueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("tongSoTienHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH.addContainerProperty("tenChuTk", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("dvpht", String.class, "");
		containerHoanTraGDTTHH.addContainerProperty("casa", String.class, "");
		
		gridHoanTraGDTTHH.setContainerDataSource(containerHoanTraGDTTHH);
		gridHoanTraGDTTHH.getColumn("stt").setHeaderCaption("STT");
		gridHoanTraGDTTHH.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridHoanTraGDTTHH.getColumn("maGd").setHeaderCaption("Mã GD");
		gridHoanTraGDTTHH.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridHoanTraGDTTHH.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridHoanTraGDTTHH.getColumn("stGd").setHeaderCaption("ST GD");
		gridHoanTraGDTTHH.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridHoanTraGDTTHH.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridHoanTraGDTTHH.getColumn("ltgd").setHeaderCaption("LDGD");
		gridHoanTraGDTTHH.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridHoanTraGDTTHH.getColumn("interchange").setHeaderCaption("Interchange");
		gridHoanTraGDTTHH.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridHoanTraGDTTHH.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridHoanTraGDTTHH.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridHoanTraGDTTHH.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridHoanTraGDTTHH.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridHoanTraGDTTHH.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridHoanTraGDTTHH.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridHoanTraGDTTHH.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridHoanTraGDTTHH.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridHoanTraGDTTHH.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridHoanTraGDTTHH.getColumn("phiIsaGd").setHeaderCaption("Phí ISA thời điểm GD");
		gridHoanTraGDTTHH.getColumn("ghiChu").setHeaderCaption("Ghi chú");
		gridHoanTraGDTTHH.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridHoanTraGDTTHH.getColumn("phiHoanTraKH").setHeaderCaption("Phí hoàn trả KH");
		gridHoanTraGDTTHH.getColumn("thueHoanTraKH").setHeaderCaption("Thuế hoàn trả KH");
		gridHoanTraGDTTHH.getColumn("tongPhiThueHoanTraKH").setHeaderCaption("Tổng phí, thuế hoàn trả KH");
		gridHoanTraGDTTHH.getColumn("tongSoTienHoanTraKH").setHeaderCaption("Tổng số tiền hoàn trả KH");
		gridHoanTraGDTTHH.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridHoanTraGDTTHH.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridHoanTraGDTTHH.getColumn("casa").setHeaderCaption("CASA");
		
		footerHoanTraGDTTHH = gridHoanTraGDTTHH.prependFooterRow();
		
		//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
		gridHoanTraGDRTM = new Grid();
		gridHoanTraGDRTM.setSizeFull();
		gridHoanTraGDRTM.setHeightByRows(10);
		gridHoanTraGDRTM.setHeightMode(HeightMode.ROW);
		gridHoanTraGDRTM.recalculateColumnWidths();
		
		containerHoanTraGDRTM = new IndexedContainer();
		
		containerHoanTraGDRTM.addContainerProperty("stt", Integer.class, "");
		containerHoanTraGDRTM.addContainerProperty("soThe", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("maGd", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("ngayGd", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("ngayFileIncoming", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("stGd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("stTqt", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("ltgd", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("lttqt", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("interchange", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("maCapPhep", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("dvcnt", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("reversalInd", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("issuerCharge", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("statusCW", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("phiIsaGd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("phiRtmGd", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("ghiChu", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("phiHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("thueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("tongPhiThueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("tongSoTienHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM.addContainerProperty("tenChuTk", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("dvpht", String.class, "");
		containerHoanTraGDRTM.addContainerProperty("casa", String.class, "");
		
		gridHoanTraGDRTM.setContainerDataSource(containerHoanTraGDRTM);
		gridHoanTraGDRTM.getColumn("stt").setHeaderCaption("STT");
		gridHoanTraGDRTM.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridHoanTraGDRTM.getColumn("maGd").setHeaderCaption("Mã GD");
		gridHoanTraGDRTM.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridHoanTraGDRTM.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridHoanTraGDRTM.getColumn("stGd").setHeaderCaption("ST GD");
		gridHoanTraGDRTM.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridHoanTraGDRTM.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridHoanTraGDRTM.getColumn("ltgd").setHeaderCaption("LDGD");
		gridHoanTraGDRTM.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridHoanTraGDRTM.getColumn("interchange").setHeaderCaption("Interchange");
		gridHoanTraGDRTM.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridHoanTraGDRTM.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridHoanTraGDRTM.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridHoanTraGDRTM.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridHoanTraGDRTM.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridHoanTraGDRTM.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridHoanTraGDRTM.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridHoanTraGDRTM.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridHoanTraGDRTM.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridHoanTraGDRTM.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridHoanTraGDRTM.getColumn("phiIsaGd").setHeaderCaption("Phí ISA thời điểm GD");
		gridHoanTraGDRTM.getColumn("phiRtmGd").setHeaderCaption("Phí RTM thời điểm GD");
		gridHoanTraGDRTM.getColumn("ghiChu").setHeaderCaption("Ghi chú");
		gridHoanTraGDRTM.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridHoanTraGDRTM.getColumn("phiHoanTraKH").setHeaderCaption("Phí hoàn trả KH");
		gridHoanTraGDRTM.getColumn("thueHoanTraKH").setHeaderCaption("Thuế hoàn trả KH");
		gridHoanTraGDRTM.getColumn("tongPhiThueHoanTraKH").setHeaderCaption("Tổng phí, thuế hoàn trả KH");
		gridHoanTraGDRTM.getColumn("tongSoTienHoanTraKH").setHeaderCaption("Tổng số tiền hoàn trả KH");
		gridHoanTraGDRTM.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridHoanTraGDRTM.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridHoanTraGDRTM.getColumn("casa").setHeaderCaption("CASA");
		
		footerHoanTraGDRTM = gridHoanTraGDRTM.prependFooterRow();
		
		//6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
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
		gridGDBaoCoBatThuong.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoCoBatThuong.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		//7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
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
		gridGDBaoNoBatThuong.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoNoBatThuong.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		
		//----------------------------THANH QUYẾT TOÁN THEO LOẠI TIỀN USD------------------------------------------
		//--- 1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA
		//--- 1.1. GD được thanh quyết toán
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
		containerGDTTHHDuocTQT_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHDuocTQT_USD.addContainerProperty("statusCW", String.class, "");
		
		
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
		gridGDTTHHDuocTQT_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHDuocTQT_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHDuocTQT_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHDuocTQT_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHDuocTQT_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHDuocTQT_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHDuocTQT_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHDuocTQT_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHDuocTQT_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHDuocTQT_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		footerGDTTHHDuocTQT_USD = gridGDTTHHDuocTQT_USD.appendFooterRow();
		footerGDTTHHDuocTQTTyGiaTQT_USD = gridGDTTHHDuocTQT_USD.appendFooterRow();
		footerGDTTHHDuocTQTThuPhiGD_USD = gridGDTTHHDuocTQT_USD.appendFooterRow();
		footerGDTTHHDuocTQTTongGDDuocTQT_USD = gridGDTTHHDuocTQT_USD.appendFooterRow();
		//-----------------------------------------------------//
		//--- 1.2. Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
		gridGDTTHHChiTietGDDuocTQT_USD = new Grid();
		gridGDTTHHChiTietGDDuocTQT_USD.setCaption("Chi tiết GD được thanh quyết toán có phát sinh chênh lệch");
		gridGDTTHHChiTietGDDuocTQT_USD.setSizeFull();
		gridGDTTHHChiTietGDDuocTQT_USD.setHeightByRows(10);
		gridGDTTHHChiTietGDDuocTQT_USD.setHeightMode(HeightMode.ROW);
		gridGDTTHHChiTietGDDuocTQT_USD.recalculateColumnWidths();
		
		containerGDTTHHChiTietGDDuocTQT_USD = new IndexedContainer();
		
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("soThe", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("maGd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHChiTietGDDuocTQT_USD.addContainerProperty("statusCW", String.class, "");
		
		
		gridGDTTHHChiTietGDDuocTQT_USD.setContainerDataSource(containerGDTTHHChiTietGDDuocTQT_USD);
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHChiTietGDDuocTQT_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		//-----------------------------------------------------//
		//--- 1.3. Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDTTHHTrichNoBoSungKH_USD = new Grid();
		gridGDTTHHTrichNoBoSungKH_USD.setCaption("Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDTTHHTrichNoBoSungKH_USD.setSizeFull();
		gridGDTTHHTrichNoBoSungKH_USD.setHeightByRows(10);
		gridGDTTHHTrichNoBoSungKH_USD.setHeightMode(HeightMode.ROW);
		gridGDTTHHTrichNoBoSungKH_USD.recalculateColumnWidths();
		
		containerGDTTHHTrichNoBoSungKH_USD = new IndexedContainer();
		
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("soThe", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("maGd", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("statusCW", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("soTienGdTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("phiIsaTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("vatPhiIsaTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("tongPhiVatTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("tongTruyThu", BigDecimal.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("tenChuTk", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("casa", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("dvpht", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("trace", String.class, "");
		containerGDTTHHTrichNoBoSungKH_USD.addContainerProperty("adv", String.class, "");
		
		gridGDTTHHTrichNoBoSungKH_USD.setContainerDataSource(containerGDTTHHTrichNoBoSungKH_USD);
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("soTienGdTruyThu").setHeaderCaption("Số tiền GD truy thu");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("phiIsaTruyThu").setHeaderCaption("Phí ISA truy thu");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("vatPhiIsaTruyThu").setHeaderCaption("VAT phí ISA truy thu");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("tongPhiVatTruyThu").setHeaderCaption("Tổng phí + VAT truy thu");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("tongTruyThu").setHeaderCaption("Tổng truy thu");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("casa").setHeaderCaption("CASA");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("trace").setHeaderCaption("Trace");
		gridGDTTHHTrichNoBoSungKH_USD.getColumn("adv").setHeaderCaption("ADV");
		
		//-----------------------------------------------------//
		//--- 1.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch TTHH Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDTTHHHoanTraTienTrichThua_USD = new Grid();
		gridGDTTHHHoanTraTienTrichThua_USD.setCaption("Hoàn trả KH tiền trích thừa các giao dịch các giao dịch TTHH Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDTTHHHoanTraTienTrichThua_USD.setSizeFull();
		gridGDTTHHHoanTraTienTrichThua_USD.setHeightByRows(10);
		gridGDTTHHHoanTraTienTrichThua_USD.setHeightMode(HeightMode.ROW);
		gridGDTTHHHoanTraTienTrichThua_USD.recalculateColumnWidths();
		
		containerGDTTHHHoanTraTienTrichThua_USD = new IndexedContainer();
		
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stt", Integer.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("soThe", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("maGd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("ltgd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("lttqt", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("statusCW", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("phiIsaHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("vatPhiIsaHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("tongPhiVatHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("tongHoanTra", BigDecimal.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("tenChuTk", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("casa", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("dvpht", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("trace", String.class, "");
		containerGDTTHHHoanTraTienTrichThua_USD.addContainerProperty("adv", String.class, "");
		
		gridGDTTHHHoanTraTienTrichThua_USD.setContainerDataSource(containerGDTTHHHoanTraTienTrichThua_USD);
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("phiIsaHoanTra").setHeaderCaption("Phí ISA hoàn trả");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("vatPhiIsaHoanTra").setHeaderCaption("VAT phí ISA hoàn trả");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("tongPhiVatHoanTra").setHeaderCaption("Tổng phí + VAT hoàn trả");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("tongHoanTra").setHeaderCaption("Tổng hoàn trả");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("casa").setHeaderCaption("CASA");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("trace").setHeaderCaption("Trace");
		gridGDTTHHHoanTraTienTrichThua_USD.getColumn("adv").setHeaderCaption("ADV");
		
		// 2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT
		//--- 2.1. GD được thanh quyết toán
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
		containerGDRTMDuocTQT_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMDuocTQT_USD.addContainerProperty("statusCW", String.class, "");
		
		
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
		gridGDRTMDuocTQT_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMDuocTQT_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMDuocTQT_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMDuocTQT_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMDuocTQT_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMDuocTQT_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMDuocTQT_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMDuocTQT_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMDuocTQT_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMDuocTQT_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		footerGDRTMDuocTQT_USD = gridGDRTMDuocTQT_USD.appendFooterRow();
		footerGDRTMDuocTQTTyGiaTQT_USD = gridGDRTMDuocTQT_USD.appendFooterRow();
		footerGDRTMDuocTQTInterchangeKHCN_USD = gridGDRTMDuocTQT_USD.appendFooterRow();
		footerGDRTMDuocTQTInterchangeKHDN_USD = gridGDRTMDuocTQT_USD.appendFooterRow();
		footerGDRTMDuocTQTInterchangeTotal_USD = gridGDRTMDuocTQT_USD.appendFooterRow();
		//-----------------------------------------------------//
		//--- 2.2. Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
		gridGDRTMChiTietGDDuocTQT_USD = new Grid();
		gridGDRTMChiTietGDDuocTQT_USD.setCaption("Chi tiết GD được thanh quyết toán có phát sinh chênh lệch");
		gridGDRTMChiTietGDDuocTQT_USD.setSizeFull();
		gridGDRTMChiTietGDDuocTQT_USD.setHeightByRows(10);
		gridGDRTMChiTietGDDuocTQT_USD.setHeightMode(HeightMode.ROW);
		gridGDRTMChiTietGDDuocTQT_USD.recalculateColumnWidths();
		
		containerGDRTMChiTietGDDuocTQT_USD = new IndexedContainer();
		
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stt", Integer.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("soThe", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("maGd", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("ltgd", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("lttqt", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMChiTietGDDuocTQT_USD.addContainerProperty("statusCW", String.class, "");
		
		gridGDRTMChiTietGDDuocTQT_USD.setContainerDataSource(containerGDRTMChiTietGDDuocTQT_USD);
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMChiTietGDDuocTQT_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		//-----------------------------------------------------//
		//--- 2.3. Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDRTMTrichNoBoSungKH_USD = new Grid();
		gridGDRTMTrichNoBoSungKH_USD.setCaption("Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDRTMTrichNoBoSungKH_USD.setSizeFull();
		gridGDRTMTrichNoBoSungKH_USD.setHeightByRows(10);
		gridGDRTMTrichNoBoSungKH_USD.setHeightMode(HeightMode.ROW);
		gridGDRTMTrichNoBoSungKH_USD.recalculateColumnWidths();
		
		containerGDRTMTrichNoBoSungKH_USD = new IndexedContainer();
		
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stt", Integer.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("soThe", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("maGd", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("ltgd", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("lttqt", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("statusCW", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("soTienGdTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("phiIsaTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("vatPhiIsaTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("tongPhiVatTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("tongTruyThu", BigDecimal.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("tenChuTk", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("casa", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("dvpht", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("trace", String.class, "");
		containerGDRTMTrichNoBoSungKH_USD.addContainerProperty("adv", String.class, "");
		
		gridGDRTMTrichNoBoSungKH_USD.setContainerDataSource(containerGDRTMTrichNoBoSungKH_USD);
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("soTienGdTruyThu").setHeaderCaption("Số tiền GD truy thu");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("phiIsaTruyThu").setHeaderCaption("Phí ISA truy thu");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("vatPhiIsaTruyThu").setHeaderCaption("VAT phí ISA truy thu");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("tongPhiVatTruyThu").setHeaderCaption("Tổng phí + VAT truy thu");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("tongTruyThu").setHeaderCaption("Tổng truy thu");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("casa").setHeaderCaption("CASA");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("trace").setHeaderCaption("Trace");
		gridGDRTMTrichNoBoSungKH_USD.getColumn("adv").setHeaderCaption("ADV");
		
		//-----------------------------------------------------//
		//--- 2.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
		gridGDRTMHoanTraTienTrichThua_USD = new Grid();
		gridGDRTMHoanTraTienTrichThua_USD.setCaption("Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>");
		gridGDRTMHoanTraTienTrichThua_USD.setSizeFull();
		gridGDRTMHoanTraTienTrichThua_USD.setHeightByRows(10);
		gridGDRTMHoanTraTienTrichThua_USD.setHeightMode(HeightMode.ROW);
		gridGDRTMHoanTraTienTrichThua_USD.recalculateColumnWidths();
		
		containerGDRTMHoanTraTienTrichThua_USD = new IndexedContainer();
		
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stt", Integer.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("soThe", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("maGd", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("ngayGd", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("ltgd", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("lttqt", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("statusCW", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("phiIsaHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("vatPhiIsaHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("tongPhiVatHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("tongHoanTra", BigDecimal.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("tenChuTk", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("casa", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("dvpht", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("trace", String.class, "");
		containerGDRTMHoanTraTienTrichThua_USD.addContainerProperty("adv", String.class, "");
		
		gridGDRTMHoanTraTienTrichThua_USD.setContainerDataSource(containerGDRTMHoanTraTienTrichThua_USD);
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stt").setHeaderCaption("STT");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("phiIsaHoanTra").setHeaderCaption("Phí ISA hoàn trả");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("vatPhiIsaHoanTra").setHeaderCaption("VAT phí ISA hoàn trả");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("tongPhiVatHoanTra").setHeaderCaption("Tổng phí + VAT hoàn trả");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("tongHoanTra").setHeaderCaption("Tổng hoàn trả");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("casa").setHeaderCaption("CASA");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("trace").setHeaderCaption("Trace");
		gridGDRTMHoanTraTienTrichThua_USD.getColumn("adv").setHeaderCaption("ADV");
		
		// 3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
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
		containerGDMoneySendFF_USD.addContainerProperty("maCapPhep", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("dvcnt", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("reversalInd", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("issuerCharge", String.class, "");
		containerGDMoneySendFF_USD.addContainerProperty("statusCW", String.class, "");
		
		
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
		gridGDMoneySendFF_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDMoneySendFF_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridGDMoneySendFF_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridGDMoneySendFF_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridGDMoneySendFF_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridGDMoneySendFF_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridGDMoneySendFF_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridGDMoneySendFF_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridGDMoneySendFF_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridGDMoneySendFF_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		
		footerGDMoneySendFF_USD = gridGDMoneySendFF_USD.appendFooterRow();
		footerGDMoneySendFFTyGiaTQT_USD = gridGDMoneySendFF_USD.appendFooterRow();
		footerGDMoneySendFFInterchangeKHCN_USD = gridGDMoneySendFF_USD.appendFooterRow();
		footerGDMoneySendFFInterchangeKHDN_USD = gridGDMoneySendFF_USD.appendFooterRow();
		footerGDMoneySendFFInterchangeTotal_USD = gridGDMoneySendFF_USD.appendFooterRow();
		
		// 4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
		gridHoanTraGDTTHH_USD = new Grid();
		gridHoanTraGDTTHH_USD.setSizeFull();
		gridHoanTraGDTTHH_USD.setHeightByRows(10);
		gridHoanTraGDTTHH_USD.setHeightMode(HeightMode.ROW);
		gridHoanTraGDTTHH_USD.recalculateColumnWidths();
		
		containerHoanTraGDTTHH_USD = new IndexedContainer();
		
		containerHoanTraGDTTHH_USD.addContainerProperty("stt", Integer.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("soThe", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("maGd", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("ngayGd", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("ltgd", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("lttqt", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("maCapPhep", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("dvcnt", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("reversalInd", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("issuerCharge", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("statusCW", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("phiIsaGd", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("ghiChu", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("phiHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("thueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("tongPhiThueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("tongSoTienHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("tenChuTk", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("dvpht", String.class, "");
		containerHoanTraGDTTHH_USD.addContainerProperty("casa", String.class, "");
		
		gridHoanTraGDTTHH_USD.setContainerDataSource(containerHoanTraGDTTHH_USD);
		gridHoanTraGDTTHH_USD.getColumn("stt").setHeaderCaption("STT");
		gridHoanTraGDTTHH_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridHoanTraGDTTHH_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridHoanTraGDTTHH_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridHoanTraGDTTHH_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridHoanTraGDTTHH_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridHoanTraGDTTHH_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridHoanTraGDTTHH_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridHoanTraGDTTHH_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridHoanTraGDTTHH_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridHoanTraGDTTHH_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridHoanTraGDTTHH_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridHoanTraGDTTHH_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridHoanTraGDTTHH_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridHoanTraGDTTHH_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridHoanTraGDTTHH_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridHoanTraGDTTHH_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridHoanTraGDTTHH_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridHoanTraGDTTHH_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridHoanTraGDTTHH_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridHoanTraGDTTHH_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridHoanTraGDTTHH_USD.getColumn("phiIsaGd").setHeaderCaption("Phí ISA thời điểm GD");
		gridHoanTraGDTTHH_USD.getColumn("ghiChu").setHeaderCaption("Ghi chú");
		gridHoanTraGDTTHH_USD.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridHoanTraGDTTHH_USD.getColumn("phiHoanTraKH").setHeaderCaption("Phí hoàn trả KH");
		gridHoanTraGDTTHH_USD.getColumn("thueHoanTraKH").setHeaderCaption("Thuế hoàn trả KH");
		gridHoanTraGDTTHH_USD.getColumn("tongPhiThueHoanTraKH").setHeaderCaption("Tổng phí, thuế hoàn trả KH");
		gridHoanTraGDTTHH_USD.getColumn("tongSoTienHoanTraKH").setHeaderCaption("Tổng số tiền hoàn trả KH");
		gridHoanTraGDTTHH_USD.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridHoanTraGDTTHH_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridHoanTraGDTTHH_USD.getColumn("casa").setHeaderCaption("CASA");
		
		footerHoanTraGDTTHH_USD = gridHoanTraGDTTHH_USD.appendFooterRow();
		footerHoanTraGDTTHHTyGiaTQT_USD = gridHoanTraGDTTHH_USD.appendFooterRow();
		footerHoanTraGDTTHHThuPhiGD_USD = gridHoanTraGDTTHH_USD.appendFooterRow();
		footerHoanTraGDTTHHTongGDDuocTQT_USD = gridHoanTraGDTTHH_USD.appendFooterRow();
		
		//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
		gridHoanTraGDRTM_USD = new Grid();
		gridHoanTraGDRTM_USD.setSizeFull();
		gridHoanTraGDRTM_USD.setHeightByRows(10);
		gridHoanTraGDRTM_USD.setHeightMode(HeightMode.ROW);
		gridHoanTraGDRTM_USD.recalculateColumnWidths();
		
		containerHoanTraGDRTM_USD = new IndexedContainer();
		
		containerHoanTraGDRTM_USD.addContainerProperty("stt", Integer.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("soThe", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("maGd", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("ngayGd", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("ngayFileIncoming", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stGd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stTqt", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stQdVnd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("ltgd", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("lttqt", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("interchange", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("maCapPhep", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("dvcnt", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("reversalInd", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("issuerCharge", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("statusCW", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("phiIsaGd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("phiRtmGd", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("ghiChu", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("soTienGdHoanTra", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("phiHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("thueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("tongPhiThueHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("tongSoTienHoanTraKH", BigDecimal.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("tenChuTk", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("dvpht", String.class, "");
		containerHoanTraGDRTM_USD.addContainerProperty("casa", String.class, "");
		
		gridHoanTraGDRTM_USD.setContainerDataSource(containerHoanTraGDRTM_USD);
		gridHoanTraGDRTM_USD.getColumn("stt").setHeaderCaption("STT");
		gridHoanTraGDRTM_USD.getColumn("soThe").setHeaderCaption("Số thẻ");
		gridHoanTraGDRTM_USD.getColumn("maGd").setHeaderCaption("Mã GD");
		gridHoanTraGDRTM_USD.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		gridHoanTraGDRTM_USD.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		gridHoanTraGDRTM_USD.getColumn("stGd").setHeaderCaption("ST GD");
		gridHoanTraGDRTM_USD.getColumn("stTqt").setHeaderCaption("ST TQT");
		gridHoanTraGDRTM_USD.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		gridHoanTraGDRTM_USD.getColumn("ltgd").setHeaderCaption("LDGD");
		gridHoanTraGDRTM_USD.getColumn("lttqt").setHeaderCaption("LTTQT");
		gridHoanTraGDRTM_USD.getColumn("interchange").setHeaderCaption("Interchange");
		gridHoanTraGDRTM_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridHoanTraGDRTM_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		gridHoanTraGDRTM_USD.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
		gridHoanTraGDRTM_USD.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
		gridHoanTraGDRTM_USD.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
		gridHoanTraGDRTM_USD.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
		gridHoanTraGDRTM_USD.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
		gridHoanTraGDRTM_USD.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
		gridHoanTraGDRTM_USD.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
		gridHoanTraGDRTM_USD.getColumn("statusCW").setHeaderCaption("Status trên CW");
		gridHoanTraGDRTM_USD.getColumn("phiIsaGd").setHeaderCaption("Phí RTM thời điểm GD");
		gridHoanTraGDRTM_USD.getColumn("phiRtmGd").setHeaderCaption("Phí RTM thời điểm GD");
		gridHoanTraGDRTM_USD.getColumn("ghiChu").setHeaderCaption("Ghi chú");
		gridHoanTraGDRTM_USD.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
		gridHoanTraGDRTM_USD.getColumn("phiHoanTraKH").setHeaderCaption("Phí hoàn trả KH");
		gridHoanTraGDRTM_USD.getColumn("thueHoanTraKH").setHeaderCaption("Thuế hoàn trả KH");
		gridHoanTraGDRTM_USD.getColumn("tongPhiThueHoanTraKH").setHeaderCaption("Tổng phí, thuế hoàn trả KH");
		gridHoanTraGDRTM_USD.getColumn("tongSoTienHoanTraKH").setHeaderCaption("Tổng số tiền hoàn trả KH");
		gridHoanTraGDRTM_USD.getColumn("tenChuTk").setHeaderCaption("Tên chủ TK");
		gridHoanTraGDRTM_USD.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		gridHoanTraGDRTM_USD.getColumn("casa").setHeaderCaption("CASA");
		
		footerHoanTraGDRTM_USD = gridHoanTraGDRTM_USD.prependFooterRow();
		footerHoanTraGDRTMTyGiaTQT_USD = gridHoanTraGDRTM_USD.prependFooterRow();
		footerHoanTraGDRTMInterchangeKHCN_USD = gridHoanTraGDRTM_USD.prependFooterRow();
		footerHoanTraGDRTMInterchangeKHDN_USD = gridHoanTraGDRTM_USD.prependFooterRow();
		footerHoanTraGDRTMInterchangeTotal_USD = gridHoanTraGDRTM_USD.prependFooterRow();
		//6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
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
		gridGDBaoCoBatThuong_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoCoBatThuong_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		//7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
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
		gridGDBaoNoBatThuong_USD.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		gridGDBaoNoBatThuong_USD.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		
		
		btGiaoDichTheTTHH = new Button("[+] 1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA");
		btGiaoDichTheTTHH.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGiaoDichTheRTM = new Button("[+] 2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT");
		btGiaoDichTheRTM.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGiaoDichMoneySendFastFund = new Button("[+] 3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)");
		btGiaoDichMoneySendFastFund.setStyleName(ValoTheme.BUTTON_LINK);

		btHoanTraGiaoDichTTHH = new Button("[+] 4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN");
		btHoanTraGiaoDichTTHH.setStyleName(ValoTheme.BUTTON_LINK);
		
		btHoanTraGDRTM = new Button("[+] 5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN");
		btHoanTraGDRTM.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGDBaoCoBatThuong = new Button("[+] 6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>");
		btGDBaoCoBatThuong.setStyleName(ValoTheme.BUTTON_LINK);

		btGDBaoNoBatThuong = new Button("[+] 7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>");
		btGDBaoNoBatThuong.setStyleName(ValoTheme.BUTTON_LINK);
		
		final VerticalLayout verticalLayoutTQTTheoVND = new VerticalLayout();
		verticalLayoutTQTTheoVND.addComponent(btGiaoDichTheTTHH);
		verticalLayoutTQTTheoVND.addComponent(gridGDTTHHDuocTQT);
		verticalLayoutTQTTheoVND.addComponent(gridGDTTHHChiTietGDDuocTQT);
		verticalLayoutTQTTheoVND.addComponent(gridGDTTHHTrichNoBoSungKH);
		verticalLayoutTQTTheoVND.addComponent(gridGDTTHHHoanTraTienTrichThua);
		
		verticalLayoutTQTTheoVND.addComponent(btGiaoDichTheRTM);
		verticalLayoutTQTTheoVND.addComponent(gridGDRTMDuocTQT);
		verticalLayoutTQTTheoVND.addComponent(gridGDRTMChiTietGDDuocTQT);
		verticalLayoutTQTTheoVND.addComponent(gridGDRTMTrichNoBoSungKH);
		verticalLayoutTQTTheoVND.addComponent(gridGDRTMHoanTraTienTrichThua);
		
		verticalLayoutTQTTheoVND.addComponent(btGiaoDichMoneySendFastFund);
		verticalLayoutTQTTheoVND.addComponent(gridGDMoneySendFF);
		
		verticalLayoutTQTTheoVND.addComponent(btHoanTraGiaoDichTTHH);
		verticalLayoutTQTTheoVND.addComponent(gridHoanTraGDTTHH);
		
		verticalLayoutTQTTheoVND.addComponent(btHoanTraGDRTM);
		verticalLayoutTQTTheoVND.addComponent(gridHoanTraGDRTM);
		
		verticalLayoutTQTTheoVND.addComponent(btGDBaoCoBatThuong);
		verticalLayoutTQTTheoVND.addComponent(gridGDBaoCoBatThuong);
		
		verticalLayoutTQTTheoVND.addComponent(btGDBaoNoBatThuong);
		verticalLayoutTQTTheoVND.addComponent(gridGDBaoNoBatThuong);
		
		panelTQTTheoVND.setContent(verticalLayoutTQTTheoVND);
		
		final Panel panelTQTTheoUSD= new Panel("THANH QUYẾT TOÁN THEO LOẠI TIỀN USD");
		panelTQTTheoUSD.setStyleName(Reindeer.PANEL_LIGHT);
		panelTQTTheoUSD.setSizeFull();
		
		btGiaoDichTheTTHH_USD = new Button("[+] 1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA");
		btGiaoDichTheTTHH_USD.setStyleName(ValoTheme.BUTTON_LINK);

		btGiaoDichTheRTM_USD = new Button("[+] 2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT");
		btGiaoDichTheRTM_USD.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGiaoDichMoneySendFastFund_USD = new Button("[+] 3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)");
		btGiaoDichMoneySendFastFund_USD.setStyleName(ValoTheme.BUTTON_LINK);

		btHoanTraGiaoDichTTHH_USD = new Button("[+] 4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN");
		btHoanTraGiaoDichTTHH_USD.setStyleName(ValoTheme.BUTTON_LINK);
		
		btHoanTraGDRTM_USD = new Button("[+] 5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN");
		btHoanTraGDRTM_USD.setStyleName(ValoTheme.BUTTON_LINK);
		
		btGDBaoCoBatThuong_USD = new Button("[+] 6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>");
		btGDBaoCoBatThuong_USD.setStyleName(ValoTheme.BUTTON_LINK);

		btGDBaoNoBatThuong_USD = new Button("[+] 7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>");
		btGDBaoNoBatThuong_USD.setStyleName(ValoTheme.BUTTON_LINK);
		
		
		switch(loaiGD) {
			case "GDTTHH":
				visibleAllGrid(false);
				//VND
				gridGDTTHHDuocTQT.setVisible(true);
				gridGDTTHHChiTietGDDuocTQT.setVisible(true);
				gridGDTTHHTrichNoBoSungKH.setVisible(true);
				gridGDTTHHHoanTraTienTrichThua.setVisible(true);
				btGiaoDichTheTTHH.setCaption(btGiaoDichTheTTHH.getCaption().replace("+", "-"));
				//USD
				gridGDTTHHDuocTQT_USD.setVisible(true);
				gridGDTTHHChiTietGDDuocTQT_USD.setVisible(true);
				gridGDTTHHTrichNoBoSungKH_USD.setVisible(true);
				gridGDTTHHHoanTraTienTrichThua_USD.setVisible(true);
				btGiaoDichTheTTHH_USD.setCaption(btGiaoDichTheTTHH_USD.getCaption().replace("+", "-"));
				break;
			case "GDRTIM":
				visibleAllGrid(false);
				//VND
				gridGDRTMDuocTQT.setVisible(true);
				gridGDRTMChiTietGDDuocTQT.setVisible(true);
				gridGDRTMTrichNoBoSungKH.setVisible(true);
				gridGDRTMHoanTraTienTrichThua.setVisible(true);
				btGiaoDichTheRTM.setCaption(btGiaoDichTheRTM.getCaption().replace("+", "-"));
				//USD
				gridGDRTMDuocTQT_USD.setVisible(true);
				gridGDRTMChiTietGDDuocTQT_USD.setVisible(true);
				gridGDRTMTrichNoBoSungKH_USD.setVisible(true);
				gridGDRTMHoanTraTienTrichThua_USD.setVisible(true);
				btGiaoDichTheRTM_USD.setCaption(btGiaoDichTheRTM_USD.getCaption().replace("+", "-"));
				break;
			case "GDMSFF": 
				visibleAllGrid(false);
				//VND
				gridGDMoneySendFF.setVisible(true);
				btGiaoDichMoneySendFastFund.setCaption(btGiaoDichMoneySendFastFund.getCaption().replace("+", "-"));
				//USD
				gridGDMoneySendFF_USD.setVisible(true);
				btGiaoDichMoneySendFastFund_USD.setCaption(btGiaoDichMoneySendFastFund_USD.getCaption().replace("+", "-"));
				break;
			case "HTGDTT": 
				visibleAllGrid(false);
				//VND
				gridHoanTraGDTTHH.setVisible(true);
				btHoanTraGiaoDichTTHH.setCaption(btHoanTraGiaoDichTTHH.getCaption().replace("+", "-"));
				//USD
				gridHoanTraGDTTHH_USD.setVisible(true);
				btHoanTraGiaoDichTTHH_USD.setCaption(btHoanTraGiaoDichTTHH_USD.getCaption().replace("+", "-"));
				break;
			case "HTGDRT": 
				visibleAllGrid(false);
				//VND
				gridHoanTraGDRTM.setVisible(true);
				btHoanTraGDRTM.setCaption(btHoanTraGDRTM.getCaption().replace("+", "-"));
				//USD
				gridHoanTraGDRTM_USD.setVisible(true);
				btHoanTraGDRTM_USD.setCaption(btHoanTraGDRTM_USD.getCaption().replace("+", "-"));
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
				gridGDTTHHChiTietGDDuocTQT.setVisible(true);
				gridGDTTHHTrichNoBoSungKH.setVisible(true);
				gridGDTTHHHoanTraTienTrichThua.setVisible(true);
			}
			else {
				btGiaoDichTheTTHH.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDTTHHDuocTQT.setVisible(false);
				gridGDTTHHChiTietGDDuocTQT.setVisible(false);
				gridGDTTHHTrichNoBoSungKH.setVisible(false);
				gridGDTTHHHoanTraTienTrichThua.setVisible(false);
			}
		});
		
		btGiaoDichTheRTM.addClickListener(event -> {
			if(btGiaoDichTheRTM.getCaption().contains("+")) {
				btGiaoDichTheRTM.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDRTMDuocTQT.setVisible(true);
				gridGDRTMChiTietGDDuocTQT.setVisible(true);
				gridGDRTMTrichNoBoSungKH.setVisible(true);
				gridGDRTMHoanTraTienTrichThua.setVisible(true);
			}
			else {
				btGiaoDichTheRTM.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDRTMDuocTQT.setVisible(false);
				gridGDRTMChiTietGDDuocTQT.setVisible(false);
				gridGDRTMTrichNoBoSungKH.setVisible(false);
				gridGDRTMHoanTraTienTrichThua.setVisible(false);
			}
		});
		
		btGiaoDichMoneySendFastFund.addClickListener(event -> {
			if(btGiaoDichMoneySendFastFund.getCaption().contains("+")) {
				btGiaoDichMoneySendFastFund.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDMoneySendFF.setVisible(true);
			}
			else {
				btGiaoDichMoneySendFastFund.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDMoneySendFF.setVisible(false);
			}
		});
		
		btHoanTraGiaoDichTTHH.addClickListener(event -> {
			if(btHoanTraGiaoDichTTHH.getCaption().contains("+")) {
				btHoanTraGiaoDichTTHH.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridHoanTraGDTTHH.setVisible(true);
			}
			else {
				btHoanTraGiaoDichTTHH.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridHoanTraGDTTHH.setVisible(false);
			}
		});
		
		btHoanTraGDRTM.addClickListener(event -> {
			if(btHoanTraGDRTM.getCaption().contains("+")) {
				btHoanTraGDRTM.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridHoanTraGDRTM.setVisible(true);
			}
			else {
				btHoanTraGDRTM.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridHoanTraGDRTM.setVisible(false);
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
				gridGDTTHHChiTietGDDuocTQT_USD.setVisible(true);
				gridGDTTHHTrichNoBoSungKH_USD.setVisible(true);
				gridGDTTHHHoanTraTienTrichThua_USD.setVisible(true);
			}
			else {
				btGiaoDichTheTTHH_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDTTHHDuocTQT_USD.setVisible(false);
				gridGDTTHHChiTietGDDuocTQT_USD.setVisible(false);
				gridGDTTHHTrichNoBoSungKH_USD.setVisible(false);
				gridGDTTHHHoanTraTienTrichThua_USD.setVisible(false);
			}
		});
		
		btGiaoDichTheRTM_USD.addClickListener(event -> {
			if(btGiaoDichTheRTM_USD.getCaption().contains("+")) {
				btGiaoDichTheRTM_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDRTMDuocTQT_USD.setVisible(true);
				gridGDRTMChiTietGDDuocTQT_USD.setVisible(true);
				gridGDRTMTrichNoBoSungKH_USD.setVisible(true);
				gridGDRTMHoanTraTienTrichThua_USD.setVisible(true);
			}
			else {
				btGiaoDichTheRTM_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDRTMDuocTQT_USD.setVisible(false);
				gridGDRTMChiTietGDDuocTQT_USD.setVisible(false);
				gridGDRTMTrichNoBoSungKH_USD.setVisible(false);
				gridGDRTMHoanTraTienTrichThua_USD.setVisible(false);
			}
		});
		
		btGiaoDichMoneySendFastFund_USD.addClickListener(event -> {
			if(btGiaoDichMoneySendFastFund_USD.getCaption().contains("+")) {
				btGiaoDichMoneySendFastFund_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridGDMoneySendFF_USD.setVisible(true);
			}
			else {
				btGiaoDichMoneySendFastFund_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridGDMoneySendFF_USD.setVisible(false);
			}
		});
		
		btHoanTraGiaoDichTTHH_USD.addClickListener(event -> {
			if(btHoanTraGiaoDichTTHH_USD.getCaption().contains("+")) {
				btHoanTraGiaoDichTTHH_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridHoanTraGDTTHH_USD.setVisible(true);
			}
			else {
				btHoanTraGiaoDichTTHH_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridHoanTraGDTTHH_USD.setVisible(false);
			}
		});
		
		btHoanTraGDRTM_USD.addClickListener(event -> {
			if(btHoanTraGDRTM_USD.getCaption().contains("+")) {
				btHoanTraGDRTM_USD.setCaption(event.getButton().getCaption().replace("+", "-"));
				gridHoanTraGDRTM_USD.setVisible(true);
			}
			else {
				btHoanTraGDRTM_USD.setCaption(event.getButton().getCaption().replace("-", "+"));
				gridHoanTraGDRTM_USD.setVisible(false);
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
		verticalLayoutTQTTheoUSD.addComponent(gridGDTTHHChiTietGDDuocTQT_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDTTHHTrichNoBoSungKH_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDTTHHHoanTraTienTrichThua_USD);
		
		verticalLayoutTQTTheoUSD.addComponent(btGiaoDichTheRTM_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDRTMDuocTQT_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDRTMChiTietGDDuocTQT_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDRTMTrichNoBoSungKH_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDRTMHoanTraTienTrichThua_USD);
		
		verticalLayoutTQTTheoUSD.addComponent(btGiaoDichMoneySendFastFund_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridGDMoneySendFF_USD);
		
		verticalLayoutTQTTheoUSD.addComponent(btHoanTraGiaoDichTTHH_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridHoanTraGDTTHH_USD);
		
		verticalLayoutTQTTheoUSD.addComponent(btHoanTraGDRTM_USD);
		verticalLayoutTQTTheoUSD.addComponent(gridHoanTraGDRTM_USD);
		
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
						initGridGDTTHHChiTietGDDuocTQT("VND");
						initGridGDTTHHTrichNoBoSungKH("VND");
						initGridGDTTHHHoanTraTienTrichThua("VND");
						break;
					case "GDRTIM":
						initGridGDRTMDuocTQT("VND");
						initGridGDRTMChiTietGDDuocTQT("VND");
						initGridGDRTMTrichNoBoSungKH("VND");
						initGridGDRTMHoanTraTienTrichThua("VND");
						break;
					case "GDMSFF": 
						initGridGDMoneySendFF("VND");
						break;
					case "HTGDTT": 
						initGridHoanTraGDTTHH("VND");
						break;
					case "HTGDRT": 
						initGridHoanTraGDRTM("VND");
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
						initGridGDTTHHChiTietGDDuocTQT("VND");
						initGridGDTTHHTrichNoBoSungKH("VND");
						initGridGDTTHHHoanTraTienTrichThua("VND");
						initGridGDRTMDuocTQT("VND");
						initGridGDRTMChiTietGDDuocTQT("VND");
						initGridGDRTMTrichNoBoSungKH("VND");
						initGridGDRTMHoanTraTienTrichThua("VND");
						initGridGDMoneySendFF("VND");
						initGridHoanTraGDTTHH("VND");
						initGridHoanTraGDRTM("VND");
						initGridGDBaoCoBatThuong("VND");
						initGridGDBaoNoBatThuong("VND");
						break;
				}
				
			case "USD":
				switch(loaiGD) {
					case "GDTTHH":
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDTTHHChiTietGDDuocTQT_USD("USD");
						initGridGDTTHHTrichNoBoSungKH_USD("USD");
						initGridGDTTHHHoanTraTienTrichThua_USD("USD");
						break;
					case "GDRTIM":
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDRTMChiTietGDDuocTQT_USD("USD");
						initGridGDRTMTrichNoBoSungKH_USD("USD");
						initGridGDRTMHoanTraTienTrichThua_USD("USD");
						break;
					case "GDMSFF": 
						initGridGDMoneySendFF_USD("USD");
						break;
					case "HTGDTT": 
						initGridHoanTraGDTTHH_USD("USD");
						break;
					case "HTGDRT": 
						initGridHoanTraGDRTM_USD("USD");
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
						initGridGDTTHHChiTietGDDuocTQT_USD("USD");
						initGridGDTTHHTrichNoBoSungKH_USD("USD");
						initGridGDTTHHHoanTraTienTrichThua_USD("USD");
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDRTMChiTietGDDuocTQT_USD("USD");
						initGridGDRTMTrichNoBoSungKH_USD("USD");
						initGridGDRTMHoanTraTienTrichThua_USD("USD");
						initGridGDMoneySendFF_USD("USD");
						initGridHoanTraGDTTHH_USD("USD");
						initGridHoanTraGDRTM_USD("USD");
						initGridGDBaoCoBatThuong_USD("USD");
						initGridGDBaoNoBatThuong_USD("USD");
						break;
				}
				
			default:
				switch(loaiGD) {
					case "GDTTHH":
						initGridGDTTHHDuocTQT("VND");
						initGridGDTTHHChiTietGDDuocTQT("VND");
						initGridGDTTHHTrichNoBoSungKH("VND");
						initGridGDTTHHHoanTraTienTrichThua("VND");
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDTTHHChiTietGDDuocTQT_USD("USD");
						initGridGDTTHHTrichNoBoSungKH_USD("USD");
						initGridGDTTHHHoanTraTienTrichThua_USD("USD");
						break;
					case "GDRTIM":
						initGridGDRTMDuocTQT("VND");
						initGridGDRTMChiTietGDDuocTQT("VND");
						initGridGDRTMTrichNoBoSungKH("VND");
						initGridGDRTMHoanTraTienTrichThua("VND");
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDRTMChiTietGDDuocTQT_USD("USD");
						initGridGDRTMTrichNoBoSungKH_USD("USD");
						initGridGDRTMHoanTraTienTrichThua_USD("USD");
						break;
					case "GDMSFF": 
						initGridGDMoneySendFF("VND");
						initGridGDMoneySendFF_USD("USD");
						break;
					case "HTGDTT": 
						initGridHoanTraGDTTHH("VND");
						initGridHoanTraGDTTHH_USD("USD");
						break;
					case "HTGDRT": 
						initGridHoanTraGDRTM("VND");
						initGridHoanTraGDTTHH_USD("USD");
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
						initGridGDTTHHChiTietGDDuocTQT("VND");
						initGridGDTTHHTrichNoBoSungKH("VND");
						initGridGDTTHHHoanTraTienTrichThua("VND");
						initGridGDRTMDuocTQT("VND");
						initGridGDRTMChiTietGDDuocTQT("VND");
						initGridGDRTMTrichNoBoSungKH("VND");
						initGridGDRTMHoanTraTienTrichThua("VND");
						initGridGDMoneySendFF("VND");
						initGridHoanTraGDTTHH("VND");
						initGridHoanTraGDRTM("VND");
						initGridGDBaoCoBatThuong("VND");
						initGridGDBaoNoBatThuong("VND");
						//USD - INIT & LOAT DATA 
						initGridGDTTHHDuocTQT_USD("USD");
						initGridGDTTHHChiTietGDDuocTQT_USD("USD");
						initGridGDTTHHTrichNoBoSungKH_USD("USD");
						initGridGDTTHHHoanTraTienTrichThua_USD("USD");
						initGridGDRTMDuocTQT_USD("USD");
						initGridGDRTMChiTietGDDuocTQT_USD("USD");
						initGridGDRTMTrichNoBoSungKH_USD("USD");
						initGridGDRTMHoanTraTienTrichThua_USD("USD");
						initGridGDMoneySendFF_USD("USD");
						initGridHoanTraGDTTHH_USD("USD");
						initGridHoanTraGDRTM_USD("USD");
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
			switch(loaitienTqt) {
				case "VND": 
					filename = "DoiChieuTqtVnd.jasper";
					addExtension(downloader);
					myResourceXLSX = createTransMKResourceXLSDoiChieuTqtVnd();
					downloader.setFileDownloadResource(myResourceXLSX);
					break;
				case "USD": 
					filename = "DoiChieuTqtVnd.jasper";
					addExtension(downloader);
					myResourceXLSX = createTransMKResourceXLSDoiChieuTqtUsd();
					downloader.setFileDownloadResource(myResourceXLSX);
					break;
			}
			
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
			gridGDTTHHChiTietGDDuocTQT.setVisible(true);
			gridGDTTHHTrichNoBoSungKH.setVisible(true);
			gridGDTTHHHoanTraTienTrichThua.setVisible(true);
			gridGDMoneySendFF.setVisible(true);
			gridGDRTMDuocTQT.setVisible(true);
			gridGDRTMChiTietGDDuocTQT.setVisible(true);
			gridGDRTMTrichNoBoSungKH.setVisible(true);
			gridGDRTMHoanTraTienTrichThua.setVisible(true);
			gridHoanTraGDTTHH.setVisible(true);
			gridHoanTraGDRTM.setVisible(true);
			gridGDBaoCoBatThuong.setVisible(true);
			gridGDBaoNoBatThuong.setVisible(true);
			
			btGiaoDichTheTTHH.setCaption(btGiaoDichTheTTHH.getCaption().replace("+", "-"));
			btGiaoDichTheRTM.setCaption(btGiaoDichTheRTM.getCaption().replace("+", "-"));
			btGiaoDichMoneySendFastFund.setCaption(btGiaoDichMoneySendFastFund.getCaption().replace("+", "-"));
			btHoanTraGiaoDichTTHH.setCaption(btHoanTraGiaoDichTTHH.getCaption().replace("+", "-"));
			btHoanTraGDRTM.setCaption(btHoanTraGDRTM.getCaption().replace("+", "-"));
			btGDBaoCoBatThuong.setCaption(btGDBaoCoBatThuong.getCaption().replace("+", "-"));
			btGDBaoNoBatThuong.setCaption(btGDBaoNoBatThuong.getCaption().replace("+", "-"));
			
			//USD
			gridGDTTHHDuocTQT_USD.setVisible(true);
			gridGDTTHHChiTietGDDuocTQT_USD.setVisible(true);
			gridGDTTHHTrichNoBoSungKH_USD.setVisible(true);
			gridGDTTHHHoanTraTienTrichThua_USD.setVisible(true);
			gridGDMoneySendFF_USD.setVisible(true);
			gridGDRTMDuocTQT_USD.setVisible(true);
			gridGDRTMChiTietGDDuocTQT_USD.setVisible(true);
			gridGDRTMTrichNoBoSungKH_USD.setVisible(true);
			gridGDRTMHoanTraTienTrichThua_USD.setVisible(true);
			gridHoanTraGDTTHH_USD.setVisible(true);
			gridHoanTraGDRTM_USD.setVisible(true);
			gridGDBaoCoBatThuong_USD.setVisible(true);
			gridGDBaoNoBatThuong_USD.setVisible(true);
			
			btGiaoDichTheTTHH_USD.setCaption(btGiaoDichTheTTHH_USD.getCaption().replace("+", "-"));
			btGiaoDichTheRTM_USD.setCaption(btGiaoDichTheRTM_USD.getCaption().replace("+", "-"));
			btGiaoDichMoneySendFastFund_USD.setCaption(btGiaoDichMoneySendFastFund_USD.getCaption().replace("+", "-"));
			btHoanTraGiaoDichTTHH_USD.setCaption(btHoanTraGiaoDichTTHH_USD.getCaption().replace("+", "-"));
			btHoanTraGDRTM_USD.setCaption(btHoanTraGDRTM_USD.getCaption().replace("+", "-"));
			btGDBaoCoBatThuong_USD.setCaption(btGDBaoCoBatThuong_USD.getCaption().replace("+", "-"));
			btGDBaoNoBatThuong_USD.setCaption(btGDBaoNoBatThuong_USD.getCaption().replace("+", "-"));
			
		}
		else
		{
			//VND
			gridGDTTHHDuocTQT.setVisible(false);
			gridGDTTHHChiTietGDDuocTQT.setVisible(false);
			gridGDTTHHTrichNoBoSungKH.setVisible(false);
			gridGDTTHHHoanTraTienTrichThua.setVisible(false);
			gridGDMoneySendFF.setVisible(false);
			gridGDRTMDuocTQT.setVisible(false);
			gridGDRTMChiTietGDDuocTQT.setVisible(false);
			gridGDRTMTrichNoBoSungKH.setVisible(false);
			gridGDRTMHoanTraTienTrichThua.setVisible(false);
			gridHoanTraGDTTHH.setVisible(false);
			gridHoanTraGDRTM.setVisible(false);
			gridGDBaoCoBatThuong.setVisible(false);
			gridGDBaoNoBatThuong.setVisible(false);
			
			btGiaoDichTheTTHH.setCaption(btGiaoDichTheTTHH.getCaption().replace("-", "+"));
			btGiaoDichTheRTM.setCaption(btGiaoDichTheRTM.getCaption().replace("-", "+"));
			btGiaoDichMoneySendFastFund.setCaption(btGiaoDichMoneySendFastFund.getCaption().replace("-", "+"));
			btHoanTraGiaoDichTTHH.setCaption(btHoanTraGiaoDichTTHH.getCaption().replace("-", "+"));
			btHoanTraGDRTM.setCaption(btHoanTraGDRTM.getCaption().replace("-", "+"));
			btGDBaoCoBatThuong.setCaption(btGDBaoCoBatThuong.getCaption().replace("-", "+"));
			btGDBaoNoBatThuong.setCaption(btGDBaoNoBatThuong.getCaption().replace("-", "+"));
			
			//USD
			gridGDTTHHDuocTQT_USD.setVisible(false);
			gridGDTTHHChiTietGDDuocTQT_USD.setVisible(false);
			gridGDTTHHTrichNoBoSungKH_USD.setVisible(false);
			gridGDTTHHHoanTraTienTrichThua_USD.setVisible(false);
			gridGDMoneySendFF_USD.setVisible(false);
			gridGDRTMDuocTQT_USD.setVisible(false);
			gridGDRTMChiTietGDDuocTQT_USD.setVisible(false);
			gridGDRTMTrichNoBoSungKH_USD.setVisible(false);
			gridGDRTMHoanTraTienTrichThua_USD.setVisible(false);
			gridHoanTraGDTTHH_USD.setVisible(false);
			gridHoanTraGDRTM_USD.setVisible(false);
			gridGDBaoCoBatThuong_USD.setVisible(false);
			gridGDBaoNoBatThuong_USD.setVisible(false);
			
			btGiaoDichTheTTHH_USD.setCaption(btGiaoDichTheTTHH_USD.getCaption().replace("-", "+"));
			btGiaoDichTheRTM_USD.setCaption(btGiaoDichTheRTM_USD.getCaption().replace("-", "+"));
			btGiaoDichMoneySendFastFund_USD.setCaption(btGiaoDichMoneySendFastFund_USD.getCaption().replace("-", "+"));
			btHoanTraGiaoDichTTHH_USD.setCaption(btHoanTraGiaoDichTTHH_USD.getCaption().replace("-", "+"));
			btHoanTraGDRTM_USD.setCaption(btHoanTraGDRTM_USD.getCaption().replace("-", "+"));
			btGDBaoCoBatThuong_USD.setCaption(btGDBaoCoBatThuong_USD.getCaption().replace("-", "+"));
			btGDBaoNoBatThuong_USD.setCaption(btGDBaoNoBatThuong_USD.getCaption().replace("-", "+"));
		}
	}
	
	//1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA
	private Page<DoiSoatData> getDataGDTTHHDuocTQT(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr) 
						&& ((i.getMaGd().startsWith("00") && i.getStatusCw().equals(" ") && !i.getReversalInd().equals("R"))
							|| (i.getMaGd().startsWith("00") && !i.getStatusCw().equals(" ") && i.getReversalInd().equals("R"))
							|| (i.getMaGd().startsWith("18") && i.getStatusCw().equals(" "))
							|| (i.getMaGd().startsWith("05") && i.getStatusCw().equals(" "))
							|| (i.getMaGd().startsWith("25") && !i.getStatusCw().equals(" ")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDTTHH = dsDataListGDTTHH.stream().filter(i -> i.getLttqt().equals(curr)).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDTTHH);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDTTHHDuocTQT(curr));
		return result;
	}
	
	private Page<DoiSoatData> getDataGDTTHHChiTietGDDuocTQT(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& ((i.getMaGd().startsWith("00") && i.getStatusCw().equals(" ") && !i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("00") && !i.getStatusCw().equals(" ") && i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("18") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("05") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("25") && !i.getStatusCw().equals(" ")))
						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)!=0 
						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)!=0).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDTTHH = dsDataListGDTTHH.stream()
//				.filter(i -> i.getLttqt().equals(curr) 
//						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)!=0 
//						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)!=0).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDTTHH);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDTTHHChiTietGDDuocTQT(curr));
		return result;
	}
	
	private Page<DoiSoatData> getDataGDTTHHTrichNoBoSungKH(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& ((i.getMaGd().startsWith("00") && i.getStatusCw().equals(" ") && !i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("00") && !i.getStatusCw().equals(" ") && i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("18") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("05") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("25") && !i.getStatusCw().equals(" ")))
						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)>0
						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDTTHH = dsDataListGDTTHH.stream()
//				.filter(i -> i.getLttqt().equals(curr) 
//						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)>0
//						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDTTHH);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDTTHHTrichNoBoSungKH(curr));
		return result;
	}
	
	private Page<DoiSoatData> getDataGDTTHHHoanTraTienTrichThua(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& ((i.getMaGd().startsWith("00") && i.getStatusCw().equals(" ") && !i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("00") && !i.getStatusCw().equals(" ") && i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("18") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("05") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("25") && !i.getStatusCw().equals(" ")))
						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)<0
						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)<0).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDTTHH = dsDataListGDTTHH.stream()
//				.filter(i -> i.getLttqt().equals(curr) 
//						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)<0
//						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)<0).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDTTHH);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDTTHHHoanTraTienTrichThua(curr));
		return result;
	}
	
	//2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT
	private Page<DoiSoatData> getDataGDRTMDuocTQT(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& (((i.getMaGd().startsWith("01") || i.getMaGd().startsWith("12")) && i.getStatusCw().equals(" ") && i.getReversalInd().equals(" "))
								|| ((i.getMaGd().startsWith("01") || i.getMaGd().startsWith("12")) && !i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("07"))
								|| (i.getMaGd().startsWith("27") && !i.getStatusCw().equals(" ")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDRTM = dsDataListGDRTM.stream().filter(i -> i.getLttqt().equals(curr)).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDRTM);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDRTMDuocTQT(curr));
		return result;
	}
	
	private Page<DoiSoatData> getDataGDRTMChiTietGDDuocTQT(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& (((i.getMaGd().startsWith("01") || i.getMaGd().startsWith("12")) && i.getReversalInd().equals(" "))
								|| (i.getMaGd().startsWith("07"))
								|| (i.getMaGd().startsWith("27") && !i.getStatusCw().equals(" ")))
						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)!=0 
						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)!=0).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDRTM = dsDataListGDRTM.stream()
//				.filter(i -> i.getLttqt().equals(curr) 
//						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)!=0 
//						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)!=0).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDRTM);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDRTMChiTietGDDuocTQT(curr));
		return result;
	}
	
	private Page<DoiSoatData> getDataGDRTMTrichNoBoSungKH(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& (((i.getMaGd().startsWith("01") || i.getMaGd().startsWith("12")) && i.getReversalInd().equals(" "))
								|| (i.getMaGd().startsWith("07"))
								|| (i.getMaGd().startsWith("27") && !i.getStatusCw().equals(" ")))
						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)>0 
						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDRTM = dsDataListGDRTM.stream()
//				.filter(i -> i.getLttqt().equals(curr) 
//						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)>0 
//						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDRTM);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDRTMTrichNoBoSungKH(curr));
		return result;
	}
	
	private Page<DoiSoatData> getDataGDRTMHoanTraTienTrichThua(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream()
				.filter(i -> i.getLttqt().equals(curr) 
						&& (((i.getMaGd().startsWith("01") || i.getMaGd().startsWith("12")) && i.getReversalInd().equals(" "))
								|| (i.getMaGd().startsWith("07"))
								|| (i.getMaGd().startsWith("27") && !i.getStatusCw().equals(" ")))
						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)<0 
						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)<0).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDRTM = dsDataListGDRTM.stream()
//				.filter(i -> i.getLttqt().equals(curr) 
//						&& i.getStgdChenhLechDoTyGia().compareTo(BigDecimal.ZERO)<0 
//						&& i.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)<0).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDRTM);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDRTMHoanTraTienTrichThua(curr));
		return result;
	}
	
	//3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
	private Page<DoiSoatData> getDataGDMoneySendFF(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& ((i.getMaGd().startsWith("28") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("06") && i.getStatusCw().equals(" ") && i.getMerchantCity().contains("Visa Direct")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDMoneySendFF = dsDataListGDMoneySendFF.stream().filter(i -> i.getLttqt().equals(curr)).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDMoneySendFF);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDMoneySendFF(curr));
		return result;
	}
	
	//4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
	private Page<DoiSoatData> getDataHoanTraGDTTHH(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& ((i.getMaGd().startsWith("20") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("00") && !i.getStatusCw().equals(" ") && i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("25") && i.getStatusCw().equals(" "))
								|| (i.getMaGd().startsWith("06") && i.getStatusCw().equals(" ") && !i.getMerchantCity().contains("Merchant City")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListHoanTraGDTTHH = dsDataListHoanTraGDTTHH.stream().filter(i -> i.getLttqt().equals(curr)).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListHoanTraGDTTHH);
//	    result = new PageImpl<>(doiSoatDataService.findAllHoanTraGDTTHH(curr));
		return result;
	}
	
	//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
	private Page<DoiSoatData> getDataHoanTraGDRTM(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& ((i.getMaGd().startsWith("01") && i.getStatusCw().equals(" ") && i.getReversalInd().equals("R"))
								|| (i.getMaGd().startsWith("27") && i.getStatusCw().equals(" ")))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		result = new PageImpl<>(dsDataListHoanTraGDRTM);
//	    result = new PageImpl<>(doiSoatDataService.findAllHoanTraGDRTM(curr));
		return result;
	}
	
	//6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
	private Page<DoiSoatData> getDataGDBaoCoBatThuong(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& (i.getMaGd().startsWith("29") || i.getMaGd().startsWith("10"))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDBaoCoBatThuong = dsDataListGDBaoCoBatThuong.stream().filter(i -> i.getLttqt().equals(curr)).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDBaoCoBatThuong);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDBaoCoBatThuong(curr));
		return result;
	}
	
	//7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
	private Page<DoiSoatData> getDataGDBaoNoBatThuong(String curr, Pageable page) {
		doisoatDataListTemp = doisoatDataList.stream().
				filter(i -> i.getLttqt().equals(curr)
						&& (i.getMaGd().startsWith("19") || i.getMaGd().startsWith("30") || i.getMaGd().startsWith("20"))).collect(Collectors.toList());
		result = new PageImpl<>(doisoatDataListTemp);
//		dsDataListGDBaoNoBatThuong = dsDataListGDBaoNoBatThuong.stream().filter(i -> i.getLttqt().equals(curr)).collect(Collectors.toList());
//		result = new PageImpl<>(dsDataListGDBaoNoBatThuong);
//	    result = new PageImpl<>(doiSoatDataService.findAllGDBaoNoBatThuong(curr));
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
			footerGDTTHHDuocTQT.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		}
	
	}
	
	public void initGridGDTTHHChiTietGDDuocTQT(String curr) {
		dataSourceGDTTHHChiTietGDDuocTQT = getDataGDTTHHChiTietGDDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHChiTietGDDuocTQT() == true) {
//			if (!gridGDTTHHChiTietGDDuocTQT.isVisible()) {
//				gridGDTTHHChiTietGDDuocTQT.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDTTHHTrichNoBoSungKH(String curr) {
		dataSourceGDTTHHTrichNoBoSungKH = getDataGDTTHHTrichNoBoSungKH(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHTrichNoBoSungKH() == true) {
//			if (!gridGDTTHHTrichNoBoSungKH.isVisible()) {
//				gridGDTTHHTrichNoBoSungKH.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDTTHHHoanTraTienTrichThua(String curr) {
		dataSourceGDTTHHHoanTraTienTrichThua = getDataGDTTHHHoanTraTienTrichThua(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHHoanTraTienTrichThua() == true) {
//			if (!gridGDTTHHHoanTraTienTrichThua.isVisible()) {
//				gridGDTTHHHoanTraTienTrichThua.setVisible(true);
//			}
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
			footerGDRTMDuocTQT.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
			interchangeGdrtmKhcn = BigDecimal.ZERO;
			interchangeGdrtmKhdn = BigDecimal.ZERO;
			try {
				List<PhiInterchange> interchangeList = phiInterchangeService.phiInterchangeGDRTMDuocTQT("VND",ngayAdv);
				for(PhiInterchange s : interchangeList) {
					switch(s.getCustType()) {
						case "I":
							interchangeGdrtmKhcn = interchangeGdrtmKhcn.add(s.getInterchange());
							break;
						case "C":
							interchangeGdrtmKhdn = interchangeGdrtmKhdn.add(s.getInterchange());
							break;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			footerGDRTMDuocTQTInterchangeKHCN.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHCN" +"</div>");
			footerGDRTMDuocTQTInterchangeKHCN.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ interchangeGdrtmKhcn +"</div>");
			
			footerGDRTMDuocTQTInterchangeKHDN.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHDN" +"</div>");
			footerGDRTMDuocTQTInterchangeKHDN.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ interchangeGdrtmKhdn +"</div>");
			
			footerGDRTMDuocTQTInterchangeTotal.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ "Tổng phí interchange GD RTM" +"</div>");
			footerGDRTMDuocTQTInterchangeTotal.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ interchangeGdrtmKhcn.add(interchangeGdrtmKhdn) +"</div>");
		}
	
	}
	
	public void initGridGDRTMChiTietGDDuocTQT(String curr) {
		dataSourceGDRTMChiTietGDDuocTQT = getDataGDRTMChiTietGDDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMChiTietGDDuocTQT() == true) {
//			if (!gridGDRTMChiTietGDDuocTQT.isVisible()) {
//				gridGDRTMChiTietGDDuocTQT.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDRTMTrichNoBoSungKH(String curr) {
		dataSourceGDRTMTrichNoBoSungKH = getDataGDRTMTrichNoBoSungKH(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMTrichNoBoSungKH() == true) {
//			if (!gridGDRTMTrichNoBoSungKH.isVisible()) {
//				gridGDRTMTrichNoBoSungKH.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDRTMHoanTraTienTrichThua(String curr) {
		dataSourceGDRTMHoanTraTienTrichThua = getDataGDRTMHoanTraTienTrichThua(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMHoanTraTienTrichThua() == true) {
//			if (!gridGDRTMHoanTraTienTrichThua.isVisible()) {
//				gridGDRTMHoanTraTienTrichThua.setVisible(true);
//			}
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
			footerGDMoneySendFF.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		}
	
	}
	
	public void initGridHoanTraGDTTHH(String curr) {
		dataSourceHoanTraGDTTHH = getDataHoanTraGDTTHH(curr,new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerHoanTraGDTTHH() == true) {
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerHoanTraGDTTHH.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerHoanTraGDTTHH.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		}
		
	}
	
	public void initGridHoanTraGDRTM(String curr) {
		dataSourceHoanTraGDRTM = getDataHoanTraGDRTM(curr,new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerHoanTraGDRTM() == true) {
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerHoanTraGDRTM.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerHoanTraGDRTM.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
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
			
			footerGDTTHHDuocTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tổng cộng</div>");
			footerGDTTHHDuocTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(stTqtTotal)+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().setScale(0, RoundingMode.HALF_UP).doubleValue())+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(interchangeTotal)+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDTTHHDuocTQT_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDTTHHDuocTQT_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			
			footerGDTTHHDuocTQTTyGiaTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tỷ giá TQT</div>");
			footerGDTTHHDuocTQTTyGiaTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>" + tyGiaTQT + "</div>");
			
			footerGDTTHHDuocTQTThuPhiGD_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Thu phí GD TTHH</div>");
			footerGDTTHHDuocTQTThuPhiGD_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format(interchangeTotal) + "</div>");
			footerGDTTHHDuocTQTThuPhiGD_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format(interchangeTotal.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) + "</div>");
			
			footerGDTTHHDuocTQTTongGDDuocTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tổng GD TTHH được TQT</div>");
			footerGDTTHHDuocTQTTongGDDuocTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format(stTqtTotal.subtract(interchangeTotal)) + "</div>");
			footerGDTTHHDuocTQTTongGDDuocTQT_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format((stTqtTotal.subtract(interchangeTotal)).multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) + "</div>");
		}
	
	}
	
	public void initGridGDTTHHChiTietGDDuocTQT_USD(String curr) {
		dataSourceGDTTHHChiTietGDDuocTQT_USD = getDataGDTTHHChiTietGDDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHChiTietGDDuocTQT_USD() == true) {
//			if (!gridGDTTHHChiTietGDDuocTQT_USD.isVisible()) {
//				gridGDTTHHChiTietGDDuocTQT_USD.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDTTHHTrichNoBoSungKH_USD(String curr) {
		dataSourceGDTTHHTrichNoBoSungKH_USD = getDataGDTTHHTrichNoBoSungKH(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHTrichNoBoSungKH_USD() == true) {
//			if (!gridGDTTHHTrichNoBoSungKH_USD.isVisible()) {
//				gridGDTTHHTrichNoBoSungKH_USD.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDTTHHHoanTraTienTrichThua_USD(String curr) {
		dataSourceGDTTHHHoanTraTienTrichThua_USD = getDataGDTTHHHoanTraTienTrichThua(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDTTHHHoanTraTienTrichThua_USD() == true) {
//			if (!gridGDTTHHHoanTraTienTrichThua_USD.isVisible()) {
//				gridGDTTHHHoanTraTienTrichThua_USD.setVisible(true);
//			}
		}
	
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
			footerGDRTMDuocTQT_USD.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDRTMDuocTQT_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDRTMDuocTQT_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
			interchangeGdrtmKhcnUSD = BigDecimal.ZERO;
			interchangeGdrtmKhdnUSD = BigDecimal.ZERO;
			try {
				List<PhiInterchange> interchangeList = phiInterchangeService.phiInterchangeGDRTMDuocTQT("USD",ngayAdv);
				for(PhiInterchange s : interchangeList) {
					switch(s.getCustType()) {
						case "I":
							interchangeGdrtmKhcnUSD = interchangeGdrtmKhcnUSD.add(s.getInterchange());
							break;
						case "C":
							interchangeGdrtmKhdnUSD = interchangeGdrtmKhdnUSD.add(s.getInterchange());
							break;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			footerGDRTMDuocTQTTyGiaTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Tỷ giá TQT" +"</div>");
			footerGDRTMDuocTQTTyGiaTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ tyGiaTQT +"</div>");
			
			footerGDRTMDuocTQTInterchangeKHCN_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHCN" +"</div>");
			footerGDRTMDuocTQTInterchangeKHCN_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdrtmKhcnUSD) +"</div>");
			footerGDRTMDuocTQTInterchangeKHCN_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdrtmKhcnUSD.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
			
			footerGDRTMDuocTQTInterchangeKHDN_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHDN" +"</div>");
			footerGDRTMDuocTQTInterchangeKHDN_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdrtmKhdnUSD) +"</div>");
			footerGDRTMDuocTQTInterchangeKHDN_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdrtmKhdnUSD.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
			
			footerGDRTMDuocTQTInterchangeTotal_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Tổng phí interchange GD RTM" +"</div>");
			footerGDRTMDuocTQTInterchangeTotal_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdrtmKhcnUSD.add(interchangeGdrtmKhdnUSD)) +"</div>");
			footerGDRTMDuocTQTInterchangeTotal_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdrtmKhcnUSD.add(interchangeGdrtmKhdnUSD).multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
		}
	
	}
	
	public void initGridGDRTMChiTietGDDuocTQT_USD(String curr) {
		dataSourceGDRTMChiTietGDDuocTQT_USD = getDataGDRTMChiTietGDDuocTQT(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMChiTietGDDuocTQT_USD() == true) {
//			if (!gridGDRTMChiTietGDDuocTQT_USD.isVisible()) {
//				gridGDRTMChiTietGDDuocTQT_USD.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDRTMTrichNoBoSungKH_USD(String curr) {
		dataSourceGDRTMTrichNoBoSungKH_USD = getDataGDRTMTrichNoBoSungKH(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMTrichNoBoSungKH_USD() == true) {
//			if (!gridGDRTMTrichNoBoSungKH_USD.isVisible()) {
//				gridGDRTMTrichNoBoSungKH_USD.setVisible(true);
//			}
		}
	
	}
	
	public void initGridGDRTMHoanTraTienTrichThua_USD(String curr) {
		dataSourceGDRTMHoanTraTienTrichThua_USD = getDataGDRTMHoanTraTienTrichThua(curr, new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerGDRTMHoanTraTienTrichThua_USD() == true) {
//			if (!gridGDRTMHoanTraTienTrichThua_USD.isVisible()) {
//				gridGDRTMHoanTraTienTrichThua_USD.setVisible(true);
//			}
		}
	
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
			footerGDMoneySendFF_USD.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerGDMoneySendFF_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceGDMoneySendFF_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
			interchangeGdmsffKhcnUSD = BigDecimal.ZERO;
			interchangeGdmsffKhdnUSD = BigDecimal.ZERO;
			try {
				List<PhiInterchange> interchangeList = phiInterchangeService.phiInterchangeGDMoneySendFF("USD",ngayAdv);
				for(PhiInterchange s : interchangeList) {
					switch(s.getCustType()) {
						case "I":
							interchangeGdmsffKhcnUSD = interchangeGdrtmKhcnUSD.add(s.getInterchange());
							break;
						case "C":
							interchangeGdmsffKhdnUSD = interchangeGdrtmKhdnUSD.add(s.getInterchange());
							break;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			footerGDMoneySendFFTyGiaTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Tỷ giá TQT" +"</div>");
			footerGDMoneySendFFTyGiaTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ tyGiaTQT +"</div>");
			
			footerGDMoneySendFFInterchangeKHCN_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHCN" +"</div>");
			footerGDMoneySendFFInterchangeKHCN_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdmsffKhcnUSD) +"</div>");
			footerGDMoneySendFFInterchangeKHCN_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdmsffKhcnUSD.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
			
			footerGDMoneySendFFInterchangeKHDN_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHDN" +"</div>");
			footerGDMoneySendFFInterchangeKHDN_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdmsffKhdnUSD) +"</div>");
			footerGDMoneySendFFInterchangeKHDN_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdmsffKhdnUSD.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
			
			footerGDMoneySendFFInterchangeTotal_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Tổng phí interchange GD RTM" +"</div>");
			footerGDMoneySendFFInterchangeTotal_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdmsffKhcnUSD.add(interchangeGdmsffKhdnUSD)) +"</div>");
			footerGDMoneySendFFInterchangeTotal_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeGdmsffKhcnUSD.add(interchangeGdmsffKhdnUSD).multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP))+"</div>");
		}
	
	}
	
	public void initGridHoanTraGDTTHH_USD(String curr) {
		dataSourceHoanTraGDTTHH_USD = getDataHoanTraGDTTHH(curr,new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerHoanTraGDTTHH_USD() == true) {
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			BigDecimal interchangeTotal = BigDecimal.valueOf(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue());
			BigDecimal stTqtTotal = BigDecimal.valueOf(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue());
			
			footerHoanTraGDTTHH_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tổng cộng</div>");
			footerHoanTraGDTTHH_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().setScale(0, RoundingMode.HALF_UP).doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDTTHH_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDTTHH_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
			footerHoanTraGDTTHHTyGiaTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tỷ giá TQT</div>");
			footerHoanTraGDTTHHTyGiaTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>" + tyGiaTQT + "</div>");
			
			footerHoanTraGDTTHHThuPhiGD_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Thu phí GD TTHH</div>");
			footerHoanTraGDTTHHThuPhiGD_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format(interchangeTotal) + "</div>");
			footerHoanTraGDTTHHThuPhiGD_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format(interchangeTotal.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) + "</div>");
			
			footerHoanTraGDTTHHTongGDDuocTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>Tổng GD TTHH được TQT</div>");
			footerHoanTraGDTTHHTongGDDuocTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format(stTqtTotal.subtract(interchangeTotal)) + "</div>");
			footerHoanTraGDTTHHTongGDDuocTQT_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>" + formatter.format((stTqtTotal.subtract(interchangeTotal)).multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) + "</div>");
		
		}
		
	}
	
	public void initGridHoanTraGDRTM_USD(String curr) {
		dataSourceHoanTraGDRTM_USD = getDataHoanTraGDRTM(curr,new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		if (createDataForContainerHoanTraGDRTM_USD() == true) {
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			footerHoanTraGDRTM_USD.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
			footerHoanTraGDRTM_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().setScale(0, RoundingMode.HALF_UP).doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
			footerHoanTraGDRTM_USD.getCell("phiXuLyGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSourceHoanTraGDRTM_USD.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
		
			interchangeHtgdrtmKhcnUSD = BigDecimal.ZERO;
			interchangeHtgdrtmKhdnUSD = BigDecimal.ZERO;
			try {
				List<PhiInterchange> interchangeList = phiInterchangeService.phiInterchangeHoanTraGDRTM("USD",ngayAdv);
				for(PhiInterchange s : interchangeList) {
					switch(s.getCustType()) {
						case "I":
							interchangeHtgdrtmKhcnUSD = interchangeHtgdrtmKhcnUSD.add(s.getInterchange());
							break;
						case "C":
							interchangeHtgdrtmKhdnUSD = interchangeHtgdrtmKhdnUSD.add(s.getInterchange());
							break;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			footerHoanTraGDRTMTyGiaTQT_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Tỷ giá TQT" +"</div>");
			footerHoanTraGDRTMTyGiaTQT_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ tyGiaTQT +"</div>");
			
			footerHoanTraGDRTMInterchangeKHCN_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHCN" +"</div>");
			footerHoanTraGDRTMInterchangeKHCN_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeHtgdrtmKhcnUSD) +"</div>");
			footerHoanTraGDRTMInterchangeKHCN_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeHtgdrtmKhcnUSD.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
			
			footerHoanTraGDRTMInterchangeKHDN_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Phí interchange GD RTM thẻ KHDN" +"</div>");
			footerHoanTraGDRTMInterchangeKHDN_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeHtgdrtmKhdnUSD) +"</div>");
			footerHoanTraGDRTMInterchangeKHDN_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeHtgdrtmKhdnUSD.multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP)) +"</div>");
			
			footerHoanTraGDRTMInterchangeTotal_USD.getCell("stGd").setHtml("<div align='right' style='font-weight:bold'>"+ "Tổng phí interchange GD RTM" +"</div>");
			footerHoanTraGDRTMInterchangeTotal_USD.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeHtgdrtmKhcnUSD.add(interchangeHtgdrtmKhdnUSD)) +"</div>");
			footerHoanTraGDRTMInterchangeTotal_USD.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+ formatter.format(interchangeHtgdrtmKhcnUSD.add(interchangeHtgdrtmKhdnUSD).multiply(tyGiaTQT).setScale(0, RoundingMode.HALF_UP))+"</div>");
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHChiTietGDDuocTQT() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHChiTietGDDuocTQT != null && dataSourceGDTTHHChiTietGDDuocTQT.getTotalElements()>0) {
			containerGDTTHHChiTietGDDuocTQT.removeAllItems();
			dataSourceGDTTHHChiTietGDDuocTQT.forEach(s -> {
				stt++;
				Item item = containerGDTTHHChiTietGDDuocTQT.getItem(containerGDTTHHChiTietGDDuocTQT.addItem());
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 1.3. Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHTrichNoBoSungKH() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHTrichNoBoSungKH != null && dataSourceGDTTHHTrichNoBoSungKH.getTotalElements()>0) {
			containerGDTTHHTrichNoBoSungKH.removeAllItems();
			dataSourceGDTTHHTrichNoBoSungKH.forEach(s -> {
				stt++;
				Item item = containerGDTTHHTrichNoBoSungKH.getItem(containerGDTTHHTrichNoBoSungKH.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0) {
					item.getItemProperty("soTienGdTruyThu").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaTruyThu").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaTruyThu").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatTruyThu").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongTruyThu").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}

				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 1.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch TTHH Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHHoanTraTienTrichThua() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHHoanTraTienTrichThua != null && dataSourceGDTTHHHoanTraTienTrichThua.getTotalElements()>0) {
			containerGDTTHHHoanTraTienTrichThua.removeAllItems();
			dataSourceGDTTHHHoanTraTienTrichThua.forEach(s -> {
				stt++;
				Item item = containerGDTTHHHoanTraTienTrichThua.getItem(containerGDTTHHHoanTraTienTrichThua.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)<0) {
					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaHoanTra").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaHoanTra").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatHoanTra").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongHoanTra").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}
				
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
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
					System.out.println(s.getStTrichNoKhGd());
					item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
					System.out.println(s.getStgdChenhLechDoTyGia());
					item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
					System.out.println(s.getStgdNguyenTeGd());
					item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
					System.out.println(s.getStgdNguyenTeChenhLech());
					item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
					item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
					item.getItemProperty("reversalInd").setValue(s.getReversalInd());
					item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
					item.getItemProperty("statusCW").setValue(s.getStatusCw());
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
	private boolean createDataForContainerGDRTMChiTietGDDuocTQT() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMChiTietGDDuocTQT != null && dataSourceGDRTMChiTietGDDuocTQT.getTotalElements()>0) {
			containerGDRTMChiTietGDDuocTQT.removeAllItems();
			dataSourceGDRTMChiTietGDDuocTQT.forEach(s -> {
				stt++;
				Item item = containerGDRTMChiTietGDDuocTQT.getItem(containerGDRTMChiTietGDDuocTQT.addItem());
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 2.3. Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDRTMTrichNoBoSungKH() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMTrichNoBoSungKH != null && dataSourceGDRTMTrichNoBoSungKH.getTotalElements()>0) {
			containerGDRTMTrichNoBoSungKH.removeAllItems();
			dataSourceGDRTMTrichNoBoSungKH.forEach(s -> {
				stt++;
				Item item = containerGDRTMTrichNoBoSungKH.getItem(containerGDRTMTrichNoBoSungKH.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0) {
					item.getItemProperty("soTienGdTruyThu").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaTruyThu").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaTruyThu").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatTruyThu").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongTruyThu").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}
				
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 2.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDRTMHoanTraTienTrichThua() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMHoanTraTienTrichThua != null && dataSourceGDRTMHoanTraTienTrichThua.getTotalElements()>0) {
			containerGDRTMHoanTraTienTrichThua.removeAllItems();
			dataSourceGDRTMHoanTraTienTrichThua.forEach(s -> {
				stt++;
				Item item = containerGDRTMHoanTraTienTrichThua.getItem(containerGDRTMHoanTraTienTrichThua.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0) {
					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaHoanTra").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaHoanTra").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatHoanTra").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongHoanTra").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	// 4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerHoanTraGDTTHH() {
		boolean flag = false;
		stt = 0;
		if (dataSourceHoanTraGDTTHH != null && dataSourceHoanTraGDTTHH.getTotalElements()>0) {
			containerHoanTraGDTTHH.removeAllItems();
			dataSourceHoanTraGDTTHH.forEach(s -> {
				stt++;
				Item item = containerHoanTraGDTTHH.getItem(containerHoanTraGDTTHH.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				item.getItemProperty("phiIsaGd").setValue(s.getPhiIsaGd());
//					item.getItemProperty("ghiChu").setValue(s.getGhiChu());
//					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
//					item.getItemProperty("phiHoanTraKH").setValue(s.getPhiHoanTraKh());
//					item.getItemProperty("thueHoanTraKH").setValue(s.getThueHoanTraKh());
//					item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getTongPhiThueHoanTraKh());
//					item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getTongHoanTraKh());
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("casa").setValue(s.getCasa());
				
				if(s.getStGd().compareTo(s.getStgdNguyenTeGd())>0) {
					item.getItemProperty("ghiChu").setValue("1P");
					item.getItemProperty("soTienGdHoanTra").setValue(s.getStQdVnd());
					item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStQdVnd());
				}
				else
				{
					if(s.getStGd().compareTo(s.getStgdNguyenTeGd())==0) {
						item.getItemProperty("ghiChu").setValue("TP");
						item.getItemProperty("soTienGdHoanTra").setValue(s.getStTrichNoKhGd());
						item.getItemProperty("phiHoanTraKH").setValue(s.getPhiIsaGd());
						item.getItemProperty("thueHoanTraKH").setValue(s.getPhiIsaGd().multiply(BigDecimal.valueOf(0.1)));
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getPhiIsaGd().multiply(BigDecimal.valueOf(1.1)));
						item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStTrichNoKhGd().add(s.getPhiIsaGd().multiply(BigDecimal.valueOf(1.1))));
					}
					else {
						item.getItemProperty("ghiChu").setValue("");
						item.getItemProperty("soTienGdHoanTra").setValue(BigDecimal.ZERO);
						item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongSoTienHoanTraKH").setValue(BigDecimal.ZERO);
					}
				}
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerHoanTraGDRTM() {
		boolean flag = false;
		stt = 0;
		if (dataSourceHoanTraGDRTM != null && dataSourceHoanTraGDRTM.getTotalElements()>0) {
			containerHoanTraGDRTM.removeAllItems();
			dataSourceHoanTraGDRTM.forEach(s -> {
				stt++;
				Item item = containerHoanTraGDRTM.getItem(containerHoanTraGDRTM.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				item.getItemProperty("phiIsaGd").setValue(s.getPhiIsaGd());
				item.getItemProperty("phiRtmGd").setValue(s.getPhiRtmGd());
//					item.getItemProperty("ghiChu").setValue(s.getGhiChu());
//					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
//					item.getItemProperty("phiHoanTraKH").setValue(s.getPhiHoanTraKh());
//					item.getItemProperty("thueHoanTraKH").setValue(s.getThueHoanTraKh());
//					item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getTongPhiThueHoanTraKh());
//					item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getTongHoanTraKh());
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("casa").setValue(s.getCasa());
				
				if(s.getStGd().compareTo(s.getStgdNguyenTeGd())>0) {
					item.getItemProperty("ghiChu").setValue("1P");
					item.getItemProperty("soTienGdHoanTra").setValue(s.getStQdVnd());
					item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStQdVnd());
				}
				else
				{
					if(s.getStGd().compareTo(s.getStgdNguyenTeGd())==0) {
						item.getItemProperty("ghiChu").setValue("TP");
						item.getItemProperty("soTienGdHoanTra").setValue(s.getStTrichNoKhGd());
						item.getItemProperty("phiHoanTraKH").setValue(s.getPhiRtmGd());
						item.getItemProperty("thueHoanTraKH").setValue(s.getPhiRtmGd().multiply(BigDecimal.valueOf(0.1)));
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getPhiRtmGd().multiply(BigDecimal.valueOf(1.1)));
						item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStTrichNoKhGd().add(s.getPhiRtmGd().multiply(BigDecimal.valueOf(1.1))));
					} else {
						item.getItemProperty("ghiChu").setValue("");
						item.getItemProperty("soTienGdHoanTra").setValue(s.getStTrichNoKhGd());
						item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongSoTienHoanTraKH").setValue(BigDecimal.ZERO);
					}
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHChiTietGDDuocTQT_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHChiTietGDDuocTQT_USD != null && dataSourceGDTTHHChiTietGDDuocTQT_USD.getTotalElements()>0) {
			containerGDTTHHChiTietGDDuocTQT_USD.removeAllItems();
			dataSourceGDTTHHChiTietGDDuocTQT_USD.forEach(s -> {
				stt++;
				Item item = containerGDTTHHChiTietGDDuocTQT_USD.getItem(containerGDTTHHChiTietGDDuocTQT_USD.addItem());
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 1.3. Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHTrichNoBoSungKH_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHTrichNoBoSungKH_USD != null && dataSourceGDTTHHTrichNoBoSungKH_USD.getTotalElements()>0) {
			containerGDTTHHTrichNoBoSungKH_USD.removeAllItems();
			dataSourceGDTTHHTrichNoBoSungKH_USD.forEach(s -> {
				stt++;
				Item item = containerGDTTHHTrichNoBoSungKH_USD.getItem(containerGDTTHHTrichNoBoSungKH_USD.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0) {
					item.getItemProperty("soTienGdTruyThu").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaTruyThu").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaTruyThu").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatTruyThu").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongTruyThu").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}

				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 1.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch TTHH Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDTTHHHoanTraTienTrichThua_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDTTHHHoanTraTienTrichThua_USD != null && dataSourceGDTTHHHoanTraTienTrichThua_USD.getTotalElements()>0) {
			containerGDTTHHHoanTraTienTrichThua_USD.removeAllItems();
			dataSourceGDTTHHHoanTraTienTrichThua_USD.forEach(s -> {
				stt++;
				Item item = containerGDTTHHHoanTraTienTrichThua_USD.getItem(containerGDTTHHHoanTraTienTrichThua_USD.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)<0) {
					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaHoanTra").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaHoanTra").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatHoanTra").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongHoanTra").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}
				
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
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
					System.out.println(s.getStTrichNoKhGd());
					item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
					System.out.println(s.getStgdChenhLechDoTyGia());
					item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
					System.out.println(s.getStgdNguyenTeGd());
					item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
					System.out.println(s.getStgdNguyenTeChenhLech());
					item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
					item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
					item.getItemProperty("reversalInd").setValue(s.getReversalInd());
					item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
					item.getItemProperty("statusCW").setValue(s.getStatusCw());
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
	private boolean createDataForContainerGDRTMChiTietGDDuocTQT_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMChiTietGDDuocTQT_USD != null && dataSourceGDRTMChiTietGDDuocTQT_USD.getTotalElements()>0) {
			containerGDRTMChiTietGDDuocTQT_USD.removeAllItems();
			dataSourceGDRTMChiTietGDDuocTQT_USD.forEach(s -> {
				stt++;
				Item item = containerGDRTMChiTietGDDuocTQT_USD.getItem(containerGDRTMChiTietGDDuocTQT_USD.addItem());
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 2.3. Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDRTMTrichNoBoSungKH_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMTrichNoBoSungKH_USD != null && dataSourceGDRTMTrichNoBoSungKH_USD.getTotalElements()>0) {
			containerGDRTMTrichNoBoSungKH_USD.removeAllItems();
			dataSourceGDRTMTrichNoBoSungKH_USD.forEach(s -> {
				stt++;
				Item item = containerGDRTMTrichNoBoSungKH_USD.getItem(containerGDRTMTrichNoBoSungKH_USD.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0) {
					item.getItemProperty("soTienGdTruyThu").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaTruyThu").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaTruyThu").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatTruyThu").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongTruyThu").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}
				
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//--- 2.4. Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerGDRTMHoanTraTienTrichThua_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceGDRTMHoanTraTienTrichThua_USD != null && dataSourceGDRTMHoanTraTienTrichThua_USD.getTotalElements()>0) {
			containerGDRTMHoanTraTienTrichThua_USD.removeAllItems();
			dataSourceGDRTMHoanTraTienTrichThua_USD.forEach(s -> {
				stt++;
				Item item = containerGDRTMHoanTraTienTrichThua_USD.getItem(containerGDRTMHoanTraTienTrichThua_USD.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				
				if(s.getStgdNguyenTeChenhLech().compareTo(BigDecimal.ZERO)>0) {
					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaHoanTra").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaHoanTra").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatHoanTra").setValue(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu()));
					item.getItemProperty("tongHoanTra").setValue(s.getSoTienGdHoanTraTruyThu().add(s.getPhiIsaHoanTraTruyThu().add(s.getVatPhiIsaHoanTraTruyThu())));
				}
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue("");
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
				System.out.println(s.getStTrichNoKhGd());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				System.out.println(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				System.out.println(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				System.out.println(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	// 4. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerHoanTraGDTTHH_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceHoanTraGDTTHH_USD != null && dataSourceHoanTraGDTTHH_USD.getTotalElements()>0) {
			containerHoanTraGDTTHH_USD.removeAllItems();
			dataSourceHoanTraGDTTHH_USD.forEach(s -> {
				stt++;
				Item item = containerHoanTraGDTTHH_USD.getItem(containerHoanTraGDTTHH_USD.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				item.getItemProperty("phiIsaGd").setValue(s.getPhiIsaGd());
//				item.getItemProperty("ghiChu").setValue(s.getGhiChu());
//				item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
//				item.getItemProperty("phiHoanTraKH").setValue(s.getPhiHoanTraKh());
//				item.getItemProperty("thueHoanTraKH").setValue(s.getThueHoanTraKh());
//				item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getTongPhiThueHoanTraKh());
//				item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getTongHoanTraKh());
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("casa").setValue(s.getCasa());
				
				if(s.getStGd().compareTo(s.getStgdNguyenTeGd())<0) {
					item.getItemProperty("ghiChu").setValue("1P");
					item.getItemProperty("soTienGdHoanTra").setValue(s.getStQdVnd());
					item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStQdVnd());
				}
				else
				{
					if(s.getStGd().compareTo(s.getStgdNguyenTeGd())==0) {
						item.getItemProperty("ghiChu").setValue("TP");
						item.getItemProperty("soTienGdHoanTra").setValue(s.getStTrichNoKhGd());
						item.getItemProperty("phiHoanTraKH").setValue(s.getPhiIsaGd());
						item.getItemProperty("thueHoanTraKH").setValue(s.getPhiIsaGd().multiply(BigDecimal.valueOf(0.1)));
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getPhiIsaGd().multiply(BigDecimal.valueOf(1.1)));
						item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStTrichNoKhGd().add(s.getPhiIsaGd().multiply(BigDecimal.valueOf(1.1))));
					}
					else {
						item.getItemProperty("ghiChu").setValue("");
						item.getItemProperty("soTienGdHoanTra").setValue(BigDecimal.ZERO);
						item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongSoTienHoanTraKH").setValue(BigDecimal.ZERO);
					}
				}
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
	@SuppressWarnings("unchecked")
	private boolean createDataForContainerHoanTraGDRTM_USD() {
		boolean flag = false;
		stt = 0;
		if (dataSourceHoanTraGDRTM_USD != null && dataSourceHoanTraGDRTM_USD.getTotalElements()>0) {
			containerHoanTraGDRTM_USD.removeAllItems();
			dataSourceHoanTraGDRTM_USD.forEach(s -> {
				stt++;
				Item item = containerHoanTraGDRTM_USD.getItem(containerHoanTraGDRTM_USD.addItem());
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
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				item.getItemProperty("phiIsaGd").setValue(s.getPhiIsaGd());
				item.getItemProperty("phiRtmGd").setValue(s.getPhiRtmGd());
//				item.getItemProperty("ghiChu").setValue(s.getGhiChu());
//				item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
//				item.getItemProperty("phiHoanTraKH").setValue(s.getPhiHoanTraKh());
//				item.getItemProperty("thueHoanTraKH").setValue(s.getThueHoanTraKh());
//				item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getTongPhiThueHoanTraKh());
//				item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getTongHoanTraKh());
				item.getItemProperty("tenChuTk").setValue(s.getTenChuTk());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("casa").setValue(s.getCasa());
				
				if(s.getStGd().compareTo(s.getStgdNguyenTeGd())<0) {
					item.getItemProperty("ghiChu").setValue("1P");
					item.getItemProperty("soTienGdHoanTra").setValue(s.getStQdVnd());
					item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
					item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStQdVnd());
				}
				else
				{
					if(s.getStGd().compareTo(s.getStgdNguyenTeGd())==0) {
						item.getItemProperty("ghiChu").setValue("TP");
						item.getItemProperty("soTienGdHoanTra").setValue(s.getStTrichNoKhGd());
						item.getItemProperty("phiHoanTraKH").setValue(s.getPhiRtmGd());
						item.getItemProperty("thueHoanTraKH").setValue(s.getPhiRtmGd().multiply(BigDecimal.valueOf(0.1)));
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(s.getPhiRtmGd().multiply(BigDecimal.valueOf(1.1)));
						item.getItemProperty("tongSoTienHoanTraKH").setValue(s.getStTrichNoKhGd().add(s.getPhiRtmGd().multiply(BigDecimal.valueOf(1.1))));
					} else {
						item.getItemProperty("ghiChu").setValue("");
						item.getItemProperty("soTienGdHoanTra").setValue(s.getStTrichNoKhGd());
						item.getItemProperty("phiHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("thueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongPhiThueHoanTraKH").setValue(BigDecimal.ZERO);
						item.getItemProperty("tongSoTienHoanTraKH").setValue(BigDecimal.ZERO);
					}
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
		parameters.put("p_ngayadv", ngayAdv);
		parameters.put("p_loaitien", loaitienTqt);
		parameters.put("p_cardbrn", cardType);

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
	public StreamResource createTransMKResourceXLSDoiChieuTqtVnd() {
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
		}, "DoiChieuTQT_VND.xlsx");
	}
	
	@SuppressWarnings("serial")
	public StreamResource createTransMKResourceXLSDoiChieuTqtUsd() {
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
		}, "DoiChieuTQT_USD.xlsx");
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
}
