package com.dvnb.components.dstqt;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DsqtFeeDaily;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.DsqtFeeDailyService;
import com.dvnb.services.TyGiaTqtService;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class CapNhatPhiInterchange extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CapNhatPhiInterchange.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "CẬP NHẬT PHÍ INTERCHANGE HÀNG NGÀY";
	private static final String CARD_TYPE = "LOẠI THẺ *";
	private static final String LOAI_GD = "LOẠI GD *";
	private static final String LOAI_TIEN = "LOẠI TIỀN TQT *";
	private static final String SAVE = "SAVE";
	private static final String DOI_CHIEU = "ĐỐI CHIẾU";
	private static final String INTERCHANGE_DUOC_HUONG = "PHÍ INTERCHANGE ĐƯỢC HƯỞNG/PHẢI TRẢ";
	private static final String INTERCHANGE_DUOC_HOAN_TRA = "PHÍ INTERCHANGE ĐƯỢC HOÀN TRẢ";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String TY_GIA_TQT = "TỶ GIÁ TQT";
	private static final String TONG_VND = "TỔNG (VND)";
	private static final String PHI_XU_LY_GD = "PHÍ XỬ LÝ GD";
	private static final String PHI_XU_LY_GD_HOAN_TRA = "PHÍ XỬ LÝ GD ĐƯỢC HOÀN TRẢ";
	
	private transient String sUserId;
	private final ComboBox cbbCardType;
	private final ComboBox cbbLoaiTienTQT;
	private final ComboBox cbbLoaiGD;
	private DateField dfNgayIncomingFile;
	private DateField dfNgayADV;
	private DateField dfNgayHachToan;
	private final TextField tfInterchangeDuocHuong;
	private final TextField tfInterchangeDuocHoanTra;
//	private final TextField tfTyGiaTQT;
	private final TextField tfTongVNDInterchange;
	private final TextField tfTongVNDPhiGD;
	private final TextField tfPhiXuLyGD;
	private final TextField tfPhiXuLyGDDuocHoanTra;
	
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
	BigDecimal phiXuLyGDDuocHoanTra = BigDecimal.ZERO;
	
	BigDecimal tygiaInterchange = BigDecimal.ZERO.setScale(5);
	BigDecimal tygiaInterchangeDuocHoanTra= BigDecimal.ZERO.setScale(5);
	BigDecimal tygiaPhiXuLyGd = BigDecimal.ZERO.setScale(5);
	BigDecimal tygiaPhiXuLyGdDuocHoanTra= BigDecimal.ZERO.setScale(5);
	
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
		lbCardType.setWidth(68f, Unit.PIXELS);
		cbbCardType = new ComboBox();
		cbbCardType.setWidth(100f, Unit.PIXELS);
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD","VS","VSD");
		cbbCardType.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbCardType.setValidationVisible(false);
		
		final Label lbNgayIncomingFile = new Label("NGÀY INCOMING FILE");
		dfNgayIncomingFile = new DateField();
		dfNgayIncomingFile.setWidth(120f, Unit.PIXELS);
		dfNgayIncomingFile.setDateFormat("dd/MM/yyyy");
		dfNgayIncomingFile.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayIncomingFile.setValidationVisible(false);
		
		final Label lbNgayADV = new Label("NGÀY ADV *");
		dfNgayADV = new DateField();
		dfNgayADV.setWidth(120f, Unit.PIXELS);
		dfNgayADV.setDateFormat("dd/MM/yyyy");
		dfNgayADV.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayADV.setValidationVisible(false);
		
		final Label lbNgayHachToan = new Label("NGÀY HẠCH TOÁN *");
		dfNgayHachToan = new DateField();
		dfNgayHachToan.setWidth(120f, Unit.PIXELS);
		dfNgayHachToan.setDateFormat("dd/MM/yyyy");
		dfNgayHachToan.addValidator(new NullValidator(INPUT_FIELD, false));
		dfNgayHachToan.setValidationVisible(false);
		
		final Label lbLoaiGD = new Label(LOAI_GD);
		lbLoaiGD.setWidth(68f, Unit.PIXELS);
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setWidth(100f, Unit.PIXELS);
		cbbLoaiGD.setNullSelectionAllowed(false);
		cbbLoaiGD.addItems("RTM","TTHH","MSFF");
		cbbLoaiGD.setItemCaption("MSFF","MoneySend/FastFund");
		cbbLoaiGD.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbLoaiGD.setValidationVisible(false);
		
		final Label lbLoaiTienTQT = new Label(LOAI_TIEN);
		lbLoaiTienTQT.setWidth(128.92f, Unit.PIXELS);
		cbbLoaiTienTQT = new ComboBox();
		cbbLoaiTienTQT.setWidth(120f,Unit.PIXELS);
		cbbLoaiTienTQT.setNullSelectionAllowed(false);
		cbbLoaiTienTQT.addItems("VND","USD");
		cbbLoaiTienTQT.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbLoaiTienTQT.setValidationVisible(false);
		
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
		
//		final Label lbTyGiaTQT = new Label(TY_GIA_TQT);
//		tfTyGiaTQT = new TextField();
//		tfTyGiaTQT.setWidth(110f, Unit.PIXELS);
//		tfTyGiaTQT.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbTongVND1 = new Label(TONG_VND);
		tfTongVNDInterchange = new TextField();
		tfTongVNDInterchange.setWidth(110f, Unit.PIXELS);
		tfTongVNDInterchange.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbPhiXuLyGD = new Label(PHI_XU_LY_GD);
		lbPhiXuLyGD.setWidth(150f,Unit.PIXELS);
		tfPhiXuLyGD = new TextField();
		tfPhiXuLyGD.setWidth(110f, Unit.PIXELS);
		tfPhiXuLyGD.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbPhiXuLyGDDuocHoanTra = new Label(PHI_XU_LY_GD_HOAN_TRA);
		lbPhiXuLyGDDuocHoanTra.setWidth(120f,Unit.PIXELS);
		tfPhiXuLyGDDuocHoanTra = new TextField();
		tfPhiXuLyGDDuocHoanTra.setWidth(110f, Unit.PIXELS);
		tfPhiXuLyGDDuocHoanTra.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		final Label lbTongVND2 = new Label(TONG_VND);
		tfTongVNDPhiGD = new TextField();
		tfTongVNDPhiGD.setWidth(110f, Unit.PIXELS);
		tfTongVNDPhiGD.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		cbbCardType.addValueChangeListener(event -> {
			initData();
			calcTyGia();
			
			if(cbbCardType.getValue().toString().startsWith("V")) {
				formLayout4th.setVisible(true);
//				lbTyGiaTQT.setVisible(false);
//				tfTyGiaTQT.setVisible(false);
			} else {
				formLayout4th.setVisible(false);
//				lbTyGiaTQT.setVisible(false);
//				tfTyGiaTQT.setVisible(false);
//				tfInterchangeDuocHuong.setCaption(null);
//				tfInterchangeDuocHoanTra.setCaption(null);
			}
			
		});
		
		dfNgayADV.addValueChangeListener(event -> {
			initData();
			
			calcTyGia();
			
		});
		
		cbbLoaiTienTQT.addValueChangeListener(event -> {
			initData();
			calcTyGia();

		});
		
		cbbLoaiGD.addValueChangeListener(event -> {
			initData();
			calcTyGia();
		});
		
		tfInterchangeDuocHuong.addValueChangeListener(event -> {
			interchangeDuocHuongPhaiTra = tfInterchangeDuocHuong.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfInterchangeDuocHuong.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
			
			if(cbbCardType.getValue()==null || dfNgayADV.getValue()==null || cbbLoaiGD.getValue()==null || cbbLoaiTienTQT.getValue()==null) {
				return;
			}
			
			if(cbbCardType.getValue().toString().startsWith("M")) {
//				if(//!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && 
//				!tfTyGiaTQT.isEmpty()) {
//					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).add(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
//				}
			} else {
				interchangeDuocHuongPhaiTra = tfInterchangeDuocHuong.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfInterchangeDuocHuong.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
//					if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty()) {
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).add(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
//					}
			}
			
		});
		
		tfInterchangeDuocHoanTra.addValueChangeListener(event -> {
			interchangeDuocHoanTra = tfInterchangeDuocHoanTra.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfInterchangeDuocHoanTra.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
			
			if(cbbCardType.getValue()==null || dfNgayADV.getValue()==null || cbbLoaiGD.getValue()==null || cbbLoaiTienTQT.getValue()==null) {
				return;
			}
			
			if(cbbCardType.getValue().toString().startsWith("M")) {
//				if(//!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && 
//				!tfTyGiaTQT.isEmpty()) {
//					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).add(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
//				}
			} else {
//					if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty()) {
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).add(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
//					}
			}
			
		});
		
//		tfTyGiaTQT.addValueChangeListener(event -> {
//			if(cbbCardType.getValue()==null || dfNgayADV.getValue()==null || cbbLoaiGD.getValue()==null || cbbLoaiTienTQT.getValue()==null) {
//				return;
//			}
//			
//			if(cbbCardType.getValue().toString().startsWith("M")) {
////				BigDecimal tyGiaTQT = tfTyGiaTQT.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
//				if(//!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty() && 
//				!tfTyGiaTQT.isEmpty()) {
//					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).subtract(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
//					tfTongVNDInterchange.setValue(tongVnd1.toString());
//				}
//				if(!tfPhiXuLyGD.isEmpty() && !tfTyGiaTQT.isEmpty()) {
//					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP);
//					tfTongVNDPhiGD.setValue(tongVnd2.toString());
//				}
//			}
//		
//			
//		});
		
		tfPhiXuLyGD.addValueChangeListener(event -> {
			phiXuLyGD = tfPhiXuLyGD.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfPhiXuLyGD.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));

			if(cbbCardType.getValue()==null || dfNgayADV.getValue()==null || cbbLoaiGD.getValue()==null || cbbLoaiTienTQT.getValue()==null) {
				return;
			}
			
			if(cbbCardType.getValue().toString().startsWith("M")) {
//				if(!tfTyGiaTQT.isEmpty()) {
//					BigDecimal tyGiaTQT = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaPhiXuLyGd).setScale(0, RoundingMode.HALF_UP);
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
//				}
			} else {
//					if(!tfPhiXuLyGD.isEmpty()) {
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaPhiXuLyGd).setScale(0, RoundingMode.HALF_UP).add(phiXuLyGDDuocHoanTra.multiply(tygiaPhiXuLyGdDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
//					}
			}
			
		});
		
		tfPhiXuLyGDDuocHoanTra.addValueChangeListener(event -> {
			phiXuLyGDDuocHoanTra = tfPhiXuLyGDDuocHoanTra.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfPhiXuLyGDDuocHoanTra.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));

			if(cbbCardType.getValue()==null || dfNgayADV.getValue()==null || cbbLoaiGD.getValue()==null || cbbLoaiTienTQT.getValue()==null) {
				return;
			}
			
			if(cbbCardType.getValue().toString().startsWith("M")) {
//					if(!tfPhiXuLyGDDuocHoanTra.isEmpty()) {
//					BigDecimal tyGiaTQT = tfTyGiaTQT.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP);
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
//					}
			} else {
//					if(!tfPhiXuLyGDDuocHoanTra.isEmpty()) {
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaPhiXuLyGd).setScale(0, RoundingMode.HALF_UP).add(phiXuLyGDDuocHoanTra.multiply(tygiaPhiXuLyGdDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
//					}
			}
			
		});
		
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setWidth(120.0f, Unit.PIXELS);
		btSave.setIcon(FontAwesome.EYE);
		btSave.addClickListener(event -> {
			try {
				if(!checkValidator()) {
					return;
				}
				
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
				fd.setId(fd.getLoaiThe()+fd.getNgayAdv()+fd.getLoaiGd()+fd.getLoaiTienTqt());
//				BigDecimal tyGiaTqt = new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
				fd.setTyGiaTqt(BigDecimal.ZERO);
				fd.setInterchangeDuocHuongPhaiTra(interchangeDuocHuongPhaiTra);
				fd.setInterchangeDuocHoanTra(interchangeDuocHoanTra);
				fd.setPhiXuLyGd(phiXuLyGD);
				fd.setPhiXuLyGdDuocHoanTra(phiXuLyGDDuocHoanTra);
				BigDecimal tongInterchange = tfTongVNDInterchange.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTongVNDInterchange.getValue().replace(",", ""));
				fd.setTongInterchange(tongInterchange);
				BigDecimal tongPhiGd = tfTongVNDPhiGD.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTongVNDPhiGD.getValue().replace(",", ""));
				fd.setTongPhiGd(tongPhiGd);
				dsqtFeeDailyService.save(fd);
				Notification.show("Thông báo","Dữ liệu thêm mới thành công.", Type.WARNING_MESSAGE);
			} catch (Exception e) {
				// TODO: handle exception
				Notification.show("Lỗi",e.getCause().toString(), Type.ERROR_MESSAGE);
				e.printStackTrace();
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
//		formLayout3rd.addComponent(lbTyGiaTQT);
//		formLayout3rd.addComponent(tfTyGiaTQT);
		formLayout3rd.addComponent(lbTongVND1);
		formLayout3rd.addComponent(tfTongVNDInterchange);
		
		formLayout4th.addComponent(lbPhiXuLyGD);
		formLayout4th.setExpandRatio(lbPhiXuLyGD,1);
		formLayout4th.addComponent(tfPhiXuLyGD);
		formLayout4th.addComponent(lbPhiXuLyGDDuocHoanTra);
		formLayout4th.addComponent(tfPhiXuLyGDDuocHoanTra);
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
//		formLayout3rd.setComponentAlignment(lbTyGiaTQT, Alignment.MIDDLE_LEFT);
//		formLayout3rd.setComponentAlignment(tfTyGiaTQT, Alignment.MIDDLE_LEFT);
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
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout4th, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout5th, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	
	@Override
	public void eventReload() {
	}
	
	
	private void calcTyGia() {
		if(!dfNgayADV.isEmpty() && !cbbLoaiTienTQT.isEmpty() && !cbbCardType.isEmpty()) {
			String advDate = timeConverter.convertDatetime(dfNgayADV.getValue());
			String cardtype = cbbCardType.getValue().toString();
			totalStQdVnd = doiSoatDataService.totalStQdVnd(advDate,cardtype);
			if(totalStQdVnd==null && cbbLoaiTienTQT.getValue().equals("USD")) {
				Notification.show("Lỗi","Ngày này không có dữ liệu ADV. Vui lòng chọn đúng ngày ADV", Type.WARNING_MESSAGE);
				return;
			}
			tygia = tyGiaTqtService.findTyGiaTqtByNgayAdvAndCardType(advDate, cardtype);
			
			if(cbbLoaiTienTQT.getValue().equals("VND"))
			{
//				tfTyGiaTQT.setValue("1");
				tygiaInterchange = new BigDecimal(1);
				tygiaInterchangeDuocHoanTra = new BigDecimal(1);
				tygiaPhiXuLyGd = new BigDecimal(1);
				tygiaPhiXuLyGdDuocHoanTra = new BigDecimal(1);
				
				tfInterchangeDuocHuong.setCaption("(Tỷ giá: 1)");
				tfInterchangeDuocHoanTra.setCaption("(Tỷ giá: 1)");
				tfPhiXuLyGD.setCaption("(Tỷ giá: 1)");
				tfPhiXuLyGDDuocHoanTra.setCaption("(Tỷ giá: 1)");
				
			}
			else
			{
				if(tygia!=null) {
					
					if(cbbLoaiGD.getValue()!=null) {
						switch(cbbLoaiGD.getValue().toString()) {
							case "TTHH":
								tygiaInterchange = tygia.getTyGiaGdTthh().setScale(5, RoundingMode.HALF_UP);
								tygiaInterchangeDuocHoanTra = cardtype.equals("MC") ? tygia.getTyGiaGdTthh().setScale(5, RoundingMode.HALF_UP) : tygia.getTyGiaGdHttthh().setScale(5, BigDecimal.ROUND_HALF_UP);
								tygiaPhiXuLyGd = tygia.getTyGiaPxlTthh().setScale(5, RoundingMode.HALF_UP);
								tygiaPhiXuLyGdDuocHoanTra = tygia.getTyGiaPxlHttthh().setScale(5, RoundingMode.HALF_UP);
								break;
							case "RTM":
								tygiaInterchange = tygia.getTyGiaGdRtm().setScale(5, RoundingMode.HALF_UP);
								tygiaInterchangeDuocHoanTra = cardtype.equals("MC") ? tygia.getTyGiaGdRtm().setScale(5, RoundingMode.HALF_UP) : tygia.getTyGiaGdHtrtm().setScale(5, RoundingMode.HALF_UP);
								tygiaPhiXuLyGd = tygia.getTyGiaPxlRtm().setScale(5, RoundingMode.HALF_UP);
								tygiaPhiXuLyGdDuocHoanTra = tygia.getTyGiaPxlHtrtm().setScale(5, RoundingMode.HALF_UP);
								break;
							case "MSFF":
								tygiaInterchange = tygia.getTyGiaGdMsff().setScale(5, RoundingMode.HALF_UP);
								tygiaInterchangeDuocHoanTra = BigDecimal.ZERO.setScale(5);
								tygiaPhiXuLyGd = tygia.getTyGiaPxlMsff().setScale(5, RoundingMode.HALF_UP);
								tygiaPhiXuLyGdDuocHoanTra = BigDecimal.ZERO.setScale(5);
								break;
						}
//						tfTyGiaTQT.setValue(tygiaInterchange.toString());
					}
					
					tfInterchangeDuocHuong.setCaption("(Tỷ giá: " + tygiaInterchange.toString() + ")");
					tfInterchangeDuocHoanTra.setCaption("(Tỷ giá: " + tygiaInterchangeDuocHoanTra.toString() + ")");
					tfPhiXuLyGD.setCaption("(Tỷ giá: " + tygiaPhiXuLyGd.toString() + ")");
					tfPhiXuLyGDDuocHoanTra.setCaption("(Tỷ giá: " + tygiaPhiXuLyGdDuocHoanTra.toString() + ")");
				} else {
//					tfTyGiaTQT.setValue("0");
					tfInterchangeDuocHuong.setCaption("(Tỷ giá: 0)");
					tfInterchangeDuocHoanTra.setCaption("(Tỷ giá: 0)");
					tfPhiXuLyGD.setCaption("(Tỷ giá: 0)");
					tfPhiXuLyGDDuocHoanTra.setCaption("(Tỷ giá: 0)");
				}
				
			}
				
			
			if(cbbCardType.getValue().toString().startsWith("M")) {
				if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty()) {
//					BigDecimal tyGiaTQT = tfTyGiaTQT.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).add(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
				}
				
				if(!tfPhiXuLyGD.isEmpty()) {
//					BigDecimal tyGiaTQT =  tfTyGiaTQT.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tfTyGiaTQT.getValue().replace(",", ""));
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP);
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
				}
//				tfInterchangeDuocHuong.setCaption(null);
//				tfInterchangeDuocHoanTra.setCaption(null);
			} else {
				if(!tfInterchangeDuocHuong.isEmpty() && !tfInterchangeDuocHoanTra.isEmpty()) {
					BigDecimal tongVnd1 = interchangeDuocHuongPhaiTra.multiply(tygiaInterchange).setScale(0, RoundingMode.HALF_UP).add(interchangeDuocHoanTra.multiply(tygiaInterchangeDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDInterchange.setValue(tongVnd1.toString());
				}
				
				if(!tfPhiXuLyGD.isEmpty() && !tfPhiXuLyGDDuocHoanTra.isEmpty()) {
					BigDecimal tongVnd2 = phiXuLyGD.multiply(tygiaPhiXuLyGd).setScale(0, RoundingMode.HALF_UP).add(phiXuLyGDDuocHoanTra.multiply(tygiaPhiXuLyGdDuocHoanTra).setScale(0, RoundingMode.HALF_UP));
					tfTongVNDPhiGD.setValue(tongVnd2.toString());
				}
			}
			
			
		}
	}

	private void initData() {
			
		try {
			String loaiThe = cbbCardType.getValue()==null ? null : cbbCardType.getValue().toString();
			String ngayAdv = dfNgayADV.getValue()==null ? null :  timeConverter.convertDatetime(dfNgayADV.getValue());
			String loaiGd = cbbLoaiGD.getValue()==null ? null : cbbLoaiGD.getValue().toString();
			String loaiTien = cbbLoaiTienTQT.getValue()==null ? null : cbbLoaiTienTQT.getValue().toString();
			
			
			if(StringUtils.isNotEmpty(loaiThe) && StringUtils.isNotEmpty(ngayAdv) && StringUtils.isNotEmpty(loaiGd) && StringUtils.isNotEmpty(loaiTien)) {
				DsqtFeeDaily feeDaily = dsqtFeeDailyService.findOneByLoaiTheAndNgayAdvAndLoaiGdAndLoaiTienTqt(loaiThe, ngayAdv, loaiGd, loaiTien);
				if(feeDaily==null) {
					tfInterchangeDuocHuong.setValue("");
					tfInterchangeDuocHoanTra.setValue("");
//					tfTyGiaTQT.setValue("");
					tfTongVNDInterchange.setValue("");
					tfPhiXuLyGD.setValue("");
					tfPhiXuLyGDDuocHoanTra.setValue("");
					tfTongVNDPhiGD.setValue("");
					dfNgayIncomingFile.setValue(null);
					dfNgayHachToan.setValue(null);
				} else {
					tfInterchangeDuocHuong.setValue(feeDaily.getInterchangeDuocHuongPhaiTra().toString());
					tfInterchangeDuocHoanTra.setValue(feeDaily.getInterchangeDuocHoanTra().toString());
//					tfTyGiaTQT.setValue(feeDaily.getTyGiaTqt().toString());
					tfTongVNDInterchange.setValue(feeDaily.getTongInterchange().toString());
					tfPhiXuLyGD.setValue(feeDaily.getPhiXuLyGd().toString());
					tfPhiXuLyGDDuocHoanTra.setValue(feeDaily.getPhiXuLyGdDuocHoanTra().toString());
					tfTongVNDPhiGD.setValue(feeDaily.getTongPhiGd().toString());
					dfNgayIncomingFile.setValue(feeDaily.getNgayIncomingFile()==null ? null : new SimpleDateFormat("yyyyMMdd").parse(feeDaily.getNgayIncomingFile()));
					dfNgayHachToan.setValue(feeDaily.getNgayHachToan()==null ? null : new SimpleDateFormat("yyyyMMdd").parse(feeDaily.getNgayHachToan()));
				}
			}
			
			
		} catch (ReadOnlyException | ConversionException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public boolean checkValidator() {
		try {
			cbbCardType.validate();
			dfNgayADV.validate();
			cbbLoaiGD.validate();
			cbbLoaiTienTQT.validate();
			dfNgayHachToan.validate();
			return true;
		} catch (InvalidValueException ex) {
			cbbCardType.setValidationVisible(true);
			dfNgayADV.setValidationVisible(true);
			cbbLoaiGD.setValidationVisible(true);
			cbbLoaiTienTQT.setValidationVisible(true);
			dfNgayHachToan.setValidationVisible(true);
		}
		return false;
	}
}
