package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100401;
import com.dvnb.repositories.Act2100401Repo;


@Service("act2100401Service")
@Transactional
public class Act2100401ServiceImpl implements Act2100401Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100401Repo act2100401Repo;
	

	@Override
	public Page<Act2100401> findAll(Pageable page) {
		return act2100401Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100401> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100401Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100401 act2100401) {

		act2100401Repo.save(act2100401);
	}

	@Override
	public Act2100401 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2100401Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100401Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100401Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
