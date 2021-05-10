package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act2080103;
import com.dvnb.entities.Act2080105;

public interface Act2080105Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2080105> findAll(Pageable page);
	
	public Page<Act2080105> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2080105 act2080105);
	
	public void updateErpMappingByKybaocao(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
	
}
