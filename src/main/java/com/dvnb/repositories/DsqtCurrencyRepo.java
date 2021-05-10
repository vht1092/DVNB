package com.dvnb.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2100501;
import com.dvnb.entities.DsqtCurrency;
import com.dvnb.entities.Act1420101;

@Repository
public interface DsqtCurrencyRepo extends JpaRepository<DsqtCurrency, String> {
	
	DsqtCurrency findOneByCurrNum(@Param("currnum") String currnum);
	
}
