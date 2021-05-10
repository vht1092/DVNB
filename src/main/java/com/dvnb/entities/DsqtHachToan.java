package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DSQT_HACH_TOAN")
@NamedQuery(name = "DsqtHachToan.findAll", query = "SELECT f FROM DsqtHachToan f")
public class DsqtHachToan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false, length = 11)
	private String id;
	
	@Column(name = "CRE_TMS", nullable = false, precision = 17)
	private BigDecimal creTms;
	
	@Column(name = "USR_ID", nullable = false, length = 12)
	private String usrId;
	
	@Column(name = "NGAY_ADV", nullable = false, length = 8)
	private String ngayAdv;
	
	@Column(name = "TRANS_TYPE", nullable = false, length = 10)
	private String transType;
	
	@Column(name = "CARD_BRN", nullable = false, length = 3)
	private String cardBrn;
	
	@Column(name = "CURR", nullable = false, length = 3)
	private String curr;
	
	@Column(name = "CYCLE_NO", nullable = false, precision = 3)
	private BigDecimal cycleNo;
	
	@Column(name = "COUNT", nullable = false, precision = 8)
	private BigDecimal count;
	
	@Column(name = "AMOUNT", nullable = false, precision = 20,scale = 6)
	private BigDecimal amount;
	
	@Column(name = "AMOUNT_TYPE", nullable = false, length = 2)
	private String amountType;
	
	@Column(name = "TRANS_FEE", nullable = false, precision = 20,scale = 6)
	private BigDecimal transFee;
	
	@Column(name = "TRANS_FEE_TYPE", nullable = false, length = 2)
	private String transFeeType;
	
	@Column(name = "T140_DATE", nullable = false, length = 8)
	private String t140Date;
	
	@Column(name = "FILE_NAME", nullable = false, length = 100)
	private String filename;
	
	@Column(name = "REVERSAL", nullable = false, length = 1)
	private String reversal;
	
	
	public BigDecimal getCreTms() {
		return creTms;
	}

	public void setCreTms(BigDecimal creTms) {
		this.creTms = creTms;
	}


	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getNgayAdv() {
		return ngayAdv;
	}

	public void setNgayAdv(String ngayAdv) {
		this.ngayAdv = ngayAdv;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
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

	public BigDecimal getCycleNo() {
		return cycleNo;
	}

	public void setCycleNo(BigDecimal cycleNo) {
		this.cycleNo = cycleNo;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public BigDecimal getTransFee() {
		return transFee;
	}

	public void setTransFee(BigDecimal transFee) {
		this.transFee = transFee;
	}

	public String getTransFeeType() {
		return transFeeType;
	}

	public void setTransFeeType(String transFeeType) {
		this.transFeeType = transFeeType;
	}

	public String getT140Date() {
		return t140Date;
	}

	public void setT140Date(String t140Date) {
		this.t140Date = t140Date;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getReversal() {
		return reversal;
	}

	public void setReversal(String reversal) {
		this.reversal = reversal;
	}

	
	

}