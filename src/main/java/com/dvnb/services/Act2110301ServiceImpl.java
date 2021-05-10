package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110301;
import com.dvnb.repositories.Act2110301Repo;


@Service("act2110301Service")
@Transactional
public class Act2110301ServiceImpl implements Act2110301Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110301Repo act2110301Repo;
	

	@Override
	public Page<Act2110301> findAll(Pageable page) {
		return act2110301Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110301> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110301Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110301 act2110301) {

		act2110301Repo.save(act2110301);
	}

	@Override
	public Act2110301 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110301Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110301Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110301Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
