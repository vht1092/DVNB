package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2090801;

public interface Act2090801Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2090801> findAll(Pageable page);
	
	public Act2090801 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2090801 act2090801);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
