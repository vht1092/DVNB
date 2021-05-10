package com.dvnb.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.DvnbInvoiceVs;

public interface DvnbInvoiceVsService {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<DvnbInvoiceVs> findAll(Pageable page);
	
	Page<DvnbInvoiceVs> findAllByKy(String ky, Pageable page);
	
	Page<DvnbInvoiceVs> findAllByNgayHoaDon(String tungay, String denngay, String ketchuyenFlag, Pageable page);
	
	void create(DvnbInvoiceVs gstsInvoiceVs);
	
	void updateKetChuyenById(String ketchuyen, String id);
	
	void updateDeviationById(String deviation, String id);
	
	void updateKhongphanboByBillingLineAndInvoiceId(String ky, String billingLine, String invoiceId);
	
	int countInvoiceByKyAndKetChuyenStatus(String ky, String ketchuyenStatus);
	
	void deleteByKyHoaDon(String ky);
	
	int updateKyMoiByIdAndKyMoiIsNotNull(String ky, String id);

	int updateKyMoiByIdAndKyMoiIsNull(String ky, String id);
	
	
	int insertKhongKetChuyenIntoKyMoiById(String id,String newId,BigDecimal creTms,String userId,String kyMoi,String ngaythuchien,String ketchuyenStatus);
	
	void deleteByOldId(String oldId);
}
