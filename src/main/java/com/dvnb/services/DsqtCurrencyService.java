package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2100501;
import com.dvnb.entities.DsqtCurrency;
import com.dvnb.entities.Act1420101;

public interface DsqtCurrencyService {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public DsqtCurrency findOneByCurrNum(String currnum);
	
	public List<DsqtCurrency> findAll();
	
}
