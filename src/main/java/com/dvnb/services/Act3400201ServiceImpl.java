package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400201;
import com.dvnb.repositories.Act3400201Repo;


@Service("act3400201Service")
@Transactional
public class Act3400201ServiceImpl implements Act3400201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400201Repo act3400201Repo;
	

	@Override
	public Page<Act3400201> findAll(Pageable page) {
		return act3400201Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400201> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400201Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400201 act3400201) {

		act3400201Repo.save(act3400201);
	}

	@Override
	public Act3400201 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3400201Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400201Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
