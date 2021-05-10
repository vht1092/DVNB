package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.Act2080101;

public interface Act2080101Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act2080101> findAll(Pageable page);
	
	public Page<Act2080101> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act2080101 act2080101);
	
	public Act2080101 getERPMappingByLOC(BigDecimal loc); 
	
	public void updateErpMappingById(String id);
	
	public void updateErpMappingByKybaocao(String ky);
	
	public List<Act2080101> findGiaoDichHitRuleNhom1(String fromdate,String todate);
	
	void importGiaoDichHitRuleNhom1(String fromdate,String todate,String usrid,String kybaocao);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
