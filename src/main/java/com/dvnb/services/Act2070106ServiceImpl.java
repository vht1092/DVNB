package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2070106;
import com.dvnb.repositories.Act2070106Repo;

@Service("act2070106Service")
@Transactional
public class Act2070106ServiceImpl implements Act2070106Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2070106Repo act2070106Repo;
	
	@Override
	public Page<Act2070106> findAll(Pageable page) {
		return act2070106Repo.findAll(page);
	}
	
	@Override
	public Page<Act2070106> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2070106Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2070106 act2070106) {

		act2070106Repo.save(act2070106);
	}
	
	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2070106Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2070106Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
	
}
