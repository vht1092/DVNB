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
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.money.Monetary;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
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
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
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
import com.vaadin.server.StreamResource.StreamSource;
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
@ViewScope
public class InterchangeTrongThang extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(InterchangeTrongThang.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "INTERCHANGE TRONG THÁNG";
	private static final String LOAI_GD = "LOẠI GD";
	private static final String XUAT_INTERCHANGE = "XUẤT FILE INTERCHANGE";
	private static final String XUAT_XU_LY_GD = "XUẤT FILE XỬ LÝ GD";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String CARD_TYPE = "LOẠI THẺ";
	
	private transient String sUserId;
	private final ComboBox cbbLoaiGD;
	private DateField dfTuNgay;
	private DateField dfDenNgay;
	private final ComboBox cbbCardType;
	
	final Button btExportInterchange = new Button(XUAT_INTERCHANGE);
	final Button btExportXuLyGD = new Button(XUAT_XU_LY_GD);
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	public String filename;
	protected DataSource localDataSource;
	
	public InterchangeTrongThang() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		formLayout1st.setMargin(new MarginInfo(false, false, true, false));
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");
		this.sUserId = SecurityUtils.getUserId();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, true, false, true));
		
		final Label lbTuNgay = new Label("TỪ NGÀY");
		dfTuNgay = new DateField();
		dfTuNgay.setDateFormat("dd/MM/yyyy");
		dfTuNgay.addValidator(new NullValidator(INPUT_FIELD, false));
		dfTuNgay.setValidationVisible(false);
		
		final Label lbDenNgay = new Label("ĐẾN NGÀY");
		dfDenNgay = new DateField();
		dfDenNgay.setDateFormat("dd/MM/yyyy");
		dfDenNgay.addValidator(new NullValidator(INPUT_FIELD, false));
		dfDenNgay.setValidationVisible(false);
		
		final Label lbLoaiGD = new Label(LOAI_GD);
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setNullSelectionAllowed(false);
		cbbLoaiGD.addItems("RTM","TTHH","MSFF");
		cbbLoaiGD.setItemCaption("MSFF","MoneySend/FastFund");
//		cbbLoaiGD.setValue("RTM");
		cbbLoaiGD.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbLoaiGD.setValidationVisible(false);
		
		final Label lbCardType = new Label(CARD_TYPE);
		cbbCardType = new ComboBox();
		cbbCardType.setWidth(100f, Unit.PIXELS);
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addValidator(new NullValidator(INPUT_FIELD, false));
		cbbCardType.addItems("MC","MD","VS","VSD");
		cbbCardType.setValidationVisible(false);
		
		btExportInterchange.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportInterchange.setWidth(250f, Unit.PIXELS);
		btExportInterchange.setIcon(FontAwesome.FILE_EXCEL_O);
		
		btExportXuLyGD.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btExportXuLyGD.setWidth(250f, Unit.PIXELS);
		btExportXuLyGD.setIcon(FontAwesome.FILE_EXCEL_O);
		
		btExportInterchange.addClickListener(event -> {
			if(!checkValidator()) {
				return;
			}
//			if(cbbCardType.getValue().toString().startsWith("M"))
//				filename = "InterchangeSummary.jasper";
//			else
				filename = "InterchangeSummaryVS.jasper";
			
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			String cardtype = cbbCardType.getValue().toString();
			String tungay = timeConverter.convertDatetime(dfTuNgay.getValue());
			String denngay = timeConverter.convertDatetime(dfDenNgay.getValue());
			String loaigd = cbbLoaiGD.getValue().toString();
			StreamResource myResourceXLSX = createTransMKResourceXLS(cardtype + "_" + "InterchangeTongHop" + "_" + tungay + "_" + denngay + "_" + loaigd);
			
			
			downloader.setFileDownloadResource(myResourceXLSX);
			downloader.download();
		});
		
		btExportXuLyGD.addClickListener(event -> {
			if(!checkValidator()) {
				return;
			}
			
			if(cbbCardType.getValue().toString().startsWith("M"))
				filename = "PhiXuLyGDSummary.jasper";
			else 
				filename = "PhiXuLyGDSummaryVS.jasper";
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			String cardtype = cbbCardType.getValue().toString();
			String tungay = timeConverter.convertDatetime(dfTuNgay.getValue());
			String denngay = timeConverter.convertDatetime(dfDenNgay.getValue());
			String loaigd = cbbLoaiGD.getValue().toString();
			StreamResource myResourceXLSX = createTransMKResourceXLS(cardtype + "_" + "PhiXuLyGDTongHop" + "_" + tungay + "_" + denngay + "_" + loaigd);
			
			downloader.setFileDownloadResource(myResourceXLSX);
			downloader.download();
		});
		
		formLayout1st.addComponent(lbTuNgay);
		formLayout1st.addComponent(dfTuNgay);
		formLayout1st.addComponent(lbDenNgay);
		formLayout1st.addComponent(dfDenNgay);
		formLayout1st.addComponent(lbLoaiGD);
		formLayout1st.addComponent(cbbLoaiGD);
		formLayout1st.addComponent(lbCardType);
		formLayout1st.addComponent(cbbCardType);
		
		formLayout2nd.addComponent(btExportInterchange);
		formLayout2nd.addComponent(btExportXuLyGD);
		
		
		formLayout1st.setComponentAlignment(lbTuNgay, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dfTuNgay, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbDenNgay, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dfDenNgay, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbLoaiGD, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbLoaiGD, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	

	@Override
	public void eventReload() {
	}
	
	public Map<String, Object> getParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p_loaigd", cbbLoaiGD.getValue().toString());
		parameters.put("p_loaithe", cbbCardType.getValue().toString());
		parameters.put("p_fromdate", timeConverter.convertDatetime(dfTuNgay.getValue()));
		parameters.put("p_todate", timeConverter.convertDatetime(dfDenNgay.getValue()));

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
		try {
			dfTuNgay.validate();
			dfDenNgay.validate();
			cbbLoaiGD.validate();
			cbbCardType.validate();
			return true;
		} catch (InvalidValueException ex) {
			dfTuNgay.setValidationVisible(true);
			dfDenNgay.setValidationVisible(true);
			cbbLoaiGD.setValidationVisible(true);
			cbbCardType.setValidationVisible(true);
		}
		return false;
	}

	

}
