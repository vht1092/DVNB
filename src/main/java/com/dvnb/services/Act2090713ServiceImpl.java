package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090713;
import com.dvnb.repositories.Act2090713Repo;


@Service("act2090713Service")
@Transactional
public class Act2090713ServiceImpl implements Act2090713Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090713Repo act2090713Repo;
	

	@Override
	public Page<Act2090713> findAll(Pageable page) {
		return act2090713Repo.findAll(page);
	}
	
	@Override
	public Act2090713 findOneByKyBaoCao(String kyBaoCao) {
		return act2090713Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090713 act2090713) {

		act2090713Repo.save(act2090713);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090713Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090713Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
