package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2100601;

@Repository
public interface Act2100601Repo extends JpaRepository<Act2100601, String> {
	
	

	Page<Act2100601> findAll(Pageable page);
	
	Page<Act2100601> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2100601 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
