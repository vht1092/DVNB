package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.dvnb.entities.Act2070101;
import com.dvnb.entities.Act2080101;
import com.dvnb.repositories.Act2080101Repo;

import oracle.jdbc.OracleTypes;

@Service("act2080101Service")
@Transactional
public class Act2080101ServiceImpl implements Act2080101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080101Repo act2080101Repo;
	
	@Autowired
    private EntityManager em;
	
	@Autowired
	private DataSource dataSource;


	@Override
	public Page<Act2080101> findAll(Pageable page) {
		return act2080101Repo.findAll(page);
	}

	@Override
	public Page<Act2080101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080101Repo.findAllByKy(kyBaoCao, page);
	}
	
	@Override
	public void create(Act2080101 act2080101) {

		act2080101Repo.save(act2080101);
	}
	
	@Override
	public Act2080101 getERPMappingByLOC(BigDecimal loc) {
		// TODO Auto-generated method stub
		String sp_GetClaimPaidList = "{call GET_ERP_MAPPING_BY_LOC(?,?)}";
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement callableStatement = null;
//		List<act2080101> act2080101List = new ArrayList<act2080101>();
		Act2080101 act2080101 = new Act2080101();
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_GetClaimPaidList);
			callableStatement.setBigDecimal(1, loc);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
			callableStatement.executeUpdate();
			rs = (ResultSet) callableStatement.getObject(2);
			
			while(rs.next()) {
				act2080101.setMaSanPham(rs.getString("MA_SAN_PHAM"));
				act2080101.setMaLoaiKhachHang(rs.getString("MA_LOAI_KHACH_HANG"));
				act2080101.setMaDonVi(rs.getString("MA_DON_VI"));
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			try {
				conn.close();
				rs.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return act2080101;
	} 
	
	@Override
	public void updateErpMappingById(String id) {
		// TODO Auto-generated method stub
		String sp_GetClaimPaidList = "{call ACT_2080101_UPDATE_MAPPING(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_GetClaimPaidList);
			callableStatement.setString(1, id);
			callableStatement.executeUpdate();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2080101Repo.updateErpMappingByKybaocao(ky);
		
	} 
	
	@Override
	public List<Act2080101> findGiaoDichHitRuleNhom1(String fromdate,String todate) {
		return act2080101Repo.findGiaoDichHitRuleNhom1(fromdate, todate);
	}
	
//	@Override
//	public List<Act2080101> findGiaoDichHitRuleNhom1(String fromdate,String todate) {
//		return act2080101Repo.findGiaoDichHitRuleNhom1();
//	}
	
	@Override
	public void importGiaoDichHitRuleNhom1(String fromdate,String todate,String usrid,String kybaocao) {
		act2080101Repo.importGiaoDichHitRuleNhom1(fromdate, todate, usrid, kybaocao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080101Repo.deleteByKyAndUsrId(ky, userId);
	}
}
