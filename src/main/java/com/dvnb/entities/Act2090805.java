package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DVNB_ACT_2090805")
@NamedQuery(name = "Act2090805.findAll", query = "SELECT f FROM Act2090805 f")
public class Act2090805 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CRE_TMS", nullable = false, length = 17)
	private String creTms;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "UPD_TMS", nullable = false, length = 17)
	private String updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;
	
	@Column(name = "MA_DVNB", nullable = false, length = 20)
	private String maDvnb;
	
	@Id
	@Column(name = "KY", nullable = false, length = 6)
	private String ky;
	
	@Column(name = "SO_LUONG_NGAY_LAM_VIEC", nullable = false, precision = 12)
	private BigDecimal soLuongNgayLamViec;
	
	public String getCreTms() {
		return creTms;
	}

	public void setCreTms(String creTms) {
		this.creTms = creTms;
	}

	public String getUpdTms() {
		return updTms;
	}

	public void setUpdTms(String updTms) {
		this.updTms = updTms;
	}

	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getUpdUid() {
		return updUid;
	}

	public void setUpdUid(String updUid) {
		this.updUid = updUid;
	}

	public String getMaDvnb() {
		return maDvnb;
	}

	public void setMaDvnb(String maDvnb) {
		this.maDvnb = maDvnb;
	}

	public String getKy() {
		return ky;
	}

	public void setKy(String ky) {
		this.ky = ky;
	}

	public BigDecimal getSoLuongNgayLamViec() {
		return soLuongNgayLamViec;
	}

	public void setSoLuongNgayLamViec(BigDecimal soLuongNgayLamViec) {
		this.soLuongNgayLamViec = soLuongNgayLamViec;
	}


	
}