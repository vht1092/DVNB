package com.dvnb.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2070103;

@Repository
public interface Act2070103Repo extends JpaRepository<Act2070103, String> {
	
	Page<Act2070103> findAll(Pageable page);
	
	Page<Act2070103> findAllByKy(@Param("ky") String ky, Pageable page);
	
	@Modifying
	@Query(value = "MERGE Into dvnb_act_2070103 a USING OA177@AM b " + 
			"ON (TRIM(a.MA_THIET_BI)=PX_OA177_TID AND a.MA_THIET_BI IS NOT NULL AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.ma_don_vi = 'ENT_' || b.FX_OA177_BRCH_CDE ", nativeQuery = true)
	void updateErpMappingByKybaocao(@Param(value = "kybaocao") String kybaocao);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
