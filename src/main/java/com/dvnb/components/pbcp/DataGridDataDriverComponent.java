package com.dvnb.components.pbcp;

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
import java.util.HashMap;
import java.util.List;
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
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbSummaryPbcp;
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
public class DataGridDataDriverComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridDataDriverComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	public Grid gridUnselect = new Grid();
	
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	
	private SpringConfigurationValueHelper configurationHelper;
	public String filename;
	protected DataSource localDataSource;
	
	public Page<DvnbSummaryPbcp> dataSource;
	
	List<InvoiceUpload> invoiceUploadSummary;
	List<InvoiceUpload> invoiceUploadDetail;
	
	@SuppressWarnings("unchecked")
	public DataGridDataDriverComponent() {

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
		grid.setHeightByRows(15);
		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
//		grid.setSelectionMode(SelectionMode.MULTI);
//		grid.setEditorEnabled(true);
		
		container = new IndexedContainer();
		
		
		
		grid.setCellStyleGenerator(cell -> {
			BigDecimal totalDebit;
			BigDecimal totalCredit;
			BigDecimal totalKhcn;
			BigDecimal totalKhdn;
			
			if (cell.getPropertyId().equals("totalDebit")) {
				totalDebit = cell.getItem().getItemProperty("totalDebit").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("totalDebit").getValue().toString());
				if(totalDebit.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("totalCredit")) {
				totalCredit = cell.getItem().getItemProperty("totalCredit").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("totalCredit").getValue().toString());
				if(totalCredit.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("totalKhcn")) {
				totalKhcn = cell.getItem().getItemProperty("totalKhcn").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("totalKhcn").getValue().toString());
				if(totalKhcn.compareTo(BigDecimal.ZERO) < 0) {
					return "v-align-right-color";
				}
				return "v-align-right";
			}
			if (cell.getPropertyId().equals("totalKhdn")) {
				totalKhdn = cell.getItem().getItemProperty("totalKhdn").getValue() == null ? BigDecimal.ZERO : convertStringToBigdecimal(cell.getItem().getItemProperty("totalKhdn").getValue().toString());
				if(totalKhdn.compareTo(BigDecimal.ZERO) < 0) {
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
		
		IndexedContainer containerTemp = new IndexedContainer();
		try {
			containerTemp = (IndexedContainer) container.clone();
			for (Object propertyId : containerTemp.getContainerPropertyIds()) {
			    container.removeContainerProperty(propertyId);
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		container.addContainerProperty("driverCode", String.class, "");
		container.addContainerProperty("driverDesc", String.class, "");
		container.addContainerProperty("totalDebit", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("totalCredit", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("totalKhcn", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("totalKhdn", BigDecimal.class, BigDecimal.ZERO);
		
		grid.setContainerDataSource(container);
		grid.getColumn("driverCode").setHeaderCaption("DRIVER CODE");
		grid.getColumn("driverDesc").setHeaderCaption("DRIVER DESCRIPTION");
		grid.getColumn("totalDebit").setHeaderCaption("DEBIT");
		grid.getColumn("totalCredit").setHeaderCaption("CREDIT");
		grid.getColumn("totalKhcn").setHeaderCaption("KHCN");
		grid.getColumn("totalKhdn").setHeaderCaption("KHDN");
				
		if (createDataForContainer() == false) {
			if (!lbNoDataFound.isVisible() && (dataSource != null)) {
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
		
		if (dataSource != null && dataSource.getTotalElements()>0) {
			container.removeAllItems();
			dataSource.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				item.getItemProperty("driverCode").setValue(s.getDriverCode());
				item.getItemProperty("driverDesc").setValue(s.getDriverDesc());
				item.getItemProperty("totalDebit").setValue(s.getTotalDebit());
				item.getItemProperty("totalCredit").setValue(s.getTotalCredit());
				item.getItemProperty("totalKhcn").setValue(s.getTotalKhcn());
				item.getItemProperty("totalKhdn").setValue(s.getTotalKhdn());
			});
			flag = true;
		} 
		
		return flag;
	}
	
	
	private String formatNumberColor(BigDecimal number) {
		if (number.compareTo(BigDecimal.ZERO) < 0) {
			return "<span style=\"padding:7px 0px; background-color: #FFFF00\">" + number + "</span>";

		} else
			return String.valueOf(number);
	}
	
	
	
	private BigDecimal convertStringToBigdecimal(String stringValue) {
		stringValue = stringValue.replaceAll(",", "");
		if (stringValue.startsWith("(") && stringValue.endsWith(")"))
		{
		   return new BigDecimal(stringValue.substring(1, stringValue.length() - 1)).negate();
		}
		else
		{
		   return new BigDecimal(stringValue);
		}
	}
	

}
