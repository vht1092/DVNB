package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390501;
import com.dvnb.repositories.Act3390501Repo;


@Service("act3390501Service")
@Transactional
public class Act3390501ServiceImpl implements Act3390501Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390501Repo act3390501Repo;
	

	@Override
	public Page<Act3390501> findAll(Pageable page) {
		return act3390501Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390501> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390501Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390501 act3390501) {

		act3390501Repo.save(act3390501);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390501Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390501Repo.deleteByKyAndUsrId(ky, userId);
	}

}
