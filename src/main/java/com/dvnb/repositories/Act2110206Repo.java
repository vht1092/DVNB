package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110205;
import com.dvnb.entities.Act2110206;

@Repository
public interface Act2110206Repo extends JpaRepository<Act2110206, String> {
	
	

	Page<Act2110206> findAll(Pageable page);
	
	Page<Act2110206> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2110206 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
