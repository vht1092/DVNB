package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400204;
import com.dvnb.repositories.Act3400204Repo;


@Service("act3400204Service")
@Transactional
public class Act3400204ServiceImpl implements Act3400204Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400204Repo act3400204Repo;
	

	@Override
	public Page<Act3400204> findAll(Pageable page) {
		return act3400204Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400204> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400204Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400204 act3400204) {

		act3400204Repo.save(act3400204);
	}

	@Override
	public Act3400204 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3400204Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400204Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400204Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
