package com.dvnb.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;


/**
 * The persistent class for the DVNB_DESCRIPTION database table.
 * 
 */
@Entity
@Table(name="DVNB_TYGIA")
public class DvnbTyGia implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", nullable = false, length = 10)
	private String id;
	
	@Column(name = "CRE_TMS", nullable = false, length = 17)
	private String creTms;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "UPD_TMS", nullable = false, length = 17)
	private String updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;

	@Column(nullable=false, length=6)
	private String ky;

	@Column(name = "NGAY_TYGIA", nullable = false, length = 20)
	private String ngayTyGia;
	
	@Column(name = "TYGIA", nullable = false, precision = 10)
	private BigDecimal tyGia;
	
	@Column(name = "CRD_BRN",nullable=false, length=6)
	private String crdBrn;
	
	public DvnbTyGia() {
	}

	public String getKy() {
		return ky;
	}

	public void setKy(String ky) {
		this.ky = ky;
	}

	public String getNgayTyGia() {
		return ngayTyGia;
	}

	public void setNgayTyGia(String ngayTyGia) {
		this.ngayTyGia = ngayTyGia;
	}

	public BigDecimal getTyGia() {
		return tyGia;
	}

	public void setTyGia(BigDecimal tyGia) {
		this.tyGia = tyGia;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



}
