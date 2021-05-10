package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090703;
import com.dvnb.repositories.Act2090703Repo;


@Service("act2090703Service")
@Transactional
public class Act2090703ServiceImpl implements Act2090703Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090703Repo act2090703Repo;
	

	@Override
	public Page<Act2090703> findAll(Pageable page) {
		return act2090703Repo.findAll(page);
	}
	
	@Override
	public Act2090703 findOneByKyBaoCao(String kyBaoCao) {
		return act2090703Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090703 act2090703) {

		act2090703Repo.save(act2090703);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090703Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090703Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
