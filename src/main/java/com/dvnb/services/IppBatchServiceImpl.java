package com.dvnb.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import oracle.jdbc.OracleTypes;

@Service("ippBatchService")
@Transactional
public class IppBatchServiceImpl implements IppBatchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IppBatchServiceImpl.class);
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	
	@Autowired
	private DataSource dataSource;


	@Override
	public String chuyenNgayTraGop() {
		// TODO Auto-generated method stub
		String sp_IppDailyApplXfer = "{call IPP_DAILY_APPL_XFER(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_IppDailyApplXfer);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	}


	@Override
	public String tongHopThongTinTaiChinhIppDailyProcess() {
		// TODO Auto-generated method stub
		String sp_IppDailyProcess = "{call IPP_DAILY_PROCESS(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_IppDailyProcess);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	}


	@Override
	public String tongHopThongTinTaiChinhIppDailyGlGen() {
		// TODO Auto-generated method stub
		String sp_IppDailyGlGen = "{call IPP_DAILY_GL_GEN(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_IppDailyGlGen);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	}


	@Override
	public String tongHopThongTinTaiChinhIppDailyLocBal() {
		// TODO Auto-generated method stub
		String sp_IppDailyIppLocBal = "{call IPP_DAILY_IPP_LOC_BAL(?)}";
		Connection conn = null;
		CallableStatement callableStatement = null;
		String responseCode = "";
		try {
			conn = dataSource.getConnection();
			callableStatement = conn.prepareCall(sp_IppDailyIppLocBal);
			callableStatement.registerOutParameter(1, OracleTypes.VARCHAR);
			callableStatement.executeUpdate();
			responseCode = callableStatement.getString(1);
		}
		catch (Exception ex){
			ex.printStackTrace();
			LOGGER.error(ex.getMessage());
		}
		finally{
			try {
				conn.close();
				callableStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
		
		return responseCode;
	} 
	
}
