package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390101b;

public interface Act3390101bService {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390101b> findAll(Pageable page);
	
	public Page<Act3390101b> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390101b act3390101);
	
	public void updateErpMappingByKybaocao(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
