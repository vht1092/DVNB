package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110302;
import com.dvnb.repositories.Act2110302Repo;


@Service("act2110302Service")
@Transactional
public class Act2110302ServiceImpl implements Act2110302Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110302Repo act2110302Repo;
	

	@Override
	public Page<Act2110302> findAll(Pageable page) {
		return act2110302Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110302> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110302Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110302 act2110302) {

		act2110302Repo.save(act2110302);
	}

	@Override
	public Act2110302 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110302Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110302Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110302Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
