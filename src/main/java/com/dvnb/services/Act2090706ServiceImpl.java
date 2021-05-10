package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090706;
import com.dvnb.repositories.Act2090706Repo;


@Service("act2090706Service")
@Transactional
public class Act2090706ServiceImpl implements Act2090706Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090706Repo act2090706Repo;
	

	@Override
	public Page<Act2090706> findAll(Pageable page) {
		return act2090706Repo.findAll(page);
	}
	
	@Override
	public Act2090706 findOneByKyBaoCao(String kyBaoCao) {
		return act2090706Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090706 act2090706) {

		act2090706Repo.save(act2090706);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090706Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090706Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
