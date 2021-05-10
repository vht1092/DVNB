package com.dvnb.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbTyGia;
import com.dvnb.repositories.TyGiaRepo;

@Service("tyGiaService")
@Transactional
public class TyGiaServiceImpl implements TyGiaService {

	@Autowired
	private TyGiaRepo tyGiaRepo;

	@Override
	public Iterable<DvnbTyGia> findAllByKy(String ky) {
		return tyGiaRepo.findAllByKy(ky);
	}
	
	@Override
	public Iterable<DvnbTyGia> findAllByKyAndCardbrn(String ky,String cardBrn) {
		return tyGiaRepo.findAllByKyAndCrdBrn(ky,cardBrn);
	}
	
	
	@Override
	public void save(DvnbTyGia fdsTyGia) {

		tyGiaRepo.save(fdsTyGia);
	}

	@Override
	public void deleteByKyAndCrdBrn(String ky, String crdBrn) {
		// TODO Auto-generated method stub
		tyGiaRepo.deleteByKyAndCrdBrn(ky, crdBrn);
	}
	

}
