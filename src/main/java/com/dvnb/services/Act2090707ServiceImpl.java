package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090707;
import com.dvnb.repositories.Act2090707Repo;


@Service("act2090707Service")
@Transactional
public class Act2090707ServiceImpl implements Act2090707Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090707Repo act2090707Repo;
	

	@Override
	public Page<Act2090707> findAll(Pageable page) {
		return act2090707Repo.findAll(page);
	}
	
	@Override
	public Act2090707 findOneByKyBaoCao(String kyBaoCao) {
		return act2090707Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090707 act2090707) {

		act2090707Repo.save(act2090707);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090707Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090707Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
