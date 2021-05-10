package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110102;
import com.dvnb.entities.Act3400204;

public interface Act3400204Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3400204> findAll(Pageable page);
	
	public Page<Act3400204> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act3400204 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act3400204 act3400204);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
