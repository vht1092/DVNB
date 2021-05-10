package com.dvnb.components.dstqt;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;

import com.dvnb.SpringConfigurationValueHelper;
import com.dvnb.SpringContextHelper;
import com.dvnb.TimeConverter;
import com.dvnb.entities.DoiSoatData;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringComponent
@Scope("prototype")
public class DataGridGiaoDichTQTChoXuLy extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DataGridGiaoDichTQTChoXuLy.class);
	private final transient TimeConverter timeConverter = new TimeConverter();
	public transient Grid grid;
	
	public final transient Label lbNoDataFound;
	private transient IndexedContainer container;
	private String txnStatus = "";
	
	private SpringConfigurationValueHelper configurationHelper;
	protected DataSource localDataSource;
	
	public Page<DoiSoatData> dataSource;

	int stt=0;
	
	Date d1 = null;
	Date d2 = null;
	private String cardType = "";
	
	@SuppressWarnings("unchecked")
	public DataGridGiaoDichTQTChoXuLy(String _txnStatus) {

		setSizeFull();

		// init SpringContextHelper de truy cap service bean
		final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
		this.txnStatus = _txnStatus;
		
		// init label
		lbNoDataFound = new Label("Không tìm thấy dữ liệu");
		lbNoDataFound.setVisible(false);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_FAILURE);
		lbNoDataFound.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		lbNoDataFound.setSizeUndefined();

		// init grid
		grid = new Grid();
//		grid.setVisible(false);
		grid.setSizeFull();
		grid.setHeightByRows(15);
//		grid.setReadOnly(true);
		grid.setHeightMode(HeightMode.ROW);
		
		
		if(this.txnStatus.equals("GDPSHTL")) 
			grid.setEditorEnabled(true);
		else
			grid.setSelectionMode(SelectionMode.MULTI);
		
		container = new IndexedContainer();
		
		if(this.txnStatus.equals("GDPSHTL")) {
			container.addContainerProperty("thoiGianChoXuLy", Long.class, "");
		}
		
		container.addContainerProperty("ngayHoanTra", String.class, "");
		container.addContainerProperty("stt", Integer.class, 0);
		container.addContainerProperty("soThe", String.class, "");
		container.addContainerProperty("maGd", String.class, "");
		container.addContainerProperty("ngayGd", String.class, "");
		container.addContainerProperty("ngayFileIncoming", String.class, "");
		container.addContainerProperty("stGd", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("stTqt", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("stQdVnd", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("ltgd", String.class, "");
		container.addContainerProperty("lttqt", String.class, "");
		container.addContainerProperty("interchange", BigDecimal.class, BigDecimal.ZERO);
		container.addContainerProperty("maCapPhep", String.class, "");
		container.addContainerProperty("dvcnt", String.class, "");
		
		
		
		
		
		grid.setContainerDataSource(container);
		if(this.txnStatus.equals("GDPSHTL")) {
			grid.getColumn("thoiGianChoXuLy").setHeaderCaption("Thời gian chờ xử lý");
			grid.getColumn("thoiGianChoXuLy").setEditable(false);
		}
		grid.getColumn("ngayHoanTra").setHeaderCaption("Ngày hoàn trả");
		grid.getColumn("ngayHoanTra").setEditable(true);
		grid.getColumn("stt").setEditable(false);
		grid.getColumn("soThe").setEditable(false);
		grid.getColumn("maGd").setEditable(false);
		grid.getColumn("ngayGd").setEditable(false);
		grid.getColumn("ngayFileIncoming").setEditable(false);
		grid.getColumn("stGd").setEditable(false);
		grid.getColumn("stTqt").setEditable(false);
		grid.getColumn("stQdVnd").setEditable(false);
		grid.getColumn("ltgd").setEditable(false);
		grid.getColumn("lttqt").setEditable(false);
		grid.getColumn("interchange").setEditable(false);
		grid.getColumn("maCapPhep").setEditable(false);
		grid.getColumn("dvcnt").setEditable(false);
		
		
		grid.getColumn("stt").setHeaderCaption("STT");
		grid.getColumn("soThe").setHeaderCaption("Số thẻ");
		grid.getColumn("maGd").setHeaderCaption("Mã GD");
		grid.getColumn("ngayGd").setHeaderCaption("Ngày GD");
		grid.getColumn("ngayFileIncoming").setHeaderCaption("Ngày file Incoming");
		grid.getColumn("stGd").setHeaderCaption("ST GD");
		grid.getColumn("stTqt").setHeaderCaption("ST TQT");
		grid.getColumn("stQdVnd").setHeaderCaption("ST QD VND");
		grid.getColumn("ltgd").setHeaderCaption("LDGD");
		grid.getColumn("lttqt").setHeaderCaption("LTTQT");
		grid.getColumn("interchange").setHeaderCaption("Interchange");
		grid.getColumn("maCapPhep").setHeaderCaption("Mã cấp phép");
		grid.getColumn("dvcnt").setHeaderCaption("ĐVCNT");

		
		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);
	}

	public void initGrid(String _cardType, final String getColumn) {
		this.cardType = _cardType;
		
		switch(cardType) {
			case "MD" : case "VSD" : 
				container.removeContainerProperty("loaiTienNguyenTeGd");
				container.removeContainerProperty("stgdNguyenTeGd");
				container.removeContainerProperty("loaiTienNguyenTe");
				container.removeContainerProperty("stgdNguyenTeChenhLech");
				container.removeContainerProperty("stTrichNoKhGd");
				container.removeContainerProperty("stgdChenhLechDoTyGia");
				container.removeContainerProperty("phiIsa");
				container.removeContainerProperty("vatPhiIsa");
				container.removeContainerProperty("phiRtm");
				container.removeContainerProperty("vatPhiRtm");
				container.removeContainerProperty("tenChuThe");
				container.removeContainerProperty("dvpht");
				container.removeContainerProperty("loc");
				container.removeContainerProperty("id");
				
				container.addContainerProperty("stTrichNoKhGd", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("phiXuLyGd", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("reversalInd", String.class, "");
				container.addContainerProperty("issuerCharge", String.class, "");
				container.addContainerProperty("statusCW", String.class, "");
				container.addContainerProperty("soTienGdHoanTra", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("phiIsaHoanTra", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("vatPhiIsaHoanTra", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("tongPhiVatTruyThu", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("tongHoanTra", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("tenChuThe", String.class, "");
				container.addContainerProperty("casa", String.class, "");
				container.addContainerProperty("dvpht", String.class, "");
				container.addContainerProperty("trace", String.class, "");
				container.addContainerProperty("adv", String.class, "");
				container.addContainerProperty("id", String.class, "");
				
				grid.getColumn("stTrichNoKhGd").setEditable(false);
				grid.getColumn("stgdChenhLechDoTyGia").setEditable(false);
				grid.getColumn("stgdNguyenTeGd").setEditable(false);
				grid.getColumn("stgdNguyenTeChenhLech").setEditable(false);
				grid.getColumn("phiXuLyGd").setEditable(false);
				grid.getColumn("reversalInd").setEditable(false);
				grid.getColumn("issuerCharge").setEditable(false);
				grid.getColumn("statusCW").setEditable(false);
				grid.getColumn("soTienGdHoanTra").setEditable(false);
				grid.getColumn("phiIsaHoanTra").setEditable(false);
				grid.getColumn("vatPhiIsaHoanTra").setEditable(false);
				grid.getColumn("tongPhiVatTruyThu").setEditable(false);
				grid.getColumn("tongHoanTra").setEditable(false);
				grid.getColumn("tenChuThe").setEditable(false);
				grid.getColumn("casa").setEditable(false);
				grid.getColumn("dvpht").setEditable(false);
				grid.getColumn("trace").setEditable(false);
				grid.getColumn("adv").setEditable(false);
				grid.getColumn("id").setEditable(false);
				
				grid.getColumn("stTrichNoKhGd").setHeaderCaption("ST trích nợ KH thời điểm GD");
				grid.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("STGD chênh lệch do tỷ giá");
				grid.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ thời điểm GD");
				grid.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
				grid.getColumn("phiXuLyGd").setHeaderCaption("Phí xử lý GD");
				grid.getColumn("reversalInd").setHeaderCaption("Reversal Indicator");
				grid.getColumn("issuerCharge").setHeaderCaption("Issuer charge");
				grid.getColumn("statusCW").setHeaderCaption("Status trên CW");
				grid.getColumn("soTienGdHoanTra").setHeaderCaption("Số tiền GD hoàn trả");
				grid.getColumn("phiIsaHoanTra").setHeaderCaption("Phí ISA hoàn trả");
				grid.getColumn("vatPhiIsaHoanTra").setHeaderCaption("VAT phí ISA hoàn trả");
				grid.getColumn("tongPhiVatTruyThu").setHeaderCaption("Tổng phí + VAT truy thu");
				grid.getColumn("tongHoanTra").setHeaderCaption("Tổng hoàn trả");
				grid.getColumn("tenChuThe").setHeaderCaption("Tên chủ thẻ");
				grid.getColumn("casa").setHeaderCaption("CASA");
				grid.getColumn("dvpht").setHeaderCaption("ĐVPHT");
				grid.getColumn("trace").setHeaderCaption("Trace");
				grid.getColumn("adv").setHeaderCaption("ADV");
				grid.getColumn("id").setHeaderCaption("ID");
				break;
			case "MC" : case "VS":
				container.removeContainerProperty("stTrichNoKhGd");
				container.removeContainerProperty("stgdChenhLechDoTyGia");
				container.removeContainerProperty("stgdNguyenTeGd");
				container.removeContainerProperty("stgdNguyenTeChenhLech");
				container.removeContainerProperty("phiXuLyGd");
				container.removeContainerProperty("reversalInd");
				container.removeContainerProperty("issuerCharge");
				container.removeContainerProperty("statusCW");
				container.removeContainerProperty("soTienGdHoanTra");
				container.removeContainerProperty("phiIsaHoanTra");
				container.removeContainerProperty("vatPhiIsaHoanTra");
				container.removeContainerProperty("tongPhiVatTruyThu");
				container.removeContainerProperty("tongHoanTra");
				container.removeContainerProperty("tenChuThe");
				container.removeContainerProperty("casa");
				container.removeContainerProperty("dvpht");
				container.removeContainerProperty("trace");
				container.removeContainerProperty("adv");
				container.removeContainerProperty("id");
				
				container.addContainerProperty("stgdNguyenTeGd", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("loaiTienNguyenTe", String.class, "");
				container.addContainerProperty("stgdNguyenTeChenhLech", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("stTrichNoKhGd", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("stgdChenhLechDoTyGia", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("phiIsa", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("vatPhiIsa", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("phiRtm", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("vatPhiRtm", BigDecimal.class, BigDecimal.ZERO);
				container.addContainerProperty("tenChuThe", String.class, "");
				container.addContainerProperty("dvpht", String.class, "");
				container.addContainerProperty("loc", String.class, "");
				container.addContainerProperty("id", String.class, "");
				
				grid.getColumn("stgdNguyenTeGd").setEditable(false);
				grid.getColumn("loaiTienNguyenTe").setEditable(false);
				grid.getColumn("stgdNguyenTeChenhLech").setEditable(false);
				grid.getColumn("stTrichNoKhGd").setEditable(false);
				grid.getColumn("stgdChenhLechDoTyGia").setEditable(false);
				grid.getColumn("phiIsa").setEditable(false);
				grid.getColumn("vatPhiIsa").setEditable(false);
				grid.getColumn("phiRtm").setEditable(false);
				grid.getColumn("vatPhiRtm").setEditable(false);
				grid.getColumn("tenChuThe").setEditable(false);
				grid.getColumn("dvpht").setEditable(false);
				grid.getColumn("loc").setEditable(false);
				grid.getColumn("id").setEditable(false);
				
				grid.getColumn("stgdNguyenTeGd").setHeaderCaption("STGD nguyên tệ");
				grid.getColumn("loaiTienNguyenTe").setHeaderCaption("Loại tiền nguyên tệ");
				grid.getColumn("stgdNguyenTeChenhLech").setHeaderCaption("STGD nguyên tệ chênh lệch");
				grid.getColumn("stTrichNoKhGd").setHeaderCaption("STGD ghi nợ KH (VND)");
				grid.getColumn("stgdChenhLechDoTyGia").setHeaderCaption("Chênh lệch quy đổi VND");
				grid.getColumn("phiIsa").setHeaderCaption("Phí ISA");
				grid.getColumn("vatPhiIsa").setHeaderCaption("VAT phí ISA");
				grid.getColumn("phiRtm").setHeaderCaption("Phí RTM");
				grid.getColumn("vatPhiRtm").setHeaderCaption("VAT phí RTM");
				grid.getColumn("tenChuThe").setHeaderCaption("Tên chủ thẻ");
				grid.getColumn("dvpht").setHeaderCaption("ĐVPHT");
				grid.getColumn("loc").setHeaderCaption("LOC");
				break;
		}
		
		
		if (createDataForContainer() == false) {
			if (!lbNoDataFound.isVisible() && dataSource != null) {
				lbNoDataFound.setVisible(true);
				grid.setVisible(false);
			}
		} else {
			if (!grid.isVisible()) {
				grid.setVisible(true);
				lbNoDataFound.setVisible(false);
			}
		}
        
	}
	
	public void refreshData() {
		getUI().access(() -> {
			if (createDataForContainer() == false) {
				if (!lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(true);
				}
				if (grid.isVisible()) {
					grid.setVisible(false);
				}
			} else {
				if (lbNoDataFound.isVisible()) {
					lbNoDataFound.setVisible(false);
				}
				if (!grid.isVisible()) {
					grid.setVisible(true);
				}
			}
		});
	}
	int i=0;
	@SuppressWarnings("unchecked")
	private boolean createDataForContainer() {
		boolean flag = false;
		stt = 0;
		
		
		if (dataSource != null && dataSource.getTotalElements()>0) {
			container.removeAllItems();
			dataSource.forEach(s -> {
				i++;
				stt++;
				Item item = container.getItem(container.addItem());
				if(this.txnStatus.equals("GDPSHTL")) {
					String ngayFileIncoming = s.getNgayFileIncoming();
					LOGGER.info("ngayFileIncoming: " + ngayFileIncoming);
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date dateNow = new Date();
					try {
						d1 = format.parse(ngayFileIncoming);
						d2 = format.parse(format.format(dateNow));
						long diff = d2.getTime() - d1.getTime();
						long diffDays = diff / (24 * 60 * 60 * 1000);
						item.getItemProperty("thoiGianChoXuLy").setValue(diffDays);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				if(this.txnStatus.equals("GDPSHTL")) 
					item.getItemProperty("ngayHoanTra").setValue("");
				else
					item.getItemProperty("ngayHoanTra").setValue(s.getNgayHoanTra());
				item.getItemProperty("stt").setValue(stt);
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(s.getStGd());
				item.getItemProperty("stTqt").setValue(s.getStTqt());
				item.getItemProperty("stQdVnd").setValue(s.getStQdVnd());
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(s.getInterchange());
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("stTrichNoKhGd").setValue(s.getStTrichNoKhGd());
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(s.getStgdChenhLechDoTyGia());
				item.getItemProperty("stgdNguyenTeGd").setValue(s.getStgdNguyenTeGd());
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(s.getStgdNguyenTeChenhLech());
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("id").setValue(s.getId());
				
				if(cardType.matches("(MD|VSD)")) {
					item.getItemProperty("phiXuLyGd").setValue(s.getPhiXuLyGd());
					item.getItemProperty("reversalInd").setValue(s.getReversalInd());
					item.getItemProperty("issuerCharge").setValue(s.getIssuerCharge());
					item.getItemProperty("statusCW").setValue(s.getStatusCw());
					item.getItemProperty("soTienGdHoanTra").setValue(s.getSoTienGdHoanTraTruyThu());
					item.getItemProperty("phiIsaHoanTra").setValue(s.getPhiIsaHoanTraTruyThu());
					item.getItemProperty("vatPhiIsaHoanTra").setValue(s.getVatPhiIsaHoanTraTruyThu());
					item.getItemProperty("tongPhiVatTruyThu").setValue(s.getTongPhiVatHoanTraTruyThu());
					item.getItemProperty("tongHoanTra").setValue(s.getTongHoanTraTruyThu());
					item.getItemProperty("casa").setValue(s.getCasa());
					item.getItemProperty("trace").setValue(s.getTrace());
					item.getItemProperty("adv").setValue(s.getNgayAdv());
				} else {
					item.getItemProperty("loaiTienNguyenTe").setValue(s.getLoaiTienNguyenTeGd());
					item.getItemProperty("phiIsa").setValue(s.getPhiIsaGd());
					item.getItemProperty("vatPhiIsa").setValue(s.getVatPhiIsaGd());
					item.getItemProperty("phiRtm").setValue(s.getPhiRtmGd());
					item.getItemProperty("vatPhiRtm").setValue(s.getVatPhiRtmGd());
					item.getItemProperty("loc").setValue(s.getLoc());
				}
			});
			
			flag = true;
		} 
		
		if(flag == true)
			return true;
		else 
			return false;
	}
	
	private String formatNumberColor(BigDecimal number) {
		if (number.compareTo(BigDecimal.ZERO) < 0) {
			return "<span style=\"padding:7px 0px; background-color: #FFFF00\">" + number + "</span>";

		} else
			return String.valueOf(number);
	}
	
	

}
