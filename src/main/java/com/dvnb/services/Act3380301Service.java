package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3380301;

public interface Act3380301Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3380301> findAll(Pageable page);
	
	public Page<Act3380301> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3380301 act3380301);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
