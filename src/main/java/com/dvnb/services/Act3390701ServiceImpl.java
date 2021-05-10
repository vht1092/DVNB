package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390701;
import com.dvnb.repositories.Act3390701Repo;


@Service("act3390701Service")
@Transactional
public class Act3390701ServiceImpl implements Act3390701Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390701Repo act3390701Repo;
	

	@Override
	public Page<Act3390701> findAll(Pageable page) {
		return act3390701Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390701> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390701Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390701 act3390701) {

		act3390701Repo.save(act3390701);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390701Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390701Repo.deleteByKyAndUsrId(ky, userId);
	}

	@Override
	public void updateERPByLoaiThe(String ky) {
		act3390701Repo.updateERPByLoaiThe(ky);
		
	}
	
}
