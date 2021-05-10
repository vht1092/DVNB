package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390601;

public interface Act3390601Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390601> findAll(Pageable page);
	
	public Page<Act3390601> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390601 act3390601);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
