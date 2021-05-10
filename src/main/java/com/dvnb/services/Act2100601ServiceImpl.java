package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100601;
import com.dvnb.repositories.Act2100601Repo;


@Service("act2100601Service")
@Transactional
public class Act2100601ServiceImpl implements Act2100601Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100601Repo act2100601Repo;
	

	@Override
	public Page<Act2100601> findAll(Pageable page) {
		return act2100601Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100601> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100601Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100601 act2100601) {

		act2100601Repo.save(act2100601);
	}

	@Override
	public Act2100601 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2100601Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100601Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100601Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
