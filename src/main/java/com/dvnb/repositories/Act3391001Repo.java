package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3391001;

@Repository
public interface Act3391001Repo extends JpaRepository<Act3391001, String> {
	
	

	Page<Act3391001> findAll(Pageable page);
	
	Page<Act3391001> findAllByKy(@Param("ky") String ky, Pageable page);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_3391001 a USING OA177@AM b  " + 
			"ON (TID=PX_OA177_TID AND MID IS NULL AND CIF IS NULL AND a.KY=:kybaocao)  " + 
			"when matched then UPDATE SET a.ma_don_vi = 'ENT_' || b.FX_OA177_BRCH_CDE, a.MA_SAN_PHAM='PRO_241'", nativeQuery = true)
	void updateERPByTidAtm(@Param(value = "kybaocao") String kybaocao);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_3391001 a USING DVNB_DVCNT b " + 
			"ON (a.TID=b.TID AND a.MID IS NOT NULL AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.ma_don_vi = 'ENT_' || b.DON_VI_QL_DVCNT, a.MA_SAN_PHAM='PRO_243', a.MA_LOAI_KHACH_HANG='CUS_03' ", nativeQuery = true)
	void updateERPByTidPos(@Param(value = "kybaocao") String kybaocao);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_3391001 a USING FCUSR01.STTM_CUSTOMER@EXADATA b  " + 
			"ON (a.CIF=b.CUSTOMER_NO AND a.CIF IS NOT NULL AND a.KY=:kybaocao)  " + 
			"when matched then UPDATE SET a.ma_don_vi = 'ENT_' || b.LOCAL_BRANCH ,  " + 
			"a.ma_loai_khach_hang= (CASE WHEN b.CUSTOMER_TYPE = 'I' THEN 'CUS_01' WHEN b.CUSTOMER_TYPE = 'C' THEN 'CUS_03' END), " + 
			"a.MA_SAN_PHAM='PRO_255' ", nativeQuery = true)
	void updateERPByCif(@Param(value = "kybaocao") String kybaocao);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
