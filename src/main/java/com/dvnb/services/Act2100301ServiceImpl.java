package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100301;
import com.dvnb.repositories.Act2100301Repo;

@Service("act2100301Service")
@Transactional
public class Act2100301ServiceImpl implements Act2100301Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100301Repo act2100301Repo;
	
	@Override
	public Page<Act2100301> findAll(Pageable page) {
		return act2100301Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100301> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100301Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100301 act2100301) {

		act2100301Repo.save(act2100301);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2100301Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100301Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100301Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
