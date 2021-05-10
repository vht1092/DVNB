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
}
