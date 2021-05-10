package com.dvnb.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbSysRoletxn;

@Repository
public interface SysRoleTxnRepo extends CrudRepository<DvnbSysRoletxn, Long> {
	List<DvnbSysRoletxn> findAllByIdrole(int roleid);	
}
