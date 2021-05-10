package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3400203;

public interface Act3400203Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3400203> findAll(Pageable page);
	
	public Page<Act3400203> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3400203 act3400203);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
