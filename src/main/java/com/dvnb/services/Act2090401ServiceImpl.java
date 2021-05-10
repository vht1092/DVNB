package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090401;
import com.dvnb.repositories.Act2090401Repo;


@Service("act2090401Service")
@Transactional
public class Act2090401ServiceImpl implements Act2090401Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090401Repo act2090401Repo;
	

	@Override
	public Page<Act2090401> findAll(Pageable page) {
		return act2090401Repo.findAll(page);
	}
	
	@Override
	public Act2090401 findOneByKyBaoCao(String kyBaoCao) {
		return act2090401Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090401 act2090401) {

		act2090401Repo.save(act2090401);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090401Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090401Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
