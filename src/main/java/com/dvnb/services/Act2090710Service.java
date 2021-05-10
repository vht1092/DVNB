package com.dvnb.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2080107;
import com.dvnb.entities.Act2090710;

public interface Act2090710Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2090710> findAll(Pageable page);
	
	public Page<Act2090710> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2090710 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2090710 act2090710);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
