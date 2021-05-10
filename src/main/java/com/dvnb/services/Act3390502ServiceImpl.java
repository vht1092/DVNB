package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390502;
import com.dvnb.repositories.Act3390502Repo;


@Service("act3390502Service")
@Transactional
public class Act3390502ServiceImpl implements Act3390502Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390502Repo act3390502Repo;
	

	@Override
	public Page<Act3390502> findAll(Pageable page) {
		return act3390502Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390502> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390502Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390502 act3390502) {

		act3390502Repo.save(act3390502);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390502Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390502Repo.deleteByKyAndUsrId(ky, userId);
	}

	@Override
	public void updateERPByLoaiThe(String ky) {
		act3390502Repo.updateERPByLoaiThe(ky);
		
	}
	
}
