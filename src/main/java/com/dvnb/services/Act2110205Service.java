package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110204;
import com.dvnb.entities.Act2110205;

public interface Act2110205Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2110205> findAll(Pageable page);
	
	public Page<Act2110205> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act2110205 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act2110205 act2110205);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
