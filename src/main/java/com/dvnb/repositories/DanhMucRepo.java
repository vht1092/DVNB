package com.dvnb.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.DvnbDanhMuc;

public interface DanhMucRepo extends CrudRepository<DvnbDanhMuc, Long> {
	
	Iterable<DvnbDanhMuc> findAllByDanhMuc(String danhmuc);
	
	@Query(nativeQuery = true, value = "SELECT DANHMUC, MA, TEN, GHICHU FROM DVNB_DANHMUC WHERE DANHMUC = :danhmuc ORDER BY MA ASC")
	public Iterable<DvnbDanhMuc> findAllByDanhMucOrderByMaAsc(@Param(value = "danhmuc") String danhmuc);
}
