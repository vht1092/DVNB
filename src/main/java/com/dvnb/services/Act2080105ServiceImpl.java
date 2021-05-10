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
import com.dvnb.entities.Act2080105;
import com.dvnb.repositories.Act2080105Repo;

import oracle.jdbc.OracleTypes;

@Service("act2080105Service")
@Transactional
public class Act2080105ServiceImpl implements Act2080105Service {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private Act2080105Repo act2080105Repo;
	
	@Autowired
    private EntityManager em;
	
	@Autowired
	private DataSource dataSource;


	@Override
	public Page<Act2080105> findAll(Pageable page) {
		return act2080105Repo.findAll(page);
	}
	
	@Override
	public Page<Act2080105> findAllByKyBaoCao(String kyBaoCao, Pageable page) {
		return act2080105Repo.findAllByKy(kyBaoCao, page);
	}

	@Override
	public void create(Act2080105 act2080105) {

		act2080105Repo.save(act2080105);
	}
	
	@Override
	public void updateErpMappingByKybaocao(String ky) {
		// TODO Auto-generated method stub
		act2080105Repo.updateErpMappingByKybaocao(ky);
		
	}

	@Override
	public void deleteByKyBaoCao(String ky) {
		// TODO Auto-generated method stub
		act2080105Repo.deleteByKy(ky);
	}

	@Override
	public void deleteByKyBaoCaoAndUserId(String ky, String userId) {
		// TODO Auto-generated method stub
		act2080105Repo.deleteByKyAndUsrId(ky, userId);
	} 
	
}
