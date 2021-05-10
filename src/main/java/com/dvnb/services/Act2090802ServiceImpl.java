package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090802;
import com.dvnb.repositories.Act2090802Repo;


@Service("act2090802Service")
@Transactional
public class Act2090802ServiceImpl implements Act2090802Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090802Repo act2090802Repo;
	

	@Override
	public Page<Act2090802> findAll(Pageable page) {
		return act2090802Repo.findAll(page);
	}
	
	@Override
	public Act2090802 findOneByKyBaoCao(String kyBaoCao) {
		return act2090802Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090802 act2090802) {

		act2090802Repo.save(act2090802);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090802Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090802Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
