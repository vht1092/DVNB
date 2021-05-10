package com.dvnb.components;

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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hpsf.SummaryInformation;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.Act2070101;
import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080105;
import com.dvnb.entities.Act2080201;
import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.entities.DvnbSummary;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.services.Act2070101Service;
import com.dvnb.services.Act2080101Service;
import com.dvnb.services.Act2080102Service;
import com.dvnb.services.Act2080103Service;
import com.dvnb.services.Act2080104Service;
import com.dvnb.services.Act2080105Service;
import com.dvnb.services.Act2080201Service;
import com.dvnb.services.DanhMucService;
import com.dvnb.services.DescriptionService;
import com.dvnb.services.DvnbInvoiceMcService;
import com.dvnb.services.DvnbInvoiceUploadService;
import com.dvnb.services.DvnbInvoiceVsService;
import com.dvnb.services.DvnbSummaryService;
import com.dvnb.services.SysUserroleService;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Sizeable.Unit;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class BaoCaoDVNB extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(BaoCaoDVNB.class);
	private final SysUserroleService sysUserroleService;
	private SpringConfigurationValueHelper configurationHelper;
	
	public static final String CAPTION = "BÁO CÁO DỊCH VỤ NỘI BỘ";
	private static final String VIEW = "VIEW";
	private static final String APPLY = "APPLY";
	private static final String EXPORT = "EXPORT";
	private static final String DVNB = "DỊCH VỤ NỘI BỘ";
	private static final String KYBAOCAO = "KỲ BÁO CÁO";
	private static final String DONVI = "ĐƠN VỊ";

	private transient String sUserId;
	private final ComboBox cbbDVNB;
	private final ComboBox cbbDonVi;
	private final ComboBox cbbKyBaoCao;
	String fileNameImport;
	private Window confirmDialog = new Window();
	private Button bOK = new Button("OK");
	final Button btView = new Button(VIEW);
	final Button btApply = new Button(APPLY);
	final Button btExport = new Button(EXPORT);
	private String CheckUserId = "";
	private int rowNumExport = 0;
	private String maDvnb;
	private String ky;
	private String maDonVi;
	private String fileNameOutput = null;
	private Path pathExport = null;
	final TimeConverter timeConverter = new TimeConverter();
	private List<DvnbSummary> dvnbSummaryList;
	private List<DvnbSummary> act2080101SummaryList;
	private List<DvnbSummary> act2080102SummaryList;
	private List<DvnbSummary> act2080103SummaryList;
	private List<DvnbSummary> act2080104SummaryList;
	private final transient DvnbSummaryService dvnbSummaryService;
	private DataGridBaoCaoDVNB grid;
	private transient Page<DvnbSummary> result;
	
	// Paging
	private final static int SIZE_OF_PAGE = 1000000;
	private final static int FIRST_OF_PAGE = 0;
		
	public BaoCaoDVNB() {
		
		final VerticalLayout mainLayout = new VerticalLayout();
		final HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setSpacing(true);
		final VerticalLayout vformLayout2nd = new VerticalLayout();
		vformLayout2nd.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		final DanhMucService danhMucService = (DanhMucService) helper.getBean("danhMucService");
		sysUserroleService = (SysUserroleService) helper.getBean("sysUserroleService");
		dvnbSummaryService = (DvnbSummaryService) helper.getBean("dvnbSummaryService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		this.sUserId = SecurityUtils.getUserId();
		CheckUserId = sysUserroleService.findByRoleId(sUserId);
		
		
		grid = new DataGridBaoCaoDVNB();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));

		final Label lbDVNB = new Label(DVNB);
		cbbDVNB = new ComboBox();
		cbbDVNB.setNullSelectionAllowed(true);
		cbbDVNB.setPageLength(20);
		descService.findAllByTypeByOrderBySequencenoAsc("DVNB").forEach(item -> {
			switch(CheckUserId) {
				case "1": //GSTS
					if (item.getSequenceno().compareTo(new BigDecimal(100))<0) {
						cbbDVNB.addItem(item.getId());
						cbbDVNB.setItemCaption(item.getId(), item.getId() + " - " + item.getDescription());
					}
					break;
				
				case "2": //KTT&VH&NHS
					if (item.getSequenceno().compareTo(new BigDecimal(100))>=0 && item.getSequenceno().compareTo(new BigDecimal(200))<0) {
						cbbDVNB.addItem(item.getId());
						cbbDVNB.setItemCaption(item.getId(), item.getId() + " - " + item.getDescription());
					}
					break;
				
				case "3": //NC&PTKD
					if (item.getSequenceno().compareTo(new BigDecimal(200))>=0 && item.getSequenceno().compareTo(new BigDecimal(300))<0) {
						cbbDVNB.addItem(item.getId());
						cbbDVNB.setItemCaption(item.getId(), item.getId() + " - " + item.getDescription());
					}
					break;
				case "4": //All
					cbbDVNB.addItem(item.getId());
					cbbDVNB.setItemCaption(item.getId(), item.getId() + " - " + item.getDescription());
					break;
			}
			
		});
		
		final Label lbDonVi = new Label(DONVI);
		cbbDonVi = new ComboBox();
		cbbDonVi.setNullSelectionAllowed(true);
		danhMucService.findAllByDanhMucOrderByMaAsc("MA_DON_VI").forEach(item -> {
			cbbDonVi.addItem(item.getMa());
			cbbDonVi.setItemCaption(item.getMa(), item.getMa() + " - " + item.getTen());
			
		});
		
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		final Label lbKyBaoCao = new Label(KYBAOCAO);
		cbbKyBaoCao = new ComboBox();
		cbbKyBaoCao.setNullSelectionAllowed(false);
		cbbKyBaoCao.setPageLength(12);
		descService.findAllByTypeByOrderBySequencenoAsc("KYBAOCAO").forEach(item -> {
			cbbKyBaoCao.addItem(item.getId());
			cbbKyBaoCao.setItemCaption(item.getId(),item.getDescription());
		});
		cbbKyBaoCao.setValue(String.valueOf(cal.get(Calendar.MONTH) + 1) + String.valueOf(cal.get(Calendar.YEAR)));

		bOK.addClickListener(event -> {
        	confirmDialog.close();
        });
		
		btExport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btExport.setWidth(100.0f, Unit.PIXELS);
		btExport.setIcon(FontAwesome.FILE_EXCEL_O);
		btExport.addClickListener(event -> {
			
			if(cbbKyBaoCao.getValue()==null) {
				Notification.show("Lỗi","Chưa chọn kỳ báo cáo", Type.ERROR_MESSAGE);
				return;
			}
			
			ky = cbbKyBaoCao.getValue().toString();
			maDvnb = cbbDVNB.getValue() == null ? "All" : cbbDVNB.getValue().toString();
			maDonVi = cbbDonVi.getValue() == null ? "All" : cbbDonVi.getValue().toString();
			String sFromDate = ky.substring(2) + ky.substring(0, 2) + "01" + "000000000";
			String sToDate = ky.substring(2) + ky.substring(0,2) + LastDayOfMonth(Integer.valueOf(ky.substring(2)),Integer.valueOf(ky.substring(0,2))) + "999999999";
			try {
				switch(maDvnb) {
					case "All":
						dvnbSummaryList = dvnbSummaryService.searchAllDvnbSummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
						dvnbSummaryList.addAll(dvnbSummaryService.searchAct2080101SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate));
						dvnbSummaryList.addAll(dvnbSummaryService.searchAct2080103SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate));
						if(CheckUserId.equals(1)) {
							dvnbSummaryList.addAll(dvnbSummaryService.searchAct2080102FromEBHitRuleNhom1(ky, sUserId));
							dvnbSummaryList.addAll(dvnbSummaryService.searchAct2080104FromEBHitRuleNhom2(ky, sUserId));
						}
						
						dvnbSummaryList.sort(Comparator.comparing(DvnbSummary::getType)
			                      .reversed()
			                      .thenComparing(Comparator.comparing(DvnbSummary::getMaHoatDong))
								);
						break;
					case "ACT_2080101":
						dvnbSummaryList = dvnbSummaryService.searchAct2080101SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
						break;
					case "ACT_2080102":
						dvnbSummaryList = dvnbSummaryService.searchAct2080102FromEBHitRuleNhom1(ky, sUserId);
						break;
					case "ACT_2080103":
						dvnbSummaryList = dvnbSummaryService.searchAct2080103SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
						break;
					case "ACT_2080104":
						dvnbSummaryList = dvnbSummaryService.searchAct2080104FromEBHitRuleNhom2(ky, sUserId);
						break;
					default:
						dvnbSummaryList = dvnbSummaryService.searchAllDvnbSummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
						break;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				LOGGER.error(e1.getMessage());
			}
			//EXPORT LIST TO EXCEL FILE
            XSSFWorkbook workbookExport = new XSSFWorkbook();
            XSSFSheet sheetExport = workbookExport.createSheet("Tong hop DVNB");
	        
            DataFormat format = workbookExport.createDataFormat();
            CellStyle styleNumber;
            styleNumber = workbookExport.createCellStyle();
            styleNumber.setDataFormat(format.getFormat("0.0"));
            
            rowNumExport = 0;
	        LOGGER.info("Creating excel");

	        if(rowNumExport == 0) {
            	Object[] rowHeader = {"KY","MA_DON_VI","MA_DRIVER","MA_SAN_PHAM","MA_PHONG_BAN","MA_LOAI_KHACH_HANG",
            			"MA_HOAT_DONG","PCMCS_RESERVE2_ID","SO_TIEN/SO_LUONG","TYPE"};
            	int colNum = 0;	 
            	XSSFRow row = sheetExport.createRow(rowNumExport++);         	
            	for (Object field : rowHeader) {
            		Cell cell = row.createCell(colNum++, CellType.STRING);
            		cell.setCellValue((String)field);
            	}      
            	LOGGER.info("Created row " + rowNumExport + " for header sheet in excel.");
	        }
	        
	        for(DvnbSummary item : dvnbSummaryList) {
				XSSFRow row = sheetExport.createRow(rowNumExport++);
				
				row.createCell(0).setCellValue(item.getKy());
				row.createCell(1).setCellValue(item.getMaDonVi());
				row.createCell(2).setCellValue(item.getMaDriver());
				row.createCell(3).setCellValue(item.getMaSanPham());
				row.createCell(4).setCellValue(item.getMaPhongBan());
				row.createCell(5).setCellValue(item.getMaLoaiKhachHang());
				row.createCell(6).setCellValue(item.getMaHoatDong());
				row.createCell(7).setCellValue(item.getPcmcsReserve2Id());
				row.createCell(8,CellType.NUMERIC).setCellValue(Long.parseLong(item.getSoTienSoLuong()));
				row.createCell(9).setCellValue(item.getType());
	        }
	        
	        sheetExport.createFreezePane(0, 1);
	        try {
	        	
	        	fileNameOutput = "DVNB_SUMMARY_" + maDvnb + "_"+ ky + "_" + timeConverter.getCurrentTime() + ".xlsx";
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
	            LOGGER.info("Done");
		        LOGGER.info("Export excel file " + fileNameOutput);
		        messageExportXLSX("Info","Export compeleted.");
		        
	        } catch (FileNotFoundException e) {
	            LOGGER.error(e.getMessage());
	        } catch (IOException e) {
	            LOGGER.error(e.getMessage());
	        }
		});
		
		btApply.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btApply.setWidth(100.0f, Unit.PIXELS);
		btApply.setIcon(FontAwesome.CHECK_CIRCLE_O);
		btApply.addClickListener(event -> {
			
			dvnbSummaryService.deleteDvnbSummaryByMaHoatDong(ky,maDvnb,CheckUserId);
			
			for(DvnbSummary s : dvnbSummaryList) {
				s.setCreTms(timeConverter.getCurrentTime());
    			s.setUsrId(sUserId);
    			String maDonVi = s.getMaDonVi()==null?"":s.getMaDonVi();
    			String maDriver = s.getMaDriver()==null?"":s.getMaDriver();
    			String maSanPham = s.getMaSanPham()==null?"":s.getMaSanPham();
    			String maPhongBan = s.getMaPhongBan()==null?"":s.getMaPhongBan();
    			String maLoaiKH = s.getMaLoaiKhachHang()==null?"":s.getMaLoaiKhachHang();
				s.setIdno(s.getKy()+s.getMaHoatDong()+maDonVi+maDriver+maSanPham+maPhongBan+maLoaiKH);
				dvnbSummaryService.save(s);
			}
		});
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(100.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		btView.addClickListener(event -> {
			if(cbbKyBaoCao.getValue()==null) {
				Notification.show("Lỗi","Chưa chọn kỳ báo cáo", Type.ERROR_MESSAGE);
				return;
			}
			
			grid.dataSource = getData(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "id"));
			grid.initGrid("All");

			grid.refreshData();
		});
		
		formLayout.addComponent(lbDVNB);
		formLayout.addComponent(cbbDVNB);
		formLayout.addComponent(lbKyBaoCao);
		formLayout.addComponent(cbbKyBaoCao);
		formLayout.addComponent(lbDonVi);
		formLayout.addComponent(cbbDonVi);
		
		formLayout.setComponentAlignment(lbDVNB, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(cbbDVNB, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(lbKyBaoCao, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(cbbKyBaoCao, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(lbDonVi, Alignment.MIDDLE_LEFT);
		formLayout.setComponentAlignment(cbbDonVi, Alignment.MIDDLE_LEFT);
		
		formLayout2nd.addComponent(btView);
		formLayout2nd.addComponent(btExport);
		formLayout2nd.addComponent(btApply);
		formLayout2nd.setComponentAlignment(btView, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btExport, Alignment.MIDDLE_CENTER);
		formLayout2nd.setComponentAlignment(btApply, Alignment.MIDDLE_CENTER);
		
		vformLayout2nd.addComponent(formLayout2nd);
		vformLayout2nd.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
		
		
		form.addComponent(formLayout);
		form.addComponent(vformLayout2nd);
		
		mainLayout.addComponent(form);
		mainLayout.addComponent(grid);
		
		setCompositionRoot(mainLayout);
	}
	
	static int LastDayOfMonth(int Y, int M) {
	    return LocalDate.of(Y, M, 1).getMonth().length(Year.of(Y).isLeap());
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
	                LOGGER.error(e.getMessage());
	            }
	              return input;

	        }
	    };
	    StreamResource resource = new StreamResource ( source, inputfile.getName());
	    return resource;
	}
	
	private Page<DvnbSummary> getData(Pageable page) {
		
		ky = cbbKyBaoCao.getValue().toString();
		maDvnb = cbbDVNB.getValue() == null ? "All" : cbbDVNB.getValue().toString();
		maDonVi = cbbDonVi.getValue() == null ? "All" : cbbDonVi.getValue().toString();
		String sFromDate = ky.substring(2) + ky.substring(0, 2) + "01" + "000000000";
		String sToDate = ky.substring(2) + ky.substring(0,2) + LastDayOfMonth(Integer.valueOf(ky.substring(2)),Integer.valueOf(ky.substring(0,2))) + "999999999";
		try {
			switch(maDvnb) {
				case "All":
					dvnbSummaryList = dvnbSummaryService.searchAllDvnbSummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
					act2080101SummaryList = dvnbSummaryService.searchAct2080101SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
					act2080103SummaryList = dvnbSummaryService.searchAct2080103SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);

					dvnbSummaryList.addAll(act2080101SummaryList);
					dvnbSummaryList.addAll(act2080103SummaryList);
					
					if(CheckUserId.equals(1)) {
						act2080102SummaryList = dvnbSummaryService.searchAct2080102FromEBHitRuleNhom1(ky, sUserId);
						act2080104SummaryList = dvnbSummaryService.searchAct2080104FromEBHitRuleNhom2(ky, sUserId);
						dvnbSummaryList.addAll(act2080102SummaryList);
						dvnbSummaryList.addAll(act2080104SummaryList);
					}
					
//					Comparator<DvnbSummary> compareByType = (DvnbSummary o1, DvnbSummary o2) -> o1.getType().compareTo( o2.getType());
//					Comparator<DvnbSummary> compareByMaHoatDong = (DvnbSummary o1, DvnbSummary o2) -> o1.getMaHoatDong().compareTo( o2.getMaHoatDong());
//					Collections.sort(dvnbSummaryList, compareByType);
//					Collections.sort(dvnbSummaryList, compareByMaHoatDong);
					
//					dvnbSummaryList.sort((p1, p2) -> p1.getMaHoatDong().compareTo(p2.getMaHoatDong()));
//					dvnbSummaryList.sort((p1, p2) -> p1.getType().compareTo(p2.getType()));
					
					dvnbSummaryList.sort(Comparator.comparing(DvnbSummary::getType)
		                      .reversed()
		                      .thenComparing(Comparator.comparing(DvnbSummary::getMaHoatDong))
							);
					break;
				case "ACT_2080101":
					dvnbSummaryList = dvnbSummaryService.searchAct2080101SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
					break;
				case "ACT_2080103":
					dvnbSummaryList = dvnbSummaryService.searchAct2080103SummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
					break;
				case "ACT_2080102":
					dvnbSummaryList = dvnbSummaryService.searchAct2080102FromEBHitRuleNhom1(ky, sUserId);
					break;
				case "ACT_2080104":
					dvnbSummaryList = dvnbSummaryService.searchAct2080104FromEBHitRuleNhom2(ky, sUserId);
					break;	
				default:
					dvnbSummaryList = dvnbSummaryService.searchAllDvnbSummaryProc(ky, maDvnb, maDonVi,CheckUserId,sUserId,sFromDate,sToDate);
					break;
			}
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			LOGGER.error(e1.getMessage());
		}
		
	    result = new PageImpl<>(dvnbSummaryList);
	    		
	    		//doiSoatDataService.findAll(page);
		return result;
	}

}
