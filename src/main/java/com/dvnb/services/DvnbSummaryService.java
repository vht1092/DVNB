package com.dvnb.services;

import java.sql.SQLException;
import java.util.List;

import com.dvnb.entities.DvnbSummary;
import com.dvnb.entities.TyGiaTqt;

public interface DvnbSummaryService {

	public void save(DvnbSummary dvnbSummary);
	
	public void deleteDvnbSummaryByMaHoatDong(String ky,String maHoatDong,String role);
	
	public List<DvnbSummary> searchAllDvnbSummary(String ky, String maDvnb, String maDonVi) throws SQLException;
	
	public List<DvnbSummary> searchAllDvnbSummaryProc(String ky, String maDvnb, String maDonVi, String role, String userid,String fromdate,String todate) throws SQLException;
	
	public List<DvnbSummary> searchAct2080101SummaryProc(String ky, String maDvnb, String maDonVi, String role, String userid,String fromdate,String todate) throws SQLException;
	
	public List<DvnbSummary> searchAct2080103SummaryProc(String ky, String maDvnb, String maDonVi, String role, String userid,String fromdate,String todate) throws SQLException;
	
	public List<DvnbSummary> searchAct2080102FromEBHitRuleNhom1(String ky, String userid) throws SQLException;
	
	public List<DvnbSummary> searchAct2080104FromEBHitRuleNhom2(String ky, String userid) throws SQLException;
}
