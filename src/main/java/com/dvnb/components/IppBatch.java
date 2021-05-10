package com.dvnb.components;

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
import com.dvnb.services.DvnbSummaryService;
import com.dvnb.services.IppBatchService;
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
public class IppBatch extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(IppBatch.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "BATCH TRẢ GÓP";
	private transient String sUserId;
	private final transient IppBatchService ippBatchService;
	
	final Button btChuyenNgayTraGop = new Button("Chuyển ngày trả góp");
	final Button btTaoDlThuNo = new Button("Tạo dữ liệu thu nợ (ngày thu nợ đầu tiên)");
	final Button btCapNhatDlThuNo = new Button("Cập nhật dữ liệu thu nợ (ngày thu nợ sau)");
	final Button btXuLyThuNo = new Button("Chạy xử lý thu nợ IPP");
	final Button btTongHopIPP = new Button("Tổng hợp thông tin tài chính IPP trong ngày");
	final Button btReadGlIPP = new Button("Read GL IPP");
	
	final TimeConverter timeConverter = new TimeConverter();
	
	public String filename;
	protected DataSource localDataSource;
	
	public IppBatch() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");
		ippBatchService = (IppBatchService) helper.getBean("ippBatchService");
		
		this.sUserId = SecurityUtils.getUserId();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, true, false, true));

		final Label lbChuyenNgayTraGop = new Label("RESPONSE CODE: ");
		lbChuyenNgayTraGop.setVisible(false);
		
		btChuyenNgayTraGop.setStyleName("v-align-left");
		btChuyenNgayTraGop.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btChuyenNgayTraGop.setWidth(350f, Unit.PIXELS);
		btChuyenNgayTraGop.setIcon(FontAwesome.ARCHIVE);
		btChuyenNgayTraGop.addClickListener(event -> {
			String respCode = ippBatchService.chuyenNgayTraGop();
			lbChuyenNgayTraGop.setValue("RESPONSE CODE: " + respCode);
			lbChuyenNgayTraGop.setVisible(true);
		});
		
		btTaoDlThuNo.setStyleName("v-align-left");
		btTaoDlThuNo.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btTaoDlThuNo.setWidth(350f, Unit.PIXELS);
		btTaoDlThuNo.setIcon(FontAwesome.ARCHIVE);
		
		btCapNhatDlThuNo.setStyleName("v-align-left");
		btCapNhatDlThuNo.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btCapNhatDlThuNo.setWidth(350f, Unit.PIXELS);
		btCapNhatDlThuNo.setIcon(FontAwesome.ARCHIVE);
		
		btXuLyThuNo.setStyleName("v-align-left");
		btXuLyThuNo.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btXuLyThuNo.setWidth(350f, Unit.PIXELS);
		btXuLyThuNo.setIcon(FontAwesome.ARCHIVE);
		
		final Label lbTongHopIPPDailyProcess = new Label("Response code Daily Process: ");
		lbTongHopIPPDailyProcess.setVisible(false);
		
		final Label lbTongHopIPPDailyGlGen = new Label("Response code Daily Gen: ");
		lbTongHopIPPDailyGlGen.setVisible(false);
		
		final Label lbTongHopIPPDailyLocBal = new Label("Response code Daily LOC Bal: ");
		lbTongHopIPPDailyLocBal.setVisible(false);
		
		btTongHopIPP.setStyleName("v-align-left");
		btTongHopIPP.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btTongHopIPP.setWidth(350f, Unit.PIXELS);
		btTongHopIPP.setIcon(FontAwesome.ARCHIVE);
		btTongHopIPP.addClickListener(event -> {
			String ippDailyProcessRespCode = ippBatchService.tongHopThongTinTaiChinhIppDailyProcess();
			String ippDailyGlGenRespCode = ippBatchService.tongHopThongTinTaiChinhIppDailyGlGen();
			String ippDailyLocBalRespCode = ippBatchService.tongHopThongTinTaiChinhIppDailyLocBal();
			
			lbTongHopIPPDailyProcess.setValue("RESPONSE CODE IPP_DAILY_PROCESS: " + ippDailyProcessRespCode);
			lbTongHopIPPDailyGlGen.setValue("RESPONSE CODE IPP_DAILY_GL_GEN: " + ippDailyGlGenRespCode);
			lbTongHopIPPDailyLocBal.setValue("RESPONSE CODE IPP_DAILY_IPP_LOC_BAL: " + ippDailyLocBalRespCode);
			
			lbTongHopIPPDailyProcess.setVisible(true);
			lbTongHopIPPDailyGlGen.setVisible(true);
			lbTongHopIPPDailyLocBal.setVisible(true);
		});
		
		btReadGlIPP.setStyleName("v-align-left");
		btReadGlIPP.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		btReadGlIPP.setWidth(350f, Unit.PIXELS);
		btReadGlIPP.setIcon(FontAwesome.ARCHIVE);
		
		formLayout.addComponent(btChuyenNgayTraGop);
		formLayout.addComponent(lbChuyenNgayTraGop);
		formLayout.addComponent(btTaoDlThuNo);
		formLayout.addComponent(btCapNhatDlThuNo);
		formLayout.addComponent(btXuLyThuNo);
		formLayout.addComponent(btTongHopIPP);
		formLayout.addComponent(lbTongHopIPPDailyProcess);
		formLayout.addComponent(lbTongHopIPPDailyGlGen);
		formLayout.addComponent(lbTongHopIPPDailyLocBal);
		formLayout.addComponent(btReadGlIPP);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	

	@Override
	public void eventReload() {
	}
	

}
