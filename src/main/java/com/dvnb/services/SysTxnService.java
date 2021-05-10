package com.dvnb.services;

import java.util.List;

import com.dvnb.entities.DvnbSysTxn;

public interface SysTxnService {
	List<Object[]> findAllByUserId(String id);
	List<Object[]> findAllByUserIdAndAppName(String id,String appName);
	List<Object[]> findAllByRoleId(int id);
	List<DvnbSysTxn> findAll();
}
