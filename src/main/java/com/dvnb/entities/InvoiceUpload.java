package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceUpload implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private String batchId;
	
	private String maDriver;
	
	private String ngay;
	
	private String chiNhanhChuyen;
	
	private String taiKhoanChuyen;
	
	private String tenNguoiChuyen;
	
	private String loaiTien;
	
	private String soTien;
	
	private String chiNhanhHuong;
	
	private String taiKhoanHuong;
	
	private String tenNguoiHuong;
	
	private String noiDung;
	
	private String productCode;
	
	private String tenUdf;
	
	private String giaTriUdf;

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getNgay() {
		return ngay;
	}

	public void setNgay(String ngay) {
		this.ngay = ngay;
	}

	public String getChiNhanhChuyen() {
		return chiNhanhChuyen;
	}

	public void setChiNhanhChuyen(String chiNhanhChuyen) {
		this.chiNhanhChuyen = chiNhanhChuyen;
	}

	public String getTaiKhoanChuyen() {
		return taiKhoanChuyen;
	}

	public void setTaiKhoanChuyen(String taiKhoanChuyen) {
		this.taiKhoanChuyen = taiKhoanChuyen;
	}

	public String getTenNguoiChuyen() {
		return tenNguoiChuyen;
	}

	public void setTenNguoiChuyen(String tenNguoiChuyen) {
		this.tenNguoiChuyen = tenNguoiChuyen;
	}

	public String getLoaiTien() {
		return loaiTien;
	}

	public void setLoaiTien(String loaiTien) {
		this.loaiTien = loaiTien;
	}

	public String getSoTien() {
		return soTien;
	}

	public void setSoTien(String soTien) {
		this.soTien = soTien;
	}

	public String getChiNhanhHuong() {
		return chiNhanhHuong;
	}

	public void setChiNhanhHuong(String chiNhanhHuong) {
		this.chiNhanhHuong = chiNhanhHuong;
	}

	public String getTaiKhoanHuong() {
		return taiKhoanHuong;
	}

	public void setTaiKhoanHuong(String taiKhoanHuong) {
		this.taiKhoanHuong = taiKhoanHuong;
	}

	public String getTenNguoiHuong() {
		return tenNguoiHuong;
	}

	public void setTenNguoiHuong(String tenNguoiHuong) {
		this.tenNguoiHuong = tenNguoiHuong;
	}

	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getTenUdf() {
		return tenUdf;
	}

	public void setTenUdf(String tenUdf) {
		this.tenUdf = tenUdf;
	}

	public String getGiaTriUdf() {
		return giaTriUdf;
	}

	public void setGiaTriUdf(String giaTriUdf) {
		this.giaTriUdf = giaTriUdf;
	}

	public String getMaDriver() {
		return maDriver;
	}

	public void setMaDriver(String maDriver) {
		this.maDriver = maDriver;
	}
	
	
}