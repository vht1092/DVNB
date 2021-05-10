package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2090709;
import com.dvnb.repositories.Act2090709Repo;


@Service("act2090709Service")
@Transactional
public class Act2090709ServiceImpl implements Act2090709Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2090709Repo act2090709Repo;
	

	@Override
	public Page<Act2090709> findAll(Pageable page) {
		return act2090709Repo.findAll(page);
	}
	
	@Override
	public Act2090709 findOneByKyBaoCao(String kyBaoCao) {
		return act2090709Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void create(Act2090709 act2090709) {

		act2090709Repo.save(act2090709);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2090709Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2090709Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
