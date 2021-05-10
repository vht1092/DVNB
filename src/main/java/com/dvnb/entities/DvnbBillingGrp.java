package com.dvnb.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;


/**
 * The persistent class for the DVNB_BILLING_GRP database table.
 * 
 */
@Entity
@Table(name="DVNB_BILLING_GRP")
public class DvnbBillingGrp implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CRE_TMS", nullable = false, length = 17)
	private String creTms;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "UPD_TMS", nullable = false, length = 17)
	private String updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;

	@Column(name = "GRP", nullable = false, length = 50)
	private String grp;

	@Id
	@Column(name = "BILLING_CDE", nullable = false, length = 20)
	private String billingCde;
	
	@Column(name = "BLIING_NAME", nullable = false, length = 500)
	private String billingName;
	
	@Column(name = "BILLING_DESC",nullable=false, length = 2000)
	private String billingDesc;
	
	@Column(name = "DRIVER_CDE",nullable=false, length = 5)
	private String driverCde;
	
	@Column(name = "DRIVER_NAME",nullable=false, length = 400)
	private String driverName;
	
	@Column(name = "CRD_BRN",nullable=false, length = 2)
	private String crdBrn;
	
	public DvnbBillingGrp() {
	}


	public String getCreTms() {
		return creTms;
	}

	public void setCreTms(String creTms) {
		this.creTms = creTms;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getUpdTms() {
		return updTms;
	}

	public void setUpdTms(String updTms) {
		this.updTms = updTms;
	}

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public String getCrdBrn() {
		return crdBrn;
	}

	public void setCrdBrn(String crdBrn) {
		this.crdBrn = crdBrn;
	}


	public String getGrp() {
		return grp;
	}


	public void setGrp(String grp) {
		this.grp = grp;
	}


	public String getBillingCde() {
		return billingCde;
	}


	public void setBillingCde(String billingCde) {
		this.billingCde = billingCde;
	}


	public String getBillingName() {
		return billingName;
	}


	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}


	public String getBillingDesc() {
		return billingDesc;
	}


	public void setBillingDesc(String billingDesc) {
		this.billingDesc = billingDesc;
	}


	public String getDriverCde() {
		return driverCde;
	}


	public void setDriverCde(String driverCde) {
		this.driverCde = driverCde;
	}


	public String getDriverName() {
		return driverName;
	}


	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}




}
