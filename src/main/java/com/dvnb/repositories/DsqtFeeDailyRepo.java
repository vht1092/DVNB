package com.dvnb.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DsqtFeeDaily;

@Repository
public interface DsqtFeeDailyRepo extends JpaRepository<DsqtFeeDaily, String> {
	
	@Query(value = "SELECT * FROM DSQT_UPDATE_FEE_DAILY\r\n" + 
			"WHERE NGAY_HACH_TOAN=:ngayHachToan", nativeQuery = true)
	List<DsqtFeeDaily> findAllByNgayHachToan(@Param("ngayHachToan") String ngayHachToan);
	
	DsqtFeeDaily findOneByLoaiTheAndNgayAdvAndLoaiGdAndLoaiTienTqt(@Param("loaiThe") String loaiThe,@Param("ngayAdv") String ngayAdv,@Param("loaiGd") String loaiGd,@Param("loaiTienTqt") String loaiTienTqt);
	
}
