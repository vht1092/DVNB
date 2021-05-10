package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the DVNB_ACT_2080102 database table.
 */
@Entity
@Table(name = "DVNB_ACT_2080102")
@NamedQuery(name = "Act2080102.findAll", query = "SELECT f FROM Act2080102 f")
public class Act2080102 implements Serializable {
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
	
	@Column(name = "CASE_NO", nullable = false, length = 30)
	private String caseNo;
	
	@Column(name = "THOI_GIAN", nullable = false, precision = 20)
	private BigDecimal thoiGian;
	
	@Column(name = "TAI_KHOAN_DANG_NHAP", nullable = false, length = 50)
	private String taiKhoanDangNhap;
	
	@Column(name = "KENH_GD", nullable = false, length = 10)
	private String kenhGd;
	
	@Column(name = "TAI_KHOAN_GIAO_DICH", nullable = false, length = 30)
	private String taiKhoanGiaoDich;
	
	@Column(name = "CIF", nullable = false, length = 19)
	private String cif;
	
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

	public String getCaseNo() {
		return caseNo;
	}

	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public BigDecimal getThoiGian() {
		return thoiGian;
	}

	public void setThoiGian(BigDecimal thoiGian) {
		this.thoiGian = thoiGian;
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

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getTaiKhoanDangNhap() {
		return taiKhoanDangNhap;
	}

	public void setTaiKhoanDangNhap(String taiKhoanDangNhap) {
		this.taiKhoanDangNhap = taiKhoanDangNhap;
	}

	public String getKenhGd() {
		return kenhGd;
	}

	public void setKenhGd(String kenhGd) {
		this.kenhGd = kenhGd;
	}

	public String getTaiKhoanGiaoDich() {
		return taiKhoanGiaoDich;
	}

	public void setTaiKhoanGiaoDich(String taiKhoanGiaoDich) {
		this.taiKhoanGiaoDich = taiKhoanGiaoDich;
	}

	
	
}