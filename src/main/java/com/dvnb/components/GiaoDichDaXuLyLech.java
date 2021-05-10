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
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
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
public class GiaoDichDaXuLyLech extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GiaoDichDaXuLyLech.class);
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "GD ĐÃ XỬ LÝ LỆCH";
	private static final String CARD_TYPE = "LOẠI THẺ";
	private static final String LOAI_TIEN = "LOẠI TIỀN TQT";
	private static final String VIEW = "VIEW";
	private static final String SAVE = "SAVE";
	private static final String EXPORT = "EXPORT";
	private static final String LOAI_GD = "LOẠI GD";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	
	private transient String sUserId;
	private final ComboBox cbbCardType;
	private final ComboBox cbbLoaiTienTQT;
	private final ComboBox cbbLoaiGD;
	private DateField dffromDate;
	private DateField dftoDate;
	private Label lbNote;
	
	final Button btView = new Button(VIEW);
	final Button btSave = new Button(SAVE);
	final Button btExport = new Button(EXPORT);
	
	private DataGridGiaoDichTQTChoXuLy grid;
	private transient Page<DoiSoatData> result;
	private final transient DoiSoatDataService doiSoatDataService;
	
	// Paging
	private final static int SIZE_OF_PAGE = 100;
	private final static int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	public String filename;
	protected DataSource localDataSource;
	
	public GiaoDichDaXuLyLech() {
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
		localDataSource = (DataSource) helper.getBean("dataSource");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridGiaoDichTQTChoXuLy("GDDXLL");
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
		lbtoDate.setWidth(89.23f, Unit.PIXELS);
		dftoDate = new DateField();
		dftoDate.setDateFormat("dd/MM/yyyy");
		dftoDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dftoDate.setValidationVisible(false);
		
		
		final Label lbCardType = new Label(CARD_TYPE);
		cbbCardType = new ComboBox();
		cbbCardType.setNullSelectionAllowed(true);
		cbbCardType.addItems("MC","MD","VS","VSD");
		
		final Label lbLoaiTienTQT = new Label(LOAI_TIEN);
		cbbLoaiTienTQT = new ComboBox();
		cbbLoaiTienTQT.setNullSelectionAllowed(false);
		cbbLoaiTienTQT.addItems("All","VND","USD");
		cbbLoaiTienTQT.setValue("All");

		final Label lbLoaiGD = new Label(LOAI_GD);
		cbbLoaiGD = new ComboBox();
		cbbLoaiGD.setNullSelectionAllowed(false);
		cbbLoaiGD.addItems("All","GDTTHH","GDRTM");
		cbbLoaiGD.setItemCaption("GDTTHH", "GD thanh toán hàng hóa");
		cbbLoaiGD.setItemCaption("GDRTM","GD rút tiền mặt");
		cbbLoaiGD.setValue("All");
		
		btView.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		btView.setWidth(100.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setWidth(100.0f, Unit.PIXELS);
		btSave.setIcon(FontAwesome.SAVE);
		
		btExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btExport.setWidth(100.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.DOWNLOAD);
		
		lbNote = new Label("<i>Tick chọn GD để chuyển về lại trạng thái \"Giao dịch phát sinh hoàn trả lệch\", nếu người dùng đã chọn nhầm trước đó</i>");
		lbNote.setContentMode(ContentMode.HTML);
		lbNote.setCaptionAsHtml(true);
		
		btView.addClickListener(event -> {
			grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid("All");

			grid.refreshData();
			// Refresh paging button
		});
		
		btExport.addClickListener(event -> {
			filename = "GiaoDichDaXuLyLech.jasper";
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			StreamResource myResourceXLSX = createTransMKResourceXLS();
			
			downloader.setFileDownloadResource(myResourceXLSX);
			downloader.download();
		});
		
		btSave.addClickListener(event -> {
			grid.grid.getSelectedRows().forEach(id -> {
				if(grid.grid.getContainerDataSource().getItemIds().contains(id)) {
					String ngayHoanTra = grid.grid.getContainerDataSource().getContainerProperty(id, "ngayHoanTra").toString();
					String soThe = grid.grid.getContainerDataSource().getContainerProperty(id, "soThe").toString();
					String maCapPhep = grid.grid.getContainerDataSource().getContainerProperty(id, "maCapPhep").toString();
					String ngayGd = grid.grid.getContainerDataSource().getContainerProperty(id, "ngayGd").toString();
					String traceNo = grid.grid.getContainerDataSource().getContainerProperty(id, "trace").toString();
					System.out.println("ngayHoanTra: " + ngayHoanTra + "\n" + "soThe: " + soThe + "\n" 
							+ "mapCapPhep: " + maCapPhep + "\n" + "ngayGd: " + ngayGd + "\n" + "traceNo: " + traceNo + "\n" );
					doiSoatDataService.updateNgayHoanTra(null, soThe, maCapPhep, ngayGd, traceNo);
					
					grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.initGrid("All");

					grid.refreshData();
				}
				
			});
		});
		
		formLayout1st.addComponent(lbfromDate);
		formLayout1st.addComponent(dffromDate);
		formLayout1st.addComponent(lbtoDate);
		formLayout1st.addComponent(dftoDate);
		
		formLayout2nd.addComponent(lbCardType);
		formLayout2nd.addComponent(cbbCardType);
		formLayout2nd.addComponent(lbLoaiTienTQT);
		formLayout2nd.addComponent(cbbLoaiTienTQT);
		formLayout2nd.addComponent(lbLoaiGD);
		formLayout2nd.addComponent(cbbLoaiGD);
		formLayout2nd.addComponent(btView);
		
		formLayout3rd.addComponent(btSave);
		formLayout3rd.addComponent(btExport);
		
		formLayout1st.setComponentAlignment(lbfromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dffromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbtoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dftoDate, Alignment.MIDDLE_LEFT);
		
		formLayout2nd.setComponentAlignment(lbCardType, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbCardType, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbLoaiTienTQT, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(cbbLoaiTienTQT, Alignment.MIDDLE_LEFT);
		formLayout2nd.setComponentAlignment(lbLoaiGD, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(cbbLoaiGD, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btView, Alignment.MIDDLE_CENTER);
		
		formLayout3rd.setComponentAlignment(btSave, Alignment.MIDDLE_CENTER);
		
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.addComponent(formLayout3rd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout3rd, Alignment.MIDDLE_CENTER);
		
		form.addComponent(formLayout);
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		
		mainLayout.addComponent(lbNote);
		mainLayout.addComponent(grid);
		
		setCompositionRoot(mainLayout);
	}
	

	private Page<DoiSoatData> getData(Pageable page) {
		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
		String lttqt = cbbLoaiTienTQT == null ? "All" : cbbLoaiTienTQT.getValue().toString();
		String loaigd = cbbLoaiGD == null ? "All" : cbbLoaiGD.getValue().toString();
	    result = new PageImpl<>(doiSoatDataService.findGDDaXuLyLechTuNgayDenNgayAndLttqtAndLoaiGd(tungay, denngay, lttqt, loaigd));
		return result;
	}

	@Override
	public void eventReload() {
	}
	
	
	public Map<String, Object> getParameter() {
		String loaigd = cbbLoaiGD == null ? "All" : cbbLoaiGD.getValue().toString();
		String lttqt = cbbLoaiTienTQT == null ? "All" : cbbLoaiTienTQT.getValue().toString();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("p_fromdate", timeConverter.convertDatetime(dffromDate.getValue()));
		parameters.put("p_todate", timeConverter.convertDatetime(dftoDate.getValue()));
		parameters.put("p_loaigd", loaigd);
		parameters.put("p_lttqt", lttqt);
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
	public StreamResource createTransMKResourceXLS() {
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
		}, "GiaoDichDaXuLyLech.xlsx");
	}

}
