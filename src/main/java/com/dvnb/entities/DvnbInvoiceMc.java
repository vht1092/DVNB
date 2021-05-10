package com.dvnb.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "DVNB_INVOICE_MC")
@NamedQuery(name = "DvnbInvoiceMc.findAll", query = "SELECT f FROM DvnbInvoiceMc f")
public class DvnbInvoiceMc implements Serializable {
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
	
	@Column(name = "DOC_TYPE", nullable = false, length = 20)
	private String docType;
	
	@Column(name = "INVOICE_NUMBER", nullable = false, length = 50)
	private String invoiceNumber;
	
	@Column(name = "CURRENCY", nullable = false, length = 5)
	private String currency;
	
	@Column(name = "BILLING_CYCLE_DATE", nullable = false, length = 22)
	private String billingCycleDate;
	
	@Column(name = "INVOICE_ICA", nullable = false, length = 10)
	private String invoiceIca;
	
	@Column(name = "ACTIVITY_ICA", nullable = false, length = 10)
	private String activityIca;
	
	@Column(name = "BILLABLE_ICA", nullable = false, length = 10)
	private String billableIca;
	
	@Column(name = "COLLECTION_METHOD", nullable = false, length = 10)
	private String collectionMethod;
	
	@Column(name = "SERVICE_CODE", nullable = false, length = 10)
	private String serviceCode;
	
	@Column(name = "SERVICE_CODE_DESC", nullable = false, length = 60)
	private String serviceCodeDesc;
	
	@Column(name = "PERIOD_START_TIME", nullable = false, length = 20)
	private String periodStartTime;
	
	@Column(name = "PERIOD_END_TIME", nullable = false, length = 20)
	private String periodEndTime;
	
	@Column(name = "ORIGINAL_INVOICE_NUMBER", nullable = false, length = 20)
	private String originalInvoiceNumber;
	
	@Column(name = "EVENT_ID", nullable = false, length = 20)
	private String eventId;
	
	@Column(name = "EVENT_DESC", nullable = false, length = 200)
	private String eventDesc;
	
	@Column(name = "AFFILIATE", nullable = false, length = 20)
	private String affiliate;
	
	@Column(name = "UOM", nullable = false, length = 1)
	private String uom;
	
	@Column(name = "QUANTITY_AMOUNT", nullable = false, length = 20)
	private String quantityAmount;
	
	@Column(name = "RATE", nullable = false, length = 20)
	private String rate;
	
	@Column(name = "CHARGE", nullable = false, length = 20)
	private String charge;
	
	@Column(name = "TAX_CHARGE", nullable = false, length = 20)
	private String taxCharge;
	
	@Column(name = "DIAVITION", nullable = false, length = 20)
	private String deviation;
	
	@Column(name = "TOTAL_CHARGE", nullable = false, length = 20)
	private String totalCharge;
	
	@Column(name = "VAT_CHARGE", nullable = false, length = 20)
	private String vatCharge;
	
	@Column(name = "VAT_CURRENCY", nullable = false, length = 20)
	private String vatCurrency;
	
	@Column(name = "VAT_CODE", nullable = false, length = 20)
	private String vatCode;
	
	@Column(name = "VAT_RATE", nullable = false, length = 20)
	private String vatRate;
	
	@Column(name = "SBF_EXPLANATORY_TEXT", nullable = false, length = 1000)
	private String sbfExplanatoryText;
	
	@Column(name = "KET_CHUYEN", nullable = false, length = 20)
	private String ketChuyen;
	
	@Column(name = "NGAY_THUC_HIEN", nullable = false, precision = 8)
	private BigDecimal ngayThucHien;
	
	@Column(name = "KY_MOI", nullable = false, length = 6) 	
	private String kyMoi;
	
	@Column(name = "OLD_ID", nullable = false, length = 24)
	private String oldId;

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

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBillingCycleDate() {
		return billingCycleDate;
	}

	public void setBillingCycleDate(String billingCycleDate) {
		this.billingCycleDate = billingCycleDate;
	}

	public String getInvoiceIca() {
		return invoiceIca;
	}

	public void setInvoiceIca(String invoiceIca) {
		this.invoiceIca = invoiceIca;
	}

	public String getActivityIca() {
		return activityIca;
	}

	public void setActivityIca(String activityIca) {
		this.activityIca = activityIca;
	}

	public String getBillableIca() {
		return billableIca;
	}

	public void setBillableIca(String billableIca) {
		this.billableIca = billableIca;
	}

	public String getCollectionMethod() {
		return collectionMethod;
	}

	public void setCollectionMethod(String collectionMethod) {
		this.collectionMethod = collectionMethod;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceCodeDesc() {
		return serviceCodeDesc;
	}

	public void setServiceCodeDesc(String serviceCodeDesc) {
		this.serviceCodeDesc = serviceCodeDesc;
	}

	public String getPeriodStartTime() {
		return periodStartTime;
	}

	public void setPeriodStartTime(String periodStartTime) {
		this.periodStartTime = periodStartTime;
	}

	public String getPeriodEndTime() {
		return periodEndTime;
	}

	public void setPeriodEndTime(String periodEndTime) {
		this.periodEndTime = periodEndTime;
	}

	public String getOriginalInvoiceNumber() {
		return originalInvoiceNumber;
	}

	public void setOriginalInvoiceNumber(String originalInvoiceNumber) {
		this.originalInvoiceNumber = originalInvoiceNumber;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public String getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(String affiliate) {
		this.affiliate = affiliate;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getQuantityAmount() {
		return quantityAmount;
	}

	public void setQuantityAmount(String quantityAmount) {
		this.quantityAmount = quantityAmount;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getTaxCharge() {
		return taxCharge;
	}

	public void setTaxCharge(String taxCharge) {
		this.taxCharge = taxCharge;
	}

	public String getTotalCharge() {
		return totalCharge;
	}

	public void setTotalCharge(String totalCharge) {
		this.totalCharge = totalCharge;
	}

	public String getVatCharge() {
		return vatCharge;
	}

	public void setVatCharge(String vatCharge) {
		this.vatCharge = vatCharge;
	}

	public String getVatCurrency() {
		return vatCurrency;
	}

	public void setVatCurrency(String vatCurrency) {
		this.vatCurrency = vatCurrency;
	}

	public String getVatCode() {
		return vatCode;
	}

	public void setVatCode(String vatCode) {
		this.vatCode = vatCode;
	}

	public String getVatRate() {
		return vatRate;
	}

	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}

	public String getSbfExplanatoryText() {
		return sbfExplanatoryText;
	}

	public void setSbfExplanatoryText(String sbfExplanatoryText) {
		this.sbfExplanatoryText = sbfExplanatoryText;
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

	public String getKyMoi() {
		return kyMoi;
	}

	public void setKyMoi(String kyMoi) {
		this.kyMoi = kyMoi;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	
}