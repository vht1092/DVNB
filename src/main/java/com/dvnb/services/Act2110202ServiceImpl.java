package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110202;
import com.dvnb.repositories.Act2110202Repo;


@Service("act2110202Service")
@Transactional
public class Act2110202ServiceImpl implements Act2110202Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110202Repo act2110202Repo;
	

	@Override
	public Page<Act2110202> findAll(Pageable page) {
		return act2110202Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110202> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110202Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110202 act2110202) {

		act2110202Repo.save(act2110202);
	}

	@Override
	public Act2110202 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110202Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110202Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110202Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
