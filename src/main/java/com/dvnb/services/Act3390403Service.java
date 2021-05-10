package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390403;

public interface Act3390403Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390403> findAll(Pageable page);
	
	public Page<Act3390403> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390403 act3390403);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
