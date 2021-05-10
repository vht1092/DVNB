package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390402;

public interface Act3390402Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390402> findAll(Pageable page);
	
	public Page<Act3390402> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390402 act3390402);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
	
	public void updateERPByLoaiThe(String ky);
}
