package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3370103;
import com.dvnb.repositories.Act3370103Repo;


@Service("act3370103Service")
@Transactional
public class Act3370103ServiceImpl implements Act3370103Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3370103Repo act3370103Repo;
	

	@Override
	public Page<Act3370103> findAll(Pageable page) {
		return act3370103Repo.findAll(page);
	}
	
	@Override
	public Page<Act3370103> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3370103Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3370103 act3370103) {

		act3370103Repo.save(act3370103);
	}

	@Override
	public Act3370103 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3370103Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3370103Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3370103Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
