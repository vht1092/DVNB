package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2070103;
import com.dvnb.repositories.Act2070103Repo;

@Service("act2070103Service")
@Transactional
public class Act2070103ServiceImpl implements Act2070103Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2070103Repo act2070103Repo;
	
	@Override
	public Page<Act2070103> findAll(Pageable page) {
		return act2070103Repo.findAll(page);
	}
	
	@Override
	public Page<Act2070103> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2070103Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2070103 act2070103) {

		act2070103Repo.save(act2070103);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2070103Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2070103Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2070103Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
