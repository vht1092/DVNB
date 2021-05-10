package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110102;
import com.dvnb.entities.Act3370102;

@Repository
public interface Act3370102Repo extends JpaRepository<Act3370102, String> {
	
	

	Page<Act3370102> findAll(Pageable page);
	
	Page<Act3370102> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act3370102 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
