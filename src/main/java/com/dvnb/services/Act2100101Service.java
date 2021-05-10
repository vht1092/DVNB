package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2090710;
import com.dvnb.entities.Act2100101;

public interface Act2100101Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2100101> findAll(Pageable page);
	
	public Page<Act2100101> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2100101 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2100101 act2100101);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
