package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3400202;

public interface Act3400202Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3400202> findAll(Pageable page);
	
	public Page<Act3400202> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3400202 act3400202);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
