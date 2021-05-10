package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2100401;
import com.dvnb.entities.Act2100501;

public interface Act2100501Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2100501> findAll(Pageable page);
	
	public Page<Act2100501> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2100501 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2100501 act2100501);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
