package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090501;
import com.dvnb.repositories.Act2090501Repo;


@Service("act2090501Service")
@Transactional
public class Act2090501ServiceImpl implements Act2090501Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090501Repo act2090501Repo;
	

	@Override
	public Page<Act2090501> findAll(Pageable page) {
		return act2090501Repo.findAll(page);
	}
	
	@Override
	public Act2090501 findOneByKyBaoCao(String kyBaoCao) {
		return act2090501Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090501 act2090501) {

		act2090501Repo.save(act2090501);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090501Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090501Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
