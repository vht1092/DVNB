package com.dvnb.components;

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
import com.dvnb.entities.DsqtFeeDaily;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.DsqtFeeDailyService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.dvnb.services.TyGiaTqtService;
import com.monitorjbl.xlsx.StreamingReader;
import com.sun.javafx.print.Units;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.validator.NullValidator;
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
public class CapNhatPhiInterchange extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CapNhatPhiInterchange.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "CẬP NHẬT PHÍ INTERCHANGE HÀNG NGÀY";
	private static final String CARD_TYPE = "LOẠI THẺ";
	private static final String LOAI_GD = "LOẠI GD";
	private static final String LOAI_TIEN = "LOẠI TIỀN TQT";
	private static final String SAVE = "SAVE";
	private static final String DOI_CHIEU = "ĐỐI CHIẾU";
	private static final String INTERCHANGE_DUOC_HUONG = "PHÍ INTERCHANGE ĐƯỢC HƯỞNG/PHẢI TRẢ";
	private static final String INTERCHANGE_DUOC_HOAN_TRA = "PHÍ INTERCHANGE ĐƯỢC HOÀN TRẢ";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String TY_GIA_TQT = "TỶ GIÁ TQT";
	private static final String TONG_VND = "TỔNG (VND)";
	private static final String PHI_XU_LY_GD = "PHÍ XỬ LÝ GD SCB PHẢI TRẢ";
	
	private transient String sUserId;
	private final ComboBox cbbCardType;
	private final ComboBox cbbLoaiTienTQT;
	private final ComboBox cbbLoaiGD;
	private DateField dfNgayIncomingFile;
	private DateField dfNgayADV;
	private DateField dfNgayHachToan;
	private final TextField tfInterchangeDuocHuong;
	private final TextField tfInterchangeDuocHoanTra;
	private final TextField tfTyGiaTQT;
	private final TextField tfTongVNDInterchange;
	private final TextField tfTongVNDPhiGD;
	private final TextField tfPhiXuLyGD;
	
	final Button btSave = new Button(SAVE);
	private TyGiaTqt tygia = new TyGiaTqt();
	private BigDecimal totalStQdVnd;
	private final transient DoiSoatDataService doiSoatDataService;
	private final transient TyGiaTqtService tyGiaTqtService;
	private final transient DsqtFeeDailyService dsqtFeeDailyService;
	
	final TimeConverter timeConverter = new TimeConverter();
	
	
	BigDecimal interchangeDuocHuongPhaiTra = BigDecimal.ZERO;
	BigDecimal interchangeDuocHoanTra = BigDecimal.ZERO;
	BigDecimal phiXuLyGD = BigDecimal.ZERO;
	
	public CapNhatPhiInterchange() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		formLayout2nd.setMargin(new MarginInfo(false, false, true, false));
		final HorizontalLayout formLayout3rd = new HorizontalLayout();
		formLayout3rd.setSpacing(true);
		final HorizontalLayout formLayout4th = new HorizontalLayout();
		formLayout4th.setSpacing(true);
		formLayout4th.setMargin(new MarginInfo(false, false, true, false));
		final HorizontalLayout formLayout5th = new HorizontalLayout();
		formLayout5th.setSpacing(true);
		formLayout5th.setMargin(new MarginInfo(true, false, false, false));
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		tyGiaTqtService = (TyGiaTqtService) helper.getBean("tyGiaTqtService");
		dsqtFeeDailyService = (DsqtFeeDailyService) helper.getBean("dsqtFeeDailyService");
		this.sUserId = SecurityUtils.getUserId();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, true, false, false));
		
		final Label lbCardType = new Label(CARD_TYPE);
		cbbCardType = new ComboBox();
		cbbCardType.setWidth(100f, Unit.PIXELS);
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD","VS","VSD");
		
		final Label lbNgayIncomingFile = new Label("NGÀY INCOMING FILE");
		dfNgayIncomingFile = new DateField();
		dfNgayIncomingFile.setWidth(120f, Unit.PIXELS);
		dfNgayIncomingFile.setDateFormat("dd/MM/yyyy");
		dfNgayIncomingFile.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayIncomingFile.setValidationVisible(false);
		
		final Label lbNgayADV = new Label("NGÀY ADV");
		dfNgayADV = new DateField();
		dfNgayADV.setWidth(120f, Unit.PIXELS);
		dfNgayADV.setDateFormat("dd/MM/yyyy");
		dfNgayADV.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayADV.setValidationVisible(false);
		
		final Label lbNgayHachToan = new Label("NGÀY HẠCH TOÁN");
		dfNgayHachToan = new DateField();
		dfNgayHachToan.setWidth(120f, Unit.PIXELS);
		dfNgayHachToan.setDateFormat("dd/MM/yyyy");
		dfNgayHachToan.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayHachToan.setValidationVisible(false);
		
		final Label lbLoaiGD = new Label(LOAI_GD);
		lbLoaiGD.setWidth(56.05f, Unit.PIXELS);
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setWidth(100f, Unit.PIXELS);
		cbbLoaiGD.setNullSelectionAllowed(false);
		cbbLoaiGD.addItems("RTM","TTHH","MSFF");
		cbbLoaiGD.setItemCaption("MSFF","MoneySend/FastFund");
		
		
		final Label lbLoaiTienTQT = new Label(LOAI_TIEN);
		lbLoaiTienTQT.setWidth(128.92f, Unit.PIXELS);
		cbbLoaiTienTQT = new ComboBox();
		cbbLoaiTienTQT.setWidth(120f,Unit.PIXELS);
		cbbLoaiTienTQT.setNullSelectionAllowed(false);
		cbbLoaiTienTQT.addItems("VND","USD");

		final Label lbInterchangeDuocHuong = new Label(INTERCHANGE_DUOC_HUONG);
		lbInterchangeDuocHuong.setWidth(150f,Unit.PIXELS);
		tfInterchangeDuocHuong = new TextField();
		tfInterchangeDuocHuong.setWidth(110f, Unit.PIXELS);
		tfInterchangeDuocHuong.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbInterchangeDuocHoanTra = new Label(INTERCHANGE_DUOC_HOAN_TRA);
		lbInterchangeDuocHoanTra.setWidth(120f,Unit.PIXELS);
		tfInterchangeDuocHoanTra = new TextField();
		tfInterchangeDuocHoanTra.setWidth(110f, Unit.PIXELS);
		tfInterchangeDuocHoanTra.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbTyGiaTQT = new Label(TY_GIA_TQT);
		tfTyGiaTQT = new TextField();
		tfTyGiaTQT.setWidth(110f, Unit.PIXELS);
		tfTyGiaTQT.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbTongVND1 = new Label(TONG_VND);
		tfTongVNDInterchange = new TextField();
		tfTongVNDInterchange.setWidth(110f, Unit.PIXELS);
		tfTongVNDInterchange.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbPhiXuLyGD = new Label(PHI_XU_LY_GD);
		lbPhiXuLyGD.setWidth(85f,Unit.PIXELS);
		tfPhiXuLyGD = new TextField();
		tfPhiXuLyGD.setWidth(110f, Unit.PIXELS);
		tfPhiXuLyGD.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbTongVND2 = new Label(TONG_VND);
		tfTongVNDPhiGD = new TextField();
		tfTongVNDPhiGD.setWidth(110f, Unit.PIXELS);
		tfTongVNDPhiGD.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		dfNgayADV.addValueChangeListener(event -> {
			if(!dfNgayADV.isEmpty() && !cbbLoaiTienTQT.isEmpty() && !cbbCardType.isEmpty()) {
				String advDate = timeConverter.convertDatetime(dfNgayADV.getValue());
				String cardtype = cbbCardType.getValue().toString();
				totalStQdVnd = doiSoatDataService.totalStQdVnd(advDate,cardtype);
				if(totalStQdVnd==null) {
					Notification.show("Lỗi","Ngày này không có dữ liệu ADV. Vui lòng chọn đúng ngày ADV", Type.WARNING_MESSAGE);
					return;
				}
				tygia = tyGiaTqtService.findTyGiaTqtByNgayAdv(advDate);
				if(tygia!=null) {
					if(cbbLoaiTienTQT.getValue().equals("VND"))
						tfTyGiaTQT.setValue("1");
					else
						tfTyGiaTQT.setValue(tygia.getTyGiaTqt().toString());
					
				} else {
					tfTyGiaTQT.setValue("0");
				}
				
				if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && !tfTyGiaTQT.isEmpty()) {
					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tyGiaTQT).subtract(interchangeDuocHoanTra.multiply(tyGiaTQT));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
				}
				
				if(!tfPhiXuLyGD.isEmpty() && !tfTyGiaTQT.isEmpty()) {
					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tyGiaTQT);
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
				}
			}
			
		});
		
		cbbLoaiTienTQT.addValueChangeListener(event -> {
			if(!dfNgayADV.isEmpty() && !cbbLoaiTienTQT.isEmpty() && !cbbCardType.isEmpty()) {
				String advDate = timeConverter.convertDatetime(dfNgayADV.getValue());
				String cardtype = cbbCardType.getValue().toString();
				totalStQdVnd = doiSoatDataService.totalStQdVnd(advDate,cardtype);
				tygia = tyGiaTqtService.findTyGiaTqtByNgayAdv(advDate);
				if(tygia!=null) {
					if(cbbLoaiTienTQT.getValue().equals("VND"))
						tfTyGiaTQT.setValue("1");
					else
						tfTyGiaTQT.setValue(tygia.getTyGiaTqt().toString());
				} else {
					tfTyGiaTQT.setValue("0");
				}
				
				if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && !tfTyGiaTQT.isEmpty()) {
					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tyGiaTQT).subtract(interchangeDuocHoanTra.multiply(tyGiaTQT));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
				}
				
				if(!tfPhiXuLyGD.isEmpty() && !tfTyGiaTQT.isEmpty()) {
					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tyGiaTQT);
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
				}
			}
		});
		
		tfInterchangeDuocHuong.addValueChangeListener(event -> {
			interchangeDuocHuongPhaiTra = tfInterchangeDuocHuong.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfInterchangeDuocHuong.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
			if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && !tfTyGiaTQT.isEmpty()) {
				BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tyGiaTQT).subtract(interchangeDuocHoanTra.multiply(tyGiaTQT));
				tfTongVNDInterchange.setValue(tongVnd1.toString());
			}
		});
		
		tfInterchangeDuocHoanTra.addValueChangeListener(event -> {
			interchangeDuocHoanTra = tfInterchangeDuocHoanTra.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfInterchangeDuocHoanTra.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
			if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && !tfTyGiaTQT.isEmpty()) {
				BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tyGiaTQT).subtract(interchangeDuocHoanTra.multiply(tyGiaTQT));
				tfTongVNDInterchange.setValue(tongVnd1.toString());
			}
		});
		
		tfTyGiaTQT.addValueChangeListener(event -> {
			if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && !tfTyGiaTQT.isEmpty()) {
				BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tyGiaTQT).subtract(interchangeDuocHoanTra.multiply(tyGiaTQT));
				tfTongVNDInterchange.setValue(tongVnd1.toString());
			}
			if(!tfPhiXuLyGD.isEmpty() && !tfTyGiaTQT.isEmpty()) {
				BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				BigDecimal tongVnd2 = phiXuLyGD.multiply(tyGiaTQT);
				tfTongVNDPhiGD.setValue(tongVnd2.toString());
			}
		});
		
		tfPhiXuLyGD.addValueChangeListener(event -> {
			phiXuLyGD = tfInterchangeDuocHoanTra.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfInterchangeDuocHoanTra.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
			if(!tfPhiXuLyGD.isEmpty() && !tfTyGiaTQT.isEmpty()) {
				BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				BigDecimal tongVnd2 = phiXuLyGD.multiply(tyGiaTQT);
				tfTongVNDPhiGD.setValue(tongVnd2.toString());
			}
		});
		
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setWidth(120.0f, Unit.PIXELS);
		btSave.setIcon(FontAwesome.EYE);
		btSave.addClickListener(event -> {
			try {
				DsqtFeeDaily fd = new DsqtFeeDaily();
				fd.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
				fd.setUsrId(sUserId);
				fd.setLoaiThe(cbbCardType.getValue().toString());
				String ngayIncomingFile = timeConverter.convertDatetime(dfNgayIncomingFile.getValue());
				fd.setNgayIncomingFile(ngayIncomingFile);
				String ngayAdv = timeConverter.convertDatetime(dfNgayADV.getValue());
				fd.setNgayAdv(ngayAdv);
				String ngayHachToan = timeConverter.convertDatetime(dfNgayHachToan.getValue());
				fd.setNgayHachToan(ngayHachToan);
				fd.setLoaiGd(cbbLoaiGD.getValue().toString());
				fd.setLoaiTienTqt(cbbLoaiTienTQT.getValue().toString());
				fd.setId(fd.getLoaiThe()+fd.getNgayIncomingFile()+fd.getNgayAdv()+fd.getNgayHachToan()+fd.getLoaiGd()+fd.getLoaiTienTqt());
				BigDecimal tyGiaTqt = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				fd.setTyGiaTqt(tyGiaTqt);
				fd.setInterchangeDuocHuongPhaiTra(interchangeDuocHuongPhaiTra);
				fd.setInterchangeDuocHoanTra(interchangeDuocHoanTra);
				fd.setPhiXuLyGd(phiXuLyGD);
				BigDecimal tongInterchange = new BigDecimal(tfTongVNDInterchange.getValue().replace(",", ""));
				fd.setTongInterchange(tongInterchange);
				BigDecimal tongPhiGd = new BigDecimal(tfTongVNDPhiGD.getValue().replace(",", ""));
				fd.setTongPhiGd(tongPhiGd);
				dsqtFeeDailyService.save(fd);
				Notification.show("Thông báo","Dữ liệu thêm mới thành công.", Type.WARNING_MESSAGE);
			} catch (Exception e) {
				// TODO: handle exception
				Notification.show("Lỗi",e.getCause().toString(), Type.ERROR_MESSAGE);
			}
			
		});
		
		formLayout1st.addComponent(lbCardType);
		formLayout1st.addComponent(cbbCardType);
		formLayout1st.addComponent(lbNgayIncomingFile);
		formLayout1st.addComponent(dfNgayIncomingFile);
		formLayout1st.addComponent(lbNgayADV);
		formLayout1st.addComponent(dfNgayADV);
		formLayout1st.addComponent(lbNgayHachToan);
		formLayout1st.addComponent(dfNgayHachToan);
		
		formLayout2nd.addComponent(lbLoaiGD);
		formLayout2nd.addComponent(cbbLoaiGD);
		formLayout2nd.addComponent(lbLoaiTienTQT);
		formLayout2nd.addComponent(cbbLoaiTienTQT);
		
		formLayout3rd.addComponent(lbInterchangeDuocHuong);
		formLayout3rd.setExpandRatio(lbInterchangeDuocHuong,1);
		formLayout3rd.addComponent(tfInterchangeDuocHuong);
		formLayout3rd.addComponent(lbInterchangeDuocHoanTra);
		formLayout3rd.setExpandRatio(lbInterchangeDuocHoanTra,1);
		formLayout3rd.addComponent(tfInterchangeDuocHoanTra);
		formLayout3rd.addComponent(lbTyGiaTQT);
		formLayout3rd.addComponent(tfTyGiaTQT);
		formLayout3rd.addComponent(lbTongVND1);
		formLayout3rd.addComponent(tfTongVNDInterchange);
		
		formLayout4th.addComponent(lbPhiXuLyGD);
		formLayout4th.setExpandRatio(lbPhiXuLyGD,1);
		formLayout4th.addComponent(tfPhiXuLyGD);
		formLayout4th.addComponent(lbTongVND2);
		formLayout4th.addComponent(tfTongVNDPhiGD);
		
		formLayout5th.addComponent(btSave);
		
		formLayout1st.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbNgayIncomingFile, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dfNgayIncomingFile, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbNgayADV, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dfNgayADV, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbNgayHachToan, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dfNgayHachToan, Alignment.MIDDLE_LEFT);
		
		formLayout2nd.setComponentAlignment(lbLoaiGD, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbLoaiGD, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbLoaiTienTQT, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbLoaiTienTQT, Alignment.MIDDLE_LEFT);
		
		formLayout3rd.setComponentAlignment(lbInterchangeDuocHuong, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(tfInterchangeDuocHuong, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(lbInterchangeDuocHoanTra, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(tfInterchangeDuocHoanTra, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(lbTyGiaTQT, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(tfTyGiaTQT, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(lbTongVND1, Alignment.MIDDLE_LEFT);
		formLayout3rd.setComponentAlignment(tfTongVNDInterchange, Alignment.MIDDLE_LEFT);
		
		formLayout4th.setComponentAlignment(lbPhiXuLyGD, Alignment.MIDDLE_RIGHT);
		formLayout4th.setComponentAlignment(tfPhiXuLyGD, Alignment.MIDDLE_RIGHT);
		formLayout4th.setComponentAlignment(lbTongVND2, Alignment.MIDDLE_RIGHT);
		formLayout4th.setComponentAlignment(tfTongVNDPhiGD, Alignment.MIDDLE_RIGHT);
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.addComponent(formLayout3rd);
		formLayout.addComponent(formLayout4th);
		formLayout.addComponent(formLayout5th);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_RIGHT);
		formLayout.setComponentAlignment(formLayout4th, Alignment.MIDDLE_RIGHT);
		formLayout.setComponentAlignment(formLayout5th, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	
	@Override
	public void eventReload() {
	}
	

}
