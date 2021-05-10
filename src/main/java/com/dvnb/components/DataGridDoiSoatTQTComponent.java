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

import org.apache.commons.lang.StringUtils;
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
import com.vaadin.ui.Grid.HeaderRow;
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
public class DataGridDoiSoatTQTComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridDoiSoatTQTComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	private String ketchuyenFlag;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	private SpringConfigurationValueHelper configurationHelper;
	public String filename;
	protected DataSource localDataSource;
	
	public Page<DoiSoatData> dataSource;

	FooterRow footer;
	HeaderRow header;
	int stt=0;
	
	private DoiSoatData doiSoatTotal = new DoiSoatData();
	
	@SuppressWarnings("unchecked")
	public DataGridDoiSoatTQTComponent() {

		setSizeFull();

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
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
//		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(15);
//		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
//		grid.setSelectionMode(SelectionMode.MULTI);
//		grid.setEditorEnabled(true);
		grid.recalculateColumnWidths();
		
		container = new IndexedContainer();
		
		container.addContainerProperty("stt", Integer.class, "");
		container.addContainerProperty("tenChuThe", String.class, "");
		container.addContainerProperty("casa", String.class, "");
		container.addContainerProperty("soThe", String.class, "");
		container.addContainerProperty("maGd", String.class, "");
		container.addContainerProperty("ngayGd", String.class, "");
		container.addContainerProperty("ngayFileIncoming", String.class, "");
		container.addContainerProperty("stGd", BigDecimal.class, "");
		container.addContainerProperty("stTqt", BigDecimal.class, "");
		container.addContainerProperty("stQdVnd", BigDecimal.class, "");
		container.addContainerProperty("ltgd", String.class, "");
		container.addContainerProperty("lttqt", String.class, "");
		container.addContainerProperty("interchange", BigDecimal.class, "");
		container.addContainerProperty("maCapPhep", String.class, "");
		container.addContainerProperty("dvcnt", String.class, "");
		container.addContainerProperty("reversalInd", String.class, "");
		container.addContainerProperty("issuerCharge", String.class, "");
		container.addContainerProperty("merchantCity", String.class, "");
		container.addContainerProperty("stTrichNoKhGd", BigDecimal.class, "");
		container.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, "");
		container.addContainerProperty("loaiTienNguyenTeGd", String.class, "");
		container.addContainerProperty("phiIsaGd", BigDecimal.class, "");
		container.addContainerProperty("phiRtmGd", BigDecimal.class, "");
		container.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, "");
		container.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, "");
		container.addContainerProperty("tyGiaTrichNo", BigDecimal.class, "");
		container.addContainerProperty("soTienGdHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("phiIsaHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("vatPhiIsaHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("phiRtmHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("vatPhiRtmHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("tongPhiVatHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("tongHoanTraTruyThu", BigDecimal.class, "");
		container.addContainerProperty("phiXuLyGd", BigDecimal.class, "");
		container.addContainerProperty("dvpht", String.class, "");
		container.addContainerProperty("trace", String.class, "");
		container.addContainerProperty("statusCW", String.class, "");
		container.addContainerProperty("mcc", String.class, "");
		
		grid.setContainerDataSource(container);
		grid.getColumn("stt").setHeaderCaption("STT");
		grid.getColumn("tenChuThe").setHeaderCaption("Tên");
		grid.getColumn("casa").setHeaderCaption("CASA");
		grid.getColumn("soThe").setHeaderCaption("Số thẻ");
		grid.getColumn("maGd").setHeaderCaption("Mã GD");
		grid.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		grid.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		grid.getColumn("stGd").setHeaderCaption("ST GD");
		grid.getColumn("stTqt").setHeaderCaption("ST TQT");
		grid.getColumn("stQdVnd").setHeaderCaption("ST QD");
		grid.getColumn("ltgd").setHeaderCaption("LDGD");
		grid.getColumn("lttqt").setHeaderCaption("LTTQT");
		grid.getColumn("interchange").setHeaderCaption("Interchange");
		grid.getColumn("maCapPhep").setHeaderCaption("Mã");
		grid.getColumn("dvcnt").setHeaderCaption("ĐVCNT");
		grid.getColumn("reversalInd").setHeaderCaption("Reversal");
		grid.getColumn("issuerCharge").setHeaderCaption("Issuer");
		grid.getColumn("merchantCity").setHeaderCaption("Merchant");
		grid.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH");
		grid.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ");
		grid.getColumn("loaiTienNguyenTeGd").setHeaderCaption("Loại tiền nguyên tệ");
		grid.getColumn("phiIsaGd").setHeaderCaption("Phí ISA");
		grid.getColumn("phiRtmGd").setHeaderCaption("Phí RTM");
		grid.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ");
		grid.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch");
		grid.getColumn("tyGiaTrichNo").setHeaderCaption("Tỷ giá tại");
		grid.getColumn("soTienGdHoanTraTruyThu").setHeaderCaption("Số tiền GD");
		grid.getColumn("phiIsaHoanTraTruyThu").setHeaderCaption("Phí ISA");
		grid.getColumn("vatPhiIsaHoanTraTruyThu").setHeaderCaption("VAT phí ISA");
		grid.getColumn("phiRtmHoanTraTruyThu").setHeaderCaption("Phí RTM");
		grid.getColumn("vatPhiRtmHoanTraTruyThu").setHeaderCaption("VAT Phí RTM");
		grid.getColumn("tongPhiVatHoanTraTruyThu").setHeaderCaption("Tổng phí + VAT");
		grid.getColumn("tongHoanTraTruyThu").setHeaderCaption("Tổng hoàn trả");
		grid.getColumn("phiXuLyGd").setHeaderCaption("Phí xử");
		grid.getColumn("dvpht").setHeaderCaption("ĐVPHT");
		grid.getColumn("trace").setHeaderCaption("Trace");
		grid.getColumn("statusCW").setHeaderCaption("Status");
		grid.getColumn("mcc").setHeaderCaption("MCC");
		
		header = grid.appendHeaderRow();
		header.getCell("tenChuThe").setText("chủ thẻ");
		header.getCell("stQdVnd").setText("VND");
		header.getCell("maCapPhep").setText("cấp phép");
		header.getCell("reversalInd").setText("Indicator");
		header.getCell("issuerCharge").setText("charge");
		header.getCell("merchantCity").setText("City");
		header.getCell("stTrichNoKhGd").setText("thời điểm GD");
		header.getCell("stgdNguyenTeGd").setText("thời điểm GD");
		header.getCell("loaiTienNguyenTeGd").setText("thời điểm GD");
		header.getCell("phiIsaGd").setText("thời điểm GD");
		header.getCell("phiRtmGd").setText("thời điểm GD");
		header.getCell("stgdNguyenTeChenhLech").setText("chênh lệch");
		header.getCell("stgdChenhLechDoTyGia").setText("do tỷ giá");
		header.getCell("tyGiaTrichNo").setText("thời điểm trích nợ");
		header.getCell("soTienGdHoanTraTruyThu").setText("hoàn trả/truy thu");
		header.getCell("phiIsaHoanTraTruyThu").setText("hoàn trả/truy thu");
		header.getCell("vatPhiIsaHoanTraTruyThu").setText("hoàn trả/truy thu");
		header.getCell("phiRtmHoanTraTruyThu").setText("hoàn trả/truy thu");
		header.getCell("vatPhiRtmHoanTraTruyThu").setText("hoàn trả/truy thu");
		header.getCell("tongPhiVatHoanTraTruyThu").setText("hoàn trả/truy thu");
		header.getCell("tongHoanTraTruyThu").setText("/truy thu");
		header.getCell("phiXuLyGd").setText("lý GD");
		header.getCell("statusCW").setText("trên CW");
		
		footer = grid.prependFooterRow();
		
		grid.setCellStyleGenerator(cell -> {
			BigDecimal numStGd;
			BigDecimal numStTqt;
			BigDecimal numStQdVnd;
			BigDecimal numInterchange;
			BigDecimal numStTrichNoKhGd;
			BigDecimal numStgdNguyenTeGd;
			BigDecimal numPhiIsaGd;
			BigDecimal numPhiRtmGd;
			BigDecimal numStgdNguyenTeChenhLech;
			BigDecimal numStgdChenhLechDoTyGia;
			BigDecimal numTyGiaTrichNo;
			BigDecimal numSoTienGdHoanTraTruyThu;
			BigDecimal numPhiIsaHoanTraTruyThu;
			BigDecimal numVatPhiIsaHoanTraTruyThu;
			BigDecimal numPhiRtmHoanTraTruyThu;
			BigDecimal numVatPhiRtmHoanTraTruyThu;
			BigDecimal numTongPhiVatHoanTraTruyThu;
			BigDecimal numTongHoanTraTruyThu;
			BigDecimal numPhiXuLyGd;
			
			if (cell.getPropertyId().equals("stGd")) {
				numStGd = cell.getItem().getItemProperty("stGd").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stGd").getValue().toString());
				if(numStGd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("stTqt")) {
				numStTqt = cell.getItem().getItemProperty("stTqt").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stTqt").getValue().toString());
				if(numStTqt.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("stQdVnd")) {
				numStQdVnd = cell.getItem().getItemProperty("stQdVnd").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stQdVnd").getValue().toString());
				if(numStQdVnd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("interchange")) {
				numInterchange = cell.getItem().getItemProperty("interchange").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("interchange").getValue().toString());
				if(numInterchange.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("stTrichNoKhGd")) {
				numStTrichNoKhGd = cell.getItem().getItemProperty("stTrichNoKhGd").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stTrichNoKhGd").getValue().toString());
				if(numStTrichNoKhGd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("stgdNguyenTeGd")) {
				numStgdNguyenTeGd = cell.getItem().getItemProperty("stgdNguyenTeGd").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stgdNguyenTeGd").getValue().toString());
				if(numStgdNguyenTeGd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("phiIsaGd")) {
				numPhiIsaGd = cell.getItem().getItemProperty("phiIsaGd").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("phiIsaGd").getValue().toString());
				if(numPhiIsaGd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("phiRtmGd")) {
				numPhiRtmGd = cell.getItem().getItemProperty("phiRtmGd").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("phiRtmGd").getValue().toString());
				if(numPhiRtmGd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("stgdNguyenTeChenhLech")) {
				numStgdNguyenTeChenhLech = cell.getItem().getItemProperty("stgdNguyenTeChenhLech").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stgdNguyenTeChenhLech").getValue().toString());
				if(numStgdNguyenTeChenhLech.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("stgdChenhLechDoTyGia")) {
				numStgdChenhLechDoTyGia = cell.getItem().getItemProperty("stgdChenhLechDoTyGia").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("stgdChenhLechDoTyGia").getValue().toString());
				if(numStgdChenhLechDoTyGia.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("tyGiaTrichNo")) {
				numTyGiaTrichNo = cell.getItem().getItemProperty("tyGiaTrichNo").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("tyGiaTrichNo").getValue().toString());
				if(numTyGiaTrichNo.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("soTienGdHoanTraTruyThu")) {
				numSoTienGdHoanTraTruyThu = cell.getItem().getItemProperty("soTienGdHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("soTienGdHoanTraTruyThu").getValue().toString());
				if(numSoTienGdHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("phiIsaHoanTraTruyThu")) {
				numPhiIsaHoanTraTruyThu = cell.getItem().getItemProperty("phiIsaHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("phiIsaHoanTraTruyThu").getValue().toString());
				if(numPhiIsaHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("vatPhiIsaHoanTraTruyThu")) {
				numVatPhiIsaHoanTraTruyThu = cell.getItem().getItemProperty("vatPhiIsaHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("vatPhiIsaHoanTraTruyThu").getValue().toString());
				if(numVatPhiIsaHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("phiRtmHoanTraTruyThu")) {
				numPhiRtmHoanTraTruyThu = cell.getItem().getItemProperty("phiRtmHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("phiRtmHoanTraTruyThu").getValue().toString());
				if(numPhiRtmHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("vatPhiRtmHoanTraTruyThu")) {
				numVatPhiRtmHoanTraTruyThu = cell.getItem().getItemProperty("vatPhiRtmHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("vatPhiRtmHoanTraTruyThu").getValue().toString());
				if(numVatPhiRtmHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("tongPhiVatHoanTraTruyThu")) {
				numTongPhiVatHoanTraTruyThu = cell.getItem().getItemProperty("tongPhiVatHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("tongPhiVatHoanTraTruyThu").getValue().toString());
				if(numTongPhiVatHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("tongHoanTraTruyThu")) {
				numTongHoanTraTruyThu = cell.getItem().getItemProperty("tongHoanTraTruyThu").getValue() == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("tongHoanTraTruyThu").getValue().toString());
				if(numTongHoanTraTruyThu.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("phiXuLyGd")) {
				numPhiXuLyGd = cell.getItem().getItemProperty("phiXuLyGd") == null ? BigDecimal.ZERO : new BigDecimal(cell.getItem().getItemProperty("phiXuLyGd").getValue().toString());
				if(numPhiXuLyGd.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			
			return "";
				
		});
		
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
			NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
			
			footer.getCell("stGd").setHtml("<div align='left' style='font-weight:bold'>Tổng cộng</div>");
	        footer.getCell("stTqt").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("stQdVnd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("interchange").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("stTrichNoKhGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("stgdNguyenTeGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("phiIsaGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getPhiIsaGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("phiRtmGd").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getPhiRtmGd).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("stgdNguyenTeChenhLech").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("stgdChenhLechDoTyGia").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("soTienGdHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getSoTienGdHoanTraTruyThu).reduce(BigDecimal::add).get())+"</div>");
	        footer.getCell("phiIsaHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getPhiIsaHoanTraTruyThu).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("vatPhiIsaHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getVatPhiIsaHoanTraTruyThu).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("phiRtmHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getPhiRtmHoanTraTruyThu).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("vatPhiRtmHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getVatPhiRtmHoanTraTruyThu).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("tongPhiVatHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getTongPhiVatHoanTraTruyThu).reduce(BigDecimal::add).get().doubleValue())+"</div>");
	        footer.getCell("tongHoanTraTruyThu").setHtml("<div align='right' style='font-weight:bold'>"+formatter.format(dataSource.getContent().stream().map(DoiSoatData::getTongHoanTraTruyThu).reduce(BigDecimal::add).get().doubleValue())+"</div>");
//	        footer.getCell("phiXuLyGd").setText(formatter.format(dataSource.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get().doubleValue()));
			if (!grid.isVisible()) {
				grid.setVisible(true);
				lbNoDataFound.setVisible(false);
			}
		}
		
        grid.recalculateColumnWidths();
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
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
				item.getItemProperty("merchantCity").setValue(s.getMerchantCity());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("loaiTienNguyenTeGd").setValue(s.getLoaiTienNguyenTeGd());
				item.getItemProperty("phiIsaGd").setValue(s.getPhiIsaGd());
				item.getItemProperty("phiRtmGd").setValue(s.getPhiRtmGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("tyGiaTrichNo").setValue(s.getTyGiaTrichNo());
				item.getItemProperty("soTienGdHoanTraTruyThu").setValue(s.getSoTienGdHoanTraTruyThu());
				item.getItemProperty("phiIsaHoanTraTruyThu").setValue(s.getPhiIsaHoanTraTruyThu());
				item.getItemProperty("vatPhiIsaHoanTraTruyThu").setValue(s.getVatPhiIsaHoanTraTruyThu());
				item.getItemProperty("phiRtmHoanTraTruyThu").setValue(s.getPhiRtmHoanTraTruyThu());
				item.getItemProperty("vatPhiRtmHoanTraTruyThu").setValue(s.getVatPhiRtmHoanTraTruyThu());
				item.getItemProperty("tongPhiVatHoanTraTruyThu").setValue(s.getTongPhiVatHoanTraTruyThu());
				item.getItemProperty("tongHoanTraTruyThu").setValue(s.getTongHoanTraTruyThu());
				item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				item.getItemProperty("mcc").setValue(s.getMcc());
				
			});
//			doiSoatTotal.setStTqt(dataSource.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get());
//			doiSoatTotal.setStQdVnd(dataSource.getContent().stream().map(DoiSoatData::getStQdVnd).reduce(BigDecimal::add).get());
//			doiSoatTotal.setInterchange(dataSource.getContent().stream().map(DoiSoatData::getInterchange).reduce(BigDecimal::add).get());
//			doiSoatTotal.setStTqt(dataSource.getContent().stream().map(DoiSoatData::getStTqt).reduce(BigDecimal::add).get());
//			doiSoatTotal.setStTrichNoKhGd(dataSource.getContent().stream().map(DoiSoatData::getStTrichNoKhGd).reduce(BigDecimal::add).get());
//			doiSoatTotal.setStgdNguyenTeGd(dataSource.getContent().stream().map(DoiSoatData::getStgdNguyenTeGd).reduce(BigDecimal::add).get());
////			doiSoatTotal.setPhiIsaGd(dataSource.getContent().stream().map(DoiSoatData::getPhiIsaGd).reduce(BigDecimal::add).get());
////			doiSoatTotal.setPhiRtmGd(dataSource.getContent().stream().map(DoiSoatData::getPhiRtmGd).reduce(BigDecimal::add).get());
//			doiSoatTotal.setStgdNguyenTeChenhLech(dataSource.getContent().stream().map(DoiSoatData::getStgdNguyenTeChenhLech).reduce(BigDecimal::add).get());
//			doiSoatTotal.setStgdChenhLechDoTyGia(dataSource.getContent().stream().map(DoiSoatData::getStgdChenhLechDoTyGia).reduce(BigDecimal::add).get());
//			doiSoatTotal.setSoTienGdHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getSoTienGdHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setPhiIsaHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getPhiIsaHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setVatPhiIsaHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getVatPhiIsaHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setPhiRtmHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getPhiRtmHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setVatPhiRtmHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getVatPhiRtmHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setTongPhiVatHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getTongPhiVatHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setTongHoanTraTruyThu(dataSource.getContent().stream().map(DoiSoatData::getTongHoanTraTruyThu).reduce(BigDecimal::add).get());
//			doiSoatTotal.setPhiXuLyGd(dataSource.getContent().stream().map(DoiSoatData::getPhiXuLyGd).reduce(BigDecimal::add).get());
			
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	private void messageExportXLSX(String caption, String text) {
		Window confirmDialog = new Window();
		FormLayout content = new FormLayout();
        content.setMargin(true);
		Button bOK = new Button("OK");
		Label lbText = new Label(text);
		confirmDialog.setCaption(caption);
		confirmDialog.setWidth(300.0f, Unit.PIXELS);
		
		 bOK.addClickListener(event -> {
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			StreamResource resource = getStream(new File(pathExport + "\\" + fileNameOutput));
			downloader.setFileDownloadResource(resource);
			downloader.download();
         	confirmDialog.close();
         });
		
		VerticalLayout layoutBtn = new VerticalLayout();
		layoutBtn.addComponent(lbText);
        layoutBtn.addComponents(bOK);
        content.addComponent(layoutBtn);
        
        confirmDialog.setContent(content);

        getUI().addWindow(confirmDialog);
        // Center it in the browser window
        confirmDialog.center();
        confirmDialog.setResizable(false);
	}
	
	private StreamResource getStream(File inputfile) {
	    
	    StreamResource.StreamSource source = new StreamResource.StreamSource() {

	        public InputStream getStream() {
	           
	            InputStream input=null;
	            try
	            {
	                input = new  FileInputStream(inputfile);
	            } 
	            catch (FileNotFoundException e)
	            {
	                e.printStackTrace();
	            }
	              return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
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
