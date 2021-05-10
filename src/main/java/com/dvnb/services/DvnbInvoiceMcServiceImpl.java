package com.dvnb.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.repositories.DvnbInvoiceMcRepo;

@Service("dvnbInvoiceMcService")
@Transactional
public class DvnbInvoiceMcServiceImpl implements DvnbInvoiceMcService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DvnbInvoiceMcRepo dvnbInvoiceMcRepo;

	@Override
	public Page<DvnbInvoiceMc> findAll(Pageable page) {
		return dvnbInvoiceMcRepo.findAll(page);
	}

	@Override
	public Page<DvnbInvoiceMc> findAllByKy(String ky, Pageable page) {
		return dvnbInvoiceMcRepo.findAllByKy(ky, page);
	}
	
	@Override
	public Page<DvnbInvoiceMc> findAllByNgayHoaDon(String tungay, String denngay, String ketchuyenFlag, Pageable page){
		return dvnbInvoiceMcRepo.findAllByNgayHoaDon(tungay,denngay,ketchuyenFlag, page);
	}
	
	@Override
	public void create(DvnbInvoiceMc dvnbInvoiceMc) {

		dvnbInvoiceMcRepo.save(dvnbInvoiceMc);
	}
	
	@Override
	public void updateKetChuyenById(String ketchuyen, String id) {
		dvnbInvoiceMcRepo.updateKetChuyenStatus(ketchuyen, id);
	}
	
	@Override
	public void updateDeviationById(String deviation, String id) {
		dvnbInvoiceMcRepo.updateDeviation(deviation, id);
	}
	
	@Override
	public void updateKhongphanboByEventIdAndInvoiceNumber(String ky, String eventId, String invoiceNumber) {
		dvnbInvoiceMcRepo.updateKhongphanboByEventIdAndInvoiceNumber(ky, eventId, invoiceNumber);
	}
	
	@Override
	public int countInvoiceByKyAndKetChuyenStatus(String ky, String ketchuyenStatus) {
		return dvnbInvoiceMcRepo.countInvoiceByKyAndKetChuyenStatus(ky, ketchuyenStatus);
	}
	
	@Override
	public void deleteByKyHoaDon(String ky) {
		dvnbInvoiceMcRepo.deleteByKy(ky);
	}
}
