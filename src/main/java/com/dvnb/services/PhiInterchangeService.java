package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.PhiInterchange;

public interface PhiInterchangeService {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	
	public List<PhiInterchange> phiInterchangeGDRTMDuocTQT(String curr, String ngayAdv, String cardtype) throws SQLException;
	
	public List<PhiInterchange> phiInterchangeGDMoneySendFF(String curr, String ngayAdv, String cardtype) throws SQLException;
	
	public List<PhiInterchange> phiInterchangeHoanTraGDRTM(String curr, String ngayAdv, String cardtype) throws SQLException;
	
}