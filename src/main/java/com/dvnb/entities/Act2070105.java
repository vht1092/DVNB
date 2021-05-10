package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the DVNB_ACT_2070105 database table.
 * Tiếp nhận và xử lý tra soát, tranh chấp, bồi hoàn giao dịch thẻ quốc tế do SCB phát hành
 * Input file ACT_2070105.xlsx
 */
@Entity
@Table(name = "DVNB_ACT_2070105")
@NamedQuery(name = "Act2070105.findAll", query = "SELECT f FROM Act2070105 f")
public class Act2070105 implements Serializable {
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
	
	@Column(name = "NGAY_TIEP_NHAN", nullable = false, length = 20)
	private String ngayTiepNhan;
	
	@Column(name = "KHACH_HANG", nullable = false, length = 120)
	private String khachHang;
	
	@Column(name = "CIF", nullable = false, length = 19)
	private String cif;
	
	@Column(name = "TAI_KHOAN_NGUON", nullable = false, length = 50)
	private String taiKhoanNguon;
	
	@Column(name = "TAI_KHOAN_DICH", nullable = false, length = 50)
	private String taiKhoanDich;
	
	@Column(name = "SO_GIAO_DICH_FCC", nullable = false, length = 20)
	private String soGiaoDichFcc;
	
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


	public String getMaDonVi() {
		return maDonVi;
	}


	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}


	public String getNgayTiepNhan() {
		return ngayTiepNhan;
	}

	public void setNgayTiepNhan(String ngayTiepNhan) {
		this.ngayTiepNhan = ngayTiepNhan;
	}

	public void setMaDonVi(String maDonVi) {
		this.maDonVi = maDonVi;
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

	public String getKhachHang() {
		return khachHang;
	}

	public void setKhachHang(String khachHang) {
		this.khachHang = khachHang;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getTaiKhoanNguon() {
		return taiKhoanNguon;
	}

	public void setTaiKhoanNguon(String taiKhoanNguon) {
		this.taiKhoanNguon = taiKhoanNguon;
	}

	public String getTaiKhoanDich() {
		return taiKhoanDich;
	}

	public void setTaiKhoanDich(String taiKhoanDich) {
		this.taiKhoanDich = taiKhoanDich;
	}

	public String getSoGiaoDichFcc() {
		return soGiaoDichFcc;
	}

	public void setSoGiaoDichFcc(String soGiaoDichFcc) {
		this.soGiaoDichFcc = soGiaoDichFcc;
	}



	
	
}