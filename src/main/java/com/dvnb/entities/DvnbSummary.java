package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DVNB_SUMMARY")
@NamedQuery(name = "DvnbSummary.findAll", query = "SELECT f FROM DvnbSummary f")
public class DvnbSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "CRE_TMS", nullable = false, length = 17)
	private String creTms;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "UPD_TMS", nullable = false, length = 17)
	private String updTms;

	@Column(name = "UPD_UID", nullable = false, length = 12)
	private String updUid;
	
	@Id
	@Column(name = "ID_NO", nullable = false, length = 24) 	
	private String idno;
	
	@Column(name = "KY", nullable = false, length = 6)
	private String ky;
	
	@Column(name = "MA_DON_VI", nullable = false, length = 20)
	private String maDonVi;
	
	@Column(name = "MA_DRIVER", nullable = false, length = 20)
	private String maDriver;
	
	@Column(name = "MA_SAN_PHAM", nullable = false, length = 20)
	private String maSanPham;

	@Column(name = "MA_PHONG_BAN", nullable = false, length = 20)
	private String maPhongBan;
	
	@Column(name = "MA_LOAI_KHACH_HANG", nullable = false, length = 20)
	private String maLoaiKhachHang;
	
	@Column(name = "MA_HOAT_DONG", nullable = false, length = 20)
	private String maHoatDong;
	
	@Column(name = "PCMCSREVERSE2ID", nullable = false, length = 20)
	private String pcmcsReserve2Id;
	
	@Column(name = "SO_TIEN_SO_LUONG", nullable = false, precision = 12)
	private String soTienSoLuong;
	
	@Column(name = "TYPE", nullable = false, length = 20)
	private String type;
	
	public String getKy() {
		return ky;
	}
	public void setKy(String ky) {
		this.ky = ky;
	}
	public String getMaDonVi() {
		return maDonVi;
	}
	public void setMaDonVi(String maDonVi) {
		this.maDonVi = maDonVi;
	}
	public String getMaDriver() {
		return maDriver;
	}
	public void setMaDriver(String maDriver) {
		this.maDriver = maDriver;
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getMaPhongBan() {
		return maPhongBan;
	}
	public void setMaPhongBan(String maPhongBan) {
		this.maPhongBan = maPhongBan;
	}
	public String getMaLoaiKhachHang() {
		return maLoaiKhachHang;
	}
	public void setMaLoaiKhachHang(String maLoaiKhachHang) {
		this.maLoaiKhachHang = maLoaiKhachHang;
	}
	public String getMaHoatDong() {
		return maHoatDong;
	}
	public void setMaHoatDong(String maHoatDong) {
		this.maHoatDong = maHoatDong;
	}
	public String getPcmcsReserve2Id() {
		return pcmcsReserve2Id;
	}
	public void setPcmcsReserve2Id(String pcmcsReserve2Id) {
		this.pcmcsReserve2Id = pcmcsReserve2Id;
	}
	public String getSoTienSoLuong() {
		return soTienSoLuong;
	}
	public void setSoTienSoLuong(String soTienSoLuong) {
		this.soTienSoLuong = soTienSoLuong;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	
	
}