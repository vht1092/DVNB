package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the DVNB_ACT_3400202 database table.
 * Tiếp nhận và xử lý tra soát, tranh chấp, bồi hoàn giao dịch thẻ quốc tế do SCB phát hành
 * Input file ACT_3400202.xlsx
 */
@Entity
@Table(name = "DVNB_ACT_3400202")
@NamedQuery(name = "Act3400202.findAll", query = "SELECT f FROM Act3400202 f")
public class Act3400202 implements Serializable {
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
	
	@Column(name = "SO_HO_SO", nullable = false, length = 20)
	private String soHoSo;
	
	@Column(name = "DON_VI_TRINH_GOC", nullable = false, length = 200)
	private String donViTrinhGoc;
	
	@Column(name = "NGAY_DE_NGHI_PHAN_HOI", nullable = false, length = 20)
	private String ngayDeNghiPhanHoi;
	
	@Column(name = "MA_SAN_PHAM", nullable = false, length = 20)
	private String maSanPham;
	
	@Column(name = "MA_LOAI_KHACH_HANG", nullable = false, length = 20)
	private String maLoaiKhachHang;
	
	@Column(name = "MA_DON_VI", nullable = false, length = 20)
	private String maDonVi;
	
	@Column(name = "MA_PHONG_BAN", nullable = false, length = 20)
	private String maPhongBan;
	
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

	public String getSoHoSo() {
		return soHoSo;
	}

	public void setSoHoSo(String soHoSo) {
		this.soHoSo = soHoSo;
	}

	public String getNgayDeNghiPhanHoi() {
		return ngayDeNghiPhanHoi;
	}

	public void setNgayDeNghiPhanHoi(String ngayDeNghiPhanHoi) {
		this.ngayDeNghiPhanHoi = ngayDeNghiPhanHoi;
	}

	public String getMaPhongBan() {
		return maPhongBan;
	}

	public void setMaPhongBan(String maPhongBan) {
		this.maPhongBan = maPhongBan;
	}

	public String getDonViTrinhGoc() {
		return donViTrinhGoc;
	}

	public void setDonViTrinhGoc(String donViTrinhGoc) {
		this.donViTrinhGoc = donViTrinhGoc;
	}


	
}