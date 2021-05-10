package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act1450201;

@Repository
public interface Act1450201Repo extends JpaRepository<Act1450201, String> {
	
	

	Page<Act1450201> findAll(Pageable page);
	
	Page<Act1450201> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act1450201 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
