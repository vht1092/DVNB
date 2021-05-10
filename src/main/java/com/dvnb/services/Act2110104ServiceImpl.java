package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110104;
import com.dvnb.repositories.Act2110104Repo;


@Service("act2110104Service")
@Transactional
public class Act2110104ServiceImpl implements Act2110104Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110104Repo act2110104Repo;
	

	@Override
	public Page<Act2110104> findAll(Pageable page) {
		return act2110104Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110104> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110104Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110104 act2110104) {

		act2110104Repo.save(act2110104);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110104Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110104Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
