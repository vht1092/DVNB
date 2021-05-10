package com.dvnb.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbSysUserrole;

@Repository
public interface SysUserroleRepo extends CrudRepository<DvnbSysUserrole, String> {

	@Query(value = "select f from DvnbSysUserrole f where f.id.iduser=:iduser")
	List<DvnbSysUserrole> findAllByIdUser(@Param("iduser") String iduser);

	@Query(value = "delete from {h-schema}dvnb_sys_userrole t where t.iduser=:iduser", nativeQuery = true)
	List<DvnbSysUserrole> deleteByIduser(@Param("iduser") String iduser);
	
	@Query(value = "select IDROLE from dvnb_sys_userrole where IDUSER = :userid and rownum <= 1", nativeQuery = true)
	String findByRoleID(@Param(value = "userid") String userid);
}
