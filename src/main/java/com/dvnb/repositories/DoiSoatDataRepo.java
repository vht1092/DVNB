package com.dvnb.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DoiSoatData;

@Repository
public interface DoiSoatDataRepo extends JpaRepository<DoiSoatData, String> {

	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE ST_TRICH_NO_KH_GD <> 0 AND SO_THE=:cardno AND MA_CAP_PHEP=:apvcode AND MA_GD=:maGd AND REVERSAL_IND=:reversalInd " + 
			"AND NGAY_ADV=:ngayadv", nativeQuery = true)
	DoiSoatData findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("maGd") String maGd,@Param("reversalInd") String reversalInd,@Param("ngayadv") String ngayadv);
	
	Page<DoiSoatData> findAll(Pageable page);
	
	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay", nativeQuery = true)
	List<DoiSoatData> findAllTuNgayDenNgay(@Param("tungay") String tungay,@Param("denngay") String denngay);
	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay " + 
			"AND ('All'=:cardno OR SO_THE=:cardno) " + 
			"AND ('All'=:apvcode OR MA_CAP_PHEP=:apvcode) " + 
			"AND ('All'=:dvcnt OR DVCNT LIKE '%' || :dvcnt || '%') ", nativeQuery = true)
	List<DoiSoatData> findAllTuNgayDenNgayAndPanAndApvCodeAndDvcnt(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("dvcnt") String dvcnt);
	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay " + 
			"AND ('All'=:cardno OR SO_THE=:cardno) " + 
			"AND ('All'=:apvcode OR MA_CAP_PHEP=:apvcode) " +
			"AND ('999' IN :magd OR SUBSTR(MA_GD,0,2) IN :magd) " + 
			"AND ('All'=:cardType " + 
			"    OR ('MD'=:cardType AND (SUBSTR(SO_THE,1,7) IN (5471390,5471391,5507960,5507961) OR SUBSTR(SO_THE,1,6)=524188)) " + 
			"    OR ('MC'=:cardType AND SUBSTR(SO_THE,1,6) IN (510235,512454,545579,554627)) " + 
			"    OR ('VS'=:cardType AND (SUBSTR(SO_THE,1,7) IN (4895160,4895170,4895180) OR SUBSTR(SO_THE,1,8)=48951899)) " + 
			"    OR ('VSD'=:cardType AND SUBSTR(SO_THE,1,6)=453618) " + 
			")", nativeQuery = true)
	List<DoiSoatData> findAllTuNgayDenNgayAndPanAndApvCode(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("magd") Set<String> magd,@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("cardType") String cardType);
	
	@Query(value = "SELECT * FROM DSQT_DATA A\r\n" + 
			"WHERE NGAY_ADV=:ngayAdv\r\n" + 
			"AND ('All'=:lttqt OR LTTQT=:lttqt)\r\n" + 
			"AND ('All'=:loaiThe\r\n" + 
			"OR ('MD'=:loaiThe AND (SUBSTR(SO_THE,1,7) IN (5471390,5471391,5507960,5507961) OR SUBSTR(SO_THE,1,6)=524188))\r\n" + 
			"OR ('MC'=:loaiThe AND SUBSTR(SO_THE,1,6) IN (510235,512454,545579,554627))\r\n" + 
			"OR ('VS'=:loaiThe AND (SUBSTR(SO_THE,1,7) IN (4895160,4895170,4895180) OR SUBSTR(SO_THE,1,8)=48951899))\r\n" + 
			"OR ('VSD'=:loaiThe AND SUBSTR(SO_THE,1,6)=453618))", nativeQuery = true)
	List<DoiSoatData> findAllByLoaiTheAndLttqtAndNgayAdv(@Param("loaiThe") String loaiThe,@Param("lttqt") String lttqt, @Param("ngayAdv") String ngayAdv);
	
	@Query(value = "SELECT A.EMB_NAME,TRIM(PX_OA179_CB_ACCT_NUM) ACC_NUM,CRD_STAT,BRCH_CDE " + 
			"FROM  " + 
			"( " + 
			"    SELECT " + 
			"        trim(trim(FX_DW005_EMB_LST_NM) || ' ' || trim(FX_DW005_EMB_MID_NM)) || ' ' || trim(FX_DW005_EMB_NAME) AS EMB_NAME, " + 
			"        PX_DW005_PAN PAN,FX_DW005_CRD_STAT CRD_STAT,FX_DW005_BRCH_CDE BRCH_CDE " + 
			"    FROM " + 
			"        CCPS.DW005 " + 
			"    WHERE PX_DW005_PAN=:pan " + 
			"    UNION ALL " + 
			"    SELECT " + 
			"        trim(trim(FX_DW006_EMB_LST_NM) || ' ' || trim(FX_DW006_EMB_MID_NM)) || ' ' || trim(FX_DW006_EMB_NAME) AS EMB_NAME, " + 
			"        PX_DW006_OWN_PAN PAN,FX_DW006_CRD_STAT CRD_STAT,FX_DW006_BRCH_CDE BRCH_CDE " + 
			"    FROM " + 
			"        CCPS.DW006 " + 
			"    WHERE PX_DW006_OWN_PAN=:pan " + 
			") A LEFT JOIN OA179@AM B ON A.PAN=B.PX_OA179_PAN AND FX_OA179_DEF_ACCT_IND='Y' ", nativeQuery = true)
	List<Object[]> findDoiSoatInfoByCardno(@Param("pan") String pan);
	
	@Modifying
	@Query(value = "UPDATE DSQT_DATA SET PAN=ECD2@IM(SO_THE,'A') WHERE ID=:id", nativeQuery = true)
	void updatePANByID(@Param(value = "id") String id);
	
	
	@Modifying
	@Query(value = "MERGE INTO DSQT_DATA A " + 
			"USING OA179@AM B ON (PX_OA179_PAN=PAN AND FX_OA179_DEF_ACCT_IND='Y') " + 
			"when matched then UPDATE SET A.CASA=PX_OA179_CB_ACCT_NUM ", nativeQuery = true)
	void updateDoiSoatInfo();
	
	
			
	@Query(value = "SELECT ECD2@IM(:cardno,'A') FROM DUAL", nativeQuery = true)
	String convertPanByCardno(@Param("cardno") String cardno);
	
	@Query(value = "SELECT PX_IRC_OWN_PAN,FX_IRC_APV_CDE,P9_IRC_PST_DT, f9_irc_amt_lcl_crncy as so_tien_gd, " + 
			"  f9_irc_txn_amt as so_tien_gd_goc, F9_IRC_TXN_CRNCY_CDE as loai_tien_gd  " + 
			"FROM IRC04@IM " + 
			"WHERE PX_IRC_OWN_PAN=:pan AND FX_IRC_APV_CDE=:apvCode", nativeQuery = true)
	List<Object[]> getDwhByCardnoAndApvCode(@Param("pan") String pan,@Param("apvCode") String apvCode);
	
//	@Query(value = "SELECT * FROM (\r\n" + 
//			"    SELECT A.PAN,B.EMB_NAME,A.APV_CODE,A.DR_AMOUNT,A.DR_LCY_AMOUNT,A.CCY,A.ISA_FEE,CASE WHEN TRAN_TYPE_ID LIKE 'C%' THEN A.FEE ELSE 0 END RTM_FEE,A.BRANCH_CODE,A.AUTH_STATUS \r\n" + 
//			"    ,A.TRACE_NO,A.CASA_ACCOUNT_SEND,CURR_CODE,TRN_DT,CUST_ID,TRIM(CARD_TYPE_ID) CARD_TYPE\r\n" + 
//			"    FROM fpt.dwh_credit_card_tran A \r\n" + 
//			"    LEFT JOIN  \r\n" + 
//			"    ( \r\n" + 
//			"        SELECT   \r\n" + 
//			"            trim(trim(FX_DW005_EMB_LST_NM) || ' ' || trim(FX_DW005_EMB_MID_NM)) || ' ' || trim(FX_DW005_EMB_NAME) AS EMB_NAME,   \r\n" + 
//			"            PX_DW005_PAN PAN,FX_DW005_BRCH_CDE BRCH_CDE   \r\n" + 
//			"        FROM       \r\n" + 
//			"            CCPS.DW005 \r\n" + 
//			"        WHERE PX_DW005_PAN=:pan\r\n" + 
//			"        UNION ALL       \r\n" + 
//			"        SELECT       \r\n" + 
//			"            trim(trim(FX_DW006_EMB_LST_NM) || ' ' || trim(FX_DW006_EMB_MID_NM)) || ' ' || trim(FX_DW006_EMB_NAME) AS EMB_NAME,   \r\n" + 
//			"            PX_DW006_OWN_PAN PAN,FX_DW006_BRCH_CDE BRCH_CDE       \r\n" + 
//			"        FROM   \r\n" + 
//			"            CCPS.DW006   \r\n" + 
//			"        WHERE PX_DW006_OWN_PAN=:pan \r\n" + 
//			"    ) B ON A.PAN=B.PAN  \r\n" + 
//			"    LEFT JOIN DSQT_CURRENCY CURR ON A.CCY=TO_NUMBER(CURR_NUM) \r\n" + 
//			"    WHERE A.PAN=:pan AND A.APV_CODE=:apvCode \r\n" + 
//			"    ORDER BY TRN_DT DESC\r\n" + 
//			") WHERE ROWNUM=1", nativeQuery = true)
//	List<Object[]> findDoiSoatInfoByPanAndApvCode(@Param("pan") String pan,@Param("apvCode") String apvCode);
	
	@Query(value = "SELECT * FROM (   \r\n" + 
			"    SELECT B.PAN,B.EMB_NAME,A.APV_CODE,A.DR_AMOUNT,A.DR_LCY_AMOUNT,A.CCY,A.ISA_FEE,CASE WHEN TRAN_TYPE_ID LIKE 'C%' THEN A.FEE ELSE 0 END RTM_FEE,A.BRANCH_CODE,A.AUTH_STATUS    \r\n" + 
			"    ,A.TRACE_NO,A.CASA_ACCOUNT_SEND,CURR_CODE,TRN_DT,CUST_ID,TRIM(CARD_TYPE_ID) CARD_TYPE, AC_DESC    \r\n" + 
			"    FROM (  \r\n" + 
			"        WITH t AS\r\n" + 
			"        (\r\n" + 
			"            SELECT CASE WHEN (SELECT COUNT(*) FROM CCPS.DW012 WHERE PX_DW012_O_PAN=:pan)>0 \r\n" + 
			"            THEN (SELECT PX_DW012_N_PAN FROM CCPS.DW012 WHERE PX_DW012_O_PAN=:pan) ELSE :pan END PAN FROM DUAL\r\n" + 
			"        )\r\n" + 
			"        SELECT      \r\n" + 
			"            trim(trim(FX_DW005_EMB_LST_NM) || ' ' || trim(FX_DW005_EMB_MID_NM)) || ' ' || trim(FX_DW005_EMB_NAME) AS EMB_NAME,      \r\n" + 
			"            PX_DW005_PAN PAN,FX_DW005_BRCH_CDE BRCH_CDE  \r\n" + 
			"        FROM          \r\n" + 
			"            CCPS.DW005    \r\n" + 
			"        WHERE PX_DW005_PAN IN ((SELECT * FROM t),:pan)\r\n" + 
			"        UNION ALL          \r\n" + 
			"        SELECT          \r\n" + 
			"            trim(trim(FX_DW006_EMB_LST_NM) || ' ' || trim(FX_DW006_EMB_MID_NM)) || ' ' || trim(FX_DW006_EMB_NAME) AS EMB_NAME,      \r\n" + 
			"            PX_DW006_OWN_PAN PAN,FX_DW006_BRCH_CDE BRCH_CDE  \r\n" + 
			"        FROM      \r\n" + 
			"            CCPS.DW006   \r\n" + 
			"        WHERE PX_DW006_OWN_PAN IN ((SELECT * FROM t),:pan)\r\n" + 
			"    ) B  \r\n" + 
			"    LEFT JOIN fpt.dwh_credit_card_tran A ON A.PAN=B.PAN AND A.APV_CODE=TO_NUMBER(:apvCode) AND :apvCode<>'00000Y'  \r\n" + 
			"    LEFT JOIN DSQT_CURRENCY CURR ON A.CCY=TO_NUMBER(CURR_NUM)  \r\n" + 
			"    LEFT JOIN FCUSR01.STTM_CUST_ACCOUNT@EXADATA ON CUST_AC_NO=A.CASA_ACCOUNT_SEND  \r\n" + 
			"    ORDER BY ABS((TO_DATE(TRN_DT, 'DD-MON-RR')-TO_DATE(:ngayGd, 'YYMMDD'))) ASC  \r\n" + 
			") WHERE ROWNUM=1", nativeQuery = true)
	List<Object[]> findDoiSoatInfoByPanAndApvCode(@Param("pan") String pan,@Param("apvCode") String apvCode,@Param("ngayGd") String ngayGd);
	
	@Query(value = "SELECT SUM(ST_QD_VND) FROM DSQT_DATA " + 
			"WHERE NGAY_ADV=:ngayAdv " + 
			"AND LTTQT<>'VND' " +
			"AND ('All'= :cardType " + 
			"        OR ('MD'=:cardType AND (SUBSTR(SO_THE,1,7) IN (5471390,5471391,5507960,5507961) OR SUBSTR(SO_THE,1,6)=524188)) " + 
			"        OR ('MC'=:cardType AND SUBSTR(SO_THE,1,6) IN (510235,512454,545579,554627)) " + 
			"        OR ('VS'=:cardType AND (SUBSTR(SO_THE,1,7) IN (4895160,4895170,4895180) OR SUBSTR(SO_THE,1,8)=48951899)) " + 
			"        OR ('VSD'=:cardType AND SUBSTR(SO_THE,1,6)=453618)" + 
			"     )", nativeQuery = true)
	BigDecimal totalStQdVnd(@Param("ngayAdv") String ngayAdv,@Param("cardType") String cardType);
	
	@Query(value = "SELECT * FROM DSQT_DATA\r\n" + 
			"WHERE ('All'=:lttqt OR LTTQT=:lttqt) AND NGAY_ADV BETWEEN :tungay AND :denngay\r\n" + 
			"AND ('All'=:loaigd AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')\r\n" + 
			"OR (MA_GD LIKE '01%' AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR (MA_GD LIKE '01%' AND STATUS_CW<>' '))\r\n" + 
			"OR 'GDRTM'=:loaigd AND ((MA_GD LIKE '01%' AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR (MA_GD LIKE '01%' AND STATUS_CW<>' '))\r\n" + 
			"OR 'GDTTHH'=:loaigd AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')))\r\n" + 
			"AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0 AND NGAY_HOAN_TRA IS NULL", nativeQuery = true)
	List<DoiSoatData> findGDPhatSinhHoanTraLechTuNgayDenNgayAndLttqtAndLoaiGd(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("lttqt") String lttqt,@Param("loaigd") String loaigd);
	
	@Query(value = "SELECT * FROM DSQT_DATA\r\n" + 
			"WHERE ('All'=:lttqt OR LTTQT=:lttqt) AND NGAY_ADV BETWEEN :tungay AND :denngay\r\n" + 
			"AND ('All'=:loaigd AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')\r\n" + 
			"OR (MA_GD LIKE '01%' AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR (MA_GD LIKE '01%' AND STATUS_CW<>' '))\r\n" + 
			"OR 'GDRTM'=:loaigd AND ((MA_GD LIKE '01%' AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR (MA_GD LIKE '01%' AND STATUS_CW<>' '))\r\n" + 
			"OR 'GDTTHH'=:loaigd AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')))\r\n" + 
			"AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0 AND NGAY_HOAN_TRA IS NOT NULL", nativeQuery = true)
	List<DoiSoatData> findGDDaXuLyLechTuNgayDenNgayAndLttqtAndLoaiGd(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("lttqt") String lttqt,@Param("loaigd") String loaigd);
	
	@Modifying
	@Query(value = "  UPDATE DSQT_DATA\r\n" + 
			"  SET NGAY_HOAN_TRA=:ngayHoanTra\r\n" + 
			"  WHERE SO_THE=:soThe AND MA_CAP_PHEP=:maCapPhep AND NGAY_GD=:ngayGd AND TRACE=:trace", nativeQuery = true)
	void updateNgayHoanTra(@Param(value = "ngayHoanTra") String ngayHoanTra,@Param(value = "soThe") String soThe,@Param(value = "maCapPhep") String maCapPhep,@Param(value = "ngayGd") String ngayGd,@Param(value = "trace") String trace);
	
	
	@Query(value = "SELECT DISTINCT FILE_NAME " + 
			"FROM CCPS.INCOMING_MASTER " + 
			"WHERE REPLACE(SUBSTR(FILE_NAME, INSTR(FILE_NAME, 'TT112T0.')+8,10),'-','') BETWEEN :fromIncomingDate AND :toIncomingDate " + 
			"ORDER BY REPLACE(SUBSTR(FILE_NAME, INSTR(FILE_NAME, 'TT112T0.')+8,10),'-','') ASC", nativeQuery = true)
	List<String> findMCIncomingFileNameByDate(@Param("fromIncomingDate") String fromIncomingDate,@Param("toIncomingDate") String toIncomingDate);
	
	//1.1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - GD được thanh quyết toán
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' '))", nativeQuery = true)
	List<DoiSoatData> findAllGDTTHHDuocTQT(@Param("curr") String curr);
	
	//1.2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')) " + 
			"AND STGD_CHENH_LECH_DO_TY_GIA<>0 AND STGD_NGUYEN_TE_CHENH_LECH<>0", nativeQuery = true)
	List<DoiSoatData> findAllGDTTHHChiTietGDDuocTQT(@Param("curr") String curr);
	
	//1.3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')) " + 
			"AND STGD_CHENH_LECH_DO_TY_GIA>0 AND STGD_NGUYEN_TE_CHENH_LECH>0", nativeQuery = true)
	List<DoiSoatData> findAllGDTTHHTrichNoBoSungKH(@Param("curr") String curr);
	
	//1.4. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')) " + 
			"AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0", nativeQuery = true)
	List<DoiSoatData> findAllGDTTHHHoanTraTienTrichThua(@Param("curr") String curr);
		
	//2.1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - GD được thanh quyết toán
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND (((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' '))", nativeQuery = true)
	List<DoiSoatData> findAllGDRTMDuocTQT(@Param("curr") String curr);
	
	//2.2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND (((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' ')) " + 
			"AND STGD_CHENH_LECH_DO_TY_GIA<>0 AND STGD_NGUYEN_TE_CHENH_LECH<>0", nativeQuery = true)
	List<DoiSoatData> findAllGDRTMChiTietGDDuocTQT(@Param("curr") String curr);
	
	//2.3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND (((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' ')) " + 
			"AND STGD_CHENH_LECH_DO_TY_GIA>0 AND STGD_NGUYEN_TE_CHENH_LECH>0", nativeQuery = true)
	List<DoiSoatData> findAllGDRTMTrichNoBoSungKH(@Param("curr") String curr);
	
	//2.4. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) " + 
			"AND (((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND=' ') OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' ')) " + 
			"AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0", nativeQuery = true)
	List<DoiSoatData> findAllGDRTMHoanTraTienTrichThua(@Param("curr") String curr);
	
	//3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA)  " + 
			"AND MA_GD LIKE '28%' AND STATUS_CW=' '", nativeQuery = true)
	List<DoiSoatData> findAllGDMoneySendFF(@Param("curr") String curr);
	
	//4. MC/VS HOÀN TRẢ GD THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
	@Query(value = "SELECT CRE_TMS, UPD_TMS, USR_ID, UPD_UID, ID, TEN_CHU_THE, CASA, SO_THE, MA_GD, NGAY_GD, NGAY_FILE_INCOMING, \r\n" + 
			"ST_GD, ST_TQT, ST_QD_VND, LTGD, LTTQT, INTERCHANGE, MA_CAP_PHEP, DVCNT, REVERSAL_IND, ISSUER_CHARGE, \r\n" + 
			"MERCHANT_CITY, ST_TRICH_NO_KH_GD, STGD_NGUYEN_TE_GD, LOAI_TIEN_NGUYEN_TE_GD, PHI_ISA_GD, PHI_RTM_GD, \r\n" + 
			"STGD_NGUYEN_TE_CHENH_LECH, NGAY_ADV, PAN, STGD_CHENH_LECH_DO_TY_GIA, TY_GIA_TRICH_NO, SO_TIEN_GD_HOAN_TRA_TRUY_THU, \r\n" + 
			"PHI_ISA_HOAN_TRA_TRUY_THU, VAT_PHI_ISA_HOAN_TRA_TRUY_THU, PHI_RTM_HOAN_TRA_TRUY_THU, VAT_PHI_RTM_HOAN_TRA_TRUY_THU, \r\n" + 
			"TONG_PHI_VAT_HOAN_TRA_TRUY_THU, TONG_HOAN_TRA_TRUY_THU, PHI_XU_LY_GD, DVPHT, TRACE, STATUS_CW, MCC, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN '1P' WHEN ST_GD=STGD_NGUYEN_TE_GD THEN 'TP' END GHI_CHU,\r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN ST_QD_VND WHEN ST_GD=STGD_NGUYEN_TE_GD THEN ST_TRICH_NO_KH_GD ELSE 0 END ST_GD_HOAN_TRA, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN 0 WHEN ST_GD=STGD_NGUYEN_TE_GD THEN PHI_ISA_GD ELSE 0 END PHI_HOAN_TRA_KH, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN 0 WHEN ST_GD=STGD_NGUYEN_TE_GD THEN PHI_ISA_GD*0.1 ELSE 0 END THUE_HOAN_TRA_KH, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN 0 WHEN ST_GD=STGD_NGUYEN_TE_GD THEN PHI_ISA_GD*1.1 ELSE 0 END TONG_PHI_THUE_HOAN_TRA_KH, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN ST_QD_VND WHEN ST_GD=STGD_NGUYEN_TE_GD THEN ST_QD_VND+PHI_ISA_GD*1.1 ELSE 0 END TONG_HOAN_TRA_KH\r\n" + 
			"FROM DSQT_DATA  \r\n" + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA)   \r\n" + 
			"AND (MA_GD LIKE '20%' AND STATUS_CW=' ')  \r\n" + 
			"OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW=' ')", nativeQuery = true)
	List<DoiSoatData> findAllHoanTraGDTTHH(@Param("curr") String curr);
	
	//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
	@Query(value = "SELECT CRE_TMS, UPD_TMS, USR_ID, UPD_UID, ID, TEN_CHU_THE, CASA, SO_THE, MA_GD, NGAY_GD, NGAY_FILE_INCOMING, \r\n" + 
			"ST_GD, ST_TQT, ST_QD_VND, LTGD, LTTQT, INTERCHANGE, MA_CAP_PHEP, DVCNT, REVERSAL_IND, ISSUER_CHARGE, \r\n" + 
			"MERCHANT_CITY, ST_TRICH_NO_KH_GD, STGD_NGUYEN_TE_GD, LOAI_TIEN_NGUYEN_TE_GD, PHI_ISA_GD, PHI_RTM_GD, \r\n" + 
			"STGD_NGUYEN_TE_CHENH_LECH, NGAY_ADV, PAN, STGD_CHENH_LECH_DO_TY_GIA, TY_GIA_TRICH_NO, SO_TIEN_GD_HOAN_TRA_TRUY_THU, \r\n" + 
			"PHI_ISA_HOAN_TRA_TRUY_THU, VAT_PHI_ISA_HOAN_TRA_TRUY_THU, PHI_RTM_HOAN_TRA_TRUY_THU, VAT_PHI_RTM_HOAN_TRA_TRUY_THU, \r\n" + 
			"TONG_PHI_VAT_HOAN_TRA_TRUY_THU, TONG_HOAN_TRA_TRUY_THU, PHI_XU_LY_GD, DVPHT, TRACE, STATUS_CW, MCC, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN '1P' WHEN ST_GD=STGD_NGUYEN_TE_GD THEN 'TP' END GHI_CHU,\r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN ST_QD_VND WHEN ST_GD=STGD_NGUYEN_TE_GD THEN ST_TRICH_NO_KH_GD ELSE 0 END ST_GD_HOAN_TRA, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN 0 WHEN ST_GD=STGD_NGUYEN_TE_GD THEN PHI_RTM_GD ELSE 0 END PHI_HOAN_TRA_KH, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN 0 WHEN ST_GD=STGD_NGUYEN_TE_GD THEN PHI_RTM_GD*0.1 ELSE 0 END THUE_HOAN_TRA_KH, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN 0 WHEN ST_GD=STGD_NGUYEN_TE_GD THEN PHI_RTM_GD*1.1 ELSE 0 END TONG_PHI_THUE_HOAN_TRA_KH, \r\n" + 
			"CASE WHEN ST_GD<STGD_NGUYEN_TE_GD THEN ST_QD_VND WHEN ST_GD=STGD_NGUYEN_TE_GD THEN ST_QD_VND+PHI_RTM_GD*1.1 ELSE 0 END TONG_HOAN_TRA_KH\r\n" + 
			"FROM DSQT_DATA  \r\n" + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA)  \r\n" + 
			"AND MA_GD LIKE '01%' AND REVERSAL_IND='R' AND STATUS_CW=' '", nativeQuery = true)
	List<DoiSoatData> findAllHoanTraGDRTM(@Param("curr") String curr);
	
	//6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) "  + 
			"AND MA_GD LIKE '29%'", nativeQuery = true)
	List<DoiSoatData> findAllGDBaoCoBatThuong(@Param("curr") String curr);
	
	//7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE LTTQT=:curr AND NGAY_ADV = (SELECT MAX(NGAY_ADV) FROM DSQT_DATA) "  + 
			"AND MA_GD LIKE '19%'", nativeQuery = true)
	List<DoiSoatData> findAllGDBaoNoBatThuong(@Param("curr") String curr);
	
}
