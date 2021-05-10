package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2110102;
import com.dvnb.entities.Act3400102;

public interface Act3400102Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3400102> findAll(Pageable page);
	
	public Page<Act3400102> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	public Act3400102 findOneByKyBaoCao(String kyBaoCao);
	
	void create(Act3400102 act3400102);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
