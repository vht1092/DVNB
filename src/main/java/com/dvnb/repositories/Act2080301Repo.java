package com.dvnb.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2080301;

@Repository
public interface Act2080301Repo extends JpaRepository<Act2080301, String> {
	
	Page<Act2080301> findAll(Pageable page);
	
	Page<Act2080301> findAllByKy(@Param("ky") String ky, Pageable page);
	
	@Modifying
	@Query(value = "MERGE Into DVNB_ACT_2080301 a USING DVNB_DVCNT b  " + 
			"ON (a.TID=b.TID AND a.TID IS NOT NULL AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.MA_DON_VI='ENT_' || b.DON_VI_QL_DVCNT ", nativeQuery = true)
	void updateErpMappingByKybaocao(@Param(value = "kybaocao") String kybaocao);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
