package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3391001;
import com.dvnb.repositories.Act3391001Repo;


@Service("act3391001Service")
@Transactional
public class Act3391001ServiceImpl implements Act3391001Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3391001Repo act3391001Repo;
	

	@Override
	public Page<Act3391001> findAll(Pageable page) {
		return act3391001Repo.findAll(page);
	}
	
	@Override
	public Page<Act3391001> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3391001Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3391001 act3391001) {

		act3391001Repo.save(act3391001);
	}

	@Override
	public void updateERPByTidAtm(String ky) {
		act3391001Repo.updateERPByTidAtm(ky);
		
	}

	@Override
	public void updateERPByTidPos(String ky) {
		act3391001Repo.updateERPByTidPos(ky);
		
	}

	@Override
	public void updateERPByCif(String ky) {
		act3391001Repo.updateERPByCif(ky);
		
	}
	
	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3391001Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3391001Repo.deleteByKyAndUsrId(ky, userId);
	}

	
	
}
