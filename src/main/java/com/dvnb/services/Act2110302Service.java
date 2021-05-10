package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110205;
import com.dvnb.entities.Act2110302;

public interface Act2110302Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2110302> findAll(Pageable page);
	
	public Page<Act2110302> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2110302 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2110302 act2110302);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
