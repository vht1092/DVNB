package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090805;
import com.dvnb.repositories.Act2090805Repo;


@Service("act2090805Service")
@Transactional
public class Act2090805ServiceImpl implements Act2090805Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090805Repo act2090805Repo;
	

	@Override
	public Page<Act2090805> findAll(Pageable page) {
		return act2090805Repo.findAll(page);
	}
	
	@Override
	public Act2090805 findOneByKyBaoCao(String kyBaoCao) {
		return act2090805Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090805 act2090805) {

		act2090805Repo.save(act2090805);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090805Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090805Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
