package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390201;
import com.dvnb.repositories.Act3390201Repo;


@Service("act3390201Service")
@Transactional
public class Act3390201ServiceImpl implements Act3390201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390201Repo act3390201Repo;
	

	@Override
	public Page<Act3390201> findAll(Pageable page) {
		return act3390201Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390201> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390201Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390201 act3390201) {

		act3390201Repo.save(act3390201);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390201Repo.deleteByKyAndUsrId(ky, userId);
	}

	@Override
	public void updateERPByLoaiThe(String ky) {
		act3390201Repo.updateERPByLoaiThe(ky);
		
	}
	
}
