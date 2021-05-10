package com.dvnb.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.TimeConverter;
import com.dvnb.entities.DvnbSysUser;
import com.dvnb.entities.DvnbSysUserrole;
import com.dvnb.entities.DvnbSysUserrolePK;
import com.dvnb.repositories.SysUserRepo;
import com.dvnb.repositories.SysUserroleRepo;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserRepo sysUserRepo;

//	@Autowired
//	private SysRoleRepo roleRepository;
	@Autowired
	private SysUserroleRepo userroleRepo;
	private final TimeConverter timeConverter = new TimeConverter();

	@Override
	public DvnbSysUser findAllByEmail(String email) {
		return sysUserRepo.findByEmail(email);
	}

	@Override
	public DvnbSysUser findByUserid(String userid) {
		return sysUserRepo.findOne(userid);
	}

	@Override
	public String createNew(String userid, String email, String fullname) {
		DvnbSysUser sysUser = new DvnbSysUser();
		sysUser.setUserid(userid);
		sysUser.setActiveflag(true);// Mac dinh duoc active
		sysUser.setEmail(email);
		sysUser.setFullname(fullname);
		sysUser.setUsertype("OFF");// User mac dinh la officer
		sysUser.setCreatedate(timeConverter.getCurrentTime());
		sysUser.setLastlogin(timeConverter.getCurrentTime());
		sysUserRepo.save(sysUser);
		// Gan role mac dinh
//		List<GstsSysRole> listDefaultRole = roleRepository.findAllByDefaultroleIsTrue();
//
//		for (GstsSysRole list : listDefaultRole) {
//			GstsSysUserrolePK id = new GstsSysUserrolePK();
//			id.setIdrole(list.getId());
//			id.setIduser(sysUser.getUserid());
//			userroleRepo.save(new GstsSysUserrole(id));
//		}
		return sysUser.getUserid();
	}

	@Override
	public void updateLastLogin(String userid) {
		DvnbSysUser user = sysUserRepo.findOne(userid);
		user.setLastlogin(timeConverter.getCurrentTime());
		sysUserRepo.save(user);
	}

	@Override
	public List<DvnbSysUser> findAllUser() {
		return sysUserRepo.findAll();
	}

	@Override
	public void updateUserByUserId(String userid, String fullname, String usertype, Boolean active) {
		DvnbSysUser GstsSysUser = sysUserRepo.findOne(userid);
		if (GstsSysUser == null) {
			GstsSysUser = new DvnbSysUser();
			GstsSysUser.setUserid(userid);
		}
		GstsSysUser.setFullname(fullname);
		GstsSysUser.setUsertype(usertype);
		GstsSysUser.setActiveflag(active);
		GstsSysUser.setUpdatedate(timeConverter.getCurrentTime());
		sysUserRepo.save(GstsSysUser);

	}

	@Override
	public List<DvnbSysUser> findAllUserByActiveflagIsTrue() {
		return sysUserRepo.findAllUserByActiveflagIsTrue();
	}

}
