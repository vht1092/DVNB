package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act1450201;
import com.dvnb.repositories.Act1450201Repo;


@Service("act1450201Service")
@Transactional
public class Act1450201ServiceImpl implements Act1450201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act1450201Repo act1450201Repo;
	

	@Override
	public Page<Act1450201> findAll(Pageable page) {
		return act1450201Repo.findAll(page);
	}
	
	@Override
	public Page<Act1450201> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act1450201Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act1450201 act1450201) {

		act1450201Repo.save(act1450201);
	}

	@Override
	public Act1450201 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act1450201Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act1450201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act1450201Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
