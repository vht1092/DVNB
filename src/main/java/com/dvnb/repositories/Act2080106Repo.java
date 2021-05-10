package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080106;

@Repository
public interface Act2080106Repo extends JpaRepository<Act2080106, String> {

	Page<Act2080106> findAll(Pageable page);
	
	Page<Act2080106> findAllByKy(@Param("ky") String ky, Pageable page);
	
	@Modifying
	@Query(value = "MERGE Into dvnb_act_2080106 a " + 
			"USING FCUSR01.STTM_CUSTOMER@EXADATA b ON (a.MA_KHACH_HANG=b.CUSTOMER_NO AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.ma_don_vi= 'ENT_' || b.LOCAL_BRANCH, " + 
			"a.ma_loai_khach_hang= (CASE WHEN b.CUSTOMER_TYPE = 'I' THEN 'CUS_01' WHEN b.CUSTOMER_TYPE = 'C' THEN 'CUS_03' END) ", nativeQuery = true)
	void updateErpMappingByKybaocao(@Param(value = "kybaocao") String kybaocao);

	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
