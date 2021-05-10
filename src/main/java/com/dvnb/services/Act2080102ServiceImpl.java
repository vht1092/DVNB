package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080102;
import com.dvnb.repositories.Act2080102Repo;
import com.vaadin.server.VaadinServlet;

import oracle.jdbc.OracleTypes;

@Service("act2080102Service")
@Transactional
public class Act2080102ServiceImpl implements Act2080102Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080102Repo act2080102Repo;
	
	@Autowired
    private EntityManager em;
	
	@Autowired
	private DataSource dataSource;
	
	private SpringConfigurationValueHelper configurationHelper;
	
	final TimeConverter timeConverter = new TimeConverter();

	@Override
	public Page<Act2080102> findAll(Pageable page) {
		return act2080102Repo.findAll(page);
	}

	@Override
	public Page<Act2080102> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080102Repo.findAllByKy(kyBaoCao, page);
	}
	
	@Override
	public void create(Act2080102 act2080102) {

		act2080102Repo.save(act2080102);
	}
	
	@Override
	public void importGiaoDichEbankHitRuleNhom1(String usrId, String kyBaoCao) throws SQLException {
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		dataSource = (DataSource) helper.getBean("dataSource");

		String sFromDate = kyBaoCao.substring(2) + kyBaoCao.substring(0, 2) + "01" + "000000000";
		String sToDate = kyBaoCao.substring(2) + kyBaoCao.substring(0,2) + LastDayOfMonth(Integer.valueOf(kyBaoCao.substring(2)),Integer.valueOf(kyBaoCao.substring(0,2))) + "999999999";
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("SELECT CASE_NO, THOI_GIAN, TAI_KHOAN_DANG_NHAP, KENH_GD, TAI_KHOAN_GD, CIF, " + 
				"    CASE WHEN CUS_TYPE='CA NHAN' THEN 'CUS_01' WHEN CUS_TYPE='DOANH NGHIEP' THEN 'CUS_04' END MA_LOAI_KHACH_HANG,  " + 
				"    CASE WHEN KENH_GD='MB' THEN 'PRO_251' WHEN KENH_GD='IB' THEN 'PRO_247' ELSE 'PRO_255' END MA_SAN_PHAM,  " + 
				"    MA_DV_QUAN_LY_DV " + 
				"FROM ( " + 
				"SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD " + 
				",ACCOUNTNO TAI_KHOAN_GD,TXN_CUSTOMER CIF,CUS_TYPE,'' MA_DV_QUAN_LY_DV " + 
				"FROM FPT.FDS_CASE_STATUS A INNER JOIN FPT.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO " + 
				"WHERE A.CLOSED_REASON IN ('CCE','CCR','CRC') AND A.CRE_TMS between 20190901000000000 and  20190930999999999  " + 
				"UNION ALL " + 
				"SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD " + 
				",ACCOUNTNO TAI_KHOAN_GD,TXN_CUSTOMER CIF,CUS_TYPE,'' MA_DV_QUAN_LY_DV " + 
				"FROM FPT.FDS_EBANK_DESCRIPTION A " + 
				"LEFT JOIN FPT.FDS_CASE_STATUS B ON B.CLOSED_REASON = A.TYPE AND B.OTHER LIKE '%'|| A.ID ||'%' " + 
				"INNER JOIN FPT.FDS_EBANK_CASE_DETAIL C ON B.CASE_NO = C.CASE_NO  " + 
				"WHERE TYPE='CCN' AND B.CRE_TMS between 20190901000000000 and  20190930999999999  " + 
				"UNION ALL " + 
				"SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD " + 
				",ACCOUNTNO TAI_KHOAN_GD,TXN_CUSTOMER CIF,CUS_TYPE,'' MA_DV_QUAN_LY_DV " + 
				"FROM FPT.FDS_CASE_STATUS A INNER JOIN FPT.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO " + 
				"WHERE A.CLOSED_REASON = 'CCT' AND A.CRE_TMS between 20190901000000000 and  20190930999999999  " + 
				"UNION ALL " + 
				"SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD " + 
				",ACCOUNTNO TAI_KHOAN_GD,TXN_CUSTOMER CIF,CUS_TYPE,'' MA_DV_QUAN_LY_DV " + 
				"FROM FPT.FDS_CASE_STATUS A INNER JOIN FPT.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO " + 
				"WHERE A.CLOSED_REASON = 'CNC' AND A.CRE_TMS between 20190901000000000 and  20190930999999999  " + 
				"UNION ALL " + 
				"SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD " + 
				",ACCOUNTNO TAI_KHOAN_GD,TXN_CUSTOMER CIF,CUS_TYPE,'' MA_DV_QUAN_LY_DV " + 
				"FROM FPT.FDS_CASE_STATUS A INNER JOIN FPT.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO " + 
				"WHERE A.CLOSED_REASON = 'BCC' AND A.CRE_TMS between 20190901000000000 and  20190930999999999  " + 
				"UNION ALL " + 
				"SELECT B.CASE_NO,TXN_CRE_TMS THOI_GIAN,TXN_USERNAME TAI_KHOAN_DANG_NHAP,TXN_CHANNEL KENH_GD " + 
				",ACCOUNTNO TAI_KHOAN_GD,TXN_CUSTOMER CIF,CUS_TYPE LOAI_KH,'' MA_DV_QUAN_LY_DV " + 
				"FROM FPT.FDS_CASE_STATUS A INNER JOIN FPT.FDS_EBANK_CASE_DETAIL B ON A.CASE_NO = B.CASE_NO " + 
				"WHERE A.CLOSED_REASON = 'NCR' AND A.CRE_TMS between 20190901000000000 and  20190930999999999  " + 
				")");
					
		
		Connection connect = dataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());

		ResultSet rs = preStmt.executeQuery();
//		List<Act2080102> act2080102List = new ArrayList<Act2080102>();
		int i=0;
		
		
		while(rs.next()) {
			i++;
			Act2080102 act2080102 = new Act2080102();
			act2080102.setCreTms(timeConverter.getCurrentTime());
			act2080102.setUsrId(usrId);
			act2080102.setIdno(timeConverter.getCurrentTime() + String.format("%07d", i));
			act2080102.setMaDvnb("ACT_2080102");
			act2080102.setKy(kyBaoCao);
			act2080102.setCaseNo(rs.getString("CASE_NO"));
			act2080102.setThoiGian(new BigDecimal(rs.getString("THOI_GIAN")));
			act2080102.setTaiKhoanDangNhap(rs.getString("TAI_KHOAN_DANG_NHAP"));
			act2080102.setKenhGd(rs.getString("KENH_GD"));
			act2080102.setTaiKhoanGiaoDich(rs.getString("TAI_KHOAN_GD"));
			act2080102.setCif(rs.getString("CIF"));
			act2080102.setMaSanPham(rs.getString("MA_SAN_PHAM"));
			act2080102.setMaLoaiKhachHang(rs.getString("MA_LOAI_KHACH_HANG"));
//			act2080102.setMaDonVi(rs.getString("MA_DON_VI"));

//			act2080102List.add(act2080102);
			act2080102Repo.save(act2080102);
		}
		
		preStmt.close();
//		return act2080102List;
	}
	
	private static int LastDayOfMonth(int Y, int M) {
	    return LocalDate.of(Y, M, 1).getMonth().length(Year.of(Y).isLeap());
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080102Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080102Repo.deleteByKyAndUsrId(ky, userId);
	}
}
