package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390401;

public interface Act3390401Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390401> findAll(Pageable page);
	
	public Page<Act3390401> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390401 act3390401);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
	
	public void updateERPByLoaiThe(String ky);
}
