package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2080301;
import com.dvnb.repositories.Act2080301Repo;

@Service("act2080301Service")
@Transactional
public class Act2080301ServiceImpl implements Act2080301Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080301Repo act2080301Repo;
	
	@Override
	public Page<Act2080301> findAll(Pageable page) {
		return act2080301Repo.findAll(page);
	}
	
	@Override
	public Page<Act2080301> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080301Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2080301 act2080301) {

		act2080301Repo.save(act2080301);
	}

	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2080301Repo.updateErpMappingByKybaocao(ky);
		
	}
	
	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080301Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080301Repo.deleteByKyAndUsrId(ky, userId);
	}
	
	
}
