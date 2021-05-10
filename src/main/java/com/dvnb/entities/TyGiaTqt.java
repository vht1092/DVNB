package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DSQT_TY_GIA_TQT")
@NamedQuery(name = "TyGiaTqt.findAll", query = "SELECT f FROM TyGiaTqt f")
public class TyGiaTqt implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;
	
	@Column(name = "UPD_TMS", nullable = false, precision = 17)
	private BigDecimal updTms;

	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;
	
	@Id
	@Column(name = "NGAY_ADV", nullable = false, length = 8)
	private String ngayAdv;
	
	@Column(name = "CARD_TYPE", nullable = false, length = 6)
	private String cardType;
	
	@Column(name = "ST_QD_VND", nullable = false, precision = 20,scale = 5)
	private BigDecimal stQdVnd;
	
	@Column(name = "ST_GD_USD", nullable = false, precision = 20,scale = 5)
	private BigDecimal stGdUsd;
	
	@Column(name = "TY_GIA_TQT", nullable = false, precision = 20,scale = 5)
	private BigDecimal tyGiaTqt;
	
	
	public BigDecimal getCreTms() {
		return creTms;
	}

	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}

	public BigDecimal getUpdTms() {
		return updTms;
	}

	public void setUpdTms(BigDecimal updTms) {
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

	public String getNgayAdv() {
		return ngayAdv;
	}

	public void setNgayAdv(String ngayAdv) {
		this.ngayAdv = ngayAdv;
	}

	public BigDecimal getStQdVnd() {
		return stQdVnd;
	}

	public void setStQdVnd(BigDecimal stQdVnd) {
		this.stQdVnd = stQdVnd;
	}

	public BigDecimal getStGdUsd() {
		return stGdUsd;
	}

	public void setStGdUsd(BigDecimal stGdUsd) {
		this.stGdUsd = stGdUsd;
	}

	public BigDecimal getTyGiaTqt() {
		return tyGiaTqt;
	}

	public void setTyGiaTqt(BigDecimal tyGiaTqt) {
		this.tyGiaTqt = tyGiaTqt;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	
	

}