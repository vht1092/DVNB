package com.dvnb.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbInvoiceVs;
import com.dvnb.repositories.DvnbInvoiceVsRepo;

@Service("dvnbInvoiceVsService")
@Transactional
public class DvnbInvoiceVsServiceImpl implements DvnbInvoiceVsService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DvnbInvoiceVsRepo dvnbInvoiceVsRepo;

	@Override
	public Page<DvnbInvoiceVs> findAll(Pageable page) {
		return dvnbInvoiceVsRepo.findAll(page);
	}

	@Override
	public Page<DvnbInvoiceVs> findAllByKy(String ky, Pageable page) {
		return dvnbInvoiceVsRepo.findAllByKy(ky, page);
	}
	
	@Override
	public Page<DvnbInvoiceVs> findAllByNgayHoaDon(String tungay, String denngay, String ketchuyenFlag, Pageable page){
		return dvnbInvoiceVsRepo.findAllByNgayHoaDon(tungay,denngay,ketchuyenFlag, page);
	}
	
	@Override
	public void create(DvnbInvoiceVs gstsInvoiceVs) {

		dvnbInvoiceVsRepo.save(gstsInvoiceVs);
	}
	
	@Override
	public void updateKetChuyenById(String ketchuyen, String id) {
		dvnbInvoiceVsRepo.updateStatusCase(ketchuyen, id);
	}
	
	@Override
	public void updateDeviationById(String deviation, String id) {
		dvnbInvoiceVsRepo.updateDeviation(deviation, id);
	}
}
