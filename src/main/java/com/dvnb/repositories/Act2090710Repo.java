package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2080107;
import com.dvnb.entities.Act2090710;

@Repository
public interface Act2090710Repo extends JpaRepository<Act2090710, String> {
	
	

	Page<Act2090710> findAll(Pageable page);
	
	Page<Act2090710> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2090710 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
