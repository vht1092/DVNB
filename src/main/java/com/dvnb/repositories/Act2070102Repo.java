package com.dvnb.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2070102;

@Repository
public interface Act2070102Repo extends JpaRepository<Act2070102, String> {
	
	Page<Act2070102> findAll(Pageable page);
	
	Page<Act2070102> findAllByKy(@Param("ky") String ky, Pageable page);
	
	@Modifying
	@Query(value = "MERGE Into dvnb_act_2070102 a USING DVNB_ERP b " + 
			"ON (SO_THE=REPLACE(b.PX_IRPANMAP_PANMASK,'XXXXXX',b.PX_IRPANMAP_MID) AND a.SO_THE IS NOT NULL AND a.KY=:kybaocao) " + 
			"when matched then UPDATE SET a.ma_don_vi=b.ma_don_vi, a.MA_SAN_PHAM=b.MA_SAN_PHAM, a.ma_loai_khach_hang=b.ma_loai_khach_hang ", nativeQuery = true)
	void updateErpMappingByKybaocao(@Param(value = "kybaocao") String kybaocao);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
