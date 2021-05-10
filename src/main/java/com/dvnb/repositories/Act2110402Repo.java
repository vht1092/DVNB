package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110402;

@Repository
public interface Act2110402Repo extends JpaRepository<Act2110402, String> {
	
	

	Page<Act2110402> findAll(Pageable page);
	
	Page<Act2110402> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2110402 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
