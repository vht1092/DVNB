package com.dvnb.components.dvnb;

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
import com.dvnb.entities.DvnbSummary;
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
public class DataGridBaoCaoDVNB extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridBaoCaoDVNB.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	
	private SpringConfigurationValueHelper configurationHelper;
	protected DataSource localDataSource;
	
	public Page<DvnbSummary> dataSource;

	@SuppressWarnings("unchecked")
	public DataGridBaoCaoDVNB() {

		setSizeFull();

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();

		// init grid
		grid = new Grid();
		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(20);
		grid.setHeightMode(HeightMode.ROW);
		
		container = new IndexedContainer();
		
//		container.addContainerProperty("ky", String.class, "");
		container.addContainerProperty("maDonVi", String.class, "");
		container.addContainerProperty("maDriver", String.class, "");
		container.addContainerProperty("maSanPham", String.class, "");
		container.addContainerProperty("maPhongBan", String.class, "");
		container.addContainerProperty("maLoaiKH", String.class, "");
		container.addContainerProperty("maHoatDong", String.class, "");
		container.addContainerProperty("pcmcsReserve2Id", String.class, "");
		container.addContainerProperty("soTienSoLuong", Double.class, "");
		container.addContainerProperty("typeDesc", String.class, "");
		
		grid.setContainerDataSource(container);

//		grid.getColumn("ky").setHeaderCaption("KỲ");
		grid.getColumn("maDonVi").setHeaderCaption("MA_DON_VI");
		grid.getColumn("maDriver").setHeaderCaption("MA_DRIVER");
		grid.getColumn("maSanPham").setHeaderCaption("MA_SP");
		grid.getColumn("maPhongBan").setHeaderCaption("MA_PB");
		grid.getColumn("maLoaiKH").setHeaderCaption("MA_LKH");
		grid.getColumn("maHoatDong").setHeaderCaption("MA_HOAT_DONG");
		grid.getColumn("pcmcsReserve2Id").setHeaderCaption("PCMCS_RESERVE2_ID");
		grid.getColumn("soTienSoLuong").setHeaderCaption("SO_TIEN/SL");
		grid.getColumn("typeDesc").setHeaderCaption("TYPE");
		
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
	@SuppressWarnings("unchecked")
	private boolean createDataForContainer() {
		boolean flag = false;
		
		if (dataSource != null && dataSource.getTotalElements()>0) {
			container.removeAllItems();
			dataSource.forEach(s -> {
				Item item = container.getItem(container.addItem());
				
//				item.getItemProperty("ky").setValue(s.getKy());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maDriver").setValue(s.getMaDriver());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
				item.getItemProperty("maLoaiKH").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maHoatDong").setValue(s.getMaHoatDong());
				item.getItemProperty("pcmcsReserve2Id").setValue(s.getPcmcsReserve2Id());
				item.getItemProperty("soTienSoLuong").setValue(Double.parseDouble(s.getSoTienSoLuong()));
				item.getItemProperty("typeDesc").setValue(s.getType());
				
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
	
	

}
