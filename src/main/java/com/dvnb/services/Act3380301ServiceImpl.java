package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3380301;
import com.dvnb.repositories.Act3380301Repo;


@Service("act3380301Service")
@Transactional
public class Act3380301ServiceImpl implements Act3380301Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3380301Repo act3380301Repo;
	

	@Override
	public Page<Act3380301> findAll(Pageable page) {
		return act3380301Repo.findAll(page);
	}
	
	@Override
	public Page<Act3380301> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3380301Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3380301 act3380301) {

		act3380301Repo.save(act3380301);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3380301Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3380301Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
