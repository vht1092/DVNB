package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act1440101;
import com.dvnb.repositories.Act1440101Repo;


@Service("act1440101Service")
@Transactional
public class Act1440101ServiceImpl implements Act1440101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act1440101Repo act1440101Repo;
	

	@Override
	public Page<Act1440101> findAll(Pageable page) {
		return act1440101Repo.findAll(page);
	}
	
	@Override
	public Page<Act1440101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act1440101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act1440101 act1440101) {

		act1440101Repo.save(act1440101);
	}

	@Override
	public Act1440101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act1440101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act1440101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act1440101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
