package com.dvnb.services;

import java.util.List;

import com.dvnb.entities.DvnbSysUserrole;

public interface SysUserroleService {
	void save(String iduser, int idrole);
	void deleteByIduser(String iduser);
	List<DvnbSysUserrole> findAllByUserId(String iduser);	
	String findByRoleId(String userid);
}
