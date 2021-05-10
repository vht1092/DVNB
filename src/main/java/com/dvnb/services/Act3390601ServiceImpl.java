package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390601;
import com.dvnb.repositories.Act3390601Repo;


@Service("act3390601Service")
@Transactional
public class Act3390601ServiceImpl implements Act3390601Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390601Repo act3390601Repo;
	

	@Override
	public Page<Act3390601> findAll(Pageable page) {
		return act3390601Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390601> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390601Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390601 act3390601) {

		act3390601Repo.save(act3390601);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390601Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390601Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
