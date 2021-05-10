package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090708;
import com.dvnb.repositories.Act2090708Repo;


@Service("act2090708Service")
@Transactional
public class Act2090708ServiceImpl implements Act2090708Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090708Repo act2090708Repo;
	

	@Override
	public Page<Act2090708> findAll(Pageable page) {
		return act2090708Repo.findAll(page);
	}
	
	@Override
	public Act2090708 findOneByKyBaoCao(String kyBaoCao) {
		return act2090708Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090708 act2090708) {

		act2090708Repo.save(act2090708);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090708Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090708Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
