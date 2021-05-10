package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100202;
import com.dvnb.repositories.Act2100202Repo;


@Service("act2100202Service")
@Transactional
public class Act2100202ServiceImpl implements Act2100202Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100202Repo act2100202Repo;
	

	@Override
	public Page<Act2100202> findAll(Pageable page) {
		return act2100202Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100202> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100202Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100202 act2100202) {

		act2100202Repo.save(act2100202);
	}

	@Override
	public Act2100202 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2100202Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100202Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100202Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
