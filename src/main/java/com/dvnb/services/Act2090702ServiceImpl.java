package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090702;
import com.dvnb.repositories.Act2090702Repo;


@Service("act2090702Service")
@Transactional
public class Act2090702ServiceImpl implements Act2090702Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090702Repo act2090702Repo;
	

	@Override
	public Page<Act2090702> findAll(Pageable page) {
		return act2090702Repo.findAll(page);
	}
	
	@Override
	public Act2090702 findOneByKyBaoCao(String kyBaoCao) {
		return act2090702Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090702 act2090702) {

		act2090702Repo.save(act2090702);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090702Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090702Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
