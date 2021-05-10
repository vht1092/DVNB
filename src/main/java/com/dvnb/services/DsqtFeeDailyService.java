package com.dvnb.services;

import java.util.List;

import com.dvnb.entities.DsqtFeeDaily;

public interface DsqtFeeDailyService {

	public void save(DsqtFeeDaily feeDaily);
	
	public List<DsqtFeeDaily> findAllByNgayHachToan(String ngayHachToan);
}
