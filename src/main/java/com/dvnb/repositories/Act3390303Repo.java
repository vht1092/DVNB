package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3390303;

@Repository
public interface Act3390303Repo extends JpaRepository<Act3390303, String> {
	
	

	Page<Act3390303> findAll(Pageable page);
	
	Page<Act3390303> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act3390303 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
