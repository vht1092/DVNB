package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090101;
import com.dvnb.repositories.Act2090101Repo;


@Service("act2090101Service")
@Transactional
public class Act2090101ServiceImpl implements Act2090101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090101Repo act2090101Repo;
	

	@Override
	public Page<Act2090101> findAll(Pageable page) {
		return act2090101Repo.findAll(page);
	}
	
	@Override
	public Act2090101 findOneByKyBaoCao(String kyBaoCao) {
		return act2090101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090101 act2090101) {

		act2090101Repo.save(act2090101);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
