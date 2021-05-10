package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2100301;

public interface Act2100301Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2100301> findAll(Pageable page);
	
	public Page<Act2100301> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2100301 act2100301);
	
	public void updateErpMappingByKybaocao(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
