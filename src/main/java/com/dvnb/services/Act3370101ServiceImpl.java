package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3370101;
import com.dvnb.repositories.Act3370101Repo;


@Service("act3370101Service")
@Transactional
public class Act3370101ServiceImpl implements Act3370101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3370101Repo act3370101Repo;
	

	@Override
	public Page<Act3370101> findAll(Pageable page) {
		return act3370101Repo.findAll(page);
	}
	
	@Override
	public Page<Act3370101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3370101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3370101 act3370101) {

		act3370101Repo.save(act3370101);
	}

	@Override
	public Act3370101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3370101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3370101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3370101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
