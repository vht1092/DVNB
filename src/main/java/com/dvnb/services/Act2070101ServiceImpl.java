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

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act2070101;
import com.dvnb.repositories.Act2070101Repo;

import oracle.jdbc.OracleTypes;

@Service("act2070101Service")
@Transactional
public class Act2070101ServiceImpl implements Act2070101Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2070101Repo act2070101Repo;
	
	@Autowired
    private EntityManager em;
	
	@Autowired
	private DataSource dataSource;


	@Override
	public Page<Act2070101> findAll(Pageable page) {
		return act2070101Repo.findAll(page);
	}
	
	@Override
	public Page<Act2070101> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2070101Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2070101 act2070101) {

		act2070101Repo.save(act2070101);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2070101Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2070101Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2070101Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
