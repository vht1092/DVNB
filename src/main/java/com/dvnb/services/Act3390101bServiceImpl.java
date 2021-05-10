package com.dvnb.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act3390101b;
import com.dvnb.repositories.Act3390101bRepo;


@Service("act3390101bService")
@Transactional
public class Act3390101bServiceImpl implements Act3390101bService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act3390101bRepo act3390101bRepo;
	

	@Override
	public Page<Act3390101b> findAll(Pageable page) {
		return act3390101bRepo.findAll(page);
	}
	
	@Override
	public Page<Act3390101b> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act3390101bRepo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act3390101b act3390101b) {

		act3390101bRepo.save(act3390101b);
	}

	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act3390101bRepo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act3390101bRepo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act3390101bRepo.deleteByKyAndUsrId(ky, userId);
	}
	
}
