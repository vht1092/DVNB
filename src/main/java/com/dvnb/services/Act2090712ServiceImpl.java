package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090712;
import com.dvnb.repositories.Act2090712Repo;


@Service("act2090712Service")
@Transactional
public class Act2090712ServiceImpl implements Act2090712Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090712Repo act2090712Repo;
	

	@Override
	public Page<Act2090712> findAll(Pageable page) {
		return act2090712Repo.findAll(page);
	}
	
	@Override
	public Act2090712 findOneByKyBaoCao(String kyBaoCao) {
		return act2090712Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090712 act2090712) {

		act2090712Repo.save(act2090712);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090712Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090712Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
