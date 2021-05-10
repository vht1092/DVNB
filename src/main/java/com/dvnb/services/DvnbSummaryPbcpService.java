package com.dvnb.services;

import java.sql.SQLException;
import java.util.List;

import com.dvnb.entities.DvnbSummary;
import com.dvnb.entities.DvnbSummaryPbcp;
import com.dvnb.entities.TyGiaTqt;

public interface DvnbSummaryPbcpService {

	
	public List<DvnbSummaryPbcp> searchAllDataByDriverAndKyAndCardbrn(String tuky, String denKy, String cardbrn) throws SQLException;
	
}
