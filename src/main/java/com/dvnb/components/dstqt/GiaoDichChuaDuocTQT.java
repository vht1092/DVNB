package com.dvnb.components.dstqt;

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
import com.dvnb.entities.DoiSoatData;
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
public class GiaoDichChuaDuocTQT extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GiaoDichChuaDuocTQT.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "GD CHƯA ĐƯỢC TQT";
	private DataGridGiaoDichChuaTQT grid;
	private static final String CARD_TYPE = "LOẠI THẺ";
	private static final String LOAI_TIEN = "LOẠI TIỀN TQT";
	private static final String VIEW = "VIEW";
	private static final String EXPORT = "EXPORT";
	private static final String LOAI_GD = "LOẠI GD";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	
	private transient String sUserId;
	private final ComboBox cbbCardType;
	private final ComboBox cbbLoaiGD;
	private DateField dffromDate;
	private DateField dftoDate;
	
	final Button btView = new Button(VIEW);
	final Button btExport = new Button(EXPORT);
	
	private transient Page<DoiSoatData> result;
	private final transient DoiSoatDataService doiSoatDataService;
	
	// Paging
	private final static int SIZE_OF_PAGE = 100;
	private final static int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	private int i;
	
	public GiaoDichChuaDuocTQT() {
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
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		doiSoatDataService = (DoiSoatDataService) helper.getBean("doiSoatDataService");
		this.sUserId = SecurityUtils.getUserId();
		
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final Label lbfromDate = new Label("TỪ NGÀY");
		lbfromDate.setWidth(56.05f, Unit.PIXELS);
		dffromDate = new DateField();
		dffromDate.setDateFormat("dd/MM/yyyy");
		dffromDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dffromDate.setValidationVisible(false);
		
		final Label lbtoDate = new Label("ĐẾN NGÀY");
//		lbtoDate.setWidth(89.23f, Unit.PIXELS);
		dftoDate = new DateField();
		dftoDate.setDateFormat("dd/MM/yyyy");
		dftoDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dftoDate.setValidationVisible(false);
		
		
		final Label lbCardType = new Label(CARD_TYPE);
		cbbCardType = new ComboBox();
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD","VS","VSD");
		
		final Label lbLoaiGD = new Label(LOAI_GD);
		lbLoaiGD.setWidth(61.75f, Unit.PIXELS);
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setNullSelectionAllowed(false);
		cbbLoaiGD.addItems("GDTTHH","GDRTIM");
		cbbLoaiGD.setItemCaption("GDTTHH", "GD thanh toán hàng hóa");
		cbbLoaiGD.setItemCaption("GDRTIM","GD rút tiền mặt");
		cbbLoaiGD.setValue("GDTTHH");
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(100.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		
		btExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btExport.setWidth(100.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.DOWNLOAD);
		
		btView.addClickListener(event -> {
			grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid(cbbLoaiGD.getValue().toString());

			grid.refreshData();
			// Refresh paging button
		});
		
		grid = new DataGridGiaoDichChuaTQT();
		
		formLayout1st.addComponent(lbfromDate);
		formLayout1st.addComponent(dffromDate);
		formLayout1st.addComponent(lbtoDate);
		formLayout1st.addComponent(dftoDate);
		formLayout1st.addComponent(lbCardType);
		formLayout1st.addComponent(cbbCardType);
		formLayout1st.addComponent(lbLoaiGD);
		formLayout1st.addComponent(cbbLoaiGD);
		formLayout3rd.addComponent(btView);
		formLayout3rd.addComponent(btExport);
		
		formLayout1st.setComponentAlignment(lbfromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dffromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbtoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dftoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbLoaiGD, Alignment.MIDDLE_CENTER);
		formLayout1st.setComponentAlignment(cbbLoaiGD, Alignment.MIDDLE_CENTER);
		formLayout3rd.setComponentAlignment(btView, Alignment.MIDDLE_CENTER);
		
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.addComponent(formLayout3rd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(grid);
		
		setCompositionRoot(mainLayout);
	}
	
	private Page<DoiSoatData> getData(Pageable page) {
		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
	    result = new PageImpl<>(doiSoatDataService.findAllTuNgayDenNgay(tungay, denngay));
		return result;
	}

	@Override
	public void eventReload() {
	}
	

}
