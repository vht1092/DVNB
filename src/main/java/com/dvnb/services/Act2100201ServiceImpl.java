package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100201;
import com.dvnb.repositories.Act2100201Repo;

@Service("act2100201Service")
@Transactional
public class Act2100201ServiceImpl implements Act2100201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100201Repo act2100201Repo;
	
	@Override
	public Page<Act2100201> findAll(Pageable page) {
		return act2100201Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100201> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100201Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100201 act2100201) {

		act2100201Repo.save(act2100201);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2100201Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100201Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
