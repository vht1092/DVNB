package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390401;
import com.dvnb.repositories.Act3390401Repo;


@Service("act3390401Service")
@Transactional
public class Act3390401ServiceImpl implements Act3390401Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390401Repo act3390401Repo;
	

	@Override
	public Page<Act3390401> findAll(Pageable page) {
		return act3390401Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390401> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390401Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390401 act3390401) {

		act3390401Repo.save(act3390401);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390401Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390401Repo.deleteByKyAndUsrId(ky, userId);
	}

	@Override
	public void updateERPByLoaiThe(String ky) {
		act3390401Repo.updateERPByLoaiThe(ky);
		
	}
	
}
