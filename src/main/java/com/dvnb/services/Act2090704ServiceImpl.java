package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090704;
import com.dvnb.repositories.Act2090704Repo;


@Service("act2090704Service")
@Transactional
public class Act2090704ServiceImpl implements Act2090704Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090704Repo act2090704Repo;
	

	@Override
	public Page<Act2090704> findAll(Pageable page) {
		return act2090704Repo.findAll(page);
	}
	
	@Override
	public Act2090704 findOneByKyBaoCao(String kyBaoCao) {
		return act2090704Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090704 act2090704) {

		act2090704Repo.save(act2090704);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090704Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090704Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
