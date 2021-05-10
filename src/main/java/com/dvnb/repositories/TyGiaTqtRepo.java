package com.dvnb.repositories;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.TyGiaTqt;

@Repository
public interface TyGiaTqtRepo extends JpaRepository<TyGiaTqt, String> {
	
	@Query(value = "SELECT * FROM DSQT_TY_GIA_TQT\r\n" + 
			"WHERE NGAY_ADV=:ngayAdv", nativeQuery = true)
	TyGiaTqt findTyGiaTqtByNgayAdv(@Param("ngayAdv") String ngayAdv);
	
//	@Query(value = "SELECT * FROM DSQT_TY_GIA_TQT\r\n" + 
//			"WHERE NGAY_ADV=:ngayAdv", nativeQuery = true)
	TyGiaTqt findAllByNgayAdvAndCardType(@Param("ngayAdv") String ngayAdv,@Param("cardType") String cardType);

	@Query(value = "WITH t  \r\n" + 
			"AS  \r\n" + 
			"(  \r\n" + 
			"    SELECT NGAY_ADV,CARD_BRN,ST_QD_VND,ST_TQT, \r\n" + 
			"                CASE WHEN (MA_GD LIKE '00%' AND REVERSAL_IND<>'R' AND STATUS_CW=' ')   \r\n" + 
			"                    OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' ')  \r\n" + 
			"                    OR (MA_GD LIKE '00%' AND REVERSAL_IND<>'R' AND STATUS_CW<>' ')  \r\n" + 
			"                    OR (MA_GD LIKE '18%' AND STATUS_CW=' ')   \r\n" + 
			"                    OR (MA_GD LIKE '20%' AND STATUS_CW='C')  \r\n" + 
			"                    OR (:cardtype='MC' AND (MA_GD LIKE '20%' OR MA_GD LIKE '00%' OR MA_GD LIKE '18%')) THEN 'TTHH'   \r\n" + 
			"                WHEN ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW=' ' AND REVERSAL_IND<>'R')   \r\n" + 
			"                    OR ((MA_GD LIKE '01%' OR MA_GD LIKE '12%') AND STATUS_CW<>' ')  \r\n" + 
			"                    OR (:cardtype='MC' AND MA_GD LIKE '01%') THEN 'RTM'  \r\n" + 
			"                WHEN MA_GD LIKE '28%' AND STATUS_CW=' ' \r\n" + 
			"                    OR (:cardtype='MC' AND MA_GD LIKE '28%') THEN 'MSFF'  \r\n" + 
			"                WHEN :cardtype='MD' AND ((MA_GD LIKE '20%' AND STATUS_CW=' ')    \r\n" + 
			"                    OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW=' ')) THEN 'HTTTHH'  \r\n" + 
			"                WHEN :cardtype='MD' AND MA_GD LIKE '01%' AND REVERSAL_IND='R' AND STATUS_CW=' ' THEN 'HTRTM'  \r\n" + 
			"        END LOAI_GD  \r\n" + 
			"    FROM DSQT_DATA A  \r\n" + 
			"    WHERE LTTQT='USD' AND A.NGAY_ADV=:ngayAdv AND CARD_BRN=:cardtype\r\n" + 
			")  \r\n" + 
			"SELECT '' CRE_TMS, '' UPD_TMS, '' USR_ID, '' UPD_UID,MAX(NGAY_ADV) NGAY_ADV, 0 ST_QD_VND, 0 ST_GD_USD, 0 TY_GIA_TQT,MAX(CARD_BRN) CARD_TYPE, 0 TY_GIA_PXL,   \r\n" + 
			"NVL(SUM(DECODE(LOAI_GD, 'TTHH', TY_GIA)),0) TY_GIA_GD_TTHH, NVL(SUM(DECODE(LOAI_GD, 'RTM', TY_GIA)),0) TY_GIA_GD_RTM,   \r\n" + 
			"NVL(SUM(DECODE(LOAI_GD, 'MSFF', TY_GIA)),0) TY_GIA_GD_MSFF, NVL(SUM(DECODE(LOAI_GD, 'HTTTHH', TY_GIA)),0) TY_GIA_GD_HT_TTHH,   \r\n" + 
			"NVL(SUM(DECODE(LOAI_GD, 'HTRTM', TY_GIA)),0) TY_GIA_GD_HT_RTM, 0 TY_GIA_PXL_TTHH, 0 TY_GIA_PXL_RTM, 0 TY_GIA_PXL_MSFF,   \r\n" + 
			"0 TY_GIA_PXL_HT_TTHH, 0 TY_GIA_PXL_HT_RTM, MAX(NGAY_ADV)||MAX(CARD_BRN) ID  \r\n" + 
			"FROM  \r\n" + 
			"(SELECT NGAY_ADV,CARD_BRN,LOAI_GD,SUM(ST_QD_VND)/SUM(ST_TQT) TY_GIA  \r\n" + 
			"FROM t  \r\n" + 
			"GROUP BY NGAY_ADV,CARD_BRN,LOAI_GD)", nativeQuery = true)
	TyGiaTqt calcTyGiaMastercardByNgayAdvAndCardType(@Param("ngayAdv") String ngayAdv,@Param("cardtype") String cardtype);
	
}
