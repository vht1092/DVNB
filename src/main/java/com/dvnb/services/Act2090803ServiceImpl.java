package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090803;
import com.dvnb.repositories.Act2090803Repo;


@Service("act2090803Service")
@Transactional
public class Act2090803ServiceImpl implements Act2090803Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090803Repo act2090803Repo;
	

	@Override
	public Page<Act2090803> findAll(Pageable page) {
		return act2090803Repo.findAll(page);
	}
	
	@Override
	public Act2090803 findOneByKyBaoCao(String kyBaoCao) {
		return act2090803Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090803 act2090803) {

		act2090803Repo.save(act2090803);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090803Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090803Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
