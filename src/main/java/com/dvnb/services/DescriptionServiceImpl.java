package com.dvnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbDescription;
import com.dvnb.repositories.DescriptionRepo;

@Service("descriptionService")
public class DescriptionServiceImpl implements DescriptionService {

	@Autowired
	private DescriptionRepo descriptionRepo;

	@Override
	public Iterable<DvnbDescription> findAllByType(String type) {
		return descriptionRepo.findAllByType(type);
	}
	
	@Override
	public Iterable<DvnbDescription> findAllByTypeByOrderBySequencenoAsc(String type) {
		return descriptionRepo.findAllByTypeByOrderBySequencenoAsc(type);
	}
	
	@Override
	public Iterable<DvnbDescription> findAllByTypeByOrderBySequencenoDesc(String type) {
		// TODO Auto-generated method stub
		return descriptionRepo.findAllByTypeByOrderBySequencenoDesc(type);
	}
	
	@Override
	public void save(String id, String desc, String type) {

		DvnbDescription fdsDescription = new DvnbDescription();
		fdsDescription.setId(id);
		fdsDescription.setDescription(desc);
		fdsDescription.setType(type);
		descriptionRepo.save(fdsDescription);
	}
	
	@Override
	public String getNextIdContentDetail() {
		return descriptionRepo.getNextIdContentDetail();
	}
	
	@Override
	public void deleteById(String id) {
		DvnbDescription fdsDescription = new DvnbDescription();
		fdsDescription.setId(id);
		descriptionRepo.delete(fdsDescription);
	}

	

}
