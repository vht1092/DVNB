package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "DVNB_ACT_3390501")
@NamedQuery(name = "Act3390501.findAll", query = "SELECT f FROM Act3390501 f")
public class Act3390501 implements Serializable {
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
	
	@Column(name = "MA_DVNB", nullable = false, length = 20)
	private String maDvnb;
	
	@Column(name = "KY", nullable = false, length = 6)
	private String ky;
	
	@Column(name = "NGAY_YEU_CAU", nullable = false, length = 20)
	private String ngayYeuCau;
	
	@Column(name = "THUONG_HIEU_THE", nullable = false, length = 10)
	private String thuongHieuThe;
	
	@Column(name = "NOI_DUNG_YEU_CAU", nullable = false, length = 200)
	private String noiDungYeuCau;
	
	@Column(name = "TONG_SO_LUONG", nullable = false, precision = 9)
	private BigDecimal tongSoLuong;
	
	@Column(name = "MA_SAN_PHAM", nullable = false, length = 20)
	private String maSanPham;
	
	@Column(name = "MA_LOAI_KHACH_HANG", nullable = false, length = 20)
	private String maLoaiKhachHang;
	
	@Column(name = "MA_DON_VI", nullable = false, length = 20)
	private String maDonVi;
	
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

	public String getMaSanPham() {
		return maSanPham;
	}

	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}

	public String getMaLoaiKhachHang() {
		return maLoaiKhachHang;
	}

	public void setMaLoaiKhachHang(String maLoaiKhachHang) {
		this.maLoaiKhachHang = maLoaiKhachHang;
	}

	public String getMaDonVi() {
		return maDonVi;
	}

	public void setMaDonVi(String maDonVi) {
		this.maDonVi = maDonVi;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getNgayYeuCau() {
		return ngayYeuCau;
	}

	public void setNgayYeuCau(String ngayYeuCau) {
		this.ngayYeuCau = ngayYeuCau;
	}

	public String getNoiDungYeuCau() {
		return noiDungYeuCau;
	}

	public void setNoiDungYeuCau(String noiDungYeuCau) {
		this.noiDungYeuCau = noiDungYeuCau;
	}

	public String getThuongHieuThe() {
		return thuongHieuThe;
	}

	public void setThuongHieuThe(String thuongHieuThe) {
		this.thuongHieuThe = thuongHieuThe;
	}

	public BigDecimal getTongSoLuong() {
		return tongSoLuong;
	}

	public void setTongSoLuong(BigDecimal tongSoLuong) {
		this.tongSoLuong = tongSoLuong;
	}


	
}