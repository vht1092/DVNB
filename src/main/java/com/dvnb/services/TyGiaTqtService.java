package com.dvnb.services;

import com.dvnb.entities.TyGiaTqt;

public interface TyGiaTqtService {

	public void save(TyGiaTqt tyGiaTqt);
	
	public TyGiaTqt findTyGiaTqtByNgayAdv(String ngayAdv);
	
	public TyGiaTqt findTyGiaTqtByNgayAdvAndCardType(String ngayAdv,String cardType);
}
