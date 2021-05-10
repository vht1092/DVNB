package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090806;
import com.dvnb.repositories.Act2090806Repo;


@Service("act2090806Service")
@Transactional
public class Act2090806ServiceImpl implements Act2090806Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090806Repo act2090806Repo;
	

	@Override
	public Page<Act2090806> findAll(Pageable page) {
		return act2090806Repo.findAll(page);
	}
	
	@Override
	public Act2090806 findOneByKyBaoCao(String kyBaoCao) {
		return act2090806Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090806 act2090806) {

		act2090806Repo.save(act2090806);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090806Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090806Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
