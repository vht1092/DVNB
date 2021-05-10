package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110101;
import com.dvnb.repositories.Act2110101Repo;


@Service("act2110101Service")
@Transactional
public class Act2110101ServiceImpl implements Act2110101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110101Repo act2110101Repo;
	

	@Override
	public Page<Act2110101> findAll(Pageable page) {
		return act2110101Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110101 act2110101) {

		act2110101Repo.save(act2110101);
	}

	@Override
	public Act2110101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
