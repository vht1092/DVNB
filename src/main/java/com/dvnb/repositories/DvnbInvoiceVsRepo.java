package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbInvoiceVs;

@Repository
public interface DvnbInvoiceVsRepo extends JpaRepository<DvnbInvoiceVs, String> {

	Page<DvnbInvoiceVs> findAll(Pageable page);
	
	Page<DvnbInvoiceVs> findAllByKy(@Param("ky") String ky,Pageable page);
	
	@Query(value = "SELECT f FROM DvnbInvoiceVs f " +
			"WHERE (REPLACE(f.invoiceDate,'/','') BETWEEN :tungay AND :denngay) " +
			"AND ((:ketchuyenFlag='All') OR (:ketchuyenFlag<>'All' and ketChuyen=:ketchuyenFlag))")
	Page<DvnbInvoiceVs> findAllByNgayHoaDon(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("ketchuyenFlag") String ketchuyenFlag,Pageable page);
	
	@Modifying
	@Query(value = "update DvnbInvoiceVs f set f.ketChuyen=:ketChuyen where f.id=:id ")
	void updateStatusCase(@Param(value = "ketChuyen") String ketChuyen, @Param(value = "id") String id);
	
	@Modifying
	@Query(value = "update DvnbInvoiceVs f set f.deviation=:deviation where f.id=:id ")
	void updateDeviation(@Param(value = "deviation") String deviation, @Param(value = "id") String id);
}
