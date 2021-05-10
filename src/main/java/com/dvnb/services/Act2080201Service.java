package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2080201;
import com.dvnb.entities.Act2080301;

public interface Act2080201Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2080201> findAll(Pageable page);
	
	public Page<Act2080201> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2080201 act2080201);
	
	public void updateErpMappingByKybaocao(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
