package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390501;

public interface Act3390501Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390501> findAll(Pageable page);
	
	public Page<Act3390501> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390501 act3390501);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
	
}
