package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act1430101;
import com.dvnb.repositories.Act1430101Repo;


@Service("act1430101Service")
@Transactional
public class Act1430101ServiceImpl implements Act1430101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act1430101Repo act1430101Repo;
	

	@Override
	public Page<Act1430101> findAll(Pageable page) {
		return act1430101Repo.findAll(page);
	}
	
	@Override
	public Page<Act1430101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act1430101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act1430101 act1430101) {

		act1430101Repo.save(act1430101);
	}

	@Override
	public Act1430101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act1430101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act1430101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act1430101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
