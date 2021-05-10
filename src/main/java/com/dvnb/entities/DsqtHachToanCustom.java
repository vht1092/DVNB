package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class DsqtHachToanCustom implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String ngayAdv;
	
	private String cardBrn;
	
	private String curr;
	
	private String hachToan;
	
	private BigDecimal slGdChapThuan;
	
	private BigDecimal stBidvTrichNo;
	
	private BigDecimal stGd;
	
	private BigDecimal thuPhiInterchange;
	
	private BigDecimal phiInterchangePhaiTra;
	
	private BigDecimal phiXuLyGdATM;
	
	private String t140;
	
	private String filename;
	
	private String cycle;

	public String getNgayAdv() {
		return ngayAdv;
	}

	public void setNgayAdv(String ngayAdv) {
		this.ngayAdv = ngayAdv;
	}

	public String getCardBrn() {
		return cardBrn;
	}

	public void setCardBrn(String cardBrn) {
		this.cardBrn = cardBrn;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public BigDecimal getSlGdChapThuan() {
		return slGdChapThuan;
	}

	public void setSlGdChapThuan(BigDecimal slGdChapThuan) {
		this.slGdChapThuan = slGdChapThuan;
	}

	public BigDecimal getStBidvTrichNo() {
		return stBidvTrichNo;
	}

	public void setStBidvTrichNo(BigDecimal stBidvTrichNo) {
		this.stBidvTrichNo = stBidvTrichNo;
	}

	public BigDecimal getStGd() {
		return stGd;
	}

	public void setStGd(BigDecimal stGd) {
		this.stGd = stGd;
	}

	public BigDecimal getThuPhiInterchange() {
		return thuPhiInterchange;
	}

	public void setThuPhiInterchange(BigDecimal thuPhiInterchange) {
		this.thuPhiInterchange = thuPhiInterchange;
	}

	public BigDecimal getPhiInterchangePhaiTra() {
		return phiInterchangePhaiTra;
	}

	public void setPhiInterchangePhaiTra(BigDecimal phiInterchangePhaiTra) {
		this.phiInterchangePhaiTra = phiInterchangePhaiTra;
	}

	public BigDecimal getPhiXuLyGdATM() {
		return phiXuLyGdATM;
	}

	public void setPhiXuLyGdATM(BigDecimal phiXuLyGdATM) {
		this.phiXuLyGdATM = phiXuLyGdATM;
	}

	public String getT140() {
		return t140;
	}

	public void setT140(String t140) {
		this.t140 = t140;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHachToan() {
		return hachToan;
	}

	public void setHachToan(String hachToan) {
		this.hachToan = hachToan;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	
	

}