package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act1450101;
import com.dvnb.repositories.Act1450101Repo;


@Service("act1450101Service")
@Transactional
public class Act1450101ServiceImpl implements Act1450101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act1450101Repo act1450101Repo;
	

	@Override
	public Page<Act1450101> findAll(Pageable page) {
		return act1450101Repo.findAll(page);
	}
	
	@Override
	public Page<Act1450101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act1450101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act1450101 act1450101) {

		act1450101Repo.save(act1450101);
	}

	@Override
	public Act1450101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act1450101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act1450101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act1450101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
