package com.dvnb.services;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2070105;
import com.dvnb.repositories.Act2070105Repo;

@Service("act2070105Service")
@Transactional
public class Act2070105ServiceImpl implements Act2070105Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2070105Repo act2070105Repo;
	
	@Override
	public Page<Act2070105> findAll(Pageable page) {
		return act2070105Repo.findAll(page);
	}
	
	@Override
	public Page<Act2070105> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2070105Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2070105 act2070105) {

		act2070105Repo.save(act2070105);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2070105Repo.updateErpMappingByKybaocao(ky);
		
	} 
	
	@Override
	public void updateMaLoaiKHCN(String ky) {
		// TODO Auto-generated method stub
		act2070105Repo.updateMaLoaiKHCN(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2070105Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2070105Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
	
}
