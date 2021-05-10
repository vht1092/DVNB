package com.dvnb.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbSysRoletxn;
import com.dvnb.repositories.SysRoleTxnRepo;

@Service("sysRoleTxnService")
@Transactional
public class SysRoleTxnServiceImpl implements SysRoleTxnService {

	@Autowired
	private SysRoleTxnRepo sysRoleTxnRepo;

	@Override
	public void save(int roleid, String txnid) {
		DvnbSysRoletxn fdsSysRoletxn = new DvnbSysRoletxn();
		fdsSysRoletxn.setIdrole(roleid);
		fdsSysRoletxn.setIdtxn(txnid);
		fdsSysRoletxn.setFlgauth(true);
		fdsSysRoletxn.setFlginit(true);
		fdsSysRoletxn.setFlgview(true);
		sysRoleTxnRepo.save(fdsSysRoletxn);
	}

	@Override
	public void deleteByRoleId(int roleid) {
		List<DvnbSysRoletxn> fdsSysRoletxn = sysRoleTxnRepo.findAllByIdrole(roleid);
		sysRoleTxnRepo.delete(fdsSysRoletxn);
	}
}
