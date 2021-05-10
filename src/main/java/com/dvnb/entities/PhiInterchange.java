package com.dvnb.entities;

import java.math.BigDecimal;

public class PhiInterchange {
	
	private static final long serialVersionUID = 1L;
	
	private String custType;
	
	private BigDecimal interchange;

	public String getCustType() {
		return custType;
	}

	public void setCustType(String custType) {
		this.custType = custType;
	}

	public BigDecimal getInterchange() {
		return interchange;
	}

	public void setInterchange(BigDecimal interchange) {
		this.interchange = interchange;
	}
	

}
