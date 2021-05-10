package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DSQT_DATA")
@NamedQuery(name = "DoiSoatData.findAll", query = "SELECT f FROM DoiSoatData f")
public class DoiSoatData implements Serializable {
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
	@Column(name = "ID", nullable = false, length = 24)
	private String id;
	
	@Column(name = "NGAY_ADV", nullable = false, length = 8)
	private String ngayAdv;
	
	@Column(name = "TEN_CHU_THE", nullable = false, length = 300)
	private String tenChuThe;
	
	@Column(name = "TEN_CHU_TK", nullable = false, length = 300)
	private String tenChuTk;
	
	@Column(name = "CASA", nullable = false, length = 50)
	private String casa;
	
	@Column(name = "SO_THE", nullable = false, length = 20)
	private String soThe;
	
	@Column(name = "PAN", nullable = false, length = 20)
	private String pan;
	
	@Column(name = "MA_GD", nullable = false, length = 20)
	private String maGd;
	
	@Column(name = "NGAY_GD", nullable = false, length = 22)
	private String ngayGd;
	
	@Column(name = "NGAY_FILE_INCOMING", nullable = false, length = 22)
	private String ngayFileIncoming;
	
	@Column(name = "ST_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal stGd = BigDecimal.ZERO;
	
	@Column(name = "ST_TQT", nullable = false, precision = 20,scale = 5)
	private BigDecimal stTqt = BigDecimal.ZERO;
	
	@Column(name = "ST_QD_VND", nullable = false, precision = 20,scale = 5)
	private BigDecimal stQdVnd = BigDecimal.ZERO;
	
	@Column(name = "LTGD", nullable = false, length = 10)
	private String ltgd;
	
	@Column(name = "LTTQT", nullable = false, length = 10)
	private String lttqt;
	
	@Column(name = "INTERCHANGE", nullable = false, precision = 20,scale = 5)
	private BigDecimal interchange = BigDecimal.ZERO;
	
	@Column(name = "MA_CAP_PHEP", nullable = false, length = 10)
	private String maCapPhep;
	
	@Column(name = "DVCNT", nullable = false, length = 300)
	private String dvcnt;
	
	@Column(name = "REVERSAL_IND", nullable = false, length = 10)
	private String reversalInd;
	
	@Column(name = "ISSUER_CHARGE", nullable = false, length = 10)
	private String issuerCharge = " ";
	
	@Column(name = "MERCHANT_CITY", nullable = false, length = 200)
	private String merchantCity = " ";
	
	@Column(name = "ST_TRICH_NO_KH_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal stTrichNoKhGd = BigDecimal.ZERO;
	
	@Column(name = "STGD_NGUYEN_TE_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal stgdNguyenTeGd = BigDecimal.ZERO;
	
	@Column(name = "LOAI_TIEN_NGUYEN_TE_GD", nullable = false, length = 10)
	private String loaiTienNguyenTeGd = " ";
	
	@Column(name = "PHI_ISA_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal phiIsaGd = BigDecimal.ZERO;
	
	@Column(name = "VAT_PHI_ISA_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal vatPhiIsaGd = BigDecimal.ZERO;
	
	@Column(name = "PHI_RTM_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal phiRtmGd = BigDecimal.ZERO;
	
	@Column(name = "VAT_PHI_RTM_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal vatPhiRtmGd = BigDecimal.ZERO;
	
	@Column(name = "STGD_NGUYEN_TE_CHENH_LECH", nullable = false, precision = 20,scale = 5)
	private BigDecimal stgdNguyenTeChenhLech = BigDecimal.ZERO;
	
	@Column(name = "STGD_CHENH_LECH_DO_TY_GIA", nullable = false, precision = 20,scale = 5)
	private BigDecimal stgdChenhLechDoTyGia = BigDecimal.ZERO;
	
	@Column(name = "TY_GIA_TRICH_NO", nullable = false, precision = 20,scale = 5)
	private BigDecimal tyGiaTrichNo = BigDecimal.ZERO;
	
	@Column(name = "SO_TIEN_GD_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal soTienGdHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "PHI_ISA_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal phiIsaHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "VAT_PHI_ISA_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal vatPhiIsaHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "PHI_RTM_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal phiRtmHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "VAT_PHI_RTM_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal vatPhiRtmHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "TONG_PHI_VAT_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal tongPhiVatHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "TONG_HOAN_TRA_TRUY_THU", nullable = false, precision = 20,scale = 5)
	private BigDecimal tongHoanTraTruyThu = BigDecimal.ZERO;
	
	@Column(name = "PHI_XU_LY_GD", nullable = false, precision = 20,scale = 5)
	private BigDecimal phiXuLyGd = BigDecimal.ZERO;
	
	@Column(name = "DVPHT", nullable = false, length = 50)
	private String dvpht;
	
	@Column(name = "TRACE", nullable = false, length = 10)
	private String trace;
	
	@Column(name = "STATUS_CW", nullable = false, length = 10)
	private String statusCw = "NA";
	
	@Column(name = "MCC", nullable = false, length = 20)
	private String mcc;
	
	@Column(name = "CIF", nullable = false, length = 10)
	private String cif;
	
	@Column(name = "CRD_PGM", nullable = false, length = 10)
	private String crdpgm;
	
	@Column(name = "NGAY_HOAN_TRA", nullable = false, length = 8)
	private String ngayHoanTra;
	
	@Column(name = "LOC", nullable = false, length = 12)
	private String loc = " ";
	
	@Column(name = "CARD_BRN", nullable = false, length = 3)
	private String cardBrn;
	
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

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenChuThe() {
		return tenChuThe;
	}

	public void setTenChuThe(String tenChuThe) {
		this.tenChuThe = tenChuThe;
	}

	public String getCasa() {
		return casa;
	}

	public void setCasa(String casa) {
		this.casa = casa;
	}

	public String getSoThe() {
		return soThe;
	}

	public void setSoThe(String soThe) {
		this.soThe = soThe;
	}

	public String getMaGd() {
		return maGd;
	}

	public void setMaGd(String maGd) {
		this.maGd = maGd;
	}

	public String getNgayGd() {
		return ngayGd;
	}

	public void setNgayGd(String ngayGd) {
		this.ngayGd = ngayGd;
	}

	public String getNgayFileIncoming() {
		return ngayFileIncoming;
	}

	public void setNgayFileIncoming(String ngayFileIncoming) {
		this.ngayFileIncoming = ngayFileIncoming;
	}

	public String getLttqt() {
		return lttqt;
	}

	public void setLttqt(String lttqt) {
		this.lttqt = lttqt;
	}

	public String getMaCapPhep() {
		return maCapPhep;
	}

	public void setMaCapPhep(String maCapPhep) {
		this.maCapPhep = maCapPhep;
	}

	public String getDvcnt() {
		return dvcnt;
	}

	public void setDvcnt(String dvcnt) {
		this.dvcnt = dvcnt;
	}

	public String getMerchantCity() {
		return merchantCity;
	}

	public void setMerchantCity(String merchantCity) {
		this.merchantCity = merchantCity;
	}


	public String getLoaiTienNguyenTeGd() {
		return loaiTienNguyenTeGd;
	}

	public void setLoaiTienNguyenTeGd(String loaiTienNguyenTeGd) {
		this.loaiTienNguyenTeGd = loaiTienNguyenTeGd;
	}


	public String getDvpht() {
		return dvpht;
	}

	public void setDvpht(String dvpht) {
		this.dvpht = dvpht;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public String getStatusCw() {
		return statusCw;
	}

	public void setStatusCw(String statusCw) {
		this.statusCw = statusCw;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getLtgd() {
		return ltgd;
	}

	public void setLtgd(String ltgd) {
		this.ltgd = ltgd;
	}

	public String getReversalInd() {
		return reversalInd;
	}

	public void setReversalInd(String reversalInd) {
		this.reversalInd = reversalInd;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}
	
	public String getNgayAdv() {
		return ngayAdv;
	}

	public void setNgayAdv(String ngayAdv) {
		this.ngayAdv = ngayAdv;
	}

	public BigDecimal getStTrichNoKhGd() {
		return stTrichNoKhGd;
	}

	public void setStTrichNoKhGd(BigDecimal stTrichNoKhGd) {
		this.stTrichNoKhGd = stTrichNoKhGd;
	}

	public BigDecimal getStgdNguyenTeGd() {
		return stgdNguyenTeGd;
	}

	public void setStgdNguyenTeGd(BigDecimal stgdNguyenTeGd) {
		this.stgdNguyenTeGd = stgdNguyenTeGd;
	}

	public BigDecimal getPhiIsaGd() {
		return phiIsaGd;
	}

	public void setPhiIsaGd(BigDecimal phiIsaGd) {
		this.phiIsaGd = phiIsaGd;
	}

	public BigDecimal getPhiRtmGd() {
		return phiRtmGd;
	}

	public void setPhiRtmGd(BigDecimal phiRtmGd) {
		this.phiRtmGd = phiRtmGd;
	}

	public BigDecimal getStgdNguyenTeChenhLech() {
		return stgdNguyenTeChenhLech;
	}

	public void setStgdNguyenTeChenhLech(BigDecimal stgdNguyenTeChenhLech) {
		this.stgdNguyenTeChenhLech = stgdNguyenTeChenhLech;
	}

	public BigDecimal getStgdChenhLechDoTyGia() {
		return stgdChenhLechDoTyGia;
	}

	public void setStgdChenhLechDoTyGia(BigDecimal stgdChenhLechDoTyGia) {
		this.stgdChenhLechDoTyGia = stgdChenhLechDoTyGia;
	}

	public BigDecimal getTyGiaTrichNo() {
		return tyGiaTrichNo;
	}

	public void setTyGiaTrichNo(BigDecimal tyGiaTrichNo) {
		this.tyGiaTrichNo = tyGiaTrichNo;
	}

	public BigDecimal getSoTienGdHoanTraTruyThu() {
		return soTienGdHoanTraTruyThu;
	}

	public void setSoTienGdHoanTraTruyThu(BigDecimal soTienGdHoanTraTruyThu) {
		this.soTienGdHoanTraTruyThu = soTienGdHoanTraTruyThu;
	}

	public BigDecimal getPhiIsaHoanTraTruyThu() {
		return phiIsaHoanTraTruyThu;
	}

	public void setPhiIsaHoanTraTruyThu(BigDecimal phiIsaHoanTraTruyThu) {
		this.phiIsaHoanTraTruyThu = phiIsaHoanTraTruyThu;
	}

	public BigDecimal getVatPhiIsaHoanTraTruyThu() {
		return vatPhiIsaHoanTraTruyThu;
	}

	public void setVatPhiIsaHoanTraTruyThu(BigDecimal vatPhiIsaHoanTraTruyThu) {
		this.vatPhiIsaHoanTraTruyThu = vatPhiIsaHoanTraTruyThu;
	}

	public BigDecimal getPhiRtmHoanTraTruyThu() {
		return phiRtmHoanTraTruyThu;
	}

	public void setPhiRtmHoanTraTruyThu(BigDecimal phiRtmHoanTraTruyThu) {
		this.phiRtmHoanTraTruyThu = phiRtmHoanTraTruyThu;
	}

	public BigDecimal getVatPhiRtmHoanTraTruyThu() {
		return vatPhiRtmHoanTraTruyThu;
	}

	public void setVatPhiRtmHoanTraTruyThu(BigDecimal vatPhiRtmHoanTraTruyThu) {
		this.vatPhiRtmHoanTraTruyThu = vatPhiRtmHoanTraTruyThu;
	}

	public BigDecimal getTongPhiVatHoanTraTruyThu() {
		return tongPhiVatHoanTraTruyThu;
	}

	public void setTongPhiVatHoanTraTruyThu(BigDecimal tongPhiVatHoanTraTruyThu) {
		this.tongPhiVatHoanTraTruyThu = tongPhiVatHoanTraTruyThu;
	}

	public BigDecimal getTongHoanTraTruyThu() {
		return tongHoanTraTruyThu;
	}

	public void setTongHoanTraTruyThu(BigDecimal tongHoanTraTruyThu) {
		this.tongHoanTraTruyThu = tongHoanTraTruyThu;
	}

	public BigDecimal getPhiXuLyGd() {
		return phiXuLyGd;
	}

	public void setPhiXuLyGd(BigDecimal phiXuLyGd) {
		this.phiXuLyGd = phiXuLyGd;
	}

	public BigDecimal getStGd() {
		return stGd;
	}

	public void setStGd(BigDecimal stGd) {
		this.stGd = stGd;
	}

	public BigDecimal getStTqt() {
		return stTqt;
	}

	public void setStTqt(BigDecimal stTqt) {
		this.stTqt = stTqt;
	}

	public BigDecimal getStQdVnd() {
		return stQdVnd;
	}

	public void setStQdVnd(BigDecimal stQdVnd) {
		this.stQdVnd = stQdVnd;
	}

	public BigDecimal getInterchange() {
		return interchange;
	}

	public void setInterchange(BigDecimal interchange) {
		this.interchange = interchange;
	}

	public String getIssuerCharge() {
		return issuerCharge;
	}

	public void setIssuerCharge(String issuerCharge) {
		this.issuerCharge = issuerCharge;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getCrdpgm() {
		return crdpgm;
	}

	public void setCrdpgm(String crdpgm) {
		this.crdpgm = crdpgm;
	}

	public String getNgayHoanTra() {
		return ngayHoanTra;
	}

	public void setNgayHoanTra(String ngayHoanTra) {
		this.ngayHoanTra = ngayHoanTra;
	}

	public String getTenChuTk() {
		return tenChuTk;
	}

	public void setTenChuTk(String tenChuTk) {
		this.tenChuTk = tenChuTk;
	}

	public BigDecimal getVatPhiIsaGd() {
		return vatPhiIsaGd;
	}

	public void setVatPhiIsaGd(BigDecimal vatPhiIsaGd) {
		this.vatPhiIsaGd = vatPhiIsaGd;
	}

	public BigDecimal getVatPhiRtmGd() {
		return vatPhiRtmGd;
	}

	public void setVatPhiRtmGd(BigDecimal vatPhiRtmGd) {
		this.vatPhiRtmGd = vatPhiRtmGd;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getCardBrn() {
		return cardBrn;
	}

	public void setCardBrn(String cardBrn) {
		this.cardBrn = cardBrn;
	}
	
	

}