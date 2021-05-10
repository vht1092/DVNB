package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3390201;

@Repository
public interface Act3390201Repo extends JpaRepository<Act3390201, String> {
	
	

	Page<Act3390201> findAll(Pageable page);
	
	Page<Act3390201> findAllByKy(@Param("ky") String ky, Pageable page);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_3390201 a USING IR121@IM b    " + 
			"ON (a.LOAI_THE=TRIM(b.PX_IR121_CRD_PGM) AND a.KY=:kybaocao)    " + 
			"when matched then UPDATE  " + 
			"SET a.ma_loai_khach_hang= (CASE WHEN f9_ir121_prfx in ('512454','5471390', '5471391') THEN 'CUS_03' ELSE 'CUS_01' END), " + 
			"    a.MA_SAN_PHAM = (CASE WHEN F9_IR121_ACCT_PRV=2 AND FX_IR121_CRD_BRN='VS' THEN 'PRO_307'  " + 
			"        WHEN F9_IR121_ACCT_PRV=2 AND FX_IR121_CRD_BRN='MC' THEN 'PRO_234' " + 
			"        WHEN F9_IR121_ACCT_PRV=8 AND FX_IR121_CRD_BRN='VS' THEN 'PRO_222' " + 
			"        WHEN F9_IR121_ACCT_PRV=8 AND FX_IR121_CRD_BRN='MC' THEN 'PRO_228'  " + 
			"        WHEN F9_IR121_ACCT_PRV=2 AND FX_IR121_CRD_BRN = 'LC' THEN 'PRO_215' END)", nativeQuery = true)
	void updateERPByLoaiThe(@Param(value = "kybaocao") String kybaocao);
}
