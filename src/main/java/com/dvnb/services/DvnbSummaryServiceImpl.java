package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.dvnb.SecurityDataSourceConfig;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.Act2080102;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.DvnbSummary;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.repositories.DvnbInvoiceMcRepo;
import com.dvnb.repositories.DvnbSummaryRepo;
import com.vaadin.server.VaadinServlet;

import oracle.jdbc.OracleTypes;

@Service("dvnbSummaryService")
@Transactional
public class DvnbSummaryServiceImpl implements DvnbSummaryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DvnbSummaryServiceImpl.class);
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DvnbSummaryRepo dvnbSummaryRepo;
	
	protected DataSource localDataSource;
	protected SecurityDataSourceConfig securityDataSourceConfig = new SecurityDataSourceConfig();
	private final transient TimeConverter timeConverter = new TimeConverter();

	@Override
	public void save(DvnbSummary dvnbSummary) {
		// TODO Auto-generated method stub
		dvnbSummaryRepo.save(dvnbSummary);
		
	}
	
	@Override
	public void deleteDvnbSummaryByMaHoatDong(String ky,String maHoatDong,String role) {
		// TODO Auto-generated method stub
		dvnbSummaryRepo.deleteDvnbSummaryByMaHoatDong(ky,maHoatDong,role);
	}
	
	@Override
	public List<DvnbSummary> searchAllDvnbSummary(String ky, String maDvnb, String maDonVi) throws SQLException {
		
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("SELECT * FROM ( " + 
				"    WITH t (KY,MA_DON_VI, MA_DRIVER, MA_SAN_PHAM, MA_PHONG_BAN, MA_LOAI_KHACH_HANG, MA_HOAT_DONG, " + 
				"    PCMCS_RESERVE2_ID, SO_TIEN_SO_LUONG, TYPE) AS " + 
				"    ( " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CIF) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2070101 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CIF) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2070102 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(MA_THIET_BI) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2070103 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(TID) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2070104 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(*) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2070105 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080101 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080102 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080103 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080104 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_ID) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080105 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_ID) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080106 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080107 " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080201 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY, MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(MID) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2080301 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2100101 " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(CIF) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2100201 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2100401 " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2100501 " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2100502 " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2100503 " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2110102 " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(SO_HO_SO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2110103 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB,MA_PHONG_BAN " + 
				"        UNION ALL " + 
				"        SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, COUNT(SO_HO_SO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2110104 " + 
				"        GROUP BY KY,MA_DON_VI,MA_SAN_PHAM,MA_LOAI_KHACH_HANG,MA_DVNB " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,'' MA_SAN_PHAM,'DEP_27' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2110202 " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2110203 " + 
				"        UNION ALL " + 
				"        SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER070' MA_DRIVER,'' MA_SAN_PHAM,'DEP_62' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, " + 
				"        '' PCMCS_RESERVE2_ID, SO_LUONG_PHAT_SINH SO_TIEN_SO_LUONG,'DESTINATION' TYPE " + 
				"        FROM DVNB_ACT_2110204 " + 
				"    ) " + 
				"    SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER067' MA_DRIVER,'' MA_SAN_PHAM,'DEP_55' MA_PHONG_BAN,'' MA_LOAI_KHACH_HANG,MA_HOAT_DONG, " + 
				"    '' PCMCS_RESERVE2_ID, SUM(SO_TIEN_SO_LUONG) SO_TIEN_SO_LUONG,'SOURCE' TYPE " + 
				"    FROM t " + 
				"    GROUP BY MA_HOAT_DONG,KY " + 
				"    UNION ALL " + 
				"    SELECT KY,MA_DON_VI, MA_DRIVER, MA_SAN_PHAM, MA_PHONG_BAN, MA_LOAI_KHACH_HANG, MA_HOAT_DONG, " + 
				"    PCMCS_RESERVE2_ID, SO_TIEN_SO_LUONG, TYPE " + 
				"    FROM t " + 
				"    WHERE MA_HOAT_DONG NOT IN ('ACT_2110204','ACT_2110203','ACT_2110102','ACT_2100503','ACT_2100502', " + 
				"    'ACT_2100501','ACT_2100401','ACT_2100101') " + 
				") " + 
				"WHERE KY=? ");
		
		if(!maDvnb.isEmpty()) {
			sqlString.append("AND MA_HOAT_DONG=? ");
		}
		
		if(!maDonVi.isEmpty()) {
			sqlString.append("AND MA_DON_VI=? ");
		}
		
		sqlString.append("ORDER BY MA_DRIVER,MA_HOAT_DONG,MA_DON_VI ASC");
				
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		preStmt.setString(1, ky);
		if(!maDvnb.isEmpty()) {
			preStmt.setString(2, maDvnb);
		}
		if(!maDonVi.isEmpty()) {
			preStmt.setString(3, maDonVi);
		}
		
		ResultSet rs = preStmt.executeQuery();
		List<DvnbSummary> dvnbSummaryList = new ArrayList<DvnbSummary>();
		while(rs.next()) {
			
			DvnbSummary dvnbSummary = new DvnbSummary();
			dvnbSummary.setKy(rs.getString(1));
			dvnbSummary.setMaDonVi(rs.getString(2));
			dvnbSummary.setMaDriver(rs.getString(3));
			dvnbSummary.setMaSanPham(rs.getString(4));
			dvnbSummary.setMaPhongBan(rs.getString(5));
			dvnbSummary.setMaLoaiKhachHang(rs.getString(6));
			dvnbSummary.setMaHoatDong(rs.getString(7));
			dvnbSummary.setPcmcsReserve2Id(rs.getString(8));
			dvnbSummary.setSoTienSoLuong(rs.getString(9));
			dvnbSummary.setType(rs.getString(10));

			dvnbSummaryList.add(dvnbSummary);
		}
		
		preStmt.close();
		return dvnbSummaryList;
	}
	
	@Override
	public List<DvnbSummary> searchAllDvnbSummaryProc(String ky, String maDvnb, String maDonVi, String role, String userid,String fromdate,String todate) throws SQLException {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		Connection connect = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		
		String sp_DVNB_TongHop = "{call PROC_DVNB_TONGHOP(?,?,?,?,?,?,?,?)} ";
			
		List<DvnbSummary> dvnbSummaryList = new ArrayList<DvnbSummary>();
			
		try {
			
			connect = localDataSource.getConnection();
			
			callableStatement = connect.prepareCall(sp_DVNB_TongHop);
			callableStatement.setString(1, ky);
			callableStatement.setString(2, maDvnb);
			callableStatement.setString(3, maDonVi);
			callableStatement.setString(4, role);
			callableStatement.setString(5, userid);
			callableStatement.setString(6, fromdate);
			callableStatement.setString(7, todate);
			
			callableStatement.registerOutParameter(8, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			rs = (ResultSet) callableStatement.getObject(8);
			
			while(rs.next()) {
				
				DvnbSummary dvnbSummary = new DvnbSummary();
				dvnbSummary.setKy(rs.getString(1));
				dvnbSummary.setMaDonVi(rs.getString(2));
				dvnbSummary.setMaDriver(rs.getString(3));
				dvnbSummary.setMaSanPham(rs.getString(4));
				dvnbSummary.setMaPhongBan(rs.getString(5));
				dvnbSummary.setMaLoaiKhachHang(rs.getString(6));
				dvnbSummary.setMaHoatDong(rs.getString(7));
				dvnbSummary.setPcmcsReserve2Id(rs.getString(8));
				dvnbSummary.setSoTienSoLuong(rs.getString(9));
				dvnbSummary.setType(rs.getString(10));

				dvnbSummaryList.add(dvnbSummary);
			}
		}
		catch (Exception ex){
			LOGGER.error(ex.getMessage());
		}
		finally{
			connect.close();
			callableStatement.close();
			rs.close();
		}
		
		return dvnbSummaryList;
	}

	@Override
	public List<DvnbSummary> searchAct2080101SummaryProc(String ky, String maDvnb, String maDonVi, String role, String userid, String fromdate,
			String todate) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		Connection connect = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		
		String sp_DVNB_TongHop = "{call PROC_DVNB_TONGHOP_ACT_2080101(?,?,?,?,?,?,?,?)} ";
			
		List<DvnbSummary> dvnbSummaryList = new ArrayList<DvnbSummary>();
			
		try {
			
			connect = localDataSource.getConnection();
			
			callableStatement = connect.prepareCall(sp_DVNB_TongHop);
			callableStatement.setString(1, ky);
			callableStatement.setString(2, maDvnb);
			callableStatement.setString(3, maDonVi);
			callableStatement.setString(4, role);
			callableStatement.setString(5, userid);
			callableStatement.setString(6, fromdate);
			callableStatement.setString(7, todate);
			
			callableStatement.registerOutParameter(8, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			rs = (ResultSet) callableStatement.getObject(8);
			
			while(rs.next()) {
				
				DvnbSummary dvnbSummary = new DvnbSummary();
				dvnbSummary.setKy(rs.getString(1));
				dvnbSummary.setMaDonVi(rs.getString(2));
				dvnbSummary.setMaDriver(rs.getString(3));
				dvnbSummary.setMaSanPham(rs.getString(4));
				dvnbSummary.setMaPhongBan(rs.getString(5));
				dvnbSummary.setMaLoaiKhachHang(rs.getString(6));
				dvnbSummary.setMaHoatDong(rs.getString(7));
				dvnbSummary.setPcmcsReserve2Id(rs.getString(8));
				dvnbSummary.setSoTienSoLuong(rs.getString(9));
				dvnbSummary.setType(rs.getString(10));

				dvnbSummaryList.add(dvnbSummary);
			}
		}
		catch (Exception ex){
			LOGGER.error(ex.getMessage());
		}
		finally{
			connect.close();
			callableStatement.close();
			rs.close();
		}
		
		return dvnbSummaryList;
	}

	@Override
	public List<DvnbSummary> searchAct2080103SummaryProc(String ky, String maDvnb, String maDonVi, String role, String userid, String fromdate,
			String todate) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		Connection connect = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		
		String sp_DVNB_TongHop = "{call PROC_DVNB_TONGHOP_ACT_2080103(?,?,?,?,?,?,?,?)} ";
			
		List<DvnbSummary> dvnbSummaryList = new ArrayList<DvnbSummary>();
			
		try {
			
			connect = localDataSource.getConnection();
			
			callableStatement = connect.prepareCall(sp_DVNB_TongHop);
			callableStatement.setString(1, ky);
			callableStatement.setString(2, maDvnb);
			callableStatement.setString(3, maDonVi);
			callableStatement.setString(4, role);
			callableStatement.setString(5, userid);
			callableStatement.setString(6, fromdate);
			callableStatement.setString(7, todate);
			
			callableStatement.registerOutParameter(8, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			rs = (ResultSet) callableStatement.getObject(8);
			
			while(rs.next()) {
				
				DvnbSummary dvnbSummary = new DvnbSummary();
				dvnbSummary.setKy(rs.getString(1));
				dvnbSummary.setMaDonVi(rs.getString(2));
				dvnbSummary.setMaDriver(rs.getString(3));
				dvnbSummary.setMaSanPham(rs.getString(4));
				dvnbSummary.setMaPhongBan(rs.getString(5));
				dvnbSummary.setMaLoaiKhachHang(rs.getString(6));
				dvnbSummary.setMaHoatDong(rs.getString(7));
				dvnbSummary.setPcmcsReserve2Id(rs.getString(8));
				dvnbSummary.setSoTienSoLuong(rs.getString(9));
				dvnbSummary.setType(rs.getString(10));

				dvnbSummaryList.add(dvnbSummary);
			}
		}
		catch (Exception ex){
			LOGGER.error(ex.getMessage());
		}
		finally{
			connect.close();
			callableStatement.close();
			rs.close();
		}
		
		return dvnbSummaryList;
	}
	
	//IB,MB: if MA_DON_VI FDS is null -> get by user, MB: if MA_DON_VI FDS is null -> get by CIF
	@Override
	public List<DvnbSummary> searchAct2080102FromEBHitRuleNhom1(String ky, String userid) throws SQLException {
		
		securityDataSourceConfig = new SecurityDataSourceConfig();
		String sFromDate = ky.substring(2) + ky.substring(0, 2) + "01" + "000000000";
		String sToDate = ky.substring(2) + ky.substring(0,2) + LastDayOfMonth(Integer.valueOf(ky.substring(2)),Integer.valueOf(ky.substring(0,2))) + "999999999";
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("WITH t (KY,MA_DON_VI, MA_DRIVER, MA_SAN_PHAM, MA_PHONG_BAN, MA_LOAI_KHACH_HANG, MA_HOAT_DONG, \r\n" + 
				"PCMCS_RESERVE2_ID, SO_TIEN_SO_LUONG, TYPE_DESC) AS \r\n" + 
				"( \r\n" + 
				"SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, \r\n" + 
				"'' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE_DESC \r\n" + 
				"FROM \r\n" + 
				"( \r\n" + 
				"    SELECT (select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) cre_tms, '" + userid + "' usr_id, null upd_tms, null upd_uid, \r\n" + 
				"    (select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) || REPLACE(TO_CHAR(ROWNUM,'9999999'),' ',0) id_no, \r\n" + 
				"    'ACT_2080102' ma_dvnb, '" + ky + "' ky, \r\n" + 
				"    CASE_NO, THOI_GIAN, TAI_KHOAN_DANG_NHAP, KENH_GD, TAI_KHOAN_GIAO_DICH, CIF, \r\n" + 
				"    CASE WHEN KENH_GD='MB' THEN 'PRO_251' WHEN KENH_GD='IB' THEN 'PRO_247' ELSE 'PRO_255' END MA_SAN_PHAM, \r\n" + 
				"    CASE WHEN F.CUSTOMER_TYPE='I' THEN 'CUS_01' WHEN F.CUSTOMER_TYPE='C' THEN 'CUS_03' END MA_LOAI_KHACH_HANG, \r\n" + 
				"	 'ENT_' || (CASE WHEN KENH_GD='IB' AND CODSRCBRANCH IS NULL AND IB.BRANCH IS NOT NULL THEN (CASE WHEN NVL(CODSRCBRANCH,IB.BRANCH)='000' THEN '137' ELSE NVL(CODSRCBRANCH,IB.BRANCH) END) \r\n" + 
			    "	 WHEN KENH_GD='IB' AND CODSRCBRANCH IS NULL AND IB.BRANCH IS NULL THEN (CASE WHEN NVL(CODSRCBRANCH,F.LOCAL_BRANCH)='000' THEN '137' ELSE NVL(CODSRCBRANCH,F.LOCAL_BRANCH) END) \r\n" + 
			    "	 ELSE (CASE WHEN NVL(CODSRCBRANCH,BRANCH_CODE)='000' THEN '137' ELSE NVL(CODSRCBRANCH,BRANCH_CODE) END) END) MA_DON_VI \r\n" + 
				"    FROM ( \r\n" + 
				"        SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,B.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,B.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_CASE_STATUS A INNER JOIN SMS.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO \r\n" + 
				"        WHERE A.CLOSED_REASON IN ('CCE','CCR','CRC') AND A.CRE_TMS between " + sFromDate + " and " + sToDate + " \r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT C.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,C.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,C.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_EBANK_DESCRIPTION A \r\n" + 
				"        LEFT JOIN SMS.FDS_CASE_STATUS B ON B.CLOSED_REASON = A.TYPE AND B.OTHER LIKE '%'|| A.ID ||'%' \r\n" + 
				"        INNER JOIN SMS.FDS_EBANK_CASE_DETAIL C ON B.CASE_NO = C.CASE_NO \r\n" + 
				"        WHERE TYPE='CCN' AND B.CRE_TMS between " + sFromDate + " and " + sToDate + " \r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,B.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,B.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_CASE_STATUS A INNER JOIN SMS.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO \r\n" + 
				"        WHERE A.CLOSED_REASON = 'CCT' AND A.CRE_TMS between " + sFromDate + " and " + sToDate + " \r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,B.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,B.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_CASE_STATUS A INNER JOIN SMS.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO \r\n" + 
				"        WHERE A.CLOSED_REASON = 'CNC' AND A.CRE_TMS between " + sFromDate + " and " + sToDate + " \r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,B.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,B.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_CASE_STATUS A INNER JOIN SMS.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO \r\n" + 
				"        WHERE A.CLOSED_REASON = 'BCC' AND A.CRE_TMS between " + sFromDate + " and " + sToDate + " \r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,B.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,B.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_CASE_STATUS A INNER JOIN SMS.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO \r\n" + 
				"        WHERE A.CLOSED_REASON = 'NCR' AND A.CRE_TMS between " + sFromDate + " and " + sToDate + " \r\n" + 
				"    ) FDS \r\n" + 
				"    LEFT JOIN vninfoscb.mb_customer@mbanking CUST ON CUST.cif_core=FDS.CIF and CUST.cus_status<>'9'\r\n" + 
				"    LEFT JOIN (SELECT DISTINCT U_NAME,BRANCH FROM obdx_admin_prod.scb_users_ext@fcatsmslink \r\n" + 
				"            WHERE (U_NAME||CREATION_DATE||CASE WHEN STATUS='A' THEN 9 ELSE 1 END) IN (SELECT U_NAME||MAX(CREATION_DATE||CASE WHEN STATUS='A' THEN 9 ELSE 1 END) FROM obdx_admin_prod.scb_users_ext@fcatsmslink GROUP BY U_NAME)) IB ON IB.u_name=FDS.TAI_KHOAN_DANG_NHAP \r\n" + 
				"    LEFT JOIN (SELECT DISTINCT IDREF,CODSRCBRANCH FROM sms.FDS_TRANS) E ON E.IDREF=TXN_IDREF\r\n" +
				"	 LEFT JOIN fcusr01.sttm_customer@fcatfcclink F ON F.customer_no=CIF \n" +
				") \r\n" + 
				"GROUP BY KY, MA_DON_VI, MA_SAN_PHAM, MA_LOAI_KHACH_HANG, MA_DVNB, \r\n" + 
				"'DWH_DRIVER068', '', 'DESTINATION' \r\n" + 
				") \r\n" + 
				"SELECT * FROM ( \r\n" + 
				"    SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER067' MA_DRIVER,'' MA_SAN_PHAM, \r\n" + 
				"    CASE WHEN MA_HOAT_DONG LIKE 'ACT_1%' THEN 'DEP_69' WHEN MA_HOAT_DONG LIKE 'ACT_2%' THEN 'DEP_55' WHEN MA_HOAT_DONG LIKE 'ACT_3%' THEN 'DEP_39' END MA_PHONG_BAN, \r\n" + 
				"    '' MA_LOAI_KHACH_HANG,MA_HOAT_DONG, '' PCMCS_RESERVE2_ID, SUM(SO_TIEN_SO_LUONG) SO_TIEN_SO_LUONG,'SOURCE' TYPE_DESC \r\n" + 
				"    FROM t \r\n" + 
				"    GROUP BY MA_HOAT_DONG,KY \r\n" + 
				"    UNION ALL \r\n" + 
				"    SELECT KY,MA_DON_VI, MA_DRIVER, MA_SAN_PHAM, MA_PHONG_BAN, MA_LOAI_KHACH_HANG, MA_HOAT_DONG, \r\n" + 
				"    PCMCS_RESERVE2_ID, SO_TIEN_SO_LUONG, TYPE_DESC \r\n" + 
				"    FROM t \r\n" + 
				")");
					
		Connection connect = securityDataSourceConfig.securityDataSourceFDSEB().getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());

		ResultSet rs = preStmt.executeQuery();
		List<DvnbSummary> dvnbSummaryList = new ArrayList<DvnbSummary>();
		
		try {
			while(rs.next()) {
				DvnbSummary dvnbSummary = new DvnbSummary();
				dvnbSummary.setKy(rs.getString(1));
				dvnbSummary.setMaDonVi(rs.getString(2));
				dvnbSummary.setMaDriver(rs.getString(3));
				dvnbSummary.setMaSanPham(rs.getString(4));
				dvnbSummary.setMaPhongBan(rs.getString(5));
				dvnbSummary.setMaLoaiKhachHang(rs.getString(6));
				dvnbSummary.setMaHoatDong(rs.getString(7));
				dvnbSummary.setPcmcsReserve2Id(rs.getString(8));
				dvnbSummary.setSoTienSoLuong(rs.getString(9));
				dvnbSummary.setType(rs.getString(10));
	
				dvnbSummaryList.add(dvnbSummary);
			}
			
		}			
		catch (Exception ex){
			LOGGER.error(ex.getMessage());
		}
		finally{
			connect.close();
			rs.close();
		}
		return dvnbSummaryList;
	}
	
	//IB,MB: if MA_DON_VI FDS is null -> get by user, MB: if MA_DON_VI FDS is null -> get by CIF
	@Override
	public List<DvnbSummary> searchAct2080104FromEBHitRuleNhom2(String ky, String userid) throws SQLException {
		
		securityDataSourceConfig = new SecurityDataSourceConfig();
		String sFromDate = ky.substring(2) + ky.substring(0, 2) + "01" + "000000000";
		String sToDate = ky.substring(2) + ky.substring(0,2) + LastDayOfMonth(Integer.valueOf(ky.substring(2)),Integer.valueOf(ky.substring(0,2))) + "999999999";
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("WITH t (KY,MA_DON_VI, MA_DRIVER, MA_SAN_PHAM, MA_PHONG_BAN, MA_LOAI_KHACH_HANG, MA_HOAT_DONG, \r\n" + 
				"PCMCS_RESERVE2_ID, SO_TIEN_SO_LUONG, TYPE_DESC) AS \r\n" + 
				"( \r\n" + 
				"SELECT KY,MA_DON_VI,'DWH_DRIVER068' MA_DRIVER,MA_SAN_PHAM,'' MA_PHONG_BAN,MA_LOAI_KHACH_HANG,MA_DVNB MA_HOAT_DONG, \r\n" + 
				"'' PCMCS_RESERVE2_ID, COUNT(CASE_NO) SO_TIEN_SO_LUONG,'DESTINATION' TYPE_DESC \r\n" + 
				"FROM \r\n" + 
				"( \r\n" + 
				"    SELECT (select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) cre_tms, '" + userid + "' usr_id, null upd_tms, null upd_uid, \r\n" + 
				"    (select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) || REPLACE(TO_CHAR(ROWNUM,'9999999'),' ',0) id_no, \r\n" + 
				"    'ACT_2080104' ma_dvnb, '" + ky + "' ky, \r\n" + 
				"    CASE_NO, THOI_GIAN, TAI_KHOAN_DANG_NHAP, KENH_GD, TAI_KHOAN_GIAO_DICH, CIF, \r\n" + 
				"    CASE WHEN KENH_GD='MB' THEN 'PRO_251' WHEN KENH_GD='IB' THEN 'PRO_247' ELSE 'PRO_255' END MA_SAN_PHAM, \r\n" + 
				"    CASE WHEN F.CUSTOMER_TYPE='I' THEN 'CUS_01' WHEN F.CUSTOMER_TYPE='C' THEN 'CUS_03' END MA_LOAI_KHACH_HANG, \r\n" + 
				"	 'ENT_' || (CASE WHEN KENH_GD='IB' AND CODSRCBRANCH IS NULL AND IB.BRANCH IS NOT NULL THEN (CASE WHEN NVL(CODSRCBRANCH,IB.BRANCH)='000' THEN '137' ELSE NVL(CODSRCBRANCH,IB.BRANCH) END) \r\n" + 
			    "	 WHEN KENH_GD='IB' AND CODSRCBRANCH IS NULL AND IB.BRANCH IS NULL THEN (CASE WHEN NVL(CODSRCBRANCH,F.LOCAL_BRANCH)='000' THEN '137' ELSE NVL(CODSRCBRANCH,F.LOCAL_BRANCH) END) \r\n" + 
			    "	 ELSE (CASE WHEN NVL(CODSRCBRANCH,BRANCH_CODE)='000' THEN '137' ELSE NVL(CODSRCBRANCH,BRANCH_CODE) END) END) MA_DON_VI \r\n" + 
				"    FROM ( \r\n" + 
				"        SELECT C.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,C.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,C.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_EBANK_DESCRIPTION A \r\n" + 
				"        LEFT JOIN SMS.FDS_CASE_STATUS B ON B.CLOSED_REASON = A.TYPE AND B.OTHER LIKE '%'|| A.ID ||'%' \r\n" + 
				"        INNER JOIN SMS.FDS_EBANK_CASE_DETAIL C ON B.CASE_NO = C.CASE_NO \r\n" + 
				"        WHERE TYPE='HCS' AND B.CRE_TMS between " + sFromDate + " and " + sToDate + "\r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT C.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,C.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,C.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_EBANK_DESCRIPTION A \r\n" + 
				"        LEFT JOIN SMS.FDS_CASE_STATUS B ON B.CLOSED_REASON = A.TYPE AND B.OTHER LIKE '%'|| A.ID ||'%' \r\n" + 
				"        INNER JOIN SMS.FDS_EBANK_CASE_DETAIL C ON B.CASE_NO = C.CASE_NO \r\n" + 
				"        WHERE TYPE='HCF' AND B.CRE_TMS between " + sFromDate + " and " + sToDate + "\r\n" + 
				"        UNION ALL \r\n" + 
				"        SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD \r\n" + 
				"        ,B.ACCOUNTNO TAI_KHOAN_GIAO_DICH,TXN_CUSTOMER CIF,B.CUS_TYPE,TXN_IDREF\r\n" + 
				"        FROM SMS.FDS_CASE_STATUS A INNER JOIN SMS.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO \r\n" + 
				"        WHERE A.CLOSED_REASON = 'NCR' AND A.CRE_TMS between " + sFromDate + " and " + sToDate + "\r\n" + 
				"    ) FDS \r\n" + 
				"    LEFT JOIN vninfoscb.mb_customer@mbanking CUST ON CUST.cif_core=FDS.CIF and CUST.cus_status<>'9'\r\n" + 
				"    LEFT JOIN (SELECT DISTINCT U_NAME,BRANCH FROM obdx_admin_prod.scb_users_ext@fcatsmslink \r\n" + 
				"            WHERE (U_NAME||CREATION_DATE||CASE WHEN STATUS='A' THEN 9 ELSE 1 END) IN (SELECT U_NAME||MAX(CREATION_DATE||CASE WHEN STATUS='A' THEN 9 ELSE 1 END) FROM obdx_admin_prod.scb_users_ext@fcatsmslink GROUP BY U_NAME)) IB ON IB.u_name=FDS.TAI_KHOAN_DANG_NHAP \r\n" + 
				"    LEFT JOIN (SELECT DISTINCT IDREF,CODSRCBRANCH FROM sms.FDS_TRANS) E ON E.IDREF=TXN_IDREF\r\n" + 
				"    LEFT JOIN fcusr01.sttm_customer@fcatfcclink F ON F.customer_no=CIF \r\n" + 
				") \r\n" + 
				"GROUP BY KY, MA_DON_VI, MA_SAN_PHAM, MA_LOAI_KHACH_HANG, MA_DVNB, \r\n" + 
				"'DWH_DRIVER068', '', 'DESTINATION' \r\n" + 
				") \r\n" + 
				"SELECT * FROM ( \r\n" + 
				"    SELECT KY,'ENT_000' MA_DON_VI,'DWH_DRIVER067' MA_DRIVER,'' MA_SAN_PHAM, \r\n" + 
				"    CASE WHEN MA_HOAT_DONG LIKE 'ACT_1%' THEN 'DEP_69' WHEN MA_HOAT_DONG LIKE 'ACT_2%' THEN 'DEP_55' WHEN MA_HOAT_DONG LIKE 'ACT_3%' THEN 'DEP_39' END MA_PHONG_BAN, \r\n" + 
				"    '' MA_LOAI_KHACH_HANG,MA_HOAT_DONG, '' PCMCS_RESERVE2_ID, SUM(SO_TIEN_SO_LUONG) SO_TIEN_SO_LUONG,'SOURCE' TYPE_DESC \r\n" + 
				"    FROM t \r\n" + 
				"    GROUP BY MA_HOAT_DONG,KY \r\n" + 
				"    UNION ALL \r\n" + 
				"    SELECT KY,MA_DON_VI, MA_DRIVER, MA_SAN_PHAM, MA_PHONG_BAN, MA_LOAI_KHACH_HANG, MA_HOAT_DONG, \r\n" + 
				"    PCMCS_RESERVE2_ID, SO_TIEN_SO_LUONG, TYPE_DESC \r\n" + 
				"    FROM t \r\n" + 
				")");
					
		Connection connect = securityDataSourceConfig.securityDataSourceFDSEB().getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());

		ResultSet rs = preStmt.executeQuery();
		List<DvnbSummary> dvnbSummaryList = new ArrayList<DvnbSummary>();
		
		try {
			while(rs.next()) {
				DvnbSummary dvnbSummary = new DvnbSummary();
				dvnbSummary.setKy(rs.getString(1));
				dvnbSummary.setMaDonVi(rs.getString(2));
				dvnbSummary.setMaDriver(rs.getString(3));
				dvnbSummary.setMaSanPham(rs.getString(4));
				dvnbSummary.setMaPhongBan(rs.getString(5));
				dvnbSummary.setMaLoaiKhachHang(rs.getString(6));
				dvnbSummary.setMaHoatDong(rs.getString(7));
				dvnbSummary.setPcmcsReserve2Id(rs.getString(8));
				dvnbSummary.setSoTienSoLuong(rs.getString(9));
				dvnbSummary.setType(rs.getString(10));
	
				dvnbSummaryList.add(dvnbSummary);
			}
			
		}			
		catch (Exception ex){
			LOGGER.error(ex.getMessage());
		}
		finally{
			connect.close();
			rs.close();
		}
		return dvnbSummaryList;
	}

	private static int LastDayOfMonth(int Y, int M) {
	    return LocalDate.of(Y, M, 1).getMonth().length(Year.of(Y).isLeap());
	}

	
		
}
