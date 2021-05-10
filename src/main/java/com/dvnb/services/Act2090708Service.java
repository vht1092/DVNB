package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2090708;

public interface Act2090708Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2090708> findAll(Pageable page);
	
	public Act2090708 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2090708 act2090708);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
