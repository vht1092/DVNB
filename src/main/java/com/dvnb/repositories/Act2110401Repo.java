package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110401;

@Repository
public interface Act2110401Repo extends JpaRepository<Act2110401, String> {
	
	

	Page<Act2110401> findAll(Pageable page);
	
	Page<Act2110401> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2110401 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
	
}
