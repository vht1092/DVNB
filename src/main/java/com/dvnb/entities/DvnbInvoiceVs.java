package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DVNB_INVOICE_VS")
@NamedQuery(name = "DvnbInvoiceVs.findAll", query = "SELECT f FROM DvnbInvoiceVs f")
public class DvnbInvoiceVs implements Serializable {
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
	
	@Column(name = "KY", nullable = false, length = 6) 	
	private String ky;
	
	@Column(name = "BILLING_PERIOD", nullable = false, length = 20) 	
	private String billingPeriod;
	
	@Column(name = "INVOICE_DATE", nullable = false, length = 12) 	
	private String invoiceDate;
	
	@Column(name = "INVOICE_ACCOUNT", nullable = false, length = 20) 	
	private String invoiceAccount;
	
	@Column(name = "NAME", nullable = false, length = 60) 	
	private String name;
	
	@Column(name = "INVOICE_ID", nullable = false, length = 20) 	
	private String invoiceId;
	
	@Column(name = "SUB_INVOICE", nullable = false, length = 10) 	
	private String subInvoice;
	
	@Column(name = "CURRENT_OR_PREVIOUS", nullable = false, length = 20) 	
	private String currentOrPrevious;
	
	@Column(name = "ENTITY_TYPE", nullable = false, length = 10) 	
	private String entityType;
	
	@Column(name = "ENTITY_ID", nullable = false, length = 20) 	
	private String entityId;
	
	@Column(name = "BIN_MAP", nullable = false, length = 10) 	
	private String binMap;
	
	@Column(name = "ENTITY_NAME", nullable = false, length = 60) 	
	private String entityName;
	
	@Column(name = "SETTLEMENT_ID", nullable = false, length = 10) 	
	private String settlementId;
	
	@Column(name = "DESCRIPTION", nullable = false, length = 100) 	
	private String description;
	
	@Column(name = "FUTURE_USE", nullable = false, length = 20) 	
	private String futureUse;
	
	@Column(name = "NTWK", nullable = false, length = 20) 	
	private String ntwk;
	
	@Column(name = "BILLING_LINE", nullable = false, length = 20) 	
	private String billingLine;
	
	@Column(name = "TYPE", nullable = false, length = 20) 	
	private String type;
	
	@Column(name = "RATE_TYPE", nullable = false, length = 20) 	
	private String rateType;
	
	@Column(name = "UNITS", nullable = false, length = 20) 	
	private String units;
	
	@Column(name = "RATE_CUR", nullable = false, length = 5) 	
	private String rateCur;
	
	@Column(name = "RATE", nullable = false, length = 20) 	
	private String rate;
	
	@Column(name = "FOREIGN_EXCHANGE_RATE", nullable = false, length = 20) 	
	private String foreignExchangeRate;
	
	@Column(name = "BILLING_CURRENCY", nullable = false, length = 5) 	
	private String billingCurrency;
	
	@Column(name = "DIAVITION", nullable = false, length = 20)
	private String deviation;
	
	@Column(name = "TOTAL", nullable = false, length = 20) 	
	private String total;
	
	@Column(name = "TAX_TYPE", nullable = false, length = 20) 	
	private String taxType;
	
	@Column(name = "TAX", nullable = false, length = 20) 	
	private String tax;
	
	@Column(name = "TAX_RATE", nullable = false, length = 20) 	
	private String taxRate;
	
	@Column(name = "TAX_CURRENCY", nullable = false, length = 20) 	
	private String taxCurrency;
	
	@Column(name = "TAXABLE_AMOUNT", nullable = false, length = 20) 	
	private String taxableAmount;
	
	@Column(name = "TAX_TAX_CURRENCY", nullable = false, length = 20) 	
	private String taxTaxCurrency;
	
	@Column(name = "KET_CHUYEN", nullable = false, length = 20) 	
	private String ketChuyen;
	
	@Column(name = "NGAY_THUC_HIEN", nullable = false, precision = 8)
	private BigDecimal ngayThucHien;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getBillingPeriod() {
		return billingPeriod;
	}

	public void setBillingPeriod(String billingPeriod) {
		this.billingPeriod = billingPeriod;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceAccount() {
		return invoiceAccount;
	}

	public void setInvoiceAccount(String invoiceAccount) {
		this.invoiceAccount = invoiceAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSubInvoice() {
		return subInvoice;
	}

	public void setSubInvoice(String subInvoice) {
		this.subInvoice = subInvoice;
	}

	public String getCurrentOrPrevious() {
		return currentOrPrevious;
	}

	public void setCurrentOrPrevious(String currentOrPrevious) {
		this.currentOrPrevious = currentOrPrevious;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getBinMap() {
		return binMap;
	}

	public void setBinMap(String binMap) {
		this.binMap = binMap;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(String settlementId) {
		this.settlementId = settlementId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFutureUse() {
		return futureUse;
	}

	public void setFutureUse(String futureUse) {
		this.futureUse = futureUse;
	}

	public String getNtwk() {
		return ntwk;
	}

	public void setNtwk(String ntwk) {
		this.ntwk = ntwk;
	}

	public String getBillingLine() {
		return billingLine;
	}

	public void setBillingLine(String billingLine) {
		this.billingLine = billingLine;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getRateCur() {
		return rateCur;
	}

	public void setRateCur(String rateCur) {
		this.rateCur = rateCur;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getForeignExchangeRate() {
		return foreignExchangeRate;
	}

	public void setForeignExchangeRate(String foreignExchangeRate) {
		this.foreignExchangeRate = foreignExchangeRate;
	}

	public String getBillingCurrency() {
		return billingCurrency;
	}

	public void setBillingCurrency(String billingCurrency) {
		this.billingCurrency = billingCurrency;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxCurrency() {
		return taxCurrency;
	}

	public void setTaxCurrency(String taxCurrency) {
		this.taxCurrency = taxCurrency;
	}

	public String getTaxableAmount() {
		return taxableAmount;
	}

	public void setTaxableAmount(String taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	public String getTaxTaxCurrency() {
		return taxTaxCurrency;
	}

	public void setTaxTaxCurrency(String taxTaxCurrency) {
		this.taxTaxCurrency = taxTaxCurrency;
	}

	public String getKetChuyen() {
		return ketChuyen;
	}

	public void setKetChuyen(String ketChuyen) {
		this.ketChuyen = ketChuyen;
	}

	public BigDecimal getNgayThucHien() {
		return ngayThucHien;
	}

	public void setNgayThucHien(BigDecimal ngayThucHien) {
		this.ngayThucHien = ngayThucHien;
	}

	public String getKy() {
		return ky;
	}

	public void setKy(String ky) {
		this.ky = ky;
	}

	public String getDeviation() {
		return deviation;
	}

	public void setDeviation(String deviation) {
		this.deviation = deviation;
	}

	
	
}