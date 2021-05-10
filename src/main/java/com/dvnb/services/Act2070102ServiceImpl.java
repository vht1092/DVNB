package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2070102;
import com.dvnb.repositories.Act2070102Repo;

@Service("act2070102Service")
@Transactional
public class Act2070102ServiceImpl implements Act2070102Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2070102Repo act2070102Repo;
	
	@Override
	public Page<Act2070102> findAll(Pageable page) {
		return act2070102Repo.findAll(page);
	}
	
	@Override
	public Page<Act2070102> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2070102Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2070102 act2070102) {

		act2070102Repo.save(act2070102);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2070102Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2070102Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2070102Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
