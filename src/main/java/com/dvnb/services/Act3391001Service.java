package com.dvnb.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.Act3391001;

public interface Act3391001Service {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	public Page<Act3391001> findAll(Pageable page);
	
	public Page<Act3391001> findAllByKyBaoCao(String kyBaoCao, Pageable page);
	
	void create(Act3391001 act3391001);
	
	public void updateERPByTidAtm(String ky);
	
	public void updateERPByTidPos(String ky);
	
	public void updateERPByCif(String ky);
	
	void deleteByKyBaoCao(String ky);
	
	void deleteByKyBaoCaoAndUserId(String ky, String userId);
}
