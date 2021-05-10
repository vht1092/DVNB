package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110402;
import com.dvnb.repositories.Act2110402Repo;


@Service("act2110402Service")
@Transactional
public class Act2110402ServiceImpl implements Act2110402Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110402Repo act2110402Repo;
	

	@Override
	public Page<Act2110402> findAll(Pageable page) {
		return act2110402Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110402> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110402Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110402 act2110402) {

		act2110402Repo.save(act2110402);
	}

	@Override
	public Act2110402 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110402Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110402Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110402Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
