package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090711;
import com.dvnb.repositories.Act2090711Repo;


@Service("act2090711Service")
@Transactional
public class Act2090711ServiceImpl implements Act2090711Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090711Repo act2090711Repo;
	

	@Override
	public Page<Act2090711> findAll(Pageable page) {
		return act2090711Repo.findAll(page);
	}
	
	@Override
	public Act2090711 findOneByKyBaoCao(String kyBaoCao) {
		return act2090711Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090711 act2090711) {

		act2090711Repo.save(act2090711);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090711Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090711Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
