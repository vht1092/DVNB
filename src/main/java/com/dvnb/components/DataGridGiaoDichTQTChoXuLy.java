package com.dvnb.components;

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
		container.addContainerProperty("stt", String.class, "");
		container.addContainerProperty("soThe", String.class, "");
		container.addContainerProperty("maGd", String.class, "");
		container.addContainerProperty("ngayGd", String.class, "");
		container.addContainerProperty("ngayFileIncoming", String.class, "");
		container.addContainerProperty("stGd", String.class, "");
		container.addContainerProperty("stTqt", String.class, "");
		container.addContainerProperty("stQdVnd", String.class, "");
		container.addContainerProperty("ltgd", String.class, "");
		container.addContainerProperty("lttqt", String.class, "");
		container.addContainerProperty("interchange", String.class, "");
		container.addContainerProperty("maCapPhep", String.class, "");
		container.addContainerProperty("dvcnt", String.class, "");
		container.addContainerProperty("stTrichNoKhGd", String.class, "");
		container.addContainerProperty("stgdChenhLechDoTyGia", String.class, "");
		container.addContainerProperty("stgdNguyenTeGd", String.class, "");
		container.addContainerProperty("stgdNguyenTeChenhLech", String.class, "");
		container.addContainerProperty("phiXuLyGd", String.class, "");
		container.addContainerProperty("reversalInd", String.class, "");
		container.addContainerProperty("issuerCharge", String.class, "");
		container.addContainerProperty("statusCW", String.class, "");
		container.addContainerProperty("soTienGdHoanTra", String.class, "");
		container.addContainerProperty("phiIsaHoanTra", String.class, "");
		container.addContainerProperty("vatPhiIsaHoanTra", String.class, "");
		container.addContainerProperty("tongPhiVatTruyThu", String.class, "");
		container.addContainerProperty("tongHoanTra", String.class, "");
		container.addContainerProperty("tenChuThe", String.class, "");
		container.addContainerProperty("casa", String.class, "");
		container.addContainerProperty("dvpht", String.class, "");
		container.addContainerProperty("trace", String.class, "");
		container.addContainerProperty("adv", String.class, "");
		
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
		
		addComponentAsFirst(lbNoDataFound);
		addComponentAsFirst(grid);
	}

	public void initGrid(final String getColumn) {
		
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
				item.getItemProperty("stt").setValue(String.valueOf(stt));
				item.getItemProperty("soThe").setValue(s.getSoThe());
				item.getItemProperty("maGd").setValue(s.getMaGd());
				item.getItemProperty("ngayGd").setValue(s.getNgayGd());
				item.getItemProperty("ngayFileIncoming").setValue(s.getNgayFileIncoming());
				item.getItemProperty("stGd").setValue(String.valueOf(s.getStGd()));
				item.getItemProperty("stTqt").setValue(String.valueOf(s.getStTqt()));
				item.getItemProperty("stQdVnd").setValue(String.valueOf(s.getStQdVnd()));
				item.getItemProperty("ltgd").setValue(s.getLtgd());
				item.getItemProperty("lttqt").setValue(s.getLttqt());
				item.getItemProperty("interchange").setValue(String.valueOf(s.getInterchange()));
				item.getItemProperty("maCapPhep").setValue(s.getMaCapPhep());
				item.getItemProperty("dvcnt").setValue(s.getDvcnt());
				item.getItemProperty("stTrichNoKhGd").setValue(String.valueOf(s.getStTrichNoKhGd()));
				item.getItemProperty("stgdChenhLechDoTyGia").setValue(String.valueOf(s.getStgdChenhLechDoTyGia()));
				item.getItemProperty("stgdNguyenTeGd").setValue(String.valueOf(s.getStgdNguyenTeGd()));
				item.getItemProperty("stgdNguyenTeChenhLech").setValue(String.valueOf(s.getStgdNguyenTeChenhLech()));
				item.getItemProperty("phiXuLyGd").setValue(String.valueOf(s.getPhiXuLyGd()));
				item.getItemProperty("reversalInd").setValue(s.getReversalInd());
				item.getItemProperty("issuerCharge").setValue(String.valueOf(s.getIssuerCharge()));
				item.getItemProperty("statusCW").setValue(s.getStatusCw());
				item.getItemProperty("soTienGdHoanTra").setValue(String.valueOf(s.getSoTienGdHoanTraTruyThu()));
				item.getItemProperty("phiIsaHoanTra").setValue(String.valueOf(s.getPhiIsaHoanTraTruyThu()));
				item.getItemProperty("vatPhiIsaHoanTra").setValue(String.valueOf(s.getVatPhiIsaHoanTraTruyThu()));
				item.getItemProperty("tongPhiVatTruyThu").setValue(String.valueOf(s.getTongPhiVatHoanTraTruyThu()));
				item.getItemProperty("tongHoanTra").setValue(String.valueOf(s.getTongHoanTraTruyThu()));
				item.getItemProperty("tenChuThe").setValue(s.getTenChuThe());
				item.getItemProperty("casa").setValue(s.getCasa());
				item.getItemProperty("dvpht").setValue(s.getDvpht());
				item.getItemProperty("trace").setValue(s.getTrace());
				item.getItemProperty("adv").setValue(s.getNgayAdv());
				
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
