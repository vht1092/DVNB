package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3400205;
import com.dvnb.repositories.Act3400205Repo;


@Service("act3400205Service")
@Transactional
public class Act3400205ServiceImpl implements Act3400205Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3400205Repo act3400205Repo;
	

	@Override
	public Page<Act3400205> findAll(Pageable page) {
		return act3400205Repo.findAll(page);
	}
	
	@Override
	public Page<Act3400205> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3400205Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3400205 act3400205) {

		act3400205Repo.save(act3400205);
	}

	@Override
	public Act3400205 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3400205Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3400205Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3400205Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
