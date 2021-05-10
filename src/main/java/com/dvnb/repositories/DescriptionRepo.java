package com.dvnb.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.DvnbDescription;

public interface DescriptionRepo extends CrudRepository<DvnbDescription, Long> {
	Iterable<DvnbDescription> findAllByType(String type);
	
//	Iterable<FdsDescription> findAllByTypeByOrderBySequencenoAsc(String type);
	
	@Query(nativeQuery = true, value = "SELECT * FROM FPT.DVNB_DESCRIPTION WHERE TYPE = :type ORDER BY SEQUENCENO ASC")
	public Iterable<DvnbDescription> findAllByTypeByOrderBySequencenoAsc(@Param(value = "type") String type);
	
	@Query(nativeQuery = true, value = "SELECT 'A' || (MAX(SUBSTR(ID,2,2))+1) FROM FPT.DVNB_DESCRIPTION WHERE ID LIKE 'A%' AND TYPE <> 'ACTION' AND ID<'A98'")
	public String getNextIdContentDetail();
}
