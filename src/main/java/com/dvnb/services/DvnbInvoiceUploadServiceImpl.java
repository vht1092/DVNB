package com.dvnb.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DvnbInvoiceMc;
import com.dvnb.entities.InvoiceUpload;
import com.dvnb.repositories.DvnbInvoiceMcRepo;
import com.vaadin.server.VaadinServlet;

@Service("dvnbInvoiceUploadService")
@Transactional
public class DvnbInvoiceUploadServiceImpl implements DvnbInvoiceUploadService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DvnbInvoiceMcRepo dvnbInvoiceMcRepo;
	
	protected DataSource localDataSource;
	private final transient TimeConverter timeConverter = new TimeConverter();

	@Override
	public List<InvoiceUpload> searchAllInvoiceUploadSummary(String ky, String crdbrn) throws SQLException {
		
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		switch(crdbrn) {
			case "MC":
				sqlString 	.append("SELECT '' BATCH_ID,MA_DON_VI CN_CHUYEN,'801001809' TAI_KHOAN_CHUYEN,'CHI PHI TU DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_CHUYEN, " + 
						"'VND' LOAI_TIEN , SUM(ROUND(TOTAL_AMT_DRIVER/TOTAL_AMOUNT*AMOUNT)) SO_TIEN, '000' CN_HUONG, '701001809' TAI_KHOAN_HUONG, 'THU NHAP DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_HUONG, " + 
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
						"GIATRI_UDF " + 
						"FROM  " + 
						"( " + 
						"SELECT DRIVER_CDE MA_DRIVER,DRIVER_NAME TEN_DRIVER,SUM(TOTAL_CHARGE*TYGIA+DIAVITION) TOTAL_AMT_DRIVER,A.KY " + 
						"FROM DVNB_INVOICE_MC A " + 
						"LEFT JOIN DVNB_BILLING_GRP B ON A.EVENT_ID=B.BILLING_CDE    " + 
						"LEFT JOIN DVNB_TYGIA N ON N.KY=A.KY AND N.CRD_BRN='MC' " + 
						"WHERE A.KY = ? AND A.KET_CHUYEN = 'Y' " + 
						"GROUP BY DRIVER_CDE,DRIVER_NAME,A.KY " + 
						"ORDER BY MA_DRIVER ASC " + 
						") T1 " + 
						"INNER JOIN dvnb_invoice_tonghop T2 ON T1.MA_DRIVER=T2.MA_DRIVER AND T1.KY=T2.KY  " + 
						"GROUP BY MA_DON_VI,GIATRI_UDF " + 
						"ORDER BY MA_DON_VI");
				break;
			case "VS":
				sqlString 	.append("SELECT '' BATCH_ID,MA_DON_VI CN_CHUYEN,'801001809' TAI_KHOAN_CHUYEN,'CHI PHI TU DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_CHUYEN, " + 
						"'VND' LOAI_TIEN , ROUND(SUM(TOTAL_AMT_DRIVER/TOTAL_AMOUNT*AMOUNT)) SO_TIEN, '000' CN_HUONG, '701001809' TAI_KHOAN_HUONG, 'THU NHAP DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_HUONG, " + 
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
						"GIATRI_UDF " + 
						"FROM  " + 
						"( " + 
						"SELECT DRIVER_CDE MA_DRIVER,DRIVER_NAME TEN_DRIVER,SUM(TOTAL*TYGIA+DIAVITION) TOTAL_AMT_DRIVER,A.KY " + 
						"FROM DVNB_INVOICE_VS A " + 
						"LEFT JOIN DVNB_BILLING_GRP B ON A.BILLING_LINE=B.BILLING_CDE    " + 
						"LEFT JOIN DVNB_TYGIA N ON N.KY=A.KY AND N.CRD_BRN='VS' " + 
						"WHERE A.KY = ? AND A.KET_CHUYEN = 'Y' " + 
						"GROUP BY DRIVER_CDE,DRIVER_NAME,A.KY " + 
						"ORDER BY MA_DRIVER ASC " + 
						") T1 " + 
						"INNER JOIN dvnb_invoice_tonghop T2 ON T1.MA_DRIVER=T2.MA_DRIVER AND T1.KY=T2.KY  " + 
						"GROUP BY MA_DON_VI,GIATRI_UDF " + 
						"ORDER BY MA_DON_VI");
				break;
		}
		
			
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString
				.toString());
		preStmt.setString(1, ky);

		ResultSet rs = preStmt.executeQuery();
		List<InvoiceUpload> invoiceUploadList = new ArrayList<InvoiceUpload>();
		while(rs.next()) {
			
			InvoiceUpload invoiceUpload = new InvoiceUpload();
			
			invoiceUpload.setBatchId(rs.getString(1));
			invoiceUpload.setNgay(timeConverter.getCurrentTime().substring(0, 8));
			invoiceUpload.setChiNhanhChuyen(rs.getString(2));
			invoiceUpload.setTaiKhoanChuyen(rs.getString(3));
			invoiceUpload.setTenNguoiChuyen(rs.getString(4));
			invoiceUpload.setLoaiTien(rs.getString(5));
			invoiceUpload.setSoTien(rs.getString(6));
			invoiceUpload.setChiNhanhHuong(rs.getString(7));
			invoiceUpload.setTaiKhoanHuong(rs.getString(8));
			invoiceUpload.setTenNguoiHuong(rs.getString(9));
			invoiceUpload.setNoiDung(rs.getString(10) + ky);
			invoiceUpload.setProductCode(rs.getString(11));
			invoiceUpload.setTenUdf(rs.getString(12));
			invoiceUpload.setGiaTriUdf(rs.getString(13));

			invoiceUploadList.add(invoiceUpload);
		}
		
		preStmt.close();
		return invoiceUploadList;
	}

	@Override
	public List<InvoiceUpload> searchAllInvoiceUploadDetail(String ky, String crdbrn) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		switch(crdbrn) {
			case "MC":
				sqlString 	.append("SELECT '' BATCH_ID,T1.MA_DRIVER,MA_DON_VI CN_CHUYEN,'801001809' TAI_KHOAN_CHUYEN,'CHI PHI TU DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_CHUYEN, " + 
						"'VND' LOAI_TIEN , ROUND(TOTAL_AMT_DRIVER/TOTAL_AMOUNT*AMOUNT) SO_TIEN, '000' CN_HUONG, '701001809' TAI_KHOAN_HUONG, 'THU NHAP DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_HUONG, " + 
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE SCB THANG 092019' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
						"GIATRI_UDF " + 
						"FROM  " + 
						"( " + 
						"SELECT DRIVER_CDE MA_DRIVER,DRIVER_NAME TEN_DRIVER,SUM(TOTAL_CHARGE*TYGIA+DIAVITION) TOTAL_AMT_DRIVER,A.KY " + 
						"FROM DVNB_INVOICE_MC A " + 
						"LEFT JOIN DVNB_BILLING_GRP B ON A.EVENT_ID=B.BILLING_CDE    " + 
						"LEFT JOIN DVNB_TYGIA N ON N.KY=A.KY AND N.CRD_BRN='MC' " + 
						"WHERE A.KY = ? AND A.KET_CHUYEN = 'Y' " + 
						"GROUP BY DRIVER_CDE,DRIVER_NAME,A.KY " + 
						"ORDER BY MA_DRIVER ASC " + 
						") T1 " + 
						"INNER JOIN dvnb_invoice_tonghop T2 ON T1.MA_DRIVER=T2.MA_DRIVER AND T1.KY=T2.KY " + 
						"ORDER BY MA_DRIVER,MA_DON_VI");
				break;
			case "VS":
				sqlString 	.append("SELECT '' BATCH_ID,T1.MA_DRIVER,MA_DON_VI CN_CHUYEN,'801001809' TAI_KHOAN_CHUYEN,'CHI PHI TU DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_CHUYEN, " + 
						"'VND' LOAI_TIEN , ROUND(TOTAL_AMT_DRIVER/TOTAL_AMOUNT*AMOUNT) SO_TIEN, '000' CN_HUONG, '701001809' TAI_KHOAN_HUONG, 'THU NHAP DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_HUONG, " + 
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
						"GIATRI_UDF " + 
						"FROM  " + 
						"( " + 
						"SELECT DRIVER_CDE MA_DRIVER,DRIVER_NAME TEN_DRIVER,SUM(TOTAL*TYGIA+DIAVITION) TOTAL_AMT_DRIVER,A.KY " + 
						"FROM DVNB_INVOICE_VS A " + 
						"LEFT JOIN DVNB_BILLING_GRP B ON A.BILLING_LINE=B.BILLING_CDE    " + 
						"LEFT JOIN DVNB_TYGIA N ON N.KY=A.KY AND N.CRD_BRN='VS' " + 
						"WHERE A.KY = ? AND A.KET_CHUYEN = 'Y' " + 
						"GROUP BY DRIVER_CDE,DRIVER_NAME,A.KY " + 
						"ORDER BY MA_DRIVER ASC " + 
						") T1 " + 
						"INNER JOIN dvnb_invoice_tonghop T2 ON T1.MA_DRIVER=T2.MA_DRIVER AND T1.KY=T2.KY " + 
						"ORDER BY MA_DRIVER,MA_DON_VI");
				break;
		}
		
			
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString
				.toString());
		preStmt.setString(1, ky);

		ResultSet rs = preStmt.executeQuery();
		List<InvoiceUpload> invoiceUploadList = new ArrayList<InvoiceUpload>();
		while(rs.next()) {
			
			InvoiceUpload invoiceUpload = new InvoiceUpload();
			
			invoiceUpload.setBatchId(rs.getString(1));
			invoiceUpload.setNgay(timeConverter.getCurrentTime().substring(0, 8));
			invoiceUpload.setMaDriver(rs.getString(2));
			invoiceUpload.setChiNhanhChuyen(rs.getString(3));
			invoiceUpload.setTaiKhoanChuyen(rs.getString(4));
			invoiceUpload.setTenNguoiChuyen(rs.getString(5));
			invoiceUpload.setLoaiTien(rs.getString(6));
			invoiceUpload.setSoTien(rs.getString(7));
			invoiceUpload.setChiNhanhHuong(rs.getString(8));
			invoiceUpload.setTaiKhoanHuong(rs.getString(9));
			invoiceUpload.setTenNguoiHuong(rs.getString(10));
			invoiceUpload.setNoiDung(rs.getString(11) + ky);
			invoiceUpload.setProductCode(rs.getString(12));
			invoiceUpload.setTenUdf(rs.getString(13));
			invoiceUpload.setGiaTriUdf(rs.getString(14));

			invoiceUploadList.add(invoiceUpload);
		}
		
		preStmt.close();
		return invoiceUploadList;
	}
		
}
