package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080103;

public interface Act2080103Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2080103> findAll(Pageable page);
	
	public Page<Act2080103> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2080103 act2080103);
	
	void importGiaoDichHitRuleNhom2(String fromdate,String todate,String usrid,String kybaocao);

	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
