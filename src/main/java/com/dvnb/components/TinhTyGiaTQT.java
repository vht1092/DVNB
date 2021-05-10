package com.dvnb.components;

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
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "TÍNH TỶ GIÁ TQT";
	private DataGridInvoiceComponent grid;
	private static final String LUU_TY_GIA = "LƯU TỶ GIÁ TQT";
	private static final String TY_GIA_TQT = "TỶ GIÁ TQT";
	private static final String SO_TIEN_GD = "SỐ TIỀN GD (USD)";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	
	private transient String sUserId;
	private final TextField tfSoTienGD;
	private final TextField tfTyGiaTQT;
	
	final Button btLuuTyGiaTQT = new Button(LUU_TY_GIA);
	private DateField dfAdvDate;
	
	private final transient DoiSoatDataService doiSoatDataService;
	private final transient TyGiaTqtService tyGiaTqtService;
	final TimeConverter timeConverter = new TimeConverter();
	
	private TyGiaTqt tygia = new TyGiaTqt();
	private BigDecimal totalStQdVnd;
	BigDecimal tygiaCalc;
	
	public TinhTyGiaTQT() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		tyGiaTqtService = (TyGiaTqtService) helper.getBean("tyGiaTqtService");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridInvoiceComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		dfAdvDate = new DateField("Ngày ADV");
		dfAdvDate.setDateFormat("dd/MM/yyyy");
		dfAdvDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dfAdvDate.setValidationVisible(false);
		
		tfSoTienGD = new TextField(SO_TIEN_GD);
		tfSoTienGD.setConverter(BigDecimal.class);
		tfSoTienGD.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfTyGiaTQT = new TextField(TY_GIA_TQT);
		tfTyGiaTQT.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		String cardType = configurationHelper.getCardtype();
		
		dfAdvDate.addValueChangeListener(event -> {
			String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
			totalStQdVnd = doiSoatDataService.totalStQdVnd(advDate,cardType);
			if(totalStQdVnd==null) {
				Notification.show("Lỗi","Ngày này không có dữ liệu ADV nên không thể tính tỷ giá", Type.WARNING_MESSAGE);
				tfSoTienGD.setValue(null);
				tfTyGiaTQT.setValue(null);
				return;
			}
			tygia = tyGiaTqtService.findTyGiaTqtByNgayAdv(advDate);
			if(tygia!=null) {
				tfSoTienGD.setValue(tygia.getStGdUsd().toString());
				tfTyGiaTQT.setValue(tygia.getTyGiaTqt().toString());
			} else {
				
				tfSoTienGD.setValue(null);
				tfTyGiaTQT.setValue(null);
			}
		});
		
		tfSoTienGD.addValueChangeListener(event -> {
			if(!tfSoTienGD.isEmpty())
				if(tygia!=null && tfSoTienGD.getValue().equals(tygia.getStGdUsd())) {
					tfTyGiaTQT.setValue(tygia.getTyGiaTqt().toString());
				}
				else
					if(totalStQdVnd != null)
					{
						tygiaCalc = totalStQdVnd.divide(new BigDecimal(tfSoTienGD.getValue().replace(",", "")),5,RoundingMode.HALF_UP);
						System.out.println(tygiaCalc);
						System.out.println(tygiaCalc.toString());
						System.out.println(String.valueOf(tygiaCalc.doubleValue()));
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
			String advDate = timeConverter.convertDatetime(dfAdvDate.getValue());
			TyGiaTqt tg = new TyGiaTqt();
			tg.setCreTms(new BigDecimal(timeConverter.getCurrentTime()));
			tg.setUsrId(sUserId);
			tg.setNgayAdv(advDate);
			tg.setStQdVnd(totalStQdVnd);
			tg.setStGdUsd(new BigDecimal(tfSoTienGD.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setTyGiaTqt(new BigDecimal(tfTyGiaTQT.getValue().replaceAll("[\\s|\\u00A0]+", "").replace(",", "")));
			tg.setCardType(cardType);
			tyGiaTqtService.save(tg);
		});
		
		form.addComponent(dfAdvDate);
		form.addComponent(tfSoTienGD);
		form.addComponent(tfTyGiaTQT);
		form.addComponent(btLuuTyGiaTQT);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	

	@Override
	public void eventReload() {
	}
	

}
