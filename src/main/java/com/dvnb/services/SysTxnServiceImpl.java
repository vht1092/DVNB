package com.dvnb.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbSysTxn;
import com.dvnb.repositories.SysTxnRepo;

@Service("sysTxnService")
@Transactional
public class SysTxnServiceImpl implements SysTxnService {

	@Autowired
	private SysTxnRepo sysTxnRepo;
	// private List<FdsSysTxn> listFdsSysTxn;

	@Override
	public List<Object[]> findAllByUserId(String id) {
		return sysTxnRepo.findAllByUserId(id);
	}
	
	@Override
	public List<Object[]> findAllByUserIdAndAppName(String id, String appName) {
		return sysTxnRepo.findAllByUserIdAndCaption(id,appName);
	}

	// @Override
	// public List<FdsSysTxn> findAllByRoleId(String id) {
	// listFdsSysTxn = new ArrayList<FdsSysTxn>();
	// List<Object[]> listObject = sysTxnRepo.findAllByRoleId(id);
	// for (Object[] a : listObject) {
	// listFdsSysTxn.add(new FdsSysTxn(a[0] != null ? a[0].toString() : "", a[1]
	// != null ? a[1].toString() : "", a[2] != null ? a[2].toString() : "",
	// a[3] != null ? a[3].toString() : "", a[4] != null ? a[4].toString() : "",
	// a[5] != null ? a[5].toString() : "",
	// Integer.parseInt(a[6] != null ? a[6].toString() : "0")));
	// }
	// return listFdsSysTxn;
	// }

	@Override
	public List<Object[]> findAllByRoleId(int id) {

		return sysTxnRepo.findAllByRoleId(id);
	}

	@Override
	public List<DvnbSysTxn> findAll() {
		return sysTxnRepo.findAll();
	}

}
