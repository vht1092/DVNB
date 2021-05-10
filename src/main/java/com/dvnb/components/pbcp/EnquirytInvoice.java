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
import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbInvoiceMcService;
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
import com.vaadin.ui.Notification.Type;
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
	private DataGridInvoiceTreoComponent grid;
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
	private transient Page<DvnbInvoiceMc> resultMC;
	private transient Page<DvnbInvoiceVs> resultVS;
	private final ComboBox cbbCardBrn;
	private final ComboBox cbbType;
	private final ComboBox cbbKyMoi;
	final Button btSave = new Button(SAVE);
	final Button btView = new Button(VIEW);
	final Button btExport = new Button(EXPORT);
	
//	private final ComboBox cbbKyMoi;
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
	
	private int rowNumExport = 0;
	private String fileNameOutput = null;
	private Path pathExport = null;
	
	private int i;
	private int rowCount;
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
		grid = new DataGridInvoiceTreoComponent();
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
		
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		final Label lbKyMoi = new Label(KY_MOI);
		cbbKyMoi = new ComboBox();
		cbbKyMoi.setNullSelectionAllowed(false);
		cbbKyMoi.setPageLength(12);
		descService.findAllByTypeByOrderBySequencenoDesc("KYBAOCAO").forEach(item -> {
			cbbKyMoi.addItem(item.getId());
			cbbKyMoi.setItemCaption(item.getId(),item.getDescription());
		});
		cbbKyMoi.setValue(String.valueOf(cal.get(Calendar.MONTH) + 1) + String.valueOf(cal.get(Calendar.YEAR)));
		
		btSave.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btSave.setWidth(120.0f, Unit.PIXELS);
		btSave.setIcon(FontAwesome.EYE);
		
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
		
		btSave.addClickListener(event -> {
			try {
				if(!cbbKyMoi.isEmpty() && !cbbCardBrn.isEmpty()) {
					String cardbrn = cbbCardBrn.getValue().toString();
					String kyMoi = cbbKyMoi.getValue().toString();
					rowCount=0;
					
					grid.grid.getSelectedRows().forEach(item -> {
						if(grid.grid.getContainerDataSource().getItemIds().contains(item)) {
							rowCount++;
							String id = grid.grid.getContainerDataSource().getContainerProperty(item, "id").getValue().toString();
							String ketChuyen = grid.grid.getContainerDataSource().getContainerProperty(item, "ketChuyen").getValue().toString();
							String ngayThucHien = grid.grid.getContainerDataSource().getContainerProperty(item, "ngayThucHien").getValue().toString();
							String kyMoiGrid = grid.grid.getContainerDataSource().getContainerProperty(item, "kyMoi").getValue()==null ? null : grid.grid.getContainerDataSource().getContainerProperty(item, "kyMoi").getValue().toString();
							BigDecimal creTms = new BigDecimal(timeConverter.getCurrentTime());
							String newId = creTms.toString() + String.format("%07d", rowCount);
							String userId = sUserId;

							if(StringUtils.isEmpty(kyMoiGrid)) {
								if(cardbrn.equals("MC")) {
									int insertCount = dvnbInvoiceMcService.insertKhongKetChuyenIntoKyMoiById(id, newId, creTms, userId, kyMoi, ngayThucHien,"Y");
									if(insertCount>0)
										dvnbInvoiceMcService.updateKyMoiByIdAndKyMoiIsNull(kyMoi, id);
								} else {
									int insertCount = dvnbInvoiceVsService.insertKhongKetChuyenIntoKyMoiById(id, newId, creTms, userId, kyMoi, ngayThucHien,"Y");
									if(insertCount>0)
										dvnbInvoiceVsService.updateKyMoiByIdAndKyMoiIsNull(kyMoi, id);
								}
							}
						}
						Notification.show("Thông tin", "Đã cập nhật dữ liệu " + rowCount + " dòng sang kỳ " + kyMoi, Type.WARNING_MESSAGE);
						
					});
					
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				LOGGER.error("Lỗi: " + e.getMessage());
			}
			
		});
		
		btView.addClickListener(event -> {
			if(!cbbCardBrn.isEmpty() && !cbbType.isEmpty() && !dffromDate.isEmpty() && !dffromDate.isEmpty()) {
				cardBrn = cbbCardBrn.getValue().toString();
//	        	SHOW DATA IN GRID
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
				grid.refreshData();
				
				grid.grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {

					@Override
					public void preCommit(CommitEvent commitEvent) throws CommitException {
						// TODO Auto-generated method stub
//						Notification.show("Cập nhật lệch số.");
						return;
					}

					@Override
					public void postCommit(CommitEvent commitEvent) throws CommitException {
						
						try {
							String id = grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "id").toString();
							String ketChuyen = grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "ketChuyen").toString();
							String kyMoi =  grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "kyMoi")==null ? null : grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "kyMoi").toString();
							if(StringUtils.isEmpty(kyMoi) && ketChuyen.equals("N")) {
								switch(cbbCardBrn.getValue().toString()) {
									case "MC":
										int updateCountMC = dvnbInvoiceMcService.updateKyMoiByIdAndKyMoiIsNotNull(null, id);
										if(updateCountMC>0) {
											dvnbInvoiceMcService.deleteByOldId(id);
											Notification.show("Thông báo","Đã hủy bỏ invoice trong kỳ mới",Type.WARNING_MESSAGE);
										}
											
										break;
									case "VS":
										int updateCountVS =  dvnbInvoiceVsService.updateKyMoiByIdAndKyMoiIsNotNull(null, id);
										if(updateCountVS>0) {
											dvnbInvoiceVsService.deleteByOldId(id);
											Notification.show("Thông báo","Đã hủy bỏ invoice trong kỳ mới",Type.WARNING_MESSAGE);
										}
										break;
										
								}
							}
							
						} catch (Exception e) {
							Notification.show("Lỗi ứng dụng: "+ e.getMessage(), Type.ERROR_MESSAGE);
						}
					}
				});
				
				// Refresh paging button
				mainLayout.removeComponent(pagingLayout);
				pagingLayout = generatePagingLayout();
				pagingLayout.setSpacing(true);
				mainLayout.addComponent(pagingLayout);
				mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);
			}
			
		});
		
		btExport.addClickListener(event -> {
//			ketchuyenFlag = "Y";
//			switch(cardBrn)
//			{
//				case "MC":
//					filename = "ReportInvoiceMC.jasper";
//					break;
//				case "VS":
//					filename = "ReportInvoiceVS.jasper";
//					break;
//			}
//				
//			SimpleFileDownloader downloader = new SimpleFileDownloader();
//			addExtension(downloader);
//			StreamResource myResourceXLSX = createTransMKResourceXLS();
//			
//			downloader.setFileDownloadResource(myResourceXLSX);
//			downloader.download();
			List<DvnbInvoiceMc> invoiceMcList = resultMC == null ? null : resultMC.getContent();
			List<DvnbInvoiceVs> invoiceVsList = resultVS == null ? null : resultVS.getContent();
			
			if(!cbbCardBrn.isEmpty() && !cbbType.isEmpty() && !dffromDate.isEmpty() && !dffromDate.isEmpty()) {
				String cardBrn = cbbCardBrn.getValue().toString();
				String tungay = timeConverter.convertDatetime(dffromDate.getValue());
				String denngay = timeConverter.convertDatetime(dftoDate.getValue());
				exportDataInvoice(invoiceMcList,invoiceVsList,cardBrn,tungay,denngay);
			}
			
		});
		
//		grid.grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
//
//			@Override
//			public void preCommit(CommitEvent commitEvent) throws CommitException {
//				// TODO Auto-generated method stub
////				Notification.show("Cập nhật lệch số.");
//				return;
//			}
//
//			@Override
//			public void postCommit(CommitEvent commitEvent) throws CommitException {
//				
//				try {
//					String id = grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "id").toString();
//					String kyMoi = grid.grid.getContainerDataSource().getContainerProperty(grid.grid.getEditedItemId(), "kyMoi").toString();
//					if(kyMoi.isEmpty()) {
//						switch(cbbCardBrn.getValue().toString()) {
//							case "MC":
//								dvnbInvoiceMcService.updateKyMoiById(null, id);
//								dvnbInvoiceMcService.deleteByOldId(id);
//								Notification.show("Thông báo","Đã hủy bỏ invoice trong kỳ mới",Type.WARNING_MESSAGE);
//								break;
//							case "VS":
//								dvnbInvoiceVsService.updateKyMoiById(null, id);
//								dvnbInvoiceVsService.deleteByOldId(id);
//								Notification.show("Thông báo","Đã hủy bỏ invoice trong kỳ mới",Type.WARNING_MESSAGE);
//								break;
//						}
//					}
//					
//				} catch (Exception e) {
//					Notification.show("Lỗi ứng dụng: "+ e.getMessage(), Type.ERROR_MESSAGE);
//				}
//			}
//		});

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
		formLayout1st.addComponent(btView);
		formLayout1st.setComponentAlignment(lbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbCardBrn, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbfromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dffromDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbtoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(dftoDate, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(lbType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(cbbType, Alignment.MIDDLE_LEFT);
		formLayout1st.setComponentAlignment(btView, Alignment.MIDDLE_LEFT);
		
		formLayout2nd.addComponent(lbKyMoi);
		formLayout2nd.addComponent(cbbKyMoi);
		formLayout2nd.addComponent(btSave);
		formLayout2nd.addComponent(btExport);
		formLayout2nd.setComponentAlignment(lbKyMoi, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(cbbKyMoi, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btSave, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btExport, Alignment.MIDDLE_CENTER);
		
		formLayout.addComponent(formLayout1st);
		formLayout.addComponent(formLayout2nd);
		formLayout.setComponentAlignment(formLayout1st, Alignment.MIDDLE_CENTER);
		formLayout.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
		
		mainLayout.addComponent(formLayout);
		
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		grid = new DataGridInvoiceTreoComponent();
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
	
//	@SuppressWarnings("serial")
//	private StreamResource createTransMKResourceXLS() {
//		return new StreamResource(new StreamSource() {
//			@Override
//			public InputStream getStream() {
//
//				try {
//					ByteArrayOutputStream outpuf = makeFileForDownLoad(filename, "XLSX");
//					return new ByteArrayInputStream(outpuf.toByteArray());
//				} catch (Exception e) {
//					LOGGER.error("createTransMKResourceXLS - Message: " + e.getMessage());
//				}
//				return null;
//
//			}
//		}, "Invoice_baocao.xlsx");
//	}
//	
//	private ByteArrayOutputStream makeFileForDownLoad(String filename, String extension) throws JRException, SQLException {
//
//		Connection con = localDataSource.getConnection();
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		if (this.getParameter() != null) {
//			// Tham so truyen vao cho bao cao
//			Map<String, Object> parameters = this.getParameter();
//
//			JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(configurationHelper.getPathFileRoot() + "/invoicetemplate/" + filename);
//			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
//
//			// Xuat file Excel
//			if (extension.equals("XLSX")) {
//				JRXlsxExporter xlsx = new JRXlsxExporter();
//				xlsx.setExporterInput(new SimpleExporterInput(jasperPrint));
//				xlsx.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
//				xlsx.exportReport();
//			} else if (extension.equals("PDF")) { // File PDF
//				JasperExportManager.exportReportToPdfStream(jasperPrint, output);
//			}
//			return output;
//		} else {
//			return null;
//		}
//
//	}
//	
//	public Map<String, Object> getParameter() {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		String tungay = timeConverter.convertDatetime(dffromDate.getValue());
//		String denngay = timeConverter.convertDatetime(dftoDate.getValue());
//		parameters.put("p_ky", "All");
//		parameters.put("p_ketchuyen", cbbType.getValue().toString());
//		if(!cbbType.getValue().equals("All"))
//			parameters.put("p_ketchuyen_status", cbbType.getValue().equals("Y") ? "KẾT CHUYỂN VỀ ĐƠN VỊ" : "KHÔNG KẾT CHUYỂN VỀ ĐƠN VỊ");
//		else
//			parameters.put("p_ketchuyen_status", "");
//		parameters.put("p_tungay", tungay);
//		parameters.put("p_denngay", denngay);
//
//		return parameters;
//	}
	
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
	
	private void exportDataInvoice(List<DvnbInvoiceMc> invoiceMcList, List<DvnbInvoiceVs> invoiceVsList,String cardBrn,String fromdate,String todate) {
		//EXPORT LIST TO EXCEL FILE
        XSSFWorkbook workbookExport = new XSSFWorkbook();
        XSSFSheet sheetExport = workbookExport.createSheet();
        
        DataFormat format = workbookExport.createDataFormat();
        CellStyle styleNumber;
        styleNumber = workbookExport.createCellStyle();
        styleNumber.setDataFormat(format.getFormat("0.0"));
        
        rowNumExport = 0;
        LOGGER.info("Creating excel");

        if(rowNumExport == 0) {
        	Object[] rowHeader = null;
        	if(cardBrn.equals("MC"))
        		rowHeader = new Object[] {"STT","KET_CHUYEN", "NGAY_THUC_HIEN", "DOC_TYPE", "INVOICE_NUMBER", "CURRENCY", "BILLING_CYCLE_DATE", 
        				"INVOICE_ICA", "ACTIVITY_ICA", "BILLABLE_ICA", "COLLECTION_METHOD", "SERVICE_CODE", "SERVICE_CODE_DESC", "PERIOD_START_TIME", 
        				"PERIOD_END_TIME", "ORIGINAL_INVOICE_NUMBER", "EVENT_ID", "EVENT_DESC", "AFFILIATE", "UOM", "QUANTITY_AMOUNT", "RATE", "CHARGE", 
        				"TAX_CHARGE", "TOTAL_CHARGE", "VAT_CHARGE", "VAT_CURRENCY", "VAT_CODE", "VAT_RATE", "SBF_EXPLANATORY_TEXT", "KY",  "DIAVITION"};
        	else
        		rowHeader = new Object[] {"STT","KET_CHUYEN", "NGAY_THUC_HIEN", "BILLING_PERIOD", "INVOICE_DATE", "INVOICE_ACCOUNT", "NAME", 
        				"INVOICE_ID", "SUB_INVOICE", "CURRENT_OR_PREVIOUS", "ENTITY_TYPE", "ENTITY_ID", "BIN_MAP", "ENTITY_NAME", "SETTLEMENT_ID", 
        				"DESCRIPTION", "FUTURE_USE", "NTWK", "BILLING_LINE", "TYPE", "RATE_TYPE", "UNITS", "RATE_CUR", "RATE", "FOREIGN_EXCHANGE_RATE", 
        				"BILLING_CURRENCY", "TOTAL", "TAX_TYPE", "TAX", "TAX_RATE", "TAX_CURRENCY", "TAXABLE_AMOUNT", "TAX_TAX_CURRENCY", "KY", "DIAVITION"};
        	int colNum = 0;	 
        	XSSFRow row = sheetExport.createRow(rowNumExport++);         	
        	for (Object field : rowHeader) {
        		Cell cell = row.createCell(colNum++, CellType.STRING);
        		cell.setCellValue((String)field);
        	}      
        	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
        }
        
        try {
        	if(cardBrn.equals("MC")) {
        		for(DvnbInvoiceMc item : invoiceMcList) {
    				XSSFRow row = sheetExport.createRow(rowNumExport++);
    				
    				row.createCell(0).setCellValue(rowNumExport-1);
    				row.createCell(1).setCellValue(item.getKetChuyen());
    				row.createCell(2,CellType.NUMERIC).setCellValue(item.getNgayThucHien().doubleValue());
    				row.createCell(3).setCellValue(item.getDocType());
    				row.createCell(4).setCellValue(item.getInvoiceNumber());
    				row.createCell(5).setCellValue(item.getCurrency());
    				row.createCell(6).setCellValue(item.getBillingCycleDate());
    				row.createCell(7).setCellValue(item.getInvoiceIca());
    				row.createCell(8).setCellValue(item.getActivityIca());
    				row.createCell(9).setCellValue(item.getBillableIca());
    				row.createCell(10).setCellValue(item.getCollectionMethod());
    				row.createCell(11).setCellValue(item.getServiceCode());
    				row.createCell(12).setCellValue(item.getServiceCodeDesc());
    				row.createCell(13).setCellValue(item.getPeriodEndTime());
    				row.createCell(14).setCellValue(item.getPeriodEndTime());
    				row.createCell(15).setCellValue(item.getOriginalInvoiceNumber());
    				row.createCell(16).setCellValue(item.getEventId());
    				row.createCell(17).setCellValue(item.getEventDesc());
    				row.createCell(18).setCellValue(item.getAffiliate());
    				row.createCell(19).setCellValue(item.getUom());
    				row.createCell(20).setCellValue(item.getQuantityAmount());
    				row.createCell(21).setCellValue(item.getRate());
    				row.createCell(22).setCellValue(item.getCharge());
    				row.createCell(23).setCellValue(item.getTaxCharge());
    				row.createCell(24).setCellValue(item.getTotalCharge());
    				row.createCell(25).setCellValue(item.getVatCharge());
    				row.createCell(26).setCellValue(item.getVatCurrency());
    				row.createCell(27).setCellValue(item.getVatCode());
    				row.createCell(28).setCellValue(item.getVatRate());
    				row.createCell(29).setCellValue(item.getSbfExplanatoryText());
    				row.createCell(30).setCellValue(item.getKy());
    				row.createCell(31).setCellValue(item.getDeviation());
    	        }
        	} else {
        		for(DvnbInvoiceVs item : invoiceVsList) {
    				XSSFRow row = sheetExport.createRow(rowNumExport++);
    				row.createCell(0).setCellValue(rowNumExport-1);
    				row.createCell(1).setCellValue(item.getKetChuyen());
    				row.createCell(2,CellType.NUMERIC).setCellValue(item.getNgayThucHien().doubleValue());
    				row.createCell(3).setCellValue(item.getBillingPeriod());
    				row.createCell(4).setCellValue(item.getInvoiceDate());
    				row.createCell(5).setCellValue(item.getInvoiceAccount());
    				row.createCell(6).setCellValue(item.getName());
    				row.createCell(7).setCellValue(item.getInvoiceId());
    				row.createCell(8).setCellValue(item.getSubInvoice());
    				row.createCell(9).setCellValue(item.getCurrentOrPrevious());
    				row.createCell(10).setCellValue(item.getEntityType());
    				row.createCell(11).setCellValue(item.getEntityId());
    				row.createCell(12).setCellValue(item.getBinMap());
    				row.createCell(13).setCellValue(item.getEntityName());
    				row.createCell(14).setCellValue(item.getSettlementId());
    				row.createCell(15).setCellValue(item.getDescription());
    				row.createCell(16).setCellValue(item.getFutureUse());
    				row.createCell(17).setCellValue(item.getNtwk());
    				row.createCell(18).setCellValue(item.getBillingLine());
    				row.createCell(19).setCellValue(item.getType());
    				row.createCell(20).setCellValue(item.getRateType());
    				row.createCell(21).setCellValue(item.getUnits());
    				row.createCell(22).setCellValue(item.getRateCur());
    				row.createCell(23).setCellValue(item.getRate());
    				row.createCell(24).setCellValue(item.getForeignExchangeRate());
    				row.createCell(25).setCellValue(item.getBillingCurrency());
    				row.createCell(26).setCellValue(item.getTotal());
    				row.createCell(27).setCellValue(item.getTaxType());
    				row.createCell(28).setCellValue(item.getTax());
    				row.createCell(29).setCellValue(item.getTaxRate());
    				row.createCell(30).setCellValue(item.getTaxCurrency());
    				row.createCell(31).setCellValue(item.getTaxableAmount());
    				row.createCell(32).setCellValue(item.getTaxTaxCurrency());
    				row.createCell(33).setCellValue(item.getKy());
    	        }
        	}
        
	        sheetExport.createFreezePane(0, 1);
        
        	fileNameOutput = "PBCP_INV_" + cardBrn + "_" + fromdate + "_" + todate + ".xlsx";
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
	        messageExportXLSX("Info","Export compeleted.");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
	}
	

}
