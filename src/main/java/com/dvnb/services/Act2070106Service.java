package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2070106;

public interface Act2070106Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2070106> findAll(Pageable page);
	
	public Page<Act2070106> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2070106 act2070106);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
