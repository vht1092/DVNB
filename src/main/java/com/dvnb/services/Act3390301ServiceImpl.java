package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390301;
import com.dvnb.repositories.Act3390301Repo;


@Service("act3390301Service")
@Transactional
public class Act3390301ServiceImpl implements Act3390301Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390301Repo act3390301Repo;
	

	@Override
	public Page<Act3390301> findAll(Pageable page) {
		return act3390301Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390301> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390301Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390301 act3390301) {

		act3390301Repo.save(act3390301);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390301Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390301Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
