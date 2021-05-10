package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3370104;
import com.dvnb.repositories.Act3370104Repo;


@Service("act3370104Service")
@Transactional
public class Act3370104ServiceImpl implements Act3370104Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3370104Repo act3370104Repo;
	

	@Override
	public Page<Act3370104> findAll(Pageable page) {
		return act3370104Repo.findAll(page);
	}
	
	@Override
	public Page<Act3370104> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3370104Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3370104 act3370104) {

		act3370104Repo.save(act3370104);
	}

	@Override
	public Act3370104 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act3370104Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3370104Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3370104Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
