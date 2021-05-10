package com.dvnb.repositories;

import java.math.BigDecimal;

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
	
	@Modifying
	@Query(value = "update DvnbInvoiceVs f  " + 
			"set ketChuyen='N' " + 
			"where ky=:ky  " + 
			"AND ((billingLine=:billingLine AND :invoiceId='All') " + 
			"OR (billingLine=:billingLine AND :invoiceId<>'All' AND invoiceId=:invoiceId))")
	void updateKhongphanboByBillingLineAndInvoiceId(@Param(value = "ky") String ky,@Param(value = "billingLine") String billingLine, @Param(value = "invoiceId") String invoiceId);
	
	@Query(value = "SELECT count(*) FROM dvnb_invoice_vs " +
			"WHERE KY=:ky and ket_chuyen=:ketchuyenStatus ", nativeQuery = true)
	int countInvoiceByKyAndKetChuyenStatus(@Param(value = "ky") String ky,@Param(value = "ketchuyenStatus") String ketchuyenStatus);
	
	void deleteByKy(@Param("ky") String ky);
	
	@Modifying
	@Query(value = "update DvnbInvoiceVs f set f.kyMoi=:kyMoi where f.id=:id and f.kyMoi is not null")
	int updateKyMoiByIdAndKyMoiIsNotNull(@Param(value = "kyMoi") String kyMoi, @Param(value = "id") String id);
	
	@Modifying
	@Query(value = "update DvnbInvoiceVs f set f.kyMoi=:kyMoi where f.id=:id and f.kyMoi is null")
	int updateKyMoiByIdAndKyMoiIsNull(@Param(value = "kyMoi") String kyMoi, @Param(value = "id") String id);
	

	@Modifying
	@Query(value = "INSERT INTO DVNB_INVOICE_VS\r\n" + 
			"SELECT :creTms CRE_TMS, UPD_TMS,:userId USR_ID, UPD_UID,:newId ID,:kyMoi KY, BILLING_PERIOD, INVOICE_DATE, INVOICE_ACCOUNT, NAME, INVOICE_ID, SUB_INVOICE,\r\n" + 
			"CURRENT_OR_PREVIOUS, ENTITY_TYPE, ENTITY_ID, BIN_MAP, ENTITY_NAME, SETTLEMENT_ID, DESCRIPTION, FUTURE_USE, NTWK,\r\n" + 
			"BILLING_LINE, TYPE, RATE_TYPE, UNITS, RATE_CUR, RATE, FOREIGN_EXCHANGE_RATE, BILLING_CURRENCY, TOTAL, TAX_TYPE, TAX, \r\n" + 
			"TAX_RATE, TAX_CURRENCY, TAXABLE_AMOUNT, TAX_TAX_CURRENCY,:ketchuyenStatus KET_CHUYEN,:ngaythuchien NGAY_THUC_HIEN, TOTAL_VND, DIAVITION,null KY_MOI,ID OLD_ID\r\n" + 
			"FROM DVNB_INVOICE_VS\r\n" + 
			"WHERE id=:id AND KY_MOI IS NULL",nativeQuery = true)
	int insertKhongKetChuyenIntoKyMoiById(@Param(value = "id") String id,@Param(value = "newId") String newId,@Param(value = "creTms") BigDecimal creTms,@Param(value = "userId") String userId,@Param(value = "kyMoi") String kyMoi,@Param(value = "ngaythuchien") String ngaythuchien,@Param(value = "ketchuyenStatus") String ketchuyenStatus);

	void deleteByOldId(@Param("oldId") String oldId);
}
