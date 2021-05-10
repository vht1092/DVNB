package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2090705;

public interface Act2090705Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2090705> findAll(Pageable page);
	
	public Act2090705 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2090705 act2090705);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
