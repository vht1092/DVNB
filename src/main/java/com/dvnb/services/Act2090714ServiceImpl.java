package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090714;
import com.dvnb.repositories.Act2090714Repo;


@Service("act2090714Service")
@Transactional
public class Act2090714ServiceImpl implements Act2090714Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090714Repo act2090714Repo;
	

	@Override
	public Page<Act2090714> findAll(Pageable page) {
		return act2090714Repo.findAll(page);
	}
	
	@Override
	public Act2090714 findOneByKyBaoCao(String kyBaoCao) {
		return act2090714Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090714 act2090714) {

		act2090714Repo.save(act2090714);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090714Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090714Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
