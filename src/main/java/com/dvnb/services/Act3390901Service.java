package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3390901;

public interface Act3390901Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3390901> findAll(Pageable page);
	
	public Page<Act3390901> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3390901 act3390901);
	
	public void updateMaDonViByTidAtm(String ky);
	
	public void updateMaDonViByTidPos(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
