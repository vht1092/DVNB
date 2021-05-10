package com.dvnb.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbSysUserrole;
import com.dvnb.entities.DvnbSysUserrolePK;
import com.dvnb.repositories.SysUserroleRepo;

@Service("sysUserroleService")
public class SysUserroleServiceImpl implements SysUserroleService {

	@Autowired
	private SysUserroleRepo sysUserroleRepo;

	@Override
	public void save(String iduser, int idrole) {
		DvnbSysUserrole fdsSysUserrole = new DvnbSysUserrole();
		fdsSysUserrole.setId(new DvnbSysUserrolePK(iduser, idrole));
		sysUserroleRepo.save(fdsSysUserrole);
	}

	@Override
	public List<DvnbSysUserrole> findAllByUserId(String iduser) {
		return sysUserroleRepo.findAllByIdUser(iduser);
	}

	@Override
	public void deleteByIduser(String iduser) {
		List<DvnbSysUserrole> fdsSysUserrole = sysUserroleRepo.findAllByIdUser(iduser);
		if (fdsSysUserrole != null) {
			sysUserroleRepo.delete(fdsSysUserrole);
		}
	}
	
	@Override
	public String findByRoleId(String userid) {
		return sysUserroleRepo.findByRoleID(userid);
	};

}
