package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2070104;

public interface Act2070104Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2070104> findAll(Pageable page);
	
	public Page<Act2070104> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2070104 act2070104);
	
	public void updateErpMappingByKybaocao(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
