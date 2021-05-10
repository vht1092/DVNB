package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2100202;

@Repository
public interface Act2100202Repo extends JpaRepository<Act2100202, String> {
	
	

	Page<Act2100202> findAll(Pageable page);
	
	Page<Act2100202> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2100202 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
