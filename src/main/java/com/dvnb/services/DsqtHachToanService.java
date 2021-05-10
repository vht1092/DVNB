package com.dvnb.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.dvnb.entities.DoiSoatData;
import com.dvnb.entities.DsqtHachToan;
import com.dvnb.entities.DsqtHachToanCustom;

public interface DsqtHachToanService {

	public void save(DsqtHachToan dsqtHachToan);
	
	List<DsqtHachToan> findAllByNgayAdv(String ngayAdv);
	
	List<DsqtHachToan> findAllByNgayAdvAndCardBrnAndCurr(String ngayAdv,String cardBrn,String curr);
	
	List<DsqtHachToanCustom> findHachToanByNgayAdvAndCardBrnAndCurr(String ngayAdv,String cardBrn,String curr) throws SQLException;
	
	int countByFilename(String filename);
	
	List<String> findAllFilenameByNgayadv(String ngayadv);
	
	int deleteAllByNgayAdv(String ngayadv);
}
