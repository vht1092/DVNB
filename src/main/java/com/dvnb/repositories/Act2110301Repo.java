package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110301;

@Repository
public interface Act2110301Repo extends JpaRepository<Act2110301, String> {
	
	

	Page<Act2110301> findAll(Pageable page);
	
	Page<Act2110301> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2110301 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
