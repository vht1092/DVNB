package com.dvnb.components;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.*;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@Scope("prototype")
public class DataGridDvnbComponent extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridDvnbComponent.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	private String maDVNB;
	
	private SpringConfigurationValueHelper configurationHelper;
	
	public Page<Act2070101> dataSourceAct2070101;
	public Page<Act2070102> dataSourceAct2070102;
	public Page<Act2070103> dataSourceAct2070103;
	public Page<Act2070104> dataSourceAct2070104;
	public Page<Act2070105> dataSourceAct2070105;
	public Page<Act2070106> dataSourceAct2070106;
	public Page<Act2080101> dataSourceAct2080101;
	public Page<Act2080102> dataSourceAct2080102;
	public Page<Act2080103> dataSourceAct2080103;
	public Page<Act2080104> dataSourceAct2080104;
	public Page<Act2080105> dataSourceAct2080105;
	public Page<Act2080106> dataSourceAct2080106;
	public Page<Act2080201> dataSourceAct2080201;
	public Page<Act2080301> dataSourceAct2080301;
	public Page<Act2100201> dataSourceAct2100201;
	public Page<Act2100301> dataSourceAct2100301;
	public Page<Act2110103> dataSourceAct2110103;
	public Page<Act2110104> dataSourceAct2110104;
	
	public Page<Act3380201> dataSourceAct3380201;
	public Page<Act3380301> dataSourceAct3380301;
	public Page<Act3380401> dataSourceAct3380401;
	public Page<Act3390101b> dataSourceAct3390101b;
	public Page<Act3390301> dataSourceAct3390301;
	public Page<Act3390302> dataSourceAct3390302;
	public Page<Act3390901> dataSourceAct3390901;
	public Page<Act3391001> dataSourceAct3391001;
	public Page<Act3400202> dataSourceAct3400202;
	public Page<Act3400203> dataSourceAct3400203;
	public Page<Act3390201> dataSourceAct3390201;
	public Page<Act3390401> dataSourceAct3390401;
	public Page<Act3390402> dataSourceAct3390402;
	public Page<Act3390501> dataSourceAct3390501;
	public Page<Act3390502> dataSourceAct3390502;
	public Page<Act3390701> dataSourceAct3390701;
	public Page<Act3390801> dataSourceAct3390801;
	public Page<Act3390403> dataSourceAct3390403;
	public Page<Act3390601> dataSourceAct3390601;
	
	@SuppressWarnings("unchecked")
	public DataGridDvnbComponent() {

		setSizeFull();
		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
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
		
		container = new IndexedContainer();
		
		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);
	}

	public void initGrid(String maDVNB, final String getColumn) {
		this.maDVNB = maDVNB;
		
		IndexedContainer containerTemp = new IndexedContainer();
		try {
			containerTemp = (IndexedContainer) container.clone();
			for (Object propertyId : containerTemp.getContainerPropertyIds()) {
			    container.removeContainerProperty(propertyId);
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			LOGGER.error(e.getMessage());
		}
		
		switch(maDVNB) {
			case "ACT_2070101": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("tenChuThe", String.class, "");
				container.addContainerProperty("loc", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("cardNo", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("tenChuThe").setHeaderCaption("TÊN CHỦ THẺ");
				grid.getColumn("loc").setHeaderCaption("LOC");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("cardNo").setHeaderCaption("SỐ THẺ");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2070102": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("tenChuThe", String.class, "");
				container.addContainerProperty("terminalId", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("cardNo", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("tenChuThe").setHeaderCaption("TÊN CHỦ THẺ");
				grid.getColumn("terminalId").setHeaderCaption("TERMINAL ID");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("cardNo").setHeaderCaption("SỐ THẺ");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2070103": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("maThietBi", String.class, "");
				container.addContainerProperty("pan", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("maThietBi").setHeaderCaption("MÃ THIẾT BỊ");
				grid.getColumn("pan").setHeaderCaption("PAN");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2070104": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("mid", String.class, "");
				container.addContainerProperty("tenMid", String.class, "");
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("mid").setHeaderCaption("MID");
				grid.getColumn("tenMid").setHeaderCaption("TÊN MID");
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2070105": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("khachHang", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("taiKhoanNguon", String.class, "");
				container.addContainerProperty("taiKhoanDich", String.class, "");
				container.addContainerProperty("soGiaoDichFcc", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("khachHang").setHeaderCaption("KHÁCH HÀNG");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("taiKhoanNguon").setHeaderCaption("TÀI KHOẢN NGUỒN");
				grid.getColumn("taiKhoanDich").setHeaderCaption("TÀI KHOẢN ĐÍCH");
				grid.getColumn("soGiaoDichFcc").setHeaderCaption("SỐ GIAO DỊCH FCC");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2070106": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("taiKhoanNguon", String.class, "");
				container.addContainerProperty("taiKhoanDich", String.class, "");
				container.addContainerProperty("dichVu", String.class, "");
				container.addContainerProperty("soTienGiaoDich", String.class, "");
				container.addContainerProperty("ngayGiaoDich", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("taiKhoanNguon").setHeaderCaption("TÀI KHOẢN NGUỒN");
				grid.getColumn("taiKhoanDich").setHeaderCaption("TÀI KHOẢN ĐÍCH");
				grid.getColumn("dichVu").setHeaderCaption("DỊCH VỤ");
				grid.getColumn("soTienGiaoDich").setHeaderCaption("SỐ TIỀN GD");
				grid.getColumn("ngayGiaoDich").setHeaderCaption("NGÀY GD");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080101": 
				container.addContainerProperty("case", String.class, "");
				container.addContainerProperty("thoiGian", String.class, "");
				container.addContainerProperty("soThe", String.class, "");
				container.addContainerProperty("loc", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("case").setHeaderCaption("CASE");
				grid.getColumn("thoiGian").setHeaderCaption("THỜI GIAN");
				grid.getColumn("soThe").setHeaderCaption("SỐ THẺ");
				grid.getColumn("loc").setHeaderCaption("LOC");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080102": 
				container.addContainerProperty("case", String.class, "");
				container.addContainerProperty("thoiGian", String.class, "");
				container.addContainerProperty("taiKhoanDangNhap", String.class, "");
				container.addContainerProperty("kenhGiaoDich", String.class, "");
				container.addContainerProperty("taiKhoanGiaoDich", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("case").setHeaderCaption("CASE");
				grid.getColumn("thoiGian").setHeaderCaption("THỜI GIAN");
				grid.getColumn("taiKhoanDangNhap").setHeaderCaption("TÀI KHOẢN ĐĂNG NHẬP");
				grid.getColumn("kenhGiaoDich").setHeaderCaption("KÊNH GD");
				grid.getColumn("taiKhoanGiaoDich").setHeaderCaption("TÀI KHOẢN GIAO DỊCH");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080103": 
				container.addContainerProperty("case", String.class, "");
				container.addContainerProperty("thoiGian", String.class, "");
				container.addContainerProperty("soThe", String.class, "");
				container.addContainerProperty("loc", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("case").setHeaderCaption("CASE");
				grid.getColumn("thoiGian").setHeaderCaption("THỜI GIAN");
				grid.getColumn("soThe").setHeaderCaption("SỐ THẺ");
				grid.getColumn("loc").setHeaderCaption("LOC");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080104": 
				container.addContainerProperty("case", String.class, "");
				container.addContainerProperty("thoiGian", String.class, "");
				container.addContainerProperty("taiKhoanDangNhap", String.class, "");
				container.addContainerProperty("kenhGiaoDich", String.class, "");
				container.addContainerProperty("taiKhoanGiaoDich", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("case").setHeaderCaption("CASE");
				grid.getColumn("thoiGian").setHeaderCaption("THỜI GIAN");
				grid.getColumn("taiKhoanDangNhap").setHeaderCaption("TÀI KHOẢN ĐĂNG NHẬP");
				grid.getColumn("kenhGiaoDich").setHeaderCaption("KÊNH GD");
				grid.getColumn("taiKhoanGiaoDich").setHeaderCaption("TÀI KHOẢN GIAO DỊCH");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080105": 
//				
				container.addContainerProperty("caseId", String.class, "");
				container.addContainerProperty("ngayGioGoi", String.class, "");
				container.addContainerProperty("maKhachHang", String.class, "");
				container.addContainerProperty("tenKhachHang", String.class, "");
				container.addContainerProperty("dienThoai", String.class, "");
				container.addContainerProperty("maNghiepVu", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("caseId").setHeaderCaption("CASE ID");
				grid.getColumn("ngayGioGoi").setHeaderCaption("NGÀY GIỜ GỌI");
				grid.getColumn("maKhachHang").setHeaderCaption("MÃ LKHÁCH HÀNG");
				grid.getColumn("tenKhachHang").setHeaderCaption("TÊN KHÁCH HÀNG");
				grid.getColumn("dienThoai").setHeaderCaption("ĐIỆN THOẠI");
				grid.getColumn("maNghiepVu").setHeaderCaption("MÃ NGHIỆP VỤ");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080106": 
				
				container.addContainerProperty("caseId", String.class, "");
				container.addContainerProperty("ngayGioGoi", String.class, "");
				container.addContainerProperty("maKhachHang", String.class, "");
				container.addContainerProperty("tenKhachHang", String.class, "");
				container.addContainerProperty("dienThoai", String.class, "");
				container.addContainerProperty("maNghiepVu", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("caseId").setHeaderCaption("CASE ID");
				grid.getColumn("ngayGioGoi").setHeaderCaption("NGÀY GIỜ GỌI");
				grid.getColumn("maKhachHang").setHeaderCaption("MÃ LKHÁCH HÀNG");
				grid.getColumn("tenKhachHang").setHeaderCaption("TÊN KHÁCH HÀNG");
				grid.getColumn("dienThoai").setHeaderCaption("ĐIỆN THOẠI");
				grid.getColumn("maNghiepVu").setHeaderCaption("MÃ NGHIỆP VỤ");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080201": 
				
				container.addContainerProperty("caseNo", String.class, "");
				container.addContainerProperty("thoiGianGd", String.class, "");
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("mid", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("caseNo").setHeaderCaption("CASE");
				grid.getColumn("thoiGianGd").setHeaderCaption("THỜI GIAN GIAO DỊCH");
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("mid").setHeaderCaption("MID");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2080301": 
				
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("mid", String.class, "");
				container.addContainerProperty("tenMid", String.class, "");
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				

				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("mid").setHeaderCaption("MID");
				grid.getColumn("tenMid").setHeaderCaption("TÊN MID");
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2100201": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("brchCode", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("customerName", String.class, "");
				container.addContainerProperty("pan", String.class, "");
				container.addContainerProperty("loc", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("brchCode").setHeaderCaption("BRCH CODE");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("customerName").setHeaderCaption("CUSTOMER NAME");
				grid.getColumn("pan").setHeaderCaption("PAN");
				grid.getColumn("loc").setHeaderCaption("LOC");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2100301": 
				container.addContainerProperty("ngayTiepNhan", String.class, "");
				container.addContainerProperty("brchCode", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("customerName", String.class, "");
				container.addContainerProperty("pan", String.class, "");
				container.addContainerProperty("loc", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayTiepNhan").setHeaderCaption("NGÀY TIẾP NHẬN");
				grid.getColumn("brchCode").setHeaderCaption("BRCH CODE");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("customerName").setHeaderCaption("CUSTOMER NAME");
				grid.getColumn("pan").setHeaderCaption("PAN");
				grid.getColumn("loc").setHeaderCaption("LOC");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
			case "ACT_2110103": 
				container.addContainerProperty("soHoSo", String.class, "");
				container.addContainerProperty("donViTrinhGoc", String.class, "");
				container.addContainerProperty("ngayDeNghiPhanHoi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("soHoSo").setHeaderCaption("SỐ HỒ SƠ");
				grid.getColumn("donViTrinhGoc").setHeaderCaption("ĐƠN VỊ TRÌNH GỐC");
				grid.getColumn("ngayDeNghiPhanHoi").setHeaderCaption("NGÀY ĐỀ NGHỊ PHẢN HỒI");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
			case "ACT_2110104": 
				container.addContainerProperty("soHoSo", String.class, "");
				container.addContainerProperty("donViTrinhGoc", String.class, "");
				container.addContainerProperty("ngayDeNghiPhanHoi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("soHoSo").setHeaderCaption("SỐ HỒ SƠ");
				grid.getColumn("donViTrinhGoc").setHeaderCaption("ĐƠN VỊ TRÌNH GỐC");
				grid.getColumn("ngayDeNghiPhanHoi").setHeaderCaption("NGÀY ĐỀ NGHỊ PHẢN HỒI");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3380201": 
				container.addContainerProperty("soHoSo", String.class, "");
				container.addContainerProperty("donViTrinhGoc", String.class, "");
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("soHoSo").setHeaderCaption("SỐ HỒ SƠ");
				grid.getColumn("donViTrinhGoc").setHeaderCaption("ĐƠN VỊ TRÌNH GỐC");
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3380301": 
				container.addContainerProperty("soHoSo", String.class, "");
				container.addContainerProperty("donViTrinhGoc", String.class, "");
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("soHoSo").setHeaderCaption("SỐ HỒ SƠ");
				grid.getColumn("donViTrinhGoc").setHeaderCaption("ĐƠN VỊ TRÌNH GỐC");
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3380401": 
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("donViYeuCau", String.class, "");
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("donViYeuCau").setHeaderCaption("ĐƠN VỊ YÊU CẦU");
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				
				break;
				
			case "ACT_3390101b": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("phongYeuCau", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("phongYeuCau").setHeaderCaption("PHÒNG YÊU CẦU");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3390301": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("donViYeuCauTrinh", String.class, "");
//				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("donViYeuCauTrinh").setHeaderCaption("ĐƠN VỊ YÊU CẦU/ ĐƠN VỊ TRÌNH");
//				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3390302": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("donViYeuCau", String.class, "");
				container.addContainerProperty("mid", String.class, "");
				container.addContainerProperty("tenMid", String.class, "");
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("donViYeuCau").setHeaderCaption("ĐƠN VỊ YÊU CẦU");
				grid.getColumn("mid").setHeaderCaption("MID");
				grid.getColumn("tenMid").setHeaderCaption("TÊN MID");
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				break;
				
			case "ACT_3390901": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("donViYeuCau", String.class, "");
				container.addContainerProperty("mid", String.class, "");
				container.addContainerProperty("tenMid", String.class, "");
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("donViYeuCau").setHeaderCaption("ĐƠN VỊ YÊU CẦU");
				grid.getColumn("mid").setHeaderCaption("MID");
				grid.getColumn("tenMid").setHeaderCaption("TÊN MID");
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
			case "ACT_3391001": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("donViYeuCau", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("mid", String.class, "");
				container.addContainerProperty("tenMid", String.class, "");
				container.addContainerProperty("tid", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("donViYeuCau").setHeaderCaption("ĐƠN VỊ YÊU CẦU");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("mid").setHeaderCaption("MID");
				grid.getColumn("tenMid").setHeaderCaption("TÊN MID");
				grid.getColumn("tid").setHeaderCaption("TID");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
				
			case "ACT_3400202": 
				container.addContainerProperty("soHoSo", String.class, "");
				container.addContainerProperty("donViTrinhGoc", String.class, "");
				container.addContainerProperty("ngayDeNghiPhanHoi", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("soHoSo").setHeaderCaption("SỐ HỒ SƠ");
				grid.getColumn("donViTrinhGoc").setHeaderCaption("ĐƠN VỊ TRÌNH GỐC");
				grid.getColumn("ngayDeNghiPhanHoi").setHeaderCaption("NGÀY ĐỀ NGHỊ PHẢN HỒI");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3400203": 
//				
				container.addContainerProperty("soHoSo", String.class, "");
				container.addContainerProperty("donViTrinhGoc", String.class, "");
				container.addContainerProperty("ngayDeNghiPhanHoi", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("soHoSo").setHeaderCaption("SỐ HỒ SƠ");
				grid.getColumn("donViTrinhGoc").setHeaderCaption("ĐƠN VỊ TRÌNH GỐC");
				grid.getColumn("ngayDeNghiPhanHoi").setHeaderCaption("NGÀY ĐỀ NGHỊ PHẢN HỒI");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				break;
				
			case "ACT_3390201": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("hanMuc", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("hanMuc").setHeaderCaption("HẠN MỨC");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
				
			case "ACT_3390401": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
				
			case "ACT_3390402": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
			case "ACT_3390501": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("thuongHieuThe", String.class, "");
				container.addContainerProperty("noiDungYeuCau", String.class, "");
				container.addContainerProperty("tongSoLuong", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("thuongHieuThe").setHeaderCaption("THƯƠNG HIỆU THẺ");
				grid.getColumn("noiDungYeuCau").setHeaderCaption("NỘI DUNG YÊU CẦU");
				grid.getColumn("tongSoLuong").setHeaderCaption("TỔNG SỐ LƯỢNG");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
				
			case "ACT_3390502": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("loaiNghiepVu", String.class, "");
				container.addContainerProperty("noiDungYeuCau", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("loaiNghiepVu").setHeaderCaption("LOẠI NGHIỆP VỤ");
				grid.getColumn("noiDungYeuCau").setHeaderCaption("NỘI DUNG YÊU CẦU");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
				
			case "ACT_3390701": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
				
			case "ACT_3390801": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("cif", String.class, "");
				container.addContainerProperty("loaiThe", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maSanPham", String.class, "");
				container.addContainerProperty("maLoaiKhachHang", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("cif").setHeaderCaption("CIF");
				grid.getColumn("loaiThe").setHeaderCaption("LOẠI THẺ");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maSanPham").setHeaderCaption("MÃ SP");
				grid.getColumn("maLoaiKhachHang").setHeaderCaption("MÃ LKH");
				break;
			
			case "ACT_3390403": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("donViYeuCau", String.class, "");
				container.addContainerProperty("noiDungYeuCau", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("donViYeuCau").setHeaderCaption("ĐƠN VỊ YÊU CẦU");
				grid.getColumn("noiDungYeuCau").setHeaderCaption("NỘI DUNG YÊU CẦU");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				
			case "ACT_3390601": 
				container.addContainerProperty("ngayYeuCau", String.class, "");
				container.addContainerProperty("donViYeuCau", String.class, "");
				container.addContainerProperty("noiDungYeuCau", String.class, "");
				container.addContainerProperty("maDonVi", String.class, "");
				container.addContainerProperty("maPhongBan", String.class, "");
				
				grid.setContainerDataSource(container);
				
				grid.getColumn("ngayYeuCau").setHeaderCaption("NGÀY YÊU CẦU");
				grid.getColumn("donViYeuCau").setHeaderCaption("ĐƠN VỊ YÊU CẦU");
				grid.getColumn("noiDungYeuCau").setHeaderCaption("NỘI DUNG YÊU CẦU");
				grid.getColumn("maDonVi").setHeaderCaption("MÃ ĐV");
				grid.getColumn("maPhongBan").setHeaderCaption("MÃ PB");
				
			default:
				break;
		}
		
		if (createDataForContainer() == false) {
			if (!lbNoDataFound.isVisible()) {
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
		
		if (dataSourceAct2070101 != null && dataSourceAct2070101.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2070101.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("loc").setValue(String.valueOf(s.getLoc()));
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("cardNo").setValue(s.getCardNo());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		
		if (dataSourceAct2070102 != null && dataSourceAct2070102.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2070102.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("terminalId").setValue(s.getTerminalID());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("cardNo").setValue(s.getCardNo());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		
		if (dataSourceAct2070103 != null && dataSourceAct2070103.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2070103.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("maThietBi").setValue(s.getMaThietBi());
				item.getItemProperty("pan").setValue(s.getPan());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		
		if (dataSourceAct2070104 != null && dataSourceAct2070104.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2070104.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("tenMid").setValue(s.getTenMid());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		
		if (dataSourceAct2070105 != null && dataSourceAct2070105.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2070105.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("khachHang").setValue(s.getKhachHang());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("taiKhoanNguon").setValue(s.getTaiKhoanNguon());
				item.getItemProperty("taiKhoanDich").setValue(s.getTaiKhoanDich());
				item.getItemProperty("soGiaoDichFcc").setValue(s.getSoGiaoDichFcc());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		
		if (dataSourceAct2070106 != null && dataSourceAct2070106.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2070106.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("taiKhoanNguon").setValue(s.getTaiKhoanNguon());
				item.getItemProperty("taiKhoanDich").setValue(s.getTaiKhoanDich());
				item.getItemProperty("dichVu").setValue(s.getDichVu());
				item.getItemProperty("soTienGiaoDich").setValue(s.getSoTienGiaoDich().toString());
				item.getItemProperty("ngayGiaoDich").setValue(s.getNgayGiaoDich());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		
		if (dataSourceAct2080101 != null && dataSourceAct2080101.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080101.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("case").setValue(s.getCaseNo());
				item.getItemProperty("thoiGian").setValue(s.getThoiGian());
				item.getItemProperty("soThe").setValue(s.getCardNo());
				item.getItemProperty("loc").setValue(String.valueOf(s.getLoc()));
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		if (dataSourceAct2080102 != null && dataSourceAct2080102.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080102.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("case").setValue(s.getCaseNo());
				item.getItemProperty("thoiGian").setValue(s.getThoiGian().toString());
				item.getItemProperty("taiKhoanDangNhap").setValue(s.getTaiKhoanDangNhap());
				item.getItemProperty("kenhGiaoDich").setValue(s.getKenhGd());
				item.getItemProperty("taiKhoanGiaoDich").setValue(s.getTaiKhoanGiaoDich());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		if (dataSourceAct2080103 != null && dataSourceAct2080103.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080103.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("case").setValue(s.getCaseNo());
				item.getItemProperty("thoiGian").setValue(s.getThoiGian());
				item.getItemProperty("soThe").setValue(s.getCardNo());
				item.getItemProperty("loc").setValue(String.valueOf(s.getLoc()));
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		if (dataSourceAct2080104 != null && dataSourceAct2080104.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080104.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("case").setValue(s.getCaseNo());
				item.getItemProperty("thoiGian").setValue(s.getThoiGian().toString());
				item.getItemProperty("taiKhoanDangNhap").setValue(s.getTaiKhoanDangNhap());
				item.getItemProperty("kenhGiaoDich").setValue(s.getKenhGd());
				item.getItemProperty("taiKhoanGiaoDich").setValue(s.getTaiKhoanGiaoDich());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		if (dataSourceAct2080105 != null && dataSourceAct2080105.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080105.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("caseId").setValue(s.getCaseId());
				item.getItemProperty("ngayGioGoi").setValue(s.getNgayGioGoi());
				item.getItemProperty("maKhachHang").setValue(s.getMaKhachHang());
				item.getItemProperty("tenKhachHang").setValue(s.getTenKhachHang());
				item.getItemProperty("dienThoai").setValue(s.getDienThoai());
				item.getItemProperty("maNghiepVu").setValue(s.getMaNghiepVu());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		if (dataSourceAct2080106 != null && dataSourceAct2080106.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080106.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("caseId").setValue(s.getCaseId());
				item.getItemProperty("ngayGioGoi").setValue(s.getNgayGioGoi());
				item.getItemProperty("maKhachHang").setValue(s.getMaKhachHang());
				item.getItemProperty("tenKhachHang").setValue(s.getTenKhachHang());
				item.getItemProperty("dienThoai").setValue(s.getDienThoai());
				item.getItemProperty("maNghiepVu").setValue(s.getMaNghiepVu());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		} 
		if (dataSourceAct2080201 != null && dataSourceAct2080201.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080201.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				
				item.getItemProperty("caseNo").setValue(s.getCaseNo());
				item.getItemProperty("thoiGianGd").setValue(s.getThoiGianGd());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		if (dataSourceAct2080301 != null && dataSourceAct2080301.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2080301.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				
				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("tenMid").setValue(s.getTenMid());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct2100201 != null && dataSourceAct2100201.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2100201.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("brchCode").setValue(s.getBrchCode());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("customerName").setValue(s.getCustomerName());
				item.getItemProperty("pan").setValue(s.getPan());
				item.getItemProperty("loc").setValue(s.getLoc().toString());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct2100301 != null && dataSourceAct2100301.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2100301.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				item.getItemProperty("ngayTiepNhan").setValue(s.getNgayTiepNhan());
				item.getItemProperty("brchCode").setValue(s.getBrchCode());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("customerName").setValue(s.getCustomerName());
				item.getItemProperty("pan").setValue(s.getPan());
				item.getItemProperty("loc").setValue(s.getLoc().toString());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct2110103 != null && dataSourceAct2110103.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2110103.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				item.getItemProperty("soHoSo").setValue(s.getSoHoSo());
				item.getItemProperty("donViTrinhGoc").setValue(s.getDonViTrinhGoc());
				item.getItemProperty("ngayDeNghiPhanHoi").setValue(s.getNgayDeNghiPhanHoi());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct2110104 != null && dataSourceAct2110104.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct2110104.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());
				item.getItemProperty("soHoSo").setValue(s.getSoHoSo());
				item.getItemProperty("donViTrinhGoc").setValue(s.getDonViTrinhGoc());
				item.getItemProperty("ngayDeNghiPhanHoi").setValue(s.getNgayDeNghiPhanHoi());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3380201 != null && dataSourceAct3380201.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3380201.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("soHoSo").setValue(s.getSoHoSo());
				item.getItemProperty("donViTrinhGoc").setValue(s.getDonViTrinhGoc());
				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3380301 != null && dataSourceAct3380301.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3380301.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("soHoSo").setValue(s.getSoHoSo());
				item.getItemProperty("donViTrinhGoc").setValue(s.getDonViTrinhGoc());
				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3380401 != null && dataSourceAct3380401.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3380401.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("donViYeuCau").setValue(s.getDonViYeuCau());
				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390101b != null && dataSourceAct3390101b.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390101b.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("phongYeuCau").setValue(s.getPhongYeuCau());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3390301 != null && dataSourceAct3390301.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390301.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("donViYeuCauTrinh").setValue(s.getDonViYeuCauTrinh());
//				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3390302 != null && dataSourceAct3390302.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390302.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("donViYeuCau").setValue(s.getDonViYeuCau());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("tenMid").setValue(s.getTenMid());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390901 != null && dataSourceAct3390901.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390901.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("donViYeuCau").setValue(s.getDonViYeuCau());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("tenMid").setValue(s.getTenMid());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3391001 != null && dataSourceAct3391001.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3391001.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("donViYeuCau").setValue(s.getDonViYeuCau());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("mid").setValue(s.getMid());
				item.getItemProperty("tenMid").setValue(s.getTenMid());
				item.getItemProperty("tid").setValue(s.getTid());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3400202 != null && dataSourceAct3400202.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3400202.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("soHoSo").setValue(s.getSoHoSo());
				item.getItemProperty("donViTrinhGoc").setValue(s.getDonViTrinhGoc());
				item.getItemProperty("ngayDeNghiPhanHoi").setValue(s.getNgayDeNghiPhanHoi());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3400203 != null && dataSourceAct3400203.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3400203.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("soHoSo").setValue(s.getSoHoSo());
				item.getItemProperty("donViTrinhGoc").setValue(s.getDonViTrinhGoc());
				item.getItemProperty("ngayDeNghiPhanHoi").setValue(s.getNgayDeNghiPhanHoi());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3390201 != null && dataSourceAct3390201.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390201.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("hanMuc").setValue(s.getHanMuc());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390401 != null && dataSourceAct3390401.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390401.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390402 != null && dataSourceAct3390402.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390402.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390501 != null && dataSourceAct3390501.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390501.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("thuongHieuThe").setValue(s.getThuongHieuThe());
				item.getItemProperty("noiDungYeuCau").setValue(s.getNoiDungYeuCau());
				item.getItemProperty("tongSoLuong").setValue(String.valueOf(s.getTongSoLuong()));
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
			
		}
		
		if (dataSourceAct3390502 != null && dataSourceAct3390502.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390502.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("loaiNghiepVu").setValue(s.getLoaiNghiepVu());
				item.getItemProperty("noiDungYeuCau").setValue(s.getNoiDungYeuCau());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390701 != null && dataSourceAct3390701.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390701.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390801 != null && dataSourceAct3390801.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390801.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("cif").setValue(s.getCif());
				item.getItemProperty("loaiThe").setValue(s.getLoaiThe());
				item.getItemProperty("maSanPham").setValue(s.getMaSanPham());
				item.getItemProperty("maLoaiKhachHang").setValue(s.getMaLoaiKhachHang());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
			});
			flag = true;
		}
		
		if (dataSourceAct3390403 != null && dataSourceAct3390403.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390403.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("donViYeuCau").setValue(s.getDonViYeuCau());
				item.getItemProperty("noiDungYeuCau").setValue(s.getNoiDungYeuCau());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		if (dataSourceAct3390601 != null && dataSourceAct3390601.getTotalElements()>0) {
			container.removeAllItems();
			dataSourceAct3390601.forEach(s -> {
				i++;
				Item item = container.getItem(container.addItem());

				item.getItemProperty("ngayYeuCau").setValue(s.getNgayYeuCau());
				item.getItemProperty("donViYeuCau").setValue(s.getDonViYeuCau());
				item.getItemProperty("noiDungYeuCau").setValue(s.getNoiDungYeuCau());
				item.getItemProperty("maDonVi").setValue(s.getMaDonVi());
				item.getItemProperty("maPhongBan").setValue(s.getMaPhongBan());
			});
			flag = true;
		}
		
		
		return flag;
		
	}
}
