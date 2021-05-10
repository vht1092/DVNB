package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act1420101;
import com.dvnb.repositories.Act1420101Repo;


@Service("act1420101Service")
@Transactional
public class Act1420101ServiceImpl implements Act1420101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act1420101Repo act1420101Repo;
	

	@Override
	public Page<Act1420101> findAll(Pageable page) {
		return act1420101Repo.findAll(page);
	}
	
	@Override
	public Page<Act1420101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act1420101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act1420101 act1420101) {

		act1420101Repo.save(act1420101);
	}

	@Override
	public Act1420101 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act1420101Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act1420101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act1420101Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
