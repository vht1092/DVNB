package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400101;
import com.dvnb.repositories.Act3400101Repo;


@Service("act3400101Service")
@Transactional
public class Act3400101ServiceImpl implements Act3400101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400101Repo act3400101Repo;
	

	@Override
	public Page<Act3400101> findAll(Pageable page) {
		return act3400101Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400101 act3400101) {

		act3400101Repo.save(act3400101);
	}

	@Override
	public Act3400101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3400101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
