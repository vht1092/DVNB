package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390801;
import com.dvnb.repositories.Act3390801Repo;


@Service("act3390801Service")
@Transactional
public class Act3390801ServiceImpl implements Act3390801Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390801Repo act3390801Repo;
	

	@Override
	public Page<Act3390801> findAll(Pageable page) {
		return act3390801Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390801> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390801Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390801 act3390801) {

		act3390801Repo.save(act3390801);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390801Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390801Repo.deleteByKyAndUsrId(ky, userId);
	}

	@Override
	public void updateERPByLoaiThe(String ky) {
		act3390801Repo.updateERPByLoaiThe(ky);
		
	}
	
}
