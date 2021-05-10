package com.dvnb.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbTyGia;

@Repository
public interface TyGiaRepo extends JpaRepository<DvnbTyGia, String> {
	Iterable<DvnbTyGia> findAllByKy(@Param("ky") String ky);
	
	Iterable<DvnbTyGia> findAllByKyAndCrdBrn(@Param("ky") String ky, @Param("cardbrn") String cardbrn);
	
	void deleteByKyAndCrdBrn(@Param("ky") String ky,@Param("crdBrn") String crdBrn);
}
