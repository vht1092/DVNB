package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090804;
import com.dvnb.repositories.Act2090804Repo;


@Service("act2090804Service")
@Transactional
public class Act2090804ServiceImpl implements Act2090804Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090804Repo act2090804Repo;
	

	@Override
	public Page<Act2090804> findAll(Pageable page) {
		return act2090804Repo.findAll(page);
	}
	
	@Override
	public Act2090804 findOneByKyBaoCao(String kyBaoCao) {
		return act2090804Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090804 act2090804) {

		act2090804Repo.save(act2090804);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090804Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090804Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
