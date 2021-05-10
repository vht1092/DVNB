package com.dvnb.components.pbcp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.dvnb.entities.DvnbSummaryPbcp;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.DvnbSummaryPbcpService;
import com.dvnb.services.TyGiaService;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


@SpringComponent
@ViewScope
public class SummaryByDriver extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SummaryByDriver.class);
	private final transient DvnbSummaryPbcpService dvnbSummaryPbcpService;
	private TyGiaService tyGiaService;
	private SpringConfigurationValueHelper configurationHelper;
	public static final String CAPTION = "DỮ LIỆU THEO DRIVER";
	private DataGridDataDriverComponent grid;
	private static final String SHOW = "HIỂN THỊ";
	private static final String CARD_BRN = "LOẠI THẺ";
	private static final String TYPE = "TYPE";
	private static final String INPUT_FIELD = "Vui lòng chọn giá trị";
	private static final String VIEW = "VIEW";
	private static final String SAVE = "SAVE";
	private static final String EXPORT = "EXPORT";
	private static final String KY_MOI = "KỲ MỚI";
//	private static final String KY = "KỲ";
//	private static final String TYGIA = "TỶ GIÁ";

	private transient String sUserId;
	private transient Page<DvnbSummaryPbcp> result;
	private final ComboBox cbbCardBrn;
	final Button btView = new Button(VIEW);
	final Button btExport = new Button(EXPORT);
	
//	private final ComboBox cbbKyMoi;
//	private TextField tfTyGia;
	private final ComboBox cbbTuKy;
	private final ComboBox cbbDenKy;
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
	
	List<DvnbSummaryPbcp> listDataDriver = new ArrayList<DvnbSummaryPbcp>();
	protected DataSource localDataSource;
	private final VerticalLayout mainLayout = new VerticalLayout();
	
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	private int i;
	private int rowCount;
	public SummaryByDriver() {
		final VerticalLayout mainLayout = new VerticalLayout();
		final HorizontalLayout formLayout1st = new HorizontalLayout();
		formLayout1st.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		dvnbSummaryPbcpService = (DvnbSummaryPbcpService) helper.getBean("dvnbSummaryPbcpService");
		localDataSource = (DataSource) helper.getBean("dataSource");
		tyGiaService = (TyGiaService) helper.getBean("tyGiaService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		this.sUserId = SecurityUtils.getUserId();
		grid = new DataGridDataDriverComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		final Label lbCardBrn = new Label(CARD_BRN);
		cbbCardBrn = new ComboBox();
		cbbCardBrn.setNullSelectionAllowed(false);
		cbbCardBrn.addItems("MC","VS");
		
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		final Label lbTuKy = new Label("TỪ KỲ");
		cbbTuKy = new ComboBox();
		cbbTuKy.setNullSelectionAllowed(false);
		cbbTuKy.setPageLength(12);
		
		final Label lbDenKy = new Label("ĐẾN KỲ");
		cbbDenKy = new ComboBox();
		cbbDenKy.setNullSelectionAllowed(false);
		cbbDenKy.setPageLength(12);
		descService.findAllByTypeByOrderBySequencenoDesc("KYBAOCAO").forEach(item -> {
			cbbTuKy.addItem(item.getId());
			cbbTuKy.setItemCaption(item.getId(),item.getDescription());
			cbbDenKy.addItem(item.getId());
			cbbDenKy.setItemCaption(item.getId(),item.getDescription());
		});
		cbbTuKy.setValue("01" + String.valueOf(cal.get(Calendar.YEAR)));
		cbbDenKy.setValue("12" + String.valueOf(cal.get(Calendar.YEAR)));
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(120.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		
		btExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btExport.setWidth(120.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.DOWNLOAD);
		
		
		
		btView.addClickListener(event -> {
			if(!cbbCardBrn.isEmpty() && !cbbTuKy.isEmpty() && !cbbDenKy.isEmpty()) {
				cardBrn = cbbCardBrn.getValue().toString();
//	        	SHOW DATA IN GRID
				grid.dataSource = null;
				
				grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
				
				grid.initGrid("All");
				grid.refreshData();
				
				// Refresh paging button
				mainLayout.removeComponent(pagingLayout);
				pagingLayout = generatePagingLayout();
				pagingLayout.setSpacing(true);
				mainLayout.addComponent(pagingLayout);
				mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
			}
			
		});
		
		btExport.addClickListener(event -> {
			
			if(listDataDriver.size()>0 && !cbbCardBrn.isEmpty() && !cbbTuKy.isEmpty() && !cbbDenKy.isEmpty()) {
				String cardBrn = cbbCardBrn.getValue().toString();
				String tuky = cbbTuKy.getValue().toString();
				String denky = cbbDenKy.getValue().toString();
				exportDataDriver(listDataDriver,cardBrn,tuky,denky);
			}
			
		});
		

		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		
		formLayout1st.addComponent(lbTuKy);
		formLayout1st.addComponent(cbbTuKy);
		formLayout1st.addComponent(lbDenKy);
		formLayout1st.addComponent(cbbDenKy);
		formLayout1st.addComponent(lbCardBrn);
		formLayout1st.addComponent(cbbCardBrn);
		formLayout1st.setComponentAlignment(lbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbTuKy, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbTuKy, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbDenKy, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbDenKy, Alignment.MIDDLE_LEFT);
		
		
		formLayout2nd.addComponent(btView);
		formLayout2nd.addComponent(btExport);
		formLayout2nd.setComponentAlignment(btExport, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btView, Alignment.MIDDLE_LEFT);
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(formLayout);
		
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		grid = new DataGridDataDriverComponent();
		mainLayout.addComponent(grid);
		
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		
		setCompositionRoot(mainLayout);
	}
	
	private Page<DvnbSummaryPbcp> getData(Pageable page) {
		
		try {
			if(!cbbTuKy.isEmpty() && !cbbDenKy.isEmpty() && !cbbCardBrn.isEmpty()) {
				String tuky = cbbTuKy.getValue().toString();
				String denky = cbbDenKy.getValue().toString();
				String cardbrn = cbbCardBrn.getValue().toString();
				listDataDriver = dvnbSummaryPbcpService.searchAllDataByDriverAndKyAndCardbrn(tuky, denky, cardbrn);
				result = new PageImpl<>(listDataDriver);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
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
		
		if(result!=null) {
			btPreviousPage.setEnabled(result.hasPrevious());
			btNextPage.setEnabled(result.hasNext());
		}
		

		btNextPage.addClickListener(evt -> {
			grid.dataSource = getData(result.nextPageable());
			grid.refreshData();
			btNextPage.setEnabled(result.hasNext());
			btPreviousPage.setEnabled(result.hasPrevious());
			
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
				}
			});

		});

		btPreviousPage.addClickListener(evt -> {
			grid.dataSource = getData(result.previousPageable());
			grid.refreshData();
			btNextPage.setEnabled(result.hasNext());
			btPreviousPage.setEnabled(result.hasPrevious());
					
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
		if(result != null) {
			if (result.getSize() * (result.getNumber() + 1) >= result.getTotalElements()) {
				sNumberOfElements.append(result.getTotalElements());
			} else {
				sNumberOfElements.append(result.getSize() * (result.getNumber() + 1));
			}
			sTotalElements = Long.toString(result.getTotalElements());
			sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
		}
		return sLabelPaging;
	}
	
	@Override
	public void eventReload() {
		grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
		grid.refreshData();
		
		// Refresh paging button
		mainLayout.removeComponent(pagingLayout);
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
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
		layoutBtn.setSpacing(true);
		layoutBtn.addComponent(lbText);
        layoutBtn.addComponents(bOK);
        layoutBtn.setComponentAlignment(bOK, Alignment.BOTTOM_CENTER);
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
	                LOGGER.error(e.getMessage());
	            }
	              return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
	}
	
	private void exportDataDriver(List<DvnbSummaryPbcp> listdataPBCP,String cardBrn,String tuky,String denky) {
		//EXPORT LIST TO EXCEL FILE
        XSSFWorkbook workbookExport = new XSSFWorkbook();
        XSSFSheet sheetExport = workbookExport.createSheet();
        
        XSSFCellStyle cellStyle = workbookExport.createCellStyle();
        XSSFDataFormat xssfDataFormat = workbookExport.createDataFormat();
        cellStyle.setDataFormat(xssfDataFormat.getFormat("#,##0"));
        
        DataFormat format = workbookExport.createDataFormat();
        CellStyle styleNumber;
        styleNumber = workbookExport.createCellStyle();
        styleNumber.setDataFormat(format.getFormat("0.0"));
        
        rowNumExport = 0;
        LOGGER.info("Creating excel");

        if(rowNumExport == 0) {
        	Object[] rowHeader = new Object[] {"DRIVER_CODE", "DRIVER_DESCRIPTION", "DEBIT", "CREDIT", "KHCN", "KHDN"};

        	int colNum = 0;	 
        	XSSFRow row = sheetExport.createRow(rowNumExport++);         	
        	for (Object field : rowHeader) {
        		Cell cell = row.createCell(colNum++, CellType.STRING);
        		cell.setCellValue((String)field);
        	}      
        	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
        }
        
        try {
    		for(DvnbSummaryPbcp item : listdataPBCP) {
				XSSFRow row = sheetExport.createRow(rowNumExport++);
				
				row.createCell(0).setCellValue(item.getDriverCode());
				row.createCell(1).setCellValue(item.getDriverDesc());
				
				Cell cellTotalDebit = row.createCell(2,CellType.NUMERIC);
				cellTotalDebit.setCellValue(item.getTotalDebit().doubleValue());
				cellTotalDebit.setCellStyle(cellStyle);
				
				Cell cellTotalCredit = row.createCell(3,CellType.NUMERIC);
				cellTotalCredit.setCellValue(item.getTotalCredit().doubleValue());
				cellTotalCredit.setCellStyle(cellStyle);
				
				Cell cellTotalKhcn = row.createCell(4,CellType.NUMERIC);
				cellTotalKhcn.setCellValue(item.getTotalKhcn().doubleValue());
				cellTotalKhcn.setCellStyle(cellStyle);
				
				Cell cellTotalKhdn = row.createCell(5,CellType.NUMERIC);
				cellTotalKhdn.setCellValue(item.getTotalKhdn().doubleValue());
				cellTotalKhdn.setCellStyle(cellStyle);
				
	        }
        
	        sheetExport.createFreezePane(0, 1);
	        
	        for (int i=0; i<6; i++)  
	        	sheetExport.autoSizeColumn(i);
        
        	fileNameOutput = "DATA_DRIVER_" + cardBrn + "_" + tuky + "_" + denky + ".xlsx";
        	pathExport = Paths.get(configurationHelper.getPathFileRoot() + "\\Export");
        	if(Files.notExists(pathExport)) {
        		Files.createDirectories(pathExport);
            }
        	FileOutputStream outputStream = new FileOutputStream(pathExport + "\\" + fileNameOutput);
            LOGGER.info("Created file excel output " + fileNameOutput);
            workbookExport.write(outputStream);
            LOGGER.info("Write data to " + fileNameOutput + " completed");
            workbookExport.close();
            outputStream.close();
	        LOGGER.info("Export excel file " + fileNameOutput);
	        messageExportXLSX("Info","Export completed.");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
	}
	

}
