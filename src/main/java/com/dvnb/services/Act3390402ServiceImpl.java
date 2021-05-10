package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390402;
import com.dvnb.repositories.Act3390402Repo;


@Service("act3390402Service")
@Transactional
public class Act3390402ServiceImpl implements Act3390402Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390402Repo act3390402Repo;
	

	@Override
	public Page<Act3390402> findAll(Pageable page) {
		return act3390402Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390402> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390402Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390402 act3390402) {

		act3390402Repo.save(act3390402);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390402Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390402Repo.deleteByKyAndUsrId(ky, userId);
	}

	@Override
	public void updateERPByLoaiThe(String ky) {
		act3390402Repo.updateERPByLoaiThe(ky);
		
	}
	
}
