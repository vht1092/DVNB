package com.dvnb.services;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.Act1420101;
import com.dvnb.entities.DsqtCurrency;
import com.dvnb.repositories.Act1420101Repo;
import com.dvnb.repositories.DsqtCurrencyRepo;


@Service("dsqtCurrencyService")
@Transactional
public class DsqtCurrencyServiceImpl implements DsqtCurrencyService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DsqtCurrencyRepo dsqtCurrencyRepo;
	@Override
	
	public DsqtCurrency findOneByCurrNum(String currnum) {
		return dsqtCurrencyRepo.findOneByCurrNum(currnum);
	}
	
	public List<DsqtCurrency> findAll(){
		return dsqtCurrencyRepo.findAll();
	}
	
}
