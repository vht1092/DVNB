package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110103;
import com.dvnb.repositories.Act2110103Repo;


@Service("act2110103Service")
@Transactional
public class Act2110103ServiceImpl implements Act2110103Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110103Repo act2110103Repo;
	

	@Override
	public Page<Act2110103> findAll(Pageable page) {
		return act2110103Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110103> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110103Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110103 act2110103) {

		act2110103Repo.save(act2110103);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110103Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110103Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
