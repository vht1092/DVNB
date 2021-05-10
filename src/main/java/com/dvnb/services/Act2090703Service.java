package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2090703;

public interface Act2090703Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2090703> findAll(Pageable page);
	
	public Act2090703 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2090703 act2090703);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
