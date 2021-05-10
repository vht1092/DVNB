package com.dvnb.services;

import com.dvnb.entities.DvnbDanhMuc;

public interface DanhMucService {
	Iterable<DvnbDanhMuc> findAllByDanhMuc(String danhmuc);
	Iterable<DvnbDanhMuc> findAllByDanhMucOrderByMaAsc(String type);
	public void save(DvnbDanhMuc dvnbDanhMuc);
	public void deleteById(String id);
}
