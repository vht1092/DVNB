package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class DvnbSummaryPbcp implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String driverCode;
	
	private String driverDesc;
	
	private BigDecimal totalDebit;
	
	private BigDecimal totalCredit;
	
	private BigDecimal totalKhcn;
	
	private BigDecimal totalKhdn;

	public String getDriverCode() {
		return driverCode;
	}

	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}

	public String getDriverDesc() {
		return driverDesc;
	}

	public void setDriverDesc(String driverDesc) {
		this.driverDesc = driverDesc;
	}

	public BigDecimal getTotalDebit() {
		return totalDebit;
	}

	public void setTotalDebit(BigDecimal totalDebit) {
		this.totalDebit = totalDebit;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(BigDecimal totalCredit) {
		this.totalCredit = totalCredit;
	}

	public BigDecimal getTotalKhcn() {
		return totalKhcn;
	}

	public void setTotalKhcn(BigDecimal totalKhcn) {
		this.totalKhcn = totalKhcn;
	}

	public BigDecimal getTotalKhdn() {
		return totalKhdn;
	}

	public void setTotalKhdn(BigDecimal totalKhdn) {
		this.totalKhdn = totalKhdn;
	}

 
	
}