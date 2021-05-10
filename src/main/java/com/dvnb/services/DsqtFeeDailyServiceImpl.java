package com.dvnb.services;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DsqtFeeDaily;
import com.dvnb.entities.TyGiaTqt;
import com.dvnb.repositories.DsqtFeeDailyRepo;
import com.dvnb.repositories.TyGiaTqtRepo;

@Service("dsqtFeeDailyService")
@Transactional
public class DsqtFeeDailyServiceImpl implements DsqtFeeDailyService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DsqtFeeDailyRepo dsqtFeeDailyRepo;
	@Override
	public void save(DsqtFeeDaily feeDaily) {
		// TODO Auto-generated method stub
		dsqtFeeDailyRepo.save(feeDaily);
		
	}
	@Override
	public List<DsqtFeeDaily> findAllByNgayHachToan(String ngayHachToan) {
		// TODO Auto-generated method stub
		return dsqtFeeDailyRepo.findAllByNgayHachToan(ngayHachToan);
	}


	

}
