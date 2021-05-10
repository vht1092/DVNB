package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090801;
import com.dvnb.repositories.Act2090801Repo;


@Service("act2090801Service")
@Transactional
public class Act2090801ServiceImpl implements Act2090801Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090801Repo act2090801Repo;
	

	@Override
	public Page<Act2090801> findAll(Pageable page) {
		return act2090801Repo.findAll(page);
	}
	
	@Override
	public Act2090801 findOneByKyBaoCao(String kyBaoCao) {
		return act2090801Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090801 act2090801) {

		act2090801Repo.save(act2090801);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090801Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090801Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
