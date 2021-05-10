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

import com.dvnb.entities.DvnbInvoiceMc;

@Repository
public interface DvnbInvoiceMcRepo extends JpaRepository<DvnbInvoiceMc, String> {

	Page<DvnbInvoiceMc> findAll(Pageable page);
	
	Page<DvnbInvoiceMc> findAllByKy(@Param("ky") String ky,Pageable page);
	
	@Query(value = "SELECT f FROM DvnbInvoiceMc f " +
			"WHERE (REPLACE(f.billingCycleDate,'/','') BETWEEN :tungay AND :denngay) " +
			"AND ((:ketchuyenFlag='All') OR (:ketchuyenFlag<>'All' and ketChuyen=:ketchuyenFlag))")
	Page<DvnbInvoiceMc> findAllByNgayHoaDon(@Param("tungay") String tungay,@Param("denngay") String denngay,@Param("ketchuyenFlag") String ketchuyenFlag,Pageable page);
	
	@Modifying
	@Query(value = "update DvnbInvoiceMc f set f.ketChuyen=:ketChuyen where f.id=:id ")
	void updateKetChuyenStatus(@Param(value = "ketChuyen") String ketChuyen, @Param(value = "id") String id);
	
	@Modifying
	@Query(value = "update DvnbInvoiceMc f set f.deviation=:deviation where f.id=:id ")
	void updateDeviation(@Param(value = "deviation") String deviation, @Param(value = "id") String id);
	
	@Modifying
	@Query(value = "update DvnbInvoiceMc f  " + 
			"set ketChuyen='N' " + 
			"where ky=:ky  " + 
			"AND ((eventId=:eventId AND :invoiceNumber='All') " + 
			"OR (eventId=:eventId AND :invoiceNumber<>'All' AND invoiceNumber=:invoiceNumber))")
	void updateKhongphanboByEventIdAndInvoiceNumber(@Param(value = "ky") String ky,@Param(value = "eventId") String eventId, @Param(value = "invoiceNumber") String invoiceNumber);

	
	@Query(value = "SELECT count(*) FROM dvnb_invoice_mc " +
			"WHERE KY=:ky and ket_chuyen=:ketchuyenStatus ", nativeQuery = true)
	int countInvoiceByKyAndKetChuyenStatus(@Param(value = "ky") String ky,@Param(value = "ketchuyenStatus") String ketchuyenStatus);
	
	void deleteByKy(@Param("ky") String ky);
//	@Query(value = "MERGE Into dvnb_act_2070101 a USING DVNB_ERP b " + 
//			"ON (a.LOC=f9_irpanmap_loc and replace(a.SO_THE, ' ', '')=b.PX_IRPANMAP_PANMASK AND a.LOC IS NOT NULL AND a.SO_THE IS NOT NULL AND a.KY=:kybaocao) " + 
//			"when matched then UPDATE SET a.ma_don_vi=b.ma_don_vi, a.MA_SAN_PHAM=b.MA_SAN_PHAM, a.ma_loai_khach_hang=b.ma_loai_khach_hang ", nativeQuery = true)
//	List<DvnbInvoiceMc> updateErpMappingByKybaocao(@Param(value = "kybaocao") String kybaocao);
}
