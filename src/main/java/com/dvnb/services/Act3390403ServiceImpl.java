package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390403;
import com.dvnb.repositories.Act3390403Repo;


@Service("act3390403Service")
@Transactional
public class Act3390403ServiceImpl implements Act3390403Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390403Repo act3390403Repo;
	

	@Override
	public Page<Act3390403> findAll(Pageable page) {
		return act3390403Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390403> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390403Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390403 act3390403) {

		act3390403Repo.save(act3390403);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390403Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390403Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
