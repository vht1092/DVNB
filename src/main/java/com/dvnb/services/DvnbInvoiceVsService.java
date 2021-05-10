package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

}
