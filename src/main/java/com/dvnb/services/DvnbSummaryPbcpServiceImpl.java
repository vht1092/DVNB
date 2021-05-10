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
import com.dvnb.entities.DvnbSummaryPbcp;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.repositories.DvnbInvoiceMcRepo;
import com.dvnb.repositories.DvnbSummaryRepo;
import com.vaadin.server.VaadinServlet;

import oracle.jdbc.OracleTypes;

@Service("dvnbSummaryPbcpService")
@Transactional
public class DvnbSummaryPbcpServiceImpl implements DvnbSummaryPbcpService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DvnbSummaryPbcpServiceImpl.class);
	
	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	
	protected DataSource localDataSource;
	protected SecurityDataSourceConfig securityDataSourceConfig = new SecurityDataSourceConfig();
	private final transient TimeConverter timeConverter = new TimeConverter();

	
	@Override
	public List<DvnbSummaryPbcp> searchAllDataByDriverAndKyAndCardbrn(String tuky, String denKy, String cardbrn) throws SQLException {
		
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("SELECT MA_DRIVER, DRIVER_DESCRIPTION,NVL(SUM(DEBIT),0) DEBIT, NVL(SUM(CREDIT),0) CREDIT, NVL(SUM(KHCN),0) KHCN, NVL(SUM(KHDN),0) KHDN\r\n" + 
				"FROM\r\n" + 
				"(\r\n" + 
				"    SELECT A.MA_DRIVER ,DESCRIPTION DRIVER_DESCRIPTION , CASE WHEN LOAI_THE = 'DEBIT' THEN AMOUNT END DEBIT,\r\n" + 
				"    CASE WHEN LOAI_THE = 'CREDIT' THEN AMOUNT END CREDIT,\r\n" + 
				"    CASE WHEN MA_LOAI_KHACH_HANG = '01' THEN AMOUNT END KHCN,\r\n" + 
				"    CASE WHEN MA_LOAI_KHACH_HANG = '03' THEN AMOUNT END KHDN\r\n" + 
				"    FROM DVNB_INVOICE_TONGHOP A\r\n" + 
				"    LEFT JOIN DVNB_DRIVER B ON A.MA_DRIVER= B.DRIVER_CDE\r\n" + 
				"    WHERE KY BETWEEN ? AND ?\r\n" + 
				"    AND A.CRD_BRN=?\r\n" + 
				")\r\n" + 
				"GROUP BY MA_DRIVER,DRIVER_DESCRIPTION\r\n" + 
				"ORDER BY MA_DRIVER ASC");
		
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		preStmt.setString(1, tuky);
		preStmt.setString(2, denKy);
		preStmt.setString(3, cardbrn);
		
		ResultSet rs = preStmt.executeQuery();
		List<DvnbSummaryPbcp> dvnbSummaryList = new ArrayList<DvnbSummaryPbcp>();
		while(rs.next()) {
			
			DvnbSummaryPbcp dvnbSummary = new DvnbSummaryPbcp();
			dvnbSummary.setDriverCode(rs.getString(1));
			dvnbSummary.setDriverDesc(rs.getString(2));
			dvnbSummary.setTotalDebit(new BigDecimal(rs.getString(3)));
			dvnbSummary.setTotalCredit(new BigDecimal(rs.getString(4)));
			dvnbSummary.setTotalKhcn(new BigDecimal(rs.getString(5)));
			dvnbSummary.setTotalKhdn(new BigDecimal(rs.getString(6)));

			dvnbSummaryList.add(dvnbSummary);
		}
		
		preStmt.close();
		return dvnbSummaryList;
	}


		
}
