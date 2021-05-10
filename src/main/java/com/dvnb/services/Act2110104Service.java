package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2110104;

public interface Act2110104Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2110104> findAll(Pageable page);
	
	public Page<Act2110104> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2110104 act2110104);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
