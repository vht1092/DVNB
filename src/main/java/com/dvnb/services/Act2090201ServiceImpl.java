package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090201;
import com.dvnb.repositories.Act2090201Repo;


@Service("act2090201Service")
@Transactional
public class Act2090201ServiceImpl implements Act2090201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090201Repo act2090201Repo;
	

	@Override
	public Page<Act2090201> findAll(Pageable page) {
		return act2090201Repo.findAll(page);
	}
	
	@Override
	public Act2090201 findOneByKyBaoCao(String kyBaoCao) {
		return act2090201Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090201 act2090201) {

		act2090201Repo.save(act2090201);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090201Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
