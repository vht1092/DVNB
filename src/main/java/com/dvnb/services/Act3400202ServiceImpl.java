package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400202;
import com.dvnb.repositories.Act3400202Repo;


@Service("act3400202Service")
@Transactional
public class Act3400202ServiceImpl implements Act3400202Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400202Repo act3400202Repo;
	

	@Override
	public Page<Act3400202> findAll(Pageable page) {
		return act3400202Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400202> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400202Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400202 act3400202) {

		act3400202Repo.save(act3400202);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400202Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400202Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
