package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110102;
import com.dvnb.repositories.Act2110102Repo;


@Service("act2110102Service")
@Transactional
public class Act2110102ServiceImpl implements Act2110102Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110102Repo act2110102Repo;
	

	@Override
	public Page<Act2110102> findAll(Pageable page) {
		return act2110102Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110102> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110102Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110102 act2110102) {

		act2110102Repo.save(act2110102);
	}

	@Override
	public Act2110102 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110102Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110102Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110102Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
