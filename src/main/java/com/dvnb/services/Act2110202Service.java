package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110102;
import com.dvnb.entities.Act2110202;

public interface Act2110202Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2110202> findAll(Pageable page);
	
	public Page<Act2110202> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2110202 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2110202 act2110202);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
