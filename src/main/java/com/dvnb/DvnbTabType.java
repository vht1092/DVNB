package com.dvnb;

import com.dvnb.components.*;
import com.dvnb.components.dstqt.CapNhatPhiInterchange;
import com.dvnb.components.dstqt.DoiSoatThanhQuyetToan;
import com.dvnb.components.dstqt.GiaoDichChuaDuocTQT;
import com.dvnb.components.dstqt.GiaoDichDaXuLyLech;
import com.dvnb.components.dstqt.GiaoDichPhatSinhHoanTraLech;
import com.dvnb.components.dstqt.HachToanThanhQuyetToan;
import com.dvnb.components.dstqt.InterchangeTrongThang;
import com.dvnb.components.dstqt.PhanBoInterchangeVeDonVi;
import com.dvnb.components.dstqt.TinhTyGiaTQT;
import com.dvnb.components.dstqt.TruyVanGDDaTQT;
import com.dvnb.components.dvnb.BaoCaoDVNB;
import com.dvnb.components.dvnb.ImportDVNB;
import com.dvnb.components.pbcp.*;
import com.vaadin.ui.Component;

/**
 * Danh sach cac component duoc them vao tabsheet
 * Khi them moi cap nhat vao table dvnb_sys_txn de load vao menu, dvnb_sys_txn.DESCRIPTION can co noi dung nhu caption cua class
 * @see com.dvnb.views.MainView
 * */

public enum DvnbTabType {

	IMPORTINVOICE(ImportInvoice.class,ImportInvoice.CAPTION),
	ENQUIRYINVOICE(EnquirytInvoice.class,EnquirytInvoice.CAPTION),
	IMPORTGSTS010(ImportDVNB.class,ImportDVNB.CAPTION),
	BAOCAODVNB(BaoCaoDVNB.class,BaoCaoDVNB.CAPTION),
	TYGIA(TyGia.class,TyGia.CAPTION),
	DOISOATTQT(DoiSoatThanhQuyetToan.class,DoiSoatThanhQuyetToan.CAPTION),
	TINHTYGIATQT(TinhTyGiaTQT.class,TinhTyGiaTQT.CAPTION),
	TRUYVANGDDATQT(TruyVanGDDaTQT.class,TruyVanGDDaTQT.CAPTION),
	HACHTOANTQT(HachToanThanhQuyetToan.class,HachToanThanhQuyetToan.CAPTION),
	CAPNHATPHIINTERCHANGEHANGNGAY(CapNhatPhiInterchange.class,CapNhatPhiInterchange.CAPTION),
	INTERCHANGETRONGTHANG(InterchangeTrongThang.class,InterchangeTrongThang.CAPTION),
	PHANBOINTERCHANGEVEDONVI(PhanBoInterchangeVeDonVi.class,PhanBoInterchangeVeDonVi.CAPTION),
	GDPHATSINHHOANTRALECH(GiaoDichPhatSinhHoanTraLech.class,GiaoDichPhatSinhHoanTraLech.CAPTION),
	GDDAXULYLECH(GiaoDichDaXuLyLech.class,GiaoDichDaXuLyLech.CAPTION),
	GDCHUADUOCTQT(GiaoDichChuaDuocTQT.class,GiaoDichChuaDuocTQT.CAPTION),
	BATCHTARGOP(IppBatch.class,IppBatch.CAPTION),
	DATAPBCPBYDRIVER(SummaryByDriver.class,SummaryByDriver.CAPTION),
	UPDATEBILLINGGRP(UpdateCodeFee.class,UpdateCodeFee.CAPTION);
	
	private final String caption;
	private final Class<? extends Component> classComponent;

	private DvnbTabType(Class<? extends Component> classComponent,String caption) {
		this.caption = caption;
		this.classComponent = classComponent;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends Component> getClassComponent() {
		return classComponent;
	}	
	
	public static DvnbTabType getTabType(final String caption){
		DvnbTabType result=null;
		for (DvnbTabType tabType:values()){
			if(tabType.getCaption().equals(caption)){
				result=tabType;
				break;
			}
		}
		return result;
	}
	

}
