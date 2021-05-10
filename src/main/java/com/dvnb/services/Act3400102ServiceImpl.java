package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400102;
import com.dvnb.repositories.Act3400102Repo;


@Service("act3400102Service")
@Transactional
public class Act3400102ServiceImpl implements Act3400102Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400102Repo act3400102Repo;
	

	@Override
	public Page<Act3400102> findAll(Pageable page) {
		return act3400102Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400102> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400102Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400102 act3400102) {

		act3400102Repo.save(act3400102);
	}

	@Override
	public Act3400102 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3400102Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400102Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400102Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
