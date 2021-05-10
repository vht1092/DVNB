package com.dvnb.services;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080106;
import com.dvnb.repositories.Act2080106Repo;

@Service("act2080106Service")
@Transactional
public class Act2080106ServiceImpl implements Act2080106Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080106Repo act2080106Repo;


	@Override
	public Page<Act2080106> findAll(Pageable page) {
		return act2080106Repo.findAll(page);
	}
	
	@Override
	public Page<Act2080106> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080106Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2080106 act2080106) {

		act2080106Repo.save(act2080106);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2080106Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080106Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080106Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
