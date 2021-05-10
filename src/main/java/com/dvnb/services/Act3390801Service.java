package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390801;

public interface Act3390801Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390801> findAll(Pageable page);
	
	public Page<Act3390801> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390801 act3390801);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
	
	public void updateERPByLoaiThe(String ky);
}
