package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110203;
import com.dvnb.repositories.Act2110203Repo;


@Service("act2110203Service")
@Transactional
public class Act2110203ServiceImpl implements Act2110203Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110203Repo act2110203Repo;
	

	@Override
	public Page<Act2110203> findAll(Pageable page) {
		return act2110203Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110203> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110203Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110203 act2110203) {

		act2110203Repo.save(act2110203);
	}

	@Override
	public Act2110203 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110203Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110203Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110203Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
