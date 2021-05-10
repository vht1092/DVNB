package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dvnb.entities.TyGiaTqt;
import com.dvnb.repositories.TyGiaTqtRepo;

@Service("tyGiaTqtService")
@Transactional
public class TyGiaTqtServiceImpl implements TyGiaTqtService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private TyGiaTqtRepo tyGiaTqtRepo;
	@Override
	public void save(TyGiaTqt tyGiaTqt) {
		// TODO Auto-generated method stub
		tyGiaTqtRepo.save(tyGiaTqt);
		
	}
	
	@Override
	public TyGiaTqt findTyGiaTqtByNgayAdv(String ngayAdv) {
		return tyGiaTqtRepo.findTyGiaTqtByNgayAdv(ngayAdv);
		
	}
	
	@Override
	public TyGiaTqt findTyGiaTqtByNgayAdvAndCardType(String ngayAdv,String cardType) {
		return tyGiaTqtRepo.findAllByNgayAdvAndCardType(ngayAdv,cardType);
		
	}
	

}
