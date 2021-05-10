package com.dvnb.services;


import java.util.List;

import com.dvnb.entities.DvnbBillingGrp;

public interface DvnbBillingGrpService {
	
	List<DvnbBillingGrp> findAllByCrdBrn(String cardBrn);
	
	List<DvnbBillingGrp> findAllByCrdBrnOrderByCreTms(String cardBrn);
	
	List<DvnbBillingGrp> findAllByOrderByCreTms();
	
	public void save(DvnbBillingGrp dvnbBillingGrp);
	
	public void deleteByCrdBrn(String crdBrn);
	
	public boolean existsById(String billingCode);
}
