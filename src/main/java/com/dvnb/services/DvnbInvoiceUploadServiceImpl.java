package com.dvnb.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
						"'VND' LOAI_TIEN , ROUND(SUM(TOTAL_AMT_DRIVER/TOTAL_AMOUNT*AMOUNT)) SO_TIEN, '000' CN_HUONG, '701001809' TAI_KHOAN_HUONG, 'THU NHAP DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_HUONG, " + 
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE ' || (CASE WHEN T2.CRD_BRN='MC' THEN 'MASTERCARD ' ELSE 'VISA ' END) || T2.LOAI_THE || ' SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
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
						"GROUP BY MA_DON_VI,GIATRI_UDF,T2.CRD_BRN,T2.LOAI_THE " + 
						"ORDER BY MA_DON_VI");
				break;
			case "VS":
				sqlString 	.append("SELECT '' BATCH_ID,MA_DON_VI CN_CHUYEN,'801001809' TAI_KHOAN_CHUYEN,'CHI PHI TU DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_CHUYEN, " + 
						"'VND' LOAI_TIEN , ROUND(SUM(TOTAL_AMT_DRIVER/TOTAL_AMOUNT*AMOUNT)) SO_TIEN, '000' CN_HUONG, '701001809' TAI_KHOAN_HUONG, 'THU NHAP DIEU CHUYEN NOI BO KHAC' TEN_NGUOI_HUONG, " + 
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE ' || (CASE WHEN T2.CRD_BRN='MC' THEN 'MASTERCARD ' ELSE 'VISA ' END) || T2.LOAI_THE || ' SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
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
						"GROUP BY MA_DON_VI,GIATRI_UDF,T2.CRD_BRN,T2.LOAI_THE " + 
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
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE ' || (CASE WHEN T2.CRD_BRN='MC' THEN 'MASTERCARD ' ELSE 'VISA ' END) || T2.LOAI_THE || ' SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
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
						"'HT CHI PHI DICH VU KHAC - THE QUOC TE ' || (CASE WHEN T2.CRD_BRN='MC' THEN 'MASTERCARD ' ELSE 'VISA ' END) || T2.LOAI_THE || ' SCB THANG ' NOI_DUNG, 'UPPB' PRODUCT_CODE, 'SAN_PHAM_ERP~KHACH_HANG_ERP~PHONG_BAN_ERP' TEN_UDF, " + 
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

	@Override
	public List<Object[]> getDataInvoiceUploadByBIN(String ky, String crdbrn) throws SQLException {
		// TODO Auto-generated method stub
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		localDataSource = (DataSource) helper.getBean("dataSource");
		
		StringBuilder sqlString = new StringBuilder();

		switch(crdbrn) {
			case "VS":
				sqlString 	.append("select * from (\r\n" + 
						"   select GRP,BIN,TOTAL_AMT_INV_GRP/TOTAL_AMT*TOTAL_AMT_BIN PHAN_BO\r\n" + 
						"   from\r\n" + 
						"    (\r\n" + 
						"        WITH t AS\r\n" + 
						"        (\r\n" + 
						"            SELECT GRP,SUM(TOTAL*TYGIA+DIAVITION) TOTAL_AMT_INV_GRP ,A.KY\r\n" + 
						"            FROM DVNB_INVOICE_VS A\r\n" + 
						"            LEFT JOIN DVNB_BILLING_GRP B ON A.BILLING_LINE=B.BILLING_CDE\r\n" + 
						"            LEFT JOIN DVNB_TYGIA N ON N.KY=A.KY AND N.CRD_BRN='VS'\r\n" + 
						"            WHERE A.KY=022020 AND A.KET_CHUYEN='Y'\r\n" + 
						"            GROUP BY GRP,A.KY\r\n" + 
						"            ORDER BY GRP\r\n" + 
						"        )\r\n" + 
						"        \r\n" + 
						"        SELECT A.GRP,BIN,t.TOTAL_AMT_INV_GRP,SUM(AMOUNT) TOTAL_AMT_BIN,SUM(SUM(AMOUNT)) OVER (PARTITION BY A.GRP) AS TOTAL_AMT--,\r\n" + 
						"        --t.TOTAL_AMT_INV_GRP/(SUM(SUM(AMOUNT)) OVER (PARTITION BY A.GRP))*SUM(SUM(AMOUNT)) PHAN_BO_CHI_PHI\r\n" + 
						"        FROM t\r\n" + 
						"        LEFT JOIN DVNB_BILLING_GRP A  ON A.GRP=t.GRP\r\n" + 
						"        LEFT JOIN dvnb_inv_tonghop_test B ON B.MA_DRIVER=A.DRIVER_CDE AND B.CRD_BRN=A.CRD_BRN \r\n" + 
						"        WHERE B.KY=022020 AND B.CRD_BRN='VS'\r\n" + 
						"        GROUP BY A.GRP,BIN,TOTAL_AMT_INV_GRP\r\n" + 
						"        ORDER BY A.GRP,BIN\r\n" + 
						"    )   \r\n" + 
						")\r\n" + 
						"pivot \r\n" + 
						"(\r\n" + 
						"   MIN(PHAN_BO)\r\n" + 
						"   for BIN in (157979,453618,510235,512454,524188,545579)\r\n" + 
						")");
				break;
		}
		
			
		Connection connect = localDataSource.getConnection();

		PreparedStatement preStmt = connect.prepareStatement(sqlString.toString());
		ResultSet rs = preStmt.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		List<Object[]> records=new ArrayList<Object[]>();
		
		int cols = rs.getMetaData().getColumnCount();
		Object[] arr = new Object[cols];
		for (int i = 0; i < cols; i++ ) {
			arr[i] = rs.getMetaData().getColumnName(i+1);
		}
		records.add(arr);
		
		while(rs.next()) {
			arr = new Object[cols];
			// The column count starts from 1
			for (int i = 0; i < cols; i++ ) {
				arr[i] = rs.getObject(i+1);
			}
			records.add(arr);
		}
		
		preStmt.close();
		return records;
	}
		
}
