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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dvnb.SpringContextHelper;
import com.dvnb.entities.DsqtHachToan;
import com.dvnb.entities.DsqtHachToanCustom;
import com.dvnb.entities.DvnbSummaryPbcp;
import com.dvnb.repositories.DsqtHachToanRepo;
import com.vaadin.server.VaadinServlet;

@Service("dsqtHachToanService")
@Transactional
public class DsqtHachToanServiceImpl implements DsqtHachToanService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DsqtHachToanRepo dsqtHachToanRepo;
	
	protected DataSource localDataSource;
	
	@Override
	public void save(DsqtHachToan dsqtHachToan) {
		// TODO Auto-generated method stub
		dsqtHachToanRepo.save(dsqtHachToan);
	}
	
	@Override
	public List<DsqtHachToan> findAllByNgayAdv(String ngayAdv) {
		// TODO Auto-generated method stub
		return dsqtHachToanRepo.findAllByNgayAdv(ngayAdv);
	}
	
	@Override
	public List<DsqtHachToan> findAllByNgayAdvAndCardBrnAndCurr(String ngayAdv, String cardBrn, String curr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DsqtHachToanCustom> findHachToanByNgayAdvAndCardBrnAndCurr(String ngayAdv, String cardbrn, String curr) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		sqlString 	.append("WITH t AS   \r\n" + 
				"(   \r\n" + 
				"SELECT FILE_NAME,CYCLE_NO,TRANS_TYPE,LOAI_HACH_TOAN,   \r\n" + 
				"SUM(COUNT) SL_GD_CHAP_THUAN,   \r\n" + 
				"SUM(ST_TRICH_NO) ST_BIDV_TRICH_NO,   \r\n" + 
				"CASE WHEN TRANS_TYPE IN ('GDCBTTHH','GDCBRTM','GDMSFF') THEN SUM(ST_TRICH_NO)-SUM(THU_PHI_INTERCHANGE)+SUM(PHI_INTERCHANGE_PHAI_TRA)  \r\n" + 
				"    ELSE SUM(ST_TRICH_NO)+SUM(THU_PHI_INTERCHANGE)-SUM(PHI_INTERCHANGE_PHAI_TRA) END ST_GD,   \r\n" + 
				"SUM(THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,   \r\n" + 
				"SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,REVERSAL,T140_DATE   \r\n" + 
				"FROM   \r\n" + 
				"(   \r\n" + 
				"    SELECT FILE_NAME,CYCLE_NO,NGAY_ADV,TRANS_TYPE,CARD_BRN,CURR,COUNT,   \r\n" + 
				"    CASE WHEN (AMOUNT_TYPE='CR' AND REVERSAL='R') OR (TRANS_TYPE IN ('GDCBRTM','GDCBTTHH') AND AMOUNT_TYPE='CR') THEN -AMOUNT ELSE AMOUNT END ST_TRICH_NO,   \r\n" + 
				"    AMOUNT_TYPE,   \r\n" + 
				"    CASE WHEN TRANS_TYPE IN ('GDTTHH','GDMSFF','GDCBRTM','GDCBTTHH') THEN   \r\n" + 
				"            (CASE WHEN (AMOUNT_TYPE='CR' AND REVERSAL='R') OR (TRANS_TYPE IN ('GDCBRTM','GDCBTTHH') AND AMOUNT_TYPE='CR') THEN -TRANS_FEE ELSE TRANS_FEE END)    \r\n" + 
				"        ELSE 0 END THU_PHI_INTERCHANGE,   \r\n" + 
				"           \r\n" + 
				"    CASE WHEN TRANS_TYPE IN ('GDRTM') THEN   \r\n" + 
				"            (CASE WHEN AMOUNT_TYPE='CR' AND REVERSAL='R' THEN -TRANS_FEE ELSE TRANS_FEE END)    \r\n" + 
				"        ELSE 0 END PHI_INTERCHANGE_PHAI_TRA,   \r\n" + 
				"       \r\n" + 
				"    CASE WHEN AMOUNT_TYPE = 'DR' OR (AMOUNT_TYPE='CR' AND REVERSAL='R') THEN 'BAO_NO' ELSE 'BAO_CO' END LOAI_HACH_TOAN,   \r\n" + 
				"    CASE WHEN TRANS_TYPE='GDTTHH' THEN 'A'    \r\n" + 
				"        WHEN TRANS_TYPE='GDRTM' THEN 'B'    \r\n" + 
				"        WHEN TRANS_TYPE='GDMSFF' THEN 'C'    \r\n" + 
				"        WHEN TRANS_TYPE='GDNONFI' THEN 'D'    \r\n" + 
				"        WHEN TRANS_TYPE IN ('GDCBTTHH','GDCBRTM') THEN 'E'    \r\n" + 
				"        WHEN TRANS_TYPE='FEECOLL' THEN 'E' END SEQ_NO,   \r\n" + 
				"    REVERSAL,T140_DATE   \r\n" + 
				"    FROM DSQT_HACH_TOAN   \r\n" + 
				"    WHERE NGAY_ADV=? AND CURR=? AND CARD_BRN=?\r\n" + 
				"    order by TRANS_TYPE DESC   \r\n" + 
				")   \r\n" + 
				"GROUP BY FILE_NAME,CYCLE_NO,TRANS_TYPE,LOAI_HACH_TOAN,SEQ_NO,REVERSAL,T140_DATE   \r\n" + 
				"ORDER BY SEQ_NO,LOAI_HACH_TOAN DESC   \r\n" + 
				")   \r\n" + 
				"SELECT 'A. GIAO DỊCH THANH TOÁN HÀNG HÓA' HACH_TOAN, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE, ' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '1. Báo nợ' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE, ' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA, T140_DATE, FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDTTHH' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDTTHH' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '2. Báo có' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA, T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDTTHH' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM(THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDTTHH' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Tổng' A,SUM(SL_GD_CHAP_THUAN),   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_BIDV_TRICH_NO,    \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_GD ELSE ST_GD END) ST_GD,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -THU_PHI_INTERCHANGE ELSE THU_PHI_INTERCHANGE END) THU_PHI_INTERCHANGE,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -PHI_INTERCHANGE_PHAI_TRA ELSE PHI_INTERCHANGE_PHAI_TRA END) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDTTHH'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'B. GIAO DỊCH RÚT TM' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '1. Báo nợ' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDRTM' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDRTM' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '2. Báo có' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDRTM' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDRTM' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Tổng' A,SUM(SL_GD_CHAP_THUAN),   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_BIDV_TRICH_NO,    \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_GD ELSE ST_GD END) ST_GD,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -THU_PHI_INTERCHANGE ELSE THU_PHI_INTERCHANGE END) THU_PHI_INTERCHANGE,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -PHI_INTERCHANGE_PHAI_TRA ELSE PHI_INTERCHANGE_PHAI_TRA END) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDRTM'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'C. GIAO DỊCH MONEYSEND' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '1. Báo nợ' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDMSFF' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDMSFF' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '2. Báo có' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDMSFF' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDMSFF' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Tổng' A,SUM(SL_GD_CHAP_THUAN),   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_BIDV_TRICH_NO,    \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_GD ELSE ST_GD END) ST_GD,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -THU_PHI_INTERCHANGE ELSE THU_PHI_INTERCHANGE END) THU_PHI_INTERCHANGE,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -PHI_INTERCHANGE_PHAI_TRA ELSE PHI_INTERCHANGE_PHAI_TRA END) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDMSFF'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'D. PHÍ GIAO DỊCH PHI TÀI CHÍNH (KHCN)' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '1. Báo nợ' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDNONFI' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDNONFI' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '2. Báo có' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDNONFI' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDNONFI' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Tổng' A,SUM(SL_GD_CHAP_THUAN),   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_BIDV_TRICH_NO,    \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_GD ELSE ST_GD END) ST_GD,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -THU_PHI_INTERCHANGE ELSE THU_PHI_INTERCHANGE END) THU_PHI_INTERCHANGE,   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -PHI_INTERCHANGE_PHAI_TRA ELSE PHI_INTERCHANGE_PHAI_TRA END) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDNONFI'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'E. GIAO DỊCH CHARGEBACK' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '1. GD thanh toán hàng hóa' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDCBTTHH' \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDCBTTHH' \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '2. GD rút tiền mặt' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_GD, THU_PHI_INTERCHANGE, PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDCBRTM' \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN),SUM(ST_BIDV_TRICH_NO), SUM(ST_GD),SUM (THU_PHI_INTERCHANGE) THU_PHI_INTERCHANGE,SUM(PHI_INTERCHANGE_PHAI_TRA) PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='GDCBRTM' \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'F. FEECOLL (không tính vào TỔNG)' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '1. Báo nợ' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO, ST_BIDV_TRICH_NO ST_GD, NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='FEECOLL' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN) SL_GD_CHAP_THUAN,SUM(ST_BIDV_TRICH_NO) ST_BIDV_TRICH_NO,SUM(ST_BIDV_TRICH_NO) ST_GD, NULL THU_PHI_INTERCHANGE,NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='FEECOLL' AND LOAI_HACH_TOAN='BAO_NO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT '2. Báo có' A, NULL SL_GD_CHAP_THUAN,NULL ST_BIDV_TRICH_NO, NULL ST_GD,NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM DUAL   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT ' ' A,SL_GD_CHAP_THUAN,ST_BIDV_TRICH_NO,ST_BIDV_TRICH_NO ST_GD, NULL THU_PHI_INTERCHANGE, NULL PHI_INTERCHANGE_PHAI_TRA,T140_DATE,FILE_NAME, CYCLE_NO  \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='FEECOLL' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Cộng' A,SUM(SL_GD_CHAP_THUAN) SL_GD_CHAP_THUAN, SUM(ST_BIDV_TRICH_NO) ST_BIDV_TRICH_NO,SUM(ST_BIDV_TRICH_NO) ST_GD ,NULL THU_PHI_INTERCHANGE,NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='FEECOLL' AND LOAI_HACH_TOAN='BAO_CO'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'Tổng' A,SUM(SL_GD_CHAP_THUAN),   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_BIDV_TRICH_NO,    \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_GD,   \r\n" + 
				"NULL THU_PHI_INTERCHANGE,   \r\n" + 
				"NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE='FEECOLL'   \r\n" + 
				"UNION ALL   \r\n" + 
				"SELECT 'TỔNG' A,SUM(SL_GD_CHAP_THUAN),   \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_BIDV_TRICH_NO ELSE ST_BIDV_TRICH_NO END) ST_BIDV_TRICH_NO,    \r\n" + 
				"SUM(CASE WHEN LOAI_HACH_TOAN='BAO_CO' THEN -ST_GD ELSE ST_GD END) ST_GD,   \r\n" + 
				"NULL THU_PHI_INTERCHANGE,   \r\n" + 
				"NULL PHI_INTERCHANGE_PHAI_TRA,' ' T140_DATE,' ' FILE_NAME, NULL CYCLE_NO    \r\n" + 
				"FROM t   \r\n" + 
				"WHERE TRANS_TYPE<>'FEECOLL'");
		
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		preStmt.setString(1, ngayAdv);
		preStmt.setString(2, curr);
		preStmt.setString(3, cardbrn);
		
		ResultSet rs = preStmt.executeQuery();
		List<DsqtHachToanCustom> listHachToanCustom = new ArrayList<DsqtHachToanCustom>();
		while(rs.next()) {
			
			DsqtHachToanCustom item = new DsqtHachToanCustom();
			item.setNgayAdv(ngayAdv);
			item.setCardBrn(cardbrn);
			item.setCurr(curr);
			item.setHachToan(rs.getString("HACH_TOAN"));
			item.setSlGdChapThuan(BigDecimal.valueOf(rs.getDouble("SL_GD_CHAP_THUAN")));
			item.setStBidvTrichNo(BigDecimal.valueOf(rs.getDouble("ST_BIDV_TRICH_NO")));
			item.setStGd(BigDecimal.valueOf(rs.getDouble("ST_GD")));
			item.setThuPhiInterchange(BigDecimal.valueOf(rs.getDouble("THU_PHI_INTERCHANGE")));
			item.setPhiInterchangePhaiTra(BigDecimal.valueOf(rs.getDouble("PHI_INTERCHANGE_PHAI_TRA")));
			item.setPhiXuLyGdATM(null);
			item.setT140(rs.getString("T140_DATE"));
			item.setFilename(rs.getString("FILE_NAME"));
			item.setCycle(rs.getString("CYCLE_NO"));
			listHachToanCustom.add(item);
		}
		
		preStmt.close();
		return listHachToanCustom;
	}

	
	@Override
	public int countByFilename(String filename) {
		// TODO Auto-generated method stub
		return dsqtHachToanRepo.countByFilename(filename);
	}

	@Override
	public List<String> findAllFilenameByNgayadv(String ngayadv) {
		// TODO Auto-generated method stub
		return dsqtHachToanRepo.findAllFilenameByNgayadv(ngayadv);
	}

	@Override
	public int deleteAllByNgayAdv(String ngayadv) {
		// TODO Auto-generated method stub
		return dsqtHachToanRepo.deleteAllByNgayAdv(ngayadv);
	}

	
	
	
	

}
