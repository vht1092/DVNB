package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3390901;

@Repository
public interface Act3390901Repo extends JpaRepository<Act3390901, String> {
	
	

	Page<Act3390901> findAll(Pageable page);
	
	Page<Act3390901> findAllByKy(@Param("ky") String ky, Pageable page);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_3390901 a USING OA177@AM b " +
			"ON (TID=PX_OA177_TID AND MID IS NULL AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.ma_don_vi = 'ENT_' || b.FX_OA177_BRCH_CDE  ", nativeQuery = true)
	void updateMaDonViByTidAtm(@Param(value = "kybaocao") String kybaocao);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_3390901 a USING DVNB_DVCNT b " + 
			"ON (a.TID=b.TID AND a.MID IS NOT NULL AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.ma_don_vi = 'ENT_' || b.DON_VI_QL_DVCNT ", nativeQuery = true)
	void updateMaDonViByTidPos(@Param(value = "kybaocao") String kybaocao);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
