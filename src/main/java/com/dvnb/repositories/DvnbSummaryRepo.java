package com.dvnb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbSummary;

@Repository
public interface DvnbSummaryRepo extends JpaRepository<DvnbSummary, String> {
	
	@Modifying
	@Query(value = "DELETE DVNB_SUMMARY \r\n" + 
			"WHERE KY=:ky AND ('All'=:maHoatDong AND MA_HOAT_DONG=:maHoatDong)\r\n" + 
			"AND ((:role='1' AND MA_HOAT_DONG like 'ACT_2%')\r\n" + 
			"OR (:role='2' AND MA_HOAT_DONG like 'ACT_3%')\r\n" + 
			"OR (:role='3' AND MA_HOAT_DONG like 'ACT_1%')\r\n" + 
			"OR (:role='4'))", nativeQuery = true)
	void deleteDvnbSummaryByMaHoatDong(@Param(value = "ky") String ky,@Param(value = "maHoatDong") String maHoatDong,@Param(value = "role") String role);
	
	
}
