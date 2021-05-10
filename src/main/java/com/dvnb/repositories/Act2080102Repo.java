package com.dvnb.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dvnb.entities.Act2080101;
import com.dvnb.entities.Act2080102;

@Repository
public interface Act2080102Repo extends JpaRepository<Act2080102, String> {
	
	Page<Act2080102> findAll(Pageable page);
	
	Page<Act2080102> findAllByKy(@Param("ky") String ky, Pageable page);

	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
