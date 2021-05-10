package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dvnb.SpringContextHelper;
import com.dvnb.entities.PhiInterchange;
import com.vaadin.server.VaadinServlet;

@Service("phiInterchangeService")
@Transactional
public class PhiInterchangeServiceImpl implements PhiInterchangeService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	protected DataSource localDataSource;
	
	@Override
	public List<PhiInterchange> phiInterchangeGDRTMDuocTQT(String curr, String ngayAdv) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("SELECT * FROM\r\n" + 
				"(\r\n" + 
				"    SELECT 'I' CUSTOMER_TYPE,LTTQT,NGAY_ADV,SUM(INTERCHANGE) INTERCHANGE\r\n" + 
				"    FROM FPT.DSQT_DATA A \r\n" + 
				"    INNER JOIN ccps.ir121@im ON trim(PX_IR121_CRD_PGM)=A.CRD_PGM AND f9_ir121_prfx not in ('512454','5471390','5471391')\r\n" + 
				"    AND ((MA_GD LIKE '01%' AND STATUS_CW=' ' AND REVERSAL_IND=' ')\r\n" + 
				"    OR (MA_GD LIKE '01%' AND STATUS_CW<>' ') )\r\n" + 
				"    GROUP BY LTTQT,NGAY_ADV\r\n" + 
				"    UNION ALL\r\n" + 
				"    SELECT 'C' CUSTOMER_TYPE,LTTQT,NGAY_ADV,SUM(INTERCHANGE) INTERCHANGE\r\n" + 
				"    FROM FPT.DSQT_DATA A \r\n" + 
				"    INNER JOIN ccps.ir121@im ON trim(PX_IR121_CRD_PGM)=A.CRD_PGM AND f9_ir121_prfx in ('512454','5471390','5471391')\r\n" + 
				"    AND ((MA_GD LIKE '01%' AND STATUS_CW=' ' AND REVERSAL_IND=' ')\r\n" + 
				"    OR (MA_GD LIKE '01%' AND STATUS_CW<>' ') )\r\n" + 
				"    GROUP BY LTTQT,NGAY_ADV\r\n" + 
				")\r\n" + 
				"WHERE LTTQT=? AND NGAY_ADV=? ");
		
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		preStmt.setString(1, curr);
		preStmt.setString(2, ngayAdv);
		
		ResultSet rs = preStmt.executeQuery();
		List<PhiInterchange> phiInterchangeList = new ArrayList<PhiInterchange>();
		while(rs.next()) {
			
			PhiInterchange interchange = new PhiInterchange();
			interchange.setCustType(rs.getString(1));
			interchange.setInterchange(new BigDecimal(rs.getString(4)));

			phiInterchangeList.add(interchange);
		}
		
		preStmt.close();
		return phiInterchangeList;
		
	}

	@Override
	public List<PhiInterchange> phiInterchangeGDMoneySendFF(String curr, String ngayAdv) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("SELECT * FROM\r\n" + 
				"(\r\n" + 
				"    SELECT 'I' CUSTOMER_TYPE,LTTQT,NGAY_ADV,SUM(INTERCHANGE) INTERCHANGE\r\n" + 
				"    FROM FPT.DSQT_DATA A \r\n" + 
				"    INNER JOIN ccps.ir121@im ON trim(PX_IR121_CRD_PGM)=A.CRD_PGM AND f9_ir121_prfx not in ('512454','5471390','5471391')\r\n" + 
				"    AND MA_GD LIKE '28%' AND STATUS_CW=' '\r\n" + 
				"    GROUP BY LTTQT,NGAY_ADV\r\n" + 
				"    UNION ALL\r\n" + 
				"    SELECT 'C' CUSTOMER_TYPE,LTTQT,NGAY_ADV,SUM(INTERCHANGE) INTERCHANGE\r\n" + 
				"    FROM FPT.DSQT_DATA A \r\n" + 
				"    INNER JOIN ccps.ir121@im ON trim(PX_IR121_CRD_PGM)=A.CRD_PGM AND f9_ir121_prfx in ('512454','5471390','5471391')\r\n" + 
				"    AND MA_GD LIKE '28%' AND STATUS_CW=' '\r\n" + 
				"    GROUP BY LTTQT,NGAY_ADV\r\n" + 
				")\r\n" + 
				"WHERE LTTQT=? AND NGAY_ADV=? ");
		
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		preStmt.setString(1, curr);
		preStmt.setString(2, ngayAdv);
		
		ResultSet rs = preStmt.executeQuery();
		List<PhiInterchange> phiInterchangeList = new ArrayList<PhiInterchange>();
		while(rs.next()) {
			
			PhiInterchange interchange = new PhiInterchange();
			interchange.setCustType(rs.getString(1));
			interchange.setInterchange(new BigDecimal(rs.getString(4)));

			phiInterchangeList.add(interchange);
		}
		
		preStmt.close();
		return phiInterchangeList;
	}

	@Override
	public List<PhiInterchange> phiInterchangeHoanTraGDRTM(String curr, String ngayAdv) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("SELECT * FROM\r\n" + 
				"(\r\n" + 
				"    SELECT 'I' CUSTOMER_TYPE,LTTQT,NGAY_ADV,SUM(INTERCHANGE) INTERCHANGE\r\n" + 
				"    FROM FPT.DSQT_DATA A \r\n" + 
				"    INNER JOIN ccps.ir121@im ON trim(PX_IR121_CRD_PGM)=A.CRD_PGM AND f9_ir121_prfx not in ('512454','5471390','5471391')\r\n" + 
				"    AND MA_GD LIKE '01%' AND REVERSAL_IND='R' AND STATUS_CW=' '\r\n" + 
				"    GROUP BY LTTQT,NGAY_ADV\r\n" + 
				"    UNION ALL\r\n" + 
				"    SELECT 'C' CUSTOMER_TYPE,LTTQT,NGAY_ADV,SUM(INTERCHANGE) INTERCHANGE\r\n" + 
				"    FROM FPT.DSQT_DATA A \r\n" + 
				"    INNER JOIN ccps.ir121@im ON trim(PX_IR121_CRD_PGM)=A.CRD_PGM AND f9_ir121_prfx in ('512454','5471390','5471391')\r\n" + 
				"    AND MA_GD LIKE '01%' AND REVERSAL_IND='R' AND STATUS_CW=' '\r\n" + 
				"    GROUP BY LTTQT,NGAY_ADV\r\n" + 
				")\r\n" + 
				"WHERE LTTQT=? AND NGAY_ADV=? ");
		
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		preStmt.setString(1, curr);
		preStmt.setString(2, ngayAdv);
		
		ResultSet rs = preStmt.executeQuery();
		List<PhiInterchange> phiInterchangeList = new ArrayList<PhiInterchange>();
		while(rs.next()) {
			
			PhiInterchange interchange = new PhiInterchange();
			interchange.setCustType(rs.getString(1));
			interchange.setInterchange(new BigDecimal(rs.getString(4)));

			phiInterchangeList.add(interchange);
		}
		
		preStmt.close();
		return phiInterchangeList;
	}

	
}
