package com.dvnb.components;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

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
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Grid.FooterRow;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Grid.SelectionModel;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.HtmlRenderer;
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
public class DataGridUploadInterchange extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridUploadInterchange.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	public Grid gridUnselect = new Grid();
	
	private final transient DvnbInvoiceMcService dvnbInvoiceMcService;
	private final transient DvnbInvoiceVsService dvnbInvoiceVsService;
	private final transient DvnbInvoiceUploadService dvnbInvoiceUploadService;
	
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	private IndexedContainer containerUnselect= new IndexedContainer();
	private String ketchuyenFlag;
	private String fileNameExport;
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	private SpringConfigurationValueHelper configurationHelper;
	public String filename;
	protected DataSource localDataSource;
	
	public Page<DoiSoatData> dataSource;

	List<InvoiceUpload> invoiceUploadSummary;
	List<InvoiceUpload> invoiceUploadDetail;
	int stt=0;
	
	private DoiSoatData doiSoatTotal = new DoiSoatData();
	BigDecimal totalStTqt = BigDecimal.ZERO;
	BigDecimal totalStTqt2 = BigDecimal.ZERO;
	
	@SuppressWarnings("unchecked")
	public DataGridUploadInterchange() {

		setSizeFull();

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		dvnbInvoiceMcService = (DvnbInvoiceMcService) helper.getBean("dvnbInvoiceMcService");
		dvnbInvoiceVsService = (DvnbInvoiceVsService) helper.getBean("dvnbInvoiceVsService");
		dvnbInvoiceUploadService = (DvnbInvoiceUploadService) helper.getBean("dvnbInvoiceUploadService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		doiSoatTotal.setStTqt(BigDecimal.ZERO);
		
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();

		// init grid
		grid = new Grid();
		grid.setSizeFull();
		grid.setHeightByRows(15);
		grid.setHeightMode(HeightMode.ROW);
		
		container = new IndexedContainer();
		
		container.addContainerProperty("stt", String.class, "");
		container.addContainerProperty("maGd", String.class, "");
		container.addContainerProperty("soThe", String.class, "");
		container.addContainerProperty("stGd", String.class, "");
		container.addContainerProperty("stTqt", String.class, "");
		container.addContainerProperty("stQdVnd", String.class, "");
		container.addContainerProperty("ltgd", String.class, "");
		container.addContainerProperty("lttqt", String.class, "");
		container.addContainerProperty("phiInterchange", String.class, "");
		container.addContainerProperty("tyGiaTqt", String.class, "");
		container.addContainerProperty("dvcnt", String.class, "");
		container.addContainerProperty("ngayGd", String.class, "");
		container.addContainerProperty("maCapPhep", String.class, "");
		container.addContainerProperty("ngayFileIncoming", String.class, "");
		container.addContainerProperty("mcc", String.class, "");
		
		grid.setContainerDataSource(container);
		grid.getColumn("stt").setHeaderCaption("STT");
		grid.getColumn("maGd").setHeaderCaption("Mã giao dịch");
		grid.getColumn("soThe").setHeaderCaption("Số thẻ");
		grid.getColumn("stGd").setHeaderCaption("ST GD");
		grid.getColumn("stTqt").setHeaderCaption("ST TQT");
		grid.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		grid.getColumn("ltgd").setHeaderCaption("LDGD");
		grid.getColumn("lttqt").setHeaderCaption("LTTQT");
		grid.getColumn("phiInterchange").setHeaderCaption("Phí interchange");
		grid.getColumn("tyGiaTqt").setHeaderCaption("Tỷ giá TQT");
		grid.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		grid.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		grid.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		grid.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		grid.getColumn("mcc").setHeaderCaption("MCC");
		
		
		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);
	}

	public void initGrid(final String getColumn) {
		
		if (createDataForContainer() == false) {
			if (!lbNoDataFound.isVisible() && dataSource != null) {
				lbNoDataFound.setVisible(true);
				grid.setVisible(false);
			}
		} else {
			if (!grid.isVisible()) {
				grid.setVisible(true);
				lbNoDataFound.setVisible(false);
			}
		}
        
	}
	
	public void refreshData() {
		getUI().access(() -> {
			if (createDataForContainer() == false) {
				if (!lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(true);
				}
				if (grid.isVisible()) {
					grid.setVisible(false);
				}
			} else {
				if (lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(false);
				}
				if (!grid.isVisible()) {
					grid.setVisible(true);
				}
			}
		});
	}
	int i=0;
	@SuppressWarnings("unchecked")
	private boolean createDataForContainer() {
		boolean flag = false;
		stt = 0;
		
		if (dataSource != null && dataSource.getTotalElements()>0) {
			container.removeAllItems();
			dataSource.forEach(s -> {
				i++;
				stt++;
				Item item = container.getItem(container.addItem());
				
				item.getItemProperty("stt").setValue(String.valueOf(stt));
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("stGd").setValue(String.valueOf(s.getStGd()));
				item.getItemProperty("stTqt").setValue(String.valueOf(s.getStTqt()));
				item.getItemProperty("stQdVnd").setValue(String.valueOf(s.getStQdVnd()));
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("phiInterchange").setValue(String.valueOf(s.getInterchange()));
				item.getItemProperty("tyGiaTqt").setValue(String.valueOf(s.getTyGiaTrichNo()));
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("mcc").setValue(s.getMcc());
				
			});
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	
	private String formatNumberColor(BigDecimal number) {
		if (number.compareTo(BigDecimal.ZERO) < 0) {
			return "<span style=\"padding:7px 0px; background-color: #FFFF00\">" + number + "</span>";

		} else
			return String.valueOf(number);
	}
	
	public Map<String, Object> getParameter() {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_ketchuyen", ketchuyenFlag);
			parameters.put("p_ketchuyen_status", ketchuyenFlag.equals("Y") ? "KẾT CHUYỂN VỀ ĐƠN VỊ" : "KHÔNG KẾT CHUYỂN VỀ ĐƠN VỊ");
			parameters.put("p_tungay", "");
			parameters.put("p_denngay", "");

			return parameters;
	}
	
	private ByteArrayOutputStream makeFileForDownLoad(String filename, String extension) throws JRException, SQLException {

		Connection con = localDataSource.getConnection();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		if (this.getParameter() != null) {
			// Tham so truyen vao cho bao cao
			Map<String, Object> parameters = this.getParameter();

			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(configurationHelper.getPathFileRoot() + "/invoicetemplate/" + filename);
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
		}, "Invoice_baocao.xlsx");
	}
	
	

}
