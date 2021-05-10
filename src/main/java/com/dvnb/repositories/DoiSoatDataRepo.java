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

	@Query(value = "SELECT to_char(SYSDATE, 'yyyyMMddHH24MISS') CRE_TMS,NULL UPD_TMS,NULL USR_ID,NULL UPD_UID,to_char(SYSDATE,'yyyyMMddHH24MISS')||LPAD(ROWNUM,7,0) ID,NULL NGAY_ADV,NULL TEN_CHU_THE,NULL CASA, PAN SO_THE,ECD2@IM(PAN,'A') PAN,\r\n" + 
			"PROCESSING_CODE MA_GD,SUBSTR(DATES,0,6) NGAY_GD,SUBSTR(FILE_NAME, INSTR(FILE_NAME,'TT112T0.')+8,19)  NGAY_FILE_INCOMING,AMOUNT_TRANSACTION ST_GD,\r\n" + 
			"AMOUNT_RECONCILIATION ST_TQT,NVL(AMOUNT_CARDHOLDER_BILLING,0) ST_QD_VND,CURRENT_CODE_TRANSACTION LTGD,\r\n" + 
			"(SELECT CURR_CODE FROM DSQT_CURRENCY WHERE CURR_NUM=CURRENT_CODE_RECONCILIATION) LTTQT,NVL(AMT_FEE_RECONCILIATION,0) INTERCHANGE,\r\n" + 
			"APPROVAL_CODE MA_CAP_PHEP,MERCHANT DVCNT,CASE WHEN ADITIONAL_DATA LIKE '%25007R%' THEN 'R' ELSE ' ' END REVERSAL_IND,\r\n" + 
			"NULL ISSUER_CHARGE,NULL MERCHANT_CITY,0 ST_TRICH_NO_KH_GD,0 STGD_NGUYEN_TE_GD,0 LOAI_TIEN_NGUYEN_TE_GD,0 PHI_ISA_GD,0 PHI_RTM_GD,0 STGD_NGUYEN_TE_CHENH_LECH, \r\n" + 
			"0 STGD_CHENH_LECH_DO_TY_GIA,0 TY_GIA_TRICH_NO,0 SO_TIEN_GD_HOAN_TRA_TRUY_THU,0 PHI_ISA_HOAN_TRA_TRUY_THU,0 VAT_PHI_ISA_HOAN_TRA_TRUY_THU,\r\n" + 
			"0 PHI_RTM_HOAN_TRA_TRUY_THU,0 VAT_PHI_RTM_HOAN_TRA_TRUY_THU,0 TONG_PHI_VAT_HOAN_TRA_TRUY_THU,0 TONG_HOAN_TRA_TRUY_THU,0 PHI_XU_LY_GD, \r\n" + 
			"' 'DVPHT,' ' TRACE, ' ' STATUS_CW, CARD_ACCEPTOR_BUSINESS_CODE MCC, ' ' CIF, ' ' CRD_PGM, ' ' NGAY_HOAN_TRA, ' ' TEN_CHU_TK,0 VAT_PHI_ISA_GD,0 VAT_PHI_RTM_GD,' ' LOC, ' ' CARD_BRN\r\n" + 
			"FROM CCPS.INCOMING_MASTER A\r\n" + 
			"WHERE FILE_NAME IN :incomingFileName", nativeQuery = true)
	List<DoiSoatData> findAllMasterByFileIncomName(@Param("incomingFileName") Set<String> incomingFileName);
	
	@Query(value = "SELECT to_char(SYSDATE, 'yyyyMMddHH24MISS') CRE_TMS, NULL UPD_TMS, NULL USR_ID, NULL UPD_UID,\r\n" + 
			"to_char(SYSDATE,'yyyyMMddHH24MISS')||LPAD(ROWNUM,7,0) ID,NULL NGAY_ADV,NULL TEN_CHU_THE,NULL CASA, \r\n" + 
			"NVL(REPLACE(PX_IRPANMAP_PANMASK,'XXXXXX',PX_IRPANMAP_MID),ACC_NUMBER) SO_THE,NVL(PX_IRPANMAP_PAN,ECD2@IM(ACC_NUMBER,'A')) PAN, TXN_CODE MA_GD,\r\n" + 
			"SUBSTR(TXN_DT,3,8) NGAY_GD,to_char(to_date(PST_DT,'YYYYMMDD'), 'YYYY-MM-DD')||'-00-00-00' NGAY_FILE_INCOMING,\r\n" + 
			"NVL(SOURCE_AMT,0) ST_GD, 0 ST_TQT,NVL(DES_AMT,0) ST_QD_VND,SOURCE_CUR_CODE LTGD, SETTLEMENT_FLAG LTTQT,\r\n" + 
			"NVL(INTERCHANGE_FEE_AMT,0) INTERCHANGE, AUTH_CODE MA_CAP_PHEP,MERC_NAME DVCNT,' ' REVERSAL_IND, \r\n" + 
			"ISSUER_CHARGE,MERC_CITY MERCHANT_CITY,0 ST_TRICH_NO_KH_GD,0 STGD_NGUYEN_TE_GD,0 LOAI_TIEN_NGUYEN_TE_GD,0 PHI_ISA_GD,0 PHI_RTM_GD,0 STGD_NGUYEN_TE_CHENH_LECH,  \r\n" + 
			"0 STGD_CHENH_LECH_DO_TY_GIA,0 TY_GIA_TRICH_NO,0 SO_TIEN_GD_HOAN_TRA_TRUY_THU,0 PHI_ISA_HOAN_TRA_TRUY_THU,0 VAT_PHI_ISA_HOAN_TRA_TRUY_THU, \r\n" + 
			"0 PHI_RTM_HOAN_TRA_TRUY_THU,0 VAT_PHI_RTM_HOAN_TRA_TRUY_THU,0 TONG_PHI_VAT_HOAN_TRA_TRUY_THU,0 TONG_HOAN_TRA_TRUY_THU,0 PHI_XU_LY_GD,  \r\n" + 
			"' ' DVPHT,' ' TRACE, ' ' STATUS_CW, MCC, ' ' CIF, CARD_PRG CRD_PGM, NULL NGAY_HOAN_TRA, ' ' TEN_CHU_TK,0 VAT_PHI_ISA_GD,0 VAT_PHI_RTM_GD, LOC, ' ' CARD_BRN \r\n" + 
			"FROM CCPS.INCOMING_VISA_VIEW A \r\n" + 
			"LEFT JOIN IR_PAN_MAP@IM B ON A.ACC_NUMBER = B.PX_IRPANMAP_PANMASK AND A.LOC = B.F9_IRPANMAP_LOC\r\n" + 
			"WHERE PST_DT IN :incomingFileName", nativeQuery = true)
	List<DoiSoatData> findAllVisaByFileIncomName(@Param("incomingFileName") Set<String> incomingFileName);
	
	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE ST_TRICH_NO_KH_GD <> 0 AND SO_THE=:cardno AND MA_CAP_PHEP=:apvcode AND MA_GD=:maGd AND REVERSAL_IND=:reversalInd " + 
			"AND NGAY_ADV=:ngayadv AND ROWNUM=1", nativeQuery = true)
	DoiSoatData findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("maGd") String maGd,@Param("reversalInd") String reversalInd,@Param("ngayadv") String ngayadv);
	
	Page<DoiSoatData> findAll(Pageable page);
	
	@Modifying
	@Query(value = "DELETE DSQT_DATA\r\n" + 
			"WHERE NGAY_ADV=:ngayAdv\r\n" + 
			"AND ('All'=:lttqt OR LTTQT=:lttqt)\r\n" + 
			"AND ('All'=:cardbrn OR CARD_BRN=:cardbrn)", nativeQuery = true)
	int deleteAllByNgayAdvAndLttqtAndCardbrn(@Param("ngayAdv") String ngayAdv,@Param("lttqt") String lttqt,@Param("cardbrn") String cardbrn);
	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay", nativeQuery = true)
	List<DoiSoatData> findAllTuNgayDenNgay(@Param("tungay") String tungay,@Param("denngay") String denngay);
	
	@Query(value = "SELECT * FROM DSQT_DATA  \r\n" + 
			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay  \r\n" + 
			"AND ('All'=:cardno OR SO_THE=:cardno)  \r\n" + 
			"AND ('All'=:apvcode OR MA_CAP_PHEP=:apvcode)  \r\n" + 
			"AND ('All'=:dvcnt OR DVCNT LIKE '%' || :dvcnt || '%')  \r\n" + 
			"AND ('All'=:cardType OR CARD_BRN=:cardType)", nativeQuery = true)
	List<DoiSoatData> findAllTuNgayDenNgayAndPanAndApvCodeAndDvcntAndCardtype(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("dvcnt") String dvcnt,@Param("cardType") String cardType);
	
	@Query(value = "SELECT * FROM DSQT_DATA " + 
			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay " + 
			"AND ('All'=:cardno OR SO_THE=:cardno) " + 
			"AND ('All'=:apvcode OR MA_CAP_PHEP=:apvcode) " +
			"AND ('999' IN :magd OR SUBSTR(MA_GD,0,2) IN :magd) " + 
			"AND ('All'=:cardType OR CARD_BRN=:cardType)", nativeQuery = true)
	List<DoiSoatData> findAllTuNgayDenNgayAndMagdAndCardnoAndApvCodeAndCardtype(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("magd") Set<String> magd,@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("cardType") String cardType);
	
//	@Query(value = "SELECT * FROM DSQT_DATA " + 
//			"WHERE NGAY_ADV BETWEEN :tungay AND :denngay " + 
//			"AND ('All'=:cardno OR SO_THE=:cardno) " + 
//			"AND ('All'=:apvcode OR MA_CAP_PHEP=:apvcode) " +
//			"AND ('999' IN :magd OR SUBSTR(MA_GD,0,2) IN :magd) " + 
//			"AND ('All'=:cardType " + 
//			"    OR ('MD'=:cardType AND (SUBSTR(SO_THE,1,7) IN (5471390,5471391,5507960,5507961) OR SUBSTR(SO_THE,1,6)=524188)) " + 
//			"    OR ('MC'=:cardType AND SUBSTR(SO_THE,1,6) IN (510235,512454,545579,554627)) " + 
//			"    OR ('VS'=:cardType AND (SUBSTR(SO_THE,1,7) IN (4895160,4895170,4895180) OR SUBSTR(SO_THE,1,8)=48951899)) " + 
//			"    OR ('VSD'=:cardType AND SUBSTR(SO_THE,1,6)=453618) " + 
//			")", nativeQuery = true)
//	List<DoiSoatData> findAllTuNgayDenNgayAndMagdAndCardnoAndApvCodeAndCardtype(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("cardno") String cardno,@Param("apvcode") String apvcode,@Param("cardType") String cardType);
	
	@Query(value = "SELECT * FROM DSQT_DATA A \r\n" + 
			"WHERE NGAY_ADV=:ngayAdv \r\n" + 
			"AND ('All'=:lttqt OR LTTQT=:lttqt) \r\n" + 
			"AND ('All'=:loaiThe OR CARD_BRN=:loaiThe)", nativeQuery = true)
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
			"    ,A.TRACE_NO,A.CASA_ACCOUNT_SEND,CURR_CODE,TRN_DT,CUST_ID,TRIM(CARD_TYPE_ID) CARD_TYPE, ' ' AC_DESC    \r\n" + 
			"	 ,A.ISA_VAT,CASE WHEN TRAN_TYPE_ID LIKE 'C%' THEN A.VAT ELSE 0 END RTM_VAT,CR_AMOUNT,CR_LCY_AMOUNT\r\n" + 
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
			"    LEFT JOIN fpt.dwh_credit_card_tran A ON A.PAN=B.PAN AND LPAD(A.APV_CODE,6,0)=LPAD(:apvCode,6,0) AND REGEXP_LIKE(:apvCode,'^[0-9]+$')  \r\n" + 
			"    AND DR_AMOUNT<>0 AND DR_LCY_AMOUNT<>0\r\n" +
			"    LEFT JOIN DSQT_CURRENCY CURR ON A.CCY=TO_NUMBER(CURR_NUM)  \r\n" + 
//			"    LEFT JOIN FCUSR01.STTM_CUST_ACCOUNT@EXADATA ON CUST_AC_NO=A.CASA_ACCOUNT_SEND  \r\n" + 
			"    ORDER BY ABS((TO_DATE(TRN_DT, 'DD-MON-RR')-TO_DATE(:ngayGd, 'YYMMDD'))) ASC  \r\n" + 
			") WHERE ROWNUM=1", nativeQuery = true)
	List<Object[]> findDoiSoatInfoByPanAndApvCode(@Param("pan") String pan,@Param("apvCode") String apvCode,@Param("ngayGd") String ngayGd);
	
	@Query(value = "SELECT B.PAN,B.EMB_NAME,A.FX_DW009_APV_CDE,A.F9_DW009_TXN_AMT,A.F9_DW009_AMT_LCL_CRNCY,A.F9_DW009_TXN_CRNCY_CDE, \r\n" + 
			"ROUND(F9_DW009_COMM_CHRG*10/11,0) ISA_FEE,ROUND(F9_DW009_COMM_CHRG/11,0) VAT_ISA_FEE,F9_DW009_TXN_DT,FX_DW009_CRD_PGM CARD_TYPE,TRIM(B.BRCH_CDE)||'-'||TRIM(C.BRANCH_NAME) BRCH,LOC      \r\n" + 
			"FROM (    \r\n" + 
			"    SELECT        \r\n" + 
			"        trim(trim(FX_DW005_EMB_LST_NM) || ' ' || trim(FX_DW005_EMB_MID_NM)) || ' ' || trim(FX_DW005_EMB_NAME) AS EMB_NAME,        \r\n" + 
			"        PX_DW005_PAN PAN,FX_DW005_BRCH_CDE BRCH_CDE,F9_DW005_LOC_ACCT LOC    \r\n" + 
			"    FROM            \r\n" + 
			"        CCPS.DW005      \r\n" + 
			"    WHERE PX_DW005_PAN=:pan\r\n" + 
			"    UNION ALL            \r\n" + 
			"    SELECT            \r\n" + 
			"        trim(trim(FX_DW006_EMB_LST_NM) || ' ' || trim(FX_DW006_EMB_MID_NM)) || ' ' || trim(FX_DW006_EMB_NAME) AS EMB_NAME,        \r\n" + 
			"        PX_DW006_OWN_PAN PAN,FX_DW006_BRCH_CDE BRCH_CDE,F9_DW006_LOC_ACCT LOC    \r\n" + 
			"    FROM        \r\n" + 
			"        CCPS.DW006     \r\n" + 
			"    WHERE PX_DW006_OWN_PAN=:pan\r\n" + 
			") B    \r\n" + 
			"LEFT JOIN CCPS.DW009 A ON A.PX_DW009_OWN_PAN=B.PAN AND LPAD(A.FX_DW009_APV_CDE,6,0)=LPAD(:apvCode,6,0) AND REGEXP_LIKE(:apvCode,'^[0-9]+$') AND F9_DW009_SETL_AMT>0 \r\n" + 
			"LEFT JOIN FPT.FULL_BRANCH C ON TRIM(B.BRCH_CDE)=TRIM(C.BRANCH_CODE)", nativeQuery = true)
	List<Object[]> findDoiSoatCreditInfoByPanAndApvCode(@Param("pan") String pan,@Param("apvCode") String apvCode);
	
	
	@Query(value = "SELECT SUM(ST_QD_VND) FROM DSQT_DATA  \r\n" + 
			"WHERE NGAY_ADV=:ngayAdv  \r\n" + 
			"AND LTTQT<>'VND' \r\n" + 
			"AND ('All'= :cardType OR CARD_BRN=:cardType)", nativeQuery = true)
	BigDecimal totalStQdVnd(@Param("ngayAdv") String ngayAdv,@Param("cardType") String cardType);
	
	@Query(value = "SELECT *\r\n" + 
			"FROM DSQT_DATA \r\n" + 
			"WHERE ('All'=:lttqt OR LTTQT=:lttqt) AND CARD_BRN=:cardType AND NGAY_ADV BETWEEN :tungay AND :denngay\r\n" + 
			"AND ('GDRTM'=:loaigd AND (\r\n" + 
			"    :cardType='MD' AND (((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND<>'R')  \r\n" + 
			"                    OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' '))\r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='VSD' AND ((MA_GD LIKE '07%') OR (MA_GD LIKE '27%' AND STATUS_CW<>' ')) \r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='MC' AND LTTQT='USD' AND MA_GD LIKE '01%' AND REVERSAL_IND='R'\r\n" + 
			"    OR :cardType='VS' AND LTTQT='USD' AND MA_GD LIKE '27%')\r\n" + 
			"    \r\n" + 
			"OR 'GDTTHH'=:loaigd AND (\r\n" + 
			"    :cardType='MD' AND ((MA_GD LIKE '00%' AND STATUS_CW=' ')  \r\n" + 
			"    OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ') )\r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='VSD' AND (((MA_GD LIKE '05%' AND STATUS_CW=' ') OR (MA_GD LIKE '25%' AND STATUS_CW<>' ') OR MA_GD LIKE '26%')) \r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='MC' AND LTTQT='USD' AND (MA_GD LIKE '20%' OR (MA_GD LIKE '00%' AND REVERSAL_IND='R'))\r\n" + 
			"    OR :cardType='VS' AND LTTQT='USD' AND (MA_GD LIKE '25%' OR (MA_GD LIKE '06%' AND MERCHANT_CITY NOT LIKE '%Visa Direct%'))\r\n" + 
			"))\r\n" + 
			"AND NGAY_HOAN_TRA IS NULL", nativeQuery = true)
	List<DoiSoatData> findGDPhatSinhHoanTraLechTuNgayDenNgayAndLttqtAndLoaiGdAndCardtype(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("lttqt") String lttqt,@Param("loaigd") String loaigd,@Param("cardType") String cardType);
	
	@Query(value = "SELECT *\r\n" + 
			"FROM DSQT_DATA \r\n" + 
			"WHERE ('All'=:lttqt OR LTTQT=:lttqt) AND CARD_BRN=:cardType AND NGAY_ADV BETWEEN :tungay AND :denngay\r\n" + 
			"AND ('GDRTM'=:loaigd AND (\r\n" + 
			"    :cardType='MD' AND (((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND<>'R')  \r\n" + 
			"                    OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' '))\r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='VSD' AND ((MA_GD LIKE '07%') OR (MA_GD LIKE '27%' AND STATUS_CW<>' ')) \r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='MC' AND LTTQT='USD' AND MA_GD LIKE '01%' AND REVERSAL_IND='R'\r\n" + 
			"    OR :cardType='VS' AND LTTQT='USD' AND MA_GD LIKE '27%')\r\n" + 
			"    \r\n" + 
			"OR 'GDTTHH'=:loaigd AND (\r\n" + 
			"    :cardType='MD' AND ((MA_GD LIKE '00%' AND STATUS_CW=' ')  \r\n" + 
			"    OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ') )\r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='VSD' AND (((MA_GD LIKE '05%' AND STATUS_CW=' ') OR (MA_GD LIKE '25%' AND STATUS_CW<>' ') OR MA_GD LIKE '26%')) \r\n" + 
			"                    AND STGD_CHENH_LECH_DO_TY_GIA<0 AND STGD_NGUYEN_TE_CHENH_LECH<0\r\n" + 
			"    OR :cardType='MC' AND LTTQT='USD' AND (MA_GD LIKE '20%' OR (MA_GD LIKE '00%' AND REVERSAL_IND='R'))\r\n" + 
			"    OR :cardType='VS' AND LTTQT='USD' AND (MA_GD LIKE '25%' OR (MA_GD LIKE '06%' AND MERCHANT_CITY NOT LIKE '%Visa Direct%'))\r\n" + 
			"))\r\n" + 
			"AND NGAY_HOAN_TRA IS NOT NULL", nativeQuery = true)
	List<DoiSoatData> findGDDaXuLyLechTuNgayDenNgayAndLttqtAndLoaiGd(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("lttqt") String lttqt,@Param("loaigd") String loaigd,@Param("cardType") String cardType);
	
	@Modifying
	@Query(value = "  UPDATE DSQT_DATA\r\n" + 
			"  SET NGAY_HOAN_TRA=:ngayHoanTra\r\n" + 
			"  WHERE SO_THE=:soThe AND MA_CAP_PHEP=:maCapPhep AND NGAY_GD=:ngayGd AND TRACE=:trace", nativeQuery = true)
	void updateNgayHoanTra(@Param(value = "ngayHoanTra") String ngayHoanTra,@Param(value = "soThe") String soThe,@Param(value = "maCapPhep") String maCapPhep,@Param(value = "ngayGd") String ngayGd,@Param(value = "trace") String trace);
	
	@Modifying
	@Query(value = "  UPDATE DSQT_DATA\r\n" + 
			"  SET NGAY_HOAN_TRA=:ngayHoanTra\r\n" + 
			"  WHERE ID=:id", nativeQuery = true)
	void updateNgayHoanTraById(@Param(value = "ngayHoanTra") String ngayHoanTra,@Param(value = "id") String id);
	
	
	@Query(value = "SELECT DISTINCT FILE_NAME " + 
			"FROM CCPS.INCOMING_MASTER " + 
			"WHERE REPLACE(SUBSTR(FILE_NAME, INSTR(FILE_NAME, 'TT112T0.')+8,10),'-','') BETWEEN :fromIncomingDate AND :toIncomingDate " + 
			"ORDER BY REPLACE(SUBSTR(FILE_NAME, INSTR(FILE_NAME, 'TT112T0.')+8,10),'-','') ASC", nativeQuery = true)
	List<String> findMCIncomingFileNameByDate(@Param("fromIncomingDate") String fromIncomingDate,@Param("toIncomingDate") String toIncomingDate);
	
	@Query(value = "SELECT DISTINCT PST_DT\r\n" + 
			"FROM CCPS.INCOMING_VISA_VIEW  \r\n" + 
			"WHERE PST_DT BETWEEN :fromIncomingDate AND :toIncomingDate \r\n" + 
			"ORDER BY PST_DT ASC", nativeQuery = true)
	List<String> findVSIncomingFileNameByDate(@Param("fromIncomingDate") String fromIncomingDate,@Param("toIncomingDate") String toIncomingDate);
	
	@Query(value = "SELECT LISTAGG(F9_IR121_PRFX, '|')\r\n" + 
			"         WITHIN GROUP (ORDER BY F9_IR121_PRFX) \"BIN_LIST\"\r\n" + 
			"FROM (SELECT DISTINCT F9_IR121_PRFX FROM IR121@IM\r\n" + 
			"    WHERE FX_IR121_CRD_BRN=:crdBrn\r\n" + 
			"    AND F9_IR121_ACCT_PRV=:acctPrv)", nativeQuery = true)
	String findBinByCardtype(@Param("crdBrn") String crdBrn, @Param("acctPrv") int acctPrv);
	
	@Query(value = "SELECT TRIM(AC_DESC) FROM FCUSR01.STTM_CUST_ACCOUNT@EXADATA WHERE CUST_AC_NO=:casa", nativeQuery = true)
	String findAccountDescription(@Param("casa") String casa);
	
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
