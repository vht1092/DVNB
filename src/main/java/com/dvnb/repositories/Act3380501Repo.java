package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3380501;

@Repository
public interface Act3380501Repo extends JpaRepository<Act3380501, String> {
	
	

	Page<Act3380501> findAll(Pageable page);
	
	Page<Act3380501> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act3380501 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
