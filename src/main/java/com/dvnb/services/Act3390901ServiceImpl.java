package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390901;
import com.dvnb.repositories.Act3390901Repo;


@Service("act3390901Service")
@Transactional
public class Act3390901ServiceImpl implements Act3390901Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390901Repo act3390901Repo;
	

	@Override
	public Page<Act3390901> findAll(Pageable page) {
		return act3390901Repo.findAll(page);
	}
	
	@Override
	public Page<Act3390901> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390901Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390901 act3390901) {

		act3390901Repo.save(act3390901);
	}

	@Override
	public void updateMaDonViByTidAtm(String ky) {
		act3390901Repo.updateMaDonViByTidAtm(ky);
	}

	@Override
	public void updateMaDonViByTidPos(String ky) {
		act3390901Repo.updateMaDonViByTidPos(ky);
		
	}
	
	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390901Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390901Repo.deleteByKyAndUsrId(ky, userId);
	}

	
	
}
