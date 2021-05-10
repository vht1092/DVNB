package com.dvnb.components.dstqt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import com.dvnb.components.pbcp.DataGridInvoiceComponent;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DoiSoatDataService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.dvnb.services.TyGiaTqtService;
import com.monitorjbl.xlsx.StreamingReader;
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
public class TinhTyGiaTQT extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TinhTyGiaTQT.class);
//	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "TÍNH TỶ GIÁ TQT";
	private DataGridInvoiceComponent grid;
	private static final String LUU_TY_GIA = "LƯU TỶ GIÁ";
	private static final String TINH_LAI = "TÍNH LẠI";
	private static final String TY_GIA_TQT = "TỶ GIÁ TQT";
	private static final String TY_GIA_PXL = "TỶ GIÁ PXL";
	private static final String TY_GIA_GD_TTHH = "TỶ GIÁ GD TTHH";
	private static final String TY_GIA_GD_RTM = "TỶ GIÁ GD RTM";
	private static final String TY_GIA_GD_MSFF = "TỶ GIÁ GD MSFF";
	private static final String TY_GIA_GD_HTTTHH = "TỶ GIÁ GD HT TTHH";
	private static final String TY_GIA_GD_HTRTM = "TỶ GIÁ GD HT RTM";
	private static final String TY_GIA_PXL_TTHH = "TỶ GIÁ PXL TTHH";
	private static final String TY_GIA_PXL_RTM = "TỶ GIÁ PXL RTM";
	private static final String TY_GIA_PXL_MSFF = "TỶ GIÁ PXL MSFF";
	private static final String TY_GIA_PXL_HTTTHH = "TỶ GIÁ PXL HT TTHH";
	private static final String TY_GIA_PXL_HTRTM = "TỶ GIÁ PXL HT RTM";
	private static final String SO_TIEN_GD = "SỐ TIỀN GD (USD)";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	
	private transient String sUserId;
	private final TextField tfSoTienGD;
	private final TextField tfTyGiaTQT;
//	private final TextField tfTyGiaPXL;
	private final TextField tfTyGiaGdTTHH;
	private final TextField tfTyGiaGdRTM;
	private final TextField tfTyGiaGdMSFF;
	private final TextField tfTyGiaGdHTTTHH;
	private final TextField tfTyGiaGdHTRTM;
	private final TextField tfTyGiaPxlTTHH;
	private final TextField tfTyGiaPxlRTM;
	private final TextField tfTyGiaPxlMSFF;
	private final TextField tfTyGiaPxlHTTTHH;
	private final TextField tfTyGiaPxlHTRTM;
	final Button btLuuTyGiaTQT = new Button(LUU_TY_GIA);
	final Button btTinhLai = new Button(TINH_LAI);
//	private DateField dfAdvDate;
	private TextField tfCardtype;
	private TextField tfNgayAdv;
	
	private final transient DoiSoatDataService doiSoatDataService;
	private final transient TyGiaTqtService tyGiaTqtService;
	final TimeConverter timeConverter = new TimeConverter();
	
	private TyGiaTqt tygia = new TyGiaTqt();
	private TyGiaTqt tygiaMastercard = new TyGiaTqt();
	private BigDecimal totalStQdVnd;
	BigDecimal tygiaCalc;
	BigDecimal soTienGd = BigDecimal.ZERO;
	DoiSoatThanhQuyetToan doisoatForm;// = new DoiSoatThanhQuyetToan();
	
	public TinhTyGiaTQT() throws ParseException {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayoutDetail = new HorizontalLayout();
		formLayoutDetail.setSpacing(true);
		final HorizontalLayout formLayoutButton = new HorizontalLayout();
		formLayoutButton.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
//		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		tyGiaTqtService = (TyGiaTqtService) helper.getBean("tyGiaTqtService");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridInvoiceComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final FormLayout formHeader = new FormLayout();
		
		final FormLayout formLeft = new FormLayout();
		formLeft.setMargin(new MarginInfo(false, false, false, false));
		
		final FormLayout formRight = new FormLayout();
		formRight.setMargin(new MarginInfo(false, false, false, true));
		
		String cardType = doisoatForm.cbbCardType==null ? "" : doisoatForm.cbbCardType.getValue().toString() ;//configurationHelper.getCardtype();
		tfCardtype = new TextField("Loại thẻ");
		tfCardtype.setValue(cardType);
		tfCardtype.setReadOnly(true);
		
		String ngayAdv = timeConverter.convertDatetime(doisoatForm.dfAdvDate.getValue());//configurationHelper.getNgayAdv();
		tfNgayAdv = new TextField("Ngày ADV");
		if(ngayAdv!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date ngayAdvConvers = sdf.parse(ngayAdv);
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			tfNgayAdv.setValue(sdf.format(ngayAdvConvers));
		}
		tfNgayAdv.setReadOnly(true);
		
		
		tfSoTienGD = new TextField(SO_TIEN_GD);
//		tfSoTienGD.setConverter(BigDecimal.class);
		tfSoTienGD.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaTQT = new TextField(TY_GIA_TQT);
		tfTyGiaTQT.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
//		tfTyGiaPXL = new TextField(TY_GIA_PXL);
//		tfTyGiaPXL.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaGdTTHH = new TextField(TY_GIA_GD_TTHH);
		tfTyGiaGdTTHH.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaGdRTM = new TextField(TY_GIA_GD_RTM);
		tfTyGiaGdRTM.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaGdMSFF = new TextField(TY_GIA_GD_MSFF);
		tfTyGiaGdMSFF.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaGdHTTTHH = new TextField(TY_GIA_GD_HTTTHH);
		tfTyGiaGdHTTTHH.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaGdHTRTM = new TextField(TY_GIA_GD_HTRTM);
		tfTyGiaGdHTRTM.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaPxlTTHH = new TextField(TY_GIA_PXL_TTHH);
		tfTyGiaPxlTTHH.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaPxlRTM = new TextField(TY_GIA_PXL_RTM);
		tfTyGiaPxlRTM.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaPxlMSFF = new TextField(TY_GIA_PXL_MSFF);
		tfTyGiaPxlMSFF.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaPxlHTTTHH = new TextField(TY_GIA_PXL_HTTTHH);
		tfTyGiaPxlHTTTHH.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaPxlHTRTM = new TextField(TY_GIA_PXL_HTRTM);
		tfTyGiaPxlHTRTM.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		if(cardType==null || ngayAdv==null) {
			Notification.show("Lỗi","Chọn ngày ADV và loại thẻ ở màn hình ĐSTQT trước khi tính tỷ giá", Type.ERROR_MESSAGE);
			return;
		}
		
		if(cardType.startsWith("V")) {
			tfSoTienGD.setVisible(false);
			tfTyGiaTQT.setVisible(false);
			formLayoutDetail.setVisible(true);
		} else {
			tfSoTienGD.setVisible(false);
			tfTyGiaTQT.setVisible(false);
			formLeft.setVisible(true);
			formRight.setVisible(false);
		}
		
//		if(cardType.startsWith("V")) {
//			tfSoTienGD.setVisible(false);
//			tfTyGiaTQT.setVisible(false);
//			formLayoutDetail.setVisible(true);
//		} else {
//			tfSoTienGD.setVisible(true);
//			tfTyGiaTQT.setVisible(true);
//			formLayoutDetail.setVisible(false);
//		}
		
		totalStQdVnd = doiSoatDataService.totalStQdVnd(ngayAdv,cardType);
		if(totalStQdVnd==null) {
			Notification.show("Lỗi","Ngày này không có dữ liệu ADV nên không thể tính tỷ giá", Type.WARNING_MESSAGE);
			tfSoTienGD.setValue("0");
			tfTyGiaTQT.setValue("0");
//				tfTyGiaPXL.setValue("0");
			tfTyGiaGdTTHH.setValue("0");
			tfTyGiaGdRTM.setValue("0");
			tfTyGiaGdMSFF.setValue("0");
			tfTyGiaGdHTTTHH.setValue("0");
			tfTyGiaGdHTRTM.setValue("0");
			tfTyGiaPxlTTHH.setValue("0");
			tfTyGiaPxlRTM.setValue("0");
			tfTyGiaPxlMSFF.setValue("0");
			tfTyGiaPxlHTTTHH.setValue("0");
			tfTyGiaPxlHTRTM.setValue("0");
			return;
		}
			
		tygia = tyGiaTqtService.findTyGiaTqtByNgayAdvAndCardType(ngayAdv,cardType);
		
		if(cardType.startsWith("M"))
			tygiaMastercard = tyGiaTqtService.calcTyGiaMastercardByNgayAdvAndCardType(ngayAdv,cardType);
		
		if(tygia!=null) {
			tfSoTienGD.setValue(tygia.getStGdUsd().toString());
			tfTyGiaTQT.setValue(tygia.getTyGiaTqt().setScale(6).toString());
//				tfTyGiaPXL.setValue(tygia.getTyGiaPxl().setScale(6).toString());
			tfTyGiaGdTTHH.setValue(tygia.getTyGiaGdTthh().setScale(6).toString());
			tfTyGiaGdRTM.setValue(tygia.getTyGiaGdRtm().setScale(6).toString());
			tfTyGiaGdMSFF.setValue(tygia.getTyGiaGdMsff().setScale(6).toString());
			tfTyGiaGdHTTTHH.setValue(tygia.getTyGiaGdHttthh().setScale(6).toString());
			tfTyGiaGdHTRTM.setValue(tygia.getTyGiaGdHtrtm().setScale(6).toString());
			tfTyGiaPxlTTHH.setValue(tygia.getTyGiaPxlTthh().setScale(6).toString());
			tfTyGiaPxlRTM.setValue(tygia.getTyGiaPxlRtm().setScale(6).toString());
			tfTyGiaPxlMSFF.setValue(tygia.getTyGiaPxlMsff().setScale(6).toString());
			tfTyGiaPxlHTTTHH.setValue(tygia.getTyGiaPxlHttthh().setScale(6).toString());
			tfTyGiaPxlHTRTM.setValue(tygia.getTyGiaPxlHtrtm().setScale(6).toString());
		} else {
			if(cardType.startsWith("V") || tygiaMastercard==null) {
				tfSoTienGD.setValue("0");
				tfTyGiaTQT.setValue("0");
//					tfTyGiaPXL.setValue("0");
				tfTyGiaGdTTHH.setValue("0");
				tfTyGiaGdRTM.setValue("0");
				tfTyGiaGdMSFF.setValue("0");
				tfTyGiaGdHTTTHH.setValue("0");
				tfTyGiaGdHTRTM.setValue("0");
				tfTyGiaPxlTTHH.setValue("0");
				tfTyGiaPxlRTM.setValue("0");
				tfTyGiaPxlMSFF.setValue("0");
				tfTyGiaPxlHTTTHH.setValue("0");
				tfTyGiaPxlHTRTM.setValue("0");
			} else {
				tfSoTienGD.setValue("0");
				tfTyGiaTQT.setValue("0");
//					tfTyGiaPXL.setValue("0");
				tfTyGiaGdTTHH.setValue(tygiaMastercard.getTyGiaGdTthh().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
				tfTyGiaGdRTM.setValue(tygiaMastercard.getTyGiaGdRtm().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
				tfTyGiaGdMSFF.setValue(tygiaMastercard.getTyGiaGdMsff().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
				tfTyGiaGdHTTTHH.setValue(tygiaMastercard.getTyGiaGdHttthh().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
				tfTyGiaGdHTRTM.setValue(tygiaMastercard.getTyGiaGdHtrtm().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
				tfTyGiaPxlTTHH.setValue("0");
				tfTyGiaPxlRTM.setValue("0");
				tfTyGiaPxlMSFF.setValue("0");
				tfTyGiaPxlHTTTHH.setValue("0");
				tfTyGiaPxlHTRTM.setValue("0");
			}
			
		}
//	);
		
		btTinhLai.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btTinhLai.setWidth(150.0f, Unit.PIXELS);
		btTinhLai.setIcon(FontAwesome.SAVE);
		btTinhLai.setDescription("Chỉ áp dụng cho MC/MD (VS/VSD tự nhập tỷ giá). Click Lưu tỷ giá để cập nhật sau khi tính lại tỷ giá");
		btTinhLai.addClickListener(event -> {
			if(cardType.startsWith("M")) {
				totalStQdVnd = doiSoatDataService.totalStQdVnd(ngayAdv,cardType);
				if(totalStQdVnd==null) {
					Notification.show("Lỗi","Ngày này không có dữ liệu ADV nên không thể tính tỷ giá", Type.ERROR_MESSAGE);
					tfSoTienGD.setValue("0");
					tfTyGiaTQT.setValue("0");
					tfTyGiaGdTTHH.setValue("0");
					tfTyGiaGdRTM.setValue("0");
					tfTyGiaGdMSFF.setValue("0");
					tfTyGiaGdHTTTHH.setValue("0");
					tfTyGiaGdHTRTM.setValue("0");
					tfTyGiaPxlTTHH.setValue("0");
					tfTyGiaPxlRTM.setValue("0");
					tfTyGiaPxlMSFF.setValue("0");
					tfTyGiaPxlHTTTHH.setValue("0");
					tfTyGiaPxlHTRTM.setValue("0");
					return;
				}
				
				tygiaMastercard = tyGiaTqtService.calcTyGiaMastercardByNgayAdvAndCardType(ngayAdv,cardType);
				
				if(tygiaMastercard != null) {
					tfSoTienGD.setValue("0");
					tfTyGiaTQT.setValue("0");
					tfTyGiaGdTTHH.setValue(tygiaMastercard.getTyGiaGdTthh().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
					tfTyGiaGdRTM.setValue(tygiaMastercard.getTyGiaGdRtm().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
					tfTyGiaGdMSFF.setValue(tygiaMastercard.getTyGiaGdMsff().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
					tfTyGiaGdHTTTHH.setValue(tygiaMastercard.getTyGiaGdHttthh().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
					tfTyGiaGdHTRTM.setValue(tygiaMastercard.getTyGiaGdHtrtm().setScale(6,BigDecimal.ROUND_HALF_UP).toString());
					tfTyGiaPxlTTHH.setValue("0");
					tfTyGiaPxlRTM.setValue("0");
					tfTyGiaPxlMSFF.setValue("0");
					tfTyGiaPxlHTTTHH.setValue("0");
					tfTyGiaPxlHTRTM.setValue("0");
					Notification.show("Thông báo","Tỷ giá đã được tính lại. Bấm nút Lưu tỷ giá để cập nhật", Type.WARNING_MESSAGE);
				}
				
			} else {
				Notification.show("Lỗi","Chỉ áp dụng cho MC/MD", Type.ERROR_MESSAGE);
			}
			
		});
		
		tfSoTienGD.addValueChangeListener(event -> {
			if(!tfSoTienGD.isEmpty())
				soTienGd = new BigDecimal(tfSoTienGD.getValue().replaceAll("[\\s|\\u00A0]+", "").replaceAll(",", ""));
				if(tygia!=null && tfSoTienGD.getValue().equals(tygia.getStGdUsd())) {
					tfTyGiaTQT.setValue(tygia.getTyGiaTqt().toString());
				}
				else
					if(totalStQdVnd != null && !tfSoTienGD.getValue().equals("0"))
					{
						tygiaCalc = totalStQdVnd.divide(soTienGd,6,RoundingMode.HALF_UP);
						tfTyGiaTQT.setValue(tygiaCalc.toString());
					}
		});
		
		btLuuTyGiaTQT.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btLuuTyGiaTQT.setWidth(150.0f, Unit.PIXELS);
		btLuuTyGiaTQT.setIcon(FontAwesome.SAVE);
		btLuuTyGiaTQT.addClickListener(event -> {
			if(totalStQdVnd==null) {
				Notification.show("Lỗi","Ngày này không có dữ liệu ADV nên không thể tính tỷ giá", Type.WARNING_MESSAGE);
				return;
			}
			TyGiaTqt tg = new TyGiaTqt();
			tg.setId(ngayAdv + cardType);
			tg.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
			tg.setUsrId(sUserId);
			tg.setNgayAdv(ngayAdv);
			tg.setStQdVnd(totalStQdVnd);
			tg.setStGdUsd(soTienGd);
			tg.setTyGiaTqt(new BigDecimal(tfTyGiaTQT.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
//			BigDecimal tygiaPXL = StringUtils.isEmpty(tfTyGiaPXL.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaPXL.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", ""));
			tg.setTyGiaGdTthh(StringUtils.isEmpty(tfTyGiaGdTTHH.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaGdTTHH.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaGdRtm(StringUtils.isEmpty(tfTyGiaGdRTM.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaGdRTM.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaGdMsff(StringUtils.isEmpty(tfTyGiaGdMSFF.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaGdMSFF.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaGdHttthh(StringUtils.isEmpty(tfTyGiaGdHTTTHH.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaGdHTTTHH.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaGdHtrtm(StringUtils.isEmpty(tfTyGiaGdHTRTM.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaGdHTRTM.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaPxlTthh(StringUtils.isEmpty(tfTyGiaPxlTTHH.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaPxlTTHH.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaPxlRtm(StringUtils.isEmpty(tfTyGiaPxlRTM.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaPxlRTM.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaPxlMsff(StringUtils.isEmpty(tfTyGiaPxlMSFF.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaPxlMSFF.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaPxlHttthh(StringUtils.isEmpty(tfTyGiaPxlHTTTHH.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaPxlHTTTHH.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaPxlHtrtm(StringUtils.isEmpty(tfTyGiaPxlHTRTM.getValue()) ? BigDecimal.ZERO :  new BigDecimal(tfTyGiaPxlHTRTM.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setCardType(cardType);
			tyGiaTqtService.save(tg);
			Notification.show("Thông tin","Cập nhật tý giá thành công", Type.WARNING_MESSAGE);
		});
		
		formHeader.addComponent(tfNgayAdv);
		formHeader.addComponent(tfCardtype);
		formHeader.addComponent(tfSoTienGD);
		formHeader.addComponent(tfTyGiaTQT);
//		form.addComponent(tfTyGiaPXL);
		
		formLeft.addComponent(tfTyGiaGdTTHH);
		formLeft.addComponent(tfTyGiaGdRTM);
		formLeft.addComponent(tfTyGiaGdMSFF);
		formLeft.addComponent(tfTyGiaGdHTTTHH);
		formLeft.addComponent(tfTyGiaGdHTRTM);
		
		formRight.addComponent(tfTyGiaPxlTTHH);
		formRight.addComponent(tfTyGiaPxlRTM);
		formRight.addComponent(tfTyGiaPxlMSFF);
		formRight.addComponent(tfTyGiaPxlHTTTHH);
		formRight.addComponent(tfTyGiaPxlHTRTM);
		
		form.addComponent(formHeader);
		formLayoutDetail.addComponent(formLeft);
		formLayoutDetail.addComponent(formRight);
		form.addComponent(formLayoutDetail);
		
		
		formLayoutButton.addComponent(btLuuTyGiaTQT);
		formLayoutButton.addComponent(btTinhLai);
		formLayoutButton.setComponentAlignment(btLuuTyGiaTQT, Alignment.MIDDLE_CENTER);
		formLayoutButton.setComponentAlignment(btTinhLai, Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(form);
		mainLayout.addComponent(formLayoutButton);
		
		
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	

	@Override
	public void eventReload() {
	}
	

}
