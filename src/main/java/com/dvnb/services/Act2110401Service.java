package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110205;
import com.dvnb.entities.Act2110401;

public interface Act2110401Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2110401> findAll(Pageable page);
	
	public Page<Act2110401> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2110401 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2110401 act2110401);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
