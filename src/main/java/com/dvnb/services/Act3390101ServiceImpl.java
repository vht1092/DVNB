package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390101;
import com.dvnb.repositories.Act3390101Repo;


@Service("act3390101Service")
@Transactional
public class Act3390101ServiceImpl implements Act3390101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390101Repo act3390101Repo;
	

	@Override
	public Page<Act3390101> findAll(Pageable page) {
		return act3390101Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390101 act3390101) {

		act3390101Repo.save(act3390101);
	}

	@Override
	public Act3390101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3390101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
