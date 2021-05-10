package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3380401;
import com.dvnb.repositories.Act3380401Repo;


@Service("act3380401Service")
@Transactional
public class Act3380401ServiceImpl implements Act3380401Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3380401Repo act3380401Repo;
	

	@Override
	public Page<Act3380401> findAll(Pageable page) {
		return act3380401Repo.findAll(page);
	}
	
	@Override
	public Page<Act3380401> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3380401Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3380401 act3380401) {

		act3380401Repo.save(act3380401);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act3380401Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3380401Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3380401Repo.deleteByKyAndUsrId(ky, userId);
	} 
}
