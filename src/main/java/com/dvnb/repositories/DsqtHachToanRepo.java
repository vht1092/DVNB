package com.dvnb.repositories;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DsqtHachToan;

@Repository
public interface DsqtHachToanRepo extends JpaRepository<DsqtHachToan, String> {
	
	List<DsqtHachToan> findAllByNgayAdv(@Param("ngayAdv") String ngayAdv);
	
	List<DsqtHachToan> findAllByNgayAdvAndCardBrnAndCurr(@Param("ngayAdv") String ngayAdv, @Param("cardBrn") String cardBrn, @Param("curr") String curr);
	
	@Query(value = "SELECT COUNT(*) FROM DSQT_HACH_TOAN WHERE FILE_NAME=:filename", nativeQuery = true)
	int countByFilename(@Param("filename") String filename);
	
	
	@Query(value = "SELECT DISTINCT FILE_NAME\r\n" + 
			"FROM ( SELECT FILE_NAME \r\n" + 
			"    FROM DSQT_HACH_TOAN\r\n" + 
			"    WHERE NGAY_ADV=:ngayadv\r\n" + 
			"    ORDER BY CRE_TMS DESC)", nativeQuery = true)
	List<String> findAllFilenameByNgayadv(@Param("ngayadv") String ngayadv);
	
	@Modifying
	@Query(value = "DELETE DSQT_HACH_TOAN WHERE NGAY_ADV=:ngayadv", nativeQuery = true)
	int deleteAllByNgayAdv(@Param("ngayadv") String ngayadv);
}
