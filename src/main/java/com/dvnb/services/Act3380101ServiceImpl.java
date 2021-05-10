package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3380101;
import com.dvnb.repositories.Act3380101Repo;


@Service("act3380101Service")
@Transactional
public class Act3380101ServiceImpl implements Act3380101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3380101Repo act3380101Repo;
	

	@Override
	public Page<Act3380101> findAll(Pageable page) {
		return act3380101Repo.findAll(page);
	}
	
	@Override
	public Page<Act3380101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3380101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3380101 act3380101) {

		act3380101Repo.save(act3380101);
	}

	@Override
	public Act3380101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3380101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3380101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3380101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
