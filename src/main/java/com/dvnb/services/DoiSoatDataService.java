package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dvnb.entities.DoiSoatData;

public interface DoiSoatDataService {

	/**
	 * Danh sach boi user
	 * 
	 * @param page
	 *            PageRequest
	 * @param userid
	 *            user id
	 * @return Page
	 */
	
	public List<DoiSoatData> findAllMasterByFileIncomName(Set<String> incomingFileName);
	
	public List<DoiSoatData> findAllVisaByFileIncomName(Set<String> incomingFileName);
	
	public DoiSoatData findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(String cardno, String apvCode,String maGd,String reversalInd,String ngayDstqt);
	
	public Page<DoiSoatData> findAll(Pageable page);
	
	public int deleteAllByNgayAdvAndLttqtAndCardbrn(String ngayAdv, String lttqt, String cardbrn);
	
	public List<DoiSoatData> findAllTuNgayDenNgay(String tungay, String denngay);
	
	public List<DoiSoatData> findAllTuNgayDenNgayAndPanAndApvCodeAndDvcntAndCardtype(String tungay, String denngay, String cardno, String apvcode, String dvcnt,String cardtype);
	
	public List<DoiSoatData> findAllTuNgayDenNgayAndMagdAndCardnoAndApvCodeAndCardtype(String tungay, String denngay, Set<String> magd, String cardno, String apvcode,String cardType);
	
	public List<DoiSoatData> findAllByLoaiTheAndLttqtAndNgayAdv(String loaithe, String lttqt, String ngayAdv);
	
	void create(DoiSoatData gstsInvoiceMc);
	
	public List<Object[]> findDoiSoatInfoByCardno(String pan);
	
	public String convertPanByCardno(String cardno);
	
	public void updatePANByID(String id);
	
	public void updateDoiSoatInfo();
	
	public List<Object[]> getDwhByCardnoAndApvCode(String pan,String apvCode);
	
	public List<Object[]> findDoiSoatInfoByPanAndApvCode(String pan, String apvCode, String ngayGd);
	
	public List<Object[]> findDoiSoatCreditInfoByPanAndApvCode(String pan, String apvCode);
	
	public BigDecimal totalStQdVnd(String ngayAdv,String cardType);
	
	public List<DoiSoatData> findGDPhatSinhHoanTraLechTuNgayDenNgayAndLttqtAndLoaiGdAndCardtype(String tungay, String denngay,String lttqt,String loaigd,String cardType);
	
	public List<DoiSoatData> findGDDaXuLyLechTuNgayDenNgayAndLttqtAndLoaiGd(String tungay, String denngay,String lttqt,String loaigd,String cardType);
	
	public void updateNgayHoanTra(String ngayHoanTra,String soThe,String maCapPhep,String ngayGd,String trace); 
	
	public void updateNgayHoanTraById(String ngayHoanTra,String id); 
	
	public List<String> findMCIncomingFileNameByDate(String fromIncomingDate,String toIncomingDate); 
	
	public List<String> findVSIncomingFileNameByDate(String fromIncomingDate,String toIncomingDate); 
	
	public String findBinByCardtype(String crdBrn, int acctPrv);
	
	public String findAccountDescription(String casa);
	
	//1.1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - GD được thanh quyết toán
//	@Query(value = "SELECT * FROM DSQT_DATA " + 
//			"WHERE NGAY_DSTQT = (SELECT MAX(NGAY_DSTQT) FROM DSQT_DATA) " + 
//			"AND ((MA_GD LIKE '00%' AND STATUS_CW=' ') OR (MA_GD LIKE '18%' AND STATUS_CW=' ') OR (MA_GD LIKE '00%' AND REVERSAL_IND='R' AND STATUS_CW<>' '))", nativeQuery = true)
	public List<DoiSoatData> findAllGDTTHHDuocTQT(String curr);
	
	//1.2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
	public List<DoiSoatData> findAllGDTTHHChiTietGDDuocTQT(String curr);
	
	//1.3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	public List<DoiSoatData> findAllGDTTHHTrichNoBoSungKH(String curr);
	
	//1.4. GIAO DỊCH THẺ <MC/VS> DEBIT SCB THANH TOÁN HÀNG HÓA - Trích nợ bổ sung KH các giao dịch TTHH thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	public List<DoiSoatData> findAllGDTTHHHoanTraTienTrichThua(String curr);
		
	//2.1. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - GD được thanh quyết toán
	public List<DoiSoatData> findAllGDRTMDuocTQT(String curr);
	
	//2.2. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - Chi tiết GD được thanh quyết toán có phát sinh chênh lệch
	public List<DoiSoatData> findAllGDRTMChiTietGDDuocTQT(String curr);
	
	//2.3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - Trích nợ bổ sung KH các giao dịch RTM thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	public List<DoiSoatData> findAllGDRTMTrichNoBoSungKH(String curr);
	
	//2.4. GIAO DỊCH THẺ <MC/VS> DEBIT SCB RÚT TIỀN MẶT - Hoàn trả KH tiền trích thừa các giao dịch các giao dịch RTM Thẻ <MC/VS> Debit được thanh quyết toán ngày <ngày file incoming>
	public List<DoiSoatData> findAllGDRTMHoanTraTienTrichThua(String curr);
	
	//3. GIAO DỊCH THẺ <MC/VS> DEBIT SCB NHẬN TIỀN CHUYỂN KHOẢN TỪ THẺ <MC/VS> LIÊN MINH (GIAO DỊCH <MONEYSEND/FASTFUND>)
	public List<DoiSoatData> findAllGDMoneySendFF(String curr);
		
	//4. MC/VS HOÀN TRẢ GD THẺ DEBIT SCB TTHH KHÔNG TC, KH BỊ TRỪ TIỀN 
	public List<DoiSoatData> findAllHoanTraGDTTHH(String curr);
	
	//5. <MC/VS> HOÀN TRẢ GIAO DỊCH THẺ DEBIT SCB RTM KHÔNG TC, KH BỊ TRỪ TIỀN 
	public List<DoiSoatData> findAllHoanTraGDRTM(String curr);
	
	//6. GIAO DỊCH BÁO CÓ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
	public List<DoiSoatData> findAllGDBaoCoBatThuong(String curr);
	
	//7. GIAO DỊCH BÁO NỢ BẤT THƯỜNG THẺ <MC/VS> DEBIT SCB <TTHH/RTM>
	public List<DoiSoatData> findAllGDBaoNoBatThuong(String curr);
	
	
}
