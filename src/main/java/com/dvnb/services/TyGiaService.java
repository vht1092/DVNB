package com.dvnb.services;


import com.dvnb.entities.DvnbTyGia;

public interface TyGiaService {
	Iterable<DvnbTyGia> findAllByKy(String type);
	Iterable<DvnbTyGia> findAllByKyAndCardbrn(String type,String cardBrn);
	public void save(DvnbTyGia dvnbTyGia);
	
	public void deleteByKyAndCrdBrn(String ky, String crdBrn);
}
