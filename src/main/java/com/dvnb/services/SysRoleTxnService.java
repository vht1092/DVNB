package com.dvnb.services;

public interface SysRoleTxnService {
	void save(int roleid, String txtid);
	void deleteByRoleId(int roleid);
}
