package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3380201;
import com.dvnb.repositories.Act3380201Repo;


@Service("act3380201Service")
@Transactional
public class Act3380201ServiceImpl implements Act3380201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3380201Repo act3380201Repo;
	

	@Override
	public Page<Act3380201> findAll(Pageable page) {
		return act3380201Repo.findAll(page);
	}
	
	@Override
	public Page<Act3380201> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3380201Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3380201 act3380201) {

		act3380201Repo.save(act3380201);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3380201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3380201Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
