package com.dvnb.entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the DVNB_DESCRIPTION database table.
 * 
 */
@Entity
@Table(name="DVNB_DANHMUC")
public class DvnbDanhMuc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DANHMUC", nullable=false, length=20)
	private String danhMuc;

	@Id
	@Column(name = "MA",nullable=false, length=20)
	private String ma;

	@Column(name = "TEN",length=500)
	private String ten;
	
	@Column(name = "GHICHU",nullable = false, length=200)
	private String ghiChu;
	
	public DvnbDanhMuc() {
	}

	public String getDanhMuc() {
		return danhMuc;
	}

	public void setDanhMuc(String danhMuc) {
		this.danhMuc = danhMuc;
	}

	public String getMa() {
		return ma;
	}

	public void setMa(String ma) {
		this.ma = ma;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	


}
