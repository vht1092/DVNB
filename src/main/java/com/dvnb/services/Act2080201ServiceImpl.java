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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2080201;
import com.dvnb.entities.Act2080301;
import com.dvnb.repositories.Act2080201Repo;

import oracle.jdbc.OracleTypes;

@Service("act2080201Service")
@Transactional
public class Act2080201ServiceImpl implements Act2080201Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080201Repo act2080201Repo;
	
	@Override
	public Page<Act2080201> findAll(Pageable page) {
		return act2080201Repo.findAll(page);
	}

	@Override
	public Page<Act2080201> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080201Repo.findAllByKy(kyBaoCao, page);
	}
	
	@Override
	public void create(Act2080201 act2080201) {

		act2080201Repo.save(act2080201);
	}

	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2080201Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080201Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080201Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}