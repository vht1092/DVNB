package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2100501;
import com.dvnb.entities.Act2100502;

public interface Act2100502Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2100502> findAll(Pageable page);
	
	public Page<Act2100502> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2100502 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2100502 act2100502);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
