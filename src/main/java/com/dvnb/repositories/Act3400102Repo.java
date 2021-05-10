package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act3400102;

@Repository
public interface Act3400102Repo extends JpaRepository<Act3400102, String> {
	
	

	Page<Act3400102> findAll(Pageable page);
	
	Page<Act3400102> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act3400102 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
