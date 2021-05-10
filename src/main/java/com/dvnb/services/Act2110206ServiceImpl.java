package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110206;
import com.dvnb.repositories.Act2110206Repo;


@Service("act2110206Service")
@Transactional
public class Act2110206ServiceImpl implements Act2110206Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110206Repo act2110206Repo;
	

	@Override
	public Page<Act2110206> findAll(Pageable page) {
		return act2110206Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110206> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110206Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110206 act2110206) {

		act2110206Repo.save(act2110206);
	}

	@Override
	public Act2110206 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110206Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110206Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110206Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
