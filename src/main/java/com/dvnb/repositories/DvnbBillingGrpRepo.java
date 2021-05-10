package com.dvnb.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dvnb.entities.DvnbBillingGrp;
import com.dvnb.entities.DvnbTyGia;

@Repository
public interface DvnbBillingGrpRepo extends JpaRepository<DvnbBillingGrp, String> {
	
	List<DvnbBillingGrp> findAllByCrdBrn(@Param("cardBrn") String cardBrn);
	
	List<DvnbBillingGrp> findAllByCrdBrnOrderByCreTms(@Param("cardBrn") String cardBrn);
	
	List<DvnbBillingGrp> findAllByOrderByCreTms();
	
	void deleteByCrdBrn(@Param("crdBrn") String crdBrn);
}
