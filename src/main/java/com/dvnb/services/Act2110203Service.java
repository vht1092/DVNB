package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110202;
import com.dvnb.entities.Act2110203;

public interface Act2110203Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2110203> findAll(Pageable page);
	
	public Page<Act2110203> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2110203 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2110203 act2110203);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
