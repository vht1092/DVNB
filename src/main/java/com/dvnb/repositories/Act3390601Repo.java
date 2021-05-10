package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3390601;

@Repository
public interface Act3390601Repo extends JpaRepository<Act3390601, String> {
	
	

	Page<Act3390601> findAll(Pageable page);
	
	Page<Act3390601> findAllByKy(@Param("ky") String ky, Pageable page);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
