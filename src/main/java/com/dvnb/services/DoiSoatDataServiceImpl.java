package com.dvnb.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dvnb.entities.DoiSoatData;
import com.dvnb.repositories.DoiSoatDataRepo;

@Service("doiSoatDataService")
@Transactional
public class DoiSoatDataServiceImpl implements DoiSoatDataService {

	@Value("${spring.jpa.properties.hibernate.default_schema}")
	private String sSchema;
	@Autowired
	private DoiSoatDataRepo doiSoatDataRepo;

	@Override
	public Page<DoiSoatData> findAll(Pageable page) {
		return doiSoatDataRepo.findAll(page);
	}

	@Override
	public List<DoiSoatData> findAllTuNgayDenNgay(String tungay, String denngay){
		return doiSoatDataRepo.findAllTuNgayDenNgay(tungay,denngay);
	}
	
	@Override
	public List<DoiSoatData> findAllTuNgayDenNgayAndPanAndApvCodeAndDvcnt(String tungay, String denngay, String cardno, String apvcode, String dvcnt) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllTuNgayDenNgayAndPanAndApvCodeAndDvcnt(tungay, denngay, cardno, apvcode,dvcnt);
	}
	
	@Override
	public List<DoiSoatData> findAllTuNgayDenNgayAndPanAndApvCode(String tungay, String denngay, Set<String> magd, String cardno, String apvcode,String cardType) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllTuNgayDenNgayAndPanAndApvCode(tungay, denngay, magd, cardno, apvcode,cardType);
	}


	
	@Override
	public List<DoiSoatData> findAllByLoaiTheAndLttqtAndNgayAdv(String loaithe, String lttqt, String ngayAdv) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllByLoaiTheAndLttqtAndNgayAdv(loaithe, lttqt,ngayAdv);
	}
	
	@Override
	public void create(DoiSoatData DoiSoatData) {

		doiSoatDataRepo.save(DoiSoatData);
	}

	@Override
	public List<Object[]> findDoiSoatInfoByCardno(String pan) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findDoiSoatInfoByCardno(pan);
	}
	
	@Override
	public void updatePANByID(String id) {
		doiSoatDataRepo.updatePANByID(id);
		
	}

	@Override
	public void updateDoiSoatInfo() {
		doiSoatDataRepo.updateDoiSoatInfo();
		
	}

	@Override
	public String convertPanByCardno(String cardno) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.convertPanByCardno(cardno);
	}
	
	public List<Object[]> getDwhByCardnoAndApvCode(String pan,String apvCode) {
		return doiSoatDataRepo.getDwhByCardnoAndApvCode(pan,apvCode);
	}

	@Override
	public List<Object[]> findDoiSoatInfoByPanAndApvCode(String pan, String apvCode,String ngayGd) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findDoiSoatInfoByPanAndApvCode(pan, apvCode,ngayGd);
	}

	@Override
	public DoiSoatData findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(String cardno, String apvCode,String maGd,String reversalInd,String ngayadv) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findOneByCardnoAndApvcodeAndMagdAndAndReversalIdAndAdvdate(cardno, apvCode,maGd,reversalInd,ngayadv);
	}

	@Override
	public BigDecimal totalStQdVnd(String ngayAdv,String cardType) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.totalStQdVnd(ngayAdv,cardType);
	}
	
	@Override
	public List<DoiSoatData> findGDPhatSinhHoanTraLechTuNgayDenNgayAndLttqtAndLoaiGd(String tungay, String denngay, String lttqt, String loaigd) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findGDPhatSinhHoanTraLechTuNgayDenNgayAndLttqtAndLoaiGd(tungay, denngay, lttqt, loaigd);
	}
	
	@Override
	public List<DoiSoatData> findGDDaXuLyLechTuNgayDenNgayAndLttqtAndLoaiGd(String tungay, String denngay, String lttqt, String loaigd) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findGDDaXuLyLechTuNgayDenNgayAndLttqtAndLoaiGd(tungay, denngay, lttqt, loaigd);
	}


	@Override
	public void updateNgayHoanTra(String ngayHoanTra, String soThe, String maCapPhep, String ngayGd, String trace) {
		// TODO Auto-generated method stub
		doiSoatDataRepo.updateNgayHoanTra(ngayHoanTra, soThe, maCapPhep, ngayGd, trace);
	}
	
	@Override
	public List<String> findMCIncomingFileNameByDate(String fromIncomingDate,String toIncomingDate) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findMCIncomingFileNameByDate(fromIncomingDate,toIncomingDate);
	}

	@Override
	public List<DoiSoatData> findAllGDTTHHDuocTQT(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDTTHHDuocTQT(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDTTHHChiTietGDDuocTQT(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDTTHHChiTietGDDuocTQT(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDTTHHTrichNoBoSungKH(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDTTHHTrichNoBoSungKH(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDTTHHHoanTraTienTrichThua(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDTTHHHoanTraTienTrichThua(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDRTMDuocTQT(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDRTMDuocTQT(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDRTMChiTietGDDuocTQT(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDRTMChiTietGDDuocTQT(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDRTMTrichNoBoSungKH(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDRTMTrichNoBoSungKH(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDRTMHoanTraTienTrichThua(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDRTMHoanTraTienTrichThua(curr);
	}
	
	@Override
	public List<DoiSoatData> findAllGDMoneySendFF(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDMoneySendFF(curr);
	}
	
	@Override
	public List<DoiSoatData> findAllHoanTraGDTTHH(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllHoanTraGDTTHH(curr);
	}

	@Override
	public List<DoiSoatData> findAllHoanTraGDRTM(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllHoanTraGDRTM(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDBaoCoBatThuong(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDBaoCoBatThuong(curr);
	}

	@Override
	public List<DoiSoatData> findAllGDBaoNoBatThuong(String curr) {
		// TODO Auto-generated method stub
		return doiSoatDataRepo.findAllGDBaoNoBatThuong(curr);
	}

	

	
	


	
}
