package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090705;
import com.dvnb.repositories.Act2090705Repo;


@Service("act2090705Service")
@Transactional
public class Act2090705ServiceImpl implements Act2090705Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090705Repo act2090705Repo;
	

	@Override
	public Page<Act2090705> findAll(Pageable page) {
		return act2090705Repo.findAll(page);
	}
	
	@Override
	public Act2090705 findOneByKyBaoCao(String kyBaoCao) {
		return act2090705Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090705 act2090705) {

		act2090705Repo.save(act2090705);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090705Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090705Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
