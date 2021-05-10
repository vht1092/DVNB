package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3370102;
import com.dvnb.repositories.Act3370102Repo;


@Service("act3370102Service")
@Transactional
public class Act3370102ServiceImpl implements Act3370102Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3370102Repo act3370102Repo;
	

	@Override
	public Page<Act3370102> findAll(Pageable page) {
		return act3370102Repo.findAll(page);
	}
	
	@Override
	public Page<Act3370102> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3370102Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3370102 act3370102) {

		act3370102Repo.save(act3370102);
	}

	@Override
	public Act3370102 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3370102Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3370102Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3370102Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
