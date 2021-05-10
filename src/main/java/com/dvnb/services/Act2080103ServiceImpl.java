package com.dvnb.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080103;
import com.dvnb.repositories.Act2080103Repo;

@Service("act2080103Service")
@Transactional
public class Act2080103ServiceImpl implements Act2080103Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080103Repo act2080103Repo;
	
	@Override
	public Page<Act2080103> findAll(Pageable page) {
		return act2080103Repo.findAll(page);
	}
	
	@Override
	public Page<Act2080103> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080103Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2080103 act2080103) {

		act2080103Repo.save(act2080103);
	}
	
	
	@Override
	public void importGiaoDichHitRuleNhom2(String fromdate,String todate,String usrid,String kybaocao) {
		act2080103Repo.importGiaoDichHitRuleNhom2(fromdate, todate, usrid, kybaocao);
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080103Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080103Repo.deleteByKyAndUsrId(ky, userId);
	}
}
