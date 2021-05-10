package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080102;

public interface Act2080102Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2080102> findAll(Pageable page);
	
	public Page<Act2080102> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2080102 act2080102);
	
	public void importGiaoDichEbankHitRuleNhom1(String usrId, String kyBaoCao) throws SQLException;
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
