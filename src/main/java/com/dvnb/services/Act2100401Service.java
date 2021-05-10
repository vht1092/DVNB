package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2100101;
import com.dvnb.entities.Act2100401;

public interface Act2100401Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2100401> findAll(Pageable page);
	
	public Page<Act2100401> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2100401 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2100401 act2100401);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
