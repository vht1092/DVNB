package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DSQT_UPDATE_FEE_DAILY")
@NamedQuery(name = "DsqtFeeDaily.findAll", query = "SELECT f FROM DsqtFeeDaily f")
public class DsqtFeeDaily implements Serializable {
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
	@Column(name = "ID", nullable = false, length = 20)
	private String id;
	
	@Column(name = "LOAI_THE", nullable = false, length = 8)
	private String loaiThe;
	
	@Column(name = "NGAY_INCOMING_FILE", nullable = false, length = 8)
	private String ngayIncomingFile;
	
	@Column(name = "NGAY_ADV", nullable = false, length = 8)
	private String ngayAdv;
	
	@Column(name = "NGAY_HACH_TOAN", nullable = false, length = 8)
	private String ngayHachToan;
	
	@Column(name = "LOAI_GD", nullable = false, length = 5)
	private String loaiGd;
	
	@Column(name = "LOAI_TIEN_TQT", nullable = false, length = 3)
	private String loaiTienTqt;
	
	@Column(name = "TY_GIA_TQT", nullable = false, precision = 20,scale = 5)
	private BigDecimal tyGiaTqt;
	
	@Column(name = "INTERCHANGE_DUOC_HUONG_TRA", nullable = false, precision = 20,scale = 5)
	private BigDecimal interchangeDuocHuongPhaiTra;
	
	@Column(name = "INTERCHANGE_DUOC_HOAN_TRA", nullable = false, precision = 20,scale = 5)
	private BigDecimal interchangeDuocHoanTra;
	
	@Column(name = "PHI_XU_LY_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal phiXuLyGd;
	
	@Column(name = "TONG_INTERCHANGE", nullable = false, precision = 20,scale = 5)
	private BigDecimal tongInterchange;
	
	@Column(name = "TONG_PHIGD", nullable = false, precision = 20,scale = 5)
	private BigDecimal tongPhiGd;
	
	
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

	public BigDecimal getTyGiaTqt() {
		return tyGiaTqt;
	}

	public void setTyGiaTqt(BigDecimal tyGiaTqt) {
		this.tyGiaTqt = tyGiaTqt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoaiThe() {
		return loaiThe;
	}

	public void setLoaiThe(String loaiThe) {
		this.loaiThe = loaiThe;
	}

	public String getNgayIncomingFile() {
		return ngayIncomingFile;
	}

	public void setNgayIncomingFile(String ngayIncomingFile) {
		this.ngayIncomingFile = ngayIncomingFile;
	}

	public String getNgayHachToan() {
		return ngayHachToan;
	}

	public void setNgayHachToan(String ngayHachToan) {
		this.ngayHachToan = ngayHachToan;
	}

	public String getLoaiGd() {
		return loaiGd;
	}

	public void setLoaiGd(String loaiGd) {
		this.loaiGd = loaiGd;
	}

	public String getLoaiTienTqt() {
		return loaiTienTqt;
	}

	public void setLoaiTienTqt(String loaiTienTqt) {
		this.loaiTienTqt = loaiTienTqt;
	}

	public BigDecimal getInterchangeDuocHuongPhaiTra() {
		return interchangeDuocHuongPhaiTra;
	}

	public void setInterchangeDuocHuongPhaiTra(BigDecimal interchangeDuocHuongPhaiTra) {
		this.interchangeDuocHuongPhaiTra = interchangeDuocHuongPhaiTra;
	}

	public BigDecimal getInterchangeDuocHoanTra() {
		return interchangeDuocHoanTra;
	}

	public void setInterchangeDuocHoanTra(BigDecimal interchangeDuocHoanTra) {
		this.interchangeDuocHoanTra = interchangeDuocHoanTra;
	}

	public BigDecimal getPhiXuLyGd() {
		return phiXuLyGd;
	}

	public void setPhiXuLyGd(BigDecimal phiXuLyGd) {
		this.phiXuLyGd = phiXuLyGd;
	}

	public BigDecimal getTongInterchange() {
		return tongInterchange;
	}

	public void setTongInterchange(BigDecimal tongInterchange) {
		this.tongInterchange = tongInterchange;
	}

	public BigDecimal getTongPhiGd() {
		return tongPhiGd;
	}

	public void setTongPhiGd(BigDecimal tongPhiGd) {
		this.tongPhiGd = tongPhiGd;
	}

	
	

}