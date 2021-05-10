package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110204;
import com.dvnb.repositories.Act2110204Repo;


@Service("act2110204Service")
@Transactional
public class Act2110204ServiceImpl implements Act2110204Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110204Repo act2110204Repo;
	

	@Override
	public Page<Act2110204> findAll(Pageable page) {
		return act2110204Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110204> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110204Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110204 act2110204) {

		act2110204Repo.save(act2110204);
	}

	@Override
	public Act2110204 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110204Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110204Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110204Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
