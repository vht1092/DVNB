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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.TyGiaService;
import com.monitorjbl.xlsx.StreamingReader;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
public class EnquirytInvoice extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(EnquirytInvoice.class);
	private final transient DvnbInvoiceMcService dvnbInvoiceMcService;
	private final transient DvnbInvoiceVsService dvnbInvoiceVsService;
	private TyGiaService tyGiaService;
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "TRUY VẤN INVOICE";
	private DataGridInvoiceComponent grid;
	private static final String SHOW = "HIỂN THỊ";
	private static final String CARD_BRN = "LOẠI THẺ";
	private static final String TYPE = "TYPE";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String VIEW = "XEM";
	private static final String EXPORT = "XLSX";
//	private static final String KY = "KỲ";
//	private static final String TYGIA = "TỶ GIÁ";

	private transient String sUserId;
	private transient Page<DvnbInvoiceMc> resultMC;
	private transient Page<DvnbInvoiceVs> resultVS;
	private final ComboBox cbbCardBrn;
	private final ComboBox cbbType;
	final Button btView = new Button(VIEW);
	final Button btExport = new Button(EXPORT);
//	private final ComboBox cbbKy;
//	private TextField tfTyGia;
	private DateField dffromDate;
	private DateField dftoDate;
	String fileNameImport;
	private Window confirmDialog = new Window();
	private Button bOK = new Button("OK");
	private String cardBrn = "";
//	private String ketchuyenFlag;
	public String filename;
	
	// Paging
	private final static int SIZE_OF_PAGE = 50;
	private final static int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	List<DvnbInvoiceMc> dvnbInvoiceMcList;
	List<DvnbInvoiceVs> dvnbInvoiceVsList;
	protected DataSource localDataSource;
	private final VerticalLayout mainLayout = new VerticalLayout();
	
	private int i;
	
	public EnquirytInvoice() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		dvnbInvoiceMcService = (DvnbInvoiceMcService) helper.getBean("dvnbInvoiceMcService");
		dvnbInvoiceVsService = (DvnbInvoiceVsService) helper.getBean("dvnbInvoiceVsService");
		localDataSource = (DataSource) helper.getBean("dataSource");
		tyGiaService = (TyGiaService) helper.getBean("tyGiaService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridInvoiceComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final Label lbCardBrn = new Label(CARD_BRN);
		cbbCardBrn = new ComboBox();
		cbbCardBrn.setNullSelectionAllowed(false);
		cbbCardBrn.addItems("MC","VS");

		final Label lbfromDate = new Label("Từ ngày");
		dffromDate = new DateField();
		dffromDate.setDateFormat("dd/MM/yyyy");
		dffromDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dffromDate.setValidationVisible(false);
		
		final Label lbtoDate = new Label("Đến ngày");
		dftoDate = new DateField();
		dftoDate.setDateFormat("dd/MM/yyyy");
		dftoDate.addValidator(new NullValidator(INPUT_FIELD, false));
		dftoDate.setValidationVisible(false);
		
		final Label lbType = new Label(TYPE);
		cbbType = new ComboBox();
		cbbType.setNullSelectionAllowed(false);
		cbbType.addItems("All","Y","N");
		cbbType.setItemCaption("Y", "Đã kết chuyển");
		cbbType.setItemCaption("N", "Không kết chuyển");
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(120.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		
		btExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btExport.setWidth(120.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.DOWNLOAD);
		
//		pagingLayout = generatePagingLayout();	
//		pagingLayout.setSpacing(true);
		
//		final Button btPaging = new Button();
//		btPaging.setCaption(reloadLabelPaging());
//		btPaging.setEnabled(false);
		
		btView.addClickListener(event -> {
			cardBrn = cbbCardBrn.getValue().toString();
//        	SHOW DATA IN GRID
			grid.dataSourceMC = null;
			grid.dataSourceVS = null;
			
			switch(cardBrn) {
				case "MC":
					grid.dataSourceMC = getDataMC(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					break;
				case "VS":
					grid.dataSourceVS = getDataVS(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					break;
			}
			grid.initGrid(cbbCardBrn.getValue().toString(),"", "All");
			
			grid.btKetChuyen.setVisible(false);
			grid.btKhongKetChuyen.setVisible(false);
			grid.btExportUpload.setVisible(false);
			
//			grid.refreshData();
			// Refresh paging button
			mainLayout.removeComponent(pagingLayout);
			pagingLayout = generatePagingLayout();
			pagingLayout.setSpacing(true);
			mainLayout.addComponent(pagingLayout);
			mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
		});
		
		btExport.addClickListener(event -> {
//			ketchuyenFlag = "Y";
			switch(cardBrn)
			{
				case "MC":
					filename = "ReportInvoiceMC.jasper";
					break;
				case "VS":
					filename = "ReportInvoiceVS.jasper";
					break;
			}
				
			SimpleFileDownloader downloader = new SimpleFileDownloader();
			addExtension(downloader);
			StreamResource myResourceXLSX = createTransMKResourceXLS();
			
			downloader.setFileDownloadResource(myResourceXLSX);
			downloader.download();
		});

		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		
		formLayout1st.addComponent(lbCardBrn);
		formLayout1st.addComponent(cbbCardBrn);
		formLayout1st.addComponent(lbfromDate);
		formLayout1st.addComponent(dffromDate);
		formLayout1st.addComponent(lbtoDate);
		formLayout1st.addComponent(dftoDate);
		formLayout1st.addComponent(lbType);
		formLayout1st.addComponent(cbbType);
		formLayout1st.setComponentAlignment(lbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbfromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dffromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbtoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dftoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbType, Alignment.MIDDLE_LEFT);
		
		formLayout2nd.addComponent(btView);
		formLayout2nd.addComponent(btExport);
		formLayout2nd.setComponentAlignment(btView, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btExport, Alignment.MIDDLE_CENTER);
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_CENTER);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(formLayout);
		
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		grid = new DataGridInvoiceComponent();
		mainLayout.addComponent(grid);
		
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	
	private Page<DvnbInvoiceMc> getDataMC(Pageable page) {
		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
	    resultMC = dvnbInvoiceMcService.findAllByNgayHoaDon(tungay, denngay, cbbType.getValue().toString(), page);
		return resultMC;
	}
	
	private Page<DvnbInvoiceVs> getDataVS(Pageable page) {
		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
		resultVS = dvnbInvoiceVsService.findAllByNgayHoaDon(tungay, denngay, cbbType.getValue().toString(), page);
		return resultVS;
		
	}

	private HorizontalLayout generatePagingLayout() {
		final Button btPaging = new Button();
		btPaging.setCaption(reloadLabelPaging());
		btPaging.setEnabled(false);

		final Button btPreviousPage = new Button("Trang trước");
		btPreviousPage.setIcon(FontAwesome.ANGLE_LEFT);
		

		final Button btNextPage = new Button("Trang sau");
		btNextPage.setStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		btNextPage.setIcon(FontAwesome.ANGLE_RIGHT);
		
		switch(cardBrn) {
			case "MC":
				btPreviousPage.setEnabled(resultMC.hasPrevious());
				btNextPage.setEnabled(resultMC.hasNext());
				break;
			case "VS":
				btPreviousPage.setEnabled(resultVS.hasPrevious());
				btNextPage.setEnabled(resultVS.hasNext());
				break;
		}

		btNextPage.addClickListener(evt -> {
			switch(cardBrn) {
				case "MC":
					
					grid.dataSourceMC = getDataMC(resultMC.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultMC.hasNext());
					btPreviousPage.setEnabled(resultMC.hasPrevious());
					break;
				case "VS":
					
					grid.dataSourceVS = getDataVS(resultVS.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultVS.hasNext());
					btPreviousPage.setEnabled(resultVS.hasPrevious());
					break;
			}
			
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
				}
			});

		});

		btPreviousPage.addClickListener(evt -> {
			switch(cardBrn) {
				case "MC":
					grid.dataSourceMC = getDataMC(resultMC.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultMC.hasNext());
					btPreviousPage.setEnabled(resultMC.hasPrevious());
					break;
				case "VS":
					grid.dataSourceVS = getDataVS(resultVS.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultVS.hasNext());
					btPreviousPage.setEnabled(resultVS.hasPrevious());
					break;
			}
					
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
				}
			});
		});

		final HorizontalLayout pagingLayout = new HorizontalLayout();
		pagingLayout.setSizeUndefined();
		pagingLayout.setSpacing(true);
		pagingLayout.addComponent(btPaging);
		pagingLayout.addComponent(btPreviousPage);
		pagingLayout.addComponent(btNextPage);
		pagingLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

		return pagingLayout;
	}

	private String reloadLabelPaging() {
		final StringBuilder sNumberOfElements = new StringBuilder();
		String sTotalElements = null;
		String sLabelPaging = "";
		switch(cardBrn) {
			case "MC":
				if(resultMC != null) {
					if (resultMC.getSize() * (resultMC.getNumber() + 1) >= resultMC.getTotalElements()) {
						sNumberOfElements.append(resultMC.getTotalElements());
					} else {
						sNumberOfElements.append(resultMC.getSize() * (resultMC.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultMC.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "VS":
				if(resultVS != null) {
					if (resultVS.getSize() * (resultVS.getNumber() + 1) >= resultVS.getTotalElements()) {
						sNumberOfElements.append(resultVS.getTotalElements());
					} else {
						sNumberOfElements.append(resultVS.getSize() * (resultVS.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultVS.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
		}
		return sLabelPaging;
	}
	
	@Override
	public void eventReload() {
		switch(cardBrn) {
			case "MC":
				if(resultMC != null)
				{
					grid.dataSourceMC = getDataMC(new PageRequest(resultMC.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.refreshData();
				}
				break;
			case "VS":
				if(resultVS != null)
				{
					grid.dataSourceVS = getDataVS(new PageRequest(resultVS.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
					grid.refreshData();
				}
				break;
		}
		
		// Refresh paging button
		mainLayout.removeComponent(pagingLayout);
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
	}
	
	@SuppressWarnings("serial")
	private StreamResource createTransMKResourceXLS() {
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
	
	public Map<String, Object> getParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
		parameters.put("p_ky", "All");
		parameters.put("p_ketchuyen", cbbType.getValue().toString());
		if(!cbbType.getValue().equals("All"))
			parameters.put("p_ketchuyen_status", cbbType.getValue().equals("Y") ? "KẾT CHUYỂN VỀ ĐƠN VỊ" : "KHÔNG KẾT CHUYỂN VỀ ĐƠN VỊ");
		else
			parameters.put("p_ketchuyen_status", "");
		parameters.put("p_tungay", tungay);
		parameters.put("p_denngay", denngay);

		return parameters;
	}

}
