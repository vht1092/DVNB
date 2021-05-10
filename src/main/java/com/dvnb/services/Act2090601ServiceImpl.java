package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090601;
import com.dvnb.repositories.Act2090601Repo;


@Service("act2090601Service")
@Transactional
public class Act2090601ServiceImpl implements Act2090601Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090601Repo act2090601Repo;
	

	@Override
	public Page<Act2090601> findAll(Pageable page) {
		return act2090601Repo.findAll(page);
	}
	
	@Override
	public Act2090601 findOneByKyBaoCao(String kyBaoCao) {
		return act2090601Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090601 act2090601) {

		act2090601Repo.save(act2090601);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090601Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090601Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
