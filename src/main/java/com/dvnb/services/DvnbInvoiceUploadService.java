package com.dvnb.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.InvoiceUpload;

public interface DvnbInvoiceUploadService {

	public List<InvoiceUpload> searchAllInvoiceUploadSummary(String ky, String crdbrn) throws SQLException;
	
	public List<InvoiceUpload> searchAllInvoiceUploadDetail(String ky, String crdbrn) throws SQLException ;
	
	public List<Object[]> getDataInvoiceUploadByBIN(String ky, String crdbrn) throws SQLException;
	
}
