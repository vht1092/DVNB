package com.dvnb.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080103;

@Repository
public interface Act2080103Repo extends JpaRepository<Act2080103, String> {
	
	Page<Act2080103> findAll(Pageable page);
	
	Page<Act2080103> findAllByKy(@Param("ky") String ky, Pageable page);

	@Modifying
	@Query(value = "INSERT INTO FPT.DVNB_ACT_2080103(CRE_TMS, USR_ID, UPD_TMS, UPD_UID, ID_NO, MA_DVNB, KY, CASE_NO, THOI_GIAN, CARD_NO, LOC, CIF, MA_SAN_PHAM, MA_LOAI_KHACH_HANG, MA_DON_VI) " + 
			"SELECT (select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) cre_tms, :usrid usr_id, null upd_tms, null upd_uid, " + 
			"    (select to_char(SYSDATE, 'yyyyMMddHH24MISS') from dual) || REPLACE(TO_CHAR(ROWNUM,'9999999'),' ',0) id_no, " + 
			"    'ACT_2080103' ma_dvnb, :kybaocao ky, CASE_NO, CRE_TMS thoi_gian, CARD_NO, LOC, CIF_NO cif, " + 
			"    CASE WHEN LOC<800000000000 AND CRD_BRN='VS' THEN 'PRO_307'  " + 
			"        WHEN LOC<800000000000 AND CRD_BRN='MC' THEN 'PRO_234' " + 
			"        WHEN LOC>=800000000000 AND CRD_BRN='VS' THEN 'PRO_222' " + 
			"        WHEN LOC>=800000000000 AND CRD_BRN='MC' THEN 'PRO_228' END MA_SAN_PHAM, " + 
			"    CASE WHEN f9_ir121_prfx in ('512454','5471390', '5471391') THEN 'CUS_03' ELSE 'CUS_01' END MA_LOAI_KHACH_HANG, " + 
			"    'ENT_' || BRCH_CDE ma_don_vi " + 
			"FROM ( " + 
			"SELECT CASE_NO, CRE_TMS ,CARD_NO , F9_DW005_LOC_ACCT || F9_DW006_LOC_ACCT LOC, CIF_NO , FX_DW005_BRCH_CDE || FX_DW006_BRCH_CDE BRCH_CDE,CRD_BRN,CRD_PGM " + 
			"FROM ( " + 
			"SELECT C.CRE_TMS,C.CASE_NO,SUBSTR(DED2@IM(ENC_CRD_NO,'FDS'),1,6) || 'XXXXXX' || SUBSTR(DED2@IM(ENC_CRD_NO,'FDS'),13,16) CARD_NO " + 
			", F9_DW005_LOC_ACCT, F9_DW006_LOC_ACCT,C.CIF_NO, (SELECT LISTAGG(RULE_ID, ',') WITHIN GROUP (ORDER BY CASE_NO) FROM (SELECT * FROM CCPS.fds_case_hit_rules UNION ALL SELECT * FROM CCPS.fds_case_hit_rules_hist) WHERE CASE_NO = B.CASE_NO) RULE " + 
			",FX_DW005_BRCH_CDE,FX_DW006_BRCH_CDE, D.FX_IR124_BRCH_NAME,F.FX_IR124_BRCH_NAME FX_IR124_BRCH_NAME_SUPP,CRD_BRN,FX_DW005_CRD_PGM||FX_DW006_CRD_PGM CRD_PGM " + 
			"FROM CCPS.FDS_DESCRIPTION A " + 
			"INNER JOIN (SELECT * FROM CCPS.FDS_CASE_STATUS UNION ALL SELECT * FROM CCPS.FDS_CASE_STATUS_HIST) B ON B.CLOSED_REASON = A.TYPE AND B.OTHER LIKE '%'|| A.ID ||'%' " + 
			"INNER JOIN (SELECT * FROM CCPS.FDS_CASE_DETAIL UNION ALL SELECT * FROM CCPS.FDS_CASE_DETAIL_HIST) C ON B.CASE_NO = C.CASE_NO " + 
//			"INNER JOIN ( " + 
//			"    select min(r.rule_priority) keep(dense_rank first order by r.rule_priority) rule_priority,t.case_no " + 
//			"    from (SELECT * FROM CCPS.fds_case_hit_rules UNION ALL SELECT * FROM CCPS.fds_case_hit_rules_hist) t  " + 
//			"    join CCPS.fds_rules r on t.rule_id=r.rule_id  " + 
//			"    GROUP BY t.case_no " + 
//			"    order by t.case_no asc " + 
//			") C ON C.CASE_NO=B.CASE_NO " + 
//			"INNER JOIN CCPS.fds_rules D ON C.rule_priority=D.rule_priority AND D.RULE_ID IN ('RULE13','RULE14','RULE15','RULE16','RULE34','RULE35','RULE20','RULE19','RULE36','RULE37','RULE38','RULE42') " + 
			"LEFT JOIN CCPS.DW005 C ON PX_DW005_PAN = ENC_CRD_NO " + 
			"LEFT JOIN CCPS.DW006 E ON PX_DW006_OWN_PAN = ENC_CRD_NO " + 
			"LEFT JOIN IR124@IM D ON FX_DW005_BRCH_CDE = D.PX_IR124_BRCH_CDE " + 
			"LEFT JOIN IR124@IM F ON FX_DW006_BRCH_CDE = F.PX_IR124_BRCH_CDE " + 
			"WHERE TYPE='HCS' AND B.CRE_TMS between :fromdate and  :todate " + 
			"UNION ALL " + 
			"SELECT C.CRE_TMS,C.CASE_NO,SUBSTR(DED2@IM(ENC_CRD_NO,'FDS'),1,6) || 'XXXXXX' || SUBSTR(DED2@IM(ENC_CRD_NO,'FDS'),13,16) CARD_NO " + 
			", F9_DW005_LOC_ACCT, F9_DW006_LOC_ACCT,C.CIF_NO, (SELECT LISTAGG(RULE_ID, ',') WITHIN GROUP (ORDER BY CASE_NO) FROM (SELECT * FROM CCPS.fds_case_hit_rules UNION ALL SELECT * FROM CCPS.fds_case_hit_rules_hist) WHERE CASE_NO = B.CASE_NO) RULE " + 
			",FX_DW005_BRCH_CDE,FX_DW006_BRCH_CDE, D.FX_IR124_BRCH_NAME,F.FX_IR124_BRCH_NAME FX_IR124_BRCH_NAME_SUPP,CRD_BRN,FX_DW005_CRD_PGM||FX_DW006_CRD_PGM CRD_PGM " + 
			"FROM CCPS.FDS_DESCRIPTION A " + 
			"LEFT JOIN (SELECT * FROM CCPS.FDS_CASE_STATUS UNION ALL SELECT * FROM CCPS.FDS_CASE_STATUS_HIST) B ON B.CLOSED_REASON = A.TYPE AND B.OTHER LIKE '%'|| A.ID ||'%' " + 
			"INNER JOIN (SELECT * FROM CCPS.FDS_CASE_DETAIL UNION ALL SELECT * FROM CCPS.FDS_CASE_DETAIL_HIST) C ON B.CASE_NO = C.CASE_NO " + 
//			"INNER JOIN ( " + 
//			"    select min(r.rule_priority) keep(dense_rank first order by r.rule_priority) rule_priority,t.case_no " + 
//			"    from (SELECT * FROM CCPS.fds_case_hit_rules UNION ALL SELECT * FROM CCPS.fds_case_hit_rules_hist) t  " + 
//			"    join CCPS.fds_rules r on t.rule_id=r.rule_id  " + 
//			"    GROUP BY t.case_no " + 
//			"    order by t.case_no asc " + 
//			") C ON C.CASE_NO=B.CASE_NO " + 
//			"INNER JOIN CCPS.fds_rules D ON C.rule_priority=D.rule_priority AND D.RULE_ID IN ('RULE13','RULE14','RULE15','RULE16','RULE34','RULE35','RULE20','RULE19','RULE36','RULE37','RULE38','RULE42') " + 
			"LEFT JOIN CCPS.DW005 C ON PX_DW005_PAN = ENC_CRD_NO " + 
			"LEFT JOIN CCPS.DW006 E ON PX_DW006_OWN_PAN = ENC_CRD_NO " + 
			"LEFT JOIN IR124@IM D ON FX_DW005_BRCH_CDE = D.PX_IR124_BRCH_CDE " + 
			"LEFT JOIN IR124@IM F ON FX_DW006_BRCH_CDE = F.PX_IR124_BRCH_CDE " + 
			"WHERE A.TYPE='HCF' AND B.CRE_TMS between :fromdate and  :todate " + 
			"UNION ALL " + 
			"SELECT B.CRE_TMS,B.CASE_NO,SUBSTR(DED2@IM(ENC_CRD_NO,'FDS'),1,6) || 'XXXXXX' || SUBSTR(DED2@IM(ENC_CRD_NO,'FDS'),13,16) CARD_NO " + 
			", F9_DW005_LOC_ACCT, F9_DW006_LOC_ACCT,B.CIF_NO, (SELECT LISTAGG(RULE_ID, ',') WITHIN GROUP (ORDER BY CASE_NO) FROM (SELECT * FROM CCPS.fds_case_hit_rules UNION ALL SELECT * FROM CCPS.fds_case_hit_rules_hist) WHERE CASE_NO = B.CASE_NO) RULE " + 
			",FX_DW005_BRCH_CDE,FX_DW006_BRCH_CDE, D.FX_IR124_BRCH_NAME,F.FX_IR124_BRCH_NAME FX_IR124_BRCH_NAME_SUPP,CRD_BRN,FX_DW005_CRD_PGM||FX_DW006_CRD_PGM CRD_PGM " + 
			"FROM (SELECT * FROM CCPS.FDS_CASE_STATUS UNION ALL SELECT * FROM CCPS.FDS_CASE_STATUS_HIST) A INNER JOIN (SELECT * FROM CCPS.FDS_CASE_DETAIL UNION ALL SELECT * FROM CCPS.FDS_CASE_DETAIL_HIST) B ON A.CASE_NO = B.CASE_NO " + 
			"INNER JOIN ( " + 
			"    select min(r.rule_priority) keep(dense_rank first order by r.rule_priority) rule_priority,t.case_no " + 
			"    from (SELECT * FROM CCPS.fds_case_hit_rules UNION ALL SELECT * FROM CCPS.fds_case_hit_rules_hist) t  " + 
			"    join CCPS.fds_rules r on t.rule_id=r.rule_id  " + 
			"    GROUP BY t.case_no " + 
			"    order by t.case_no asc " + 
			") C ON C.CASE_NO=B.CASE_NO " + 
			"INNER JOIN CCPS.fds_rules D ON C.rule_priority=D.rule_priority AND D.RULE_ID IN ('RULE13','RULE14','RULE15','RULE16','RULE34','RULE35','RULE20','RULE19','RULE36','RULE37','RULE38','RULE42') " + 
			"LEFT JOIN CCPS.DW005 C ON PX_DW005_PAN = ENC_CRD_NO " + 
			"LEFT JOIN CCPS.DW006 E ON PX_DW006_OWN_PAN = ENC_CRD_NO " + 
			"LEFT JOIN IR124@IM D ON FX_DW005_BRCH_CDE = D.PX_IR124_BRCH_CDE " + 
			"LEFT JOIN IR124@IM F ON FX_DW006_BRCH_CDE = F.PX_IR124_BRCH_CDE " + 
			"WHERE A.CLOSED_REASON = 'NCR' AND A.CRE_TMS between :fromdate and  :todate " + 
			")) Z LEFT JOIN IR121@IM F ON px_ir121_crd_pgm=Z.CRD_PGM " , nativeQuery = true)
	@Transactional
	void importGiaoDichHitRuleNhom2(@Param(value = "fromdate") String fromdate,@Param(value = "todate") String todate,@Param(value = "usrid") String usrid,@Param(value = "kybaocao") String kybaocao);

	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
