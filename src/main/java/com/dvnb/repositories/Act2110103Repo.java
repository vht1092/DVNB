package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110103;

@Repository
public interface Act2110103Repo extends JpaRepository<Act2110103, String> {
	
	

	Page<Act2110103> findAll(Pageable page);
	
	Page<Act2110103> findAllByKy(@Param("ky") String ky, Pageable page);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
