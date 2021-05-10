package com.dvnb.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbBillingGrp;
import com.dvnb.entities.DvnbTyGia;
import com.dvnb.repositories.DvnbBillingGrpRepo;
import com.dvnb.repositories.TyGiaRepo;

@Service("dvnbBillingGrpService")
@Transactional
public class DvnbBillingGrpServiceImpl implements DvnbBillingGrpService {

	@Autowired
	private DvnbBillingGrpRepo dvnbBillingGrpRepo;

	@Override
	public List<DvnbBillingGrp> findAllByCrdBrn(String cardBrn) {
		// TODO Auto-generated method stub
		return dvnbBillingGrpRepo.findAllByCrdBrn(cardBrn);
	}
	
	@Override
	public List<DvnbBillingGrp> findAllByCrdBrnOrderByCreTms(String cardBrn) {
		// TODO Auto-generated method stub
		return dvnbBillingGrpRepo.findAllByCrdBrnOrderByCreTms(cardBrn);
	}
	
	@Override
	public List<DvnbBillingGrp> findAllByOrderByCreTms() {
		// TODO Auto-generated method stub
		return dvnbBillingGrpRepo.findAllByOrderByCreTms();
	}

	@Override
	public void save(DvnbBillingGrp dvnbBillingGrp) {
		// TODO Auto-generated method stub
		dvnbBillingGrpRepo.save(dvnbBillingGrp);
	}

	@Override
	public void deleteByCrdBrn(String crdBrn) {
		// TODO Auto-generated method stub
		dvnbBillingGrpRepo.deleteByCrdBrn(crdBrn);
	}

	@Override
	public boolean existsById(String billingCode) {
		// TODO Auto-generated method stub
		return dvnbBillingGrpRepo.exists(billingCode);
	}

	

	


}
