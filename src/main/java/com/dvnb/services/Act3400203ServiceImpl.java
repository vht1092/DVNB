package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400203;
import com.dvnb.repositories.Act3400203Repo;


@Service("act3400203Service")
@Transactional
public class Act3400203ServiceImpl implements Act3400203Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400203Repo act3400203Repo;
	

	@Override
	public Page<Act3400203> findAll(Pageable page) {
		return act3400203Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400203> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400203Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400203 act3400203) {

		act3400203Repo.save(act3400203);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400203Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400203Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
