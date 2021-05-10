package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2110205;
import com.dvnb.repositories.Act2110205Repo;


@Service("act2110205Service")
@Transactional
public class Act2110205ServiceImpl implements Act2110205Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2110205Repo act2110205Repo;
	

	@Override
	public Page<Act2110205> findAll(Pageable page) {
		return act2110205Repo.findAll(page);
	}
	
	@Override
	public Page<Act2110205> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2110205Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2110205 act2110205) {

		act2110205Repo.save(act2110205);
	}

	@Override
	public Act2110205 findOneByKyBaoCao(String kyBaoCao) {
		// TODO Auto-generated method stub
		return act2110205Repo.findOneByKy(kyBaoCao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2110205Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2110205Repo.deleteByKyAndUsrId(ky, userId);
	}
	
}
