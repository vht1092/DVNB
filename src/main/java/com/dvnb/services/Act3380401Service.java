package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3380401;

public interface Act3380401Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3380401> findAll(Pageable page);
	
	public Page<Act3380401> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3380401 act3380401);
	
	public void updateErpMappingByKybaocao(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
