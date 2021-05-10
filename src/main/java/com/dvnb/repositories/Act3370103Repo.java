package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110102;
import com.dvnb.entities.Act3370103;

@Repository
public interface Act3370103Repo extends JpaRepository<Act3370103, String> {
	
	

	Page<Act3370103> findAll(Pageable page);
	
	Page<Act3370103> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act3370103 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
