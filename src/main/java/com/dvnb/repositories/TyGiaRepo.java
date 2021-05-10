package com.dvnb.repositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.DvnbTyGia;

public interface TyGiaRepo extends CrudRepository<DvnbTyGia, Long> {
	Iterable<DvnbTyGia> findAllByKy(@Param("ky") String ky);
	
	Iterable<DvnbTyGia> findAllByKyAndCrdBrn(@Param("ky") String ky, @Param("cardbrn") String cardbrn);
	
}
