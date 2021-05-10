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
	@Column(name = "ID", nullable = false, length = 11)
	private String id;
	
	@Column(name = "NGAY_ADV", nullable = false, length = 8)
	private String ngayAdv;
	
	@Column(name = "CARD_TYPE", nullable = false, length = 6)
	private String cardType;
	
	@Column(name = "ST_QD_VND", nullable = false, precision = 20,scale = 5)
	private BigDecimal stQdVnd;
	
	@Column(name = "ST_GD_USD", nullable = false, precision = 20,scale = 5)
	private BigDecimal stGdUsd;
	
	@Column(name = "TY_GIA_TQT", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaTqt;
	
	@Column(name = "TY_GIA_PXL", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaPxl;
	
	@Column(name = "TY_GIA_GD_TTHH", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaGdTthh;
	
	@Column(name = "TY_GIA_GD_RTM", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaGdRtm;
	
	@Column(name = "TY_GIA_GD_MSFF", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaGdMsff;
	
	@Column(name = "TY_GIA_GD_HT_TTHH", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaGdHttthh;
	
	@Column(name = "TY_GIA_GD_HT_RTM", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaGdHtrtm;
	
	@Column(name = "TY_GIA_PXL_TTHH", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaPxlTthh;
	
	@Column(name = "TY_GIA_PXL_RTM", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaPxlRtm;
	
	@Column(name = "TY_GIA_PXL_MSFF", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaPxlMsff;
	
	@Column(name = "TY_GIA_PXL_HT_TTHH", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaPxlHttthh;
	
	@Column(name = "TY_GIA_PXL_HT_RTM", nullable = false, precision = 20,scale = 6)
	private BigDecimal tyGiaPxlHtrtm;
	

	
	
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

	public BigDecimal getTyGiaPxl() {
		return tyGiaPxl;
	}

	public void setTyGiaPxl(BigDecimal tyGiaPxl) {
		this.tyGiaPxl = tyGiaPxl;
	}

	public BigDecimal getTyGiaGdTthh() {
		return tyGiaGdTthh;
	}

	public void setTyGiaGdTthh(BigDecimal tyGiaGdTthh) {
		this.tyGiaGdTthh = tyGiaGdTthh;
	}

	public BigDecimal getTyGiaGdRtm() {
		return tyGiaGdRtm;
	}

	public void setTyGiaGdRtm(BigDecimal tyGiaGdRtm) {
		this.tyGiaGdRtm = tyGiaGdRtm;
	}

	public BigDecimal getTyGiaGdMsff() {
		return tyGiaGdMsff;
	}

	public void setTyGiaGdMsff(BigDecimal tyGiaGdMsff) {
		this.tyGiaGdMsff = tyGiaGdMsff;
	}

	public BigDecimal getTyGiaGdHttthh() {
		return tyGiaGdHttthh;
	}

	public void setTyGiaGdHttthh(BigDecimal tyGiaGdHttthh) {
		this.tyGiaGdHttthh = tyGiaGdHttthh;
	}

	public BigDecimal getTyGiaGdHtrtm() {
		return tyGiaGdHtrtm;
	}

	public void setTyGiaGdHtrtm(BigDecimal tyGiaGdHtrtm) {
		this.tyGiaGdHtrtm = tyGiaGdHtrtm;
	}

	public BigDecimal getTyGiaPxlTthh() {
		return tyGiaPxlTthh;
	}

	public void setTyGiaPxlTthh(BigDecimal tyGiaPxlTthh) {
		this.tyGiaPxlTthh = tyGiaPxlTthh;
	}

	public BigDecimal getTyGiaPxlRtm() {
		return tyGiaPxlRtm;
	}

	public void setTyGiaPxlRtm(BigDecimal tyGiaPxlRtm) {
		this.tyGiaPxlRtm = tyGiaPxlRtm;
	}

	public BigDecimal getTyGiaPxlMsff() {
		return tyGiaPxlMsff;
	}

	public void setTyGiaPxlMsff(BigDecimal tyGiaPxlMsff) {
		this.tyGiaPxlMsff = tyGiaPxlMsff;
	}

	public BigDecimal getTyGiaPxlHttthh() {
		return tyGiaPxlHttthh;
	}

	public void setTyGiaPxlHttthh(BigDecimal tyGiaPxlHttthh) {
		this.tyGiaPxlHttthh = tyGiaPxlHttthh;
	}

	public BigDecimal getTyGiaPxlHtrtm() {
		return tyGiaPxlHtrtm;
	}

	public void setTyGiaPxlHtrtm(BigDecimal tyGiaPxlHtrtm) {
		this.tyGiaPxlHtrtm = tyGiaPxlHtrtm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	

}