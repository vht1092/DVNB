package com.dvnb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbSysUser;

@Repository
public interface SysUserRepo extends JpaRepository<DvnbSysUser, String> {
	DvnbSysUser findByEmail(String email);

	List<DvnbSysUser> findAllUserByActiveflagIsTrue();
}
