package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390302;
import com.dvnb.repositories.Act3390302Repo;


@Service("act3390302Service")
@Transactional
public class Act3390302ServiceImpl implements Act3390302Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390302Repo act3390302Repo;
	

	@Override
	public Page<Act3390302> findAll(Pageable page) {
		return act3390302Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390302> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390302Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390302 act3390302) {

		act3390302Repo.save(act3390302);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390302Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390302Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
