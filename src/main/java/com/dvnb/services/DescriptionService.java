package com.dvnb.services;

import com.dvnb.entities.DvnbDescription;

public interface DescriptionService {
	Iterable<DvnbDescription> findAllByType(String type);
	Iterable<DvnbDescription> findAllByTypeByOrderBySequencenoAsc(String type);
	Iterable<DvnbDescription> findAllByTypeByOrderBySequencenoDesc(String type);
	public void save(String id, String desc, String type);
	public String getNextIdContentDetail();
	public void deleteById(String id);
}
