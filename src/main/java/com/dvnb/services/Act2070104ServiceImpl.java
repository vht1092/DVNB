package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2070104;
import com.dvnb.repositories.Act2070104Repo;

@Service("act2070104Service")
@Transactional
public class Act2070104ServiceImpl implements Act2070104Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2070104Repo act2070104Repo;
	
	@Override
	public Page<Act2070104> findAll(Pageable page) {
		return act2070104Repo.findAll(page);
	}
	
	@Override
	public Page<Act2070104> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2070104Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2070104 act2070104) {

		act2070104Repo.save(act2070104);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2070104Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2070104Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2070104Repo.deleteByKyAndUsrId(ky, userId);
	} 
}
