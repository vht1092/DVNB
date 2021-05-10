package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2080107;
import com.dvnb.entities.Act2090710;
import com.dvnb.repositories.Act2090710Repo;


@Service("act2090710Service")
@Transactional
public class Act2090710ServiceImpl implements Act2090710Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090710Repo act2090710Repo;
	

	@Override
	public Page<Act2090710> findAll(Pageable page) {
		return act2090710Repo.findAll(page);
	}
	
	@Override
	public Page<Act2090710> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2090710Repo.findAllByKy(kyBaoCao, page);
	}
	
	@Override
	public Act2090710 findOneByKyBaoCao(String kyBaoCao) {
		return act2090710Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090710 act2090710) {

		act2090710Repo.save(act2090710);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090710Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090710Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
