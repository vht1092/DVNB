package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390303;
import com.dvnb.repositories.Act3390303Repo;


@Service("act3390303Service")
@Transactional
public class Act3390303ServiceImpl implements Act3390303Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390303Repo act3390303Repo;
	

	@Override
	public Page<Act3390303> findAll(Pageable page) {
		return act3390303Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390303> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390303Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390303 act3390303) {

		act3390303Repo.save(act3390303);
	}

	@Override
	public Act3390303 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3390303Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390303Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390303Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
