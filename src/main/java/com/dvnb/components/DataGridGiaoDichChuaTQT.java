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
public class DataGridGiaoDichChuaTQT extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridGiaoDichChuaTQT.class);
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
	private String loaiGD = "";
	
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
	public DataGridGiaoDichChuaTQT() {

		setSizeFull();

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		dvnbInvoiceMcService = (DvnbInvoiceMcService) helper.getBean("dvnbInvoiceMcService");
		dvnbInvoiceVsService = (DvnbInvoiceVsService) helper.getBean("dvnbInvoiceVsService");
		dvnbInvoiceUploadService = (DvnbInvoiceUploadService) helper.getBean("dvnbInvoiceUploadService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		doiSoatTotal.setStTqt(BigDecimal.ZERO);
		
		// init grid
		grid = new Grid();
//		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(15);
//		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
		grid.setSelectionMode(SelectionMode.MULTI);
//		grid.setEditorEnabled(true);
		
		container = new IndexedContainer();
		
		container.addContainerProperty("stt", Integer.class, "");
		container.addContainerProperty("cif", String.class, "");
		container.addContainerProperty("dvpht", String.class, "");
		container.addContainerProperty("tenChuThe", String.class, "");
		container.addContainerProperty("soThe", String.class, "");
		container.addContainerProperty("casa", String.class, "");
		container.addContainerProperty("ngayGd", String.class, "");
		container.addContainerProperty("dvcnt", String.class, "");
		container.addContainerProperty("maLoi", String.class, "");
		container.addContainerProperty("maCp", String.class, "");
		container.addContainerProperty("trace", String.class, "");
		container.addContainerProperty("mcc", String.class, "");
		container.addContainerProperty("phatSinhNoNguyenTe", BigDecimal.class, "");
		container.addContainerProperty("maNguyenTe", String.class, "");
		container.addContainerProperty("phatSinhNoVnd", BigDecimal.class, "");
		container.addContainerProperty("phiIsa", BigDecimal.class, "");
		container.addContainerProperty("vatPhiIsa", BigDecimal.class, "");
		container.addContainerProperty("soTienHoanTraKh", BigDecimal.class, "");
//		if(loaiGD.equals("GDRTIM")) {
//			container.addContainerProperty("phiRtm", BigDecimal.class, "");
//			container.addContainerProperty("vatPhiRtm", BigDecimal.class, "");
//		}
		container.addContainerProperty("ngayHeThongRelease", String.class, "");
		
		
		grid.setContainerDataSource(container);

		grid.getColumn("stt").setHeaderCaption("STT");
		grid.getColumn("cif").setHeaderCaption("CIF");
		grid.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		grid.getColumn("tenChuThe").setHeaderCaption("Tên chủ thẻ");
		grid.getColumn("soThe").setHeaderCaption("Số thẻ");
		grid.getColumn("casa").setHeaderCaption("CASA");
		grid.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		grid.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		grid.getColumn("maLoi").setHeaderCaption("Mã lỗi");
		grid.getColumn("maCp").setHeaderCaption("Mã CP");
		grid.getColumn("trace").setHeaderCaption("Trace");
		grid.getColumn("mcc").setHeaderCaption("MCC");
		grid.getColumn("phatSinhNoNguyenTe").setHeaderCaption("Phát sinh nợ (nguyên tệ)");
		grid.getColumn("maNguyenTe").setHeaderCaption("Mã nguyên tệ");
		grid.getColumn("phatSinhNoVnd").setHeaderCaption("Phát sinh nợ (VNĐ)");
		grid.getColumn("phiIsa").setHeaderCaption("Phí ISA");
		grid.getColumn("vatPhiIsa").setHeaderCaption("VAT phí ISA");
		grid.getColumn("soTienHoanTraKh").setHeaderCaption("Số tiền hoàn trả KH");
//		if(loaiGD.equals("GDRTIM")) {
//			grid.getColumn("phiRtm").setHeaderCaption("Phí RTM");
//			grid.getColumn("vatPhiRtm").setHeaderCaption("VAT phí RTM");
//		}
		grid.getColumn("ngayHeThongRelease").setHeaderCaption("Ngày hệ thống release");
		
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();
		
		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);
	}

	public void initGrid(final String _loaiGD) {
		
		this.loaiGD = _loaiGD;
		
		if(!loaiGD.equals("GDRTIM")) {
			container.removeContainerProperty("phiRtm");
			container.removeContainerProperty("vatPhiRtm");
		} else {
			container.addContainerProperty("phiRtm", BigDecimal.class, BigDecimal.ZERO);
			container.addContainerProperty("vatPhiRtm", BigDecimal.class, BigDecimal.ZERO);
			
			grid.getColumn("phiRtm").setHeaderCaption("Phí RTM");
			grid.getColumn("vatPhiRtm").setHeaderCaption("VAT phí RTM");
		}
		
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
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("cif").setValue("");
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("maLoi").setValue("");
				item.getItemProperty("maCp").setValue("");
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("mcc").setValue(s.getMcc());
				item.getItemProperty("phatSinhNoNguyenTe").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("maNguyenTe").setValue(s.getLoaiTienNguyenTeGd());
				item.getItemProperty("phatSinhNoVnd").setValue(s.getStQdVnd());
				item.getItemProperty("phiIsa").setValue(s.getPhiIsaHoanTraTruyThu());
				item.getItemProperty("vatPhiIsa").setValue(s.getVatPhiIsaHoanTraTruyThu());
				item.getItemProperty("soTienHoanTraKh").setValue(s.getSoTienGdHoanTraTruyThu());
				if(loaiGD.equals("GDRTIM")) {
					item.getItemProperty("phiRtm").setValue(s.getPhiRtmHoanTraTruyThu());
					item.getItemProperty("vatPhiRtm").setValue(s.getVatPhiRtmHoanTraTruyThu());
				}
				item.getItemProperty("ngayHeThongRelease").setValue("");
				
				
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
