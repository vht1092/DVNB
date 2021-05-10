package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100503;
import com.dvnb.repositories.Act2100503Repo;


@Service("act2100503Service")
@Transactional
public class Act2100503ServiceImpl implements Act2100503Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100503Repo act2100503Repo;
	

	@Override
	public Page<Act2100503> findAll(Pageable page) {
		return act2100503Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100503> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100503Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100503 act2100503) {

		act2100503Repo.save(act2100503);
	}

	@Override
	public Act2100503 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2100503Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100503Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100503Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
