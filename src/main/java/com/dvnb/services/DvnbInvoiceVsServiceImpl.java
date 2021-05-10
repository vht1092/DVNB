package com.dvnb.services;

import java.math.BigDecimal;

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
	
	@Override
	public void updateKhongphanboByBillingLineAndInvoiceId(String ky, String billingLine, String invoiceId) {
		// TODO Auto-generated method stub
		dvnbInvoiceVsRepo.updateKhongphanboByBillingLineAndInvoiceId(ky, billingLine, invoiceId);
	}
	
	@Override
	public int countInvoiceByKyAndKetChuyenStatus(String ky, String ketchuyenStatus) {
		// TODO Auto-generated method stub
		return dvnbInvoiceVsRepo.countInvoiceByKyAndKetChuyenStatus(ky, ketchuyenStatus);
	}
	
	@Override
	public void deleteByKyHoaDon(String ky) {
		dvnbInvoiceVsRepo.deleteByKy(ky);
	}

	@Override
	public int updateKyMoiByIdAndKyMoiIsNotNull(String ky, String id) {
		// TODO Auto-generated method stub
		return dvnbInvoiceVsRepo.updateKyMoiByIdAndKyMoiIsNotNull(ky, id);
	}

	@Override
	public int insertKhongKetChuyenIntoKyMoiById(String id, String newId, BigDecimal creTms, String userId, String kyMoi, String ngaythuchien,String ketchuyenStatus) {
		// TODO Auto-generated method stub
		return dvnbInvoiceVsRepo.insertKhongKetChuyenIntoKyMoiById(id, newId, creTms, userId, kyMoi, ngaythuchien,ketchuyenStatus);
	}

	@Override
	public void deleteByOldId(String oldId) {
		// TODO Auto-generated method stub
		dvnbInvoiceVsRepo.deleteByOldId(oldId);
	}

	@Override
	public int updateKyMoiByIdAndKyMoiIsNull(String ky, String id) {
		// TODO Auto-generated method stub
		return dvnbInvoiceVsRepo.updateKyMoiByIdAndKyMoiIsNull(ky, id);
	}



	
}
