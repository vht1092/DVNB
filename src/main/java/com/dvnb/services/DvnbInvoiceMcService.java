package com.dvnb.services;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.DvnbInvoiceMc;

public interface DvnbInvoiceMcService {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<DvnbInvoiceMc> findAll(Pageable page);
	
	Page<DvnbInvoiceMc> findAllByKy(String ky, Pageable page);
	
	Page<DvnbInvoiceMc> findAllByNgayHoaDon(String tungay, String denngay, String ketchuyenFlag, Pageable page);
	
	void create(DvnbInvoiceMc gstsInvoiceMc);
	
	void updateKetChuyenById(String ketchuyen, String id);
	
	void updateDeviationById(String deviation, String id);
	
	void updateKhongphanboByEventIdAndInvoiceNumber(String ky, String eventId, String invoiceNumber);
	
	int countInvoiceByKyAndKetChuyenStatus(String ky, String ketchuyenStatus);
	
	void deleteByKyHoaDon(String ky);

	int updateKyMoiByIdAndKyMoiIsNotNull(String ky, String id);
	
	int updateKyMoiByIdAndKyMoiIsNull(String ky, String id);
	
	int insertKhongKetChuyenIntoKyMoiById(String id,String newId,BigDecimal creTms,String userId,String kyMoi,String ngaythuchien,String ketchuyenStatus);
	
	void deleteByOldId(String oldId);
}
