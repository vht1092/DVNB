package com.dvnb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.Act2110204;
import com.dvnb.entities.Act2110205;

@Repository
public interface Act2110205Repo extends JpaRepository<Act2110205, String> {
	
	

	Page<Act2110205> findAll(Pageable page);
	
	Page<Act2110205> findAllByKy(@Param("ky") String ky, Pageable page);
	
	Act2110205 findOneByKy(@Param("ky") String ky);
	
	void deleteByKy(@Param("ky") String ky);
	
	void deleteByKyAndUsrId(@Param("ky") String ky,@Param("usrId") String usrId);
}
