package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2100501;
import com.dvnb.repositories.Act2100501Repo;


@Service("act2100501Service")
@Transactional
public class Act2100501ServiceImpl implements Act2100501Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2100501Repo act2100501Repo;
	

	@Override
	public Page<Act2100501> findAll(Pageable page) {
		return act2100501Repo.findAll(page);
	}
	
	@Override
	public Page<Act2100501> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2100501Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2100501 act2100501) {

		act2100501Repo.save(act2100501);
	}

	@Override
	public Act2100501 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2100501Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2100501Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2100501Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
