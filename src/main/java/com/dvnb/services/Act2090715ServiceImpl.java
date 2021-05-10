package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090715;
import com.dvnb.repositories.Act2090715Repo;


@Service("act2090715Service")
@Transactional
public class Act2090715ServiceImpl implements Act2090715Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090715Repo act2090715Repo;
	

	@Override
	public Page<Act2090715> findAll(Pageable page) {
		return act2090715Repo.findAll(page);
	}
	
	@Override
	public Act2090715 findOneByKyBaoCao(String kyBaoCao) {
		return act2090715Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090715 act2090715) {

		act2090715Repo.save(act2090715);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090715Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090715Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
