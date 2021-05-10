package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2100401;
import com.dvnb.entities.Act2100501;

@Repository
public interface Act2100501Repo extends JpaRepository<Act2100501, String> {
	
	

	Page<Act2100501> findAll(Pageable page);
	
	Page<Act2100501> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2100501 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
