package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2070105;

public interface Act2070105Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2070105> findAll(Pageable page);
	
	public Page<Act2070105> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2070105 act2070105);
	
	public void updateErpMappingByKybaocao(String ky);
	
	public void updateMaLoaiKHCN(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
