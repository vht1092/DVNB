package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2080107;
import com.dvnb.repositories.Act2080107Repo;


@Service("act2080107Service")
@Transactional
public class Act2080107ServiceImpl implements Act2080107Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080107Repo act2080107Repo;
	

	@Override
	public Page<Act2080107> findAll(Pageable page) {
		return act2080107Repo.findAll(page);
	}
	
	@Override
	public Act2080107 findOneByKyBaoCao(String kyBaoCao) {
		return act2080107Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2080107 act2080107) {

		act2080107Repo.save(act2080107);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080107Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080107Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
