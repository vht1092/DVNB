package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090701;
import com.dvnb.repositories.Act2090701Repo;


@Service("act2090701Service")
@Transactional
public class Act2090701ServiceImpl implements Act2090701Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090701Repo act2090701Repo;
	

	@Override
	public Page<Act2090701> findAll(Pageable page) {
		return act2090701Repo.findAll(page);
	}
	
	@Override
	public Act2090701 findOneByKyBaoCao(String kyBaoCao) {
		return act2090701Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090701 act2090701) {

		act2090701Repo.save(act2090701);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090701Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090701Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
