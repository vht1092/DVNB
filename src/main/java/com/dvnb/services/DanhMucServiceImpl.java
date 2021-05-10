package com.dvnb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DvnbDanhMuc;
import com.dvnb.repositories.DanhMucRepo;

@Service("danhMucService")
public class DanhMucServiceImpl implements DanhMucService {

	@Autowired
	private DanhMucRepo danhMucRepo;

	@Override
	public Iterable<DvnbDanhMuc> findAllByDanhMuc(String danhmuc) {
		return danhMucRepo.findAllByDanhMuc(danhmuc);
	}
	
	@Override
	public Iterable<DvnbDanhMuc> findAllByDanhMucOrderByMaAsc(String danhmuc) {
		return danhMucRepo.findAllByDanhMucOrderByMaAsc(danhmuc);
	}
	
	@Override
	public void save(DvnbDanhMuc dvnbDanhMuc) {

		danhMucRepo.save(dvnbDanhMuc);
	}
	
	@Override
	public void deleteById(String ma) {
		DvnbDanhMuc dvnbDanhMuc = new DvnbDanhMuc();
		dvnbDanhMuc.setMa(ma);
		danhMucRepo.delete(dvnbDanhMuc);
	}

}
