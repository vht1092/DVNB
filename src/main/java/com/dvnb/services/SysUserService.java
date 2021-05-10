package com.dvnb.services;

import java.util.List;

import com.dvnb.entities.DvnbSysUser;

public interface SysUserService {
	public DvnbSysUser findAllByEmail(String email);

	public List<DvnbSysUser> findAllUser();
	
	public List<DvnbSysUser> findAllUserByActiveflagIsTrue();

	public String createNew(String userid, String email, String fullname);

	public void updateLastLogin(String userid);

	public void updateUserByUserId(String userid, String fullname, String usertype, Boolean active);

	public DvnbSysUser findByUserid(String userid);
}
