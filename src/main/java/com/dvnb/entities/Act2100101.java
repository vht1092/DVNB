package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the DVNB_ACT_2100101 database table.
 * Tiếp nhận và xử lý tra soát, tranh chấp, bồi hoàn giao dịch thẻ quốc tế do SCB phát hành
 */
@Entity
@Table(name = "DVNB_ACT_2100101")
@NamedQuery(name = "Act2100101.findAll", query = "SELECT f FROM Act2100101 f")
public class Act2100101 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CRE_TMS", nullable = false, length = 17)
	private String creTms;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "UPD_TMS", nullable = false, length = 17)
	private String updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;
	
//	@Column(name = "ID_NO", nullable = false, length = 24) 	
//	private String idno;
	
	@Column(name = "MA_DVNB", nullable = false, length = 20)
	private String maDvnb;
	
	@Id
	@Column(name = "KY", nullable = false, length = 6)
	private String ky;
	
	@Column(name = "SO_LUONG_PHAT_SINH", nullable = false, precision = 12)
	private BigDecimal soLuongPhatSinh;
	
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

	public BigDecimal getSoLuongPhatSinh() {
		return soLuongPhatSinh;
	}

	public void setSoLuongPhatSinh(BigDecimal soLuongPhatSinh) {
		this.soLuongPhatSinh = soLuongPhatSinh;
	}


	
}