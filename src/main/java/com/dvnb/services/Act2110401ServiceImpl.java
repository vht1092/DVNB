package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110401;
import com.dvnb.repositories.Act2110401Repo;


@Service("act2110401Service")
@Transactional
public class Act2110401ServiceImpl implements Act2110401Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110401Repo act2110401Repo;
	

	@Override
	public Page<Act2110401> findAll(Pageable page) {
		return act2110401Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110401> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110401Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110401 act2110401) {

		act2110401Repo.save(act2110401);
	}

	@Override
	public Act2110401 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110401Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110401Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110401Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
