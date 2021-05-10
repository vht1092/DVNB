package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090301;
import com.dvnb.repositories.Act2090301Repo;


@Service("act2090301Service")
@Transactional
public class Act2090301ServiceImpl implements Act2090301Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090301Repo act2090301Repo;
	

	@Override
	public Page<Act2090301> findAll(Pageable page) {
		return act2090301Repo.findAll(page);
	}
	
	@Override
	public Act2090301 findOneByKyBaoCao(String kyBaoCao) {
		return act2090301Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090301 act2090301) {

		act2090301Repo.save(act2090301);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090301Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090301Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
