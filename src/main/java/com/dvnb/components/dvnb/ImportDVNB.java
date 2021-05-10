package com.dvnb.components.dvnb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.dvnb.ReloadComponent;
import com.dvnb.SecurityUtils;
import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.*;
import com.dvnb.services.*;
import com.monitorjbl.xlsx.StreamingReader;
import com.vaadin.server.FontAwesome;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
public class ImportDVNB extends CustomComponent implements ReloadComponent {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportDVNB.class);
	private final transient Act1420101Service act1420101Service;
	private final transient Act1430101Service act1430101Service;
	private final transient Act1440101Service act1440101Service;
	private final transient Act1450101Service act1450101Service;
	private final transient Act1450201Service act1450201Service;
	private final transient Act2070101Service act2070101Service;
	private final transient Act2070102Service act2070102Service;
	private final transient Act2070103Service act2070103Service;
	private final transient Act2070104Service act2070104Service;
	private final transient Act2070105Service act2070105Service;
	private final transient Act2070106Service act2070106Service;
	private final transient Act2080101Service act2080101Service;
	private final transient Act2080102Service act2080102Service;
	private final transient Act2080103Service act2080103Service;
	private final transient Act2080104Service act2080104Service;
	private final transient Act2080105Service act2080105Service;
	private final transient Act2080106Service act2080106Service;
	private final transient Act2080107Service act2080107Service;
	private final transient Act2080201Service act2080201Service;
	private final transient Act2080301Service act2080301Service;
	private final transient Act2090101Service act2090101Service; 
	private final transient Act2090201Service act2090201Service; 
	private final transient Act2090301Service act2090301Service; 
	private final transient Act2090401Service act2090401Service;
	private final transient Act2090501Service act2090501Service;
	private final transient Act2090601Service act2090601Service; 
	private final transient Act2090701Service act2090701Service; 
	private final transient Act2090702Service act2090702Service; 
	private final transient Act2090703Service act2090703Service; 
	private final transient Act2090704Service act2090704Service;
	private final transient Act2090705Service act2090705Service; 
	private final transient Act2090706Service act2090706Service; 
	private final transient Act2090707Service act2090707Service; 
	private final transient Act2090708Service act2090708Service; 
	private final transient Act2090709Service act2090709Service;
	private final transient Act2090711Service act2090711Service; 
	private final transient Act2090712Service act2090712Service; 
	private final transient Act2090713Service act2090713Service; 
	private final transient Act2090714Service act2090714Service; 
	private final transient Act2090715Service act2090715Service;
	private final transient Act2090801Service act2090801Service; 
	private final transient Act2090802Service act2090802Service; 
	private final transient Act2090803Service act2090803Service; 
	private final transient Act2090804Service act2090804Service; 
	private final transient Act2090805Service act2090805Service;
	private final transient Act2090806Service act2090806Service;

	private final transient Act2090710Service act2090710Service;
	private final transient Act2100101Service act2100101Service;
	private final transient Act2100201Service act2100201Service;
	private final transient Act2100301Service act2100301Service;
	private final transient Act2100401Service act2100401Service;
	private final transient Act2100501Service act2100501Service;
	private final transient Act2100502Service act2100502Service;
	private final transient Act2100503Service act2100503Service;
	private final transient Act2110101Service act2110101Service;
	private final transient Act2110102Service act2110102Service;
	private final transient Act2110103Service act2110103Service;
	private final transient Act2110104Service act2110104Service;
	private final transient Act2110202Service act2110202Service;
	private final transient Act2110203Service act2110203Service;
	private final transient Act2110204Service act2110204Service;
	private final transient Act2110205Service act2110205Service;
	private final transient Act2110206Service act2110206Service;
	private final transient Act3370101Service act3370101Service;
	private final transient Act3370102Service act3370102Service;
	private final transient Act3370103Service act3370103Service;
	private final transient Act3370104Service act3370104Service;
	private final transient Act3380101Service act3380101Service;
	private final transient Act3390303Service act3390303Service;
	private final transient Act3400101Service act3400101Service;
	private final transient Act3400102Service act3400102Service;
	private final transient Act3400201Service act3400201Service;
	private final transient Act3400204Service act3400204Service;
	private final transient Act3400205Service act3400205Service;
	private final transient Act3380501Service act3380501Service;
	private final transient Act2100601Service act2100601Service;
	private final transient Act2100202Service act2100202Service;
	private final transient Act2110301Service act2110301Service;
	private final transient Act2110302Service act2110302Service;
	private final transient Act2110401Service act2110401Service;
	private final transient Act2110402Service act2110402Service;
	
	private final transient Act3380201Service act3380201Service;
	private final transient Act3380301Service act3380301Service;
	private final transient Act3380401Service act3380401Service;
	private final transient Act3390101bService act3390101bService;
	private final transient Act3390101Service act3390101Service;
	private final transient Act3390301Service act3390301Service;
	private final transient Act3390302Service act3390302Service;
	private final transient Act3390901Service act3390901Service;
	private final transient Act3391001Service act3391001Service;
	private final transient Act3400202Service act3400202Service;
	private final transient Act3400203Service act3400203Service;
	private final transient Act3390201Service act3390201Service;
	private final transient Act3390401Service act3390401Service;
	private final transient Act3390402Service act3390402Service;
	private final transient Act3390501Service act3390501Service;
	private final transient Act3390502Service act3390502Service;
	private final transient Act3390701Service act3390701Service;
	private final transient Act3390801Service act3390801Service;
	private final transient Act3390403Service act3390403Service;
	private final transient Act3390601Service act3390601Service;
	
	
	private final SysUserroleService sysUserroleService;
	private SpringConfigurationValueHelper configurationHelper;
	
	public static final String CAPTION = "IMPORT DATA DVNB";
	private DataGridDvnbComponent grid;
	private static final String VIEW = "VIEW";
	private static final String DEL = "DELETE";
	private static final String IMPORT = "IMPORT";
	private static final String DVNB = "DỮ LIỆU DỊCH VỤ NỘI BỘ";
	private static final String KYBAOCAO = "KỲ BÁO CÁO";
	private static final String SLPHATSINHTT = "SỐ LƯỢNG PHÁT SINH THỰC TẾ";
	private static final String SLNGAYLAMVIEC = "SỐ LƯỢNG NGÀY LÀM VIỆC";
	private static final String SONGAYTHUCHIEN = "SỐ NGÀY THỰC HIỆN";
	private static final String DONGIA = "ĐƠN GIÁ";

	private transient String sUserId;
	private transient Page<Act2070101> resultAct2070101;
	private transient Page<Act2070102> resultAct2070102;
	private transient Page<Act2070103> resultAct2070103;
	private transient Page<Act2070104> resultAct2070104;
	private transient Page<Act2070105> resultAct2070105;
	private transient Page<Act2070106> resultAct2070106;
	private transient Page<Act2080101> resultAct2080101;
	private transient Page<Act2080102> resultAct2080102;
	private transient Page<Act2080103> resultAct2080103;
	private transient Page<Act2080104> resultAct2080104;
	private transient Page<Act2080105> resultAct2080105;
	private transient Page<Act2080106> resultAct2080106;
	private transient Page<Act2080201> resultAct2080201;
	private transient Page<Act2080301> resultAct2080301;
	private transient Page<Act2100201> resultAct2100201;
	private transient Page<Act2100301> resultAct2100301;
	private transient Page<Act2110103> resultAct2110103;
	private transient Page<Act2110104> resultAct2110104;
	
	private transient Page<Act3380201> resultAct3380201;
	private transient Page<Act3380301> resultAct3380301;
	private transient Page<Act3380401> resultAct3380401;
	private transient Page<Act3390101b> resultAct3390101b;
	private transient Page<Act3390301> resultAct3390301;
	private transient Page<Act3390302> resultAct3390302;
	private transient Page<Act3390901> resultAct3390901;
	private transient Page<Act3391001> resultAct3391001;
	private transient Page<Act3400202> resultAct3400202;
	private transient Page<Act3400203> resultAct3400203;
	private transient Page<Act3390201> resultAct3390201;
	private transient Page<Act3390401> resultAct3390401;
	private transient Page<Act3390402> resultAct3390402;
	private transient Page<Act3390501> resultAct3390501;
	private transient Page<Act3390502> resultAct3390502;
	private transient Page<Act3390701> resultAct3390701;
	private transient Page<Act3390801> resultAct3390801;
	private transient Page<Act3390403> resultAct3390403;
	private transient Page<Act3390601> resultAct3390601;
	
	private final ComboBox cbbDVNB;
	private final ComboBox cbbKyBaoCao;
	private TextField tfSLPhatSinhThucTe;
	private TextField tfDonGia;
	private Label lbNote;
	String fileNameImport;
	private Window confirmDialog = new Window();
	private Button bOK = new Button("OK");
	final Button btImport = new Button(IMPORT);
	final Button btView = new Button(VIEW);
	final Button btDelete = new Button(DEL);
	private String CheckUserId = "";
	private String maDVNB = "";
	private String kyBaoCao = "";
	
	// Paging
	private final static int SIZE_OF_PAGE = 100;
	private final static int FIRST_OF_PAGE = 0;
	private transient HorizontalLayout pagingLayout;
	
	File fileImport = null;
	final TimeConverter timeConverter = new TimeConverter();
	
	private int i;
	
	private final VerticalLayout mainLayout = new VerticalLayout();
	
	
	public ImportDVNB() {
		
		final VerticalLayout mainLayout = new VerticalLayout();
		final VerticalLayout formLayout = new VerticalLayout();
		formLayout.setSpacing(true);
		final VerticalLayout vformLayout2nd = new VerticalLayout();
		vformLayout2nd.setSpacing(true);
		final HorizontalLayout formLayout2nd = new HorizontalLayout();
		formLayout2nd.setSpacing(true);
		
		mainLayout.setCaption(CAPTION);
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		final DescriptionService descService = (DescriptionService) helper.getBean("descriptionService");
		act1420101Service = (Act1420101Service) helper.getBean("act1420101Service");
		act1430101Service = (Act1430101Service) helper.getBean("act1430101Service");
		act1440101Service = (Act1440101Service) helper.getBean("act1440101Service");
		act1450101Service = (Act1450101Service) helper.getBean("act1450101Service");
		act1450201Service = (Act1450201Service) helper.getBean("act1450201Service");
		act2070101Service = (Act2070101Service) helper.getBean("act2070101Service");
		act2070102Service = (Act2070102Service) helper.getBean("act2070102Service");
		act2070103Service = (Act2070103Service) helper.getBean("act2070103Service");
		act2070104Service = (Act2070104Service) helper.getBean("act2070104Service");
		act2070105Service = (Act2070105Service) helper.getBean("act2070105Service");
		act2070106Service = (Act2070106Service) helper.getBean("act2070106Service");
		act2080101Service = (Act2080101Service) helper.getBean("act2080101Service");
		act2080102Service = (Act2080102Service) helper.getBean("act2080102Service");
		act2080103Service = (Act2080103Service) helper.getBean("act2080103Service");
		act2080104Service = (Act2080104Service) helper.getBean("act2080104Service");
		act2080105Service = (Act2080105Service) helper.getBean("act2080105Service");
		act2080106Service = (Act2080106Service) helper.getBean("act2080106Service");
		act2080107Service = (Act2080107Service) helper.getBean("act2080107Service");
		act2080201Service = (Act2080201Service) helper.getBean("act2080201Service");
		act2080301Service = (Act2080301Service) helper.getBean("act2080301Service");
		act2090101Service = (Act2090101Service) helper.getBean("act2090101Service"); 
		act2090201Service = (Act2090201Service) helper.getBean("act2090201Service"); 
		act2090301Service = (Act2090301Service) helper.getBean("act2090301Service"); 
		act2090401Service = (Act2090401Service) helper.getBean("act2090401Service");
		act2090501Service = (Act2090501Service) helper.getBean("act2090501Service");				
		act2090601Service = (Act2090601Service) helper.getBean("act2090601Service"); 
		act2090701Service = (Act2090701Service) helper.getBean("act2090701Service"); 
		act2090702Service = (Act2090702Service) helper.getBean("act2090702Service"); 
		act2090703Service = (Act2090703Service) helper.getBean("act2090703Service"); 
		act2090704Service = (Act2090704Service) helper.getBean("act2090704Service");				
		act2090705Service = (Act2090705Service) helper.getBean("act2090705Service"); 
		act2090706Service = (Act2090706Service) helper.getBean("act2090706Service"); 
		act2090707Service = (Act2090707Service) helper.getBean("act2090707Service"); 
		act2090708Service = (Act2090708Service) helper.getBean("act2090708Service"); 
		act2090709Service = (Act2090709Service) helper.getBean("act2090709Service");				
		act2090711Service = (Act2090711Service) helper.getBean("act2090711Service"); 
		act2090712Service = (Act2090712Service) helper.getBean("act2090712Service"); 
		act2090713Service = (Act2090713Service) helper.getBean("act2090713Service"); 
		act2090714Service = (Act2090714Service) helper.getBean("act2090714Service"); 
		act2090715Service = (Act2090715Service) helper.getBean("act2090715Service");
		act2090801Service = (Act2090801Service) helper.getBean("act2090801Service"); 
		act2090802Service = (Act2090802Service) helper.getBean("act2090802Service"); 
		act2090803Service = (Act2090803Service) helper.getBean("act2090803Service"); 
		act2090804Service = (Act2090804Service) helper.getBean("act2090804Service"); 
		act2090805Service = (Act2090805Service) helper.getBean("act2090805Service");
		act2090806Service = (Act2090806Service) helper.getBean("act2090806Service");
		act2090710Service = (Act2090710Service) helper.getBean("act2090710Service");
		act2100101Service = (Act2100101Service) helper.getBean("act2100101Service");
		act2100201Service = (Act2100201Service) helper.getBean("act2100201Service");
		act2100301Service = (Act2100301Service) helper.getBean("act2100301Service");
		act2100401Service = (Act2100401Service) helper.getBean("act2100401Service");
		act2100501Service = (Act2100501Service) helper.getBean("act2100501Service");
		act2100502Service = (Act2100502Service) helper.getBean("act2100502Service");
		act2100503Service = (Act2100503Service) helper.getBean("act2100503Service");
		act2110101Service = (Act2110101Service) helper.getBean("act2110101Service");
		act2110102Service = (Act2110102Service) helper.getBean("act2110102Service");
		act2110103Service = (Act2110103Service) helper.getBean("act2110103Service");
		act2110104Service = (Act2110104Service) helper.getBean("act2110104Service");
		act2110202Service = (Act2110202Service) helper.getBean("act2110202Service");
		act2110203Service = (Act2110203Service) helper.getBean("act2110203Service");
		act2110204Service = (Act2110204Service) helper.getBean("act2110204Service");
		act2110205Service = (Act2110205Service) helper.getBean("act2110205Service");
		act2110206Service = (Act2110206Service) helper.getBean("act2110206Service");
		act2100601Service = (Act2100601Service) helper.getBean("act2100601Service");
		act2100202Service = (Act2100202Service) helper.getBean("act2100202Service");
		act2110301Service = (Act2110301Service) helper.getBean("act2110301Service");
		act2110302Service = (Act2110302Service) helper.getBean("act2110302Service");
		act2110401Service = (Act2110401Service) helper.getBean("act2110401Service");
		act2110402Service = (Act2110402Service) helper.getBean("act2110402Service");
		
		act3370101Service = (Act3370101Service) helper.getBean("act3370101Service");
		act3370102Service = (Act3370102Service) helper.getBean("act3370102Service");
		act3370103Service = (Act3370103Service) helper.getBean("act3370103Service");
		act3370104Service = (Act3370104Service) helper.getBean("act3370104Service");
		act3380101Service = (Act3380101Service) helper.getBean("act3380101Service");
		act3390303Service = (Act3390303Service) helper.getBean("act3390303Service");
		act3400101Service = (Act3400101Service) helper.getBean("act3400101Service");
		act3400102Service = (Act3400102Service) helper.getBean("act3400102Service");
		act3400201Service = (Act3400201Service) helper.getBean("act3400201Service");
		act3400204Service = (Act3400204Service) helper.getBean("act3400204Service");
		act3400205Service = (Act3400205Service) helper.getBean("act3400205Service");
		act3380201Service = (Act3380201Service) helper.getBean("act3380201Service");
		act3380301Service = (Act3380301Service) helper.getBean("act3380301Service");
		act3380401Service = (Act3380401Service) helper.getBean("act3380401Service");
		act3390101Service = (Act3390101Service) helper.getBean("act3390101Service");
		act3390101bService = (Act3390101bService) helper.getBean("act3390101bService");
		act3390301Service = (Act3390301Service) helper.getBean("act3390301Service");
		act3390302Service = (Act3390302Service) helper.getBean("act3390302Service");
		act3390901Service = (Act3390901Service) helper.getBean("act3390901Service");
		act3391001Service = (Act3391001Service) helper.getBean("act3391001Service");
		act3400202Service = (Act3400202Service) helper.getBean("act3400202Service");
		act3400203Service = (Act3400203Service) helper.getBean("act3400203Service");
		act3380501Service = (Act3380501Service) helper.getBean("act3380501Service");
		act3390201Service = (Act3390201Service) helper.getBean("act3390201Service");
		act3390401Service = (Act3390401Service) helper.getBean("act3390401Service");
		act3390402Service = (Act3390402Service) helper.getBean("act3390402Service");
		act3390501Service = (Act3390501Service) helper.getBean("act3390501Service");
		act3390502Service = (Act3390502Service) helper.getBean("act3390502Service");
		act3390701Service = (Act3390701Service) helper.getBean("act3390701Service");
		act3390801Service = (Act3390801Service) helper.getBean("act3390801Service");
		act3390403Service = (Act3390403Service) helper.getBean("act3390403Service");
		act3390601Service = (Act3390601Service) helper.getBean("act3390601Service");
		
		
		sysUserroleService = (SysUserroleService) helper.getBean("sysUserroleService");
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		this.sUserId = SecurityUtils.getUserId();
		CheckUserId = sysUserroleService.findByRoleId(sUserId);
		
		
//		grid = new DataGridComponent();
		mainLayout.setSpacing(true);
		mainLayout.setMargin(new MarginInfo(true, false, false, false));

		final Label lbDVNB = new Label(DVNB);
		cbbDVNB = new ComboBox(DVNB);
		cbbDVNB.setSizeFull();
		cbbDVNB.setNullSelectionAllowed(false);
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
//		cbbDVNB.setValue("ACT_2110206");
		cbbDVNB.removeItem("ACT_2080101");
		cbbDVNB.removeItem("ACT_2080102");
		cbbDVNB.removeItem("ACT_2080103");
		cbbDVNB.removeItem("ACT_2080104");
		
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		final Label lbKyBaoCao = new Label(KYBAOCAO);
		cbbKyBaoCao = new ComboBox(KYBAOCAO);
		cbbKyBaoCao.setNullSelectionAllowed(false);
		cbbKyBaoCao.setPageLength(12);
		descService.findAllByTypeByOrderBySequencenoAsc("KYBAOCAO").forEach(item -> {
			cbbKyBaoCao.addItem(item.getId());
			cbbKyBaoCao.setItemCaption(item.getId(),item.getDescription());
		});
		cbbKyBaoCao.setValue(String.valueOf(cal.get(Calendar.MONTH) + 1) + String.valueOf(cal.get(Calendar.YEAR)));
		cbbKyBaoCao.addValueChangeListener(event -> {
			String sMaDVNB = cbbDVNB.getValue()==null ? "" : cbbDVNB.getValue().toString();
			String kyBaoCao = cbbKyBaoCao.getValue()==null ? "" : cbbKyBaoCao.getValue().toString();
//			reloadData(maDVNB,kyBaoCao);
			switch(sMaDVNB) {
			case "ACT_2090101": case "ACT_2090201": case "ACT_2090301": case "ACT_2090401": case "ACT_2090501":
			case "ACT_2090601": case "ACT_2090701": case "ACT_2090702": case "ACT_2090703": case "ACT_2090704":
				reloadData(sMaDVNB,kyBaoCao);
				if(tfSLPhatSinhThucTe.getValue().equals("0")) {
					if(cbbKyBaoCao.getValue()!=null) {
						lbNote.setValue("Dữ liệu chưa được import, bấm Import để thêm mới");
						int monthOfPeriod = Integer.valueOf(cbbKyBaoCao.getValue().toString().substring(0, 2));
		    			int yearOfPeriod = Integer.valueOf(cbbKyBaoCao.getValue().toString().substring(2, 6));
		    			tfSLPhatSinhThucTe.setValue(String.valueOf(soNgayLamViecTrongKy(monthOfPeriod-1,yearOfPeriod)));
					}
				}
				else {
					lbNote.setValue("Dữ liệu đã import. Bấm Import để nhập lại");
				}
				
    			break;
			case "ACT_2090705": case "ACT_2090706": case "ACT_2090707": case "ACT_2090708": case "ACT_2090709":
			case "ACT_2090710":	case "ACT_2090715":	case "ACT_2090801": case "ACT_2090802": case "ACT_2090803": 
			case "ACT_2090804": case "ACT_2090805":	case "ACT_2090806":
				reloadData(sMaDVNB,kyBaoCao);
				if(tfSLPhatSinhThucTe.getValue().equals("0")) {
					if(cbbKyBaoCao.getValue()!=null) {
						lbNote.setValue("Dữ liệu chưa được import, bấm Import để thêm mới");
		    			tfSLPhatSinhThucTe.setValue("1");
					}
				}
				else {
					lbNote.setValue("Dữ liệu đã import. Bấm Import để nhập lại");
				}
				break;
			case "ACT_2090711": case "ACT_2090712": case "ACT_2090713": case "ACT_2090714": 
				reloadData(sMaDVNB,kyBaoCao);
				if(tfSLPhatSinhThucTe.getValue().equals("0")) {
					if(cbbKyBaoCao.getValue()!=null) {
						lbNote.setValue("Dữ liệu chưa được import, bấm Import để thêm mới");
						int monthOfPeriod = Integer.valueOf(cbbKyBaoCao.getValue().toString().substring(0, 2));
						if(monthOfPeriod==3 || monthOfPeriod==6 || monthOfPeriod==9 || monthOfPeriod==12)
							tfSLPhatSinhThucTe.setValue("1");
					}
				}
				else {
					lbNote.setValue("Dữ liệu đã import. Bấm Import để nhập lại");
				}
    			break;
			default: 
				reloadData(sMaDVNB,kyBaoCao);
				break;
		}
			
		});
		
		tfSLPhatSinhThucTe = new TextField(SLPHATSINHTT);
		tfSLPhatSinhThucTe.setVisible(false);
		tfSLPhatSinhThucTe.setConverter(BigDecimal.class);
		tfSLPhatSinhThucTe.setValue("0");
		tfSLPhatSinhThucTe.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		tfDonGia = new TextField(DONGIA);
		tfDonGia.setVisible(false);
		tfDonGia.setConverter(BigDecimal.class);
		tfDonGia.setValue("0");
		tfDonGia.addStyleName(ValoTheme.TEXTFIELD_ALIGN_RIGHT);
		
		lbNote = new Label();
		lbNote.setVisible(false);
		
		Upload chooseFile = new Upload(null, new Upload.Receiver() {
			private static final long serialVersionUID = 1L;

			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream outputFile = null;
				try {
					
//					generatePagingLayout().setVisible(false);
					pagingLayout.setVisible(false);
					// TODO Auto-generated method stub
					fileNameImport = StringUtils.substringBefore(filename, ".xlsx") + "_" + timeConverter.getCurrentTime() + ".xlsx";
					
					Window confirmDialog = new Window();
					final FormLayout content = new FormLayout();
		            content.setMargin(true);
		            
		            Button bYes = new Button("OK");
					
					confirmDialog.setCaption("Dữ liệu excel sẽ import vào database theo DVNB & KỲ BÁO CÁO");
					confirmDialog.setWidth(450.0f, Unit.PIXELS);
			        try {
			        	if(!filename.isEmpty()) {
			        		fileImport = new File(configurationHelper.getPathFileRoot() + "/"+ fileNameImport);
				            if(!fileImport.exists()) {
				            	fileImport.createNewFile();
				            }
							outputFile =  new FileOutputStream(fileImport);
			        	
							bYes.addClickListener(event -> {
								if(cbbKyBaoCao.getValue()==null || cbbDVNB.getValue()==null) {
									Notification.show("Lỗi","Chưa chọn kỳ báo cáo và DVNB", Type.ERROR_MESSAGE);
									return;
								}
								
								String ma_DVNB = cbbDVNB.getValue().toString();
								//IMPORT TO DATABASE
								if(!ma_DVNB.isEmpty() && !cbbKyBaoCao.getValue().toString().isEmpty()) {
									InputStream is = null;
									try {
										is = new FileInputStream(fileImport);
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										LOGGER.error(e.toString());
									}
							    	LOGGER.info("Reading file " + fileImport.getName());
							    	Workbook workbook = StreamingReader.builder()
							        .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
							        .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
							        .open(is);  
									
							    	Sheet sheet = workbook.getSheetAt(0);
							    	
							    	LOGGER.info("Reading row in " + fileImport.getName());
							    	
							    	i=0;
							    	
							    	switch(ma_DVNB) {
								    	case "ACT_2070101":
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2070101 act2070101 = new Act2070101();
									    			act2070101.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2070101.setCreTms(timeConverter.getCurrentTime());
									    			act2070101.setUsrId(sUserId);
									    			act2070101.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2070101.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070101.setNgayTiepNhan(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070101.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			}
									    			act2070101.setTenChuThe(row.getCell(1).getStringCellValue());
									    			String loc = row.getCell(2).getStringCellValue();
									    			act2070101.setLoc(new BigDecimal(loc.replaceAll("[\\s|\\u00A0]+", "")));
									    			act2070101.setCif(row.getCell(3).getStringCellValue());
									    			act2070101.setCardNo(row.getCell(4).getStringCellValue());
	
									    			act2070101Service.create(act2070101);
									    		}
									    	}
							    			act2070101Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
							    			break;
							    			
								    	case "ACT_2070102":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2070102 act2070102 = new Act2070102();
									    			act2070102.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2070102.setCreTms(timeConverter.getCurrentTime());
									    			act2070102.setUsrId(sUserId);
									    			act2070102.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2070102.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070102.setNgayTiepNhan(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070102.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			}
//									    			
									    			act2070102.setTenChuThe(row.getCell(1).getStringCellValue());
									    			act2070102.setCif(row.getCell(2).getStringCellValue());
									    			String cardNo = row.getCell(3).getStringCellValue().replaceAll("[\\s|\\u00A0]+", "");
//									    			String cardNoMask = cardNo.replace(cardNo.substring(6,12), "XXXXXX");
									    			act2070102.setCardNo(cardNo);
									    			act2070102.setTerminalID(row.getCell(4).getStringCellValue());
									    			act2070102.setMaSanPham("PRO_242");
									    			
									    			act2070102Service.create(act2070102);
									    		}
									    	}
							    			act2070102Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
								    	case "ACT_2070103":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2070103 act2070103 = new Act2070103();
									    			act2070103.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2070103.setCreTms(timeConverter.getCurrentTime());
									    			act2070103.setUsrId(sUserId);
									    			act2070103.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2070103.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070103.setNgayTiepNhan(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070103.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			}
									    			act2070103.setMaThietBi(row.getCell(1).getStringCellValue());
									    			act2070103.setPan(row.getCell(2).getStringCellValue());
									    			act2070103.setMaSanPham("PRO_242");
	
									    			act2070103Service.create(act2070103);
									    		}
									    	}
							    			act2070103Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
								    	case "ACT_2070104":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0) != null) {
									    			i++;
									    			Act2070104 act2070104 = new Act2070104();
									    			act2070104.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2070104.setCreTms(timeConverter.getCurrentTime());
									    			act2070104.setUsrId(sUserId);
									    			act2070104.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2070104.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(4))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070104.setNgayTiepNhan(df.format(row.getCell(4).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070104.setNgayTiepNhan(row.getCell(4).getStringCellValue());
									    			}
									    			act2070104.setMid(row.getCell(1).getStringCellValue());
									    			act2070104.setTenMid(row.getCell(0).getStringCellValue());
									    			act2070104.setTid(row.getCell(2).getStringCellValue());
									    			act2070104.setCardNo(row.getCell(3).getStringCellValue().replace("*", "X"));
									    			act2070104.setMaDonVi(row.getCell(6).getStringCellValue().replace("\"", ""));
									    			act2070104.setMaSanPham("PRO_243");
									    			act2070104.setMaLoaiKhachHang("CUS_03");
									    			
									    			act2070104Service.create(act2070104);
									    		}
									    	}
								    		act2070104Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
								    	case "ACT_2070105":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0) != null) {
									    			i++;
									    			Act2070105 act2070105 = new Act2070105();
									    			act2070105.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2070105.setCreTms(timeConverter.getCurrentTime());
									    			act2070105.setUsrId(sUserId);
									    			act2070105.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2070105.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(13).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(13))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070105.setNgayTiepNhan(df.format(row.getCell(13).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070105.setNgayTiepNhan(row.getCell(13).getStringCellValue());
									    			}
									    			act2070105.setKhachHang(row.getCell(1).getStringCellValue());
									    			act2070105.setCif(row.getCell(2).getStringCellValue());
									    			act2070105.setTaiKhoanNguon(row.getCell(8).getStringCellValue());
									    			act2070105.setTaiKhoanDich(row.getCell(9).getStringCellValue());
									    			act2070105.setSoGiaoDichFcc(row.getCell(10).getStringCellValue());
									    			act2070105.setMaSanPham("PRO_255");
//									    			act2070105.setMaLoaiKhachHang("CUS_01");
									    			act2070105.setMaDonVi("ENT_" + row.getCell(10).getStringCellValue().substring(0, 3));
									    			
									    			act2070105Service.create(act2070105);
									    		}
									    	}
								    		
								    		
								    		
								    		act2070105Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		act2070105Service.updateMaLoaiKHCN(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
								    	case "ACT_2070106":
								    		for (Row row : sheet) {
								    			if(row.getCell(0)==null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK) {
								    				break;
								    			}
								    			
									    		if(row.getRowNum()>0) {
									    			i++;
									    			Act2070106 act2070106 = new Act2070106();
									    			act2070106.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2070106.setCreTms(timeConverter.getCurrentTime());
									    			act2070106.setUsrId(sUserId);
									    			act2070106.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2070106.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070106.setNgayTiepNhan(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070106.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			}
									    			act2070106.setTaiKhoanNguon(row.getCell(1).getStringCellValue());
									    			act2070106.setTaiKhoanDich(row.getCell(2).getStringCellValue());
									    			act2070106.setDichVu(row.getCell(3).getStringCellValue());
									    			act2070106.setSoTienGiaoDich(new BigDecimal(row.getCell(4).getStringCellValue()));
									    			if(row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(5))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2070106.setNgayGiaoDich(df.format(row.getCell(5).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2070106.setNgayGiaoDich(row.getCell(5).getStringCellValue());
									    			}
									    			
									    			act2070106.setMaSanPham("PRO_255");
									    			act2070106.setMaDonVi("ENT_" + row.getCell(6).getStringCellValue().substring(0, 3));
									    			
									    			String loaiKH = row.getCell(7).getStringCellValue();
									    			
									    			switch(loaiKH) {
									    				case "CANHAN":
									    					act2070106.setMaLoaiKhachHang("CUS_01");
									    					break;
									    				case "DOANHNGHIEP":
									    					act2070106.setMaLoaiKhachHang("CUS_03");
									    					break;
									    			}
									    			LOGGER.info("DichVu: " + act2070106.getDichVu());
									    			act2070106Service.create(act2070106);
									    		}
									    	}

							    		case "ACT_2080101":
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2080101 act2080101 = new Act2080101();
									    			act2080101.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080101.setCreTms(timeConverter.getCurrentTime());
									    			act2080101.setUsrId(sUserId);
									    			act2080101.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080101.setKy(cbbKyBaoCao.getValue().toString());
									    			act2080101.setCaseNo(row.getCell(0).getStringCellValue());
									    			act2080101.setThoiGian(row.getCell(1).getStringCellValue());
									    			act2080101.setCardNo(row.getCell(2).getStringCellValue());
									    			act2080101.setLoc(new BigDecimal(row.getCell(3).getStringCellValue()));
									    			act2080101.setCif(row.getCell(4).getStringCellValue());

									    			act2080101Service.create(act2080101);
									    		}
									    	}
									    	
									    	act2080101Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
							    			break;
							    			
							    		case "ACT_2080102":
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2080102 act2080102 = new Act2080102();
									    			act2080102.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080102.setCreTms(timeConverter.getCurrentTime());
									    			act2080102.setUsrId(sUserId);
									    			act2080102.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080102.setKy(cbbKyBaoCao.getValue().toString());
									    			act2080102.setCaseNo(row.getCell(1).getStringCellValue());
									    			if(row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			act2080102.setThoiGian(new BigDecimal(row.getCell(2).getNumericCellValue()));
									    			}
									    			else 
									    			{
									    				act2080102.setThoiGian(new BigDecimal(row.getCell(2).getStringCellValue()));
									    			}
									    			act2080102.setTaiKhoanDangNhap(row.getCell(3).getStringCellValue());
									    			act2080102.setKenhGd(row.getCell(4).getStringCellValue());
									    			act2080102.setTaiKhoanGiaoDich(row.getCell(5).getStringCellValue());
									    			act2080102.setCif(row.getCell(6).getStringCellValue());
									    			act2080102.setMaDonVi("ENT_" + row.getCell(8).getStringCellValue());
									    			String loaiKhachHang = row.getCell(7).getStringCellValue();
									    			switch(loaiKhachHang) {
									    				case "I":
									    					act2080102.setMaLoaiKhachHang("CUS_01");
									    					break;
									    				case "C":
									    					act2080102.setMaLoaiKhachHang("CUS_03");
									    					break;
									    			}
									    			
									    			switch(act2080102.getKenhGd()) {
									    				case "MB":
									    					act2080102.setMaSanPham("PRO_251");
									    					break;
									    				case "IB":
									    					act2080102.setMaSanPham("PRO_247");
									    					break;
									    				default:
									    					act2080102.setMaSanPham("PRO_255");
									    					break;
									    			}

									    			act2080102Service.create(act2080102);
									    		}
									    	}
									    	
//									    	act2080102Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
							    			break;
							    			
							    		case "ACT_2080104":
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2080104 act2080104 = new Act2080104();
									    			act2080104.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080104.setCreTms(timeConverter.getCurrentTime());
									    			act2080104.setUsrId(sUserId);
									    			act2080104.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080104.setKy(cbbKyBaoCao.getValue().toString());
									    			act2080104.setCaseNo(row.getCell(1).getStringCellValue());
									    			if(row.getCell(2).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
									    				act2080104.setThoiGian(new BigDecimal(row.getCell(2).getNumericCellValue()));
									    			}
									    			else 
									    			{
									    				act2080104.setThoiGian(new BigDecimal(row.getCell(2).getStringCellValue()));
									    			}
									    			act2080104.setTaiKhoanDangNhap(row.getCell(3).getStringCellValue());
									    			act2080104.setKenhGd(row.getCell(4).getStringCellValue());
									    			act2080104.setTaiKhoanGiaoDich(row.getCell(5).getStringCellValue());
									    			act2080104.setCif(row.getCell(6).getStringCellValue());
									    			act2080104.setMaDonVi("ENT_" + row.getCell(8).getStringCellValue());
									    			String loaiKhachHang = row.getCell(7).getStringCellValue();
									    			switch(loaiKhachHang) {
									    				case "I":
									    					act2080104.setMaLoaiKhachHang("CUS_01");
									    					break;
									    				case "C":
									    					act2080104.setMaLoaiKhachHang("CUS_03");
									    					break;
									    			}
									    			
									    			switch(act2080104.getKenhGd()) {
									    				case "MB":
									    					act2080104.setMaSanPham("PRO_251");
									    					break;
									    				case "IB":
									    					act2080104.setMaSanPham("PRO_247");
									    					break;
									    				default:
									    					act2080104.setMaSanPham("PRO_255");
									    					break;
									    			}

									    			act2080104Service.create(act2080104);
									    		}
									    	}
									    	
//									    	act2080104Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
							    			break;
							    			
							    		case "ACT_2080105":
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2080105 act2080105 = new Act2080105();
									    			act2080105.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080105.setCreTms(timeConverter.getCurrentTime());
									    			act2080105.setUsrId(sUserId);
									    			act2080105.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080105.setKy(cbbKyBaoCao.getValue().toString());
									    			act2080105.setCaseId(row.getCell(0).getStringCellValue());
									    			act2080105.setNgayGioGoi(row.getCell(1).getStringCellValue());
									    			act2080105.setMaKhachHang(row.getCell(2).getStringCellValue());
									    			act2080105.setTenKhachHang(row.getCell(3).getStringCellValue());
									    			act2080105.setDienThoai(row.getCell(4).getStringCellValue());
									    			System.out.println(row.getCell(5).getStringCellValue().trim());
									    			System.out.println(row.getCell(6).getStringCellValue().trim());
									    			act2080105.setMaNghiepVu(row.getCell(6).getStringCellValue().trim());

									    			act2080105Service.create(act2080105);
									    		}
									    	}
									    	
							    			act2080105Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
							    			break;
							    			
							    		case "ACT_2080106":
//							    			sheet = workbook.getSheetAt(1);
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2080106 act2080106 = new Act2080106();
									    			act2080106.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080106.setCreTms(timeConverter.getCurrentTime());
									    			act2080106.setUsrId(sUserId);
									    			act2080106.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080106.setKy(cbbKyBaoCao.getValue().toString());
									    			act2080106.setCaseId(row.getCell(0).getStringCellValue());
									    			act2080106.setNgayGioGoi(row.getCell(1).getStringCellValue());
									    			act2080106.setMaKhachHang(row.getCell(2).getStringCellValue());
									    			act2080106.setTenKhachHang(row.getCell(3).getStringCellValue());
									    			act2080106.setDienThoai(row.getCell(4).getStringCellValue());
									    			act2080106.setMaNghiepVu(row.getCell(5).getStringCellValue());
									    			act2080106.setMaSanPham("PRO_255");

									    			act2080106Service.create(act2080106);
									    		}
									    	}
									    	
							    			act2080106Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
							    			break;
							    			
							    		case "ACT_2080201":
//							    			sheet = workbook.getSheetAt(1);
							    			for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty() && !row.getCell(0).getStringCellValue().contains("CASE")) {
									    			i++;
									    			Act2080201 act2080201 = new Act2080201();
									    			act2080201.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080201.setCreTms(timeConverter.getCurrentTime());
									    			act2080201.setUsrId(sUserId);
									    			act2080201.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080201.setKy(cbbKyBaoCao.getValue().toString());
									    			act2080201.setCaseNo(row.getCell(0).getStringCellValue());
									    			act2080201.setThoiGianGd(row.getCell(1).getStringCellValue());
									    			act2080201.setTid(row.getCell(2).getStringCellValue());
									    			act2080201.setMid(row.getCell(3).getStringCellValue());
//									    			act2080201.setMaDonVi("ENT_" + row.getCell(4).getStringCellValue());
									    			act2080201.setMaSanPham("PRO_243");
									    			act2080201.setMaLoaiKhachHang("CUS_03");
									    			
									    			act2080201Service.create(act2080201);
									    		}
									    	}
							    			act2080201Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
											break;
											
							    		case "ACT_2080301":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0) != null) {
									    			i++;
									    			Act2080301 act2080301 = new Act2080301();
									    			act2080301.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2080301.setCreTms(timeConverter.getCurrentTime());
									    			act2080301.setUsrId(sUserId);
									    			act2080301.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2080301.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2080301.setNgayTiepNhan(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2080301.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			}
									    			act2080301.setMid(row.getCell(1).getStringCellValue());
//									    			act2080301.setTenMid(row.getCell(3).getStringCellValue());
									    			act2080301.setTid(row.getCell(2).getStringCellValue());
//									    			act2080301.setMaDonVi(row.getCell(3).getStringCellValue().replace("\"", ""));
									    			act2080301.setMaSanPham("PRO_243");
									    			act2080301.setMaLoaiKhachHang("CUS_03");
									    			
									    			act2080301Service.create(act2080301);
									    		}
									    	}
								    		act2080301Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
							    		case "ACT_2100201":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2100201 act2100201 = new Act2100201();
									    			act2100201.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2100201.setCreTms(timeConverter.getCurrentTime());
									    			act2100201.setUsrId(sUserId);
									    			act2100201.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2100201.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2100201.setNgayTiepNhan(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2100201.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			}
									    			act2100201.setBrchCode(row.getCell(1).getStringCellValue().trim());
									    			act2100201.setCif(row.getCell(2).getStringCellValue());
									    			act2100201.setCustomerName(row.getCell(3).getStringCellValue());
									    			act2100201.setPan(row.getCell(4).getStringCellValue());
									    			String loc = row.getCell(5).getStringCellValue();
									    			act2100201.setLoc(new BigDecimal(loc.replaceAll("[\\s|\\u00A0]+", "")));
	
									    			act2100201Service.create(act2100201);
									    		}
									    	}
							    			act2100201Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
							    		case "ACT_2100301":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2100301 act2100301 = new Act2100301();
									    			act2100301.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2100301.setCreTms(timeConverter.getCurrentTime());
									    			act2100301.setUsrId(sUserId);
									    			act2100301.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2100301.setKy(cbbKyBaoCao.getValue().toString());
									    			act2100301.setNgayTiepNhan(row.getCell(0).getStringCellValue());
									    			act2100301.setBrchCode(row.getCell(1).getStringCellValue().trim());
									    			act2100301.setCif(row.getCell(2).getStringCellValue());
									    			act2100301.setCustomerName(row.getCell(3).getStringCellValue());
									    			act2100301.setPan(row.getCell(4).getStringCellValue().toUpperCase());
									    			String loc = row.getCell(5).getStringCellValue();
									    			act2100301.setLoc(new BigDecimal(loc.replaceAll("[\\s|\\u00A0]+", "")));
	
									    			act2100301Service.create(act2100301);
									    		}
									    	}
							    			act2100301Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
							    		case "ACT_2110103":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2110103 act2110103 = new Act2110103();
									    			act2110103.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2110103.setCreTms(timeConverter.getCurrentTime());
									    			act2110103.setUsrId(sUserId);
									    			act2110103.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2110103.setKy(cbbKyBaoCao.getValue().toString());
									    			act2110103.setSoHoSo(row.getCell(1).getStringCellValue());
									    			act2110103.setDonViTrinhGoc(row.getCell(4).getStringCellValue().trim());
									    			if(row.getCell(10).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(10))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2110103.setNgayDeNghiPhanHoi(df.format(row.getCell(10).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2110103.setNgayDeNghiPhanHoi(row.getCell(10).getStringCellValue());
									    			}
//									    			act2110103.setNgayDeNghiPhanHoi(row.getCell(10).getStringCellValue());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act2110103.setMaDonVi("ENT_" + row.getCell(2).getStringCellValue().trim());
									    			if(row.getCell(3).getStringCellValue()!=null && !row.getCell(3).getStringCellValue().isEmpty())
									    				act2110103.setMaPhongBan("DEP_" + row.getCell(3).getStringCellValue().trim());
	
									    			act2110103Service.create(act2110103);
									    		}
									    	}
								    		break;
								    		
							    		case "ACT_2110104":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act2110104 act2110104 = new Act2110104();
									    			act2110104.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act2110104.setCreTms(timeConverter.getCurrentTime());
									    			act2110104.setUsrId(sUserId);
									    			act2110104.setMaDvnb(cbbDVNB.getValue().toString());
									    			act2110104.setKy(cbbKyBaoCao.getValue().toString());
									    			act2110104.setSoHoSo(row.getCell(1).getStringCellValue());
									    			act2110104.setDonViTrinhGoc(row.getCell(4).getStringCellValue().trim());
									    			if(row.getCell(10).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(10))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act2110104.setNgayDeNghiPhanHoi(df.format(row.getCell(10).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act2110104.setNgayDeNghiPhanHoi(row.getCell(10).getStringCellValue());
									    			}
//									    			act2110104.setNgayDeNghiPhanHoi(row.getCell(10).getStringCellValue());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act2110104.setMaDonVi("ENT_" + row.getCell(2).getStringCellValue().trim());
									    			if(row.getCell(3).getStringCellValue()!=null && !row.getCell(3).getStringCellValue().isEmpty())
									    				act2110104.setMaPhongBan("DEP_" + row.getCell(3).getStringCellValue().trim());
	
									    			act2110104Service.create(act2110104);
									    		}
									    	}
								    		break;
							    		case "ACT_3380201":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3380201 act3380201 = new Act3380201();
									    			act3380201.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3380201.setCreTms(timeConverter.getCurrentTime());
									    			act3380201.setUsrId(sUserId);
									    			act3380201.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3380201.setKy(cbbKyBaoCao.getValue().toString());
									    			act3380201.setSoHoSo(row.getCell(1).getStringCellValue());
									    			act3380201.setDonViTrinhGoc(row.getCell(4).getStringCellValue().trim());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3380201.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3380201.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3380201.setMaDonVi("ENT_" + row.getCell(2).getStringCellValue().trim());
									    			if(row.getCell(3).getStringCellValue()!=null && !row.getCell(3).getStringCellValue().isEmpty())
									    				act3380201.setMaPhongBan("DEP_" + row.getCell(3).getStringCellValue().trim());
	
									    			act3380201Service.create(act3380201);
									    		}
									    	}
								    		break;
							    		case "ACT_3380301":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3380301 act3380301 = new Act3380301();
									    			act3380301.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3380301.setCreTms(timeConverter.getCurrentTime());
									    			act3380301.setUsrId(sUserId);
									    			act3380301.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3380301.setKy(cbbKyBaoCao.getValue().toString());
									    			act3380301.setSoHoSo(row.getCell(1).getStringCellValue());
									    			act3380301.setDonViTrinhGoc(row.getCell(4).getStringCellValue().trim());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3380301.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3380301.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3380301.setMaDonVi("ENT_" + row.getCell(2).getStringCellValue().trim());
									    			if(row.getCell(3).getStringCellValue()!=null && !row.getCell(3).getStringCellValue().isEmpty())
									    				act3380301.setMaPhongBan("DEP_" + row.getCell(3).getStringCellValue().trim());
									    			act3380301Service.create(act3380301);
									    		}
									    	}
								    		break;
							    		case "ACT_3380401":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3380401 act3380401 = new Act3380401();
									    			act3380401.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3380401.setCreTms(timeConverter.getCurrentTime());
									    			act3380401.setUsrId(sUserId);
									    			act3380401.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3380401.setKy(cbbKyBaoCao.getValue().toString());
									    			act3380401.setTid(row.getCell(2).getStringCellValue());
									    			act3380401.setDonViYeuCau(row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3380401.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3380401.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3380401Service.create(act3380401);
									    		}
									    	}
								    		act3380401Service.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390101b":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3390101b act3390101b = new Act3390101b();
									    			act3390101b.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390101b.setCreTms(timeConverter.getCurrentTime());
									    			act3390101b.setUsrId(sUserId);
									    			act3390101b.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390101b.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390101b.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390101b.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390101b.setPhongYeuCau(row.getCell(3).getStringCellValue().trim());
									    			act3390101b.setLoaiThe(row.getCell(4).getStringCellValue());
									    			if(row.getCell(1).getStringCellValue()!=null && !row.getCell(1).getStringCellValue().isEmpty())
									    				act3390101b.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3390101b.setMaPhongBan("DEP_" + row.getCell(2).getStringCellValue().trim());
									    			act3390101bService.create(act3390101b);
									    		}
									    	}
								    		act3390101bService.updateErpMappingByKybaocao(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390301":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3390301 act3390301 = new Act3390301();
									    			act3390301.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390301.setCreTms(timeConverter.getCurrentTime());
									    			act3390301.setUsrId(sUserId);
									    			act3390301.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390301.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390301.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390301.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390301.setDonViYeuCauTrinh(row.getCell(3).getStringCellValue().trim());
//									    			act3390301.setTid(row.getCell(2).getStringCellValue());
									    			if(row.getCell(1).getStringCellValue()!=null && !row.getCell(1).getStringCellValue().isEmpty())
									    				act3390301.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3390301.setMaPhongBan("DEP_" + row.getCell(2).getStringCellValue().trim());
									    			act3390301.setMaSanPham("PRO_241");
									    			act3390301Service.create(act3390301);
									    		}
									    	}
								    		break;
							    		case "ACT_3390302":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3390302 act3390302 = new Act3390302();
									    			act3390302.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390302.setCreTms(timeConverter.getCurrentTime());
									    			act3390302.setUsrId(sUserId);
									    			act3390302.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390302.setKy(cbbKyBaoCao.getValue().toString());
									    			act3390302.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			act3390302.setDonViYeuCau(row.getCell(1).getStringCellValue().trim());
									    			act3390302.setMid(row.getCell(2).getStringCellValue());
									    			act3390302.setTenMid(row.getCell(3).getStringCellValue());
									    			act3390302.setTid(row.getCell(4).getStringCellValue());
									    			act3390302.setMaSanPham("PRO_243");
									    			act3390302.setMaLoaiKhachHang("CUS_04");
									    			act3390302Service.create(act3390302);
									    		}
									    	}
								    		break;
							    		case "ACT_3390901":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(0).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3390901 act3390901 = new Act3390901();
									    			act3390901.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390901.setCreTms(timeConverter.getCurrentTime());
									    			act3390901.setUsrId(sUserId);
									    			act3390901.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390901.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390901.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390901.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390901.setDonViYeuCau(row.getCell(1).getStringCellValue().trim());
									    			act3390901.setMid(row.getCell(2).getStringCellValue());
									    			act3390901.setTenMid(row.getCell(3).getStringCellValue());
									    			act3390901.setTid(row.getCell(4).getStringCellValue());
									    			if(act3390901.getMid().isEmpty()) {
									    				act3390901.setMaSanPham("PRO_241");
									    			} else {
									    				act3390901.setMaSanPham("PRO_243");
										    			act3390901.setMaLoaiKhachHang("CUS_03");
									    			}
									    			
									    			act3390901Service.create(act3390901);
									    		}
									    	}
								    		act3390901Service.updateMaDonViByTidAtm(cbbKyBaoCao.getValue().toString());
								    		act3390901Service.updateMaDonViByTidPos(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3391001":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3391001 act3391001 = new Act3391001();
									    			act3391001.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3391001.setCreTms(timeConverter.getCurrentTime());
									    			act3391001.setUsrId(sUserId);
									    			act3391001.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3391001.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3391001.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3391001.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3391001.setDonViYeuCau(row.getCell(1).getStringCellValue().trim());
									    			act3391001.setCif(row.getCell(2).getStringCellValue());
									    			act3391001.setMid(row.getCell(3).getStringCellValue());
									    			act3391001.setTenMid(row.getCell(4).getStringCellValue());
									    			act3391001.setTid(row.getCell(5).getStringCellValue());
									    			
									    			act3391001Service.create(act3391001);
									    		}
									    	}
								    		act3391001Service.updateERPByTidAtm(cbbKyBaoCao.getValue().toString());
								    		act3391001Service.updateERPByTidPos(cbbKyBaoCao.getValue().toString());
								    		act3391001Service.updateERPByCif(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3400202":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3400202 act3400202 = new Act3400202();
									    			act3400202.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3400202.setCreTms(timeConverter.getCurrentTime());
									    			act3400202.setUsrId(sUserId);
									    			act3400202.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3400202.setKy(cbbKyBaoCao.getValue().toString());
									    			act3400202.setSoHoSo(row.getCell(0).getStringCellValue());
									    			act3400202.setDonViTrinhGoc(row.getCell(3).getStringCellValue().trim());
									    			if(row.getCell(4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(4))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3400202.setNgayDeNghiPhanHoi(df.format(row.getCell(4).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3400202.setNgayDeNghiPhanHoi(row.getCell(4).getStringCellValue());
									    			}
									    			if(row.getCell(1).getStringCellValue()!=null && !row.getCell(1).getStringCellValue().isEmpty())
									    				act3400202.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3400202.setMaPhongBan("DEP_" + row.getCell(2).getStringCellValue().trim());
	
									    			act3400202Service.create(act3400202);
									    		}
									    	}
								    		break;
							    		case "ACT_3400203":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3400203 act3400203 = new Act3400203();
									    			act3400203.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3400203.setCreTms(timeConverter.getCurrentTime());
									    			act3400203.setUsrId(sUserId);
									    			act3400203.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3400203.setKy(cbbKyBaoCao.getValue().toString());
									    			act3400203.setSoHoSo(row.getCell(0).getStringCellValue());
									    			act3400203.setDonViTrinhGoc(row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(4).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(4))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3400203.setNgayDeNghiPhanHoi(df.format(row.getCell(4).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3400203.setNgayDeNghiPhanHoi(row.getCell(4).getStringCellValue());
									    			}
									    			if(row.getCell(1).getStringCellValue()!=null && !row.getCell(1).getStringCellValue().isEmpty())
									    				act3400203.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3400203.setMaPhongBan("DEP_" + row.getCell(2).getStringCellValue().trim());
									    			
									    			act3400203Service.create(act3400203);
									    		}
									    	}
								    		break;
							    		case "ACT_3390201":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3390201 act3390201 = new Act3390201();
									    			act3390201.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390201.setCreTms(timeConverter.getCurrentTime());
									    			act3390201.setUsrId(sUserId);
									    			act3390201.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390201.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390201.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390201.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390201.setCif(row.getCell(2).getStringCellValue());
									    			act3390201.setLoaiThe(row.getCell(3).getStringCellValue());
									    			if(row.getCell(4).getStringCellValue()!=null && !row.getCell(4).getStringCellValue().isEmpty())
									    				act3390201.setHanMuc(row.getCell(4).getStringCellValue());
									    			act3390201.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			act3390201Service.create(act3390201);
									    		}
									    	}
								    		act3390201Service.updateERPByLoaiThe(cbbKyBaoCao.getValue().toString());
								    		break;
								    		
							    		case "ACT_3390401":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && !row.getCell(1).getStringCellValue().isEmpty()) {
									    			i++;
									    			Act3390401 act3390401 = new Act3390401();
									    			act3390401.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390401.setCreTms(timeConverter.getCurrentTime());
									    			act3390401.setUsrId(sUserId);
									    			act3390401.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390401.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390401.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390401.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390401.setCif(row.getCell(2).getStringCellValue());
									    			act3390401.setLoaiThe(row.getCell(3).getStringCellValue());
									    			act3390401.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			act3390401Service.create(act3390401);
									    		}
									    	}
								    		act3390401Service.updateERPByLoaiThe(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390402":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390402 act3390402 = new Act3390402();
									    			act3390402.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390402.setCreTms(timeConverter.getCurrentTime());
									    			act3390402.setUsrId(sUserId);
									    			act3390402.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390402.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390402.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390402.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390402.setCif(row.getCell(2).getStringCellValue());
									    			act3390402.setLoaiThe(row.getCell(3).getStringCellValue());
									    			act3390402.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			act3390402Service.create(act3390402);
									    		}
									    	}
								    		act3390402Service.updateERPByLoaiThe(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390501":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390501 act3390501 = new Act3390501();
									    			act3390501.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390501.setCreTms(timeConverter.getCurrentTime());
									    			act3390501.setUsrId(sUserId);
									    			act3390501.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390501.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390501.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390501.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390501.setThuongHieuThe(row.getCell(2).getStringCellValue());
									    			act3390501.setNoiDungYeuCau(row.getCell(3).getStringCellValue());
									    			act3390501.setTongSoLuong(new BigDecimal(row.getCell(4).getStringCellValue()));
									    			act3390501.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			
									    			switch(act3390501.getThuongHieuThe()) {
									    				case "VS":
									    					act3390501.setMaSanPham("PRO_222");
									    					break;
									    				case "MC":
									    					act3390501.setMaSanPham("PRO_228");
									    					break;
									    			}
									    			act3390501Service.create(act3390501);
									    		}
									    	}
								    		break;
							    		case "ACT_3390502":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390502 act3390502 = new Act3390502();
									    			act3390502.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390502.setCreTms(timeConverter.getCurrentTime());
									    			act3390502.setUsrId(sUserId);
									    			act3390502.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390502.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390502.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390502.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390502.setLoaiThe(row.getCell(2).getStringCellValue());
									    			act3390502.setLoaiNghiepVu(row.getCell(3).getStringCellValue());
									    			act3390502.setNoiDungYeuCau(row.getCell(4).getStringCellValue());
									    			act3390502.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			act3390502Service.create(act3390502);
									    		}
									    	}
								    		act3390502Service.updateERPByLoaiThe(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390701":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390701 act3390701 = new Act3390701();
									    			act3390701.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390701.setCreTms(timeConverter.getCurrentTime());
									    			act3390701.setUsrId(sUserId);
									    			act3390701.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390701.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390701.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390701.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390701.setCif(row.getCell(2).getStringCellValue());
									    			act3390701.setLoaiThe(row.getCell(3).getStringCellValue());
									    			act3390701.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			act3390701Service.create(act3390701);
									    		}
									    	}
								    		act3390701Service.updateERPByLoaiThe(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390801":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390801 act3390801 = new Act3390801();
									    			act3390801.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390801.setCreTms(timeConverter.getCurrentTime());
									    			act3390801.setUsrId(sUserId);
									    			act3390801.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390801.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390801.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390801.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390801.setCif(row.getCell(2).getStringCellValue());
									    			act3390801.setLoaiThe(row.getCell(3).getStringCellValue());
									    			act3390801.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue());
									    			act3390801Service.create(act3390801);
									    		}
									    	}
								    		act3390801Service.updateERPByLoaiThe(cbbKyBaoCao.getValue().toString());
								    		break;
							    		case "ACT_3390403":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390403 act3390403 = new Act3390403();
									    			act3390403.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390403.setCreTms(timeConverter.getCurrentTime());
									    			act3390403.setUsrId(sUserId);
									    			act3390403.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390403.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390403.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390403.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390403.setDonViYeuCau(row.getCell(3).getStringCellValue().trim());
									    			act3390403.setNoiDungYeuCau(row.getCell(4).getStringCellValue());
									    			if(row.getCell(1).getStringCellValue()!=null && !row.getCell(1).getStringCellValue().isEmpty())
									    				act3390403.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3390403.setMaPhongBan("DEP_" + row.getCell(2).getStringCellValue().trim());
									    			act3390403.setMaSanPham("PRO_241");
									    			act3390403Service.create(act3390403);
									    		}
									    	}
								    		break;
							    		case "ACT_3390601":
								    		for (Row row : sheet) {
									    		if(row.getRowNum()>0 && row.getCell(0)!=null) {
									    			i++;
									    			Act3390601 act3390601 = new Act3390601();
									    			act3390601.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
									    			act3390601.setCreTms(timeConverter.getCurrentTime());
									    			act3390601.setUsrId(sUserId);
									    			act3390601.setMaDvnb(cbbDVNB.getValue().toString());
									    			act3390601.setKy(cbbKyBaoCao.getValue().toString());
									    			if(row.getCell(0).getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
									    			{
										    			if(HSSFDateUtil.isCellDateFormatted(row.getCell(0))) {
										    				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
										    				act3390601.setNgayYeuCau(df.format(row.getCell(0).getDateCellValue()));
										    			}
									    			}
									    			else {
									    				act3390601.setNgayYeuCau(row.getCell(0).getStringCellValue());
									    			}
									    			act3390601.setDonViYeuCau(row.getCell(3).getStringCellValue().trim());
									    			act3390601.setNoiDungYeuCau(row.getCell(4).getStringCellValue());
									    			if(row.getCell(1).getStringCellValue()!=null && !row.getCell(1).getStringCellValue().isEmpty())
									    				act3390601.setMaDonVi("ENT_" + row.getCell(1).getStringCellValue().trim());
									    			if(row.getCell(2).getStringCellValue()!=null && !row.getCell(2).getStringCellValue().isEmpty())
									    				act3390601.setMaPhongBan("DEP_" + row.getCell(2).getStringCellValue().trim());
//									    			act3390601.setMaSanPham("PRO_241");
									    			act3390601Service.create(act3390601);
									    		}
									    	}
								    		break;
							    	}
							    	
								}
								
					        	confirmDialog.close();
					        });
					        
							//-----------------
							VerticalLayout layoutBtn = new VerticalLayout();
				            layoutBtn.addComponents(bYes);
				            layoutBtn.setComponentAlignment(bYes, Alignment.BOTTOM_CENTER);
				            content.addComponent(layoutBtn);
				            
				            confirmDialog.setContent(content);

				            getUI().addWindow(confirmDialog);
				            
				            // Center it in the browser window
				            confirmDialog.center();
				            confirmDialog.setResizable(false);
			        	
			        	} else
			        		outputFile = null;
			            
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			        
				} catch (Exception e) {
					// TODO: handle exception
					LOGGER.error(e.toString());
				}
				return outputFile;
			}
			
		});
		
		chooseFile.setButtonCaption("IMPORT");
		chooseFile.addStyleName("myCustomUpload");
		
		bOK.addClickListener(event -> {
        	confirmDialog.close();
        });
		
		btImport.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btImport.setWidth(100.0f, Unit.PIXELS);
		btImport.setIcon(FontAwesome.UPLOAD);
		btImport.setVisible(false);
		btImport.addClickListener(event -> {
			try {
				
				if(cbbKyBaoCao.getValue()==null || cbbDVNB.getValue()==null) {
					Notification.show("Lỗi","Chưa chọn kỳ báo cáo và DVNB", Type.ERROR_MESSAGE);
					return;
				}
				
				String sMaDVNB = cbbDVNB.getValue().toString();
				String sKyBaoCao = cbbKyBaoCao.getValue().toString();
				String sFromDate = sKyBaoCao.substring(2) + sKyBaoCao.substring(0, 2) + "01" + "000000000";
				String sToDate = sKyBaoCao.substring(2) + sKyBaoCao.substring(0,2) + LastDayOfMonth(Integer.valueOf(sKyBaoCao.substring(2)),Integer.valueOf(sKyBaoCao.substring(0,2))) + "999999999";
				i=0;
				
				switch(sMaDVNB) {
					case "ACT_1420101":
						Act1420101 act1420101 = new Act1420101();
		    			act1420101.setCreTms(timeConverter.getCurrentTime());
		    			act1420101.setUsrId(sUserId);
		    			act1420101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act1420101.setKy(cbbKyBaoCao.getValue().toString());
		    			act1420101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act1420101Service.create(act1420101);
						break;
					case "ACT_1430101":
						Act1430101 act1430101 = new Act1430101();
		    			act1430101.setCreTms(timeConverter.getCurrentTime());
		    			act1430101.setUsrId(sUserId);
		    			act1430101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act1430101.setKy(cbbKyBaoCao.getValue().toString());
		    			act1430101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act1430101Service.create(act1430101);
						break;
					case "ACT_1440101":
						Act1440101 act1440101 = new Act1440101();
		    			act1440101.setCreTms(timeConverter.getCurrentTime());
		    			act1440101.setUsrId(sUserId);
		    			act1440101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act1440101.setKy(cbbKyBaoCao.getValue().toString());
		    			act1440101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act1440101Service.create(act1440101);
						break;
					case "ACT_1450101":
						Act1450101 act1450101 = new Act1450101();
		    			act1450101.setCreTms(timeConverter.getCurrentTime());
		    			act1450101.setUsrId(sUserId);
		    			act1450101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act1450101.setKy(cbbKyBaoCao.getValue().toString());
		    			act1450101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act1450101Service.create(act1450101);
						break;
					case "ACT_1450201":
						Act1450201 act1450201 = new Act1450201();
		    			act1450201.setCreTms(timeConverter.getCurrentTime());
		    			act1450201.setUsrId(sUserId);
		    			act1450201.setMaDvnb(cbbDVNB.getValue().toString());
		    			act1450201.setKy(cbbKyBaoCao.getValue().toString());
		    			act1450201.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act1450201Service.create(act1450201);
						break;
					case "ACT_2080101":
						act2080101Service.importGiaoDichHitRuleNhom1(sFromDate, sToDate, sUserId, cbbKyBaoCao.getValue().toString());
						break;
					
					case "ACT_2080102":
						try {
							act2080102Service.importGiaoDichEbankHitRuleNhom1(sUserId, cbbKyBaoCao.getValue().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							LOGGER.error(e.toString());
						}
						break;
						
					case "ACT_2080103":
						act2080103Service.importGiaoDichHitRuleNhom2(sFromDate, sToDate, sUserId, cbbKyBaoCao.getValue().toString());
						break;
						
					case "ACT_2080104":
						try {
							act2080104Service.importGiaoDichEbankHitRuleNhom2(sUserId, cbbKyBaoCao.getValue().toString());
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							LOGGER.error(e.toString());
						}
						break;
						
					case "ACT_2080107":
						Act2080107 act2080107 = new Act2080107();
		    			act2080107.setCreTms(timeConverter.getCurrentTime());
		    			act2080107.setUsrId(sUserId);
		    			act2080107.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2080107.setKy(cbbKyBaoCao.getValue().toString());
		    			act2080107.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2080107Service.create(act2080107);
						break;
						
					case "ACT_2090101":
						Act2090101 act2090101 = new Act2090101();
		    			act2090101.setCreTms(timeConverter.getCurrentTime());
		    			act2090101.setUsrId(sUserId);
		    			act2090101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090101.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090101.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090101Service.create(act2090101);
						break;
						
					case "ACT_2090201":
						Act2090201 act2090201 = new Act2090201();
		    			act2090201.setCreTms(timeConverter.getCurrentTime());
		    			act2090201.setUsrId(sUserId);
		    			act2090201.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090201.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090201.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090201Service.create(act2090201);
						break;
						
					case "ACT_2090301":
						Act2090301 act2090301 = new Act2090301();
		    			act2090301.setCreTms(timeConverter.getCurrentTime());
		    			act2090301.setUsrId(sUserId);
		    			act2090301.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090301.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090301.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090301Service.create(act2090301);
						break;
						
					case "ACT_2090401":
						Act2090401 act2090401 = new Act2090401();
		    			act2090401.setCreTms(timeConverter.getCurrentTime());
		    			act2090401.setUsrId(sUserId);
		    			act2090401.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090401.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090401.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090401Service.create(act2090401);
						break;
						
					case "ACT_2090501":
						Act2090501 act2090501 = new Act2090501();
		    			act2090501.setCreTms(timeConverter.getCurrentTime());
		    			act2090501.setUsrId(sUserId);
		    			act2090501.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090501.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090501.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090501Service.create(act2090501);
						break;
						
					case "ACT_2090601":
						Act2090601 act2090601 = new Act2090601();
		    			act2090601.setCreTms(timeConverter.getCurrentTime());
		    			act2090601.setUsrId(sUserId);
		    			act2090601.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090601.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090601.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090601Service.create(act2090601);
						break;
						
					case "ACT_2090701":
						Act2090701 act2090701 = new Act2090701();
		    			act2090701.setCreTms(timeConverter.getCurrentTime());
		    			act2090701.setUsrId(sUserId);
		    			act2090701.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090701.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090701.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090701Service.create(act2090701);
						break;
						
					case "ACT_2090702":
						Act2090702 act2090702 = new Act2090702();
		    			act2090702.setCreTms(timeConverter.getCurrentTime());
		    			act2090702.setUsrId(sUserId);
		    			act2090702.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090702.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090702.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090702Service.create(act2090702);
						break;
						
					case "ACT_2090703":
						Act2090703 act2090703 = new Act2090703();
		    			act2090703.setCreTms(timeConverter.getCurrentTime());
		    			act2090703.setUsrId(sUserId);
		    			act2090703.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090703.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090703.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090703Service.create(act2090703);
						break;
						
					case "ACT_2090704":
						Act2090704 act2090704 = new Act2090704();
		    			act2090704.setCreTms(timeConverter.getCurrentTime());
		    			act2090704.setUsrId(sUserId);
		    			act2090704.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090704.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090704.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090704Service.create(act2090704);
						break;
						
					case "ACT_2090705":
						Act2090705 act2090705 = new Act2090705();
		    			act2090705.setCreTms(timeConverter.getCurrentTime());
		    			act2090705.setUsrId(sUserId);
		    			act2090705.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090705.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090705.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090705Service.create(act2090705);
						break;
						
					case "ACT_2090706":
						Act2090706 act2090706 = new Act2090706();
		    			act2090706.setCreTms(timeConverter.getCurrentTime());
		    			act2090706.setUsrId(sUserId);
		    			act2090706.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090706.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090706.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090706Service.create(act2090706);
						break;
						
					case "ACT_2090707":
						Act2090707 act2090707 = new Act2090707();
		    			act2090707.setCreTms(timeConverter.getCurrentTime());
		    			act2090707.setUsrId(sUserId);
		    			act2090707.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090707.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090707.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090707Service.create(act2090707);
						break;
						
					case "ACT_2090708":
						Act2090708 act2090708 = new Act2090708();
		    			act2090708.setCreTms(timeConverter.getCurrentTime());
		    			act2090708.setUsrId(sUserId);
		    			act2090708.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090708.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090708.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090708Service.create(act2090708);
						break;
						
					case "ACT_2090709":
						Act2090709 act2090709 = new Act2090709();
		    			act2090709.setCreTms(timeConverter.getCurrentTime());
		    			act2090709.setUsrId(sUserId);
		    			act2090709.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090709.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090709.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090709Service.create(act2090709);
						break;
						
					case "ACT_2090711":
						Act2090711 act2090711 = new Act2090711();
		    			act2090711.setCreTms(timeConverter.getCurrentTime());
		    			act2090711.setUsrId(sUserId);
		    			act2090711.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090711.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090711.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090711Service.create(act2090711);
						break;
						
					case "ACT_2090712":
						Act2090712 act2090712 = new Act2090712();
		    			act2090712.setCreTms(timeConverter.getCurrentTime());
		    			act2090712.setUsrId(sUserId);
		    			act2090712.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090712.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090712.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090712Service.create(act2090712);
						break;
						
					case "ACT_2090713":
						Act2090713 act2090713 = new Act2090713();
		    			act2090713.setCreTms(timeConverter.getCurrentTime());
		    			act2090713.setUsrId(sUserId);
		    			act2090713.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090713.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090713.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090713Service.create(act2090713);
						break;
						
					case "ACT_2090714":
						Act2090714 act2090714 = new Act2090714();
		    			act2090714.setCreTms(timeConverter.getCurrentTime());
		    			act2090714.setUsrId(sUserId);
		    			act2090714.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090714.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090714.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090714Service.create(act2090714);
						break;
						
					case "ACT_2090715":
						Act2090715 act2090715 = new Act2090715();
		    			act2090715.setCreTms(timeConverter.getCurrentTime());
		    			act2090715.setUsrId(sUserId);
		    			act2090715.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090715.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090715.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090715Service.create(act2090715);
						break;
						
					case "ACT_2090801":
						Act2090801 act2090801 = new Act2090801();
		    			act2090801.setCreTms(timeConverter.getCurrentTime());
		    			act2090801.setUsrId(sUserId);
		    			act2090801.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090801.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090801.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090801Service.create(act2090801);
						break;
						
					case "ACT_2090802":
						Act2090802 act2090802 = new Act2090802();
		    			act2090802.setCreTms(timeConverter.getCurrentTime());
		    			act2090802.setUsrId(sUserId);
		    			act2090802.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090802.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090802.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090802Service.create(act2090802);
						break;
						
					case "ACT_2090803":
						Act2090803 act2090803 = new Act2090803();
		    			act2090803.setCreTms(timeConverter.getCurrentTime());
		    			act2090803.setUsrId(sUserId);
		    			act2090803.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090803.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090803.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090803Service.create(act2090803);
						break;
						
					case "ACT_2090804":
						Act2090804 act2090804 = new Act2090804();
		    			act2090804.setCreTms(timeConverter.getCurrentTime());
		    			act2090804.setUsrId(sUserId);
		    			act2090804.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090804.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090804.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090804Service.create(act2090804);
						break;
						
					case "ACT_2090805":
						Act2090805 act2090805 = new Act2090805();
		    			act2090805.setCreTms(timeConverter.getCurrentTime());
		    			act2090805.setUsrId(sUserId);
		    			act2090805.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090805.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090805.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090805Service.create(act2090805);
						break;
						
					case "ACT_2090806":
						Act2090806 act2090806 = new Act2090806();
		    			act2090806.setCreTms(timeConverter.getCurrentTime());
		    			act2090806.setUsrId(sUserId);
		    			act2090806.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090806.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090806.setSoLuongNgayLamViec(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090806Service.create(act2090806);
						break;
						
					case "ACT_2090710":
						Act2090710 act2090710 = new Act2090710();
		    			act2090710.setCreTms(timeConverter.getCurrentTime());
		    			act2090710.setUsrId(sUserId);
		    			act2090710.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2090710.setKy(cbbKyBaoCao.getValue().toString());
		    			act2090710.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2090710Service.create(act2090710);
						break;
						
					case "ACT_2100101":
						Act2100101 act2100101 = new Act2100101();
		    			act2100101.setCreTms(timeConverter.getCurrentTime());
		    			act2100101.setUsrId(sUserId);
		    			act2100101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100101.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100101Service.create(act2100101);
						break;
						
					case "ACT_2100401":
						Act2100401 act2100401 = new Act2100401();
		    			act2100401.setCreTms(timeConverter.getCurrentTime());
		    			act2100401.setUsrId(sUserId);
		    			act2100401.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100401.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100401.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100401Service.create(act2100401);
						break;
						
					case "ACT_2100501":
						Act2100501 act2100501 = new Act2100501();
		    			act2100501.setCreTms(timeConverter.getCurrentTime());
		    			act2100501.setUsrId(sUserId);
		    			act2100501.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100501.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100501.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100501Service.create(act2100501);
						break;
						
					case "ACT_2100502":
						Act2100502 act2100502 = new Act2100502();
		    			act2100502.setCreTms(timeConverter.getCurrentTime());
		    			act2100502.setUsrId(sUserId);
		    			act2100502.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100502.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100502.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100502Service.create(act2100502);
						break;
						
					case "ACT_2100503":
						Act2100503 act2100503 = new Act2100503();
		    			act2100503.setCreTms(timeConverter.getCurrentTime());
		    			act2100503.setUsrId(sUserId);
		    			act2100503.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100503.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100503.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100503Service.create(act2100503);
						break;
						
					case "ACT_2110101":
						Act2110101 act2110101 = new Act2110101();
		    			act2110101.setCreTms(timeConverter.getCurrentTime());
		    			act2110101.setUsrId(sUserId);
		    			act2110101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110101.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110101Service.create(act2110101);
						break;
						
					case "ACT_2110102":
						Act2110102 act2110102 = new Act2110102();
		    			act2110102.setCreTms(timeConverter.getCurrentTime());
		    			act2110102.setUsrId(sUserId);
		    			act2110102.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110102.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110102.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110102Service.create(act2110102);
						break;
						
					case "ACT_2110202":
						Act2110202 act2110202 = new Act2110202();
		    			act2110202.setCreTms(timeConverter.getCurrentTime());
		    			act2110202.setUsrId(sUserId);
		    			act2110202.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110202.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110202.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110202Service.create(act2110202);
						break;
						
					case "ACT_2110203":
						Act2110203 act2110203 = new Act2110203();
		    			act2110203.setCreTms(timeConverter.getCurrentTime());
		    			act2110203.setUsrId(sUserId);
		    			act2110203.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110203.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110203.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110203Service.create(act2110203);
						break;
						
					case "ACT_2110204":
						Act2110204 act2110204 = new Act2110204();
		    			act2110204.setCreTms(timeConverter.getCurrentTime());
		    			act2110204.setUsrId(sUserId);
		    			act2110204.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110204.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110204.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110204Service.create(act2110204);
						break;
						
					case "ACT_2110205":
						Act2110205 act2110205 = new Act2110205();
		    			act2110205.setCreTms(timeConverter.getCurrentTime());
		    			act2110205.setUsrId(sUserId);
		    			act2110205.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110205.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110205.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110205Service.create(act2110205);
						break;
						
					case "ACT_2110206":
						Act2110206 act2110206 = new Act2110206();
		    			act2110206.setCreTms(timeConverter.getCurrentTime());
		    			act2110206.setUsrId(sUserId);
		    			act2110206.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110206.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110206.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110206Service.create(act2110206);
						break;
						
					case "ACT_2100601":
						Act2100601 act2100601 = new Act2100601();
		    			act2100601.setCreTms(timeConverter.getCurrentTime());
		    			act2100601.setUsrId(sUserId);
		    			act2100601.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100601.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100601.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100601Service.create(act2100601);
						break;
						
					case "ACT_2100202":
						Act2100202 act2100202 = new Act2100202();
		    			act2100202.setCreTms(timeConverter.getCurrentTime());
		    			act2100202.setUsrId(sUserId);
		    			act2100202.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2100202.setKy(cbbKyBaoCao.getValue().toString());
		    			act2100202.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2100202Service.create(act2100202);
						break;
						
					case "ACT_2110301":
						Act2110301 act2110301 = new Act2110301();
		    			act2110301.setCreTms(timeConverter.getCurrentTime());
		    			act2110301.setUsrId(sUserId);
		    			act2110301.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110301.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110301.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110301Service.create(act2110301);
						break;
						
					case "ACT_2110302":
						Act2110302 act2110302 = new Act2110302();
		    			act2110302.setCreTms(timeConverter.getCurrentTime());
		    			act2110302.setUsrId(sUserId);
		    			act2110302.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110302.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110302.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110302Service.create(act2110302);
						break;
						
					case "ACT_2110401":
						Act2110401 act2110401 = new Act2110401();
		    			act2110401.setCreTms(timeConverter.getCurrentTime());
		    			act2110401.setUsrId(sUserId);
		    			act2110401.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110401.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110401.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110401Service.create(act2110401);
						break;
						
					case "ACT_2110402":
						Act2110402 act2110402 = new Act2110402();
		    			act2110402.setCreTms(timeConverter.getCurrentTime());
		    			act2110402.setUsrId(sUserId);
		    			act2110402.setMaDvnb(cbbDVNB.getValue().toString());
		    			act2110402.setKy(cbbKyBaoCao.getValue().toString());
		    			act2110402.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			
		    			act2110402Service.create(act2110402);
						break;
						
					//PHONG KY THUAT THE VA NGAN HANG SO
					case "ACT_3370101":
						Act3370101 act3370101 = new Act3370101();
		    			act3370101.setCreTms(timeConverter.getCurrentTime());
		    			act3370101.setUsrId(sUserId);
		    			act3370101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3370101.setKy(cbbKyBaoCao.getValue().toString());
		    			act3370101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
//		    			act3370101.setDonGia(new BigDecimal(tfDonGia.getValue().replaceAll(",", "")));
		    			act3370101Service.create(act3370101);
						break;
					case "ACT_3370102":
						Act3370102 act3370102 = new Act3370102();
		    			act3370102.setCreTms(timeConverter.getCurrentTime());
		    			act3370102.setUsrId(sUserId);
		    			act3370102.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3370102.setKy(cbbKyBaoCao.getValue().toString());
		    			act3370102.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
//		    			act3370102.setDonGia(new BigDecimal(tfDonGia.getValue().replaceAll(",", "")));
		    			act3370102Service.create(act3370102);
						break;
					case "ACT_3370103":
						Act3370103 act3370103 = new Act3370103();
		    			act3370103.setCreTms(timeConverter.getCurrentTime());
		    			act3370103.setUsrId(sUserId);
		    			act3370103.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3370103.setKy(cbbKyBaoCao.getValue().toString());
		    			act3370103.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
//		    			act3370103.setDonGia(new BigDecimal(tfDonGia.getValue().replaceAll(",", "")));
		    			act3370103Service.create(act3370103);
						break;
					case "ACT_3370104":
						Act3370104 act3370104 = new Act3370104();
		    			act3370104.setCreTms(timeConverter.getCurrentTime());
		    			act3370104.setUsrId(sUserId);
		    			act3370104.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3370104.setKy(cbbKyBaoCao.getValue().toString());
		    			act3370104.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
//		    			act3370104.setDonGia(new BigDecimal(tfDonGia.getValue().replaceAll(",", "")));
		    			act3370104Service.create(act3370104);
						break;
					case "ACT_3380101":
						Act3380101 act3380101 = new Act3380101();
		    			act3380101.setCreTms(timeConverter.getCurrentTime());
		    			act3380101.setUsrId(sUserId);
		    			act3380101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3380101.setKy(cbbKyBaoCao.getValue().toString());
		    			act3380101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3380101Service.create(act3380101);
						break;
					case "ACT_3380501":
						Act3380501 act3380501 = new Act3380501();
		    			act3380501.setCreTms(timeConverter.getCurrentTime());
		    			act3380501.setUsrId(sUserId);
		    			act3380501.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3380501.setKy(cbbKyBaoCao.getValue().toString());
		    			act3380501.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3380501Service.create(act3380501);
						break;
					case "ACT_3390101":
						Act3390101 act3390101 = new Act3390101();
		    			act3390101.setCreTms(timeConverter.getCurrentTime());
		    			act3390101.setUsrId(sUserId);
		    			act3390101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3390101.setKy(cbbKyBaoCao.getValue().toString());
		    			act3390101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3390101Service.create(act3390101);
						break;
					case "ACT_3390303":
						Act3390303 act3390303 = new Act3390303();
		    			act3390303.setCreTms(timeConverter.getCurrentTime());
		    			act3390303.setUsrId(sUserId);
		    			act3390303.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3390303.setKy(cbbKyBaoCao.getValue().toString());
		    			act3390303.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3390303Service.create(act3390303);
						break;
					case "ACT_3400101":
						Act3400101 act3400101 = new Act3400101();
		    			act3400101.setCreTms(timeConverter.getCurrentTime());
		    			act3400101.setUsrId(sUserId);
		    			act3400101.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3400101.setKy(cbbKyBaoCao.getValue().toString());
		    			act3400101.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3400101Service.create(act3400101);
						break;
					case "ACT_3400102":
						Act3400102 act3400102 = new Act3400102();
		    			act3400102.setCreTms(timeConverter.getCurrentTime());
		    			act3400102.setUsrId(sUserId);
		    			act3400102.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3400102.setKy(cbbKyBaoCao.getValue().toString());
		    			act3400102.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3400102Service.create(act3400102);
						break;
					case "ACT_3400201":
						Act3400201 act3400201 = new Act3400201();
		    			act3400201.setCreTms(timeConverter.getCurrentTime());
		    			act3400201.setUsrId(sUserId);
		    			act3400201.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3400201.setKy(cbbKyBaoCao.getValue().toString());
		    			act3400201.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3400201Service.create(act3400201);
						break;
					case "ACT_3400204":
						Act3400204 act3400204 = new Act3400204();
		    			act3400204.setCreTms(timeConverter.getCurrentTime());
		    			act3400204.setUsrId(sUserId);
		    			act3400204.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3400204.setKy(cbbKyBaoCao.getValue().toString());
		    			act3400204.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3400204Service.create(act3400204);
						break;
					case "ACT_3400205":
						Act3400205 act3400205 = new Act3400205();
		    			act3400205.setCreTms(timeConverter.getCurrentTime());
		    			act3400205.setUsrId(sUserId);
		    			act3400205.setMaDvnb(cbbDVNB.getValue().toString());
		    			act3400205.setKy(cbbKyBaoCao.getValue().toString());
		    			act3400205.setSoLuongPhatSinh(new BigDecimal(tfSLPhatSinhThucTe.getValue()));
		    			act3400205Service.create(act3400205);
						break;
					
						
				}
				
				Notification.show("Dữ liệu được cập nhật.", Type.WARNING_MESSAGE);
				switch(sMaDVNB) {
					case "ACT_2090101": case "ACT_2090201": case "ACT_2090301": case "ACT_2090401": case "ACT_2090501":
					case "ACT_2090601": case "ACT_2090701": case "ACT_2090702": case "ACT_2090703": case "ACT_2090704":
					case "ACT_2090705": case "ACT_2090706": case "ACT_2090707": case "ACT_2090708": case "ACT_2090709":
					case "ACT_2090711": case "ACT_2090712": case "ACT_2090713": case "ACT_2090714": case "ACT_2090715":
					case "ACT_2090801": case "ACT_2090802": case "ACT_2090803": case "ACT_2090804": case "ACT_2090805":
					case "ACT_2090806": case "ACT_2090710":
						lbNote.setValue("Dữ liệu đã được cập nhật.");
						break;
				}
				
			}
			catch (Exception e) {
				// TODO: handle exception
				Notification.show("Lỗi ứng dụng: " + e.getMessage(), Type.ERROR_MESSAGE);
				LOGGER.error(e.getMessage());
			}
			
		});
		
		cbbDVNB.addValueChangeListener(event -> {
			String sMaDVNB = cbbDVNB.getValue()==null ? "" : cbbDVNB.getValue().toString();
			String kyBaoCao = cbbKyBaoCao.getValue()==null ? "" : cbbKyBaoCao.getValue().toString();
			grid.setVisible(false);
			
			switch(sMaDVNB) {
				case "ACT_2080101":
					btView.setVisible(true);
					tfSLPhatSinhThucTe.setVisible(false);
					tfDonGia.setVisible(false);
					btImport.setVisible(true);
					chooseFile.setVisible(false);
					cbbDVNB.setDescription("ACT_2080101 import từ FDS case hit rule nhóm 1");
					lbNote.setValue("*Import từ FDS case hit rule nhóm 1");
					lbNote.setVisible(true);
					vformLayout2nd.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
					break;
					
//				case "ACT_2080102":
//					btImport.setVisible(true);
//					chooseFile.setVisible(false);
//					cbbDVNB.setDescription("ACT_2080102 import từ FDSEbank case hit rule nhóm 1");
//					lbNote.setValue("*Import từ FDSEbank case hit rule nhóm 1");
//					lbNote.setVisible(true);
//					vformLayout2nd.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
//					break;
					
				case "ACT_2080103":
					btView.setVisible(true);
					tfSLPhatSinhThucTe.setVisible(false);
					tfDonGia.setVisible(false);
					btImport.setVisible(true);
					chooseFile.setVisible(false);
					cbbDVNB.setDescription("ACT_2080103 import từ FDS case hit rule nhóm 2");
					lbNote.setValue("*Import từ FDS case hit rule nhóm 2");
					lbNote.setVisible(true);
					vformLayout2nd.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
					break;
					
//				case "ACT_2080104":
//					btImport.setVisible(true);
//					chooseFile.setVisible(false);
//					cbbDVNB.setDescription("ACT_2080104 import từ FDSEbank case hit rule nhóm 2");
//					lbNote.setValue("*Import từ FDSEbank case hit rule nhóm 2");
//					lbNote.setVisible(true);
//					vformLayout2nd.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_CENTER);
//					break;
				
				case "ACT_1420101": case "ACT_1430101": case "ACT_1440101": case "ACT_1450101": 
				case "ACT_1450201":
				case "ACT_2080107": case "ACT_2090710": case "ACT_2100101": case "ACT_2100401":
				case "ACT_2100501": case "ACT_2100502": case "ACT_2100503": case "ACT_2110101":
				case "ACT_2110102": case "ACT_2110202": case "ACT_2110203": case "ACT_2110204": 
				case "ACT_2110205": case "ACT_2110206": case "ACT_2100601": case "ACT_2100202":
				case "ACT_2110301": case "ACT_2110302": case "ACT_2110401": case "ACT_2110402":
				case "ACT_3380101": case "ACT_3380501": case "ACT_3390101": case "ACT_3390303": 
				case "ACT_3400101": case "ACT_3400102": case "ACT_3400201": case "ACT_3400204": 
				case "ACT_3400205": case "ACT_3370101": case "ACT_3370102": case "ACT_3370103": 
				case "ACT_3370104":
					btView.setVisible(false);
					tfSLPhatSinhThucTe.setCaption(SLPHATSINHTT);
					tfSLPhatSinhThucTe.setVisible(true);
					tfDonGia.setVisible(false);
					chooseFile.setVisible(false);
					btImport.setVisible(true);
					if(sMaDVNB.equals("ACT_2090710"))
						lbNote.setVisible(true);
					else
						lbNote.setVisible(false);
					break;	
					
				case "ACT_2090101": case "ACT_2090201": case "ACT_2090301": case "ACT_2090401": case "ACT_2090501":
				case "ACT_2090601": case "ACT_2090701": case "ACT_2090702": case "ACT_2090703": case "ACT_2090704":
				case "ACT_2090705": case "ACT_2090706": case "ACT_2090707": case "ACT_2090708": case "ACT_2090709":
				case "ACT_2090711": case "ACT_2090712": case "ACT_2090713": case "ACT_2090714": case "ACT_2090715":
				case "ACT_2090801": case "ACT_2090802": case "ACT_2090803": case "ACT_2090804": case "ACT_2090805":
				case "ACT_2090806":
					btView.setVisible(false);
					tfSLPhatSinhThucTe.setCaption(SLNGAYLAMVIEC);
					tfSLPhatSinhThucTe.setVisible(true);
					tfDonGia.setVisible(false);
					chooseFile.setVisible(false);
					btImport.setVisible(true);
					lbNote.setVisible(true);
					break;	
					
//				case "ACT_3370101": case "ACT_3370102": case "ACT_3370103": case "ACT_3370104":
//					tfSLPhatSinhThucTe.setCaption(SONGAYTHUCHIEN);
//					tfSLPhatSinhThucTe.setVisible(true);
//					tfDonGia.setVisible(true);
//					chooseFile.setVisible(false);
//					btImport.setVisible(true);
//					lbNote.setVisible(false);
//					break;	
					
				
					
				default:
					btView.setVisible(true);
					tfSLPhatSinhThucTe.setVisible(false);
					tfDonGia.setVisible(false);
					btImport.setVisible(false);
					chooseFile.setVisible(true);
					cbbDVNB.setDescription("Import từ file excel");
					lbNote.setVisible(false);
					vformLayout2nd.setComponentAlignment(formLayout2nd, Alignment.MIDDLE_LEFT);
					break;
			}
			
			switch(sMaDVNB) {
				case "ACT_2090101": case "ACT_2090201": case "ACT_2090301": case "ACT_2090401": case "ACT_2090501":
				case "ACT_2090601": case "ACT_2090701": case "ACT_2090702": case "ACT_2090703": case "ACT_2090704":
					reloadData(sMaDVNB,kyBaoCao);
					if(tfSLPhatSinhThucTe.getValue().equals("0")) {
						if(cbbKyBaoCao.getValue()!=null) {
							lbNote.setValue("Dữ liệu chưa được import, bấm Import để thêm mới");
							int monthOfPeriod = Integer.valueOf(cbbKyBaoCao.getValue().toString().substring(0, 2));
			    			int yearOfPeriod = Integer.valueOf(cbbKyBaoCao.getValue().toString().substring(2, 6));
			    			tfSLPhatSinhThucTe.setValue(String.valueOf(soNgayLamViecTrongKy(monthOfPeriod-1,yearOfPeriod)));
						}
					}
					else {
						lbNote.setValue("Dữ liệu đã import. Bấm Import để nhập lại");
					}
					
	    			break;
				case "ACT_2090705": case "ACT_2090706": case "ACT_2090707": case "ACT_2090708": case "ACT_2090709":
				case "ACT_2090710":	case "ACT_2090715":	case "ACT_2090801": case "ACT_2090802": case "ACT_2090803": 
				case "ACT_2090804": case "ACT_2090805":	case "ACT_2090806":
					reloadData(sMaDVNB,kyBaoCao);
					if(tfSLPhatSinhThucTe.getValue().equals("0")) {
						if(cbbKyBaoCao.getValue()!=null) {
							lbNote.setValue("Dữ liệu chưa được import, bấm Import để thêm mới");
			    			tfSLPhatSinhThucTe.setValue("1");
						}
					}
					else {
						lbNote.setValue("Dữ liệu đã import. Bấm Import để nhập lại");
					}
					break;
				case "ACT_2090711": case "ACT_2090712": case "ACT_2090713": case "ACT_2090714": 
					reloadData(sMaDVNB,kyBaoCao);
					if(tfSLPhatSinhThucTe.getValue().equals("0")) {
						if(cbbKyBaoCao.getValue()!=null) {
							lbNote.setValue("Dữ liệu chưa được import, bấm Import để thêm mới");
							int monthOfPeriod = Integer.valueOf(cbbKyBaoCao.getValue().toString().substring(0, 2));
							if(monthOfPeriod==3 || monthOfPeriod==6 || monthOfPeriod==9 || monthOfPeriod==12)
								tfSLPhatSinhThucTe.setValue("1");
						}
					}
					else {
						lbNote.setValue("Dữ liệu đã import. Bấm Import để nhập lại");
					}
	    			break;
				default: 
					reloadData(sMaDVNB,kyBaoCao);
					break;
			}
		});
		
		btView.setStyleName(ValoTheme.BUTTON_PRIMARY);
		btView.setWidth(100.0f, Unit.PIXELS);
		btView.setIcon(FontAwesome.EYE);
		btView.addClickListener(event -> {
			if(cbbKyBaoCao.getValue()==null || cbbDVNB.getValue()==null) {
				Notification.show("Lỗi","Chưa chọn kỳ báo cáo và DVNB", Type.ERROR_MESSAGE);
				return;
			}
			
			try {
				grid.setVisible(true);
				maDVNB = cbbDVNB.getValue().toString();
				kyBaoCao = cbbKyBaoCao.getValue().toString();
				//SHOW DATA IN GRID
				grid.dataSourceAct2070101 = null;
				grid.dataSourceAct2070102 = null;
				grid.dataSourceAct2070103 = null;
				grid.dataSourceAct2070104 = null;
				grid.dataSourceAct2070105 = null;
				grid.dataSourceAct2070106 = null;
				grid.dataSourceAct2080101 = null;
				grid.dataSourceAct2080102 = null;
				grid.dataSourceAct2080103 = null;
				grid.dataSourceAct2080104 = null;
				grid.dataSourceAct2080105 = null;
				grid.dataSourceAct2080106 = null;
				grid.dataSourceAct2080201 = null;
				grid.dataSourceAct2080301 = null;
				grid.dataSourceAct2100201 = null;
				grid.dataSourceAct2100301 = null;
				grid.dataSourceAct2110103 = null;
				grid.dataSourceAct2110104 = null;
				grid.dataSourceAct3380201 = null;
				grid.dataSourceAct3380301 = null;
				grid.dataSourceAct3380401 = null;
				grid.dataSourceAct3390101b = null;
				grid.dataSourceAct3390301 = null;
				grid.dataSourceAct3390302 = null;
				grid.dataSourceAct3390901 = null;
				grid.dataSourceAct3391001 = null;
				grid.dataSourceAct3400202 = null;
				grid.dataSourceAct3400203 = null;
				grid.dataSourceAct3390201 = null;
				grid.dataSourceAct3390401 = null;
				grid.dataSourceAct3390402 = null;
				grid.dataSourceAct3390501 = null;
				grid.dataSourceAct3390502 = null;
				grid.dataSourceAct3390701 = null;
				grid.dataSourceAct3390801 = null;
				grid.dataSourceAct3390403 = null;
				grid.dataSourceAct3390601 = null;
				
				switch(maDVNB) {
					case "ACT_2070101":
						grid.dataSourceAct2070101 = getDataAct2070101(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
						break;
					case "ACT_2070102":
						grid.dataSourceAct2070102 = getDataAct2070102(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
						break;
					case "ACT_2070103":
						grid.dataSourceAct2070103 = getDataAct2070103(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
						break;
					case "ACT_2070104":
						grid.dataSourceAct2070104 = getDataAct2070104(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
						break;
					case "ACT_2070105":
						grid.dataSourceAct2070105 = getDataAct2070105(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
						break;
					case "ACT_2070106":
						grid.dataSourceAct2070106 = getDataAct2070106(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
						break;
					case "ACT_2080101":
						grid.dataSourceAct2080101 = getDataAct2080101(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
						break;
					case "ACT_2080102":
						grid.dataSourceAct2080102 = getDataAct2080102(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
						break;
					case "ACT_2080103":
						grid.dataSourceAct2080103 = getDataAct2080103(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
						break;
					case "ACT_2080104":
						grid.dataSourceAct2080104 = getDataAct2080104(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
						break;
					case "ACT_2080105":
						grid.dataSourceAct2080105 = getDataAct2080105(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "caseId"));
						break;
					case "ACT_2080106":
						grid.dataSourceAct2080106 = getDataAct2080106(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE, Sort.Direction.ASC, "caseId"));
						break;
					case "ACT_2080201":
						grid.dataSourceAct2080201 = getDataAct2080201(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_2080301":
						grid.dataSourceAct2080301 = getDataAct2080301(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_2100201":
						grid.dataSourceAct2100201 = getDataAct2100201(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_2100301":
						grid.dataSourceAct2100301 = getDataAct2100301(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_2110103":
						grid.dataSourceAct2110103 = getDataAct2110103(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_2110104":
						grid.dataSourceAct2110104 = getDataAct2110104(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3380201":
						grid.dataSourceAct3380201 = getDataAct3380201(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3380301":
						grid.dataSourceAct3380301 = getDataAct3380301(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3380401":
						grid.dataSourceAct3380401 = getDataAct3380401(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390101b":
						grid.dataSourceAct3390101b = getDataAct3390101b(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390301":
						grid.dataSourceAct3390301 = getDataAct3390301(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390302":
						grid.dataSourceAct3390302 = getDataAct3390302(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390901":
						grid.dataSourceAct3390901 = getDataAct3390901(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3391001":
						grid.dataSourceAct3391001 = getDataAct3391001(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3400202":
						grid.dataSourceAct3400202 = getDataAct3400202(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3400203":
						grid.dataSourceAct3400203 = getDataAct3400203(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390201":
						grid.dataSourceAct3390201 = getDataAct3390201(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390401":
						grid.dataSourceAct3390401 = getDataAct3390401(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390402":
						grid.dataSourceAct3390402 = getDataAct3390402(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390501":
						grid.dataSourceAct3390501 = getDataAct3390501(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390502":
						grid.dataSourceAct3390502 = getDataAct3390502(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390701":
						grid.dataSourceAct3390701 = getDataAct3390701(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390801":
						grid.dataSourceAct3390801 = getDataAct3390801(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390403":
						grid.dataSourceAct3390403 = getDataAct3390403(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
					case "ACT_3390601":
						grid.dataSourceAct3390601 = getDataAct3390601(new PageRequest(FIRST_OF_PAGE, SIZE_OF_PAGE));
						break;
						
				}
				grid.initGrid(cbbDVNB.getValue().toString(),"All");
			} catch (Exception e) {
				Notification.show("Lỗi ứng dụng: " + e.getMessage(), Type.ERROR_MESSAGE);
				LOGGER.error(e.getMessage());
			}
		});
		
		final FormLayout form = new FormLayout();
		form.setMargin(new MarginInfo(false, false, false, true));
		
		btDelete.setStyleName(ValoTheme.BUTTON_DANGER);
		btDelete.setWidth(100.0f, Unit.PIXELS);
		btDelete.setIcon(FontAwesome.REMOVE);
		btDelete.addClickListener(event -> {
			
			if(cbbKyBaoCao.getValue()==null || cbbDVNB.getValue()==null) {
				Notification.show("Lỗi","Chưa chọn kỳ báo cáo và DVNB", Type.ERROR_MESSAGE);
				return;
			}
			
			String maDVNB = cbbDVNB.getValue().toString();
			String kyBaoCao = cbbKyBaoCao.getValue().toString();
			
			form.setEnabled(false);
			chooseFile.setEnabled(false);
			
			Window confirmDialog = new Window();
			final FormLayout content = new FormLayout();
            content.setMargin(true);
            
            final Button bByUser = new Button("User");
            bByUser.setStyleName(ValoTheme.BUTTON_SMALL);
            bByUser.setWidth(50.0f, Unit.PIXELS);
            bByUser.setDescription("Xóa dữ liệu " + maDVNB + " của kỳ " + kyBaoCao + " theo user " + sUserId);
            
            final Button bByAll = new Button("All");
            bByAll.setStyleName(ValoTheme.BUTTON_SMALL);
            bByAll.setWidth(50.0f, Unit.PIXELS);
            bByAll.setDescription("Xóa dữ liệu " + maDVNB + " của cả kỳ " + kyBaoCao);
            
			confirmDialog.setCaption("Xóa dữ liệu " + maDVNB + " của kỳ " + kyBaoCao + " theo: ");
			confirmDialog.setWidth(350.0f, Unit.PIXELS);
			
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					bByUser.addClickListener(event -> {
						try {
							switch(maDVNB) {
								case "ACT_1420101":
									act1420101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_1430101":
									act1430101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_1440101":
									act1440101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_1450101":
									act1450101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_1450201":
									act1450201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2070101":
									act2070101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2070102":
									act2070102Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2070103":
									act2070103Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2070104":
									act2070104Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2070105":
									act2070105Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2070106":
									act2070106Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080101":
									act2080101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080102":
									act2080102Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080103":
									act2080103Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080104":
									act2080104Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080105":
									act2080105Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080106":
									act2080106Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080107":
									act2080107Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080201":
									act2080201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2080301":
									act2080301Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090101":
									act2090101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090201":
									act2090201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090301":
									act2090301Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090401":
									act2090401Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090501":
									act2090501Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090601":
									act2090601Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090701":
									act2090701Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090702":
									act2090702Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090703":
									act2090703Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090704":
									act2090704Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090705":
									act2090705Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090706":
									act2090706Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090707":
									act2090707Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090708":
									act2090708Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090709":
									act2090709Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090711":
									act2090711Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090712":
									act2090712Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090713":
									act2090713Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090714":
									act2090714Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090715":
									act2090715Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090801":
									act2090801Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090802":
									act2090802Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090803":
									act2090803Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090804":
									act2090804Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090805":
									act2090805Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090806":
									act2090806Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2090710":
									act2090710Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100101":
									act2100101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100201":
									act2100201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100301":
									act2100301Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100401":
									act2100401Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100501":
									act2100501Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100502":
									act2100502Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100503":
									act2100503Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110101":
									act2110101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110102":
									act2110102Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110103":
									act2110103Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110104":
									act2110104Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110202":
									act2110202Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110203":
									act2110203Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110204":
									act2110204Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110205":
									act2110205Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110206":
									act2110206Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100601":
									act2100601Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2100202":
									act2100202Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110301":
									act2110301Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110302":
									act2110302Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110401":
									act2110401Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_2110402":
									act2110402Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
									
								case "ACT_3370101":
									act3370101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3370102":
									act3370102Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3370103":
									act3370103Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3370104":
									act3370104Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3380101":
									act3380101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3380201":
									act3380201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3380301":
									act3380301Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3380401":
									act3380401Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3380501":
									act3380501Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390101":
									act3390101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390101b":
									act3390101bService.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390301":
									act3390301Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390302":
									act3390302Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390303":
									act3390303Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390901":
									act3390901Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3391001":
									act3391001Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400101":
									act3400101Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400102":
									act3400102Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400201":
									act3400201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400202":
									act3400202Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400203":
									act3400203Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400204":
									act3400204Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3400205":
									act3400205Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390201":
									act3390201Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390401":
									act3390401Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390402":
									act3390402Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390501":
									act3390501Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390502":
									act3390502Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390701":
									act3390701Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390801":
									act3390801Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390403":
									act3390403Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
								case "ACT_3390601":
									act3390601Service.deleteByKyBaoCaoAndUserId(kyBaoCao, sUserId);
									break;
							}
							Notification.show("Đã xóa dữ liệu " + maDVNB + " của kỳ " + kyBaoCao + " theo user " + sUserId, Type.WARNING_MESSAGE);
							
							confirmDialog.close();
							form.setEnabled(true);
							chooseFile.setEnabled(true);
							reloadData(maDVNB, kyBaoCao);
							switch(maDVNB) {
								case "ACT_2090101": case "ACT_2090201": case "ACT_2090301": case "ACT_2090401": case "ACT_2090501":
								case "ACT_2090601": case "ACT_2090701": case "ACT_2090702": case "ACT_2090703": case "ACT_2090704":
								case "ACT_2090705": case "ACT_2090706": case "ACT_2090707": case "ACT_2090708": case "ACT_2090709":
								case "ACT_2090711": case "ACT_2090712": case "ACT_2090713": case "ACT_2090714": case "ACT_2090715":
								case "ACT_2090801": case "ACT_2090802": case "ACT_2090803": case "ACT_2090804": case "ACT_2090805":
								case "ACT_2090806": case "ACT_2090710":
									if(tfSLPhatSinhThucTe.getValue().equals("0"))
										lbNote.setValue("Dữ liệu đã xóa.");
									else 
										lbNote.setValue("Dữ liệu chưa được xóa do không thuộc user " + sUserId);
									break;
							}
						} catch (Exception e) {
							// TODO: handle exception
							LOGGER.error(e.getMessage());
							Notification.show("Lỗi ứng dụng: " + e.getMessage(), Type.ERROR_MESSAGE);
							form.setEnabled(true);
							chooseFile.setEnabled(true);
						}
					});
					
					bByAll.addClickListener(event -> {
						try {
							switch(maDVNB) {
								case "ACT_1420101":
									act1420101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_1430101":
									act1430101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_1440101":
									act1440101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_1450101":
									act1450101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_1450201":
									act1450201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2070101":
									act2070101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2070102":
									act2070102Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2070103":
									act2070103Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2070104":
									act2070104Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2070105":
									act2070105Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2070106":
									act2070106Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080101":
									act2080101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080102":
									act2080102Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080103":
									act2080103Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080104":
									act2080104Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080105":
									act2080105Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080106":
									act2080106Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080107":
									act2080107Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080201":
									act2080201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2080301":
									act2080301Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090101":
									act2090101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090201":
									act2090201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090301":
									act2090301Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090401":
									act2090401Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090501":
									act2090501Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090601":
									act2090601Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090701":
									act2090701Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090702":
									act2090702Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090703":
									act2090703Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090704":
									act2090704Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090705":
									act2090705Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090706":
									act2090706Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090707":
									act2090707Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090708":
									act2090708Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090709":
									act2090709Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090711":
									act2090711Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090712":
									act2090712Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090713":
									act2090713Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090714":
									act2090714Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090715":
									act2090715Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090801":
									act2090801Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090802":
									act2090802Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090803":
									act2090803Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090804":
									act2090804Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090805":
									act2090805Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090806":
									act2090806Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2090710":
									act2090710Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100101":
									act2100101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100201":
									act2100201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100301":
									act2100301Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100401":
									act2100401Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100501":
									act2100501Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100502":
									act2100502Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100503":
									act2100503Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110101":
									act2110101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110102":
									act2110102Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110103":
									act2110103Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110104":
									act2110104Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110202":
									act2110202Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110203":
									act2110203Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110204":
									act2110204Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110205":
									act2110205Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110206":
									act2110206Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100601":
									act2100601Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2100202":
									act2100202Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110301":
									act2110301Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110302":
									act2110302Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110401":
									act2110401Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_2110402":
									act2110402Service.deleteByKyBaoCao(kyBaoCao);
									break;
									
								case "ACT_3370101":
									act3370101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3370102":
									act3370102Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3370103":
									act3370103Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3370104":
									act3370104Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3380101":
									act3380101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3380201":
									act3380201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3380301":
									act3380301Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3380401":
									act3380401Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3380501":
									act3380501Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390101":
									act3390101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390101b":
									act3390101bService.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390301":
									act3390301Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390302":
									act3390302Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390303":
									act3390303Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390901":
									act3390901Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3391001":
									act3391001Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400101":
									act3400101Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400102":
									act3400102Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400201":
									act3400201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400202":
									act3400202Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400203":
									act3400203Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400204":
									act3400204Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3400205":
									act3400205Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390201":
									act3390201Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390401":
									act3390401Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390402":
									act3390402Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390501":
									act3390501Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390502":
									act3390502Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390701":
									act3390701Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390801":
									act3390801Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390403":
									act3390403Service.deleteByKyBaoCao(kyBaoCao);
									break;
								case "ACT_3390601":
									act3390601Service.deleteByKyBaoCao(kyBaoCao);
									break;
							}
							Notification.show("Đã xóa dữ liệu " + maDVNB + " của kỳ " + kyBaoCao, Type.WARNING_MESSAGE);
							confirmDialog.close();
							form.setEnabled(true);
							chooseFile.setEnabled(true);
							reloadData(maDVNB, kyBaoCao);
						} catch (Exception e) {
							// TODO: handle exception
							LOGGER.error(e.getMessage());
							Notification.show("Lỗi ứng dụng: " + e.getMessage(), Type.ERROR_MESSAGE);
							form.setEnabled(true);
							chooseFile.setEnabled(true);
						}
					});
					
					confirmDialog.addCloseListener(event -> {
						form.setEnabled(true);
						chooseFile.setEnabled(true);
					});
				}
			});
			
			VerticalLayout layoutConfirmBtn = new VerticalLayout();
			HorizontalLayout layoutBtn = new HorizontalLayout();
			layoutBtn.setSpacing(true);
            layoutBtn.addComponents(bByUser);
            layoutBtn.addComponents(bByAll);
            layoutBtn.setComponentAlignment(bByUser, Alignment.BOTTOM_CENTER);
            layoutBtn.setComponentAlignment(bByAll, Alignment.BOTTOM_CENTER);
            layoutConfirmBtn.addComponent(layoutBtn);
            layoutConfirmBtn.setComponentAlignment(layoutBtn, Alignment.BOTTOM_CENTER);
            content.addComponent(layoutConfirmBtn);
            
			confirmDialog.setContent(content);

            getUI().addWindow(confirmDialog);
            
            // Center it in the browser window
            confirmDialog.center();
            confirmDialog.setResizable(false);
			
		});
		
		formLayout2nd.addComponent(chooseFile);
		formLayout2nd.setComponentAlignment(chooseFile, Alignment.MIDDLE_CENTER);
		formLayout2nd.addComponent(btImport);
		formLayout2nd.setComponentAlignment(btImport, Alignment.MIDDLE_CENTER);
		formLayout2nd.addComponent(btView);
		formLayout2nd.setComponentAlignment(btView, Alignment.MIDDLE_CENTER);
		formLayout2nd.addComponent(btDelete);
		formLayout2nd.setComponentAlignment(btDelete, Alignment.MIDDLE_CENTER);
		
		vformLayout2nd.addComponent(formLayout2nd);
		form.addComponent(cbbDVNB);
		form.addComponent(cbbKyBaoCao);
		form.addComponent(tfSLPhatSinhThucTe);
		form.addComponent(tfDonGia);
		form.addComponent(lbNote);
		form.addComponent(vformLayout2nd);
		
		
		mainLayout.addComponent(form);
		mainLayout.setSpacing(true);
		grid = new DataGridDvnbComponent();
		mainLayout.addComponent(grid);

//		generatePagingLayout().setVisible(false);
		pagingLayout = generatePagingLayout();
		pagingLayout.setSpacing(true);
		
		mainLayout.addComponent(pagingLayout);
		mainLayout.setComponentAlignment(pagingLayout, Alignment.BOTTOM_RIGHT);

		setCompositionRoot(mainLayout);
	}
	
	private Page<Act2070101> getDataAct2070101(Pageable page) {
		resultAct2070101 = act2070101Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2070101;
	}
	
	private Page<Act2070102> getDataAct2070102(Pageable page) {
		resultAct2070102 = act2070102Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2070102;
	}
	
	private Page<Act2070103> getDataAct2070103(Pageable page) {
		resultAct2070103 = act2070103Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2070103;
	}
	
	private Page<Act2070104> getDataAct2070104(Pageable page) {
		resultAct2070104 = act2070104Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2070104;
	}
	
	private Page<Act2070105> getDataAct2070105(Pageable page) {
		resultAct2070105 = act2070105Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2070105;
	}
	
	private Page<Act2070106> getDataAct2070106(Pageable page) {
		resultAct2070106 = act2070106Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2070106;
	}
	
	private Page<Act2080101> getDataAct2080101(Pageable page) {
		resultAct2080101 = act2080101Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080101;
	}
	
	private Page<Act2080102> getDataAct2080102(Pageable page) {
		resultAct2080102 = act2080102Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080102;
	}
	
	private Page<Act2080103> getDataAct2080103(Pageable page) {
		resultAct2080103 = act2080103Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080103;
	}
	
	private Page<Act2080104> getDataAct2080104(Pageable page) {
		resultAct2080104 = act2080104Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080104;
	}
	
	private Page<Act2080105> getDataAct2080105(Pageable page) {
		resultAct2080105 = act2080105Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080105;
	}
	
	private Page<Act2080106> getDataAct2080106(Pageable page) {
		resultAct2080106 = act2080106Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080106;
	}
	
	private Page<Act2080201> getDataAct2080201(Pageable page) {
		resultAct2080201 = act2080201Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080201;
	}
	
	private Page<Act2080301> getDataAct2080301(Pageable page) {
		resultAct2080301 = act2080301Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2080301;
	}
	
	private Page<Act2100201> getDataAct2100201(Pageable page) {
		resultAct2100201 = act2100201Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2100201;
	}
	
	private Page<Act2100301> getDataAct2100301(Pageable page) {
		resultAct2100301 = act2100301Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2100301;
	}
	
	private Page<Act2110103> getDataAct2110103(Pageable page) {
		resultAct2110103 = act2110103Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2110103;
	}
	
	private Page<Act2110104> getDataAct2110104(Pageable page) {
		resultAct2110104 = act2110104Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct2110104;
	}
	
	private Page<Act3380201> getDataAct3380201(Pageable page) {
		resultAct3380201 = act3380201Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3380201;
	}
	
	private Page<Act3380301> getDataAct3380301(Pageable page) {
		resultAct3380301 = act3380301Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3380301;
	}
	
	private Page<Act3380401> getDataAct3380401(Pageable page) {
		resultAct3380401 = act3380401Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3380401;
	}
	
	private Page<Act3390101b> getDataAct3390101b(Pageable page) {
		resultAct3390101b = act3390101bService.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390101b;
	}
	
	private Page<Act3390301> getDataAct3390301(Pageable page) {
		resultAct3390301 = act3390301Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390301;
	}
	
	private Page<Act3390302> getDataAct3390302(Pageable page) {
		resultAct3390302 = act3390302Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390302;
	}
	
	private Page<Act3390901> getDataAct3390901(Pageable page) {
		resultAct3390901 = act3390901Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390901;
	}
	
	private Page<Act3391001> getDataAct3391001(Pageable page) {
		resultAct3391001 = act3391001Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3391001;
	}
	
	private Page<Act3400202> getDataAct3400202(Pageable page) {
		resultAct3400202 = act3400202Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3400202;
	}
	
	private Page<Act3400203> getDataAct3400203(Pageable page) {
		resultAct3400203 = act3400203Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3400203;
	}

	private Page<Act3390201> getDataAct3390201(Pageable page) {
		resultAct3390201 = act3390201Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390201;
	}
	
	private Page<Act3390401> getDataAct3390401(Pageable page) {
		resultAct3390401 = act3390401Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390401;
	}
	
	private Page<Act3390402> getDataAct3390402(Pageable page) {
		resultAct3390402 = act3390402Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390402;
	}
	
	private Page<Act3390501> getDataAct3390501(Pageable page) {
		resultAct3390501 = act3390501Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390501;
	}
	
	private Page<Act3390502> getDataAct3390502(Pageable page) {
		resultAct3390502 = act3390502Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390502;
	}
	
	private Page<Act3390701> getDataAct3390701(Pageable page) {
		resultAct3390701 = act3390701Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390701;
	}
	
	private Page<Act3390801> getDataAct3390801(Pageable page) {
		resultAct3390801 = act3390801Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390801;
	}
	
	private Page<Act3390403> getDataAct3390403(Pageable page) {
		resultAct3390403 = act3390403Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390403;
	}
	
	private Page<Act3390601> getDataAct3390601(Pageable page) {
		resultAct3390601 = act3390601Service.findAllByKyBaoCao(kyBaoCao, page);
		return resultAct3390601;
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

		final HorizontalLayout pagingLayoutImpl = new HorizontalLayout();
		pagingLayoutImpl.setSizeUndefined();
		pagingLayoutImpl.setSpacing(true);
		pagingLayoutImpl.addComponent(btPaging);
		pagingLayoutImpl.addComponent(btPreviousPage);
		pagingLayoutImpl.addComponent(btNextPage);
		pagingLayoutImpl.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
		pagingLayoutImpl.setVisible(false);

		
		switch(maDVNB) {
			case "ACT_2070101":
				if(resultAct2070101 != null) {
					btPreviousPage.setEnabled(resultAct2070101.hasPrevious());
					btNextPage.setEnabled(resultAct2070101.hasNext());
				}
				break;
			case "ACT_2070102":
				if(resultAct2070102!=null) {
					btPreviousPage.setEnabled(resultAct2070102.hasPrevious());
					btNextPage.setEnabled(resultAct2070102.hasNext());
				}	
			case "ACT_2070103":
				if(resultAct2070103!=null) {
					btPreviousPage.setEnabled(resultAct2070103.hasPrevious());
					btNextPage.setEnabled(resultAct2070103.hasNext());
				}	
			case "ACT_2070104":
				if(resultAct2070104!=null) {
					btPreviousPage.setEnabled(resultAct2070104.hasPrevious());
					btNextPage.setEnabled(resultAct2070104.hasNext());
				}	
			case "ACT_2070105":
				if(resultAct2070105!=null) {
					btPreviousPage.setEnabled(resultAct2070105.hasPrevious());
					btNextPage.setEnabled(resultAct2070105.hasNext());
				}	
			case "ACT_2070106":
				if(resultAct2070106!=null) {
					btPreviousPage.setEnabled(resultAct2070106.hasPrevious());
					btNextPage.setEnabled(resultAct2070106.hasNext());
				}	
			case "ACT_2080101":
				if(resultAct2080101 != null) {
					btPreviousPage.setEnabled(resultAct2080101.hasPrevious());
					btNextPage.setEnabled(resultAct2080101.hasNext());
				}
				break;
			case "ACT_2080102":
				if(resultAct2080102 != null) {
					btPreviousPage.setEnabled(resultAct2080102.hasPrevious());
					btNextPage.setEnabled(resultAct2080102.hasNext());
				}
				break;
			case "ACT_2080103":
				if(resultAct2080103 != null) {
					btPreviousPage.setEnabled(resultAct2080103.hasPrevious());
					btNextPage.setEnabled(resultAct2080103.hasNext());
				}
				break;
			case "ACT_2080104":
				if(resultAct2080104 != null) {
					btPreviousPage.setEnabled(resultAct2080104.hasPrevious());
					btNextPage.setEnabled(resultAct2080104.hasNext());
				}
				break;
			case "ACT_2080105":
				if(resultAct2080105 != null) {
					btPreviousPage.setEnabled(resultAct2080105.hasPrevious());
					btNextPage.setEnabled(resultAct2080105.hasNext());
				}
				break;
			case "ACT_2080106":
				if(resultAct2080106 != null) {
					btPreviousPage.setEnabled(resultAct2080106.hasPrevious());
					btNextPage.setEnabled(resultAct2080106.hasNext());
				}
				break;
			case "ACT_2080201":
				if(resultAct2080201 != null) {
					btPreviousPage.setEnabled(resultAct2080201.hasPrevious());
					btNextPage.setEnabled(resultAct2080201.hasNext());
				}
				break;
			case "ACT_2080301":
				if(resultAct2080301 != null) {
					btPreviousPage.setEnabled(resultAct2080301.hasPrevious());
					btNextPage.setEnabled(resultAct2080301.hasNext());
				}
				break;
			case "ACT_2100201":
				if(resultAct2100201 != null) {
					btPreviousPage.setEnabled(resultAct2100201.hasPrevious());
					btNextPage.setEnabled(resultAct2100201.hasNext());
				}
				break;
			case "ACT_2100301":
				if(resultAct2100301 != null) {
					btPreviousPage.setEnabled(resultAct2100301.hasPrevious());
					btNextPage.setEnabled(resultAct2100301.hasNext());
				}
				break;
			case "ACT_2110103":
				if(resultAct2110103 != null) {
					btPreviousPage.setEnabled(resultAct2110103.hasPrevious());
					btNextPage.setEnabled(resultAct2110103.hasNext());
				}
				break;
			case "ACT_2110104":
				if(resultAct2110104 != null) {
					btPreviousPage.setEnabled(resultAct2110104.hasPrevious());
					btNextPage.setEnabled(resultAct2110104.hasNext());
				}
				break;
			case "ACT_3380201":
				if(resultAct3380201 != null) {
					btPreviousPage.setEnabled(resultAct3380201.hasPrevious());
					btNextPage.setEnabled(resultAct3380201.hasNext());
				}
				break;
			case "ACT_3380301":
				if(resultAct3380301 != null) {
					btPreviousPage.setEnabled(resultAct3380301.hasPrevious());
					btNextPage.setEnabled(resultAct3380301.hasNext());
				}
				break;
			case "ACT_3380401":
				if(resultAct3380401 != null) {
					btPreviousPage.setEnabled(resultAct3380401.hasPrevious());
					btNextPage.setEnabled(resultAct3380401.hasNext());
				}
				break;
			case "ACT_3390101b":
				if(resultAct3390101b != null) {
					btPreviousPage.setEnabled(resultAct3390101b.hasPrevious());
					btNextPage.setEnabled(resultAct3390101b.hasNext());
				}
				break;
			case "ACT_3390301":
				if(resultAct3390301 != null) {
					btPreviousPage.setEnabled(resultAct3390301.hasPrevious());
					btNextPage.setEnabled(resultAct3390301.hasNext());
				}
				break;
			case "ACT_3390302":
				if(resultAct2110104 != null) {
					btPreviousPage.setEnabled(resultAct2110104.hasPrevious());
					btNextPage.setEnabled(resultAct2110104.hasNext());
				}
				break;
			case "ACT_3390901":
				if(resultAct3390901 != null) {
					btPreviousPage.setEnabled(resultAct3390901.hasPrevious());
					btNextPage.setEnabled(resultAct3390901.hasNext());
				}
				break;
			case "ACT_3391001":
				if(resultAct3391001 != null) {
					btPreviousPage.setEnabled(resultAct3391001.hasPrevious());
					btNextPage.setEnabled(resultAct3391001.hasNext());
				}
				break;
			case "ACT_3400202":
				if(resultAct3400202 != null) {
					btPreviousPage.setEnabled(resultAct3400202.hasPrevious());
					btNextPage.setEnabled(resultAct3400202.hasNext());
				}
				break;
			case "ACT_3400203":
				if(resultAct3400203 != null) {
					btPreviousPage.setEnabled(resultAct3400203.hasPrevious());
					btNextPage.setEnabled(resultAct3400203.hasNext());
				}
				break;
			case "ACT_3390201":
				if(resultAct3390201 != null) {
					btPreviousPage.setEnabled(resultAct3390201.hasPrevious());
					btNextPage.setEnabled(resultAct3390201.hasNext());
				}
				break;
			case "ACT_3390401":
				if(resultAct3390401 != null) {
					btPreviousPage.setEnabled(resultAct3390401.hasPrevious());
					btNextPage.setEnabled(resultAct3390401.hasNext());
				}
				break;
			case "ACT_3390402":
				if(resultAct3390402 != null) {
					btPreviousPage.setEnabled(resultAct3390402.hasPrevious());
					btNextPage.setEnabled(resultAct3390402.hasNext());
				}
				break;
			case "ACT_3390501":
				if(resultAct3390501 != null) {
					btPreviousPage.setEnabled(resultAct3390501.hasPrevious());
					btNextPage.setEnabled(resultAct3390501.hasNext());
				}
				break;
			case "ACT_3390502":
				if(resultAct3390502 != null) {
					btPreviousPage.setEnabled(resultAct3390502.hasPrevious());
					btNextPage.setEnabled(resultAct3390502.hasNext());
				}
				break;
			case "ACT_3390701":
				if(resultAct3390701 != null) {
					btPreviousPage.setEnabled(resultAct3390701.hasPrevious());
					btNextPage.setEnabled(resultAct3390701.hasNext());
				}
				break;
			case "ACT_3390801":
				if(resultAct3390801 != null) {
					btPreviousPage.setEnabled(resultAct3390801.hasPrevious());
					btNextPage.setEnabled(resultAct3390801.hasNext());
				}
				break;
			case "ACT_3390403":
				if(resultAct3390403 != null) {
					btPreviousPage.setEnabled(resultAct3390403.hasPrevious());
					btNextPage.setEnabled(resultAct3390403.hasNext());
				}
				break;
			case "ACT_3390601":
				if(resultAct3390601 != null) {
					btPreviousPage.setEnabled(resultAct3390601.hasPrevious());
					btNextPage.setEnabled(resultAct3390601.hasNext());
				}
				break;
		}

		btNextPage.addClickListener(evt -> {
			switch(maDVNB) {
				case "ACT_2070101":
					grid.dataSourceAct2070101 = getDataAct2070101(resultAct2070101.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070101.hasNext());
					btPreviousPage.setEnabled(resultAct2070101.hasPrevious());
					break;
				case "ACT_2070102":
					grid.dataSourceAct2070102 = getDataAct2070102(resultAct2070102.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070102.hasNext());
					btPreviousPage.setEnabled(resultAct2070102.hasPrevious());
					break;	
				case "ACT_2070103":
					grid.dataSourceAct2070103 = getDataAct2070103(resultAct2070103.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070103.hasNext());
					btPreviousPage.setEnabled(resultAct2070103.hasPrevious());
					break;
				case "ACT_2070104":
					grid.dataSourceAct2070104 = getDataAct2070104(resultAct2070104.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070104.hasNext());
					btPreviousPage.setEnabled(resultAct2070104.hasPrevious());
					break;
				case "ACT_2070105":
					grid.dataSourceAct2070105 = getDataAct2070105(resultAct2070105.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070105.hasNext());
					btPreviousPage.setEnabled(resultAct2070105.hasPrevious());
					break;	
				case "ACT_2070106":
					grid.dataSourceAct2070106 = getDataAct2070106(resultAct2070106.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070106.hasNext());
					btPreviousPage.setEnabled(resultAct2070106.hasPrevious());
					break;	
				case "ACT_2080101":
					grid.dataSourceAct2080101 = getDataAct2080101(resultAct2080101.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080101.hasNext());
					btPreviousPage.setEnabled(resultAct2080101.hasPrevious());
					break;
				case "ACT_2080102":
					grid.dataSourceAct2080102 = getDataAct2080102(resultAct2080102.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080102.hasNext());
					btPreviousPage.setEnabled(resultAct2080102.hasPrevious());
					break;
				case "ACT_2080103":
					grid.dataSourceAct2080103 = getDataAct2080103(resultAct2080103.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080103.hasNext());
					btPreviousPage.setEnabled(resultAct2080103.hasPrevious());
					break;
				case "ACT_2080104":
					grid.dataSourceAct2080104 = getDataAct2080104(resultAct2080104.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080104.hasNext());
					btPreviousPage.setEnabled(resultAct2080104.hasPrevious());
					break;
				case "ACT_2080105":
					grid.dataSourceAct2080105 = getDataAct2080105(resultAct2080105.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080105.hasNext());
					btPreviousPage.setEnabled(resultAct2080105.hasPrevious());
					break;
				case "ACT_2080106":
					grid.dataSourceAct2080106 = getDataAct2080106(resultAct2080106.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080106.hasNext());
					btPreviousPage.setEnabled(resultAct2080106.hasPrevious());
					break;
				case "ACT_2080201":
					grid.dataSourceAct2080201 = getDataAct2080201(resultAct2080201.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080201.hasNext());
					btPreviousPage.setEnabled(resultAct2080201.hasPrevious());
					break;
				case "ACT_2080301":
					grid.dataSourceAct2080301 = getDataAct2080301(resultAct2080301.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080301.hasNext());
					btPreviousPage.setEnabled(resultAct2080301.hasPrevious());
					break;
				case "ACT_2100201":
					grid.dataSourceAct2100201 = getDataAct2100201(resultAct2100201.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2100201.hasNext());
					btPreviousPage.setEnabled(resultAct2100201.hasPrevious());
					break;
				case "ACT_2100301":
					grid.dataSourceAct2100301 = getDataAct2100301(resultAct2100301.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2100301.hasNext());
					btPreviousPage.setEnabled(resultAct2100301.hasPrevious());
					break;
				case "ACT_2110103":
					grid.dataSourceAct2110103 = getDataAct2110103(resultAct2110103.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2110103.hasNext());
					btPreviousPage.setEnabled(resultAct2110103.hasPrevious());
					break;
				case "ACT_2110104":
					grid.dataSourceAct2110104 = getDataAct2110104(resultAct2110104.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2110104.hasNext());
					btPreviousPage.setEnabled(resultAct2110104.hasPrevious());
					break;
				case "ACT_3380201":
					grid.dataSourceAct3380201 = getDataAct3380201(resultAct3380201.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3380201.hasNext());
					btPreviousPage.setEnabled(resultAct3380201.hasPrevious());
					break;
				case "ACT_3380301":
					grid.dataSourceAct3380301 = getDataAct3380301(resultAct3380301.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3380301.hasNext());
					btPreviousPage.setEnabled(resultAct3380301.hasPrevious());
					break;
				case "ACT_3380401":
					grid.dataSourceAct3380401 = getDataAct3380401(resultAct3380401.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3380401.hasNext());
					btPreviousPage.setEnabled(resultAct3380401.hasPrevious());
					break;
				case "ACT_3390101b":
					grid.dataSourceAct3390101b = getDataAct3390101b(resultAct3390101b.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390101b.hasNext());
					btPreviousPage.setEnabled(resultAct3390101b.hasPrevious());
					break;
				case "ACT_3390301":
					grid.dataSourceAct3390301 = getDataAct3390301(resultAct3390301.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390301.hasNext());
					btPreviousPage.setEnabled(resultAct3390301.hasPrevious());
					break;
				case "ACT_3390302":
					grid.dataSourceAct3390302 = getDataAct3390302(resultAct3390302.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390302.hasNext());
					btPreviousPage.setEnabled(resultAct3390302.hasPrevious());
					break;
				case "ACT_3390901":
					grid.dataSourceAct3390901 = getDataAct3390901(resultAct3390901.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390901.hasNext());
					btPreviousPage.setEnabled(resultAct3390901.hasPrevious());
					break;
				case "ACT_3391001":
					grid.dataSourceAct3391001 = getDataAct3391001(resultAct3391001.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3391001.hasNext());
					btPreviousPage.setEnabled(resultAct3391001.hasPrevious());
					break;
				case "ACT_3400202":
					grid.dataSourceAct3400202 = getDataAct3400202(resultAct3400202.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3400202.hasNext());
					btPreviousPage.setEnabled(resultAct3400202.hasPrevious());
					break;
				case "ACT_3400203":
					grid.dataSourceAct3400203 = getDataAct3400203(resultAct3400203.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3400203.hasNext());
					btPreviousPage.setEnabled(resultAct3400203.hasPrevious());
					break;
				case "ACT_3390201":
					grid.dataSourceAct3390201 = getDataAct3390201(resultAct3390201.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390201.hasNext());
					btPreviousPage.setEnabled(resultAct3390201.hasPrevious());
					break;
				case "ACT_3390401":
					grid.dataSourceAct3390401 = getDataAct3390401(resultAct3390401.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390401.hasNext());
					btPreviousPage.setEnabled(resultAct3390401.hasPrevious());
					break;
				case "ACT_3390402":
					grid.dataSourceAct3390402 = getDataAct3390402(resultAct3390402.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390402.hasNext());
					btPreviousPage.setEnabled(resultAct3390402.hasPrevious());
					break;
				case "ACT_3390501":
					grid.dataSourceAct3390501 = getDataAct3390501(resultAct3390501.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390501.hasNext());
					btPreviousPage.setEnabled(resultAct3390501.hasPrevious());
					break;
				case "ACT_3390502":
					grid.dataSourceAct3390502 = getDataAct3390502(resultAct3390502.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390502.hasNext());
					btPreviousPage.setEnabled(resultAct3390502.hasPrevious());
					break;
				case "ACT_3390701":
					grid.dataSourceAct3390701 = getDataAct3390701(resultAct3390701.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390701.hasNext());
					btPreviousPage.setEnabled(resultAct3390701.hasPrevious());
					break;
				case "ACT_3390801":
					grid.dataSourceAct3390801 = getDataAct3390801(resultAct3390801.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390801.hasNext());
					btPreviousPage.setEnabled(resultAct3390801.hasPrevious());
					break;
				case "ACT_3390403":
					grid.dataSourceAct3390403 = getDataAct3390403(resultAct3390403.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390403.hasNext());
					btPreviousPage.setEnabled(resultAct3390403.hasPrevious());
					break;
				case "ACT_3390601":
					grid.dataSourceAct3390601 = getDataAct3390601(resultAct3390601.nextPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390601.hasNext());
					btPreviousPage.setEnabled(resultAct3390601.hasPrevious());
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
			switch(maDVNB) {
				case "ACT_2070101":
					grid.dataSourceAct2070101 = getDataAct2070101(resultAct2070101.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070101.hasNext());
					btPreviousPage.setEnabled(resultAct2070101.hasPrevious());
					break;
					
				case "ACT_2070102":
					grid.dataSourceAct2070102 = getDataAct2070102(resultAct2070102.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070102.hasNext());
					btPreviousPage.setEnabled(resultAct2070102.hasPrevious());
					break;
				case "ACT_2070103":
					grid.dataSourceAct2070103 = getDataAct2070103(resultAct2070103.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070103.hasNext());
					btPreviousPage.setEnabled(resultAct2070103.hasPrevious());
					break;
				case "ACT_2070104":
					grid.dataSourceAct2070104 = getDataAct2070104(resultAct2070104.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070104.hasNext());
					btPreviousPage.setEnabled(resultAct2070104.hasPrevious());
					break;
				case "ACT_2070105":
					grid.dataSourceAct2070105 = getDataAct2070105(resultAct2070105.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070105.hasNext());
					btPreviousPage.setEnabled(resultAct2070105.hasPrevious());
					break;
				case "ACT_2070106":
					grid.dataSourceAct2070106 = getDataAct2070106(resultAct2070106.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2070106.hasNext());
					btPreviousPage.setEnabled(resultAct2070106.hasPrevious());
					break;	
				case "ACT_2080101":
					grid.dataSourceAct2080101 = getDataAct2080101(resultAct2080101.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080101.hasNext());
					btPreviousPage.setEnabled(resultAct2080101.hasPrevious());
					break;
				case "ACT_2080102":
					grid.dataSourceAct2080102 = getDataAct2080102(resultAct2080102.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080102.hasNext());
					btPreviousPage.setEnabled(resultAct2080102.hasPrevious());
					break;
				case "ACT_2080103":
					grid.dataSourceAct2080103 = getDataAct2080103(resultAct2080103.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080103.hasNext());
					btPreviousPage.setEnabled(resultAct2080103.hasPrevious());
					break;
				case "ACT_2080104":
					grid.dataSourceAct2080104 = getDataAct2080104(resultAct2080104.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080104.hasNext());
					btPreviousPage.setEnabled(resultAct2080104.hasPrevious());
					break;
				case "ACT_2080105":
					grid.dataSourceAct2080105 = getDataAct2080105(resultAct2080105.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080105.hasNext());
					btPreviousPage.setEnabled(resultAct2080105.hasPrevious());
					break;
				case "ACT_2080106":
					grid.dataSourceAct2080106 = getDataAct2080106(resultAct2080106.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080106.hasNext());
					btPreviousPage.setEnabled(resultAct2080106.hasPrevious());
					break;
					
				case "ACT_2080201":
					grid.dataSourceAct2080201 = getDataAct2080201(resultAct2080201.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080201.hasNext());
					btPreviousPage.setEnabled(resultAct2080201.hasPrevious());
					break;
				case "ACT_2080301":
					grid.dataSourceAct2080301 = getDataAct2080301(resultAct2080301.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2080301.hasNext());
					btPreviousPage.setEnabled(resultAct2080301.hasPrevious());
					break;
				case "ACT_2100201":
					grid.dataSourceAct2100201 = getDataAct2100201(resultAct2100201.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2100201.hasNext());
					btPreviousPage.setEnabled(resultAct2100201.hasPrevious());
					break;
				case "ACT_2100301":
					grid.dataSourceAct2100301 = getDataAct2100301(resultAct2100301.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2100301.hasNext());
					btPreviousPage.setEnabled(resultAct2100301.hasPrevious());
					break;
				case "ACT_2110103":
					grid.dataSourceAct2110103 = getDataAct2110103(resultAct2110103.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2110103.hasNext());
					btPreviousPage.setEnabled(resultAct2110103.hasPrevious());
					break;
				case "ACT_2110104":
					grid.dataSourceAct2110104 = getDataAct2110104(resultAct2110104.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct2110104.hasNext());
					btPreviousPage.setEnabled(resultAct2110104.hasPrevious());
					break;
				case "ACT_3380201":
					grid.dataSourceAct3380201 = getDataAct3380201(resultAct3380201.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3380201.hasNext());
					btPreviousPage.setEnabled(resultAct3380201.hasPrevious());
					break;
				case "ACT_3380301":
					grid.dataSourceAct3380301 = getDataAct3380301(resultAct3380301.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3380301.hasNext());
					btPreviousPage.setEnabled(resultAct3380301.hasPrevious());
					break;
				case "ACT_3380401":
					grid.dataSourceAct3380401 = getDataAct3380401(resultAct3380401.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3380401.hasNext());
					btPreviousPage.setEnabled(resultAct3380401.hasPrevious());
					break;
				case "ACT_3390101b":
					grid.dataSourceAct3390101b = getDataAct3390101b(resultAct3390101b.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390101b.hasNext());
					btPreviousPage.setEnabled(resultAct3390101b.hasPrevious());
					break;
				case "ACT_3390301":
					grid.dataSourceAct3390301 = getDataAct3390301(resultAct3390301.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390301.hasNext());
					btPreviousPage.setEnabled(resultAct3390301.hasPrevious());
					break;
				case "ACT_3390302":
					grid.dataSourceAct3390302 = getDataAct3390302(resultAct3390302.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390302.hasNext());
					btPreviousPage.setEnabled(resultAct3390302.hasPrevious());
					break;
				case "ACT_3390901":
					grid.dataSourceAct3390901 = getDataAct3390901(resultAct3390901.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390901.hasNext());
					btPreviousPage.setEnabled(resultAct3390901.hasPrevious());
					break;
				case "ACT_3391001":
					grid.dataSourceAct3391001 = getDataAct3391001(resultAct3391001.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3391001.hasNext());
					btPreviousPage.setEnabled(resultAct3391001.hasPrevious());
					break;
				case "ACT_3400202":
					grid.dataSourceAct3400202 = getDataAct3400202(resultAct3400202.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3400202.hasNext());
					btPreviousPage.setEnabled(resultAct3400202.hasPrevious());
					break;
				case "ACT_3400203":
					grid.dataSourceAct3400203 = getDataAct3400203(resultAct3400203.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3400203.hasNext());
					btPreviousPage.setEnabled(resultAct3400203.hasPrevious());
					break;
				case "ACT_3390201":
					grid.dataSourceAct3390201 = getDataAct3390201(resultAct3390201.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390201.hasNext());
					btPreviousPage.setEnabled(resultAct3390201.hasPrevious());
					break;
				case "ACT_3390401":
					grid.dataSourceAct3390401 = getDataAct3390401(resultAct3390401.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390401.hasNext());
					btPreviousPage.setEnabled(resultAct3390401.hasPrevious());
					break;
				case "ACT_3390402":
					grid.dataSourceAct3390402 = getDataAct3390402(resultAct3390402.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390402.hasNext());
					btPreviousPage.setEnabled(resultAct3390402.hasPrevious());
					break;
				case "ACT_3390501":
					grid.dataSourceAct3390501 = getDataAct3390501(resultAct3390501.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390501.hasNext());
					btPreviousPage.setEnabled(resultAct3390501.hasPrevious());
					break;
				case "ACT_3390502":
					grid.dataSourceAct3390502 = getDataAct3390502(resultAct3390502.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390502.hasNext());
					btPreviousPage.setEnabled(resultAct3390502.hasPrevious());
					break;
				case "ACT_3390701":
					grid.dataSourceAct3390701 = getDataAct3390701(resultAct3390701.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390701.hasNext());
					btPreviousPage.setEnabled(resultAct3390701.hasPrevious());
					break;
				case "ACT_3390801":
					grid.dataSourceAct3390801 = getDataAct3390801(resultAct3390801.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390801.hasNext());
					btPreviousPage.setEnabled(resultAct3390801.hasPrevious());
					break;
				case "ACT_3390403":
					grid.dataSourceAct3390403 = getDataAct3390403(resultAct3390403.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390403.hasNext());
					btPreviousPage.setEnabled(resultAct3390403.hasPrevious());
					break;
				case "ACT_3390601":
					grid.dataSourceAct3390601 = getDataAct3390601(resultAct3390601.previousPageable());
					grid.refreshData();
					btNextPage.setEnabled(resultAct3390601.hasNext());
					btPreviousPage.setEnabled(resultAct3390601.hasPrevious());
					break;
			}
			
					
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
				}
			});
		});
		
		btView.addClickListener(event -> {
			pagingLayoutImpl.setVisible(true);
			switch(maDVNB) {
				case "ACT_2070101":
					btNextPage.setEnabled(resultAct2070101.hasNext());
					btPreviousPage.setEnabled(resultAct2070101.hasPrevious());
					break;
				
				case "ACT_2070102":
					btNextPage.setEnabled(resultAct2070102.hasNext());
					btPreviousPage.setEnabled(resultAct2070102.hasPrevious());
					break;
				case "ACT_2070103":
					btNextPage.setEnabled(resultAct2070103.hasNext());
					btPreviousPage.setEnabled(resultAct2070103.hasPrevious());
					break;
				case "ACT_2070104":
					btNextPage.setEnabled(resultAct2070104.hasNext());
					btPreviousPage.setEnabled(resultAct2070104.hasPrevious());
					break;
				case "ACT_2070105":
					btNextPage.setEnabled(resultAct2070105.hasNext());
					btPreviousPage.setEnabled(resultAct2070105.hasPrevious());
					break;
				case "ACT_2070106":
					btNextPage.setEnabled(resultAct2070106.hasNext());
					btPreviousPage.setEnabled(resultAct2070106.hasPrevious());
					break;	
				case "ACT_2080101":
					btNextPage.setEnabled(resultAct2080101.hasNext());
					btPreviousPage.setEnabled(resultAct2080101.hasPrevious());
					break;
				case "ACT_2080102":
					btNextPage.setEnabled(resultAct2080102.hasNext());
					btPreviousPage.setEnabled(resultAct2080102.hasPrevious());
					break;
				case "ACT_2080103":
					btNextPage.setEnabled(resultAct2080103.hasNext());
					btPreviousPage.setEnabled(resultAct2080103.hasPrevious());
					break;
				case "ACT_2080104":
					btNextPage.setEnabled(resultAct2080104.hasNext());
					btPreviousPage.setEnabled(resultAct2080104.hasPrevious());
					break;
				case "ACT_2080105":
					btNextPage.setEnabled(resultAct2080105.hasNext());
					btPreviousPage.setEnabled(resultAct2080105.hasPrevious());
					break;
				case "ACT_2080106":
					btNextPage.setEnabled(resultAct2080106.hasNext());
					btPreviousPage.setEnabled(resultAct2080106.hasPrevious());
					break;
				case "ACT_2080201":
					btNextPage.setEnabled(resultAct2080201.hasNext());
					btPreviousPage.setEnabled(resultAct2080201.hasPrevious());
					break;
				case "ACT_2080301":
					btNextPage.setEnabled(resultAct2080301.hasNext());
					btPreviousPage.setEnabled(resultAct2080301.hasPrevious());
					break;
				case "ACT_2100201":
					btNextPage.setEnabled(resultAct2100201.hasNext());
					btPreviousPage.setEnabled(resultAct2100201.hasPrevious());
					break;
				case "ACT_2100301":
					btNextPage.setEnabled(resultAct2100301.hasNext());
					btPreviousPage.setEnabled(resultAct2100301.hasPrevious());
					break;
				case "ACT_2110103":
					btNextPage.setEnabled(resultAct2110103.hasNext());
					btPreviousPage.setEnabled(resultAct2110103.hasPrevious());
					break;
				case "ACT_2110104":
					btNextPage.setEnabled(resultAct2110104.hasNext());
					btPreviousPage.setEnabled(resultAct2110104.hasPrevious());
					break;
				case "ACT_3380201":
					btPreviousPage.setEnabled(resultAct3380201.hasPrevious());
					btNextPage.setEnabled(resultAct3380201.hasNext());
					break;
				case "ACT_3380301":
					btPreviousPage.setEnabled(resultAct3380301.hasPrevious());
					btNextPage.setEnabled(resultAct3380301.hasNext());
					break;
				case "ACT_3380401":
					btPreviousPage.setEnabled(resultAct3380401.hasPrevious());
					btNextPage.setEnabled(resultAct3380401.hasNext());
					break;
				case "ACT_3390101b":
					btPreviousPage.setEnabled(resultAct3390101b.hasPrevious());
					btNextPage.setEnabled(resultAct3390101b.hasNext());
					break;
				case "ACT_3390301":
					btPreviousPage.setEnabled(resultAct3390301.hasPrevious());
					btNextPage.setEnabled(resultAct3390301.hasNext());
					break;
				case "ACT_3390302":
					btPreviousPage.setEnabled(resultAct2110104.hasPrevious());
					btNextPage.setEnabled(resultAct2110104.hasNext());
					break;
				case "ACT_3390901":
					btPreviousPage.setEnabled(resultAct3390901.hasPrevious());
					btNextPage.setEnabled(resultAct3390901.hasNext());
					break;
				case "ACT_3391001":
					btPreviousPage.setEnabled(resultAct3391001.hasPrevious());
					btNextPage.setEnabled(resultAct3391001.hasNext());
					break;
				case "ACT_3400202":
					btPreviousPage.setEnabled(resultAct3400202.hasPrevious());
					btNextPage.setEnabled(resultAct3400202.hasNext());
					break;
				case "ACT_3400203":
					btPreviousPage.setEnabled(resultAct3400203.hasPrevious());
					btNextPage.setEnabled(resultAct3400203.hasNext());
					break;
				case "ACT_3390201":
					btPreviousPage.setEnabled(resultAct3390201.hasPrevious());
					btNextPage.setEnabled(resultAct3390201.hasNext());
					break;
				case "ACT_3390401":
					btPreviousPage.setEnabled(resultAct3390401.hasPrevious());
					btNextPage.setEnabled(resultAct3390401.hasNext());
					break;
				case "ACT_3390402":
					btPreviousPage.setEnabled(resultAct3390402.hasPrevious());
					btNextPage.setEnabled(resultAct3390402.hasNext());
					break;
				case "ACT_3390501":
					btPreviousPage.setEnabled(resultAct3390501.hasPrevious());
					btNextPage.setEnabled(resultAct3390501.hasNext());
					break;
				case "ACT_3390502":
					btPreviousPage.setEnabled(resultAct3390502.hasPrevious());
					btNextPage.setEnabled(resultAct3390502.hasNext());
					break;
				case "ACT_3390701":
					btPreviousPage.setEnabled(resultAct3390701.hasPrevious());
					btNextPage.setEnabled(resultAct3390701.hasNext());
					break;
				case "ACT_3390801":
					btPreviousPage.setEnabled(resultAct3390801.hasPrevious());
					btNextPage.setEnabled(resultAct3390801.hasNext());
					break;
				case "ACT_3390403":
					btPreviousPage.setEnabled(resultAct3390403.hasPrevious());
					btNextPage.setEnabled(resultAct3390403.hasNext());
					break;
				case "ACT_3390601":
					btPreviousPage.setEnabled(resultAct3390601.hasPrevious());
					btNextPage.setEnabled(resultAct3390601.hasNext());
					break;
				default:
					pagingLayoutImpl.setVisible(false);
					break;
					
			}
			
			UI.getCurrent().access(new Runnable() {
				@Override
				public void run() {
					btPaging.setCaption(reloadLabelPaging());
					if(grid.lbNoDataFound.isVisible()) {
						pagingLayoutImpl.setVisible(false);
					}
					else {
						pagingLayoutImpl.setVisible(true);
					}
						
				}
			});
		});
		
		cbbDVNB.addValueChangeListener(event -> {
			pagingLayoutImpl.setVisible(false);
		});

		return pagingLayoutImpl;
	}

	private String reloadLabelPaging() {
		final StringBuilder sNumberOfElements = new StringBuilder();
		String sTotalElements = null;
		String sLabelPaging = "";
		switch(maDVNB) {
			case "ACT_2070101":
				if(resultAct2070101 != null) {
					if (resultAct2070101.getSize() * (resultAct2070101.getNumber() + 1) >= resultAct2070101.getTotalElements()) {
						sNumberOfElements.append(resultAct2070101.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2070101.getSize() * (resultAct2070101.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2070101.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2070102":
				if(resultAct2070102 != null) {
					if (resultAct2070102.getSize() * (resultAct2070102.getNumber() + 1) >= resultAct2070102.getTotalElements()) {
						sNumberOfElements.append(resultAct2070102.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2070102.getSize() * (resultAct2070102.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2070102.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2070103":
				if(resultAct2070103 != null) {
					if (resultAct2070103.getSize() * (resultAct2070103.getNumber() + 1) >= resultAct2070103.getTotalElements()) {
						sNumberOfElements.append(resultAct2070103.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2070103.getSize() * (resultAct2070103.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2070103.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2070104":
				if(resultAct2070104 != null) {
					if (resultAct2070104.getSize() * (resultAct2070104.getNumber() + 1) >= resultAct2070104.getTotalElements()) {
						sNumberOfElements.append(resultAct2070104.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2070104.getSize() * (resultAct2070104.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2070104.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2070105":
				if(resultAct2070105 != null) {
					if (resultAct2070105.getSize() * (resultAct2070105.getNumber() + 1) >= resultAct2070105.getTotalElements()) {
						sNumberOfElements.append(resultAct2070105.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2070105.getSize() * (resultAct2070105.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2070105.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2070106":
				if(resultAct2070106 != null) {
					if (resultAct2070106.getSize() * (resultAct2070106.getNumber() + 1) >= resultAct2070106.getTotalElements()) {
						sNumberOfElements.append(resultAct2070106.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2070106.getSize() * (resultAct2070106.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2070106.getTotalElements());
					sLabelPaging = sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080101":
				if(resultAct2080101 != null) {
					if (resultAct2080101.getSize() * (resultAct2080101.getNumber() + 1) >= resultAct2080101.getTotalElements()) {
						sNumberOfElements.append(resultAct2080101.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080101.getSize() * (resultAct2080101.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080101.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080102":
				if(resultAct2080102 != null) {
					if (resultAct2080102.getSize() * (resultAct2080102.getNumber() + 1) >= resultAct2080102.getTotalElements()) {
						sNumberOfElements.append(resultAct2080102.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080102.getSize() * (resultAct2080102.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080102.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080103":
				if(resultAct2080103 != null) {
					if (resultAct2080103.getSize() * (resultAct2080103.getNumber() + 1) >= resultAct2080103.getTotalElements()) {
						sNumberOfElements.append(resultAct2080103.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080103.getSize() * (resultAct2080103.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080103.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080104":
				if(resultAct2080104 != null) {
					if (resultAct2080104.getSize() * (resultAct2080104.getNumber() + 1) >= resultAct2080104.getTotalElements()) {
						sNumberOfElements.append(resultAct2080104.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080104.getSize() * (resultAct2080104.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080104.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080105":
				if(resultAct2080105 != null) {
					if (resultAct2080105.getSize() * (resultAct2080105.getNumber() + 1) >= resultAct2080105.getTotalElements()) {
						sNumberOfElements.append(resultAct2080105.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080105.getSize() * (resultAct2080105.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080105.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080106":
				if(resultAct2080106 != null) {
					if (resultAct2080106.getSize() * (resultAct2080106.getNumber() + 1) >= resultAct2080106.getTotalElements()) {
						sNumberOfElements.append(resultAct2080106.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080106.getSize() * (resultAct2080106.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080106.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
				
			case "ACT_2080201":
				if(resultAct2080201 != null) {
					if (resultAct2080201.getSize() * (resultAct2080201.getNumber() + 1) >= resultAct2080201.getTotalElements()) {
						sNumberOfElements.append(resultAct2080201.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080201.getSize() * (resultAct2080201.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080201.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2080301":
				if(resultAct2080301 != null) {
					if (resultAct2080301.getSize() * (resultAct2080301.getNumber() + 1) >= resultAct2080301.getTotalElements()) {
						sNumberOfElements.append(resultAct2080301.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2080301.getSize() * (resultAct2080301.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2080301.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2100201":
				if(resultAct2100201 != null) {
					if (resultAct2100201.getSize() * (resultAct2100201.getNumber() + 1) >= resultAct2100201.getTotalElements()) {
						sNumberOfElements.append(resultAct2100201.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2100201.getSize() * (resultAct2100201.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2100201.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2100301":
				if(resultAct2100301 != null) {
					if (resultAct2100301.getSize() * (resultAct2100301.getNumber() + 1) >= resultAct2100301.getTotalElements()) {
						sNumberOfElements.append(resultAct2100301.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2100301.getSize() * (resultAct2100301.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2100301.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2110103":
				if(resultAct2110103 != null) {
					if (resultAct2110103.getSize() * (resultAct2110103.getNumber() + 1) >= resultAct2110103.getTotalElements()) {
						sNumberOfElements.append(resultAct2110103.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2110103.getSize() * (resultAct2110103.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2110103.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_2110104":
				if(resultAct2110104 != null) {
					if (resultAct2110104.getSize() * (resultAct2110104.getNumber() + 1) >= resultAct2110104.getTotalElements()) {
						sNumberOfElements.append(resultAct2110104.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct2110104.getSize() * (resultAct2110104.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct2110104.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3380201":
				if(resultAct3380201 != null) {
					if (resultAct3380201.getSize() * (resultAct3380201.getNumber() + 1) >= resultAct3380201.getTotalElements()) {
						sNumberOfElements.append(resultAct3380201.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3380201.getSize() * (resultAct3380201.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3380201.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3380301":
				if(resultAct3380301 != null) {
					if (resultAct3380301.getSize() * (resultAct3380301.getNumber() + 1) >= resultAct3380301.getTotalElements()) {
						sNumberOfElements.append(resultAct3380301.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3380301.getSize() * (resultAct3380301.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3380301.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3380401":
				if(resultAct3380401 != null) {
					if (resultAct3380401.getSize() * (resultAct3380401.getNumber() + 1) >= resultAct3380401.getTotalElements()) {
						sNumberOfElements.append(resultAct3380401.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3380401.getSize() * (resultAct3380401.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3380401.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390101b":
				if(resultAct3390101b != null) {
					if (resultAct3390101b.getSize() * (resultAct3390101b.getNumber() + 1) >= resultAct3390101b.getTotalElements()) {
						sNumberOfElements.append(resultAct3390101b.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390101b.getSize() * (resultAct3390101b.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390101b.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390301":
				if(resultAct3390301 != null) {
					if (resultAct3390301.getSize() * (resultAct3390301.getNumber() + 1) >= resultAct3390301.getTotalElements()) {
						sNumberOfElements.append(resultAct3390301.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390301.getSize() * (resultAct3390301.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390301.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390302":
				if(resultAct3390302 != null) {
					if (resultAct3390302.getSize() * (resultAct3390302.getNumber() + 1) >= resultAct3390302.getTotalElements()) {
						sNumberOfElements.append(resultAct3390302.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390302.getSize() * (resultAct3390302.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390302.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390901":
				if(resultAct3390901 != null) {
					if (resultAct3390901.getSize() * (resultAct3390901.getNumber() + 1) >= resultAct3390901.getTotalElements()) {
						sNumberOfElements.append(resultAct3390901.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390901.getSize() * (resultAct3390901.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390901.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3391001":
				if(resultAct3391001 != null) {
					if (resultAct3391001.getSize() * (resultAct3391001.getNumber() + 1) >= resultAct3391001.getTotalElements()) {
						sNumberOfElements.append(resultAct3391001.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3391001.getSize() * (resultAct3391001.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3391001.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3400202":
				if(resultAct3400202 != null) {
					if (resultAct3400202.getSize() * (resultAct3400202.getNumber() + 1) >= resultAct3400202.getTotalElements()) {
						sNumberOfElements.append(resultAct3400202.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3400202.getSize() * (resultAct3400202.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3400202.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3400203":
				if(resultAct3400203 != null) {
					if (resultAct3400203.getSize() * (resultAct3400203.getNumber() + 1) >= resultAct3400203.getTotalElements()) {
						sNumberOfElements.append(resultAct3400203.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3400203.getSize() * (resultAct3400203.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3400203.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390201":
				if(resultAct3390201 != null) {
					if (resultAct3390201.getSize() * (resultAct3390201.getNumber() + 1) >= resultAct3390201.getTotalElements()) {
						sNumberOfElements.append(resultAct3390201.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390201.getSize() * (resultAct3390201.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390201.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390401":
				if(resultAct3390401 != null) {
					if (resultAct3390401.getSize() * (resultAct3390401.getNumber() + 1) >= resultAct3390401.getTotalElements()) {
						sNumberOfElements.append(resultAct3390401.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390401.getSize() * (resultAct3390401.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390401.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390402":
				if(resultAct3390402 != null) {
					if (resultAct3390402.getSize() * (resultAct3390402.getNumber() + 1) >= resultAct3390402.getTotalElements()) {
						sNumberOfElements.append(resultAct3390402.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390402.getSize() * (resultAct3390402.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390402.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390501":
				if(resultAct3390501 != null) {
					if (resultAct3390501.getSize() * (resultAct3390501.getNumber() + 1) >= resultAct3390501.getTotalElements()) {
						sNumberOfElements.append(resultAct3390501.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390501.getSize() * (resultAct3390501.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390501.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390502":
				if(resultAct3390502 != null) {
					if (resultAct3390502.getSize() * (resultAct3390502.getNumber() + 1) >= resultAct3390502.getTotalElements()) {
						sNumberOfElements.append(resultAct3390502.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390502.getSize() * (resultAct3390502.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390502.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390701":
				if(resultAct3390701 != null) {
					if (resultAct3390701.getSize() * (resultAct3390701.getNumber() + 1) >= resultAct3390701.getTotalElements()) {
						sNumberOfElements.append(resultAct3390701.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390701.getSize() * (resultAct3390701.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390701.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390801":
				if(resultAct3390801 != null) {
					if (resultAct3390801.getSize() * (resultAct3390801.getNumber() + 1) >= resultAct3390801.getTotalElements()) {
						sNumberOfElements.append(resultAct3390801.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390801.getSize() * (resultAct3390801.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390801.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390403":
				if(resultAct3390403 != null) {
					if (resultAct3390403.getSize() * (resultAct3390403.getNumber() + 1) >= resultAct3390403.getTotalElements()) {
						sNumberOfElements.append(resultAct3390403.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390403.getSize() * (resultAct3390403.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390403.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
			case "ACT_3390601":
				if(resultAct3390601 != null) {
					if (resultAct3390601.getSize() * (resultAct3390601.getNumber() + 1) >= resultAct3390601.getTotalElements()) {
						sNumberOfElements.append(resultAct3390601.getTotalElements());
					} else {
						sNumberOfElements.append(resultAct3390601.getSize() * (resultAct3390601.getNumber() + 1));
					}
					sTotalElements = Long.toString(resultAct3390601.getTotalElements());
					sLabelPaging =  sNumberOfElements.toString() + "/" + sTotalElements;
				}
				break;
		}
		return sLabelPaging;
		
	}
	
	@Override
	public void eventReload() {
		switch(maDVNB) {
			case "ACT_2070101":
				if(resultAct2070101 != null)
				{
					grid.dataSourceAct2070101 = getDataAct2070101(new PageRequest(resultAct2070101.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
					grid.refreshData();
				}
				break;
			case "ACT_2070102":
				if(resultAct2070102 != null)
				{
					grid.dataSourceAct2070102 = getDataAct2070102(new PageRequest(resultAct2070102.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
					grid.refreshData();
				}
				break;
			case "ACT_2070103":
				if(resultAct2070103 != null)
				{
					grid.dataSourceAct2070103 = getDataAct2070103(new PageRequest(resultAct2070103.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
					grid.refreshData();
				}
				break;
			case "ACT_2070104":
				if(resultAct2070104 != null)
				{
					grid.dataSourceAct2070104 = getDataAct2070104(new PageRequest(resultAct2070104.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
					grid.refreshData();
				}
				break;
			case "ACT_2070105":
				if(resultAct2070105 != null)
				{
					grid.dataSourceAct2070105 = getDataAct2070105(new PageRequest(resultAct2070105.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
					grid.refreshData();
				}
				break;
			case "ACT_2070106":
				if(resultAct2070106 != null)
				{
					grid.dataSourceAct2070106 = getDataAct2070106(new PageRequest(resultAct2070106.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "ngayTiepNhan"));
					grid.refreshData();
				}
				break;
			case "ACT_2080101":
				if(resultAct2080101 != null)
				{
					grid.dataSourceAct2080101 = getDataAct2080101(new PageRequest(resultAct2080101.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
					grid.refreshData();
				}
				break;
			case "ACT_2080102":
				if(resultAct2080102 != null)
				{
					grid.dataSourceAct2080102 = getDataAct2080102(new PageRequest(resultAct2080102.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
					grid.refreshData();
				}
				break;
			case "ACT_2080103":
				if(resultAct2080103 != null)
				{
					grid.dataSourceAct2080103 = getDataAct2080103(new PageRequest(resultAct2080103.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
					grid.refreshData();
				}
				break;
			case "ACT_2080104":
				if(resultAct2080104 != null)
				{
					grid.dataSourceAct2080104 = getDataAct2080104(new PageRequest(resultAct2080104.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "caseNo"));
					grid.refreshData();
				}
				break;
			case "ACT_2080105":
				if(resultAct2080105 != null)
				{
					grid.dataSourceAct2080105 = getDataAct2080105(new PageRequest(resultAct2080105.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "caseId"));
					grid.refreshData();
				}
				break;
			case "ACT_2080106":
				if(resultAct2080106 != null)
				{
					grid.dataSourceAct2080106 = getDataAct2080106(new PageRequest(resultAct2080106.getNumber(), SIZE_OF_PAGE, Sort.Direction.ASC, "caseId"));
					grid.refreshData();
				}
				break;
				
			case "ACT_2080201":
				if(resultAct2080201 != null)
				{
					grid.dataSourceAct2080201 = getDataAct2080201(new PageRequest(resultAct2080201.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_2080301":
				if(resultAct2080301 != null)
				{
					grid.dataSourceAct2080301 = getDataAct2080301(new PageRequest(resultAct2080301.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_2100201":
				if(resultAct2100201 != null)
				{
					grid.dataSourceAct2100201 = getDataAct2100201(new PageRequest(resultAct2100201.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_2100301":
				if(resultAct2100301 != null)
				{
					grid.dataSourceAct2100301 = getDataAct2100301(new PageRequest(resultAct2100301.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_2110103":
				if(resultAct2110103 != null)
				{
					grid.dataSourceAct2110103 = getDataAct2110103(new PageRequest(resultAct2110103.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_2110104":
				if(resultAct2110104 != null)
				{
					grid.dataSourceAct2110104 = getDataAct2110104(new PageRequest(resultAct2110104.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3380201":
				if(resultAct3380201 != null)
				{
					grid.dataSourceAct3380201 = getDataAct3380201(new PageRequest(resultAct3380201.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3380301":
				if(resultAct3380301 != null)
				{
					grid.dataSourceAct3380301 = getDataAct3380301(new PageRequest(resultAct3380301.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3380401":
				if(resultAct3380401 != null)
				{
					grid.dataSourceAct3380401 = getDataAct3380401(new PageRequest(resultAct3380401.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390101b":
				if(resultAct3390101b != null)
				{
					grid.dataSourceAct3390101b = getDataAct3390101b(new PageRequest(resultAct3390101b.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390301":
				if(resultAct3390301 != null)
				{
					grid.dataSourceAct3390301 = getDataAct3390301(new PageRequest(resultAct3390301.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390302":
				if(resultAct3390302 != null)
				{
					grid.dataSourceAct3390302 = getDataAct3390302(new PageRequest(resultAct3390302.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390901":
				if(resultAct3390901 != null)
				{
					grid.dataSourceAct3390901 = getDataAct3390901(new PageRequest(resultAct3390901.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3391001":
				if(resultAct3391001 != null)
				{
					grid.dataSourceAct3391001 = getDataAct3391001(new PageRequest(resultAct3391001.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3400202":
				if(resultAct3400202 != null)
				{
					grid.dataSourceAct3400202 = getDataAct3400202(new PageRequest(resultAct3400202.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3400203":
				if(resultAct3400203 != null)
				{
					grid.dataSourceAct3400203 = getDataAct3400203(new PageRequest(resultAct3400203.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390201":
				if(resultAct3390201 != null)
				{
					grid.dataSourceAct3390201 = getDataAct3390201(new PageRequest(resultAct3390201.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390401":
				if(resultAct3390401 != null)
				{
					grid.dataSourceAct3390401 = getDataAct3390401(new PageRequest(resultAct3390401.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390402":
				if(resultAct3390402 != null)
				{
					grid.dataSourceAct3390402 = getDataAct3390402(new PageRequest(resultAct3390402.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390501":
				if(resultAct3390501 != null)
				{
					grid.dataSourceAct3390501 = getDataAct3390501(new PageRequest(resultAct3390501.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390502":
				if(resultAct3390502 != null)
				{
					grid.dataSourceAct3390502 = getDataAct3390502(new PageRequest(resultAct3390502.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390701":
				if(resultAct3390701 != null)
				{
					grid.dataSourceAct3390701 = getDataAct3390701(new PageRequest(resultAct3390701.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390801":
				if(resultAct3390801 != null)
				{
					grid.dataSourceAct3390801 = getDataAct3390801(new PageRequest(resultAct3390801.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390403":
				if(resultAct3390403 != null)
				{
					grid.dataSourceAct3390403 = getDataAct3390403(new PageRequest(resultAct3390403.getNumber(), SIZE_OF_PAGE));
					grid.refreshData();
				}
				break;
			case "ACT_3390601":
				if(resultAct3390601 != null)
				{
					grid.dataSourceAct3390601 = getDataAct3390601(new PageRequest(resultAct3390601.getNumber(), SIZE_OF_PAGE));
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
	
	static int LastDayOfMonth(int Y, int M) {
	    return LocalDate.of(Y, M, 1).getMonth().length(Year.of(Y).isLeap());
	}

	private void reloadData(String maDVNB, String kyBaoCao) {
		BigDecimal soLuong = BigDecimal.ZERO;
		switch(maDVNB) {
			case "ACT_1420101":
				soLuong = act1420101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act1420101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_1430101":
				soLuong = act1430101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act1430101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_1440101":
				soLuong = act1440101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act1440101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_1450101":
				soLuong = act1450101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act1450101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_1450201":
				soLuong = act1450201Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act1450201Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_2080107":
				soLuong = act2080107Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2080107Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090101":
				soLuong = act2090101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090201":
				soLuong = act2090201Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090201Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090301":
				soLuong = act2090301Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090301Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090401":
				soLuong = act2090401Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090401Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090501":
				soLuong = act2090501Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090501Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090601":
				soLuong = act2090601Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090601Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090701":
				soLuong = act2090701Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090701Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090702":
				soLuong = act2090702Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090702Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090703":
				soLuong = act2090703Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090703Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090704":
				soLuong = act2090704Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090704Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090705":
				soLuong = act2090705Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090705Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090706":
				soLuong = act2090706Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090706Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090707":
				soLuong = act2090707Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090707Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090708":
				soLuong = act2090708Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090708Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090709":
				soLuong = act2090709Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090709Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090711":
				soLuong = act2090711Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090711Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090712":
				soLuong = act2090712Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090712Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090713":
				soLuong = act2090713Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090713Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090714":
				soLuong = act2090714Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090714Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090715":
				soLuong = act2090715Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090715Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090801":
				soLuong = act2090801Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090801Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090802":
				soLuong = act2090802Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090802Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090803":
				soLuong = act2090803Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090803Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090804":
				soLuong = act2090804Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090804Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090805":
				soLuong = act2090805Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090805Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			case "ACT_2090806":
				soLuong = act2090806Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090806Service.findOneByKyBaoCao(kyBaoCao).getSoLuongNgayLamViec();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2090710":
				soLuong = act2090710Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2090710Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100101":
				soLuong = act2100101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100401":
				soLuong = act2100401Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100401Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100501":
				soLuong = act2100501Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100501Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100502":
				soLuong = act2100502Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100502Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100503":
				soLuong = act2100503Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100503Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110101":
				soLuong = act2110101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110102":
				soLuong = act2110102Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110102Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110202":
				soLuong = act2110202Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110202Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110203":
				soLuong = act2110203Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110203Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110204":
				soLuong = act2110204Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110204Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110205":
				soLuong = act2110205Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110205Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110206":
				soLuong = act2110206Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110206Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100601":
				soLuong = act2100601Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100601Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2100202":
				soLuong = act2100202Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2100202Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110301":
				soLuong = act2110301Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110301Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110302":
				soLuong = act2110302Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110302Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110401":
				soLuong = act2110401Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110401Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
				
			case "ACT_2110402":
				soLuong = act2110402Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act2110402Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			
			//PHONG KY THUAT THE VA NGAN HANG SO
			case "ACT_3370101":
				Act3370101 act3370101 = act3370101Service.findOneByKyBaoCao(kyBaoCao);
				if(act3370101!=null) {
					tfSLPhatSinhThucTe.setValue(String.valueOf(act3370101.getSoLuongPhatSinh()));
	//				tfDonGia.setValue(String.valueOf(act3370101.getDonGia()));
				}
				else 
					tfSLPhatSinhThucTe.setValue("0");
				break;
			case "ACT_3370102":
				Act3370102 act3370102 = act3370102Service.findOneByKyBaoCao(kyBaoCao);
				if(act3370102!=null) {
					tfSLPhatSinhThucTe.setValue(String.valueOf(act3370102.getSoLuongPhatSinh()));
	//				tfDonGia.setValue(String.valueOf(act3370102.getDonGia()));
				}
				else 
					tfSLPhatSinhThucTe.setValue("0");
				break;
			case "ACT_3370103":
				Act3370103 act3370103 = act3370103Service.findOneByKyBaoCao(kyBaoCao);
				if(act3370103!=null) {
					tfSLPhatSinhThucTe.setValue(String.valueOf(act3370103.getSoLuongPhatSinh()));
	//				tfDonGia.setValue(String.valueOf(act3370103.getDonGia()));
				}
				else 
					tfSLPhatSinhThucTe.setValue("0");
				break;
			case "ACT_3370104":
				Act3370104 act3370104 = act3370104Service.findOneByKyBaoCao(kyBaoCao);
				if(act3370104!=null) {
					tfSLPhatSinhThucTe.setValue(String.valueOf(act3370104.getSoLuongPhatSinh()));
	//				tfDonGia.setValue(String.valueOf(act3370104.getDonGia()));
				}
				else 
					tfSLPhatSinhThucTe.setValue("0");
				break;
				
			case "ACT_3380101":
				soLuong = act3380101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3380101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3380501":
				soLuong = act3380501Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3380501Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3390101":
				soLuong = act3390101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3390101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3390303":
				soLuong = act3390303Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3390303Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3400101":
				soLuong = act3400101Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3400101Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3400102":
				soLuong = act3400102Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3400102Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3400201":
				soLuong = act3400201Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3400201Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3400204":
				soLuong = act3400204Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3400204Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
			case "ACT_3400205":
				soLuong = act3400205Service.findOneByKyBaoCao(kyBaoCao)==null ? BigDecimal.ZERO : act3400205Service.findOneByKyBaoCao(kyBaoCao).getSoLuongPhatSinh();
				tfSLPhatSinhThucTe.setValue(String.valueOf(soLuong));
				break;
		}
	}
	
	public int soNgayLamViecTrongKy(int month,int year) 
    { 
	    int numberOfSundaysInMonth=0; 
	    Calendar calendar=Calendar.getInstance(); 
	    calendar.set(year, month,1); 
	    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 
	
	    for(int i=1;i<=days;i++) 
	    { 
	    	calendar.set(year, month, i); 
            int day=calendar.get(Calendar.DAY_OF_WEEK); 
            if(day==1) 
            	numberOfSundaysInMonth++; 
	     
	    } 
	    
	    return days-numberOfSundaysInMonth; 
    } 
}
